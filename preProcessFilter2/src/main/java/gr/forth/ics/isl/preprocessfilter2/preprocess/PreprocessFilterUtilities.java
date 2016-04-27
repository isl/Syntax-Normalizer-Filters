/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.preprocessfilter2.preprocess;

import gr.forth.ics.isl.preprocessfilter2.exceptions.PreprocessFilterException;
import gr.forth.ics.isl.preprocessfilter2.properties.PropertyReader;
import gr.forth.ics.isl.preprocessfilter2.properties.Resources;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author minadakn
 */
public class PreprocessFilterUtilities {

    /* The following fuction creates the file that contains the ordered list of the values of the selected tag
     that are to be changed. */
    public boolean createNewValuesFile(String inputFilePath, String outputFilePath, String parentNodeName) throws PreprocessFilterException {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(inputFilePath));

            TreeSet tree = new TreeSet();

            NodeList parentNodeList = document.getElementsByTagName(parentNodeName);

            //The text contexts of the given parentNodes are stored in tree
            for (int i = 0; i < parentNodeList.getLength(); i++) {
                tree.add(parentNodeList.item(i).getTextContent());
            }

            String finalString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + "\n" + "<root>" + "\n";

            Iterator treeIterator = tree.iterator();

            /* The parent nodes and the text contents are being included in the newValuesFile along with
             the newValue element that will contain the new text content that will replace the existing ones */
            while (treeIterator.hasNext()) {

                finalString += "<" + parentNodeName + ">";

                finalString += treeIterator.next();

                finalString += "</" + parentNodeName + ">" + "<newValue></newValue>" + "\n";
            }

            finalString += "</root>";

            FileWriter writer = new FileWriter(outputFilePath);
            writer.write(finalString);

            writer.close();

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }


    /*This function returns a hashmap that contains the parentNode related with the new text context that will
     replace the existing one in the input file */
    public HashMap<String, String> createValuesHashMap(String newValuesFilePath, String parentNodeName,
            String newValueTagName) throws PreprocessFilterException {
        HashMap<String, String> tagHash = new HashMap<String, String>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(newValuesFilePath));

            NodeList parentNodeList = document.getElementsByTagName(parentNodeName);
            NodeList newValuesList = document.getElementsByTagName(newValueTagName);

            int size = newValuesList.getLength();

            for (int i = 0; i < size; i++) {
                tagHash.put(parentNodeList.item(i).getTextContent(), newValuesList.item(i).getTextContent());
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tagHash;
    }

    // createOutputFile is responsible for replacing the old text contexts with the new ones
    public boolean createOutputFile(String inputFilePath, String outputFilePath, String parentNodeName,
            String newParentNodeName, String newValueTagName, String oldValueTagName, String sameValueNodeName,
            String newValuesFilePath) throws PreprocessFilterException {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(inputFilePath));

            NodeList parentNodeList = document.getElementsByTagName(parentNodeName);

            HashMap<String, String> tagHash = createValuesHashMap(newValuesFilePath, parentNodeName, newValueTagName);
            for (int i = 0; i < parentNodeList.getLength(); i++) {

                if (tagHash.get(parentNodeList.item(i).getTextContent()) == "") {
                    continue;
                }
                Node oldValueNode = document.createElement(oldValueTagName);
                Node newValueNode = document.createElement(newValueTagName);
                oldValueNode.setTextContent(parentNodeList.item(i).getTextContent());
                newValueNode.setTextContent(tagHash.get(parentNodeList.item(i).getTextContent()));

                parentNodeList.item(i).setTextContent("");

                parentNodeList.item(i).appendChild(newValueNode);
                parentNodeList.item(i).appendChild(oldValueNode);
            }
            for (int i = 0; i < parentNodeList.getLength(); i++) {
                if (tagHash.get(parentNodeList.item(i).getTextContent()) == "") {
                    document.renameNode(parentNodeList.item(i), parentNodeList.item(i).getNamespaceURI(), sameValueNodeName);
                } else {
                    document.renameNode(parentNodeList.item(i), parentNodeList.item(i).getNamespaceURI(), newParentNodeName);
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(outputFilePath));
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}

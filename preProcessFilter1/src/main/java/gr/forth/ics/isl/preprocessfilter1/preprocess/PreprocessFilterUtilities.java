/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.preprocessfilter1.preprocess;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import gr.forth.ics.isl.preprocessfilter1.exceptions.PreprocessFilterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author minadakn
 */
public class PreprocessFilterUtilities {

    public boolean createOutputFile(String inputFilePath, String outputFilePath, String parentNodeName,
            String newParentNodeName, String intermediateNode, String intermediateNodes, String delimeter)
            throws PreprocessFilterException {

        try {
            //The xml file is loaded using DOM
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(inputFilePath));     
            
            //In parentNodeList, all the nodes with the given parentNodeName are stored.
            NodeList parentNodeList = document.getElementsByTagName(parentNodeName);

            /*The intermediateNodes that are provided as input are splited based on the delimeter
             and are stored into the intNodes list*/
            
            ArrayList<String> intNodes = new ArrayList();

            String[] intNode = intermediateNodes.split(delimeter);

            for (String intNode1 : intNode) {
                intNodes.add(intNode1.trim());
            }

            /* All the preprocessing actually takes place in the code below, all the nodes that 
            are children of the nodes that have the given parentNodeName are being rearranged 
            according to the intermediateNodes contents */
            
            for (int item = 0; item < parentNodeList.getLength(); item++) {

                HashMap<String, ArrayList<String>> values = new HashMap<String, ArrayList<String>>();

                ArrayList<String> firstValuesList = new ArrayList();

                //The text content of the first element that is to be rearranged is stored in the firstTagNodes
                NodeList firstTagNodes = parentNodeList.item(item).getChildNodes();

                for (int i = 0; i < firstTagNodes.getLength(); i++) {
                    if (firstTagNodes.item(i).getNodeName().equals(intNodes.get(0))) {
                        firstValuesList.add(firstTagNodes.item(i).getTextContent());
                    }
                }
                
                for (int i = 0; i < intNodes.size(); i++) {
                    ArrayList<String> valuesList = new ArrayList();

                    //The text contents of the elements that have the first tag's name are stored in the valuesList
                    for (int j = 0; j < firstTagNodes.getLength(); j++) {
                        if (firstTagNodes.item(j).getNodeName().equals(intNodes.get(i))) {
                            if (valuesList.size() > firstValuesList.size()) {
                                System.out.println("First tag's set cannot be smaller than any other tag set");
                                break;
                            }
                            valuesList.add(firstTagNodes.item(j).getTextContent());
                        }
                    }
 
               
                    //Since the text context of the element to be arranged have been stored the corresponding node is being removed
                    for (int j = 0; j < firstTagNodes.getLength(); j++) {
                        if (firstTagNodes.item(j).getNodeName().equals(intNodes.get(i))) {
                            parentNodeList.item(item).removeChild(firstTagNodes.item(j));
                        }

                    }
  
                    //The values has map stores the intermediate nodes and the related valuesList of each node
                    values.put(intNodes.get(i), valuesList);
                }


                //The new nodes are created as children of the related parent noded in the correct order
                for (int j = 0; j < values.get(intNodes.get(0)).size(); j++) {
                    Node parentNode = document.createElement(intermediateNode);
                    parentNodeList.item(item).appendChild(parentNode);

                      
                    for (int k = 0; k < intNodes.size(); k++) {
                        if (values.get(intNodes.get(k)).size() >= j + 1) {
                            try {
                                Node childNode = document.createElement(intNodes.get(k));
                                childNode.setTextContent(values.get(intNodes.get(k)).get(j));
                                parentNode.appendChild(childNode);
                            } catch (IndexOutOfBoundsException e) {
                            }
                        }

                    }
                }
            }
            
            //The new file with the re-arranged nodes is being created
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
           
            File outputFile = new File(outputFilePath);
 
            File tempFile = new File(outputFilePath.replace(".xml","temp.xml"));
            
            StreamResult streamResult = new StreamResult(tempFile);
            //StreamResult streamResult = new StreamResult(new File(outputFilePath));
            transformer.transform(domSource, streamResult);
            
            BufferedReader reader = new BufferedReader(new FileReader(tempFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            
            String currentLine;
        
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.trim();
 
                if (!currentLine .equals(""))
                {
                    writer.write(currentLine);
                    writer.newLine();          
                }
             }
            reader.close();
            writer.close();
            
            tempFile.delete();

        } catch (TransformerException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
}

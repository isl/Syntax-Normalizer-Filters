package gr.forth.ics.isl.preprocessfilterNEW.preprocess;

import gr.forth.ics.isl.preprocessfilterNEW.exceptions.PreprocessFilterException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


public class PreprocessFilterUtilities {
    
    /*This function takes as input an xml file and restructures it based on the input parameters. For example
    it changes nodeNames, splits the elements values and creates intermediate nodes*/
    public boolean createOutputFile(String inputFilePath, String outputFilePath, String parentNodeName,
            String newParentNodeName, String intermediateNode, String intermediateNodes, String delimeter) 
            throws PreprocessFilterException {

        try {
            //The xml file is loaded using DOM
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            
            Document document = documentBuilder.parse(new File(inputFilePath));
            
            //In parentNodeList, all the nodes with the parentNodeName are stored.
            NodeList parentNodeList = document.getElementsByTagName(parentNodeName);
            
            ArrayList<String> intNodes = new ArrayList();
            
            /*The intermediateNodes that are provided as input are splited based on the comma
            and are stored into the intNodes list*/
            String[] intNode = intermediateNodes.split(",");
            
            for (String intNode1 : intNode) {
                intNodes.add(intNode1.trim());
            }
   
            /*This flag is used to indicate if the intemediate node name  is the same with the old 
            parent node name. If this is true a temporary name is being used to resolve the conflicts*/
            Boolean changeName = false;
            
            if(parentNodeName.equals(intermediateNode))
            {
                intermediateNode += "TEMPNAME";
                changeName = true;
            }
            
            
            /*The parent node is replaced with the new parent node. An intermediate node is created 
            for every parent node and the intermediate nodes are assigned to this node as children. 
            The old parent node's  values are splitted based on the delimeter and are assigned to the 
            related intermediate nodes.*/
            for (int item = 0; item < parentNodeList.getLength(); item++) {
                
                String[] values = parentNodeList.item(item).getTextContent().split(delimeter);
                
                int repeats = values.length/intNodes.size();
                
                parentNodeList.item(item).setTextContent("");
                
                for (int value = 0; value < repeats; value++) {
                    
                    Node parentNode = document.createElement(intermediateNode);
                    parentNodeList.item(item).appendChild(parentNode);
                    
                    for (int i = 0; i < intNodes.size(); i++) {
                        
                        Node childNode = document.createElement(intNodes.get(i));
                        
                        parentNode.appendChild(childNode);
                        childNode.setTextContent(values[i + value * intNodes.size()].trim());
                    }
                    
                }

            }
            
            if (newParentNodeName != parentNodeName)
                for (int item = 0; item < parentNodeList.getLength(); item++) {
                    document.renameNode(parentNodeList.item(item), parentNodeList.item(item).getNamespaceURI(), newParentNodeName);
                }
            
            if(changeName)
            {
                NodeList intNodeList = document.getElementsByTagName(intermediateNode);
                for (int item = 0; item < intNodeList.getLength(); item++) {
                    document.renameNode(intNodeList.item(item), intNodeList.item(item).getNamespaceURI(), intermediateNode.replace("TEMPNAME",""));
                }
            }

            //The new structure is the ouputFile
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(outputFilePath));
            transformer.transform(domSource, streamResult);
            

        } catch (ParserConfigurationException ex) {
            
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
            throw new PreprocessFilterException("Parser Configuration Exception:",ex);
        }
        catch (TransformerException ex) {
            
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
            throw new PreprocessFilterException("Transformer Exception:",ex);
        }
        catch (SAXException ex) {
            
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
            throw new PreprocessFilterException("PreProcess Filter Exception:",ex);
        }
        catch (IOException ex) {
            
            Logger.getLogger(PreprocessFilterUtilities.class.getName()).log(Level.SEVERE, null, ex);
            throw new PreprocessFilterException("IOException Exception:",ex);
        }
        return true;
    }
    
}

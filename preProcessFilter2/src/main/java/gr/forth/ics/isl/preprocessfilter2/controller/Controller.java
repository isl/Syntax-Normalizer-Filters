/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.preprocessfilter2.controller;

import gr.forth.ics.isl.preprocessfilter2.exceptions.PreprocessFilterException;
import gr.forth.ics.isl.preprocessfilter2.preprocess.PreprocessFilterUtilities;
import gr.forth.ics.isl.preprocessfilter2.properties.PropertyReader;
import gr.forth.ics.isl.preprocessfilter2.properties.Resources;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.createNewValuesFile;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.inputFilePath;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.newParentNode;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.newValueTag;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.newValuesFilePath;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.oldValueTag;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.outputFilePath;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.parentNode;
import static gr.forth.ics.isl.preprocessfilter2.properties.Resources.sameValueTag;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.xml.sax.SAXException;

/**
 *
 * @author minadakn
 */
public class Controller {

    public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, PreprocessFilterException, TransformerException, ParseException {
        PropertyReader prop = new PropertyReader();

        //The following block of code is executed if there are arguments from the command line
        if (args.length > 0) {

            try {
                Options options = new Options();
                CommandLineParser PARSER = new PosixParser();
                Option inputFile = new Option(
                        "inputFile", true,
                        "input xml file"
                );
                Option outputFile = new Option(
                        "outputFile", true,
                        "output xml file"
                );
                Option parentNode = new Option(
                        "parentNode", true,
                        "output xml file"
                );
                Option newValuesFile = new Option(
                        "newValuesFile", true,
                        "new values xml file"
                );
                Option newParentNode = new Option(
                        "newParentNode", true,
                        "new parent node"
                );
                Option newValueTag = new Option(
                        "newValueTag", true,
                        "new value tag"
                );
                Option oldValueTag = new Option(
                        "oldValueTag", true,
                        "old value tag"
                );
                Option sameValueTag = new Option(
                        "sameValueTag", true,
                        "same value tag"
                );
                Option createNewValues = new Option(
                        "createNewValues", true,
                        "create new values option"
                );
                options.addOption(inputFile)
                        .addOption(outputFile)
                        .addOption(parentNode)
                        .addOption(newValuesFile)
                        .addOption(newParentNode)
                        .addOption(newValueTag)
                        .addOption(oldValueTag)
                        .addOption(sameValueTag)
                        .addOption(createNewValues);
                CommandLine cli = PARSER.parse(options, args);
                String inputFileArg = cli.getOptionValue("inputFile");
                String outputFileArg = cli.getOptionValue("outputFile");
                String parentNodeArg = cli.getOptionValue("parentNode");
                String newValuesFileArg = cli.getOptionValue("newValuesFile");
                String newParentNodeArg = cli.getOptionValue("newParentNode");
                String newValueTagArg = cli.getOptionValue("newValueTag");
                String oldValueTagArg = cli.getOptionValue("oldValueTag");
                String sameValueTagArg = cli.getOptionValue("sameValueTag");
                String createNewValuesArg = cli.getOptionValue("createNewValues");
                PreprocessFilterUtilities process = new PreprocessFilterUtilities();
                if (createNewValuesArg.equals("yes")) {
                    if (process.createNewValuesFile(inputFileArg, newValuesFileArg, parentNodeArg)) {
                        System.out.println("Succesfull PreProcessing!!!");
                    }
                } else {
                    if (process.createOutputFile(inputFileArg, outputFileArg, parentNodeArg,
                            newParentNodeArg, newValueTagArg, oldValueTagArg, sameValueTagArg,
                            newValuesFileArg)) {
                        System.out.println("Succesfull PreProcessing!!!");
                    }
                }
            } catch (PreprocessFilterException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                throw new PreprocessFilterException("PreProcess Filter Exception:", ex);
            }

        } //If there are no command line arguments then the .config file is being used.
        else {

            try {
                String inputFilePathProp = prop.getProperty(inputFilePath);
                String outputFilePathProp = prop.getProperty(outputFilePath);
                String parentNodeProp = prop.getProperty(parentNode);

                String newValuesFilePathProp = prop.getProperty(newValuesFilePath);
                String newParentNodeProp = prop.getProperty(newParentNode);
                String newValueTagProp = prop.getProperty(newValueTag);
                String oldValueTagProp = prop.getProperty(oldValueTag);
                String sameValueTagProp = prop.getProperty(sameValueTag);
                String createNewValuesFileProp = prop.getProperty(createNewValuesFile);

                PreprocessFilterUtilities process = new PreprocessFilterUtilities();

                //The filter's code is executed with the .config file's resources as parameters
                if (createNewValuesFileProp.equals("yes")) {
                    process.createNewValuesFile(inputFilePathProp, newValuesFilePathProp, parentNodeProp);
                } else {
                    process.createOutputFile(inputFilePathProp, outputFilePathProp, parentNodeProp,
                            newParentNodeProp, newValueTagProp, oldValueTagProp, sameValueTagProp,
                            newValuesFilePathProp);
                }
            } catch (PreprocessFilterException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                throw new PreprocessFilterException("PreProcess Filter Exception:", ex);
            }
        }

    }

}

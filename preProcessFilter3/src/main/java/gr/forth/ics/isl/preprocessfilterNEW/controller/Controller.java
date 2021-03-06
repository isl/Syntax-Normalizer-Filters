package gr.forth.ics.isl.preprocessfilterNEW.controller;

import gr.forth.ics.isl.preprocessfilterNEW.exceptions.PreprocessFilterException;
import gr.forth.ics.isl.preprocessfilterNEW.preprocess.PreprocessFilterUtilities;
import gr.forth.ics.isl.preprocessfilterNEW.properties.PropertyReader;
import static gr.forth.ics.isl.preprocessfilterNEW.properties.Resources.delimeter;
import static gr.forth.ics.isl.preprocessfilterNEW.properties.Resources.inputFilePath;
import static gr.forth.ics.isl.preprocessfilterNEW.properties.Resources.intermediateNode;
import static gr.forth.ics.isl.preprocessfilterNEW.properties.Resources.intermediateNodes;
import static gr.forth.ics.isl.preprocessfilterNEW.properties.Resources.newParentNode;
import static gr.forth.ics.isl.preprocessfilterNEW.properties.Resources.outputFilePath;
import static gr.forth.ics.isl.preprocessfilterNEW.properties.Resources.parentNode;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Controller {

    public static void main(String[] args) throws PreprocessFilterException {
        PropertyReader prop = new PropertyReader();

        //The following block of code is executed if there are arguments from the command line
        if (args.length > 0) {

            try {

                //The values of the arguments are handled as Option instances
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
                Option delimeter = new Option(
                        "delimeter", true,
                        "output xml file"
                );
                Option newParentNode = new Option(
                        "newParentNode", true,
                        "output xml file"
                );
                Option intermediateNodes = new Option(
                        "intermediateNodes", true,
                        "output xml file"
                );
                Option intermediateNode = new Option(
                        "intermediateNode", true,
                        "output xml file"
                );
                
                options.addOption(inputFile)
                       .addOption(outputFile)
                       .addOption(parentNode)
                       .addOption(newParentNode)
                       .addOption(intermediateNode)
                       .addOption(intermediateNodes)
                       .addOption(delimeter);

                CommandLine cli = PARSER.parse(options, args);

                String inputFileArg = cli.getOptionValue("inputFile");
                String outputFileArg = cli.getOptionValue("outputFile");
                String parentNodeArg = cli.getOptionValue("parentNode");
                String newParentNodeArg = cli.getOptionValue("newParentNode");
                String intermediateNodeArg = cli.getOptionValue("intermediateNode");
                String intermediateNodesArg = cli.getOptionValue("intermediateNodes");
                String delimeterArg = cli.getOptionValue("delimeter");
                
                PreprocessFilterUtilities process = new PreprocessFilterUtilities();

                //System.out.println("INPUT:"+inputFileArg);
                //System.out.println("OUTPUT:"+outputFileArg);
                //System.out.println("PARENT NODE:"+parentNodeArg);
                //System.out.println("NEW PARENT NODE:"+newParentNodeArg);
                //System.out.println("INTERMEDIATE NODE:"+intermediateNodeArg);
                //System.out.println("INTERMEDIATE NODES:"+intermediateNodesArg);
                //System.out.println("DELIMETER:"+delimeterArg);
               
                //The filter's code is executed with the command line arguments as parameters
                if (process.createOutputFile(inputFileArg, outputFileArg, parentNodeArg,
                    newParentNodeArg, intermediateNodeArg, intermediateNodesArg, delimeterArg)) {
                    System.out.println("Succesfull PreProcessing!!!");
                }
            } catch (ParseException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                throw new PreprocessFilterException("ParseException:", ex);

            } catch (PreprocessFilterException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                throw new PreprocessFilterException("PreProcess Filter Exception:", ex);
            }

        }
        
        //If there are no command line arguments then the .config file is being used.
        else {

            try {
                
                String inputFilePathProp = prop.getProperty(inputFilePath);
                String outputFilePathProp = prop.getProperty(outputFilePath);
                String parentNodeProp = prop.getProperty(parentNode);
                String delimeterProp = prop.getProperty(delimeter);

                String newParentNodeProp = prop.getProperty(newParentNode);
                String intermediateNodesProp = prop.getProperty(intermediateNodes);
                String intermediateNodeProp = prop.getProperty(intermediateNode);

                PreprocessFilterUtilities process = new PreprocessFilterUtilities();

                //The filter's code is executed with the .config file's resources as parameters
                if (process.createOutputFile(inputFilePathProp, outputFilePathProp, parentNodeProp,
                        newParentNodeProp, intermediateNodeProp, intermediateNodesProp, delimeterProp)) {
                    System.out.println("Succesfull PreProcessing!!!");
                }
            } catch (PreprocessFilterException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                throw new PreprocessFilterException("PreProcess Filter Exception:", ex);
            }

        }
    }
}

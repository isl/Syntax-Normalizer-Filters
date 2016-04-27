/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.preprocessfilterNEW.exceptions;
/**
 * @author Yannis Marketakis (yannismarketakis 'at' gmail 'dot' com)
 */
public class PropertyReaderException extends Exception{
    public PropertyReaderException(){
        super("An error occured while reading properties file!");
    }
    
    public PropertyReaderException(String msg){
        super(msg);
    }
    
    public PropertyReaderException(String msg, Throwable throwable) {
            super(msg, throwable);
        }
}
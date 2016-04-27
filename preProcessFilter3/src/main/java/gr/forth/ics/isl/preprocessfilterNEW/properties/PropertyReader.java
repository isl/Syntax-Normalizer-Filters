package gr.forth.ics.isl.preprocessfilterNEW.properties;

import gr.forth.ics.isl.preprocessfilterNEW.exceptions.PreprocessFilterException;
import gr.forth.ics.isl.preprocessfilterNEW.exceptions.PropertyReaderException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PropertyReader{
   
    private Properties prop;
    
    public PropertyReader() {
        
        
            this.prop=new Properties();
            
            try{
            prop.load(new FileReader("./"+Resources.propertyFilename));
            } 
            catch (IOException ex) {
            
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("IOException: "+ex.getMessage()+"\n"+"Arguments Expected");
           
        }

           
    }
    
    public String getProperty(String property){
        String retValue=this.prop.getProperty(property);

        return retValue;
    }
}

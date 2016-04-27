package gr.forth.ics.isl.preprocessfilter2.properties;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class PropertyReader{
   
    private Properties prop;
    
    public PropertyReader() throws IOException{
 
            this.prop=new Properties();
            prop.load(new FileReader("./"+Resources.propertyFilename));
        
    }
    
    public String getProperty(String property){
        String retValue=this.prop.getProperty(property);
            return retValue;   
    }
}

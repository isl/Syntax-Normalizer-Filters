/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.forth.ics.isl.preprocessfilter2.preprocess;

import gr.forth.ics.isl.preprocessfilterNEW.preprocess.PreprocessFilterUtilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author minadakn
 */
public class PreprocessFilterNEWTest {
   
    /**
     * Test of tagList method, of class PreprocessFilter1.
     */
    @Test
    public void testPreprocessFilterNEW() throws Exception {

        PreprocessFilterUtilities process = new PreprocessFilterUtilities();
        process.createOutputFile("./src/test/resources/testPreProcessFilterNEW/input.xml",
                "./src/test/resources/testPreProcessFilterNEW/output.xml",
                "Dimension","Dimensions","Dimension","DimensionType,DimensionValue,DimensionUnit",",");
          
        File f1 = new File("./src/test/resources/testPreProcessFilterNEW/output.xml");
        File f2 = new File("./src/test/resources/testPreProcessFilterNEW/expected.xml");

        FileReader fR1 = new FileReader(f1);
        FileReader fR2 = new FileReader(f2);

        BufferedReader reader1 = new BufferedReader(fR1);
        BufferedReader reader2 = new BufferedReader(fR2);

        String line1 = null;
        String line2 = null;
        int flag = 1;
        while ((flag == 1) && ((line1 = reader1.readLine()) != null)
                && ((line2 = reader2.readLine()) != null)) {
            if (!line1.equalsIgnoreCase(line2))
                flag = 0;
        }
        reader1.close();
        reader2.close();
    
        assertTrue(flag==1);
    }

}

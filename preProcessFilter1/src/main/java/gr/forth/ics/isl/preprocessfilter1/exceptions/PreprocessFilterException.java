/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.forth.ics.isl.preprocessfilter1.exceptions;

import java.sql.SQLException;

public class PreprocessFilterException extends Exception {

    public PreprocessFilterException() {
        super("An error occured during Preprocessing!");
    }

    public PreprocessFilterException(String msg) {
        super(msg);
    }

    public PreprocessFilterException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}

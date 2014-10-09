/**
 * 
 */
package de.fhb.uebung1.exceptions;

/**
 * @author Max Gregor
 *
 */
public class InputException extends Exception{
    private static final long serialVersionUID = 2105976446439674168L;
    public static final int PASSWORD_LENGTH = 0;
    public static final int NICKNAME_LENGTH = 1;
    
    private final int type;
   
    
    public InputException(int type) {
	super();
	this.type = type;
    }

    public int getType() {
        return type;
    }
    
     

    
}

package de.mass.uebung1.exceptions;

public class EntryAllreadyExsistException extends Exception {
    private static final long serialVersionUID = 4812939732144101704L;

    public static final int  NICKNAME_ALREADY_EXISTS= 0;
    public static final int  EMAIL_ALREADY_EXISTS= 1;
    
    private final int type;
    
    public EntryAllreadyExsistException(final int type){
	this.type = type;
    }

    public int getType() {
        return type;
    }
    
    
    
//    public EntryAllreadyExsistException(String message) {
//	super(message);
//    }
//    public EntryAllreadyExsistException(){
//	super();
//    }
}

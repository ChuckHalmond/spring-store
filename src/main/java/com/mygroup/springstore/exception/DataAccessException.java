package com.mygroup.springstore.exception;

public class DataAccessException extends Exception {
	
	private static final long serialVersionUID = -2359693529369282413L;
	
	private DAESource source;
    
    public DataAccessException(String message, DAESource source) {
        super(message);
        this.source = source;
    }
        
    public DataAccessException(String message, DAESource source, Exception e) {
        super(message, e);
        this.source = source;
    }
    
    public DAESource getSource() {
        return source;
    }
    
    public enum DAESource {
        PK_EXISTING,
        PK_MISSING,
        UNIQUE_ATTR_DUP,
        ATTR_MISSING
    }
}

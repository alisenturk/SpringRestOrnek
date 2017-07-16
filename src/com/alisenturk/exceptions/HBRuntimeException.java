package com.alisenturk.exceptions;

public class HBRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 3324480045339476443L;

	public HBRuntimeException(){
        super();
    }

    public HBRuntimeException(String message){
        super(message);
    }
    public HBRuntimeException(Exception e){
        super(e);
    }
}

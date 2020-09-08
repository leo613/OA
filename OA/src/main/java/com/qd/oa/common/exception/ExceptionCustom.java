package com.qd.oa.common.exception;

public class ExceptionCustom extends RuntimeException {
    private String message;
    public ExceptionCustom(String message){
        super(message);
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

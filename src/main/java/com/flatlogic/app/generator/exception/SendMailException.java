package com.flatlogic.app.generator.exception;

public class SendMailException extends RuntimeException {

    public SendMailException() {
        super();
    }

    public SendMailException(String message) {
        super(message);
    }

    public SendMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendMailException(Throwable cause) {
        super(cause);
    }

}

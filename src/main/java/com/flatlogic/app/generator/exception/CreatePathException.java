package com.flatlogic.app.generator.exception;

public class CreatePathException extends RuntimeException {

    public CreatePathException() {
        super();
    }

    public CreatePathException(String message) {
        super(message);
    }

    public CreatePathException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreatePathException(Throwable cause) {
        super(cause);
    }

}
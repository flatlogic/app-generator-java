package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.exception.NoSuchEntityException;
import com.flatlogic.app.generator.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * GlobalControllerExceptionHandler controllerAdvice.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * NoSuchEntityException handler.
     *
     * @param e NoSuchEntityException
     * @return Error message
     */
    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<String> handleNoSuchEntityException(NoSuchEntityException e) {
        LOGGER.error("NoSuchEntityException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * ValidationException handler.
     *
     * @param e ValidationException
     * @return Error message
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        LOGGER.error("ValidationException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler.
     *
     * @param e Exception
     * @return Error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOGGER.error("Exception handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Throwable handler.
     *
     * @param e Throwable
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(Throwable.class)
    public void handleThrowable(Throwable e) {
        LOGGER.error("Throwable handler.", e);
    }

}

package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.exception.NoSuchEntityException;
import com.flatlogic.app.generator.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Entity not found.")
    @ExceptionHandler(NoSuchEntityException.class)
    public void handleNoSuchEntityException(NoSuchEntityException e) {
        LOGGER.error("NoSuchEntityException handler.", e);
    }

    /**
     * ValidationException handler.
     *
     * @param e ValidationException
     */
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Validation is not success.")
    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(ValidationException e) {
        LOGGER.error("ValidationException handler.", e);
    }

    /**
     * Exception handler.
     *
     * @param e Exception
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        LOGGER.error("Exception handler.", e);
    }

}

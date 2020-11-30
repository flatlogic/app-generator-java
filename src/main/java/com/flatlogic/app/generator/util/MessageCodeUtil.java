package com.flatlogic.app.generator.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MessageCodeUtil {

    private static final Logger LOGGER = Logger.getLogger(MessageCodeUtil.class.getName());

    private Locale locale = Locale.getDefault();

    @Autowired
    private MessageSource messageSource;

    public String getFullErrorMessageByBundleCode(String bundleCode, Object[] objects) {
        try {
            return messageSource.getMessage(bundleCode, objects, locale);
        } catch (NoSuchMessageException e) {
            LOGGER.log(Level.WARNING, "Unknown bundle code {}", bundleCode);
        }
        return messageSource.getMessage(Constants.ERROR_MESSAGE, objects, locale);
    }

    public String getFullErrorMessageByBundleCode(String bundleCode) {
        return getFullErrorMessageByBundleCode(bundleCode, null);
    }

}

package com.flatlogic.app.generator.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableScheduling
@EnableAspectJAutoProxy
public class ApplicationConfig implements WebMvcConfigurer {

    /**
     * Create DefaultConversionService bean.
     *
     * @param converters List of Converter
     * @return DefaultConversionService
     */
    @Bean
    @Autowired
    public DefaultConversionService defaultConversionService(List<Converter<?, ?>> converters) {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        converters.forEach(defaultConversionService::addConverter);
        return defaultConversionService;
    }

    /**
     * Create UserCache bean.
     *
     * @return UserCache
     */
    @Bean
    public UserCache userCache() {
        return new SpringCacheBasedUserCache(new ConcurrentMapCache(UserCache.class.getName()));
    }

    /**
     * Create ResourceBundleMessageSource bean.
     *
     * @return ResourceBundleMessageSource
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages/message");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    /**
     * Set time zone for ObjectMapper.
     *
     * @param objectMapper ObjectMapper
     */
    @Autowired
    public void setTimeZone(final ObjectMapper objectMapper) {
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

}

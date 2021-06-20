/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.autoconfigure.EnableAutoConfiguration
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.ComponentScan
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
 *  org.springframework.web.servlet.view.InternalResourceViewResolver
 */
package com.farm.server.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ComponentScan
@EnableAutoConfiguration
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}


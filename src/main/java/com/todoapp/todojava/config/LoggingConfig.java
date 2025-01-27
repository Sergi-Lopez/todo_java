package com.todoapp.todojava.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.Logger;

@Configuration
public class LoggingConfig {
	@Bean
	public Logger logger() {
		return (Logger) LoggerFactory.getLogger("com.todoapp.todojava");
	}
}

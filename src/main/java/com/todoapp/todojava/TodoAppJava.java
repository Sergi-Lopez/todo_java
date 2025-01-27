package com.todoapp.todojava;
import com.todoapp.todojava.config.SpringSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@ComponentScan(basePackages = "com.todoapp.todojava")
@Import(SpringSecurity.class)
public class TodoAppJava extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppJava.class, args);
	}

}
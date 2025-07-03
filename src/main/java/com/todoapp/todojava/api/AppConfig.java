package com.todoapp.todojava.api;

import com.todoapp.todojava.dao.TodoDao;
import com.todoapp.todojava.dao.UsuarioDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public UsuarioDao usuarioDao() {
		return new UsuarioDao();
	}

	@Bean
	public TodoDao todoDao() {
		return new TodoDao();
	}

}

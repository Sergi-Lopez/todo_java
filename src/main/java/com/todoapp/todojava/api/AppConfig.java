package com.todoapp.todojava.api;

import com.todoapp.todojava.dao.RolDao;
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
	public RolDao rolDao() {
		return new RolDao();
	}

}

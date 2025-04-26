package com.todoapp.todojava.controladores;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.todoapp.todojava.dao.UsuarioDao;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@ControllerAdvice
public class HtmlController {
	

	@Autowired
	private Logger logger;

	@Autowired
	private UsuarioDao usuarioDao;

	@GetMapping("/todo")
	public String index() {
		return "index";
	}

	@GetMapping("/todo/register")
	public String register() {
		return "register";
	}

	@GetMapping("/todo/login")
	public String login() {
		return "login";
	}

	@GetMapping("/todo/logout")
	public String logout() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = usuarioDao.findByField("email", authentication.getName()).get(0).getUser();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String formattedDateTime = now.format(formatter);
		logger.info("Usuario: " + username + " - Fecha/hora: " + formattedDateTime + " - Logout.");
		return "redirect:/todo/user/logout";
	}
}

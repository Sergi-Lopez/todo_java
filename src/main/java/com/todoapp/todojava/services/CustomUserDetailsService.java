package com.todoapp.todojava.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; // Import necesario para la lista vacía de authorities
import java.util.Collection; // Import necesario para Collection
import java.util.List;

import com.todoapp.todojava.clases.Usuario;
import com.todoapp.todojava.dao.UsuarioDao;
import org.slf4j.Logger; // Importar Logger de SLF4J
import org.slf4j.LoggerFactory; // Importar LoggerFactory de SLF4J
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority; // Import necesario
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioDao userRepository;

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	public CustomUserDetailsService(UsuarioDao userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Usuario> users = userRepository.findByField("user", username);

		if (users == null || users.isEmpty()) {
			logger.warn("Intento de login fallido para usuario no encontrado: {}", username);
			throw new UsernameNotFoundException("Usuario o contraseña inválidos.");
		}

		Usuario user = users.get(0);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String formattedDateTime = now.format(formatter);
		logger.info("Usuario: {} - Fecha/hora: {} - Login exitoso.", user.getUser(), formattedDateTime);

		return new org.springframework.security.core.userdetails.User(
				user.getUser(),
				user.getPassword(),
				new ArrayList<GrantedAuthority>()
		);
	}
}
package com.todoapp.todojava.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.todoapp.todojava.clases.Roles;
import com.todoapp.todojava.clases.Usuario;
import com.todoapp.todojava.dao.RolDao;
import com.todoapp.todojava.dao.UsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UsuarioDao userRepository;
	private RolDao rolRepository;
	private List<Roles> roles = new ArrayList<Roles>();

	@Autowired
	private Logger logger;

	public CustomUserDetailsService(UsuarioDao userRepository, RolDao rolRepository) {
		this.userRepository = userRepository;
		this.rolRepository = rolRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = userRepository.findByField("user", username).get(0);

		if (user != null) {
			roles.add(rolRepository.buscar(user.getIdRol()));
			
			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					mapRolesToAuthorities(username));
		} else {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String username)
			throws UsernameNotFoundException {
		Usuario user = userRepository.findByField("user", username).get(0);

		if (user != null) {
			List<Roles> userRoles = new ArrayList<Roles>();
			userRoles.add(rolRepository.buscar(user.getIdRol()));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			String formattedDateTime = now.format(formatter);
			logger.info("Usuario: " + user.getUser() + " - Fecha/hora: " + formattedDateTime + " - Login.");
			Collection<? extends GrantedAuthority> mapRoles = userRoles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre())).collect(Collectors.toList());
			return mapRoles;
		} else {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

}
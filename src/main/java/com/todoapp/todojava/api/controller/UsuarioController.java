package com.todoapp.todojava.api.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.todoapp.todojava.dao.RolDao;
import com.todoapp.todojava.clases.Usuario;
import com.todoapp.todojava.dao.UsuarioDao;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo/api/usuarios")
public class UsuarioController {

	@Autowired
	private Logger logger;

	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private RolDao rolDao;

	@Autowired
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	private void log(String operacion) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = usuarioDao.findByField("email", authentication.getName()).get(0).getUser();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String formattedDateTime = now.format(formatter);
		logger.info("Uso: " + operacion + " - Fecha/hora: " + formattedDateTime + " - Usuario: " + username);
	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> crearUsuario(@RequestBody Usuario usuario) {
		if (usuarioDao.findByField("user", usuario.getUser()).size() > 0) {
			return ResponseEntity.badRequest().body("Usuario ya existe");
		}

		usuario.setPassword(encoder().encode(usuario.getPassword()));
		usuarioDao.crear(Usuario.class, usuario);
		log("Añadir usuario [id: " + usuario.getId() + "]");
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
	}

	@PostMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> modificarUsuario(@PathVariable("id") int id, @RequestBody Usuario usuario) {
		usuario.setId(id);
		if (usuario.getPassword().equals(usuarioDao.buscar(id).getPassword())) {
			usuarioDao.modificar(usuario);
		} else {
			usuario.setPassword(encoder().encode(usuario.getPassword()));
			usuarioDao.modificar(usuario);
		}
		log("Modificar usuario [id: " + usuario.getId() + "]");
		return ResponseEntity.status(HttpStatus.CREATED).body("Usuario modificado exitosamente");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminarUsuario(@PathVariable int id) {
		usuarioDao.eliminar(id);
		log("Eliminar usuario [id: " + id + "]");
		return ResponseEntity.ok("Usuario eliminado exitosamente");
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioDao.listar();
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping(value = "/paginar/{pagina}", produces = "application/json")
	public ResponseEntity<List<Usuario>> listarUsuariosPaginado(@PathVariable int pagina) {
		List<Usuario> usuarios = usuarioDao.listarPaginado(pagina);
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable int id) {
		Usuario usuario = usuarioDao.buscar(id);
		if (usuario != null) {
			return ResponseEntity.ok(usuario);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping(value = "/buscar/{campo}/{valor}", produces = "application/json")
	public ResponseEntity<List<Usuario>> buscarUsuarioPorCampo(@PathVariable String campo, @PathVariable String valor) {
		List<Usuario> usuarios = null;
		if (campo.equalsIgnoreCase("idrol") || campo.equalsIgnoreCase("idasignacion")) {
			int numero = Integer.parseInt(valor);
			usuarios = usuarioDao.findByField(campo, numero);
		} else {
			usuarios = usuarioDao.findByField(campo, valor);
		}
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping(value = "/numero-paginas")
	public int getNumeroPaginas() {
		int paginas = usuarioDao.countPages("usuarios");
		return paginas;
	}

	@GetMapping(value = "/current", produces = "text/plain")
	public String nombreUsuarioActual() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String nombre = usuarioDao.findByField("email", authentication.getName()).get(0).getUser();
			return nombre;
		} else {
			return "El usuario no existe";
		}
	}
	
	@GetMapping(value = "/currentRol", produces = "text/plain")
	public String rolActual() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			int rol = usuarioDao.findByField("email", authentication.getName()).get(0).getIdRol();
			String nombre = rolDao.buscar(rol).getNombre();
			return nombre;
		} else {
			return "El rol no existe";
		}
	}
}

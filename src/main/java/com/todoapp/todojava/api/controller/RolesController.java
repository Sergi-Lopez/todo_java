package com.todoapp.todojava.api.controller;

import java.util.List;

import com.todoapp.todojava.clases.Roles;
import com.todoapp.todojava.dao.RolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo/api/roles")
public class RolesController {

	@Autowired
	private RolDao rolDao;

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> crearRol(@RequestBody Roles rol) {
		rolDao.crear(Roles.class, rol);
		return ResponseEntity.status(HttpStatus.CREATED).body("Rol creado exitosamente");
	}

	@PostMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> modificarRol(@PathVariable("id") int id, @RequestBody Roles rol) {
		rol.setId(id);
		rolDao.modificar(rol);
		return ResponseEntity.status(HttpStatus.CREATED).body("Rol modificado exitosamente");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminarRol(@PathVariable int id) {
		rolDao.eliminar(id);
		return ResponseEntity.ok("Rol eliminado exitosamente");
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<Roles>> listarRoles() {
		List<Roles> rol = rolDao.listar();
		return ResponseEntity.ok(rol);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Roles> buscarRolPorId(@PathVariable int id) {
		Roles rol = rolDao.buscar(id);
		if (rol != null) {
			return ResponseEntity.ok(rol);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@GetMapping(value = "/numero-paginas")
	public int getNumeroPaginas() {
		int paginas = rolDao.countPages("roles");
		return paginas;
	}

}

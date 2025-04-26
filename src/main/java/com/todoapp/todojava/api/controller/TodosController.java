package com.todoapp.todojava.api.controller;

import java.util.List;

import com.todoapp.todojava.clases.Todo;
import com.todoapp.todojava.dao.TodoDao;
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
@RequestMapping("/todo/api/todos")
public class TodosController {

	@Autowired
	private TodoDao todoDao;

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> crearTodo(@RequestBody Todo nombre) {
		todoDao.crear(Todo.class, nombre);
		return ResponseEntity.status(HttpStatus.CREATED).body("Tarea creada exitosamente");
	}

	@PostMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> modificarTodo(@PathVariable("id") int id, @RequestBody Todo nombre) {
		nombre.setId(id);
		todoDao.modificar(nombre);
		return ResponseEntity.status(HttpStatus.CREATED).body("Tarea modificada exitosamente");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminarTodo(@PathVariable int id) {
		todoDao.eliminar(id);
		return ResponseEntity.ok("Tarea eliminada exitosamente");
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<Todo>> listarTodos() {
		List<Todo> todo = todoDao.listar();
		return ResponseEntity.ok(todo);
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Todo> buscarTodoPorId(@PathVariable int id) {
		Todo todo = todoDao.buscar(id);
		if (todo != null) {
			return ResponseEntity.ok(todo);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@GetMapping(value = "/numero-paginas")
	public int getNumeroPaginas() {
		int paginas = todoDao.countPages("todo");
		return paginas;
	}

}

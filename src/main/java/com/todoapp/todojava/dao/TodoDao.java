package com.todoapp.todojava.dao;

import com.todoapp.todojava.clases.Todo;

public class TodoDao extends GenericDao<Todo> {

	public TodoDao() {
		super(Todo.class);
	}

}

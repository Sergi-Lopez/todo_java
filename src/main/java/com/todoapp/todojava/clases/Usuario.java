package com.todoapp.todojava.clases;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "USER", unique = true, nullable = false)
	private String user;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "NOMBRE", nullable = false)
	private String nombre;

	@Column(name = "APELLIDOS", nullable = false)
	private String apellidos;

	@Column(name = "NIF", unique = true, nullable = false)
	private String nif;

	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;

	@Column(name = "ID_ROL", nullable = false)
	private int idRol;

	@Column(name = "ID_TASIGNACION", nullable = false)
	private int idAsignacion;

	public Usuario() {
	}

	public Usuario(String user, String password, String nombre, String apellidos, String nif, String email, int idRol,
			int idAsignacion) {
		this.user = user;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.nif = nif;
		this.email = email;
		this.idRol = idRol;
		this.idAsignacion = idAsignacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public int getIdAsignacion() {
		return idAsignacion;
	}

	public void setIdAsignacion(int idAsignacion) {
		this.idAsignacion = idAsignacion;
	}

}

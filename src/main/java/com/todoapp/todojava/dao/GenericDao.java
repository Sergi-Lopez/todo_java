package com.todoapp.todojava.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public abstract class GenericDao<T> {

	protected static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("todo");
	private final Class<T> clase;

	public GenericDao(Class<T> clase) {
		this.clase = clase;
	}

	// Inicia una transacción
	public void setUp(EntityManager entityManager) {
		entityManager.getTransaction().begin();
	}

	// Almacena el objeto dado en la base de datos
	public void crear(Class<?> claseEntidad, Object objeto) {
		EntityManager emCrear = emf.createEntityManager();
		setUp(emCrear);
		emCrear.persist(objeto);
		cerrar(emCrear);
	}

	// Modifica el objeto dado, merge lo que hace es buscar otro objeto con el mismo id y cambia sus valores por los nuevos
	public void modificar(T objeto) {
		EntityManager emMod = emf.createEntityManager();
		setUp(emMod);
		emMod.merge(objeto);
		cerrar(emMod);
	}

	// Busca un resultado de una tabla por id
	public T buscar(int id) {
		EntityManager emBuscar = emf.createEntityManager();
		setUp(emBuscar);
		T objeto = emBuscar.find(clase, id);
		cerrar(emBuscar);
		return objeto;

	}

	// Busca en una tabla por un campo en concreto, fieldName sería un string con el nombre del campo y value es un Object para poder
	// aceptar tanto String como enteros.
	public List<T> findByField(String fieldName, Object value) throws NoResultException {
		EntityManager emBuscarCampo = emf.createEntityManager();
		setUp(emBuscarCampo);
		// Uilizando CriteriaBuilder y CriteriaQuery lo que se hace es una solicitud a la base de datos con un filtro que se definirá con
		// Predicate
		CriteriaBuilder cb = emBuscarCampo.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clase);
		Root<T> root = cq.from(clase);

		Predicate predicate = cb.equal(root.get(fieldName), value);
		cq.where(predicate);

		TypedQuery<T> query = emBuscarCampo.createQuery(cq);
		cerrar(emBuscarCampo);
		return query.getResultList();
	}

	// Primero busca por id y luego usa el metodo remove para eliminarlo de la base de datos
	public void eliminar(int id) {
		EntityManager emEliminar = emf.createEntityManager();
		setUp(emEliminar);
		T objeto = emEliminar.find(clase, id);
		emEliminar.remove(objeto);
		cerrar(emEliminar);
	}

	// Hace una query de todos los resultados de una tabla, devuelve una lista con todo lo que haya en esa tabla
	public List<T> listar() {
		EntityManager emListar = emf.createEntityManager();
		setUp(emListar);
		TypedQuery<T> query = emListar.createQuery("SELECT t FROM " + clase.getSimpleName() + " t", clase);
		List<T> lista = query.getResultList();
		cerrar(emListar);
		return lista;
	}

	// Similar al metodo anterior solo que aquí se limitan los resultados a 50, utilizado para la paginación
	public List<T> listarPaginado(int pagina) {
		EntityManager emListarPaginado = emf.createEntityManager();
		setUp(emListarPaginado);
		int longitudPagina = 50;
		TypedQuery<T> query = emListarPaginado.createQuery("SELECT t FROM " + clase.getSimpleName() + " t", clase);
		query.setFirstResult((pagina - 1) * longitudPagina);
		query.setMaxResults(longitudPagina);
		List<T> lista = query.getResultList();
		cerrar(emListarPaginado);
		return lista;
	}
	
	// Similar al anterior pero aquí de nuevo se le añade un CriteriaBuilder y CriteriaQuery para añadirle el filtro igual que está
	// explicado en findByField
	public List<T> listarPaginadoFiltrado(int pagina, String filtro, Object valor) {
	    EntityManager emListarPaginadoFiltrado = emf.createEntityManager();
	    try {
	        setUp(emListarPaginadoFiltrado);
	        int longitudPagina = 50;
	        CriteriaBuilder cb = emListarPaginadoFiltrado.getCriteriaBuilder();
	        CriteriaQuery<T> cq = cb.createQuery(clase);
	        Root<T> root = cq.from(clase);
	        Predicate predicate = cb.equal(root.get(filtro), valor);
	        cq.where(predicate);
	        int firstResult = (pagina - 1) * longitudPagina;
	        TypedQuery<T> typedQuery = emListarPaginadoFiltrado.createQuery(cq);
	        typedQuery.setFirstResult(firstResult);
	        typedQuery.setMaxResults(longitudPagina);

	        List<T> lista = typedQuery.getResultList();
	        return lista;
	    } finally {
	        if (emListarPaginadoFiltrado.isOpen()) {
	            emListarPaginadoFiltrado.close();
	        }
	    }
	}

	// Cuenta los elementos, los divide entre 50 y siendo ese el numero de paginas lo redondea hacia arriba
	public int countPages(String tabla) {
		EntityManager emContarPaginas = emf.createEntityManager();
		setUp(emContarPaginas);
		String sql = "SELECT COUNT(*) FROM " + tabla;

		Query query = emContarPaginas.createNativeQuery(sql);

		Long totalElementos = (Long) query.getSingleResult();
		cerrar(emContarPaginas);
		return (int) Math.ceil((double) totalElementos / 50);
	}
	
	// Cierra la transacción y hace un commit
	public void cerrar(EntityManager em) {
		em.getTransaction().commit();
	}
}

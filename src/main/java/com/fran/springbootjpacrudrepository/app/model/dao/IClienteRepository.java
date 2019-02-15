package com.fran.springbootjpacrudrepository.app.model.dao;

//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.fran.springbootjpacrudrepository.app.model.entity.Cliente;
/**
 * PagingAndSortRepository, hereda a su vez de JpaRepository, por lo que no presentara
 * dificultades el cambio
 * Se aniaden dos querys 'customizadas'
 * @author Fran
 *
 */
public interface IClienteRepository extends PagingAndSortingRepository<Cliente, Long> {

	@Query("select c from Cliente c WHERE c.nombre = ?1")
	public Cliente findByNombre(String nombre);
	
	@Query("select c from Cliente c WHERE c.nombre = ?1 AND c.apellidos = ?2")
	Cliente findByNombreAndApellidos(String nom, String ap);
}

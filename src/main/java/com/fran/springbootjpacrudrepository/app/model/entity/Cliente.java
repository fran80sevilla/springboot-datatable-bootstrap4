package com.fran.springbootjpacrudrepository.app.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	/**
	 * TODO la migracion a Spring Boot implica que las anotaciones @NotEmpty y @Email estaran
	 * deprecated. Habria que usar la libreria javax.validation.constraints.*;
	 */
	private static final long serialVersionUID = 1L;

	// anotamos la clave o primary key, en @GenerateValue estamos indicando que es incremental
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre")
	@NotEmpty
	private String nombre;
	@Column(name = "apellidos")
	@NotEmpty
	private String apellidos;

	@Column(name = "email")
	@NotEmpty
	@Email
	private String email;

	// standard nombre campo-propiedad
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;

	/**
	 * Este metodo interfiere en el ciclo de vida del objeto antes de crearse.
	 */
	@PrePersist
	@DateTimeFormat(pattern = "dd-MMM-YYYY")
	public void prePersist() {
		this.createAt = new Date();
	}

	// funciona pero actualiza a una fecha principios anio, mantener la anterior seria poniendo la propiedad en un input hidden de la vista
	@PreUpdate
	public void preUpdateCreateAt() {
		this.createAt = new Date();
	}
	/**
	 * CONSTRUCTOR
	 */
	public Cliente() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 * the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 * nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * @param apellidos
	 * to set
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 * to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the createAt
	 */
	public Date getCreateAt() {
		return createAt;
	}

	/**
	 * @param createAt
	 * to set
	 */
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}

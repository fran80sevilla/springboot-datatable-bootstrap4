package com.fran.springbootjpacrudrepository.app.model.service;

import java.util.List;

import com.fran.springbootjpacrudrepository.app.model.entity.Cliente;

public interface IClienteService {
	
	public Cliente save(Cliente cliente);
	
	public void eliminarCliente(Long id);
	
	public void delete(Long id);
	
	public List<Cliente> listarClientes();
	
	public Cliente buscarUnCliente(Long id);
	
	public void editarCliente(Cliente cliente);
	
	public Cliente findByNombre(String nombre);
	
	public Cliente findByNombreAndApellidos(String nom, String ap);
	
	public boolean validarEditarCliente(Cliente cliente);
}

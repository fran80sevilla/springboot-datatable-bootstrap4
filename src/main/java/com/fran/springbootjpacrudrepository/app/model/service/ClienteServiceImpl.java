package com.fran.springbootjpacrudrepository.app.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fran.springbootjpacrudrepository.app.model.dao.IClienteRepository;
import com.fran.springbootjpacrudrepository.app.model.entity.Cliente;
/**
 * implementa metodos definidos en IClienteService
 * @author Fran
 *
 */
@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	IClienteRepository iClienteRepo;

	@Override
	public Cliente findByNombre(String nombre) {
		return this.iClienteRepo.findByNombre(nombre);
	}

	@Override
	public Cliente save(Cliente cliente) {
		return iClienteRepo.save(cliente);
	}

	@Override
	public void eliminarCliente(Long id) {
		this.iClienteRepo.delete(id);
	}

	@Override
	public void delete(Long id) {
		this.iClienteRepo.delete(id);
	}

	@Override
	public void editarCliente(Cliente cliente) {
		this.iClienteRepo.save(cliente);
	}

	@Override
	public Cliente findByNombreAndApellidos(String nom, String ap) {
		return this.iClienteRepo.findByNombreAndApellidos(nom, ap);
	}

	@Override
	public Cliente buscarUnCliente(Long id) {
		return this.iClienteRepo.findOne(id);
	}

	@Override
	public boolean validarEditarCliente(Cliente cliente) {
		// si alguno de ellos esta vacio
		if (cliente.getNombre().isEmpty() || cliente.getApellidos().isEmpty() || cliente.getEmail().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public List<Cliente> listarClientes() {
		// castear para que la interfaz del DAO permita el List
		return (List<Cliente>) this.iClienteRepo.findAll(); 
	}
	
}

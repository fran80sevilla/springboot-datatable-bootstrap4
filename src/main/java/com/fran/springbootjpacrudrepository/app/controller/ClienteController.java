package com.fran.springbootjpacrudrepository.app.controller;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fran.springbootjpacrudrepository.app.model.entity.Cliente;
import com.fran.springbootjpacrudrepository.app.model.service.IClienteService;
/**
 * http://localhost:9999/
 * @author Francisco
 *
 */
@Controller
public class ClienteController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// siempre inyectamos el componente mas generico, en este caso la interfaz
	@Autowired
	IClienteService iClienteService;

	Date fecha_actual = null;

	/**
	 * Mostramos el home en la raiz del proyecto, sera la vista que se cargue en primer lugar 
	 * 
	 * @param map
	 * @return home - pantalla de bienvenida
	 */
	@RequestMapping(value = "/")
	public String home(Map<String, Object> map) {
		map.put("tituloHome", "Bienvenido a la Web de Clientes");
		map.put("encabezado", "Bootstrap +  Datatable");
		return "home";
	}

	@RequestMapping(value = "/listar-clientes", method = RequestMethod.GET)
	public String listarClientes(Model model) {

		model.addAttribute("titulo", "Lista de Clientes");
		// inyecta lista interfaz DAO de los clientes
		model.addAttribute("listaClientes", this.iClienteService.listarClientes());
		
		return "listaClientes";
	}

	@RequestMapping(value = "/editar-cliente/{id}", method = RequestMethod.POST)
	public String editarClientePost(Cliente cliente, @PathVariable(value = "id") Long id, RedirectAttributes flash,
			Model model, SessionStatus status) {

		logger.info("\nEncontramos al cliente: " + cliente.getId() + ", nombre: " + cliente.getNombre()
				+ " en el metodo POST de edicion.");

		if (!this.iClienteService.validarEditarCliente(cliente)) {
			logger.info("\nPasa por validacion de error, el metodo del servicio da "
					+ this.iClienteService.validarEditarCliente(cliente));
			//	status.setComplete();
			flash.addFlashAttribute("warning", "No puede haber datos vacios!");
			logger.info("\n Valor del atributo flash warning del post" + flash.toString());

			// le damos un tiempo a la redireccion por probar
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// redirigimos a la misma URL en caso de error
			return "redirect:/editar-cliente/" + String.valueOf(id);

		} else {
			// editamos
			this.iClienteService.editarCliente(cliente);
			flash.addFlashAttribute("exito", "Cliente editado con éxito");
			logger.info("\nLlegamos al metodo POST editar Cliente con id " + cliente.getId());

			return "redirect:/datatable";
		}
	}
	
	/**
	 * En esta caso se edita en la vista listar-clientes
	 * Similar a guardar. Buscamos el objeto cliente en la bdd para editarlo 
	 * 
	 * @param Long id parametro recibida en la url
	 * @param Model model 
	 * @return
	 */
	@RequestMapping(value = "/editar-cliente/{id}")
	public String editarCliente(@PathVariable(value = "id") Long id, Model model) {

		// esta vez no precisamos de una nueva instancia Cliente
		Cliente cliente = null;
		// se cumple, se edita el cliente
		if (id > 0 && id != null) {
			// busqueda del cliente
			cliente = this.iClienteService.buscarUnCliente(id);
			logger.info("\nSe ha encontrado el cliente: " + cliente.getNombre() + " " + cliente.getApellidos());

			// se envia al cliente a la vista de edicion
			model.addAttribute("cliente", cliente);
			model.addAttribute("tituloEdicion", "Editar Cliente");
			return "editarCliente";

		} else {
			// por seguridad
			return "redirect:/listar-clientes";
		}
	}

	@RequestMapping("eliminar-cliente/{id}")
	public String eliminarCliente(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		
		if (id > 0) {
			flash.addFlashAttribute("exito", "Cliente eliminado");
			this.iClienteService.eliminarCliente(id);
			return "redirect:/datatable";

		} else {
			flash.addFlashAttribute("error", "El id del cliente debe ser mayor que CERO, es decir no existe en la BDD");
			return "redirect:/datatable";
		}
	}
	
	/********** DATATABLE NORMAL ***********/
	@RequestMapping(value = "/datatable_normal")
	public String mostrarDatatableNormal(Model model) {

		model.addAttribute("titulo", "Datatable normal");
		return "datatable-normal";
	}
	
	/********** DATATABLE THYMELEAF ***********/
	@RequestMapping(value = "/datatable")
	public String mostrarDatatableThymeleaf(Model model) {
		
		model.addAttribute("titulo", "Jquery Datatable");
		// lista de clientes
		model.addAttribute("listaClientes", this.iClienteService.listarClientes());
		return "lista-paginador-datatable";
	}
	
	/********** FORMULARIO Test ResponseBody***********/
	// public @ResponseBody String mostrarFormularioClientes(Model model) {
	@RequestMapping(value = "/form-cliente", method = RequestMethod.GET)
	public @ResponseBody String mostrarFormularioClientes(Model model) {

		// creamos nueva instancia para mandarla a la vista
		Cliente cliente = new Cliente();
		model.addAttribute("tituloFormulario", "Formulario de Clientes");
		model.addAttribute("cliente", cliente);
		// esto aparece si invocamos a la vista con api rest
		return "formulario";
	}

	/**
	 * Creamos nueva instancia de un objeto cliente se muestra tambien un mensaje flash cuando guardamos
	 * con exito o existe un error. Se guardan en la sesion HTTP entre el REQUEST de hacer submit, y el
	 * REQUEST de redireccionar la pagina. Se les conoce como mensajes flash
	 * 
	 * @param flash valida errores
	 * mensaje que se muestra al redireccionar la pagina tras la accion
	 * @param status sesion hasta el siguiente REQUEST
	 * @param cliente objeto
	 * @param result
	 */
	@RequestMapping(value = "/form-cliente", method = RequestMethod.POST)
	public String guardarClientesForm(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash,
			SessionStatus status) {
		// no es necesario instanciar, porque ya se hizo en el GET, si no casca
		if (result.hasErrors()) {
			logger.info("\n" + "entra en errores " + result.getFieldErrorCount() + "  " + result.getAllErrors());
			return "formulario";
		}
		this.iClienteService.save(cliente);
		// indicamos el inicio del request entre guardar cliente hasta el siguiente, que es el redirect
		status.setComplete();
		flash.addFlashAttribute("exito", "Cliente guardado con éxito!");

		logger.info("\nSe ha insertado el cliente: " + cliente.getNombre() + " " + cliente.getApellidos());
		// es la ruta que le dimos en el proyecto, no el nombre de la vista
		return "redirect:listar-clientes";
	}
}

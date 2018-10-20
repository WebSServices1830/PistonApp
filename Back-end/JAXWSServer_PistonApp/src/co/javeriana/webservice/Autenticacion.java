package co.javeriana.webservice;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Usuario;

import clases_negocio.Usuario;

@WebService(name="autenticacion")
public class Autenticacion {
	
	CRUD_Usuario manejadorUsuario = new CRUD_Usuario();
	
	@WebMethod
	public boolean registrarUsuario(
			@WebParam(name = "nombreUsuario") String nombreUsuario,
			@WebParam(name = "contrasenia") String contrasenia,
			@WebParam(name = "fechaNacimiento") Date fechaNacimiento,
			@WebParam(name = "urlFoto") String urlFoto,
			@WebParam(name = "admin") boolean admin) {
		
		boolean yaExisteUsuario = manejadorUsuario.existeUsuario(nombreUsuario);
		
		if(!yaExisteUsuario) {
			Usuario usuario = new Usuario(nombreUsuario, contrasenia, fechaNacimiento, urlFoto, admin);
			manejadorUsuario.usuario_create(usuario);
			return true;
		}
		
		return false;
		
	}
	
	@WebMethod
	public Usuario validarLogin(
			@WebParam(name = "nombreUsuario") String nombreUsuario,
			@WebParam(name = "contrasenia") String contrasenia) {
		
		Usuario usuario = manejadorUsuario.usuario_getByName(nombreUsuario);
		
		if(usuario != null) {
			if(usuario.getContra().equals(contrasenia)) {
				return usuario;
			}
		}
		
		return null;
		
	}

}

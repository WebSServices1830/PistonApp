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
	public void registrarUsuario(
			@WebParam(name = "nombreUsuario") String nombreUsuario,
			@WebParam(name = "contrasenia") String contrasenia,
			@WebParam(name = "fechaNacimiento") Date fechaNacimiento,
			@WebParam(name = "urlFoto") String urlFoto,
			@WebParam(name = "admin") boolean admin) {
		Usuario usuario = new Usuario(nombreUsuario, contrasenia, fechaNacimiento, urlFoto, admin);
		manejadorUsuario.usuario_create(usuario);
	}
	
	@WebMethod
	public boolean validarLogin(
			@WebParam(name = "nombreUsuario") String nombreUsuario,
			@WebParam(name = "contrasenia")String contrasenia) {
		Usuario usuario = manejadorUsuario.usuario_getByName(nombreUsuario);
		if(usuario.getContra().equals(contrasenia)) {
			return true;
		}
		return false;
	}

}

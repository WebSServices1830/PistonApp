package co.javeriana.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Auto;
import CRUDs.CRUD_Escuderia;
import CRUDs.CRUD_GranPremio;
import CRUDs.CRUD_Piloto;
import CRUDs.CRUD_Usuario;

import clases_negocio.Usuario;

@WebService(name="pistonApp")
public class PistonApp {
	
	CRUD_Usuario manejadorUsuario = new CRUD_Usuario();
	
	@WebMethod
	public void registrarUsuario(@WebParam(name = "usuario") Usuario usuario) {
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

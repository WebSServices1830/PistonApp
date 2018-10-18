package co.javeriana.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_ClasificacionCampeonato;
import clases_negocio.ClasificacionCampeonato;

@WebService(name="admin")
public class Administrador {
	
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	
	@WebMethod
	public void registrarClasificacionCampeonato(@WebParam(name = "clasificacionCampeonato") ClasificacionCampeonato clasificacionCampeonato) {
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(clasificacionCampeonato);
	}
	
	@WebMethod
	public void actualizarClasificacionCampeonato(@WebParam(name = "clasificacionCampeonato") ClasificacionCampeonato clasificacionCampeonato) {
		manejadorClasificacion_Campeonato.clasificacionCampeonato_update(clasificacionCampeonato);
	}
	
	@WebMethod
	public void eliminarClasificacionCampeonato(@WebParam(name = "id") String id) {
		manejadorClasificacion_Campeonato.clasificacionCampeonato_delete(id);
	}

}

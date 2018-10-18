package co.javeriana.webservice;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_ClasificacionCampeonato;
import clases_negocio.ClasificacionCampeonato;

@WebService(name="infoCampeonato")
public class InformacionCampeonato {
	
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
	public List<ClasificacionCampeonato> verClasificacionesCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}
	
	@WebMethod
	public void eliminarClasificacionCampeonato(@WebParam(name = "id") String id) {
		manejadorClasificacion_Campeonato.clasificacionCampeonato_delete(id);
	}

}

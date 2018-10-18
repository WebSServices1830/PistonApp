package co.javeriana.webservice;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_ClasificacionCampeonato;
import clases_negocio.ClasificacionCampeonato;

@WebService(name="admin")
public class Administrador {
	
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	
	@WebMethod
	public void registrarClasificacionCampeonato(
			@WebParam(name = "puntaje") int puntaje,
			@WebParam(name = "tiempo") Date tiempo,
			@WebParam(name = "posicion") int posicion,
			@WebParam(name = "piloto") String piloto) {
		ClasificacionCampeonato clasificacionCampeonato = new ClasificacionCampeonato(puntaje, tiempo, posicion, piloto);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(clasificacionCampeonato);
	}
	
	@WebMethod
	public void actualizarClasificacionCampeonato(
			@WebParam(name = "id") String id,
			@WebParam(name = "puntaje") int puntaje,
			@WebParam(name = "tiempo") Date tiempo,
			@WebParam(name = "posicion") int posicion){ 
		ClasificacionCampeonato clasificacionCampeonato = manejadorClasificacion_Campeonato.clasificacionCampeonato_get(id);
		clasificacionCampeonato.setPuntaje(puntaje);
		clasificacionCampeonato.setPosicion(posicion);
		clasificacionCampeonato.setTiempo(tiempo);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_update(clasificacionCampeonato);
	}
	
	@WebMethod
	public void eliminarClasificacionCampeonato(@WebParam(name = "id") String id) {
		manejadorClasificacion_Campeonato.clasificacionCampeonato_delete(id);
	}

}

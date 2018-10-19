package co.javeriana.webservice;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_ClasificacionCarrera;
import clases_negocio.ClasificacionCampeonato;
import clases_negocio.ClasificacionCarrera;

@WebService(name="admin")
public class Administrador {
	
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	CRUD_ClasificacionCarrera manejadorClasificacion_Carrera = new CRUD_ClasificacionCarrera();
	
	@WebMethod
	public void registrarClasificacionCampeonato(
			@WebParam(name = "posicion") int posicion,
			@WebParam(name = "piloto") String piloto) {
		ClasificacionCampeonato clasificacionCampeonato = new ClasificacionCampeonato(0, posicion, piloto);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(clasificacionCampeonato);
	}
	
	@WebMethod
	public void actualizarClasificacionCampeonato(
			@WebParam(name = "id") String id,
			@WebParam(name = "puntaje") int puntaje){ 
		ClasificacionCampeonato clasificacionCampeonato = manejadorClasificacion_Campeonato.clasificacionCampeonato_get(id);
		clasificacionCampeonato.setPuntaje(puntaje);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_update(clasificacionCampeonato);
	}
	
	@WebMethod
	public void eliminarClasificacionCampeonato(@WebParam(name = "id") String id) {
		manejadorClasificacion_Campeonato.clasificacionCampeonato_delete(id);
	} 	
	
	@WebMethod
	public void registrarClasificacionCarrera(
			@WebParam(name = "puntaje") int puntaje,
			@WebParam(name = "tiempo") Date tiempo,
			@WebParam(name = "competidor") String competidor) {
		ClasificacionCarrera clasificacionCarrera = new ClasificacionCarrera(puntaje, tiempo, competidor);
		manejadorClasificacion_Carrera.clasificacionCarrera_create(clasificacionCarrera);
		
		ClasificacionCampeonato clasificacionCampeonato= manejadorClasificacion_Campeonato.clasificacionCampeonato_getByPiloto(competidor);
		actualizarClasificacionCampeonato(clasificacionCampeonato.getId_str(), puntaje+clasificacionCampeonato.getPuntaje());
	}
	
	@WebMethod
	public void actualizarClasificacionCarrera(
			@WebParam(name = "id") String id,
			@WebParam(name = "puntaje") int puntaje,
			@WebParam(name = "tiempo") Date tiempo,
			@WebParam(name = "posicion") int posicion){ 
		ClasificacionCarrera clasificacionCarrera = manejadorClasificacion_Carrera.clasificacionCarrera_get(id);
		clasificacionCarrera.setPuntaje(puntaje);
		clasificacionCarrera.setTiempo(tiempo);
		manejadorClasificacion_Carrera.clasificacionCarrera_update(clasificacionCarrera);
	}
		
	@WebMethod
	public void eliminarClasificacionCarrera(@WebParam(name = "id") String id) {
		manejadorClasificacion_Carrera.clasificacionCarrera_delete(id);
	}

}

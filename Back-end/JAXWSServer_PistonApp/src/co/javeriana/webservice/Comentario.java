package co.javeriana.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


import CRUDs.CRUD_ComentarioPiloto;
import CRUDs.CRUD_ComentarioPista;
import CRUDs.CRUD_Piloto;
import CRUDs.CRUD_Pista;
import clases_negocio.ComentarioPiloto;
import clases_negocio.ComentarioPista;
import clases_negocio.GranPremio;
import clases_negocio.Piloto;
import clases_negocio.Pista;

@WebService(name="comentario")
public class Comentario {
	
	CRUD_ComentarioPiloto manejadorComentarioPiloto = new CRUD_ComentarioPiloto();
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();
	CRUD_ComentarioPista manejadorComentarioPista = new CRUD_ComentarioPista();
	CRUD_Pista manejadorPista = new CRUD_Pista();
	
	@WebMethod 
	public void publicarComentarioPiloto(
			@WebParam(name = "idUsuario") String idUsuario,
			@WebParam(name = "comentario") String comentario,
			@WebParam(name = "idPiloto") String idPiloto,
			@WebParam(name = "calificacion") int calificacion
			) {
		
		manejadorComentarioPiloto.comentariopiloto_create(comentario, calificacion, idUsuario, idPiloto);
		List<ComentarioPiloto> comentarios = manejadorComentarioPiloto.comentariopiloto_read_Especific_Pilot(idPiloto);
		float ncalificacion=0;
		for (ComentarioPiloto comentarioPiloto : comentarios) {
				ncalificacion=ncalificacion+comentarioPiloto.getCalificacion();
		}
		manejadorPiloto.piloto_update_calificacion(idPiloto,(ncalificacion/comentarios.size()));
		
	}
	@WebMethod 
	public void publicarComentarioPista(
			@WebParam(name = "idUsuario") String idUsuario,
			@WebParam(name = "comentario") String comentario,
			@WebParam(name = "idPiloto") String idPista,
			@WebParam(name = "calificacion") int calificacion
			) {
		
		manejadorComentarioPista.comentariopiloto_create(comentario, calificacion, idPista, idUsuario);
		List<ComentarioPista> comentarios = manejadorComentarioPista.comentariopista_read_Specific_Pista(idPista);
		float ncalificacion=0;
		for (ComentarioPista comentarioPista : comentarios) {
				ncalificacion=ncalificacion+comentarioPista.getCalificacion();
		}
		manejadorPista.pista_update_calificacion(idPista,(ncalificacion/comentarios.size()));
		
	}
	@WebMethod
	public List <Piloto>obtenerPrimerosCincoPilotos() {
		List<Piloto> todos = manejadorPiloto.piloto_getAll();
		Collections.sort(todos, new Comparator<Piloto>() {

		    public int compare(Piloto p1, Piloto p2) {
		         return (int) ((int) p1.getCalificacion()-p2.getCalificacion());
		    }
		});
		List<Piloto> mejores = todos.subList(0, 4);
		return mejores;
		
	}
	public List <Pista>obtenerPrimerasCincoPistas() {
		List<Pista> todos = manejadorPista.pista_getAll();
		Collections.sort(todos, new Comparator<Pista>() {

		    public int compare(Pista p1, Pista p2) {
		         return (int) ((int) p1.getCalificacion()-p2.getCalificacion());
		    }
		});
		List<Pista> mejores = todos.subList(0, 4);
		return mejores;
		
	}

}

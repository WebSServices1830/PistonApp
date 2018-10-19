package co.javeriana.webservice;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


import CRUDs.CRUD_ComentarioPiloto;
import CRUDs.CRUD_Piloto;
import clases_negocio.ComentarioPiloto;

@WebService(name="comentario")
public class Comentario {
	
	CRUD_ComentarioPiloto manejadorComentarioPiloto = new CRUD_ComentarioPiloto();
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();
	
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

}

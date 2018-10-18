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
	public List<ClasificacionCampeonato> verClasificacionesCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}
	
}

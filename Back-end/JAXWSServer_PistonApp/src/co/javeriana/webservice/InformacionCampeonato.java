package co.javeriana.webservice;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_GranPremio;
import clases_negocio.ClasificacionCampeonato;
import clases_negocio.GranPremio;

@WebService(name="infoCampeonato")
public class InformacionCampeonato {
	
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	CRUD_GranPremio manejadorGranPremio = new CRUD_GranPremio();
	
	@WebMethod
	public List<ClasificacionCampeonato> verClasificacionesCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}
	
	@WebMethod
	public List<GranPremio> carrerasOrdenadoPorFecha(){
		return manejadorGranPremio.grandesPremios_X_Fecha();
	}
	
}

package co.javeriana.webservice;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Campeonato;
import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_GranPremio;
import CRUDs.CRUD_Pista;
import clases_mongoDB.ManejadorImagenes;
import clases_negocio.Campeonato;
import clases_negocio.ClasificacionCampeonato;
import clases_negocio.GranPremio;
import clases_negocio.Pista;
import clases_negocio.Record;

@WebService(name="infoCampeonato")
public class InformacionCampeonato {
	
	CRUD_Campeonato manejadorCampeonato = new CRUD_Campeonato();
	CRUD_GranPremio manejadorGranPremio = new CRUD_GranPremio();
	CRUD_Pista manejadorPista = new CRUD_Pista();
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	
	@WebMethod
	public void inicializarCampeonato() {
		//CAMPEONATO
		GregorianCalendar fechaInicio = new GregorianCalendar(2018,0,1);
		
		GregorianCalendar fechaFin = new GregorianCalendar(2018,11,31);
		
		Campeonato campeonato = manejadorCampeonato.campeonato_create("Campeonato 2018", fechaInicio.getTime(), fechaFin.getTime());
		
		//GRAN PREMIO 1
		GregorianCalendar fechaGranPremio_1 = new GregorianCalendar(2018,2,25);
		
		GregorianCalendar mejorVuelta_1 = new GregorianCalendar();
		mejorVuelta_1.set(Calendar.HOUR,1);
		mejorVuelta_1.set(Calendar.MINUTE,29);
		mejorVuelta_1.set(Calendar.SECOND,33);
		mejorVuelta_1.set(Calendar.MILLISECOND,283);
		
		//PISTA 1
		GregorianCalendar recordVuelta_1 = new GregorianCalendar();
		recordVuelta_1.set(Calendar.HOUR, 1);
		recordVuelta_1.set(Calendar.MINUTE, 24);
		recordVuelta_1.set(Calendar.SECOND, 0);
		recordVuelta_1.set(Calendar.MILLISECOND, 125);
		Record record_1 = new Record(recordVuelta_1.getTime(),"Michael Schumacher",2004);
		String fotoRef_1 = null;
		try {
			fotoRef_1 = ManejadorImagenes.saveImageIntoMongoDB("/fotos/australiaGrandPrix.png", "Melbourne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(fotoRef_1 != null) {
			Pista pista = manejadorPista.pista_create("Melbourne", fotoRef_1, "Sebastian Vettel", (float)307.57, (float)5.303, record_1);
			
			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_1.getTime(), 58, mejorVuelta_1.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}
		
		
	}
	
	@WebMethod
	public List<ClasificacionCampeonato> verClasificacionesCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}
	
	@WebMethod
	public List<GranPremio> carrerasOrdenadoPorFecha(){
		return manejadorGranPremio.grandesPremios_X_Fecha();
	}
	
}

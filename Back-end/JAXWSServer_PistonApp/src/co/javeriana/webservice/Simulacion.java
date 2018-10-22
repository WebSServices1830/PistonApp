package co.javeriana.webservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Apuesta;
import CRUDs.CRUD_Campeonato;
import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_ClasificacionCarrera;
import CRUDs.CRUD_GranPremio;
import CRUDs.CRUD_Piloto;
import CRUDs.CRUD_Usuario;
import clases_negocio.Apuesta;
import clases_negocio.ClasificacionCarrera;
import clases_negocio.GranPremio;
import clases_negocio.Piloto;
import clases_negocio.Usuario;

@WebService(name="simulacion")
public class Simulacion {
	
	CRUD_ClasificacionCarrera manejadorClasificacionCarrer = new CRUD_ClasificacionCarrera();
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();
	CRUD_GranPremio manejadorGranPremio = new CRUD_GranPremio();
	CRUD_Apuesta manejadorApuesta = new CRUD_Apuesta();
	CRUD_Usuario manejadorUsuario = new CRUD_Usuario();
	CRUD_Campeonato manejadorCampeonato = new CRUD_Campeonato();
	CRUD_ClasificacionCampeonato manejadorClasificacionCampeonato = new CRUD_ClasificacionCampeonato();
	
	
	private void simularApuestas(String id_granpremio,
								String piloto) {
		List<Apuesta>apuestas=manejadorApuesta.apuestas_GranPremio(id_granpremio);
		List<Apuesta> ganadores= new ArrayList<>();
		double alGanador=0;
		double total=0;
		for (Apuesta apuesta : apuestas) {
			if(apuesta.getPiloto().equals(piloto)) {
				Usuario u = manejadorUsuario.usuario_get(apuesta.getUsuario());
				manejadorUsuario.usuario_update_bolsillo(u.getId_str(), u.getBolsillo()+apuesta.getMonto());
				ganadores.add(apuesta);
				alGanador= alGanador+apuesta.getMonto();
			}else {
				total=total+apuesta.getMonto();
			}
		}
		for (Apuesta apuesta : ganadores) {
			float porcentaje = (float) (apuesta.getMonto()/alGanador);
			Usuario u = manejadorUsuario.usuario_get(apuesta.getUsuario());
			manejadorUsuario.usuario_update_bolsillo(u.getId_str(), u.getBolsillo()+(total*porcentaje));
		}
		
	}
	@WebMethod
	public void simularGranPremio(@WebParam(name = "id_granpremio")String id_granpremio) {
		GranPremio carrera = manejadorGranPremio.granPremio_read(id_granpremio);
		List<ClasificacionCarrera> resultados = new ArrayList<>();
		List<Piloto> pilotos= manejadorPiloto.piloto_getAll();
		//crea una clasificacion por cada piloto
		for (Piloto piloto : pilotos) {
			ClasificacionCarrera n = new ClasificacionCarrera(0, null, piloto.getId_str());
			manejadorClasificacionCarrer.clasificacionCarrera_create(n);
			resultados.add(n);
		}
		//otorga valores de tiempo aleatorios para cada clasificacion
		resultados= this.tiemposAleatorios(resultados);
		//ordena de manera decendente los tiempos de carrera
		Collections.sort(resultados, new Comparator<ClasificacionCarrera>() {
			  public int compare(ClasificacionCarrera o1, ClasificacionCarrera o2) {
			      return o1.getTiempo().compareTo(o2.getTiempo());
			  }
			});
		//se otorgan los puntos segun clasificacion
		int puntos = 25;
		int loop = 1;
		List<String> id_clasificaciones= new ArrayList<String>();
		for (ClasificacionCarrera c : resultados) {
			c.setPuntaje(puntos);
			if(loop==1) {
				puntos=puntos-7;
			}
			if(loop==2) {
				puntos=puntos-3;
			}
			if(loop==3) {
				puntos=puntos-5;
			}
			if(loop==4) {
				puntos=puntos-2;
			}
			if(loop==5) {
				puntos=puntos-2;
			}
			if(loop==6) {
				puntos--;
			}
			if(loop==7) {
				puntos=puntos-2;
			}
			if(loop>7&&loop<11) {
				puntos--;
			}
			if(loop>=11) {
				puntos=0;
			}
			loop++;
			manejadorClasificacionCarrer.clasificacionCarrera_update(c);
			id_clasificaciones.add(c.getId_str());
		}
		carrera.setId_clasificaciones(id_clasificaciones);
		//se ejecutan las apuestas
		this.simularApuestas(id_granpremio, resultados.get(0).getId_str());
		Campeonato campeonato = 
		
	}
	private List<ClasificacionCarrera> tiemposAleatorios(List<ClasificacionCarrera>resultados){
		for (ClasificacionCarrera clasificacionCarrera : resultados) {
			GregorianCalendar t = new GregorianCalendar();
			t.set(Calendar.HOUR,1);
			t.set(Calendar.MINUTE,(int) (Math.random() * 59) + 1);
			t.set(Calendar.SECOND,(int) (Math.random() * 59) + 1);
			t.set(Calendar.MILLISECOND,(int) (Math.random() * 1000) + 1);
			clasificacionCarrera.setTiempo(t.getTime());
			manejadorClasificacionCarrer.clasificacionCarrera_update(clasificacionCarrera);
		}
		
		return resultados;
		
	}
	
}

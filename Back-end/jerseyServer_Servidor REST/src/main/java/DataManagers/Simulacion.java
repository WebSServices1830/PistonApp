package DataManagers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import co.edu.javeriana.ws.rest.clases.Apuesta;
import co.edu.javeriana.ws.rest.clases.Campeonato;
import co.edu.javeriana.ws.rest.clases.ClasificacionCampeonato;
import co.edu.javeriana.ws.rest.clases.ClasificacionCarrera;
import co.edu.javeriana.ws.rest.clases.GranPremio;
import co.edu.javeriana.ws.rest.clases.Piloto;
import co.edu.javeriana.ws.rest.clases.Usuario;


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
	
	public void simularGranPremio(String id_granpremio) {
		System.out.println("entre a la simulacion");
		GranPremio carrera = manejadorGranPremio.granPremio_read(id_granpremio);
		List<ClasificacionCarrera> resultados = new ArrayList<>();
		List<Piloto> pilotos= manejadorPiloto.piloto_getAll();
		//crea una clasificacion por cada piloto
		System.out.println("voy a crear una clasificacion por cada piloto");
		for (Piloto piloto : pilotos) {
			ClasificacionCarrera n = new ClasificacionCarrera(0, null, piloto.getId_str());
			manejadorClasificacionCarrer.clasificacionCarrera_create(n);
			System.out.println("estoy creando la clasificacion de: "+ piloto.getNombreCompleto());
			System.out.println("cree la clasificacion con id: "+n.getId_str());
			resultados.add(n);
		}
		
		//otorga valores de tiempo aleatorios para cada clasificacion
		System.out.println("voy a crear los tiempo aleatorios-------------------");
		this.tiemposAleatorios(resultados);
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
			this.actualizarPuntosCampeonato(id_granpremio, c.getCompetidor(), puntos);
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
			
			System.out.println("el piloto: "+ manejadorPiloto.piloto_get(c.getCompetidor()).getNombreCompleto()
					+ " tiene puntos: "+ c.getPuntaje());
			manejadorClasificacionCarrer.clasificacionCarrera_update(c,c.getId_str());
			id_clasificaciones.add(c.getId_str());
			
		}
		carrera.setId_clasificaciones(id_clasificaciones);
		manejadorGranPremio.granPremio_update_clasificaciones(carrera.getFecha(), id_clasificaciones);
		//se ejecutan las apuestas
		//this.simularApuestas(id_granpremio, resultados.get(0).getId_str());
		this.actualizarPosicionCampeonato(id_granpremio);
		
	}
	private void actualizarPosicionCampeonato(String id_granpremio) {
		Campeonato c = manejadorCampeonato.campeonato_read(manejadorGranPremio.granPremio_read(id_granpremio).getId_campeonato());
		List<String> id_calsificaciones = c.getClasificaciones();
		List<ClasificacionCampeonato> tabla = new ArrayList<>();
		for (String string : id_calsificaciones) {
			ClasificacionCampeonato clasificacion = manejadorClasificacionCampeonato.clasificacionCampeonato_get(string);
			tabla.add(clasificacion);
		}
		Collections.sort(tabla, new Comparator<ClasificacionCampeonato>() {
			  public int compare(ClasificacionCampeonato o1, ClasificacionCampeonato o2) {
			      return o1.getPuntaje()-o2.getPuntaje();
			  }
			});
		for (ClasificacionCampeonato clasificacionCampeonato : tabla) {
			clasificacionCampeonato.setPosicion(tabla.indexOf(clasificacionCampeonato));
			manejadorClasificacionCampeonato.clasificacionCampeonato_update(clasificacionCampeonato, clasificacionCampeonato.getId_str());
		}
		
	}
	private void actualizarPuntosCampeonato(String id_granpremio, String piloto,int puntos) {
		Campeonato c = manejadorCampeonato.campeonato_read(manejadorGranPremio.granPremio_read(id_granpremio).getId_campeonato());
		List<String> id_calsificaciones = c.getClasificaciones();
		for (String string : id_calsificaciones) {
			ClasificacionCampeonato clasificacion = manejadorClasificacionCampeonato.clasificacionCampeonato_get(string);
			if(clasificacion.getPiloto().equals(piloto)) {
				clasificacion.setPuntaje(puntos+clasificacion.getPuntaje());
				manejadorClasificacionCampeonato.clasificacionCampeonato_update(clasificacion, clasificacion.getId_str());
				break;
			}
		}
	}
	private List<ClasificacionCarrera> tiemposAleatorios(List<ClasificacionCarrera>resultados){
		for (ClasificacionCarrera clasificacionCarrera : resultados) {
			GregorianCalendar t = new GregorianCalendar();
			t.set(Calendar.HOUR,1);
			t.set(Calendar.MINUTE,(int) (Math.random() * 59) + 1);
			t.set(Calendar.SECOND,(int) (Math.random() * 59) + 1);
			t.set(Calendar.MILLISECOND,(int) (Math.random() * 1000) + 1);
			clasificacionCarrera.setTiempo(t.getTime());
			System.out.println("la claisificacion de:" + manejadorPiloto.piloto_get(clasificacionCarrera.getCompetidor()).getNombreCompleto()
								+"es de: " +clasificacionCarrera.getTiempo());
			manejadorClasificacionCarrer.clasificacionCarrera_update(clasificacionCarrera, clasificacionCarrera.getId_str());
		}
		
		return resultados;
		
	}
	

}

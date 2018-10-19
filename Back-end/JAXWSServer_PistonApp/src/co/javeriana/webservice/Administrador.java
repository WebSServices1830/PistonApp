package co.javeriana.webservice;

import static com.mongodb.client.model.Updates.set;

import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Auto;
import CRUDs.CRUD_ClasificacionCampeonato;
import CRUDs.CRUD_ClasificacionCarrera;
import CRUDs.CRUD_Escuderia;
import clases_negocio.Auto;
import clases_negocio.ClasificacionCampeonato;
import clases_negocio.ClasificacionCarrera;
import clases_negocio.Escuderia;
import clases_negocio.Motor;

@WebService(name="admin")
public class Administrador {
	
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	CRUD_ClasificacionCarrera manejadorClasificacion_Carrera = new CRUD_ClasificacionCarrera();
	CRUD_Auto manejadorAuto = new CRUD_Auto();
	CRUD_Escuderia manejadorEscuderia= new CRUD_Escuderia();
	
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
	
	@WebMethod
	public void registrarAuto(
			@WebParam(name = "nombre") String nombre,
			@WebParam(name = "pesoEnKg") double pesoEnKg,
			@WebParam(name = "ruedas") String ruedas,
			@WebParam(name = "combustible") String combustible,
			@WebParam(name = "foto_ref") String foto_ref,
			@WebParam(name = "referencia") String referencia,
			@WebParam(name = "cilindraje") String cilindraje,
			@WebParam(name = "configuracion") String configuracion,
			@WebParam(name = "turbo") boolean turbo) {
		Motor motor= new Motor(referencia, cilindraje, configuracion, turbo);
		Auto auto= new Auto(nombre, pesoEnKg, ruedas, combustible, foto_ref, motor);
		manejadorAuto.auto_create(auto);
	}
	
	@WebMethod
	public void actualizarAuto(
			@WebParam(name = "id") String id,
			@WebParam(name = "nombre") String nombre,
			@WebParam(name = "pesoEnKg") double pesoEnKg,
			@WebParam(name = "ruedas") String ruedas,
			@WebParam(name = "combustible") String combustible,
			@WebParam(name = "foto_ref") String foto_ref,
			@WebParam(name = "referencia") String referencia,
			@WebParam(name = "cilindraje") String cilindraje,
			@WebParam(name = "configuracion") String configuracion,
			@WebParam(name = "turbo") boolean turbo){ 
		Auto auto= manejadorAuto.auto_get(id);
		auto.setNombre(nombre);
		auto.setPesoEnKg(pesoEnKg);
		auto.setRuedas(ruedas);
		auto.setCombustible(combustible);
		auto.setFoto_ref(foto_ref);
		Motor motor= new Motor(referencia, cilindraje, configuracion, turbo);
		auto.setMotor(motor);
		manejadorAuto.auto_update(auto);
	}
	
	@WebMethod
	public void eliminarAuto(@WebParam(name = "id") String id) {
		manejadorAuto.auto_delete(id);
	} 	
	
	@WebMethod
	public void registrarEscuderia(
			@WebParam(name = "nombre") String nombre,
			@WebParam(name = "lugarBase") String lugarBase,
			@WebParam(name = "jefeTecnico") String jefeTecnico,
			@WebParam(name = "jefeEquipo") String jefeEquipo,
			@WebParam(name = "chasis") String chasis,
			@WebParam(name = "cant_vecesEnPodio") int cant_vecesEnPodio,
			@WebParam(name = "cant_TitulosCampeonato") int cant_TitulosCampeonato,
			@WebParam(name = "fotoEscudo_ref") String fotoEscudo_ref) {
		Escuderia escuderia= new Escuderia(nombre, lugarBase, jefeTecnico, jefeEquipo, chasis, cant_vecesEnPodio, cant_TitulosCampeonato, fotoEscudo_ref);
		manejadorEscuderia.escuderia_create(escuderia);
	}
	
	@WebMethod
	public void actualizarEscuderia(
			@WebParam(name = "id") String id,
			@WebParam(name = "nombre") String nombre,
			@WebParam(name = "lugarBase") String lugarBase,
			@WebParam(name = "jefeTecnico") String jefeTecnico,
			@WebParam(name = "jefeEquipo") String jefeEquipo,
			@WebParam(name = "chasis") String chasis,
			@WebParam(name = "cant_vecesEnPodio") int cant_vecesEnPodio,
			@WebParam(name = "cant_TitulosCampeonato") int cant_TitulosCampeonato,
			@WebParam(name = "fotoEscudo_ref") String fotoEscudo_ref,
			@WebParam(name = "autos") List<String> autos,
			@WebParam(name = "pilotos") List<String> pilotos){ 
		Escuderia escuderia= manejadorEscuderia.escuderia_get(id);
		escuderia.setNombre(nombre);
		escuderia.setLugarBase(lugarBase);
		escuderia.setJefeTecnico(jefeTecnico);
		escuderia.setJefeEquipo(jefeEquipo);
		escuderia.setChasis(chasis);
		escuderia.setCant_vecesEnPodio(cant_vecesEnPodio);
		escuderia.setCant_TitulosCampeonato(cant_TitulosCampeonato);
		escuderia.setFotoEscudo_ref(fotoEscudo_ref);
		escuderia.setAutos(autos);
		escuderia.setPilotos(pilotos);
		manejadorEscuderia.escuderia_update(escuderia);
	}
	
	@WebMethod
	public void eliminarEscuderia(@WebParam(name = "id") String id) {
		manejadorEscuderia.escuderia_delete(id);
	} 	

}

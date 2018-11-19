package co.edu.javeriana.ws.rest;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import DataManagers.CRUD_Usuario;
import DataManagers.Simulacion;
import Deserializers.DateDeserializer;
import DataManagers.CRUD_Auto;
import DataManagers.CRUD_Apuesta;
import DataManagers.CRUD_Campeonato;
import DataManagers.CRUD_ClasificacionCampeonato;
import DataManagers.CRUD_ClasificacionCarrera;
import DataManagers.CRUD_Escuderia;
import DataManagers.CRUD_GranPremio;
import DataManagers.CRUD_Piloto;
import DataManagers.CRUD_Pista;

import clases_mongoDB.ManejadorImagenes;

import co.edu.javeriana.ws.rest.clases.Usuario;
import co.edu.javeriana.ws.rest.clases.Apuesta;
import co.edu.javeriana.ws.rest.clases.Auto;
import co.edu.javeriana.ws.rest.clases.Campeonato;
import co.edu.javeriana.ws.rest.clases.ClasificacionCampeonato;
import co.edu.javeriana.ws.rest.clases.ClasificacionCarrera;
import co.edu.javeriana.ws.rest.clases.Escuderia;
import co.edu.javeriana.ws.rest.clases.GranPremio;
import co.edu.javeriana.ws.rest.clases.Motor;
import co.edu.javeriana.ws.rest.clases.Piloto;
import co.edu.javeriana.ws.rest.clases.Pista;
import co.edu.javeriana.ws.rest.clases.Record;
import co.edu.javeriana.ws.rest.clases.SerieFibonacci;

@Path("/PistonApp")
public class MyResource {

	CRUD_Usuario manejadorUsuario = new CRUD_Usuario();
	CRUD_Piloto manejadorPiloto = new CRUD_Piloto();
	CRUD_Campeonato manejadorCampeonato = new CRUD_Campeonato();
	CRUD_ClasificacionCampeonato manejadorClasificacion_Campeonato = new CRUD_ClasificacionCampeonato();
	CRUD_ClasificacionCarrera manejadorClasificacion_Carrera = new CRUD_ClasificacionCarrera();
	CRUD_Pista manejadorPista = new CRUD_Pista();
	CRUD_GranPremio manejadorGranPremio = new CRUD_GranPremio();
	CRUD_Auto manejadorAuto = new CRUD_Auto();
	CRUD_Escuderia manejadorEscuderia = new CRUD_Escuderia();
	CRUD_Apuesta manejadorApuesta = new CRUD_Apuesta();
	Simulacion manejadorSimulacion = new Simulacion();

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	// @GET
	// @Produces(MediaType.TEXT_PLAIN)
	// public String getIt() {
	// return "Got it bro!";
	// }

	// http://localhost:8080/myapp/PistonApp/fibonacci/7
	@GET
	@Produces("application/json")
	@Path("/fibonacci/{numero}")
	public List<SerieFibonacci> getString(@PathParam("numero") String numero) {

		List<SerieFibonacci> retorno = new ArrayList<SerieFibonacci>();

		SerieFibonacci sf1 = new SerieFibonacci(1, 1);
		retorno.add(sf1);
		SerieFibonacci sf2 = new SerieFibonacci(2, 1);
		retorno.add(sf2);

		for (int i = 2; i < Integer.parseInt(numero); i++) {
			SerieFibonacci sf = new SerieFibonacci(i, retorno.get(i - 1).getValor() + retorno.get(i - 2).getValor());
			retorno.add(sf);
		}

		return retorno;
	}

	// http://localhost:8080/myapp/PistonApp/granPremio/5be0cc5e8f2dfa4c6467b6fe/simulacion
	@POST
	@Path("/granPremio/{idGranpremio}/simulacion")
	@Consumes("application/json")
	public ResponseBuilder simulacion(@PathParam("idGranpremio") String idGranpremio) {
		manejadorSimulacion.simularGranPremio(idGranpremio);
		return Response.status(200);
	}
	
	// http://localhost:8080/myapp/PistonApp/casino
	@POST
	@Path("/casino")
	@Consumes("application/json")
	public ResponseBuilder apostar(Apuesta apuesta) {

		Usuario u = manejadorUsuario.usuario_get(apuesta.getUsuario());

		if (apuesta != null) {
			if (u.getBolsillo() - apuesta.getMonto() >= 0) {
				manejadorApuesta.apuesta_create(apuesta);
				manejadorUsuario.usuario_update_bolsillo(u.getId_str(), u.getBolsillo() - apuesta.getMonto());
				return Response.status(200);
			}
		}
		return Response.status(404);
	}
	// @GET
	// @Produces({ "application/xml", "application/json" })
	// @Path("/apuestas")
	// public List<Apuesta> consultarApuestas() {
	// return manejadorApuesta.pista_getAll();
	// }

	// USUARIOS---------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp/usuarios
	@POST
	@Path("/usuarios")
	@Consumes("application/json")
	public ResponseBuilder registrarUsuario(String jsonAsString) {

		/*
		 * Transferir los datos y lo que espera consumir el servicio es JSON. Así que lo
		 * recibo como una cadena y con Gson lo convierto a un objeto.
		 * 
		 */

		System.out.println(jsonAsString);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Gson gson = gsonBuilder.create();
		JsonObject job = gson.fromJson(jsonAsString, JsonObject.class);
		Usuario data = gson.fromJson(job, Usuario.class);

		System.out.println("Objeto:" + data.getNombreUsuario() + "," + data.getFechaNacimiento().toString());

		boolean yaExisteUsuario = manejadorUsuario.existeUsuario(data.getNombreUsuario());

		if (!yaExisteUsuario) {
			manejadorUsuario.usuario_create(data);
			return Response.status(200);
		}

		return Response.status(409);

	}

	// http://localhost:8080/myapp/PistonApp/usuarios
	/*
	 * @POST
	 * 
	 * @Path("/usuarios")
	 * 
	 * @Consumes("application/json") public ResponseBuilder registrarUsuario(Usuario
	 * usuario) { System.out.println("USUARIO: " + usuario.toString()); boolean
	 * yaExisteUsuario = manejadorUsuario.existeUsuario(usuario.getNombreUsuario());
	 * 
	 * if (!yaExisteUsuario) { manejadorUsuario.usuario_create(usuario); return
	 * Response.status(200); }
	 * 
	 * return Response.status(409); }
	 */

	// http://localhost:8080/myapp/PistonApp/usuarios/{insertar nombre usuario}
	@GET
	@Produces("application/json")
	@Path("/usuarios/{nombreUsuario}")
	public Usuario obteneUsuarioPorNombre(@PathParam("nombreUsuario") String nombreUsuario) {
		try {
			Usuario u = manejadorUsuario.usuario_getByName(nombreUsuario);
			return u;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	//// PRUEBA ////

	// http://localhost:8080/myapp/PistonApp/usuarios/prueba
	// @POST
	// @Path("/usuarios/prueba")
	// @Consumes(MediaType.APPLICATION_JSON)
	// public ResponseBuilder pruebaMetodos(String string) {
	//
	// Gson gson = new Gson();
	// JsonObject job = gson.fromJson(string, JsonObject.class);
	// JsonObject ovl = job.getAsJsonObject("user");
	// Usuario data = new Gson().fromJson(ovl, Usuario.class);
	//
	// System.out.println("Bien?" + data.getNombreUsuario() +
	// data.getNombreUsuario());
	//
	// return Response.status(200);
	// }

	//// PRUEBA ////

	// http://localhost:8080/myapp/PistonApp/usuarios/{insertar nombre usuario}
	@DELETE
	@Path("/usuarios/{nombreUsuario}")
	public ResponseBuilder eliminarUsuario(@PathParam("nombreUsuario") String nombreUsuario) {
		try {
			manejadorUsuario.usuario_deleteByName(nombreUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/pilotos/<id-piloto>
	@PUT
	@Path("/usuarios/{idUsuario}")
	@Consumes("application/json")
	public ResponseBuilder actualizarUsuario(@PathParam("idUsuario") String idUsuario, Usuario usuario) {
		try {
			manejadorUsuario.usuario_update(usuario, idUsuario);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// PILOTOS---------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp/pilotos
	@GET
	@Produces("application/json")
	@Path("/pilotos")
	public List<Piloto> consultarPilotos() {
		return manejadorPiloto.piloto_getAll();
	}

	// http://localhost:8080/myapp/PistonApp/pilotos/<nombrePiloto>
	@GET
	@Produces("application/json")
	@Path("/pilotos/{nombrePiloto}")
	public List<Piloto> consultarPilotosNombre(@PathParam("nombrePiloto") String nombre) {
		return manejadorPiloto.piloto_getAllBySearchParameter(nombre);
	}

	// http://localhost:8080/myapp/PistonApp/pilotos/5bd7288b7cb2bc1e3c10d43e
	@DELETE
	@Path("/pilotos/{idPiloto}")
	public ResponseBuilder eliminarPiloto(@PathParam("idPiloto") String idPiloto) {
		try {
			manejadorPiloto.piloto_delete(idPiloto);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/pilotos/<id-piloto>
	@PUT
	@Path("/pilotos/{idPiloto}")
	@Consumes("application/json")
	public ResponseBuilder actualizarPiloto(@PathParam("idPiloto") String idPiloto, Piloto piloto) {
		try {
			manejadorPiloto.piloto_update(piloto, idPiloto);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/pilotos
	@POST
	@Path("/pilotos")
	@Consumes("application/json")
	public ResponseBuilder crearPiloto(Piloto piloto) {
		try {
			manejadorPiloto.piloto_create(piloto);
			Escuderia escuderiaAsociada = manejadorEscuderia.escuderia_get(piloto.getId_escuderia());
			System.out.println(escuderiaAsociada.toString());
			escuderiaAsociada.getPilotos().add(piloto.getId_str());
			manejadorEscuderia.escuderia_update(escuderiaAsociada, escuderiaAsociada.getId_str());
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}

		return Response.status(200);
	}

	// ESCUDERIA------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp/escuderias
	@GET
	@Produces("application/json")
	@Path("/escuderias")
	public List<Escuderia> consultarEscuderias() {
		return manejadorEscuderia.escuderia_getAll();
	}

	// http://localhost:8080/myapp/PistonApp/escuderias/<nombreEscuderia>
	@GET
	@Produces("application/json")
	@Path("/escuderias/{nombreEscuderia}")
	public List<Escuderia> consultarEscuderiasNombre(@PathParam("nombreEscuderia") String nombreEscuderia) {
		return manejadorEscuderia.escuderia_getAllBySearchParameter(nombreEscuderia);
	}

	// http://localhost:8080/myapp/PistonApp/escuderias/<id-escuderia>
	@DELETE
	@Path("/escuderias/{idEscuderia}")
	public ResponseBuilder eliminarEscuderia(@PathParam("idEscuderia") String idEscuderia) {
		try {
			manejadorEscuderia.escuderia_deleteByName(idEscuderia);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/escuderias/<id-escuderia>
	@PUT
	@Path("/escuderias/{idEscuderia}")
	@Consumes("application/json")
	public ResponseBuilder actualizarEscuderia(@PathParam("idEscuderia") String idEscuderia, Escuderia escuderia) {
		try {
			manejadorEscuderia.escuderia_update(escuderia, idEscuderia);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/escuderias
	@POST
	@Path("/escuderias")
	@Consumes("application/json")
	public ResponseBuilder crearEscuderia(Escuderia escuderia) {

		manejadorEscuderia.escuderia_create(escuderia);

		return Response.status(200);

	}

	// AUTOS---------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp/autos
	@GET
	@Produces("application/json")
	@Path("/autos")
	public List<Auto> consultarAutos() {
		return manejadorAuto.auto_getAll();
	}

	// http://localhost:8080/myapp/PistonApp/autos/5bd7288b7cb2bc1e3c10d43e
	@DELETE
	@Path("/autos/{idAuto}")
	public ResponseBuilder eliminarAuto(@PathParam("idAuto") String idAuto) {
		try {
			manejadorAuto.auto_delete(idAuto);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/autos/<idAuto>
	@PUT
	@Path("/autos/{idAuto}")
	@Consumes("application/json")
	public ResponseBuilder actualizarAuto(@PathParam("idAuto") String idAuto, Auto auto) {
		try {
			manejadorAuto.auto_update(auto, idAuto);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/autos
	@POST
	@Consumes("application/json")
	@Path("/autos")
	public ResponseBuilder crearAuto(Auto auto) {
		manejadorAuto.auto_create(auto);
		return Response.status(200);
	}

	// PISTAS---------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp/pistas
	@GET
	@Produces("application/json")
	@Path("/pistas")
	public List<Pista> consultarPistas() {
		return manejadorPista.pista_getAll();
	}

	// http://localhost:8080/myapp/PistonApp/autos/5bd7288b7cb2bc1e3c10d43e
	@DELETE
	@Path("/pistas/{idPista}")
	public ResponseBuilder eliminarPista(@PathParam("idPista") String idPista) {
		try {
			manejadorPista.pista_delete(idPista);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/autos/<idAuto>
	@PUT
	@Path("/pistas/{idPista}")
	@Consumes("application/json")
	public ResponseBuilder actualizarPista(@PathParam("idPista") String idPista, Pista pista) {
		try {
			manejadorPista.pista_update(pista, idPista);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/pistas
	@POST
	@Consumes("application/json")
	@Path("/pistas")
	public ResponseBuilder crearPista(Pista pista) {
		manejadorPista.pista_create(pista);
		return Response.status(200);
	}

	// CLASIFICACION_CARRERA---------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp/clasificacionesCarrera
	@GET
	@Produces("application/json")
	@Path("/clasificacionesCarrera")
	public List<ClasificacionCarrera> consultarClasificacionCarrera() {
		return manejadorClasificacion_Carrera.clasificacionCarrera_getAll();
	}

	// http://localhost:8080/myapp/PistonApp/clasificacionesCarrera(<idClasificacionCarrera>
	@DELETE
	@Path("/clasificacionesCarrera/{idClasificacionCarrera}")
	public ResponseBuilder eliminarClasificacionCarrera(
			@PathParam("idClasificacionCarrera") String idClasificacionCarrera) {
		try {
			manejadorClasificacion_Carrera.clasificacionCarrera_delete(idClasificacionCarrera);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/clasificacionesCarrera/<idClasificacionCarrera>
	@PUT
	@Path("/clasificacionesCarrera/{idClasificacionCarrera}")
	@Consumes("application/json")
	public ResponseBuilder actualizarClasificacionCarrera(
			@PathParam("idClasificacionCarrera") String idClasificacionCarrera,
			ClasificacionCarrera clasificacionCarrera) {
		try {
			manejadorClasificacion_Carrera.clasificacionCarrera_update(clasificacionCarrera, idClasificacionCarrera);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/clasificacionesCarrera
	@POST
	@Consumes("application/json")
	@Path("/clasificacionesCarrera")
	public ResponseBuilder crearClasificacionCarrera(ClasificacionCarrera clasificacionCarrera) {
		manejadorClasificacion_Carrera.clasificacionCarrera_create(clasificacionCarrera);
		return Response.status(200);
	}

	// CLASIFICACION_CAMPEONATO---------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp/clasificacionesCarrera
	@GET
	@Produces("application/json")
	@Path("/clasificacionesCampeonato")
	public List<ClasificacionCampeonato> consultarClasificacionCampeonato() {
		return manejadorClasificacion_Campeonato.clasificacionCampeonato_getAll();
	}

	// http://localhost:8080/myapp/PistonApp/clasificacionesCampeonato(<idClasificacionCampeonato>
	@DELETE
	@Path("/clasificacionesCampeonato/{idClasificacionCampeonato}")
	public ResponseBuilder eliminarClasificacionCampeonato(
			@PathParam("idClasificacionCampeonato") String idClasificacionCampeonato) {
		try {
			manejadorClasificacion_Campeonato.clasificacionCampeonato_delete(idClasificacionCampeonato);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/clasificacionesCampeonato/<idClasificacionCampeonato>
	@PUT
	@Path("/clasificacionesCampeonato/{idClasificacionCampeonato}")
	@Consumes("application/json")
	public ResponseBuilder actualizarClasificacionCampeonato(
			@PathParam("idClasificacionCampeonato") String idClasificacionCampeonato,
			ClasificacionCampeonato clasificacionCampeonato) {
		try {
			manejadorClasificacion_Campeonato.clasificacionCampeonato_update(clasificacionCampeonato,
					idClasificacionCampeonato);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500);
		}
		return Response.status(200);
	}

	// http://localhost:8080/myapp/PistonApp/clasificacionesCampeonato
	@POST
	@Consumes("application/json")
	@Path("/clasificacionesCampeonato")
	public ResponseBuilder crearClasificacionCampeonato(ClasificacionCampeonato clasificacionCampeonato) {
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(clasificacionCampeonato);
		return Response.status(200);
	}

	// CAMPEONATO
	// http://localhost:8080/myapp/PistonApp/campeonatos
	@GET
	@Produces("application/json")
	@Path("/campeonatos")
	public List<Campeonato> consultarCampeonato() {
		return manejadorCampeonato.campeonato_readAll();
	}

	// GRAN PREMIO
	// http://localhost:8080/myapp/PistonApp/granPremios/{id_str}
	@GET
	@Produces("application/json")
	@Path("/granPremios/{id_campeonato}")
	public List<GranPremio> consultarGranPremios(@PathParam("id_campeonato") String id_campeonato) {
		return manejadorGranPremio.grandesPremios_X_Fecha(id_campeonato);
	}

	// PISTON APP------------------------------------------------------------------
	// http://localhost:8080/myapp/PistonApp
	@POST
	public ResponseBuilder cargarDatos() {
		// CAMPEONATO
		GregorianCalendar fechaInicio = new GregorianCalendar(2018, 0, 1);

		GregorianCalendar fechaFin = new GregorianCalendar(2018, 11, 31);

		Campeonato campeonato = manejadorCampeonato.campeonato_create("Campeonato 2018", fechaInicio.getTime(),
				fechaFin.getTime());
		GregorianCalendar Vuelta_P1 = new GregorianCalendar();
		GregorianCalendar Vuelta_P2 = new GregorianCalendar();
		GregorianCalendar Vuelta_P3 = new GregorianCalendar();
		GregorianCalendar Vuelta_P4 = new GregorianCalendar();
		GregorianCalendar Vuelta_P5 = new GregorianCalendar();
		GregorianCalendar Vuelta_P6 = new GregorianCalendar();
		GregorianCalendar Vuelta_P7 = new GregorianCalendar();
		GregorianCalendar Vuelta_P8 = new GregorianCalendar();
		GregorianCalendar Vuelta_P9 = new GregorianCalendar();
		GregorianCalendar Vuelta_P10 = new GregorianCalendar();
		GregorianCalendar Vuelta_P11 = new GregorianCalendar();
		GregorianCalendar Vuelta_P12 = new GregorianCalendar();
		GregorianCalendar Vuelta_P13 = new GregorianCalendar();
		GregorianCalendar Vuelta_P14 = new GregorianCalendar();
		GregorianCalendar Vuelta_P15 = new GregorianCalendar();
		GregorianCalendar Vuelta_P16 = new GregorianCalendar();
		GregorianCalendar Vuelta_P17 = new GregorianCalendar();
		GregorianCalendar Vuelta_P18 = new GregorianCalendar();
		GregorianCalendar Vuelta_P19 = new GregorianCalendar();
		GregorianCalendar Vuelta_P20 = new GregorianCalendar();

		// PILOTO 1
		GregorianCalendar fechaNacimiento_1 = new GregorianCalendar(1985, 0, 07);
		String fotoRefP1 = null;
		try {
			fotoRefP1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/LewisHamilton.jpg", "Lewis Hamilton");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Lewis = new Piloto("Lewis Hamilton", fechaNacimiento_1.getTime(), "Stevenage, England", fotoRefP1, 131,
				2941, 225);
		manejadorPiloto.piloto_create(Lewis);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(
				new ClasificacionCampeonato(383, 1, manejadorPiloto.piloto_getByName("Lewis Hamilton").getId_str()));

		// PILOTO 2
		GregorianCalendar fechaNacimiento_2 = new GregorianCalendar(1987, 06, 03);
		String fotoRefP2 = null;
		try {
			fotoRefP2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/sebastian.jpg", "Sebastian Vettel");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Sebastian = new Piloto("Sebastian Vettel", fechaNacimiento_2.getTime(), "Heppenheim, Germany", fotoRefP2,
				109, 2689, 216);
		manejadorPiloto.piloto_create(Sebastian);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(302, 2,
				manejadorPiloto.piloto_getByName(Sebastian.getNombreCompleto()).getId_str()));
		// PILOTO 3
		GregorianCalendar fechaNacimiento_3 = new GregorianCalendar(1989, 07, 28);
		String fotoRefP3 = null;
		try {
			fotoRefP3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Valtteri Bottas.jpg", "Valtteri Bottas");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Valtteri = new Piloto("Valtteri Bottas", fechaNacimiento_3.getTime(), "Nastola, Finland", fotoRefP3, 30,
				923, 115);
		manejadorPiloto.piloto_create(Valtteri);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(327, 4,
				manejadorPiloto.piloto_getByName(Valtteri.getNombreCompleto()).getId_str()));
		// PILOTO 4
		GregorianCalendar fechaNacimiento_4 = new GregorianCalendar(1979, 9, 17);
		String fotoRefP4 = null;
		try {
			fotoRefP4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Kimi Raikkonen.jpg", "Kimi Raikkonen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Kimi = new Piloto("Kimi Raikkonen", fechaNacimiento_4.getTime(), "Espoo, Finland", fotoRefP4, 100, 1761,
				290);
		manejadorPiloto.piloto_create(Kimi);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(251, 3,
				manejadorPiloto.piloto_getByName(Kimi.getNombreCompleto()).getId_str()));
		// PILOTO 5
		GregorianCalendar fechaNacimiento_5 = new GregorianCalendar(1997, 8, 30);
		String fotoRefP5 = null;
		try {
			fotoRefP5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Max Verstappen.jpg", "Max Verstappen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Max = new Piloto("Max Verstappen", fechaNacimiento_5.getTime(), "Hasselt, Belgium", fotoRefP5, 18, 594,
				77);
		manejadorPiloto.piloto_create(Max);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(234, 5,
				manejadorPiloto.piloto_getByName(Max.getNombreCompleto()).getId_str()));

		// PILOTO 6
		GregorianCalendar fechaNacimiento_6 = new GregorianCalendar(1989, 6, 01);
		String fotoRefP6 = null;
		try {
			fotoRefP6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Daniel Ricciardo.jpg", "Daniel Ricciardo");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Daniel = new Piloto("Daniel Ricciardo", fechaNacimiento_6.getTime(), "Perth, Australia", fotoRefP6, 29,
				962, 146);
		manejadorPiloto.piloto_create(Daniel);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(158, 6,
				manejadorPiloto.piloto_getByName(Daniel.getNombreCompleto()).getId_str()));
		// PILOTO 7
		GregorianCalendar fechaNacimiento_7 = new GregorianCalendar(1990, 0, 26);
		String fotoRefP7 = null;
		try {
			fotoRefP7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sergio Perez.jpg", "Sergio Perez");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Sergio = new Piloto("Sergio Perez", fechaNacimiento_7.getTime(), "Guadalajara, Mexico", fotoRefP7, 8,
				520, 153);

		manejadorPiloto.piloto_create(Sergio);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(58, 8,
				manejadorPiloto.piloto_getByName(Sergio.getNombreCompleto()).getId_str()));
		// PILOTO 8
		GregorianCalendar fechaNacimiento_8 = new GregorianCalendar(1992, 9, 5);
		String fotoRefP8 = null;
		try {
			fotoRefP8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Kevin Magnussen.jpg", "Kevin Magnussen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Kevin = new Piloto("Kevin Magnussen", fechaNacimiento_8.getTime(), "Roskilde, Denmark", fotoRefP8, 1,
				134, 78);
		manejadorPiloto.piloto_create(Kevin);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(55, 9,
				manejadorPiloto.piloto_getByName(Kevin.getNombreCompleto()).getId_str()));
		// PILOTO 9
		GregorianCalendar fechaNacimiento_9 = new GregorianCalendar(1987, 7, 19);
		String fotoRefP9 = null;
		try {
			fotoRefP9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Nico Hulkenberg.jpg", "Nico Hulkenberg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Nico = new Piloto("Nico Hulkenberg", fechaNacimiento_9.getTime(), "Emmerich am Rhein, Germany",
				fotoRefP9, 0, 458, 154);
		manejadorPiloto.piloto_create(Nico);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(69, 7,
				manejadorPiloto.piloto_getByName(Nico.getNombreCompleto()).getId_str()));

		// PILOTO 10
		GregorianCalendar fechaNacimiento_10 = new GregorianCalendar(1981, 6, 29);
		String fotoRefP10 = null;
		try {
			fotoRefP10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Fernando Alonso.jpg", "Fernando Alonso");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Fernando = new Piloto("Fernando Alonso", fechaNacimiento_10.getTime(), "Oviedo, Spain", fotoRefP10, 97,
				1899, 310);
		manejadorPiloto.piloto_create(Fernando);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(50, 10,
				manejadorPiloto.piloto_getByName(Fernando.getNombreCompleto()).getId_str()));
		// PILOTO 11
		GregorianCalendar fechaNacimiento_11 = new GregorianCalendar(1996, 8, 17);
		String fotoRefP11 = null;
		try {
			fotoRefP11 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Esteban Ocon.jpg", "Esteban Ocon");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Esteban = new Piloto("Esteban Ocon", fechaNacimiento_11.getTime(), "ï¿½vreux, Normandy", fotoRefP11, 0,
				136, 47);
		manejadorPiloto.piloto_create(Esteban);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(49, 11,
				manejadorPiloto.piloto_getByName(Esteban.getNombreCompleto()).getId_str()));
		// PILOTO 12
		GregorianCalendar fechaNacimiento_12 = new GregorianCalendar(1994, 8, 1);
		String fotoRefP12 = null;
		try {
			fotoRefP12 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Carlos Sainz.jpg", "Carlos Sainz");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Carlos = new Piloto("Carlos Sainz", fechaNacimiento_12.getTime(), "Madrid, Spain", fotoRefP12, 0, 157,
				78);
		manejadorPiloto.piloto_create(Carlos);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(45, 12,
				manejadorPiloto.piloto_getByName(Carlos.getNombreCompleto()).getId_str()));
		// PILOTO 13
		GregorianCalendar fechaNacimiento_13 = new GregorianCalendar(1986, 3, 17);
		String fotoRefP13 = null;
		try {
			fotoRefP13 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Romain Grosjean.jpg", "Romain Grosjean");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Romain = new Piloto("Romain Grosjean", fechaNacimiento_13.getTime(), "Geneva, Switzerland", fotoRefP13,
				10, 375, 142);
		manejadorPiloto.piloto_create(Romain);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(35, 13,
				manejadorPiloto.piloto_getByName(Romain.getNombreCompleto()).getId_str()));
		// PILOTO 14
		GregorianCalendar fechaNacimiento_14 = new GregorianCalendar(1996, 1, 7);
		String fotoRefP14 = null;
		try {
			fotoRefP14 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Pierre Gasly.jpg", "Pierre Gasly");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Pierre = new Piloto("Pierre Gasly", fechaNacimiento_14.getTime(), "Rouen, France", fotoRefP14, 0, 28,
				23);
		manejadorPiloto.piloto_create(Pierre);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(29, 15,
				manejadorPiloto.piloto_getByName(Pierre.getNombreCompleto()).getId_str()));
		// PILOTO 15
		GregorianCalendar fechaNacimiento_15 = new GregorianCalendar(1997, 9, 16);
		String fotoRefP15 = null;
		try {
			fotoRefP15 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Charles Leclerc.jpg", "Charles Leclerc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Charles = new Piloto("Charles Leclerc", fechaNacimiento_15.getTime(), "Monte Carlo, Monaco", fotoRefP15,
				0, 21, 18);
		manejadorPiloto.piloto_create(Charles);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(33, 14,
				manejadorPiloto.piloto_getByName(Charles.getNombreCompleto()).getId_str()));
		// PILOTO 16
		GregorianCalendar fechaNacimiento_16 = new GregorianCalendar(1992, 2, 26);
		String fotoRefP16 = null;
		try {
			fotoRefP16 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Stoffel Vandoorne.jpg", "Stoffel Vandoorne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Stoffel = new Piloto("Stoffel Vandoorne", fechaNacimiento_16.getTime(), "Kortrijk, Belgium", fotoRefP16,
				0, 22, 39);
		manejadorPiloto.piloto_create(Stoffel);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(12, 16,
				manejadorPiloto.piloto_getByName(Stoffel.getNombreCompleto()).getId_str()));
		// PILOTO 17
		GregorianCalendar fechaNacimiento_17 = new GregorianCalendar(1998, 9, 29);
		String fotoRefP17 = null;
		try {
			fotoRefP17 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Lance Stroll.jpg", "Lance Stroll");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Lance = new Piloto("Lance Stroll", fechaNacimiento_17.getTime(), "Montreal, Canada", fotoRefP17, 1, 46,
				38);
		manejadorPiloto.piloto_create(Lance);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(6, 18,
				manejadorPiloto.piloto_getByName(Lance.getNombreCompleto()).getId_str()));
		// PILOTO 18
		GregorianCalendar fechaNacimiento_18 = new GregorianCalendar(1990, 8, 2);
		String fotoRefP18 = null;
		try {
			fotoRefP18 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Marcus Ericsson.jpg", "Marcus Ericsson");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Marcus = new Piloto("Marcus Ericsson", fechaNacimiento_18.getTime(), "Kumla, Sweden", fotoRefP18, 0, 15,
				94);
		manejadorPiloto.piloto_create(Marcus);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(9, 17,
				manejadorPiloto.piloto_getByName(Marcus.getNombreCompleto()).getId_str()));
		// PILOTO 19
		GregorianCalendar fechaNacimiento_19 = new GregorianCalendar(1989, 10, 10);
		String fotoRefP19 = null;
		try {
			fotoRefP19 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Brendon Hartley.jpg", "Brendon Hartley");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Brendon = new Piloto("Brendon Hartley", fechaNacimiento_19.getTime(), "Palmerston North, New Zealand",
				fotoRefP19, 0, 2, 22);
		manejadorPiloto.piloto_create(Brendon);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(4, 19,
				manejadorPiloto.piloto_getByName(Brendon.getNombreCompleto()).getId_str()));
		// PILOTO 20
		GregorianCalendar fechaNacimiento_20 = new GregorianCalendar(1995, 07, 25);
		String fotoRefP20 = null;
		try {
			fotoRefP20 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sergey Sirotkin.jpg", "Sergey Sirotkin");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Piloto Sergey = new Piloto("Sergey Sirotkin", fechaNacimiento_20.getTime(), "Moscow, Russia", fotoRefP20, 0, 1,
				18);
		manejadorPiloto.piloto_create(Sergey);
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(1, 20,
				manejadorPiloto.piloto_getByName(Sergey.getNombreCompleto()).getId_str()));

		ClasificacionCarrera CCP1 = new ClasificacionCarrera();
		ClasificacionCarrera CCP2 = new ClasificacionCarrera();
		ClasificacionCarrera CCP3 = new ClasificacionCarrera();
		ClasificacionCarrera CCP4 = new ClasificacionCarrera();
		ClasificacionCarrera CCP5 = new ClasificacionCarrera();
		ClasificacionCarrera CCP6 = new ClasificacionCarrera();
		ClasificacionCarrera CCP7 = new ClasificacionCarrera();
		ClasificacionCarrera CCP8 = new ClasificacionCarrera();
		ClasificacionCarrera CCP9 = new ClasificacionCarrera();
		ClasificacionCarrera CCP10 = new ClasificacionCarrera();
		ClasificacionCarrera CCP11 = new ClasificacionCarrera();
		ClasificacionCarrera CCP12 = new ClasificacionCarrera();
		ClasificacionCarrera CCP13 = new ClasificacionCarrera();
		ClasificacionCarrera CCP14 = new ClasificacionCarrera();
		ClasificacionCarrera CCP15 = new ClasificacionCarrera();
		ClasificacionCarrera CCP16 = new ClasificacionCarrera();
		ClasificacionCarrera CCP17 = new ClasificacionCarrera();
		ClasificacionCarrera CCP18 = new ClasificacionCarrera();
		ClasificacionCarrera CCP19 = new ClasificacionCarrera();
		ClasificacionCarrera CCP20 = new ClasificacionCarrera();

		// GRAN PREMIO 1
		GregorianCalendar fechaGranPremio_1 = new GregorianCalendar(2018, 2, 25);

		GregorianCalendar mejorVuelta_1 = new GregorianCalendar();
		mejorVuelta_1.set(Calendar.HOUR, 1);
		mejorVuelta_1.set(Calendar.MINUTE, 29);
		mejorVuelta_1.set(Calendar.SECOND, 33);
		mejorVuelta_1.set(Calendar.MILLISECOND, 283);

		// PISTA 1
		GregorianCalendar recordVuelta_1 = new GregorianCalendar();

		recordVuelta_1.set(Calendar.MINUTE, 1);
		recordVuelta_1.set(Calendar.SECOND, 24);
		recordVuelta_1.set(Calendar.MILLISECOND, 125);
		Record record_1 = new Record(recordVuelta_1.getTime(), "Michael Schumacher", 2004);
		String fotoRef_1 = null;
		try {
			fotoRef_1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/australiaGrandPrix.png", "Melbourne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_1 != null) {
			Pista pista = manejadorPista.pista_create("Melbourne", fotoRef_1, "Sebastian Vettel", (float) 307.57,
					(float) 5.303, record_1);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 29);
			Vuelta_P1.set(Calendar.SECOND, 33);
			Vuelta_P1.set(Calendar.MILLISECOND, 283);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Sebastian.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 29);
			Vuelta_P2.set(Calendar.SECOND, 38);
			Vuelta_P2.set(Calendar.MILLISECOND, 319);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Lewis.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 29);
			Vuelta_P3.set(Calendar.SECOND, 39);
			Vuelta_P3.set(Calendar.MILLISECOND, 592);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 29);
			Vuelta_P4.set(Calendar.SECOND, 40);
			Vuelta_P4.set(Calendar.MILLISECOND, 352);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Daniel.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 29);
			Vuelta_P5.set(Calendar.SECOND, 61);
			Vuelta_P5.set(Calendar.MILLISECOND, 169);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Fernando.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 29);
			Vuelta_P6.set(Calendar.SECOND, 62);
			Vuelta_P6.set(Calendar.MILLISECOND, 228);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Max.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 30);
			Vuelta_P7.set(Calendar.SECOND, 5);
			Vuelta_P7.set(Calendar.MILLISECOND, 954);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Nico.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 30);
			Vuelta_P8.set(Calendar.SECOND, 7);
			Vuelta_P8.set(Calendar.MILLISECOND, 622);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Valtteri.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 30);
			Vuelta_P9.set(Calendar.SECOND, 8);
			Vuelta_P9.set(Calendar.MILLISECOND, 204);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Stoffel.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 30);
			Vuelta_P10.set(Calendar.SECOND, 19);
			Vuelta_P10.set(Calendar.MILLISECOND, 005);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Carlos.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 30);
			Vuelta_P11.set(Calendar.SECOND, 20);
			Vuelta_P11.set(Calendar.MILLISECOND, 100);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Sergio.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 30);
			Vuelta_P12.set(Calendar.SECOND, 33);
			Vuelta_P12.set(Calendar.MILLISECOND, 561);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Esteban.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 30);
			Vuelta_P13.set(Calendar.SECOND, 49);
			Vuelta_P13.set(Calendar.MILLISECOND, 042);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Charles.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 30);
			Vuelta_P14.set(Calendar.SECOND, 51);
			Vuelta_P14.set(Calendar.MILLISECOND, 571);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Lance.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 30);
			Vuelta_P15.set(Calendar.SECOND, 57);
			Vuelta_P15.set(Calendar.MILLISECOND, 408);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Brendon.getId_str());

			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
		
			
			
			
			List<String> CCGPAustralia = new ArrayList<String>();
			CCGPAustralia.add(CCP1.getId_str());
			CCGPAustralia.add(CCP2.getId_str());
			CCGPAustralia.add(CCP3.getId_str());
			CCGPAustralia.add(CCP4.getId_str());
			CCGPAustralia.add(CCP5.getId_str());
			CCGPAustralia.add(CCP6.getId_str());
			CCGPAustralia.add(CCP7.getId_str());
			CCGPAustralia.add(CCP8.getId_str());
			CCGPAustralia.add(CCP9.getId_str());
			CCGPAustralia.add(CCP10.getId_str());
			CCGPAustralia.add(CCP11.getId_str());
			CCGPAustralia.add(CCP12.getId_str());
			CCGPAustralia.add(CCP13.getId_str());
			CCGPAustralia.add(CCP14.getId_str());
			CCGPAustralia.add(CCP15.getId_str());

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_1.getTime(), 58,
					mejorVuelta_1.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_1.getTime(), CCGPAustralia);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 2

		GregorianCalendar fechaGranPremio_2 = new GregorianCalendar(2018, 3, 8);

		GregorianCalendar mejorVuelta_2 = new GregorianCalendar();
		mejorVuelta_2.set(Calendar.HOUR, 1);
		mejorVuelta_2.set(Calendar.MINUTE, 32);
		mejorVuelta_2.set(Calendar.SECOND, 1);
		mejorVuelta_2.set(Calendar.MILLISECOND, 940);

		// PISTA 2
		GregorianCalendar recordVuelta_2 = new GregorianCalendar();

		recordVuelta_2.set(Calendar.MINUTE, 1);
		recordVuelta_2.set(Calendar.SECOND, 31);
		recordVuelta_2.set(Calendar.MILLISECOND, 447);
		Record record_2 = new Record(recordVuelta_2.getTime(), "Pedro de la Rosa", 2005);
		String fotoRef_2 = null;
		try {
			fotoRef_2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BahrainGrandPrix.png", "Bahrain");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_2 != null) {
			Pista pista = manejadorPista.pista_create("Bahrain", fotoRef_2, "Sebastian Vettel", (float) 308.23,
					(float) 5.412, record_2);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 32);
			Vuelta_P1.set(Calendar.SECOND, 1);
			Vuelta_P1.set(Calendar.MILLISECOND, 940);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Sebastian.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 32);
			Vuelta_P2.set(Calendar.SECOND, 2);
			Vuelta_P2.set(Calendar.MILLISECOND, 639);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Valtteri.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 32);
			Vuelta_P3.set(Calendar.SECOND, 9);
			Vuelta_P3.set(Calendar.MILLISECOND, 452);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Lewis.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 33);
			Vuelta_P4.set(Calendar.SECOND, 4);
			Vuelta_P4.set(Calendar.MILLISECOND, 174);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Pierre.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 33);
			Vuelta_P5.set(Calendar.SECOND, 17);
			Vuelta_P5.set(Calendar.MILLISECOND, 986);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Kevin.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 33);
			Vuelta_P6.set(Calendar.SECOND, 40);
			Vuelta_P6.set(Calendar.MILLISECOND, 228);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Nico.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 33);
			Vuelta_P7.set(Calendar.SECOND, 45);
			Vuelta_P7.set(Calendar.MILLISECOND, 500);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Fernando.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 33);
			Vuelta_P8.set(Calendar.SECOND, 50);
			Vuelta_P8.set(Calendar.MILLISECOND, 500);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Stoffel.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 33);
			Vuelta_P9.set(Calendar.SECOND, 55);
			Vuelta_P9.set(Calendar.MILLISECOND, 0);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Marcus.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 34);
			Vuelta_P10.set(Calendar.SECOND, 1);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Esteban.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 34);
			Vuelta_P11.set(Calendar.SECOND, 6);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Carlos.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 34);
			Vuelta_P12.set(Calendar.SECOND, 11);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Charles.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 34);
			Vuelta_P13.set(Calendar.SECOND, 16);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Romain.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 34);
			Vuelta_P14.set(Calendar.SECOND, 21);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Lance.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 34);
			Vuelta_P15.set(Calendar.SECOND, 26);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Sergey.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 34);
			Vuelta_P16.set(Calendar.SECOND, 31);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Sergio.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 34);
			Vuelta_P17.set(Calendar.SECOND, 36);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Brendon.getId_str());

			List<String> CCGPBahrain = new ArrayList<String>();
			CCGPBahrain.add(CCP1.getId_str());
			CCGPBahrain.add(CCP2.getId_str());
			CCGPBahrain.add(CCP3.getId_str());
			CCGPBahrain.add(CCP4.getId_str());
			CCGPBahrain.add(CCP5.getId_str());
			CCGPBahrain.add(CCP6.getId_str());
			CCGPBahrain.add(CCP7.getId_str());
			CCGPBahrain.add(CCP8.getId_str());
			CCGPBahrain.add(CCP9.getId_str());
			CCGPBahrain.add(CCP10.getId_str());
			CCGPBahrain.add(CCP11.getId_str());
			CCGPBahrain.add(CCP12.getId_str());
			CCGPBahrain.add(CCP13.getId_str());
			CCGPBahrain.add(CCP14.getId_str());
			CCGPBahrain.add(CCP15.getId_str());
			CCGPBahrain.add(CCP16.getId_str());
			CCGPBahrain.add(CCP17.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);
	

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_2.getTime(), 57,
			mejorVuelta_2.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_2.getTime(), CCGPBahrain);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 3

		GregorianCalendar fechaGranPremio_3 = new GregorianCalendar(2018, 3, 15);

		GregorianCalendar mejorVuelta_3 = new GregorianCalendar();
		mejorVuelta_3.set(Calendar.HOUR, 1);
		mejorVuelta_3.set(Calendar.MINUTE, 35);
		mejorVuelta_3.set(Calendar.SECOND, 36);
		mejorVuelta_3.set(Calendar.MILLISECOND, 380);

		// PISTA 3
		GregorianCalendar recordVuelta_3 = new GregorianCalendar();

		recordVuelta_3.set(Calendar.MINUTE, 1);
		recordVuelta_3.set(Calendar.SECOND, 32);
		recordVuelta_3.set(Calendar.MILLISECOND, 238);
		Record record_3 = new Record(recordVuelta_3.getTime(), "Michael Schumacher", 2004);
		String fotoRef_3 = null;
		try {
			fotoRef_3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ShangaiGrandPrix.png", "Shangai");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_3 != null) {
			Pista pista = manejadorPista.pista_create("Shangai", fotoRef_3, "Daniel Ricciardo", (float) 305.066,
					(float) 5.451, record_3);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 35);
			Vuelta_P1.set(Calendar.SECOND, 36);
			Vuelta_P1.set(Calendar.MILLISECOND, 380);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Daniel.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 35);
			Vuelta_P2.set(Calendar.SECOND, 45);
			Vuelta_P2.set(Calendar.MILLISECOND, 274);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Valtteri.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 35);
			Vuelta_P3.set(Calendar.SECOND, 46);
			Vuelta_P3.set(Calendar.MILLISECOND, 017);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 35);
			Vuelta_P4.set(Calendar.SECOND, 53);
			Vuelta_P4.set(Calendar.MILLISECOND, 365);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Lewis.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 35);
			Vuelta_P5.set(Calendar.SECOND, 56);
			Vuelta_P5.set(Calendar.MILLISECOND, 816);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Max.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 35);
			Vuelta_P6.set(Calendar.SECOND, 57);
			Vuelta_P6.set(Calendar.MILLISECOND, 432);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Nico.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 36);
			Vuelta_P7.set(Calendar.SECOND, 7);
			Vuelta_P7.set(Calendar.MILLISECOND, 19);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Fernando.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 36);
			Vuelta_P8.set(Calendar.SECOND, 12);
			Vuelta_P8.set(Calendar.MILLISECOND, 666);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Sebastian.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 36);
			Vuelta_P9.set(Calendar.SECOND, 16);
			Vuelta_P9.set(Calendar.MILLISECOND, 974);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Carlos.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 36);
			Vuelta_P10.set(Calendar.SECOND, 21);
			Vuelta_P10.set(Calendar.MILLISECOND, 430);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Kevin.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 36);
			Vuelta_P11.set(Calendar.SECOND, 22);
			Vuelta_P11.set(Calendar.MILLISECOND, 100);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Esteban.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 36);
			Vuelta_P12.set(Calendar.SECOND, 22);
			Vuelta_P12.set(Calendar.MILLISECOND, 19);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Sergio.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 36);
			Vuelta_P13.set(Calendar.SECOND, 25);
			Vuelta_P13.set(Calendar.MILLISECOND, 753);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Stoffel.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 36);
			Vuelta_P14.set(Calendar.SECOND, 31);
			Vuelta_P14.set(Calendar.MILLISECOND, 870);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Lance.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 36);
			Vuelta_P15.set(Calendar.SECOND, 34);
			Vuelta_P15.set(Calendar.MILLISECOND, 621);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Sergey.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 36);
			Vuelta_P16.set(Calendar.SECOND, 38);
			Vuelta_P16.set(Calendar.MILLISECOND, 984);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Marcus.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 36);
			Vuelta_P17.set(Calendar.SECOND, 41);
			Vuelta_P17.set(Calendar.MILLISECOND, 676);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Romain.getId_str());

			Vuelta_P18.set(Calendar.HOUR, 1);
			Vuelta_P18.set(Calendar.MINUTE, 36);
			Vuelta_P18.set(Calendar.SECOND, 42);
			Vuelta_P18.set(Calendar.MILLISECOND, 710);
			CCP18 = new ClasificacionCarrera(0, Vuelta_P18.getTime(), Pierre.getId_str());

			Vuelta_P19.set(Calendar.HOUR, 1);
			Vuelta_P19.set(Calendar.MINUTE, 36);
			Vuelta_P19.set(Calendar.SECOND, 58);
			Vuelta_P19.set(Calendar.MILLISECOND, 955);
			CCP19 = new ClasificacionCarrera(0, Vuelta_P19.getTime(), Charles.getId_str());

			Vuelta_P20.set(Calendar.HOUR, 1);
			Vuelta_P20.set(Calendar.MINUTE, 36);
			Vuelta_P20.set(Calendar.SECOND, 59);
			Vuelta_P20.set(Calendar.MILLISECOND, 0);
			CCP20 = new ClasificacionCarrera(0, Vuelta_P20.getTime(), Brendon.getId_str());

			List<String> CCGPShangai = new ArrayList<String>();
			CCGPShangai.add(CCP1.getId_str());
			CCGPShangai.add(CCP2.getId_str());
			CCGPShangai.add(CCP3.getId_str());
			CCGPShangai.add(CCP4.getId_str());
			CCGPShangai.add(CCP5.getId_str());
			CCGPShangai.add(CCP6.getId_str());
			CCGPShangai.add(CCP7.getId_str());
			CCGPShangai.add(CCP8.getId_str());
			CCGPShangai.add(CCP9.getId_str());
			CCGPShangai.add(CCP10.getId_str());
			CCGPShangai.add(CCP11.getId_str());
			CCGPShangai.add(CCP12.getId_str());
			CCGPShangai.add(CCP13.getId_str());
			CCGPShangai.add(CCP14.getId_str());
			CCGPShangai.add(CCP15.getId_str());
			CCGPShangai.add(CCP16.getId_str());
			CCGPShangai.add(CCP17.getId_str());
			CCGPShangai.add(CCP18.getId_str());
			CCGPShangai.add(CCP19.getId_str());
			CCGPShangai.add(CCP20.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP18);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP19);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP20);

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_3.getTime(), 56,
					mejorVuelta_3.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_3.getTime(), CCGPShangai);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 4

		GregorianCalendar fechaGranPremio_4 = new GregorianCalendar(2018, 3, 29);

		GregorianCalendar mejorVuelta_4 = new GregorianCalendar();
		mejorVuelta_4.set(Calendar.HOUR, 1);
		mejorVuelta_4.set(Calendar.MINUTE, 43);
		mejorVuelta_4.set(Calendar.SECOND, 44);
		mejorVuelta_4.set(Calendar.MILLISECOND, 291);

		// PISTA 4
		GregorianCalendar recordVuelta_4 = new GregorianCalendar();

		recordVuelta_4.set(Calendar.MINUTE, 1);
		recordVuelta_4.set(Calendar.SECOND, 43);
		recordVuelta_4.set(Calendar.MILLISECOND, 441);
		Record record_4 = new Record(recordVuelta_4.getTime(), "Sebastian Vettel", 2017);
		String fotoRef_4 = null;
		try {
			fotoRef_4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/AzerbaijanGrandPrix.png", "Azerbaijan");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_4 != null) {
			Pista pista = manejadorPista.pista_create("Azerbaijan", fotoRef_4, "Lewis Hamilton", (float) 306.049,
					(float) 6.003, record_4);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 43);
			Vuelta_P1.set(Calendar.SECOND, 44);
			Vuelta_P1.set(Calendar.MILLISECOND, 291);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 43);
			Vuelta_P2.set(Calendar.SECOND, 46);
			Vuelta_P2.set(Calendar.MILLISECOND, 337);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Kimi.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 43);
			Vuelta_P3.set(Calendar.SECOND, 48);
			Vuelta_P3.set(Calendar.MILLISECOND, 315);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Sergio.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 43);
			Vuelta_P4.set(Calendar.SECOND, 49);
			Vuelta_P4.set(Calendar.MILLISECOND, 751);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Sebastian.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 43);
			Vuelta_P5.set(Calendar.SECOND, 51);
			Vuelta_P5.set(Calendar.MILLISECOND, 806);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Carlos.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 43);
			Vuelta_P6.set(Calendar.SECOND, 53);
			Vuelta_P6.set(Calendar.MILLISECOND, 449);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Charles.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 43);
			Vuelta_P7.set(Calendar.SECOND, 55);
			Vuelta_P7.set(Calendar.MILLISECOND, 222);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Fernando.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 43);
			Vuelta_P8.set(Calendar.SECOND, 56);
			Vuelta_P8.set(Calendar.MILLISECOND, 837);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Lance.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 43);
			Vuelta_P9.set(Calendar.SECOND, 58);
			Vuelta_P9.set(Calendar.MILLISECOND, 443);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Stoffel.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 44);
			Vuelta_P10.set(Calendar.SECOND, 2);
			Vuelta_P10.set(Calendar.MILLISECOND, 321);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Brendon.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 44);
			Vuelta_P11.set(Calendar.SECOND, 2);
			Vuelta_P11.set(Calendar.MILLISECOND, 803);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Marcus.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 44);
			Vuelta_P12.set(Calendar.SECOND, 8);
			Vuelta_P12.set(Calendar.MILLISECOND, 954);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Pierre.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 44);
			Vuelta_P13.set(Calendar.SECOND, 24);
			Vuelta_P13.set(Calendar.MILLISECOND, 954);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Kevin.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 44);
			Vuelta_P14.set(Calendar.SECOND, 29);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Valtteri.getId_str());

			List<String> CCGPAZERBAIJAN = new ArrayList<String>();
			CCGPAZERBAIJAN.add(CCP1.getId_str());
			CCGPAZERBAIJAN.add(CCP2.getId_str());
			CCGPAZERBAIJAN.add(CCP3.getId_str());
			CCGPAZERBAIJAN.add(CCP4.getId_str());
			CCGPAZERBAIJAN.add(CCP5.getId_str());
			CCGPAZERBAIJAN.add(CCP6.getId_str());
			CCGPAZERBAIJAN.add(CCP7.getId_str());
			CCGPAZERBAIJAN.add(CCP8.getId_str());
			CCGPAZERBAIJAN.add(CCP9.getId_str());
			CCGPAZERBAIJAN.add(CCP10.getId_str());
			CCGPAZERBAIJAN.add(CCP11.getId_str());
			CCGPAZERBAIJAN.add(CCP12.getId_str());
			CCGPAZERBAIJAN.add(CCP13.getId_str());
			CCGPAZERBAIJAN.add(CCP14.getId_str());

			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
		
			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_4.getTime(), 51,
					mejorVuelta_4.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_4.getTime(), CCGPAZERBAIJAN);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 5

		GregorianCalendar fechaGranPremio_5 = new GregorianCalendar(2018, 4, 13);

		GregorianCalendar mejorVuelta_5 = new GregorianCalendar();
		mejorVuelta_5.set(Calendar.HOUR, 1);
		mejorVuelta_5.set(Calendar.MINUTE, 35);
		mejorVuelta_5.set(Calendar.SECOND, 29);
		mejorVuelta_5.set(Calendar.MILLISECOND, 972);

		// PISTA 5
		GregorianCalendar recordVuelta_5 = new GregorianCalendar();

		recordVuelta_5.set(Calendar.MINUTE, 1);
		recordVuelta_5.set(Calendar.SECOND, 18);
		recordVuelta_5.set(Calendar.MILLISECOND, 441);
		Record record_5 = new Record(recordVuelta_5.getTime(), "Daniel Ricciardo", 2018);
		String fotoRef_5 = null;
		try {
			fotoRef_5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/EspanaGrandPremio.png", "Barcelona");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_5 != null) {
			Pista pista = manejadorPista.pista_create("Barcelona", fotoRef_5, "Lewis Hamilton", (float) 307.104,
					(float) 4.655, record_5);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 35);
			Vuelta_P1.set(Calendar.SECOND, 29);
			Vuelta_P1.set(Calendar.MILLISECOND, 972);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 35);
			Vuelta_P2.set(Calendar.SECOND, 50);
			Vuelta_P2.set(Calendar.MILLISECOND, 568);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Valtteri.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 35);
			Vuelta_P3.set(Calendar.SECOND, 56);
			Vuelta_P3.set(Calendar.MILLISECOND, 845);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Max.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 35);
			Vuelta_P4.set(Calendar.SECOND, 57);
			Vuelta_P4.set(Calendar.MILLISECOND, 556);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Sebastian.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 36);
			Vuelta_P5.set(Calendar.SECOND, 19);
			Vuelta_P5.set(Calendar.MILLISECOND, 30);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Daniel.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 36);
			Vuelta_P6.set(Calendar.SECOND, 24);
			Vuelta_P6.set(Calendar.MILLISECOND, 0);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Kevin.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 36);
			Vuelta_P7.set(Calendar.SECOND, 29);
			Vuelta_P7.set(Calendar.MILLISECOND, 0);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Carlos.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 36);
			Vuelta_P8.set(Calendar.SECOND, 34);
			Vuelta_P8.set(Calendar.MILLISECOND, 0);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Fernando.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 36);
			Vuelta_P9.set(Calendar.SECOND, 39);
			Vuelta_P9.set(Calendar.MILLISECOND, 0);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Sergio.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 36);
			Vuelta_P10.set(Calendar.SECOND, 44);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Charles.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 36);
			Vuelta_P11.set(Calendar.SECOND, 49);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Lance.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 36);
			Vuelta_P12.set(Calendar.SECOND, 54);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Brendon.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 36);
			Vuelta_P13.set(Calendar.SECOND, 59);
			Vuelta_P13.set(Calendar.MILLISECOND, 00);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Marcus.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 37);
			Vuelta_P14.set(Calendar.SECOND, 4);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Sergey.getId_str());

			List<String> CCGPSPAIN = new ArrayList<String>();
			CCGPSPAIN.add(CCP1.getId_str());
			CCGPSPAIN.add(CCP2.getId_str());
			CCGPSPAIN.add(CCP3.getId_str());
			CCGPSPAIN.add(CCP4.getId_str());
			CCGPSPAIN.add(CCP5.getId_str());
			CCGPSPAIN.add(CCP6.getId_str());
			CCGPSPAIN.add(CCP7.getId_str());
			CCGPSPAIN.add(CCP8.getId_str());
			CCGPSPAIN.add(CCP9.getId_str());
			CCGPSPAIN.add(CCP10.getId_str());
			CCGPSPAIN.add(CCP11.getId_str());
			CCGPSPAIN.add(CCP12.getId_str());
			CCGPSPAIN.add(CCP13.getId_str());
			CCGPSPAIN.add(CCP14.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
	

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_5.getTime(), 66,
					mejorVuelta_5.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_5.getTime(), CCGPSPAIN);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 6

		GregorianCalendar fechaGranPremio_6 = new GregorianCalendar(2018, 4, 27);

		GregorianCalendar mejorVuelta_6 = new GregorianCalendar();
		mejorVuelta_6.set(Calendar.HOUR, 1);
		mejorVuelta_6.set(Calendar.MINUTE, 42);
		mejorVuelta_6.set(Calendar.SECOND, 54);
		mejorVuelta_6.set(Calendar.MILLISECOND, 807);

		// PISTA 6
		GregorianCalendar recordVuelta_6 = new GregorianCalendar();

		recordVuelta_6.set(Calendar.MINUTE, 1);
		recordVuelta_6.set(Calendar.SECOND, 14);
		recordVuelta_6.set(Calendar.MILLISECOND, 260);
		Record record_6 = new Record(recordVuelta_6.getTime(), "Max Verstappen", 2018);
		String fotoRef_6 = null;
		try {
			fotoRef_6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/MontecarloGrandPrix.png", "Monaco");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_6 != null) {
			Pista pista = manejadorPista.pista_create("Principado de Monaco", fotoRef_6, "Daniel Ricciardo",
					(float) 260.286, (float) 3.337, record_6);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 42);
			Vuelta_P1.set(Calendar.SECOND, 54);
			Vuelta_P1.set(Calendar.MILLISECOND, 807);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Daniel.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 43);
			Vuelta_P2.set(Calendar.SECOND, 2);
			Vuelta_P2.set(Calendar.MILLISECOND, 143);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Sebastian.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 43);
			Vuelta_P3.set(Calendar.SECOND, 11);
			Vuelta_P3.set(Calendar.MILLISECOND, 820);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Lewis.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 44);
			Vuelta_P4.set(Calendar.SECOND, 12);
			Vuelta_P4.set(Calendar.MILLISECOND, 934);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Kimi.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 44);
			Vuelta_P5.set(Calendar.SECOND, 13);
			Vuelta_P5.set(Calendar.MILLISECOND, 629);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Valtteri.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 44);
			Vuelta_P6.set(Calendar.SECOND, 17);
			Vuelta_P6.set(Calendar.MILLISECOND, 474);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Esteban.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 44);
			Vuelta_P7.set(Calendar.SECOND, 19);
			Vuelta_P7.set(Calendar.MILLISECOND, 138);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Pierre.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 44);
			Vuelta_P8.set(Calendar.SECOND, 19);
			Vuelta_P8.set(Calendar.MILLISECOND, 646);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Nico.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 44);
			Vuelta_P9.set(Calendar.SECOND, 20);
			Vuelta_P9.set(Calendar.MILLISECOND, 120);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Max.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 45);
			Vuelta_P10.set(Calendar.SECOND, 3);
			Vuelta_P10.set(Calendar.MILLISECOND, 820);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Carlos.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 45);
			Vuelta_P11.set(Calendar.SECOND, 4);
			Vuelta_P11.set(Calendar.MILLISECOND, 671);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Marcus.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 45);
			Vuelta_P12.set(Calendar.SECOND, 5);
			Vuelta_P12.set(Calendar.MILLISECOND, 268);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Sergio.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 45);
			Vuelta_P13.set(Calendar.SECOND, 9);
			Vuelta_P13.set(Calendar.MILLISECOND, 630);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Kevin.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 45);
			Vuelta_P14.set(Calendar.SECOND, 14);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Stoffel.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 45);
			Vuelta_P15.set(Calendar.SECOND, 19);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Romain.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 45);
			Vuelta_P16.set(Calendar.SECOND, 24);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Sergey.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 45);
			Vuelta_P17.set(Calendar.SECOND, 29);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Lance.getId_str());

			Vuelta_P18.set(Calendar.HOUR, 1);
			Vuelta_P18.set(Calendar.MINUTE, 45);
			Vuelta_P18.set(Calendar.SECOND, 34);
			Vuelta_P18.set(Calendar.MILLISECOND, 0);
			CCP18 = new ClasificacionCarrera(0, Vuelta_P18.getTime(), Charles.getId_str());

			Vuelta_P19.set(Calendar.HOUR, 1);
			Vuelta_P19.set(Calendar.MINUTE, 45);
			Vuelta_P19.set(Calendar.SECOND, 39);
			Vuelta_P19.set(Calendar.MILLISECOND, 0);
			CCP19 = new ClasificacionCarrera(0, Vuelta_P19.getTime(), Brendon.getId_str());

			List<String> CCGPMONACO = new ArrayList<String>();
			CCGPMONACO.add(CCP1.getId_str());
			CCGPMONACO.add(CCP2.getId_str());
			CCGPMONACO.add(CCP3.getId_str());
			CCGPMONACO.add(CCP4.getId_str());
			CCGPMONACO.add(CCP5.getId_str());
			CCGPMONACO.add(CCP6.getId_str());
			CCGPMONACO.add(CCP7.getId_str());
			CCGPMONACO.add(CCP8.getId_str());
			CCGPMONACO.add(CCP9.getId_str());
			CCGPMONACO.add(CCP10.getId_str());
			CCGPMONACO.add(CCP11.getId_str());
			CCGPMONACO.add(CCP12.getId_str());
			CCGPMONACO.add(CCP13.getId_str());
			CCGPMONACO.add(CCP14.getId_str());
			CCGPMONACO.add(CCP15.getId_str());
			CCGPMONACO.add(CCP16.getId_str());
			CCGPMONACO.add(CCP17.getId_str());
			CCGPMONACO.add(CCP18.getId_str());
			CCGPMONACO.add(CCP19.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP18);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP19);
	

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_6.getTime(), 78,
					mejorVuelta_6.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_6.getTime(), CCGPMONACO);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 7

		GregorianCalendar fechaGranPremio_7 = new GregorianCalendar(2018, 5, 10);

		GregorianCalendar mejorVuelta_7 = new GregorianCalendar();
		mejorVuelta_7.set(Calendar.HOUR, 1);
		mejorVuelta_7.set(Calendar.MINUTE, 28);
		mejorVuelta_7.set(Calendar.SECOND, 31);
		mejorVuelta_7.set(Calendar.MILLISECOND, 377);

		// PISTA 7
		GregorianCalendar recordVuelta_7 = new GregorianCalendar();

		recordVuelta_7.set(Calendar.MINUTE, 1);
		recordVuelta_7.set(Calendar.SECOND, 13);
		recordVuelta_7.set(Calendar.MILLISECOND, 622);
		Record record_7 = new Record(recordVuelta_7.getTime(), "Rubens Barrichello", 2004);
		String fotoRef_7 = null;
		try {
			fotoRef_7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/CanadaGrandPrix.png", "Canada");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_7 != null) {
			Pista pista = manejadorPista.pista_create("Canada", fotoRef_7, "Sebastian Vettel", (float) 305.27,
					(float) 4.361, record_7);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 28);
			Vuelta_P1.set(Calendar.SECOND, 31);
			Vuelta_P1.set(Calendar.MILLISECOND, 377);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Sebastian.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 28);
			Vuelta_P2.set(Calendar.SECOND, 38);
			Vuelta_P2.set(Calendar.MILLISECOND, 753);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Valtteri.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 28);
			Vuelta_P3.set(Calendar.SECOND, 39);
			Vuelta_P3.set(Calendar.MILLISECOND, 413);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Max.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 29);
			Vuelta_P4.set(Calendar.SECOND, 40);
			Vuelta_P4.set(Calendar.MILLISECOND, 352);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Daniel.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 29);
			Vuelta_P5.set(Calendar.SECOND, 61);
			Vuelta_P5.set(Calendar.MILLISECOND, 169);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Lewis.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 29);
			Vuelta_P6.set(Calendar.SECOND, 62);
			Vuelta_P6.set(Calendar.MILLISECOND, 228);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Kimi.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 30);
			Vuelta_P7.set(Calendar.SECOND, 5);
			Vuelta_P7.set(Calendar.MILLISECOND, 954);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Nico.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 30);
			Vuelta_P8.set(Calendar.SECOND, 7);
			Vuelta_P8.set(Calendar.MILLISECOND, 622);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Carlos.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 30);
			Vuelta_P9.set(Calendar.SECOND, 8);
			Vuelta_P9.set(Calendar.MILLISECOND, 204);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Esteban.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 30);
			Vuelta_P10.set(Calendar.SECOND, 19);
			Vuelta_P10.set(Calendar.MILLISECOND, 005);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Charles.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 30);
			Vuelta_P11.set(Calendar.SECOND, 20);
			Vuelta_P11.set(Calendar.MILLISECOND, 100);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Pierre.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 30);
			Vuelta_P12.set(Calendar.SECOND, 33);
			Vuelta_P12.set(Calendar.MILLISECOND, 561);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Romain.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 30);
			Vuelta_P13.set(Calendar.SECOND, 49);
			Vuelta_P13.set(Calendar.MILLISECOND, 042);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Kevin.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 30);
			Vuelta_P14.set(Calendar.SECOND, 51);
			Vuelta_P14.set(Calendar.MILLISECOND, 571);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Sergio.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 30);
			Vuelta_P15.set(Calendar.SECOND, 57);
			Vuelta_P15.set(Calendar.MILLISECOND, 408);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Marcus.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 31);
			Vuelta_P16.set(Calendar.SECOND, 2);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Stoffel.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 31);
			Vuelta_P17.set(Calendar.SECOND, 7);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Sergey.getId_str());

			List<String> CCGPCANADA = new ArrayList<String>();
			CCGPCANADA.add(CCP1.getId_str());
			CCGPCANADA.add(CCP2.getId_str());
			CCGPCANADA.add(CCP3.getId_str());
			CCGPCANADA.add(CCP4.getId_str());
			CCGPCANADA.add(CCP5.getId_str());
			CCGPCANADA.add(CCP6.getId_str());
			CCGPCANADA.add(CCP7.getId_str());
			CCGPCANADA.add(CCP8.getId_str());
			CCGPCANADA.add(CCP9.getId_str());
			CCGPCANADA.add(CCP10.getId_str());
			CCGPCANADA.add(CCP11.getId_str());
			CCGPCANADA.add(CCP12.getId_str());
			CCGPCANADA.add(CCP13.getId_str());
			CCGPCANADA.add(CCP14.getId_str());
			CCGPCANADA.add(CCP15.getId_str());
			CCGPCANADA.add(CCP16.getId_str());
			CCGPCANADA.add(CCP17.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_7.getTime(), 70,
					mejorVuelta_7.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_7.getTime(), CCGPCANADA);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 8

		GregorianCalendar fechaGranPremio_8 = new GregorianCalendar(2018, 5, 24);

		GregorianCalendar mejorVuelta_8 = new GregorianCalendar();
		mejorVuelta_8.set(Calendar.HOUR, 1);
		mejorVuelta_8.set(Calendar.MINUTE, 30);
		mejorVuelta_8.set(Calendar.SECOND, 11);
		mejorVuelta_8.set(Calendar.MILLISECOND, 385);

		// PISTA 8
		GregorianCalendar recordVuelta_8 = new GregorianCalendar();

		recordVuelta_8.set(Calendar.MINUTE, 1);
		recordVuelta_8.set(Calendar.SECOND, 334);
		recordVuelta_8.set(Calendar.MILLISECOND, 225);
		Record record_8 = new Record(recordVuelta_8.getTime(), "Valtteri Bottas", 2018);
		String fotoRef_8 = null;
		try {
			fotoRef_8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/FranceGrandPrix.png", "France");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_8 != null) {
			Pista pista = manejadorPista.pista_create("France", fotoRef_8, "Lewis Hamilton", (float) 309.69,
					(float) 5.842, record_8);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 30);
			Vuelta_P1.set(Calendar.SECOND, 11);
			Vuelta_P1.set(Calendar.MILLISECOND, 385);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 30);
			Vuelta_P2.set(Calendar.SECOND, 18);
			Vuelta_P2.set(Calendar.MILLISECOND, 475);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Max.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 30);
			Vuelta_P3.set(Calendar.SECOND, 37);
			Vuelta_P3.set(Calendar.MILLISECOND, 273);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 30);
			Vuelta_P4.set(Calendar.SECOND, 46);
			Vuelta_P4.set(Calendar.MILLISECOND, 121);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Daniel.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 31);
			Vuelta_P5.set(Calendar.SECOND, 13);
			Vuelta_P5.set(Calendar.MILLISECOND, 320);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Sebastian.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 31);
			Vuelta_P6.set(Calendar.SECOND, 31);
			Vuelta_P6.set(Calendar.MILLISECOND, 749);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Kevin.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 31);
			Vuelta_P7.set(Calendar.SECOND, 32);
			Vuelta_P7.set(Calendar.MILLISECOND, 17);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Valtteri.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 31);
			Vuelta_P8.set(Calendar.SECOND, 38);
			Vuelta_P8.set(Calendar.MILLISECOND, 569);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Carlos.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 31);
			Vuelta_P9.set(Calendar.SECOND, 43);
			Vuelta_P9.set(Calendar.MILLISECOND, 374);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Nico.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 31);
			Vuelta_P10.set(Calendar.SECOND, 44);
			Vuelta_P10.set(Calendar.MILLISECOND, 258);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Charles.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 31);
			Vuelta_P11.set(Calendar.SECOND, 49);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Romain.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 31);
			Vuelta_P12.set(Calendar.SECOND, 54);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Stoffel.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 31);
			Vuelta_P13.set(Calendar.SECOND, 59);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Marcus.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 32);
			Vuelta_P14.set(Calendar.SECOND, 4);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Brendon.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 32);
			Vuelta_P15.set(Calendar.SECOND, 9);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Sergey.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 32);
			Vuelta_P16.set(Calendar.SECOND, 14);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Fernando.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 32);
			Vuelta_P17.set(Calendar.SECOND, 19);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Lance.getId_str());

			List<String> CCGPFRANCE = new ArrayList<String>();
			CCGPFRANCE.add(CCP1.getId_str());
			CCGPFRANCE.add(CCP2.getId_str());
			CCGPFRANCE.add(CCP3.getId_str());
			CCGPFRANCE.add(CCP4.getId_str());
			CCGPFRANCE.add(CCP5.getId_str());
			CCGPFRANCE.add(CCP6.getId_str());
			CCGPFRANCE.add(CCP7.getId_str());
			CCGPFRANCE.add(CCP8.getId_str());
			CCGPFRANCE.add(CCP9.getId_str());
			CCGPFRANCE.add(CCP10.getId_str());
			CCGPFRANCE.add(CCP11.getId_str());
			CCGPFRANCE.add(CCP12.getId_str());
			CCGPFRANCE.add(CCP13.getId_str());
			CCGPFRANCE.add(CCP14.getId_str());
			CCGPFRANCE.add(CCP15.getId_str());
			CCGPFRANCE.add(CCP16.getId_str());
			CCGPFRANCE.add(CCP17.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);
		

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_8.getTime(), 53,
					mejorVuelta_8.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_8.getTime(), CCGPFRANCE);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 9

		GregorianCalendar fechaGranPremio_9 = new GregorianCalendar(2018, 6, 1);

		GregorianCalendar mejorVuelta_9 = new GregorianCalendar();
		mejorVuelta_9.set(Calendar.HOUR, 1);
		mejorVuelta_9.set(Calendar.MINUTE, 21);
		mejorVuelta_9.set(Calendar.SECOND, 56);
		mejorVuelta_9.set(Calendar.MILLISECOND, 024);

		// PISTA 9
		GregorianCalendar recordVuelta_9 = new GregorianCalendar();

		recordVuelta_9.set(Calendar.MINUTE, 1);
		recordVuelta_9.set(Calendar.SECOND, 06);
		recordVuelta_9.set(Calendar.MILLISECOND, 957);
		Record record_9 = new Record(recordVuelta_9.getTime(), "Kimi Raikkonen", 2018);
		String fotoRef_9 = null;
		try {
			fotoRef_9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/AustriaGrandPrix.png", "Austria");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_9 != null) {
			Pista pista = manejadorPista.pista_create("Austria", fotoRef_9, "Max Verstappen", (float) 306.452,
					(float) 4.318, record_9);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 21);
			Vuelta_P1.set(Calendar.SECOND, 56);
			Vuelta_P1.set(Calendar.MILLISECOND, 024);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Max.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 21);
			Vuelta_P2.set(Calendar.SECOND, 57);
			Vuelta_P2.set(Calendar.MILLISECOND, 528);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Kimi.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 21);
			Vuelta_P3.set(Calendar.SECOND, 59);
			Vuelta_P3.set(Calendar.MILLISECOND, 205);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Sebastian.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 22);
			Vuelta_P4.set(Calendar.SECOND, 4);
			Vuelta_P4.set(Calendar.MILLISECOND, 0);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Romain.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 22);
			Vuelta_P5.set(Calendar.SECOND, 9);
			Vuelta_P5.set(Calendar.MILLISECOND, 0);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Kevin.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 22);
			Vuelta_P6.set(Calendar.SECOND, 14);
			Vuelta_P6.set(Calendar.MILLISECOND, 0);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Esteban.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 22);
			Vuelta_P7.set(Calendar.SECOND, 19);
			Vuelta_P7.set(Calendar.MILLISECOND, 0);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Sergio.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 22);
			Vuelta_P8.set(Calendar.SECOND, 24);
			Vuelta_P8.set(Calendar.MILLISECOND, 0);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Fernando.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 22);
			Vuelta_P9.set(Calendar.SECOND, 29);
			Vuelta_P9.set(Calendar.MILLISECOND, 0);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Charles.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 22);
			Vuelta_P10.set(Calendar.SECOND, 34);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Marcus.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 22);
			Vuelta_P11.set(Calendar.SECOND, 39);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Pierre.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 22);
			Vuelta_P12.set(Calendar.SECOND, 44);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Carlos.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 22);
			Vuelta_P13.set(Calendar.SECOND, 49);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Sergey.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 22);
			Vuelta_P14.set(Calendar.SECOND, 54);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Lance.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 22);
			Vuelta_P15.set(Calendar.SECOND, 59);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Stoffel.getId_str());

			List<String> CCGPAUSTRIA = new ArrayList<String>();
			CCGPAUSTRIA.add(CCP1.getId_str());
			CCGPAUSTRIA.add(CCP2.getId_str());
			CCGPAUSTRIA.add(CCP3.getId_str());
			CCGPAUSTRIA.add(CCP4.getId_str());
			CCGPAUSTRIA.add(CCP5.getId_str());
			CCGPAUSTRIA.add(CCP6.getId_str());
			CCGPAUSTRIA.add(CCP7.getId_str());
			CCGPAUSTRIA.add(CCP8.getId_str());
			CCGPAUSTRIA.add(CCP9.getId_str());
			CCGPAUSTRIA.add(CCP10.getId_str());
			CCGPAUSTRIA.add(CCP11.getId_str());
			CCGPAUSTRIA.add(CCP12.getId_str());
			CCGPAUSTRIA.add(CCP13.getId_str());
			CCGPAUSTRIA.add(CCP14.getId_str());
			CCGPAUSTRIA.add(CCP15.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_9.getTime(), 71,
					mejorVuelta_9.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_9.getTime(), CCGPAUSTRIA);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 10

		GregorianCalendar fechaGranPremio_10 = new GregorianCalendar(2018, 6, 8);

		GregorianCalendar mejorVuelta_10 = new GregorianCalendar();
		mejorVuelta_10.set(Calendar.HOUR, 1);
		mejorVuelta_10.set(Calendar.MINUTE, 27);
		mejorVuelta_10.set(Calendar.SECOND, 29);
		mejorVuelta_10.set(Calendar.MILLISECOND, 784);

		// PISTA 10
		GregorianCalendar recordVuelta_10 = new GregorianCalendar();

		recordVuelta_10.set(Calendar.MINUTE, 1);
		recordVuelta_10.set(Calendar.SECOND, 30);
		recordVuelta_10.set(Calendar.MILLISECOND, 621);
		Record record_10 = new Record(recordVuelta_10.getTime(), "Lewis Hamilton", 2017);
		String fotoRef_10 = null;
		try {
			fotoRef_10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/GranBretaniaGrandPrix.png", "Gran Bretania");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_10 != null) {
			Pista pista = manejadorPista.pista_create("Gran Bretania", fotoRef_10, "Sebastian Vettel", (float) 306.198,
					(float) 5.891, record_10);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 27);
			Vuelta_P1.set(Calendar.SECOND, 29);
			Vuelta_P1.set(Calendar.MILLISECOND, 784);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Sebastian.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 27);
			Vuelta_P2.set(Calendar.SECOND, 32);
			Vuelta_P2.set(Calendar.MILLISECOND, 48);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Lewis.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 27);
			Vuelta_P3.set(Calendar.SECOND, 33);
			Vuelta_P3.set(Calendar.MILLISECOND, 436);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 27);
			Vuelta_P4.set(Calendar.SECOND, 38);
			Vuelta_P4.set(Calendar.MILLISECOND, 667);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Valtteri.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 27);
			Vuelta_P5.set(Calendar.SECOND, 39);
			Vuelta_P5.set(Calendar.MILLISECOND, 284);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Daniel.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 27);
			Vuelta_P6.set(Calendar.SECOND, 58);
			Vuelta_P6.set(Calendar.MILLISECOND, 4);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Nico.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 27);
			Vuelta_P7.set(Calendar.SECOND, 59);
			Vuelta_P7.set(Calendar.MILLISECOND, 714);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Esteban.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 28);
			Vuelta_P8.set(Calendar.SECOND, 0);
			Vuelta_P8.set(Calendar.MILLISECOND, 899);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Fernando.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 28);
			Vuelta_P9.set(Calendar.SECOND, 2);
			Vuelta_P9.set(Calendar.MILLISECOND, 972);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Kevin.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 28);
			Vuelta_P10.set(Calendar.SECOND, 3);
			Vuelta_P10.set(Calendar.MILLISECOND, 492);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Sergio.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 28);
			Vuelta_P11.set(Calendar.SECOND, 4);
			Vuelta_P11.set(Calendar.MILLISECOND, 492);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Stoffel.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 28);
			Vuelta_P12.set(Calendar.SECOND, 7);
			Vuelta_P12.set(Calendar.MILLISECOND, 890);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Lance.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 28);
			Vuelta_P13.set(Calendar.SECOND, 8);
			Vuelta_P13.set(Calendar.MILLISECOND, 913);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Pierre.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 28);
			Vuelta_P14.set(Calendar.SECOND, 17);
			Vuelta_P14.set(Calendar.MILLISECOND, 897);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Sergey.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 28);
			Vuelta_P15.set(Calendar.SECOND, 30);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Max.getId_str());

			List<String> CCGPBRETANIA = new ArrayList<String>();
			CCGPBRETANIA.add(CCP1.getId_str());
			CCGPBRETANIA.add(CCP2.getId_str());
			CCGPBRETANIA.add(CCP3.getId_str());
			CCGPBRETANIA.add(CCP4.getId_str());
			CCGPBRETANIA.add(CCP5.getId_str());
			CCGPBRETANIA.add(CCP6.getId_str());
			CCGPBRETANIA.add(CCP7.getId_str());
			CCGPBRETANIA.add(CCP8.getId_str());
			CCGPBRETANIA.add(CCP9.getId_str());
			CCGPBRETANIA.add(CCP10.getId_str());
			CCGPBRETANIA.add(CCP11.getId_str());
			CCGPBRETANIA.add(CCP12.getId_str());
			CCGPBRETANIA.add(CCP13.getId_str());
			CCGPBRETANIA.add(CCP14.getId_str());
			CCGPBRETANIA.add(CCP15.getId_str());

			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);

			
			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_10.getTime(), 52,
					mejorVuelta_10.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_10.getTime(), CCGPBRETANIA);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 11

		GregorianCalendar fechaGranPremio_11 = new GregorianCalendar(2018, 6, 22);

		GregorianCalendar mejorVuelta_11 = new GregorianCalendar();
		mejorVuelta_11.set(Calendar.HOUR, 1);
		mejorVuelta_11.set(Calendar.MINUTE, 32);
		mejorVuelta_11.set(Calendar.SECOND, 29);
		mejorVuelta_11.set(Calendar.MILLISECOND, 845);

		// PISTA 11
		GregorianCalendar recordVuelta_11 = new GregorianCalendar();

		recordVuelta_11.set(Calendar.MINUTE, 1);
		recordVuelta_11.set(Calendar.SECOND, 13);
		recordVuelta_11.set(Calendar.MILLISECOND, 780);
		Record record_11 = new Record(recordVuelta_11.getTime(), "Kimi Raikkonen", 2004);
		String fotoRef_11 = null;
		try {
			fotoRef_11 = ManejadorImagenes.saveImageIntoMongoDB("fotos/GermanyGrandPrix.png", "Germany");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_11 != null) {
			Pista pista = manejadorPista.pista_create("Hockenheim", fotoRef_11, "Lewis Hamilton", (float) 306.458,
					(float) 4.574, record_11);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 32);
			Vuelta_P1.set(Calendar.SECOND, 29);
			Vuelta_P1.set(Calendar.MILLISECOND, 845);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 32);
			Vuelta_P2.set(Calendar.SECOND, 34);
			Vuelta_P2.set(Calendar.MILLISECOND, 380);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Valtteri.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 32);
			Vuelta_P3.set(Calendar.SECOND, 36);
			Vuelta_P3.set(Calendar.MILLISECOND, 577);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 32);
			Vuelta_P4.set(Calendar.SECOND, 37);
			Vuelta_P4.set(Calendar.MILLISECOND, 499);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Max.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 32);
			Vuelta_P5.set(Calendar.SECOND, 56);
			Vuelta_P5.set(Calendar.MILLISECOND, 454);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Nico.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 32);
			Vuelta_P6.set(Calendar.SECOND, 58);
			Vuelta_P6.set(Calendar.MILLISECOND, 716);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Romain.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 33);
			Vuelta_P7.set(Calendar.SECOND, 0);
			Vuelta_P7.set(Calendar.MILLISECOND, 401);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Sergio.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 33);
			Vuelta_P8.set(Calendar.SECOND, 1);
			Vuelta_P8.set(Calendar.MILLISECOND, 595);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Esteban.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 33);
			Vuelta_P9.set(Calendar.SECOND, 2);
			Vuelta_P9.set(Calendar.MILLISECOND, 207);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Marcus.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 33);
			Vuelta_P10.set(Calendar.SECOND, 4);
			Vuelta_P10.set(Calendar.MILLISECOND, 42);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Brendon.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 33);
			Vuelta_P11.set(Calendar.SECOND, 4);
			Vuelta_P11.set(Calendar.MILLISECOND, 764);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Kevin.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 33);
			Vuelta_P12.set(Calendar.SECOND, 12);
			Vuelta_P12.set(Calendar.MILLISECOND, 914);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Carlos.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 33);
			Vuelta_P13.set(Calendar.SECOND, 16);
			Vuelta_P13.set(Calendar.MILLISECOND, 462);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Stoffel.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 33);
			Vuelta_P14.set(Calendar.SECOND, 20);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Pierre.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 33);
			Vuelta_P15.set(Calendar.SECOND, 25);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Charles.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 33);
			Vuelta_P16.set(Calendar.SECOND, 30);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Fernando.getId_str());

			List<String> CCGPGERMANY = new ArrayList<String>();
			CCGPGERMANY.add(CCP1.getId_str());
			CCGPGERMANY.add(CCP2.getId_str());
			CCGPGERMANY.add(CCP3.getId_str());
			CCGPGERMANY.add(CCP4.getId_str());
			CCGPGERMANY.add(CCP5.getId_str());
			CCGPGERMANY.add(CCP6.getId_str());
			CCGPGERMANY.add(CCP7.getId_str());
			CCGPGERMANY.add(CCP8.getId_str());
			CCGPGERMANY.add(CCP9.getId_str());
			CCGPGERMANY.add(CCP10.getId_str());
			CCGPGERMANY.add(CCP11.getId_str());
			CCGPGERMANY.add(CCP12.getId_str());
			CCGPGERMANY.add(CCP13.getId_str());
			CCGPGERMANY.add(CCP14.getId_str());
			CCGPGERMANY.add(CCP15.getId_str());
			CCGPGERMANY.add(CCP16.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
	

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_11.getTime(), 67,
					mejorVuelta_11.getTime(), pista.getId_str(), campeonato.getId_str());

			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_11.getTime(), CCGPGERMANY);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 12

		GregorianCalendar fechaGranPremio_12 = new GregorianCalendar(2018, 6, 29);

		GregorianCalendar mejorVuelta_12 = new GregorianCalendar();
		mejorVuelta_12.set(Calendar.HOUR, 1);
		mejorVuelta_12.set(Calendar.MINUTE, 37);
		mejorVuelta_12.set(Calendar.SECOND, 16);
		mejorVuelta_12.set(Calendar.MILLISECOND, 427);

		// PISTA 12
		GregorianCalendar recordVuelta_12 = new GregorianCalendar();

		recordVuelta_12.set(Calendar.MINUTE, 1);
		recordVuelta_12.set(Calendar.SECOND, 19);
		recordVuelta_12.set(Calendar.MILLISECOND, 071);
		Record record_12 = new Record(recordVuelta_12.getTime(), "Michael Schumacher", 2004);
		String fotoRef_12 = null;
		try {
			fotoRef_12 = ManejadorImagenes.saveImageIntoMongoDB("fotos/HungaryGrandPrix.png", "Hungary");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_12 != null) {
			Pista pista = manejadorPista.pista_create("Budapest", fotoRef_12, "Lewis Hamilton", (float) 306.63,
					(float) 4.381, record_12);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 37);
			Vuelta_P1.set(Calendar.SECOND, 16);
			Vuelta_P1.set(Calendar.MILLISECOND, 427);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 37);
			Vuelta_P2.set(Calendar.SECOND, 33);
			Vuelta_P2.set(Calendar.MILLISECOND, 550);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Sebastian.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 37);
			Vuelta_P3.set(Calendar.SECOND, 36);
			Vuelta_P3.set(Calendar.MILLISECOND, 528);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 38);
			Vuelta_P4.set(Calendar.SECOND, 2);
			Vuelta_P4.set(Calendar.MILLISECOND, 846);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Daniel.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 38);
			Vuelta_P5.set(Calendar.SECOND, 16);
			Vuelta_P5.set(Calendar.MILLISECOND, 427);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Valtteri.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 38);
			Vuelta_P6.set(Calendar.SECOND, 29);
			Vuelta_P6.set(Calendar.MILLISECOND, 700);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Pierre.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 38);
			Vuelta_P7.set(Calendar.SECOND, 35);
			Vuelta_P7.set(Calendar.MILLISECOND, 0);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Kevin.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 38);
			Vuelta_P8.set(Calendar.SECOND, 40);
			Vuelta_P8.set(Calendar.MILLISECOND, 0);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Fernando.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 38);
			Vuelta_P9.set(Calendar.SECOND, 45);
			Vuelta_P9.set(Calendar.MILLISECOND, 0);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Carlos.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 38);
			Vuelta_P10.set(Calendar.SECOND, 50);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Romain.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 38);
			Vuelta_P11.set(Calendar.SECOND, 55);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Brendon.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 38);
			Vuelta_P12.set(Calendar.SECOND, 59);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Nico.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 39);
			Vuelta_P13.set(Calendar.SECOND, 3);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Esteban.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 39);
			Vuelta_P14.set(Calendar.SECOND, 8);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Sergio.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 39);
			Vuelta_P15.set(Calendar.SECOND, 13);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Marcus.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 39);
			Vuelta_P16.set(Calendar.SECOND, 18);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Sergey.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 39);
			Vuelta_P17.set(Calendar.SECOND, 23);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Lance.getId_str());

			List<String> CCGPHUNGARY = new ArrayList<String>();
			CCGPHUNGARY.add(CCP1.getId_str());
			CCGPHUNGARY.add(CCP2.getId_str());
			CCGPHUNGARY.add(CCP3.getId_str());
			CCGPHUNGARY.add(CCP4.getId_str());
			CCGPHUNGARY.add(CCP5.getId_str());
			CCGPHUNGARY.add(CCP6.getId_str());
			CCGPHUNGARY.add(CCP7.getId_str());
			CCGPHUNGARY.add(CCP8.getId_str());
			CCGPHUNGARY.add(CCP9.getId_str());
			CCGPHUNGARY.add(CCP10.getId_str());
			CCGPHUNGARY.add(CCP11.getId_str());
			CCGPHUNGARY.add(CCP12.getId_str());
			CCGPHUNGARY.add(CCP13.getId_str());
			CCGPHUNGARY.add(CCP14.getId_str());
			CCGPHUNGARY.add(CCP15.getId_str());
			CCGPHUNGARY.add(CCP16.getId_str());
			CCGPHUNGARY.add(CCP17.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_12.getTime(), 70,
					mejorVuelta_12.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_12.getTime(), CCGPHUNGARY);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}
		// GRAN PREMIO 13

		GregorianCalendar fechaGranPremio_13 = new GregorianCalendar(2018, 7, 26);

		GregorianCalendar mejorVuelta_13 = new GregorianCalendar();
		mejorVuelta_13.set(Calendar.HOUR, 1);
		mejorVuelta_13.set(Calendar.MINUTE, 23);
		mejorVuelta_13.set(Calendar.SECOND, 34);
		mejorVuelta_13.set(Calendar.MILLISECOND, 476);

		// PISTA 13
		GregorianCalendar recordVuelta_13 = new GregorianCalendar();

		recordVuelta_13.set(Calendar.MINUTE, 1);
		recordVuelta_13.set(Calendar.SECOND, 46);
		recordVuelta_13.set(Calendar.MILLISECOND, 286);
		Record record_13 = new Record(recordVuelta_13.getTime(), "Valtteri Bottas", 2018);
		String fotoRef_13 = null;
		try {
			fotoRef_13 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BelgianGrandPrix.png", "Belgian");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_13 != null) {
			Pista pista = manejadorPista.pista_create("Lieja", fotoRef_13, "Sebastian Vettel", (float) 308.052,
					(float) 7.004, record_13);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 23);
			Vuelta_P1.set(Calendar.SECOND, 34);
			Vuelta_P1.set(Calendar.MILLISECOND, 476);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Sebastian.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 23);
			Vuelta_P2.set(Calendar.SECOND, 45);
			Vuelta_P2.set(Calendar.MILLISECOND, 537);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Lewis.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 24);
			Vuelta_P3.set(Calendar.SECOND, 5);
			Vuelta_P3.set(Calendar.MILLISECOND, 848);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Max.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 24);
			Vuelta_P4.set(Calendar.SECOND, 43);
			Vuelta_P4.set(Calendar.MILLISECOND, 81);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Valtteri.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 24);
			Vuelta_P5.set(Calendar.SECOND, 45);
			Vuelta_P5.set(Calendar.MILLISECOND, 499);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Sergio.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 24);
			Vuelta_P6.set(Calendar.SECOND, 53);
			Vuelta_P6.set(Calendar.MILLISECOND, 996);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Esteban.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 25);
			Vuelta_P7.set(Calendar.SECOND, 0);
			Vuelta_P7.set(Calendar.MILLISECOND, 429);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Romain.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 25);
			Vuelta_P8.set(Calendar.SECOND, 2);
			Vuelta_P8.set(Calendar.MILLISECOND, 115);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Kevin.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 25);
			Vuelta_P9.set(Calendar.SECOND, 20);
			Vuelta_P9.set(Calendar.MILLISECOND, 368);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Pierre.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 25);
			Vuelta_P10.set(Calendar.SECOND, 25);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Marcus.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 25);
			Vuelta_P11.set(Calendar.SECOND, 30);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Carlos.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 25);
			Vuelta_P12.set(Calendar.SECOND, 35);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Sergey.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 25);
			Vuelta_P13.set(Calendar.SECOND, 40);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Lance.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 25);
			Vuelta_P14.set(Calendar.SECOND, 45);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Brendon.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 25);
			Vuelta_P15.set(Calendar.SECOND, 50);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Stoffel.getId_str());

			List<String> CCGPBELGIAN = new ArrayList<String>();
			CCGPBELGIAN.add(CCP1.getId_str());
			CCGPBELGIAN.add(CCP2.getId_str());
			CCGPBELGIAN.add(CCP3.getId_str());
			CCGPBELGIAN.add(CCP4.getId_str());
			CCGPBELGIAN.add(CCP5.getId_str());
			CCGPBELGIAN.add(CCP6.getId_str());
			CCGPBELGIAN.add(CCP7.getId_str());
			CCGPBELGIAN.add(CCP8.getId_str());
			CCGPBELGIAN.add(CCP9.getId_str());
			CCGPBELGIAN.add(CCP10.getId_str());
			CCGPBELGIAN.add(CCP11.getId_str());
			CCGPBELGIAN.add(CCP12.getId_str());
			CCGPBELGIAN.add(CCP13.getId_str());
			CCGPBELGIAN.add(CCP14.getId_str());
			CCGPBELGIAN.add(CCP15.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_13.getTime(), 44,
					mejorVuelta_13.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_13.getTime(), CCGPBELGIAN);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 14

		GregorianCalendar fechaGranPremio_14 = new GregorianCalendar(2018, 8, 02);

		GregorianCalendar mejorVuelta_14 = new GregorianCalendar();
		mejorVuelta_14.set(Calendar.HOUR, 1);
		mejorVuelta_14.set(Calendar.MINUTE, 16);
		mejorVuelta_14.set(Calendar.SECOND, 54);
		mejorVuelta_14.set(Calendar.MILLISECOND, 484);

		// PISTA 14
		GregorianCalendar recordVuelta_14 = new GregorianCalendar();

		recordVuelta_14.set(Calendar.MINUTE, 1);
		recordVuelta_14.set(Calendar.SECOND, 21);
		recordVuelta_14.set(Calendar.MILLISECOND, 046);
		Record record_14 = new Record(recordVuelta_14.getTime(), "Rubens Barrichello", 2004);
		String fotoRef_14 = null;
		try {
			fotoRef_14 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ItalyGrandPrix.png", "Italy");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_14 != null) {
			Pista pista = manejadorPista.pista_create("Monza", fotoRef_14, "Lewis Hamilton", (float) 306.72,
					(float) 5.793, record_14);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 16);
			Vuelta_P1.set(Calendar.SECOND, 54);
			Vuelta_P1.set(Calendar.MILLISECOND, 484);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 17);
			Vuelta_P2.set(Calendar.SECOND, 3);
			Vuelta_P2.set(Calendar.MILLISECOND, 189);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Kimi.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 17);
			Vuelta_P3.set(Calendar.SECOND, 8);
			Vuelta_P3.set(Calendar.MILLISECOND, 550);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Valtteri.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 17);
			Vuelta_P4.set(Calendar.SECOND, 10);
			Vuelta_P4.set(Calendar.MILLISECOND, 635);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Sebastian.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 17);
			Vuelta_P5.set(Calendar.SECOND, 12);
			Vuelta_P5.set(Calendar.MILLISECOND, 692);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Max.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 17);
			Vuelta_P6.set(Calendar.SECOND, 52);
			Vuelta_P6.set(Calendar.MILLISECOND, 245);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Esteban.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 17);
			Vuelta_P7.set(Calendar.SECOND, 53);
			Vuelta_P7.set(Calendar.MILLISECOND, 162);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Sergio.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 18);
			Vuelta_P8.set(Calendar.SECOND, 12);
			Vuelta_P8.set(Calendar.MILLISECOND, 624);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Carlos.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 18);
			Vuelta_P9.set(Calendar.SECOND, 17);
			Vuelta_P9.set(Calendar.MILLISECOND, 0);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Lance.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 18);
			Vuelta_P10.set(Calendar.SECOND, 22);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Sergey.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 18);
			Vuelta_P11.set(Calendar.SECOND, 27);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Charles.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 18);
			Vuelta_P12.set(Calendar.SECOND, 32);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Stoffel.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 18);
			Vuelta_P13.set(Calendar.SECOND, 37);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Nico.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 18);
			Vuelta_P14.set(Calendar.SECOND, 42);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Pierre.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 18);
			Vuelta_P15.set(Calendar.SECOND, 47);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Sergey.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 18);
			Vuelta_P16.set(Calendar.SECOND, 52);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Kevin.getId_str());

			List<String> CCGPITALY = new ArrayList<String>();
			CCGPITALY.add(CCP1.getId_str());
			CCGPITALY.add(CCP2.getId_str());
			CCGPITALY.add(CCP3.getId_str());
			CCGPITALY.add(CCP4.getId_str());
			CCGPITALY.add(CCP5.getId_str());
			CCGPITALY.add(CCP6.getId_str());
			CCGPITALY.add(CCP7.getId_str());
			CCGPITALY.add(CCP8.getId_str());
			CCGPITALY.add(CCP9.getId_str());
			CCGPITALY.add(CCP10.getId_str());
			CCGPITALY.add(CCP11.getId_str());
			CCGPITALY.add(CCP12.getId_str());
			CCGPITALY.add(CCP13.getId_str());
			CCGPITALY.add(CCP14.getId_str());
			CCGPITALY.add(CCP15.getId_str());
			CCGPITALY.add(CCP16.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_14.getTime(), 53,
					mejorVuelta_14.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_14.getTime(), CCGPITALY);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}
		// GRAN PREMIO 15 Singa

		GregorianCalendar fechaGranPremio_15 = new GregorianCalendar(2018, 8, 16);

		GregorianCalendar mejorVuelta_15 = new GregorianCalendar();
		mejorVuelta_15.set(Calendar.HOUR, 1);
		mejorVuelta_15.set(Calendar.MINUTE, 51);
		mejorVuelta_15.set(Calendar.SECOND, 11);
		mejorVuelta_15.set(Calendar.MILLISECOND, 611);

		// PISTA 15
		GregorianCalendar recordVuelta_15 = new GregorianCalendar();

		recordVuelta_15.set(Calendar.MINUTE, 1);
		recordVuelta_15.set(Calendar.SECOND, 41);
		recordVuelta_15.set(Calendar.MILLISECOND, 905);
		Record record_15 = new Record(recordVuelta_15.getTime(), "Kevin Magnussen", 2018);
		String fotoRef_15 = null;
		try {
			fotoRef_15 = ManejadorImagenes.saveImageIntoMongoDB("fotos/SingaporeGrandPrix.png", "Singapore");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_15 != null) {
			Pista pista = manejadorPista.pista_create("Ciudad de Singapur", fotoRef_15, "Lewis Hamilton",
					(float) 308.706, (float) 5.063, record_15);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 51);
			Vuelta_P1.set(Calendar.SECOND, 11);
			Vuelta_P1.set(Calendar.MILLISECOND, 611);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 51);
			Vuelta_P2.set(Calendar.SECOND, 20);
			Vuelta_P2.set(Calendar.MILLISECOND, 572);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Max.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 51);
			Vuelta_P3.set(Calendar.SECOND, 51);
			Vuelta_P3.set(Calendar.MILLISECOND, 556);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Sebastian.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 52);
			Vuelta_P4.set(Calendar.SECOND, 3);
			Vuelta_P4.set(Calendar.MILLISECOND, 541);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Valtteri.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 52);
			Vuelta_P5.set(Calendar.SECOND, 4);
			Vuelta_P5.set(Calendar.MILLISECOND, 612);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Kimi.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 52);
			Vuelta_P6.set(Calendar.SECOND, 5);
			Vuelta_P6.set(Calendar.MILLISECOND, 593);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Daniel.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 52);
			Vuelta_P7.set(Calendar.SECOND, 54);
			Vuelta_P7.set(Calendar.MILLISECOND, 622);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Fernando.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 52);
			Vuelta_P8.set(Calendar.SECOND, 59);
			Vuelta_P8.set(Calendar.MILLISECOND, 0);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Carlos.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 53);
			Vuelta_P9.set(Calendar.SECOND, 4);
			Vuelta_P9.set(Calendar.MILLISECOND, 0);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Charles.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 53);
			Vuelta_P10.set(Calendar.SECOND, 9);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Nico.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 53);
			Vuelta_P11.set(Calendar.SECOND, 14);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Marcus.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 53);
			Vuelta_P12.set(Calendar.SECOND, 19);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Stoffel.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 53);
			Vuelta_P13.set(Calendar.SECOND, 24);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Pierre.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 53);
			Vuelta_P14.set(Calendar.SECOND, 29);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Lance.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 53);
			Vuelta_P15.set(Calendar.SECOND, 34);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Romain.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 53);
			Vuelta_P16.set(Calendar.SECOND, 39);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Sergio.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 53);
			Vuelta_P17.set(Calendar.SECOND, 44);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Brendon.getId_str());

			Vuelta_P18.set(Calendar.HOUR, 1);
			Vuelta_P18.set(Calendar.MINUTE, 53);
			Vuelta_P18.set(Calendar.SECOND, 49);
			Vuelta_P18.set(Calendar.MILLISECOND, 0);
			CCP18 = new ClasificacionCarrera(0, Vuelta_P18.getTime(), Kevin.getId_str());

			Vuelta_P19.set(Calendar.HOUR, 1);
			Vuelta_P19.set(Calendar.MINUTE, 53);
			Vuelta_P19.set(Calendar.SECOND, 54);
			Vuelta_P19.set(Calendar.MILLISECOND, 0);
			CCP19 = new ClasificacionCarrera(0, Vuelta_P19.getTime(), Sergey.getId_str());

			List<String> CCGPSINGAPORE = new ArrayList<String>();
			CCGPSINGAPORE.add(CCP1.getId_str());
			CCGPSINGAPORE.add(CCP2.getId_str());
			CCGPSINGAPORE.add(CCP3.getId_str());
			CCGPSINGAPORE.add(CCP4.getId_str());
			CCGPSINGAPORE.add(CCP5.getId_str());
			CCGPSINGAPORE.add(CCP6.getId_str());
			CCGPSINGAPORE.add(CCP7.getId_str());
			CCGPSINGAPORE.add(CCP8.getId_str());
			CCGPSINGAPORE.add(CCP9.getId_str());
			CCGPSINGAPORE.add(CCP10.getId_str());
			CCGPSINGAPORE.add(CCP11.getId_str());
			CCGPSINGAPORE.add(CCP12.getId_str());
			CCGPSINGAPORE.add(CCP13.getId_str());
			CCGPSINGAPORE.add(CCP14.getId_str());
			CCGPSINGAPORE.add(CCP15.getId_str());
			CCGPSINGAPORE.add(CCP16.getId_str());
			CCGPSINGAPORE.add(CCP17.getId_str());
			CCGPSINGAPORE.add(CCP18.getId_str());
			CCGPSINGAPORE.add(CCP19.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP18);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP19);
	

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_15.getTime(), 61,
					mejorVuelta_15.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_15.getTime(), CCGPSINGAPORE);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}
		// GRAN PREMIO 16

		GregorianCalendar fechaGranPremio_16 = new GregorianCalendar(2018, 8, 30);

		GregorianCalendar mejorVuelta_16 = new GregorianCalendar();
		mejorVuelta_16.set(Calendar.HOUR, 1);
		mejorVuelta_16.set(Calendar.MINUTE, 27);
		mejorVuelta_16.set(Calendar.SECOND, 25);
		mejorVuelta_16.set(Calendar.MILLISECOND, 181);

		// PISTA 16
		GregorianCalendar recordVuelta_16 = new GregorianCalendar();

		recordVuelta_16.set(Calendar.MINUTE, 1);
		recordVuelta_16.set(Calendar.SECOND, 35);
		recordVuelta_16.set(Calendar.MILLISECOND, 861);
		Record record_16 = new Record(recordVuelta_16.getTime(), "Valtteri Bottas", 2018);
		String fotoRef_16 = null;
		try {
			fotoRef_16 = ManejadorImagenes.saveImageIntoMongoDB("fotos/RussiaGrandPrix.png", "Russia");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_16 != null) {
			Pista pista = manejadorPista.pista_create("San Petersburgo", fotoRef_16, "Lewis Hamilton", (float) 309.745,
					(float) 5.848, record_16);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 27);
			Vuelta_P1.set(Calendar.SECOND, 25);
			Vuelta_P1.set(Calendar.MILLISECOND, 181);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 27);
			Vuelta_P2.set(Calendar.SECOND, 27);
			Vuelta_P2.set(Calendar.MILLISECOND, 726);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Valtteri.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 27);
			Vuelta_P3.set(Calendar.SECOND, 32);
			Vuelta_P3.set(Calendar.MILLISECOND, 668);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Sebastian.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 27);
			Vuelta_P4.set(Calendar.SECOND, 41);
			Vuelta_P4.set(Calendar.MILLISECOND, 724);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Kimi.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 27);
			Vuelta_P5.set(Calendar.SECOND, 56);
			Vuelta_P5.set(Calendar.MILLISECOND, 197);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Max.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 28);
			Vuelta_P6.set(Calendar.SECOND, 45);
			Vuelta_P6.set(Calendar.MILLISECOND, 632);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Daniel.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 29);
			Vuelta_P7.set(Calendar.SECOND, 3);
			Vuelta_P7.set(Calendar.MILLISECOND, 571);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Charles.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 29);
			Vuelta_P8.set(Calendar.SECOND, 8);
			Vuelta_P8.set(Calendar.MILLISECOND, 0);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Kevin.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 29);
			Vuelta_P9.set(Calendar.SECOND, 12);
			Vuelta_P9.set(Calendar.MILLISECOND, 0);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Esteban.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 29);
			Vuelta_P10.set(Calendar.SECOND, 17);
			Vuelta_P10.set(Calendar.MILLISECOND, 0);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Sergio.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 29);
			Vuelta_P11.set(Calendar.SECOND, 22);
			Vuelta_P11.set(Calendar.MILLISECOND, 0);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Romain.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 29);
			Vuelta_P12.set(Calendar.SECOND, 27);
			Vuelta_P12.set(Calendar.MILLISECOND, 0);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Nico.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 29);
			Vuelta_P13.set(Calendar.SECOND, 32);
			Vuelta_P13.set(Calendar.MILLISECOND, 0);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Marcus.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 29);
			Vuelta_P14.set(Calendar.SECOND, 37);
			Vuelta_P14.set(Calendar.MILLISECOND, 0);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Fernando.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 29);
			Vuelta_P15.set(Calendar.SECOND, 42);
			Vuelta_P15.set(Calendar.MILLISECOND, 0);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Lance.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 29);
			Vuelta_P16.set(Calendar.SECOND, 47);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Stoffel.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 29);
			Vuelta_P17.set(Calendar.SECOND, 52);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Carlos.getId_str());

			Vuelta_P18.set(Calendar.HOUR, 1);
			Vuelta_P18.set(Calendar.MINUTE, 29);
			Vuelta_P18.set(Calendar.SECOND, 57);
			Vuelta_P18.set(Calendar.MILLISECOND, 0);
			CCP18 = new ClasificacionCarrera(0, Vuelta_P18.getTime(), Sergey.getId_str());

			List<String> CCGPRUSSIA = new ArrayList<String>();
			CCGPRUSSIA.add(CCP1.getId_str());
			CCGPRUSSIA.add(CCP2.getId_str());
			CCGPRUSSIA.add(CCP3.getId_str());
			CCGPRUSSIA.add(CCP4.getId_str());
			CCGPRUSSIA.add(CCP5.getId_str());
			CCGPRUSSIA.add(CCP6.getId_str());
			CCGPRUSSIA.add(CCP7.getId_str());
			CCGPRUSSIA.add(CCP8.getId_str());
			CCGPRUSSIA.add(CCP9.getId_str());
			CCGPRUSSIA.add(CCP10.getId_str());
			CCGPRUSSIA.add(CCP11.getId_str());
			CCGPRUSSIA.add(CCP12.getId_str());
			CCGPRUSSIA.add(CCP13.getId_str());
			CCGPRUSSIA.add(CCP14.getId_str());
			CCGPRUSSIA.add(CCP15.getId_str());
			CCGPRUSSIA.add(CCP16.getId_str());
			CCGPRUSSIA.add(CCP17.getId_str());
			CCGPRUSSIA.add(CCP18.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP18);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_16.getTime(), 53,
					mejorVuelta_16.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_16.getTime(), CCGPRUSSIA);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 17

		GregorianCalendar fechaGranPremio_17 = new GregorianCalendar(2018, 9, 07);

		GregorianCalendar mejorVuelta_17 = new GregorianCalendar();
		mejorVuelta_17.set(Calendar.HOUR, 1);
		mejorVuelta_17.set(Calendar.MINUTE, 27);
		mejorVuelta_17.set(Calendar.SECOND, 17);
		mejorVuelta_17.set(Calendar.MILLISECOND, 62);

		// PISTA 17
		GregorianCalendar recordVuelta_17 = new GregorianCalendar();

		recordVuelta_17.set(Calendar.MINUTE, 1);
		recordVuelta_17.set(Calendar.SECOND, 31);
		recordVuelta_17.set(Calendar.MILLISECOND, 540);
		Record record_17 = new Record(recordVuelta_17.getTime(), "Kimi Raikkonen", 2005);
		String fotoRef_17 = null;
		try {
			fotoRef_17 = ManejadorImagenes.saveImageIntoMongoDB("fotos/JapanGrandPrix.png", "Japan");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_17 != null) {
			Pista pista = manejadorPista.pista_create("Suzuka", fotoRef_17, "Lewis Hamilton", (float) 307.471,
					(float) 5.807, record_17);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 27);
			Vuelta_P1.set(Calendar.SECOND, 17);
			Vuelta_P1.set(Calendar.MILLISECOND, 62);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 27);
			Vuelta_P2.set(Calendar.SECOND, 29);
			Vuelta_P2.set(Calendar.MILLISECOND, 981);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Valtteri.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 27);
			Vuelta_P3.set(Calendar.SECOND, 31);
			Vuelta_P3.set(Calendar.MILLISECOND, 357);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Max.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 27);
			Vuelta_P4.set(Calendar.SECOND, 36);
			Vuelta_P4.set(Calendar.MILLISECOND, 557);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Daniel.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 28);
			Vuelta_P5.set(Calendar.SECOND, 7);
			Vuelta_P5.set(Calendar.MILLISECOND, 60);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Kimi.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 28);
			Vuelta_P6.set(Calendar.SECOND, 26);
			Vuelta_P6.set(Calendar.MILLISECOND, 935);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Sebastian.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 28);
			Vuelta_P7.set(Calendar.SECOND, 36);
			Vuelta_P7.set(Calendar.MILLISECOND, 441);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Sergio.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 28);
			Vuelta_P8.set(Calendar.SECOND, 44);
			Vuelta_P8.set(Calendar.MILLISECOND, 260);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Romain.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 28);
			Vuelta_P9.set(Calendar.SECOND, 45);
			Vuelta_P9.set(Calendar.MILLISECOND, 117);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Esteban.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 28);
			Vuelta_P10.set(Calendar.SECOND, 50);
			Vuelta_P10.set(Calendar.MILLISECOND, 005);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Carlos.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 28);
			Vuelta_P11.set(Calendar.SECOND, 55);
			Vuelta_P11.set(Calendar.MILLISECOND, 100);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Pierre.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 29);
			Vuelta_P12.set(Calendar.SECOND, 0);
			Vuelta_P12.set(Calendar.MILLISECOND, 561);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Marcus.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 29);
			Vuelta_P13.set(Calendar.SECOND, 5);
			Vuelta_P13.set(Calendar.MILLISECOND, 042);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Brendon.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 29);
			Vuelta_P14.set(Calendar.SECOND, 10);
			Vuelta_P14.set(Calendar.MILLISECOND, 571);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Fernando.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 29);
			Vuelta_P15.set(Calendar.SECOND, 15);
			Vuelta_P15.set(Calendar.MILLISECOND, 408);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Stoffel.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 29);
			Vuelta_P16.set(Calendar.SECOND, 15);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Sergey.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 29);
			Vuelta_P17.set(Calendar.SECOND, 20);
			Vuelta_P17.set(Calendar.MILLISECOND, 0);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Lance.getId_str());

			List<String> CCGPJAPAN = new ArrayList<String>();
			CCGPJAPAN.add(CCP1.getId_str());
			CCGPJAPAN.add(CCP2.getId_str());
			CCGPJAPAN.add(CCP3.getId_str());
			CCGPJAPAN.add(CCP4.getId_str());
			CCGPJAPAN.add(CCP5.getId_str());
			CCGPJAPAN.add(CCP6.getId_str());
			CCGPJAPAN.add(CCP7.getId_str());
			CCGPJAPAN.add(CCP8.getId_str());
			CCGPJAPAN.add(CCP9.getId_str());
			CCGPJAPAN.add(CCP10.getId_str());
			CCGPJAPAN.add(CCP11.getId_str());
			CCGPJAPAN.add(CCP12.getId_str());
			CCGPJAPAN.add(CCP13.getId_str());
			CCGPJAPAN.add(CCP14.getId_str());
			CCGPJAPAN.add(CCP15.getId_str());
			CCGPJAPAN.add(CCP16.getId_str());
			CCGPJAPAN.add(CCP17.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_17.getTime(), 53,
					mejorVuelta_17.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_17.getTime(), CCGPJAPAN);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}
		// GRAN PREMIO 18

		GregorianCalendar fechaGranPremio_18 = new GregorianCalendar(2018, 9, 21);

		GregorianCalendar mejorVuelta_18 = new GregorianCalendar();
		mejorVuelta_18.set(Calendar.HOUR, 1);
		mejorVuelta_18.set(Calendar.MINUTE, 34);
		mejorVuelta_18.set(Calendar.SECOND, 18);
		mejorVuelta_18.set(Calendar.MILLISECOND, 643);

		// PISTA 18
		GregorianCalendar recordVuelta_18 = new GregorianCalendar();
		recordVuelta_18.set(Calendar.MINUTE, 1);
		recordVuelta_18.set(Calendar.SECOND, 37);
		recordVuelta_18.set(Calendar.MILLISECOND, 766);
		Record record_18 = new Record(recordVuelta_18.getTime(), "Sebastian Vettel", 2017);
		String fotoRef_18 = null;
		try {
			fotoRef_18 = ManejadorImagenes.saveImageIntoMongoDB("fotos/UnitedStatesGrandPrix.png", "United States");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_18 != null) {
			Pista pista = manejadorPista.pista_create("Austin", fotoRef_18, "Kimi Raikkonen", (float) 308.405,
					(float) 5.513, record_18);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 34);
			Vuelta_P1.set(Calendar.SECOND, 18);
			Vuelta_P1.set(Calendar.MILLISECOND, 643);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Kimi.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 34);
			Vuelta_P2.set(Calendar.SECOND, 19);
			Vuelta_P2.set(Calendar.MILLISECOND, 924);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Max.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 34);
			Vuelta_P3.set(Calendar.SECOND, 20);
			Vuelta_P3.set(Calendar.MILLISECOND, 985);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Lewis.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 34);
			Vuelta_P4.set(Calendar.SECOND, 36);
			Vuelta_P4.set(Calendar.MILLISECOND, 865);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Sebastian.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 34);
			Vuelta_P5.set(Calendar.SECOND, 43);
			Vuelta_P5.set(Calendar.MILLISECOND, 987);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Valtteri.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 35);
			Vuelta_P6.set(Calendar.SECOND, 45);
			Vuelta_P6.set(Calendar.MILLISECOND, 853);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Nico.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 35);
			Vuelta_P7.set(Calendar.SECOND, 53);
			Vuelta_P7.set(Calendar.MILLISECOND, 637);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Carlos.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 35);
			Vuelta_P8.set(Calendar.SECOND, 59);
			Vuelta_P8.set(Calendar.MILLISECOND, 723);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Sergio.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 36);
			Vuelta_P9.set(Calendar.SECOND, 5);
			Vuelta_P9.set(Calendar.MILLISECOND, 204);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Brendon.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 36);
			Vuelta_P10.set(Calendar.SECOND, 9);
			Vuelta_P10.set(Calendar.MILLISECOND, 005);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Marcus.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 36);
			Vuelta_P11.set(Calendar.SECOND, 14);
			Vuelta_P11.set(Calendar.MILLISECOND, 100);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Stoffel.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 36);
			Vuelta_P12.set(Calendar.SECOND, 19);
			Vuelta_P12.set(Calendar.MILLISECOND, 561);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Pierre.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 36);
			Vuelta_P13.set(Calendar.SECOND, 24);
			Vuelta_P13.set(Calendar.MILLISECOND, 042);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Sergey.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 36);
			Vuelta_P14.set(Calendar.SECOND, 29);
			Vuelta_P14.set(Calendar.MILLISECOND, 571);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Lance.getId_str());

			List<String> CCGPUNITEDSTATES = new ArrayList<String>();
			CCGPUNITEDSTATES.add(CCP1.getId_str());
			CCGPUNITEDSTATES.add(CCP2.getId_str());
			CCGPUNITEDSTATES.add(CCP3.getId_str());
			CCGPUNITEDSTATES.add(CCP4.getId_str());
			CCGPUNITEDSTATES.add(CCP5.getId_str());
			CCGPUNITEDSTATES.add(CCP6.getId_str());
			CCGPUNITEDSTATES.add(CCP7.getId_str());
			CCGPUNITEDSTATES.add(CCP8.getId_str());
			CCGPUNITEDSTATES.add(CCP9.getId_str());
			CCGPUNITEDSTATES.add(CCP10.getId_str());
			CCGPUNITEDSTATES.add(CCP11.getId_str());
			CCGPUNITEDSTATES.add(CCP12.getId_str());
			CCGPUNITEDSTATES.add(CCP13.getId_str());
			CCGPUNITEDSTATES.add(CCP14.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_18.getTime(), 56,
					mejorVuelta_18.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_18.getTime(), CCGPUNITEDSTATES);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 19

		GregorianCalendar fechaGranPremio_19 = new GregorianCalendar(2018, 9, 28);

		GregorianCalendar mejorVuelta_19 = new GregorianCalendar();
		mejorVuelta_19.set(Calendar.HOUR, 1);
		mejorVuelta_19.set(Calendar.MINUTE, 38);
		mejorVuelta_19.set(Calendar.SECOND, 28);
		mejorVuelta_19.set(Calendar.MILLISECOND, 851);

		// PISTA 19
		GregorianCalendar recordVuelta_19 = new GregorianCalendar();
		recordVuelta_19.set(Calendar.MINUTE, 1);
		recordVuelta_19.set(Calendar.SECOND, 18);
		recordVuelta_19.set(Calendar.MILLISECOND, 785);
		Record record_19 = new Record(recordVuelta_19.getTime(), "Sebastian Vettel", 2017);
		String fotoRef_19 = null;
		try {
			fotoRef_19 = ManejadorImagenes.saveImageIntoMongoDB("fotos/MexicoGrandPrix.png", "Mexico");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_19 != null) {
			Pista pista = manejadorPista.pista_create("Ciudad de Mexico", fotoRef_19, "Max Verstappen", (float) 305.354,
					(float) 4.304, record_19);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 38);
			Vuelta_P1.set(Calendar.SECOND, 28);
			Vuelta_P1.set(Calendar.MILLISECOND, 851);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Max.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 38);
			Vuelta_P2.set(Calendar.SECOND, 46);
			Vuelta_P2.set(Calendar.MILLISECOND, 167);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Sebastian.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 38);
			Vuelta_P3.set(Calendar.SECOND, 28);
			Vuelta_P3.set(Calendar.MILLISECOND, 765);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 39);
			Vuelta_P4.set(Calendar.SECOND, 47);
			Vuelta_P4.set(Calendar.MILLISECOND, 589);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Lewis.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 39);
			Vuelta_P5.set(Calendar.SECOND, 52);
			Vuelta_P5.set(Calendar.MILLISECOND, 169);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Valtteri.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 39);
			Vuelta_P6.set(Calendar.SECOND, 57);
			Vuelta_P6.set(Calendar.MILLISECOND, 228);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Nico.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 40);
			Vuelta_P7.set(Calendar.SECOND, 3);
			Vuelta_P7.set(Calendar.MILLISECOND, 954);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Charles.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 40);
			Vuelta_P8.set(Calendar.SECOND, 8);
			Vuelta_P8.set(Calendar.MILLISECOND, 622);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Stoffel.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 40);
			Vuelta_P9.set(Calendar.SECOND, 13);
			Vuelta_P9.set(Calendar.MILLISECOND, 204);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Marcus.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 40);
			Vuelta_P10.set(Calendar.SECOND, 18);
			Vuelta_P10.set(Calendar.MILLISECOND, 005);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Pierre.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 40);
			Vuelta_P11.set(Calendar.SECOND, 23);
			Vuelta_P11.set(Calendar.MILLISECOND, 100);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Esteban.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 40);
			Vuelta_P12.set(Calendar.SECOND, 28);
			Vuelta_P12.set(Calendar.MILLISECOND, 561);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Lance.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 40);
			Vuelta_P13.set(Calendar.SECOND, 32);
			Vuelta_P13.set(Calendar.MILLISECOND, 042);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Sergey.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 40);
			Vuelta_P14.set(Calendar.SECOND, 37);
			Vuelta_P14.set(Calendar.MILLISECOND, 571);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Brendon.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 40);
			Vuelta_P15.set(Calendar.SECOND, 42);
			Vuelta_P15.set(Calendar.MILLISECOND, 408);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Kevin.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 40);
			Vuelta_P16.set(Calendar.SECOND, 47);
			Vuelta_P16.set(Calendar.MILLISECOND, 0);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Romain.getId_str());

			List<String> CCGPMEXICO = new ArrayList<String>();
			CCGPMEXICO.add(CCP1.getId_str());
			CCGPMEXICO.add(CCP2.getId_str());
			CCGPMEXICO.add(CCP3.getId_str());
			CCGPMEXICO.add(CCP4.getId_str());
			CCGPMEXICO.add(CCP5.getId_str());
			CCGPMEXICO.add(CCP6.getId_str());
			CCGPMEXICO.add(CCP7.getId_str());
			CCGPMEXICO.add(CCP8.getId_str());
			CCGPMEXICO.add(CCP9.getId_str());
			CCGPMEXICO.add(CCP10.getId_str());
			CCGPMEXICO.add(CCP11.getId_str());
			CCGPMEXICO.add(CCP12.getId_str());
			CCGPMEXICO.add(CCP13.getId_str());
			CCGPMEXICO.add(CCP14.getId_str());
			CCGPMEXICO.add(CCP15.getId_str());
			CCGPMEXICO.add(CCP16.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);


			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_19.getTime(), 71,
					mejorVuelta_19.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_19.getTime(), CCGPMEXICO);
			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());

		}

		// GRAN PREMIO 20

		GregorianCalendar fechaGranPremio_20 = new GregorianCalendar(2018, 10, 11);

		GregorianCalendar mejorVuelta_20 = new GregorianCalendar();
		mejorVuelta_20.set(Calendar.HOUR, 1);
		mejorVuelta_20.set(Calendar.MINUTE, 27);
		mejorVuelta_20.set(Calendar.SECOND, 9);
		mejorVuelta_20.set(Calendar.MILLISECOND, 66);

		// PISTA 20
		GregorianCalendar recordVuelta_20 = new GregorianCalendar();
		recordVuelta_20.set(Calendar.MINUTE, 1);
		recordVuelta_20.set(Calendar.SECOND, 11);
		recordVuelta_20.set(Calendar.MILLISECOND, 044);
		Record record_20 = new Record(recordVuelta_20.getTime(), "Max Verstappen", 2017);
		String fotoRef_20 = null;
		try {
			fotoRef_20 = ManejadorImagenes.saveImageIntoMongoDB("fotos/BrasilGrandPrix.png", "Brasil");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_20 != null) {
			Pista pista = manejadorPista.pista_create("Sao Paulo", fotoRef_20, "Lewis Hamilton", (float) 305.909,
					(float) 4.309, record_20);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 27);
			Vuelta_P1.set(Calendar.SECOND, 9);
			Vuelta_P1.set(Calendar.MILLISECOND, 66);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Lewis.getId_str());

			Vuelta_P2.set(Calendar.HOUR, 1);
			Vuelta_P2.set(Calendar.MINUTE, 27);
			Vuelta_P2.set(Calendar.SECOND, 10);
			Vuelta_P2.set(Calendar.MILLISECOND, 535);
			CCP2 = new ClasificacionCarrera(18, Vuelta_P2.getTime(), Max.getId_str());

			Vuelta_P3.set(Calendar.HOUR, 1);
			Vuelta_P3.set(Calendar.MINUTE, 27);
			Vuelta_P3.set(Calendar.SECOND, 13);
			Vuelta_P3.set(Calendar.MILLISECOND, 830);
			CCP3 = new ClasificacionCarrera(15, Vuelta_P3.getTime(), Kimi.getId_str());

			Vuelta_P4.set(Calendar.HOUR, 1);
			Vuelta_P4.set(Calendar.MINUTE, 27);
			Vuelta_P4.set(Calendar.SECOND, 14);
			Vuelta_P4.set(Calendar.MILLISECOND, 259);
			CCP4 = new ClasificacionCarrera(12, Vuelta_P4.getTime(), Daniel.getId_str());

			Vuelta_P5.set(Calendar.HOUR, 1);
			Vuelta_P5.set(Calendar.MINUTE, 27);
			Vuelta_P5.set(Calendar.SECOND, 32);
			Vuelta_P5.set(Calendar.MILLISECOND, 9);
			CCP5 = new ClasificacionCarrera(10, Vuelta_P5.getTime(), Valtteri.getId_str());

			Vuelta_P6.set(Calendar.HOUR, 1);
			Vuelta_P6.set(Calendar.MINUTE, 27);
			Vuelta_P6.set(Calendar.SECOND, 36);
			Vuelta_P6.set(Calendar.MILLISECOND, 63);
			CCP6 = new ClasificacionCarrera(8, Vuelta_P6.getTime(), Sebastian.getId_str());

			Vuelta_P7.set(Calendar.HOUR, 1);
			Vuelta_P7.set(Calendar.MINUTE, 27);
			Vuelta_P7.set(Calendar.SECOND, 53);
			Vuelta_P7.set(Calendar.MILLISECOND, 265);
			CCP7 = new ClasificacionCarrera(6, Vuelta_P7.getTime(), Charles.getId_str());

			Vuelta_P8.set(Calendar.HOUR, 1);
			Vuelta_P8.set(Calendar.MINUTE, 28);
			Vuelta_P8.set(Calendar.SECOND, 0);
			Vuelta_P8.set(Calendar.MILLISECOND, 296);
			CCP8 = new ClasificacionCarrera(4, Vuelta_P8.getTime(), Romain.getId_str());

			Vuelta_P9.set(Calendar.HOUR, 1);
			Vuelta_P9.set(Calendar.MINUTE, 28);
			Vuelta_P9.set(Calendar.SECOND, 1);
			Vuelta_P9.set(Calendar.MILLISECOND, 923);
			CCP9 = new ClasificacionCarrera(2, Vuelta_P9.getTime(), Kevin.getId_str());

			Vuelta_P10.set(Calendar.HOUR, 1);
			Vuelta_P10.set(Calendar.MINUTE, 28);
			Vuelta_P10.set(Calendar.SECOND, 6);
			Vuelta_P10.set(Calendar.MILLISECOND, 005);
			CCP10 = new ClasificacionCarrera(1, Vuelta_P10.getTime(), Sergio.getId_str());

			Vuelta_P11.set(Calendar.HOUR, 1);
			Vuelta_P11.set(Calendar.MINUTE, 28);
			Vuelta_P11.set(Calendar.SECOND, 11);
			Vuelta_P11.set(Calendar.MILLISECOND, 100);
			CCP11 = new ClasificacionCarrera(0, Vuelta_P11.getTime(), Brendon.getId_str());

			Vuelta_P12.set(Calendar.HOUR, 1);
			Vuelta_P12.set(Calendar.MINUTE, 28);
			Vuelta_P12.set(Calendar.SECOND, 16);
			Vuelta_P12.set(Calendar.MILLISECOND, 561);
			CCP12 = new ClasificacionCarrera(0, Vuelta_P12.getTime(), Carlos.getId_str());

			Vuelta_P13.set(Calendar.HOUR, 1);
			Vuelta_P13.set(Calendar.MINUTE, 28);
			Vuelta_P13.set(Calendar.SECOND, 21);
			Vuelta_P13.set(Calendar.MILLISECOND, 042);
			CCP13 = new ClasificacionCarrera(0, Vuelta_P13.getTime(), Pierre.getId_str());

			Vuelta_P14.set(Calendar.HOUR, 1);
			Vuelta_P14.set(Calendar.MINUTE, 28);
			Vuelta_P14.set(Calendar.SECOND, 26);
			Vuelta_P14.set(Calendar.MILLISECOND, 571);
			CCP14 = new ClasificacionCarrera(0, Vuelta_P14.getTime(), Esteban.getId_str());

			Vuelta_P15.set(Calendar.HOUR, 1);
			Vuelta_P15.set(Calendar.MINUTE, 28);
			Vuelta_P15.set(Calendar.SECOND, 31);
			Vuelta_P15.set(Calendar.MILLISECOND, 408);
			CCP15 = new ClasificacionCarrera(0, Vuelta_P15.getTime(), Stoffel.getId_str());

			Vuelta_P16.set(Calendar.HOUR, 1);
			Vuelta_P16.set(Calendar.MINUTE, 28);
			Vuelta_P16.set(Calendar.SECOND, 36);
			Vuelta_P16.set(Calendar.MILLISECOND, 984);
			CCP16 = new ClasificacionCarrera(0, Vuelta_P16.getTime(), Sergey.getId_str());

			Vuelta_P17.set(Calendar.HOUR, 1);
			Vuelta_P17.set(Calendar.MINUTE, 28);
			Vuelta_P17.set(Calendar.SECOND, 41);
			Vuelta_P17.set(Calendar.MILLISECOND, 676);
			CCP17 = new ClasificacionCarrera(0, Vuelta_P17.getTime(), Fernando.getId_str());

			Vuelta_P18.set(Calendar.HOUR, 1);
			Vuelta_P18.set(Calendar.MINUTE, 28);
			Vuelta_P18.set(Calendar.SECOND, 46);
			Vuelta_P18.set(Calendar.MILLISECOND, 710);
			CCP18 = new ClasificacionCarrera(0, Vuelta_P18.getTime(), Lance.getId_str());

			List<String> CCGPBRASIL = new ArrayList<String>();
			CCGPBRASIL.add(CCP1.getId_str());
			CCGPBRASIL.add(CCP2.getId_str());
			CCGPBRASIL.add(CCP3.getId_str());
			CCGPBRASIL.add(CCP4.getId_str());
			CCGPBRASIL.add(CCP5.getId_str());
			CCGPBRASIL.add(CCP6.getId_str());
			CCGPBRASIL.add(CCP7.getId_str());
			CCGPBRASIL.add(CCP8.getId_str());
			CCGPBRASIL.add(CCP9.getId_str());
			CCGPBRASIL.add(CCP10.getId_str());
			CCGPBRASIL.add(CCP11.getId_str());
			CCGPBRASIL.add(CCP12.getId_str());
			CCGPBRASIL.add(CCP13.getId_str());
			CCGPBRASIL.add(CCP14.getId_str());
			CCGPBRASIL.add(CCP15.getId_str());
			CCGPBRASIL.add(CCP16.getId_str());
			CCGPBRASIL.add(CCP17.getId_str());
			CCGPBRASIL.add(CCP18.getId_str());
			
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP1);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP2);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP3);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP4);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP5);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP6);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP7);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP8);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP9);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP10);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP11);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP12);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP13);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP14);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP15);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP16);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP17);
			manejadorClasificacion_Carrera.clasificacionCarrera_create(CCP18);
	

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_20.getTime(), 71,
					mejorVuelta_20.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_20.getTime(), CCGPBRASIL);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		// GRAN PREMIO 21

		GregorianCalendar fechaGranPremio_21 = new GregorianCalendar(2018, 10, 25);

		GregorianCalendar mejorVuelta_21 = new GregorianCalendar();
		mejorVuelta_21.set(Calendar.HOUR, 0);
		mejorVuelta_21.set(Calendar.MINUTE, 0);
		mejorVuelta_21.set(Calendar.SECOND, 0);
		mejorVuelta_21.set(Calendar.MILLISECOND, 0);

		// PISTA 21
		GregorianCalendar recordVuelta_21 = new GregorianCalendar();
		recordVuelta_21.set(Calendar.MINUTE, 1);
		recordVuelta_21.set(Calendar.SECOND, 40);
		recordVuelta_21.set(Calendar.MILLISECOND, 279);
		Record record_21 = new Record(recordVuelta_21.getTime(), "Sebastian Vettel", 2009);
		String fotoRef_21 = null;
		try {
			fotoRef_21 = ManejadorImagenes.saveImageIntoMongoDB("fotos/ArabGrandPrix.png", "UNITED ARAB EMIRATES");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (fotoRef_21 != null) {
			Pista pista = manejadorPista.pista_create("Abu Dabi", fotoRef_21, "Valtteri Bottas", (float) 305.355,
					(float) 5.554, record_21);

			Vuelta_P1.set(Calendar.HOUR, 1);
			Vuelta_P1.set(Calendar.MINUTE, 24);
			Vuelta_P1.set(Calendar.SECOND, 11);
			Vuelta_P1.set(Calendar.MILLISECOND, 672);
			CCP1 = new ClasificacionCarrera(25, Vuelta_P1.getTime(), Sebastian.getId_str());

			List<String> CCGPUNITEDARABEMIRATES = new ArrayList<String>();
			CCGPUNITEDARABEMIRATES.add(CCP1.getId_str());

			GranPremio granPremio = manejadorGranPremio.granPremio_create(fechaGranPremio_21.getTime(), 55,
					mejorVuelta_21.getTime(), pista.getId_str(), campeonato.getId_str());
			manejadorGranPremio.granPremio_update_clasificaciones(fechaGranPremio_21.getTime(), CCGPUNITEDARABEMIRATES);

			manejadorCampeonato.campeonato_addGranPremio(campeonato.getId_str(), granPremio.getId_str());
		}

		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Nico.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Fernando.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Esteban.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Carlos.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Romain.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Pierre.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Charles.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Stoffel.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Lance.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Marcus.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Brendon.getNombreCompleto()).getId_str()));
		manejadorClasificacion_Campeonato.clasificacionCampeonato_create(new ClasificacionCampeonato(0, 0,
				manejadorPiloto.piloto_getByName(Sergey.getNombreCompleto()).getId_str()));

		// MOTOR 1

		Motor motor1 = new Motor("SF71H", "1.6 L", "V6", true);

		// AUTO 1

		String fotoRefAuto1 = null;
		try {
			fotoRefAuto1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Ferrari SF71-H.jpg", "Ferrari SF71-H");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto1 = new Auto("Ferrari", 733, "OZ Racing", "Shell", fotoRefAuto1, motor1);
		manejadorAuto.auto_create(auto1);

		// MOTOR 2

		Motor motor2 = new Motor("W09", "1.6 L", "V6", true);

		// AUTO 2

		String fotoRefAuto2 = null;
		try {
			fotoRefAuto2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Mercedes AMG F1 W09.jpg",
					"Mercedes AMG F1 W09 ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto2 = new Auto("Mercedes", 733, "OZ Racing", "Petronas", fotoRefAuto2, motor2);
		manejadorAuto.auto_create(auto2);

		// MOTOR 3

		Motor motor3 = new Motor("R.S.18", "1.6 L", "V6", true);

		// AUTO 3

		String fotoRefAuto3 = null;
		try {
			fotoRefAuto3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Renault R.S.18.jpg", "Renault R.S.18");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto3 = new Auto("Renault", 742, "OZ Racing", "BP", fotoRefAuto3, motor3);
		manejadorAuto.auto_create(auto3);

		// MOTOR 4

		Motor motor4 = new Motor("FW41", "1.6 L", "V6", true);

		// AUTO 4

		String fotoRefAuto4 = null;
		try {
			fotoRefAuto4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Williams FW41.jpg", "Williams FW41");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto4 = new Auto("Williams", 742, "OZ Racing", "Petronas", fotoRefAuto4, motor4);
		manejadorAuto.auto_create(auto4);

		// MOTOR 5

		Motor motor5 = new Motor("VJM11", "1.6 L", "V6", true);

		// AUTO 5

		String fotoRefAuto5 = null;
		try {
			fotoRefAuto5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Force India VJM11.jpg", "Force India VJM11");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto5 = new Auto("Force India", 742, "OZ Racing", "Petronas", fotoRefAuto5, motor5);
		manejadorAuto.auto_create(auto5);

		// MOTOR 6

		Motor motor6 = new Motor("MCL33", "1.6 L", "V6", true);

		// AUTO 6

		String fotoRefAuto6 = null;
		try {
			fotoRefAuto6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/McLaren MCL33.jpg", "McLaren MCL33");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto6 = new Auto("McLaren", 742, "OZ Racing", "Petrobras", fotoRefAuto6, motor6);
		manejadorAuto.auto_create(auto6);

		// MOTOR 7

		Motor motor7 = new Motor("C37", "1.6 L", "V6", true);

		// AUTO 7

		String fotoRefAuto7 = null;
		try {
			fotoRefAuto7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sauber C37.jpg", "Sauber C37");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto7 = new Auto("Sauber", 742, "OZ Racing", "Shell", fotoRefAuto7, motor7);
		manejadorAuto.auto_create(auto7);

		// MOTOR 8

		Motor motor8 = new Motor("VF-18", "1.6 L", "V6", true);

		// AUTO 8

		String fotoRefAuto8 = null;
		try {
			fotoRefAuto8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Haas VF-18.jpg", "Haas VF-18");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto8 = new Auto("Haas", 733, "OZ Racing", "Shell", fotoRefAuto8, motor8);
		manejadorAuto.auto_create(auto8);

		// MOTOR 9

		Motor motor9 = new Motor("RB14", "1.6 L", "V6", true);

		// AUTO 9

		String fotoRefAuto9 = null;
		try {
			fotoRefAuto9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Red Bull RB14.jpg", "Red Bull RB14");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto9 = new Auto("Red Bull", 733, "OZ Racing", "Esso", fotoRefAuto9, motor9);
		manejadorAuto.auto_create(auto9);

		// MOTOR 10

		Motor motor10 = new Motor("STR13", "1.6 L", "V6", true);
		// AUTO 10

		String fotoRefAuto10 = null;
		try {
			fotoRefAuto10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Toro Rosso STR13.jpg", "Toro Rosso STR13");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Auto auto10 = new Auto("Toro Rosso", 742, "OZ Racing", "Repsol", fotoRefAuto10, motor10);
		manejadorAuto.auto_create(auto10);

		// Escuderia 1

		String fotoRefEsc1 = null;
		try {
			fotoRefEsc1 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Mercedes.jpg", "Mercedes");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia1 = new Escuderia("Mercedes", "Brackley, United Kingdom", "James Allison", "Toto Wolff",
				"620", 90, 4, fotoRefEsc1);
		// Agrega pilotos escuderia 1
		List<String> pilotosMercedes = new ArrayList<String>();
		pilotosMercedes.add(Lewis.getId_str());
		pilotosMercedes.add(Valtteri.getId_str());
		escuderia1.setPilotos(pilotosMercedes);
		// Agrega autos escuderia 1
		List<String> autosMercedes = new ArrayList<String>();
		autosMercedes.add(auto2.getId_str());
		escuderia1.setAutos(autosMercedes);
		manejadorEscuderia.escuderia_create(escuderia1);
		// Escuderia 2

		String fotoRefEsc2 = null;
		try {
			fotoRefEsc2 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Ferrari.jpg", "Ferrari");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia2 = new Escuderia("Ferrari", "Maranello, Italy", "Mattia Binotto", "Maurizio Arrivabene",
				"553", 215, 16, fotoRefEsc2);
		// Agrega pilotos escuderia 2
		List<String> pilotosFerrari = new ArrayList<String>();
		pilotosFerrari.add(Sebastian.getId_str());
		pilotosFerrari.add(Kimi.getId_str());
		escuderia2.setPilotos(pilotosFerrari);
		// Agrega autos escuderia 2
		List<String> autosFerrari = new ArrayList<String>();
		autosFerrari.add(auto1.getId_str());
		escuderia2.setAutos(autosFerrari);
		manejadorEscuderia.escuderia_create(escuderia2);

		// Escuderia 3

		String fotoRefEsc3 = null;
		try {
			fotoRefEsc3 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Red Bull.jpg", "Red Bull");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia3 = new Escuderia("Red Bull", "Milton Keynes, United Kingdom", "Pierre Wachï¿½",
				"Christian Horner", "392", 59, 4, fotoRefEsc3);
		// Agrega pilotos escuderia 3
		List<String> pilotosRed_Bull = new ArrayList<String>();
		pilotosRed_Bull.add(Max.getId_str());
		pilotosRed_Bull.add(Daniel.getId_str());
		escuderia3.setPilotos(pilotosRed_Bull);
		// Agrega autos escuderia 3
		List<String> autosRed_Bull = new ArrayList<String>();
		autosRed_Bull.add(auto9.getId_str());
		escuderia3.setAutos(autosRed_Bull);

		manejadorEscuderia.escuderia_create(escuderia3);
		// Escuderia 4

		String fotoRefEsc4 = null;
		try {
			fotoRefEsc4 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Renault.jpg", "Renault");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia4 = new Escuderia("Renault", "Enstone, United Kingdom", "Bob Bell", "Cyril Abiteboul",
				"114", 20, 2, fotoRefEsc4);
		// Agrega pilotos escuderia 4
		List<String> pilotosRenault = new ArrayList<String>();
		pilotosRenault.add(Nico.getId_str());
		pilotosRenault.add(Carlos.getId_str());
		escuderia4.setPilotos(pilotosRenault);
		// Agrega autos escuderia 4
		List<String> autosRenault = new ArrayList<String>();
		autosRenault.add(auto3.getId_str());
		escuderia4.setAutos(autosRenault);

		manejadorEscuderia.escuderia_create(escuderia4);
		// Escuderia 5

		String fotoRefEsc5 = null;
		try {
			fotoRefEsc5 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Haas.jpg", "Haas");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia5 = new Escuderia("Haas", "Kannapolis, United States", "Rob Taylor", "Guenther Steiner",
				"90", 0, 0, fotoRefEsc5);
		// Agrega pilotos escuderia 5
		List<String> pilotosHaas = new ArrayList<String>();
		pilotosHaas.add(Kevin.getId_str());
		pilotosHaas.add(Romain.getId_str());
		escuderia5.setPilotos(pilotosHaas);
		// Agrega autos escuderia 5
		List<String> autosHaas = new ArrayList<String>();
		autosHaas.add(auto8.getId_str());
		escuderia5.setAutos(autosHaas);

		manejadorEscuderia.escuderia_create(escuderia5);
		// Escuderia 6

		String fotoRefEsc6 = null;
		try {
			fotoRefEsc6 = ManejadorImagenes.saveImageIntoMongoDB("fotos/McLaren.jpg", "McLaren");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Agrega pilotos escuderia 6
		Escuderia escuderia6 = new Escuderia("McLaren", "Woking, United Kingdom", "Matt Morris", "Zak Brown", "62",
				155, 8, fotoRefEsc6);
		List<String> pilotosMcLaren = new ArrayList<String>();
		pilotosMcLaren.add(Fernando.getId_str());
		pilotosMcLaren.add(Stoffel.getId_str());
		escuderia6.setPilotos(pilotosMcLaren);
		// Agrega autos escuderia 6
		List<String> autosMcLaren = new ArrayList<String>();
		autosMcLaren.add(auto6.getId_str());
		escuderia6.setAutos(autosMcLaren);

		manejadorEscuderia.escuderia_create(escuderia6);
		// Escuderia 7

		String fotoRefEsc7 = null;
		try {
			fotoRefEsc7 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Force India.jpg", "Force India");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia7 = new Escuderia("Force India", "Silverstone, United Kingdom", "Andrew Green",
				"Otmar Szafnauer", "48", 0, 0, fotoRefEsc7);
		// Agrega pilotos escuderia 7
		List<String> pilotosForce_India = new ArrayList<String>();
		pilotosForce_India.add(Sergio.getId_str());
		pilotosForce_India.add(Esteban.getId_str());
		escuderia7.setPilotos(pilotosForce_India);
		// Agrega autos escuderia 7
		List<String> autosForce_India = new ArrayList<String>();
		autosForce_India.add(auto5.getId_str());
		escuderia7.setAutos(autosForce_India);

		manejadorEscuderia.escuderia_create(escuderia7);
		// Escuderia 9

				String fotoRefEsc9 = null;
				try {
					fotoRefEsc9 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Sauber.jpg", "Sauber");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Escuderia escuderia9 = new Escuderia("Alfa Romeo Sauber F1 Team", "Hinwil, Switzerland", "Simone Resta",
						"Frï¿½dï¿½ric Vasseur", "42", 1, 0, fotoRefEsc9);
				// Agrega pilotos escuderia 9
				List<String> pilotosSauber = new ArrayList<String>();
				pilotosSauber.add(Charles.getId_str());
				pilotosSauber.add(Marcus.getId_str());

				escuderia9.setPilotos(pilotosSauber);
				// Agrega autos escuderia 9
				List<String> autosSauber = new ArrayList<String>();
				autosSauber.add(auto7.getId_str());
				escuderia9.setAutos(autosSauber);

				manejadorEscuderia.escuderia_create(escuderia9);
		
		// Escuderia 8

		String fotoRefEsc8 = null;
		try {
			fotoRefEsc8 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Toro Rosso.jpg", "Toro Rosso");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia8 = new Escuderia("Toro Rosso", "Faenza, Italy", "James Key", "Franz Tost", "33", 1, 0,
				fotoRefEsc8);
		// Agrega pilotos escuderia 8
		List<String> pilotosToro_Rosso = new ArrayList<String>();
		pilotosToro_Rosso.add(Pierre.getId_str());
		pilotosToro_Rosso.add(Brendon.getId_str());
		escuderia8.setPilotos(pilotosToro_Rosso);
		// Agrega autos escuderia 8
		List<String> autosToro_Rosso = new ArrayList<String>();
		autosToro_Rosso.add(auto10.getId_str());
		escuderia8.setAutos(autosToro_Rosso);

		manejadorEscuderia.escuderia_create(escuderia8);

		
		// Escuderia 10

		String fotoRefEsc10 = null;
		try {
			fotoRefEsc10 = ManejadorImagenes.saveImageIntoMongoDB("fotos/Williams.jpg", "Williams");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Escuderia escuderia10 = new Escuderia("Williams Martini Racing", "Grove, United Kingdom", "Paddy Lowe",
				"Frank Williams", "7", 129, 9, fotoRefEsc10);
		// Agrega pilotos escuderia 10
		List<String> pilotosWilliams = new ArrayList<String>();
		pilotosWilliams.add(Lance.getId_str());
		pilotosWilliams.add(Sergey.getId_str());
		escuderia10.setPilotos(pilotosWilliams);
		// Agrega autos escuderia 10
		List<String> autosWilliams = new ArrayList<String>();
		autosWilliams.add(auto4.getId_str());
		escuderia10.setAutos(autosWilliams);

		manejadorEscuderia.escuderia_create(escuderia10);

		return Response.status(200);

	}

}

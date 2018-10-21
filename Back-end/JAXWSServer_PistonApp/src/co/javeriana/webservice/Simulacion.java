package co.javeriana.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Apuesta;
import CRUDs.CRUD_Usuario;
import clases_negocio.Apuesta;
import clases_negocio.Usuario;

@WebService(name="simulacion")
public class Simulacion {
	
	CRUD_Apuesta manejadorApuesta = new CRUD_Apuesta();
	CRUD_Usuario manejadorUsuario = new CRUD_Usuario();
	
	@WebMethod
	public void simularApuestas(@WebParam(name = "id_granpremio")String id_granpremio,
								@WebParam(name = "piloto")String piloto) {
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
}

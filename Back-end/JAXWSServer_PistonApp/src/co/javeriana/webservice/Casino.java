package co.javeriana.webservice;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import CRUDs.CRUD_Apuesta;
import CRUDs.CRUD_Usuario;
import clases_negocio.Apuesta;
import clases_negocio.GranPremio;
import clases_negocio.Usuario;

@WebService(name="casino")
public class Casino {
	CRUD_Apuesta manejadorApuesta = new CRUD_Apuesta();
	CRUD_Usuario manejadorUsuario = new CRUD_Usuario();
	
@WebMethod
public List<Apuesta> apuestasGranPremio(@WebParam(name = "id_granpremio")String id_granpremio){
	return manejadorApuesta.apuestas_GranPremio(id_granpremio);
}

@WebMethod
public boolean apostar(@WebParam(name = "id_granpremio")String id_granpremio,
					@WebParam(name = "id_usuario")String id_usuario,
					@WebParam(name = "monto")double monto,
					@WebParam(name = "piloto")String piloto) {
	Usuario u = manejadorUsuario.usuario_get(id_usuario);
	if(u.getBolsillo()-monto>=0) {
	manejadorApuesta.apuesta_create(id_usuario, piloto, id_granpremio, monto);
	manejadorUsuario.usuario_update_bolsillo(id_usuario, u.getBolsillo()-monto);
	return true;
	}
	return false;
	
	
}

@WebMethod
public void ejecutarApuesta(@WebParam(name = "id_granpremio")String id_granpremio,
							@WebParam(name = "id_piloto")String piloto) {
	
}
}

package co.javeriana.webservice;

import javax.xml.ws.Endpoint;

public class Main {
	public static final String URL_usuario ="http://localhost:8081/WS/crud_usuario";
	public static final String URL_piloto ="http://localhost:8081/WS/crud_piloto";
	public static final String URL_auto ="http://localhost:8081/WS/crud_auto";
	public static final String URL_escuderia ="http://localhost:8081/WS/crud_escuderia";
	public static final String URL_granPremio ="http://localhost:8081/WS/crud_granPremio";
	
	public static void main(String args[]){
		System.out.println("Publishing service on: "+ URL_piloto);
		Endpoint.publish(URL_piloto, new CRUD_Piloto());
		
		System.out.println("Publishing service on: "+ URL_usuario);
		Endpoint.publish(URL_usuario, new CRUD_Usuario());
		
		System.out.println("Publishing service on: "+ URL_auto);
		Endpoint.publish(URL_auto, new CRUD_Auto());
		
		System.out.println("Publishing service on: "+ URL_escuderia);
		Endpoint.publish(URL_escuderia, new CRUD_Escuderia());
		
		System.out.println("Publishing service on: "+ URL_granPremio);
		Endpoint.publish(URL_granPremio, new CRUD_GranPremio());
	}

}

package co.javeriana.webservice;

import javax.xml.ws.Endpoint;

public class Main {
	public static final String URL_AUT ="http://localhost:8080/WS/autenticacion";
	public static final String URL_INFOCAM ="http://localhost:8080/WS/infoCampeonato";
	public static final String URL_SIMUL = "http://localhost:8080/WS/simulacion";
	public static final String URL_CASINO = "http://localhost:8080/WS/casino";
	public static final String URL_ADMIN = "http://localhost:8080/WS/admin";
	
	public static void main(String args[]){
		System.out.println("Publishing service on: "+ URL_AUT);
		Endpoint.publish(URL_AUT, new Autenticacion());
		
		System.out.println("Publishing service on: "+ URL_INFOCAM);
		Endpoint.publish(URL_INFOCAM, new InformacionCampeonato());
		
		System.out.println("Publishing service on: "+ URL_SIMUL);
		Endpoint.publish(URL_SIMUL, new Simulacion());
		
		System.out.println("Publishing service on: "+ URL_CASINO);
		Endpoint.publish(URL_CASINO, new Casino());
		
		System.out.println("Publishing service on: "+ URL_ADMIN);
		Endpoint.publish(URL_ADMIN, new Administrador());
	}

}

package co.javeriana.webservice;

import javax.xml.ws.Endpoint;

public class Main {
	public static final String URL_AUT ="http://localhost:8080/WS/autenticacion";
	public static final String URL_INFOCAM ="http://localhost:8080/WS/infoCampeonato";
	
	public static void main(String args[]){
		System.out.println("Publishing service on: "+ URL_AUT);
		Endpoint.publish(URL_AUT, new Autenticacion());
		System.out.println("Publishing service on: "+ URL_INFOCAM);
		Endpoint.publish(URL_INFOCAM, new InformacionCampeonato());
	}

}

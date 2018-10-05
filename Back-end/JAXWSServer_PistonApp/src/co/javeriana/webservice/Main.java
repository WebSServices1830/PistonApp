package co.javeriana.webservice;

import javax.xml.ws.Endpoint;

public class Main {
	public static final String URL ="http://localhost:8081/WS/crud_piloto";
	public static void main(String args[]){
		System.out.println("Publishing service on: "+ URL);
		Endpoint.publish(URL, new CRUD_Piloto());
		
	}

}

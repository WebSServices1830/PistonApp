package co.edu.javeriana.sebastianmesa.conexmongo.Persistencia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class ClienteMongo {
	private static MongoClient mongoClient = null;
	
	private ClienteMongo() {
		
	}
	
	public static MongoClient getInstancia() {
		if(mongoClient == null) {
			mongoClient = MongoClients.create("mongodb://10.0.2.2");
		}
		return mongoClient;
	}
}

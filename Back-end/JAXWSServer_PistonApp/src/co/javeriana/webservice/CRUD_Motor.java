package co.javeriana.webservice;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebService(name="crud_motor")
public class CRUD_Motor {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Motor> collection = database.getCollection("motores", Motor.class);
	
    @WebMethod
	public void motor_create(
			@WebParam(name = "referencia")String referencia,
			@WebParam(name = "cilindraje")String cilindraje,
			@WebParam(name = "configuracion")String configuracion,
			@WebParam(name = "turbo")boolean turbo){
			
		Motor motor = new Motor(referencia,cilindraje,configuracion,turbo);
		collection.insertOne(motor);
	}
	
    @WebMethod
	public Motor motor_read(@WebParam(name = "id")String id){
		Motor motor = collection.find(eq("id", id)).first();
		return motor;
	}
	
	@WebMethod
	public Motor motor_readByReferencia(@WebParam(name = "referencia")String referencia){
		Motor motor= collection.find(eq("referencia", referencia)).first();
		return motor;
	}
	
	@WebMethod
	public List<Motor> motor_readAll() {
		
		final List<Motor> motores = new ArrayList<>();
		
		Block<Motor> saveBlock = new Block<Motor>() {
		    @Override
		    public void apply(Motor motor) {
		        motores.add(motor);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return motores;
		
	}
	
	@WebMethod
	public void motor_update(
			@WebParam(name = "referencia")String referencia,
			@WebParam(name = "cilindraje")String cilindraje,
			@WebParam(name = "configuracion")String configuracion,
			@WebParam(name = "turbo")boolean turbo){
		collection.updateOne(
				eq("referencia", referencia) , 
				combine(
						set("referencia",referencia), 
						set("cilindraje",cilindraje), 
						set("configuracion",configuracion),
						set("turbo",turbo)
						) 
				);
	}
	
	@WebMethod
	public void motor_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void motor_deleteByReferencia(@WebParam(name = "referencia")String referencia){
		collection.deleteOne(eq("referencia",referencia));
	}

}

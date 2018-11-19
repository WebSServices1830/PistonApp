package DataManagers;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import clases_mongoDB.ClienteMongo;
import co.edu.javeriana.ws.rest.clases.ClasificacionCarrera;

@WebService(name="crud_clasificacioncarrera")
public class CRUD_ClasificacionCarrera {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<ClasificacionCarrera> collection = database.getCollection("clasificacionescarrera", ClasificacionCarrera.class);
	
    public void clasificacionCarrera_create(ClasificacionCarrera clasificacionCarrera) {
    	collection.insertOne(clasificacionCarrera);
    }
    
    public ClasificacionCarrera clasificacionCarrera_get(String id) {
    	ClasificacionCarrera clasificacionCarrera = collection.find(eq("id_str", id)).first();
    	return clasificacionCarrera;
    }
	
	public List<ClasificacionCarrera> clasificacionCarrera_getAll() {
		
		final List<ClasificacionCarrera> clasificacionesCarrera = new ArrayList<>();
		
		Block<ClasificacionCarrera> saveBlock = new Block<ClasificacionCarrera>() {
		    @Override
		    public void apply(ClasificacionCarrera clasificacionCarrera) {
		        clasificacionesCarrera.add(clasificacionCarrera);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return clasificacionesCarrera;
		
	}
   	
   	public void clasificacionCarrera_update(ClasificacionCarrera clasificacionCarrera, String idClasificacionCarrera) throws com.mongodb.MongoWriteException, com.mongodb.MongoWriteConcernException, com.mongodb.MongoException{
   		collection.updateOne(
   				eq("id_str", idClasificacionCarrera) , 
   				combine(
   						set("puntaje",clasificacionCarrera.getPuntaje()), 
   						set("tiempo",clasificacionCarrera.getTiempo())
   						)
   				);
   		//System.out.println("2: id"+idClasificacionCarrera+" puntaje:"+clasificacionCarrera.getPuntaje()+" tiempo:"+clasificacionCarrera.getTiempo());
   	}
   	
	public void clasificacionCarrera_delete(String id){
		collection.deleteOne(eq("id", id));
	}

}

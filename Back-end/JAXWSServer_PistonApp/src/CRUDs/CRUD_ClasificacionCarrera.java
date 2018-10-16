package CRUDs;

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
import clases_negocio.ClasificacionCarrera;

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
	
    @WebMethod
	public void clasificacioncarrera_create(
			@WebParam(name = "puntaje")int puntaje,
			@WebParam(name = "tiempo")LocalTime tiempo,
			@WebParam(name = "posicion")int posicion,
			@WebParam(name = "competidor")ObjectId competidor
			){
			
		ClasificacionCarrera clasificacionCarrera= new ClasificacionCarrera(puntaje,tiempo,posicion,competidor);
		collection.insertOne(clasificacionCarrera);
	}
	
	@WebMethod
	public ClasificacionCarrera clasificacioncarrera_read(@WebParam(name = "id")String id){
		ClasificacionCarrera clasificacionCarrera = collection.find(eq("id", id)).first();
		return clasificacionCarrera;
	}
	
	@WebMethod
	public ClasificacionCarrera clasificacioncarrera_readByCompetidor(@WebParam(name = "competidor")ObjectId competidor){
		ClasificacionCarrera clasificacionCarrera = collection.find(eq("competidor", competidor)).first();
		return clasificacionCarrera;
	}
	
	@WebMethod
	public List<ClasificacionCarrera> clasificacioncarrera_readAll() {
		
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
	
	@WebMethod
	public void clasificacioncarrera_update(
			@WebParam(name = "puntaje")int puntaje,
			@WebParam(name = "tiempo")LocalTime tiempo,
			@WebParam(name = "posicion")int posicion,
			@WebParam(name = "competidor")ObjectId competidor
			){
		collection.updateOne(
				eq("competidor", competidor) , 
				combine(
						set("puntaje",puntaje), 
						set("tiempo",tiempo), 
						set("posicion",posicion),
						set("competidor",competidor)
						) 
				);
	}
	
	@WebMethod
	public void clasificacioncarrera_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void clasificacioncarrera_deleteByCompetidor(@WebParam(name = "competidor")ObjectId competidor){
		collection.deleteOne(eq("competidor",competidor));
		
	}

}

package DataManagers;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import clases_mongoDB.ClienteMongo;
import co.edu.javeriana.ws.rest.clases.ClasificacionCampeonato;

public class CRUD_ClasificacionCampeonato {

MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<ClasificacionCampeonato> collection = database.getCollection("clasificacionescampeonato", ClasificacionCampeonato.class);
	
    public void clasificacionCampeonato_create(ClasificacionCampeonato clasificacionCampeonato) {
    	collection.insertOne(clasificacionCampeonato);
    }
    
    public ClasificacionCampeonato clasificacionCampeonato_get(String id) {
    	ClasificacionCampeonato clasificacionCampeonato = collection.find(eq("id_str", id)).first();
    	return clasificacionCampeonato;
    }
    
    public ClasificacionCampeonato clasificacionCampeonato_getByPiloto(String piloto) {
    	ClasificacionCampeonato clasificacionCampeonato = collection.find(eq("piloto", piloto)).first();
    	return clasificacionCampeonato;
    }
	
	public List<ClasificacionCampeonato> clasificacionCampeonato_getAll() {
		
		final List<ClasificacionCampeonato> clasificacionesCampeonato = new ArrayList<>();
		
		Block<ClasificacionCampeonato> saveBlock = new Block<ClasificacionCampeonato>() {
		    @Override
		    public void apply(ClasificacionCampeonato clasificacionCampeonato) {
		        clasificacionesCampeonato.add(clasificacionCampeonato);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return clasificacionesCampeonato;
		
	}
   	
   	public void clasificacionCampeonato_update(ClasificacionCampeonato clasificacionCampeonato, String idClasificacionCampeonato) throws com.mongodb.MongoWriteException, com.mongodb.MongoWriteConcernException, com.mongodb.MongoException{
   		collection.updateOne(
   				eq("id_str", idClasificacionCampeonato) , 
   				combine(
   						set("puntaje",clasificacionCampeonato.getPuntaje()), 
   						set("posicion",clasificacionCampeonato.getPosicion())
   						)
   				);
   	}
   	
	public void clasificacionCampeonato_delete(String id){
		collection.deleteOne(eq("id", id));
	}
	
	
}

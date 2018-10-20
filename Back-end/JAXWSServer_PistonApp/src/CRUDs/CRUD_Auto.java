package CRUDs;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

import clases_mongoDB.ClienteMongo;
import clases_negocio.Auto;
import clases_negocio.Motor;
import clases_negocio.Usuario;

public class CRUD_Auto {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Auto> collection = database.getCollection("autos", Auto.class);
	
    public void auto_create(Auto auto) {
    	collection.insertOne(auto);
    }
    
    public Auto auto_get(String id) {
    	Auto auto = collection.find(eq("id", id)).first();
    	return auto;
    }
   	
    public Auto auto_getByName(String nombre) {
    	Auto auto = collection.find(eq("nombre", nombre)).first();
    	return auto;
    }
    
    public boolean existeAuto(String nombre) {
    	Auto auto = auto_getByName(nombre);
    	if(auto == null) {
    		return false;
    	}
    	
    	return true;
    }
	
	public List<Auto> auto_getAll() {
		
		final List<Auto> autos = new ArrayList<>();
		
		Block<Auto> saveBlock = new Block<Auto>() {
		    @Override
		    public void apply(Auto auto) {
		        autos.add(auto);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return autos;
		
	}

   	public void auto_update(Auto auto){
   		collection.updateOne(
   				eq("id", auto.getId()) , 
   				combine(
   						set("nombre",auto.getNombre()), 
   						set("pesoEnKg",auto.getPesoEnKg()), 
   						set("ruedas",auto.getRuedas()),
   						set("combustible",auto.getCombustible()),
   						set("foto_ref",auto.getFoto_ref()),
   						set("motor", auto.getMotor())
   						)
   				);
   	}
   	
	public void auto_delete(String id){
		collection.deleteOne(eq("id", id));
	}
	
	public void auto_deleteByName(String nombre){
		collection.deleteOne(eq("nombre",nombre));
		
	}

}

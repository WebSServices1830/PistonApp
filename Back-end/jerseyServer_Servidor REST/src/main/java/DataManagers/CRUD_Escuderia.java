package DataManagers;

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
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

import clases_mongoDB.ClienteMongo;
import co.edu.javeriana.ws.rest.clases.Auto;
import co.edu.javeriana.ws.rest.clases.Escuderia;

public class CRUD_Escuderia {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<co.edu.javeriana.ws.rest.clases.Escuderia> collection = database.getCollection("escuderias", Escuderia.class);
    
    public void escuderia_create(Escuderia escuderia) throws MongoWriteException, MongoWriteConcernException, MongoException{
    	collection.insertOne(escuderia);
    }
    
    public Escuderia escuderia_get(String id) {
    	Escuderia escuderia = collection.find(eq("id_str", id)).first();
    	return escuderia;
    }
   	
    public Escuderia escuderia_getByName(String nombre) {
    	Escuderia escuderia = collection.find(eq("nombre", nombre)).first();
    	return escuderia;
    }
    
    public boolean existeEscuderia(String nombre) {
    	Escuderia escuderia = escuderia_getByName(nombre);
    	if(escuderia == null) {
    		return false;
    	}
    	
    	return true;
    }
	
	public List<Escuderia> escuderia_getAll() {
		
		final List<Escuderia> escuderias = new ArrayList<>();
		
		Block<Escuderia> saveBlock = new Block<Escuderia>() {
		    @Override
		    public void apply(Escuderia escuderia) {
		        escuderias.add(escuderia);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return escuderias;
		
	}

   	public void escuderia_update(Escuderia escuderia, String id_escuderia) throws MongoWriteException, MongoWriteConcernException, MongoException{
   		collection.updateOne(
   				eq("id_str", id_escuderia) , 
   				combine(
   						set("nombre",escuderia.getNombre()), 
						set("lugarBase",escuderia.getLugarBase()), 
						set("jefeEquipo",escuderia.getJefeEquipo()),
						set("jefeTecnico",escuderia.getJefeTecnico()),
						set("chasis",escuderia.getChasis()),
						set("cant_vecesEnPodio",escuderia.getCant_vecesEnPodio()),
						set("cant_TitulosCampeonato",escuderia.getCant_TitulosCampeonato()),
						set("fotoEscudo_ref",escuderia.getFotoEscudo_ref()),
						set("autos",escuderia.getAutos()),
						set("pilotos",escuderia.getPilotos())
   						)
   				);
   	}
   	
	public void escuderia_delete(String id) throws MongoWriteException, MongoWriteConcernException, MongoException{
		collection.deleteOne(eq("id", id));
	}
	
	public void escuderia_deleteByName(String nombre) throws MongoWriteException, MongoWriteConcernException, MongoException{
		collection.deleteOne(eq("nombre",nombre));
		
	}

}

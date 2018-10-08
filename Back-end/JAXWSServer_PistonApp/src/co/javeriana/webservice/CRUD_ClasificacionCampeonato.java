package co.javeriana.webservice;

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

@WebService(name="crud_clasificacioncampeonato")
public class CRUD_ClasificacionCampeonato {

MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<ClasificacionCampeonato> collection = database.getCollection("clasificacioncampeonato", ClasificacionCampeonato.class);
	
    @WebMethod
	public void create_clasificacioncampeonato(
			@WebParam(name = "puntaje")int puntaje,
			@WebParam(name = "tiempo")LocalTime tiempo,
			@WebParam(name = "posicion")int posicion,
			@WebParam(name = "piloto")ObjectId piloto
			){
			
		ClasificacionCampeonato clasificacion = new ClasificacionCampeonato(puntaje,tiempo,posicion,piloto);
		collection.insertOne(clasificacion);
	}
	
	@WebMethod
	public ClasificacionCampeonato read_clasificacioncampeonato(@WebParam(name = "id")String id){
		ClasificacionCampeonato clasificacion = collection.find(eq("id", id)).first();
		return clasificacion;
	}

	
	@WebMethod
	public List<ClasificacionCampeonato> readAll_clasificacionescampeonato() {
		
		final List<ClasificacionCampeonato> clasificaciones = new ArrayList<>();
		
		Block<ClasificacionCampeonato> saveBlock = new Block<ClasificacionCampeonato>() {
		    @Override
		    public void apply(ClasificacionCampeonato clasificacion) {
		        clasificaciones.add(clasificacion);
		    }

			
		};
		
		collection.find().forEach(saveBlock);
		
		return clasificaciones;
		
	}
	
	@WebMethod
	public void update_clasificacioncampeonato(
			@WebParam(name = "id")String id,
			@WebParam(name = "puntaje")int puntaje,
			@WebParam(name = "tiempo")LocalTime tiempo,
			@WebParam(name = "posicion")int posicion,
			@WebParam(name = "piloto")ObjectId piloto
			){
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("puntaje",puntaje), 
						set("tiempo",tiempo), 
						set("posicion",posicion),
						set("piloto",piloto)
						) 
				);
	}
	
	
	
	@WebMethod
	public void clasificacioncampeonato_updateFromAndroid(
			@WebParam(name = "puntaje")int puntaje,
			@WebParam(name = "tiempo")LocalTime tiempo,
			@WebParam(name = "posicion")int posicion,
			@WebParam(name = "piloto")ObjectId piloto
			){
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("clasificacioncampeonato");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            ObjectId p  = doc.getObjectId("piloto");
		            
		            if (p.equals(piloto)) {
						
						collection.updateOne(
								eq("piloto", piloto) , 
								combine(
										set("puntaje",puntaje), 
										set("tiempo",tiempo), 
										set("posicion",posicion),
										set("piloto",piloto)
										) 
								);
					}
		        }
		        
		        System.out.println("@info/: 'updateFromAndroid'  ->  ClasificacionCampeonato: '" + piloto + "' actualizado.");
		        
		    } finally {
		    	cursor.close();
		    }
		} catch (MongoException e) {
		    e.printStackTrace();
		}
		
	}
	
	@WebMethod
	public void delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void deleteByName(@WebParam(name = "id")ObjectId piloto){
		collection.deleteOne(eq("piloto",piloto));
		
	}
	
	
}

package CRUDs;

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
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import clases_mongoDB.ClienteMongo;
import clases_negocio.Escuderia;
import clases_negocio.Pista;
import clases_negocio.Record;

public class CRUD_Pista {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Pista> collection = database.getCollection("pistas", Pista.class);
    
    public Pista pista_create(
			String ciudad,
			String foto_ref,
			String nombreUltimoGanador,
			float distanciaCarrera_km,
			float longitudCircuito_km,
			Record record) {
    	Pista pista = new Pista(ciudad,foto_ref,nombreUltimoGanador,distanciaCarrera_km,longitudCircuito_km,record);
    	collection.insertOne(pista);
    	return pista;
    }
	
    public void pista_create(Pista pista) {
    	collection.insertOne(pista);
    }
    
    public Pista pista_get(String id) {
    	Pista pista = collection.find(eq("id_str", id)).first();
    	return pista;
    }
   	
    public Pista pista_getByCity(String ciudad) {
    	Pista pista = collection.find(eq("ciudad", ciudad)).first();
    	return pista;
    }
    
    public boolean existePista(String ciudad) {
    	Pista pista = pista_getByCity(ciudad);
    	if(pista == null) {
    		return false;
    	}
    	
    	return true;
    }
	
	public List<Pista> pista_getAll() {
		
		final List<Pista> pistas = new ArrayList<>();
		
		Block<Pista> saveBlock = new Block<Pista>() {
		    @Override
		    public void apply(Pista pista) {
		        pistas.add(pista);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return pistas;
		
	}

   	public void pista_update(Pista pista){
   		collection.updateOne(
   				eq("id", pista.getId()) , 
   				combine(
   						set("ciudad",pista.getCiudad()), 
						set("foto_ref",pista.getFoto_ref()), 
						set("nombreUltimoGanador",pista.getNombreUltimoGanador()),
						set("distanciaCarrera_km",pista.getDistanciaCarrera_km()),
						set("longitudCircuito_km",pista.getLongitudCircuito_km()),
						set("record",pista.getRecord()),
						set("comentarios",pista.getComentarios()),
						set("calificacion",pista.getCalificacion())
   						)
   				);
   	}
   	
	public void pista_delete(String id){
		collection.deleteOne(eq("id", id));
	}
	
	public void pista_deleteByName(String ciudad){
		collection.deleteOne(eq("ciudad",ciudad));
		
	}
	
	public void pista_update_calificacion(String id, float calificacion) {
		collection.updateOne(
   				eq("id", id) , 
   				combine(
   						set("calificacion",calificacion)
   						)
   				);
	}

}

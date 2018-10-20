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
			Record record){
			
		Pista pista = new Pista(ciudad,foto_ref,nombreUltimoGanador,distanciaCarrera_km,longitudCircuito_km,record);
		collection.insertOne(pista);
		
		return pista;
	}
	
	public Pista pista_read(String id){
		Pista pista = collection.find(eq("id", id)).first();
		return pista;
	}
	
	public Pista pista_readByCiudad(String ciudad){
		Pista pista= collection.find(eq("ciudad", ciudad)).first();
		return pista;
	}
	
	public List<Pista> pista_readAll() {
		
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
	
	public void pista_update_calificacion(String pista, float calificacion){
		collection.updateOne(
				eq("id_str", pista) , 
				combine(
						set("calificacion",calificacion)
						) 
				);
	}
	
	
	public void pista_update(
			String ciudad,
			String foto_ref,
			String nombreUltimoGanador,
			float distanciaCarrera_km,
			float longitudCircuito_km,
			String record){
		collection.updateOne(
				eq("ciudad", ciudad) , 
				combine(
						set("ciudad",ciudad), 
						set("foto_ref",foto_ref), 
						set("nombreUltimoGanador",nombreUltimoGanador),
						set("distanciaCarrera_km",distanciaCarrera_km), 
						set("longitudCircuito_km",longitudCircuito_km), 
						set("record",record)
						) 
				);
	}
	
	public void pista_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	public void pista_deleteByCiudad(@WebParam(name = "ciudad")String ciudad){
		collection.deleteOne(eq("ciudad",ciudad));
	}

}

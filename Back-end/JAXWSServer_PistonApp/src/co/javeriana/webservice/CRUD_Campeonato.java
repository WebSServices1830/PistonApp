package co.javeriana.webservice;

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

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebService(name="crud_campeonato")
public class CRUD_Campeonato {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Campeonato> collection = database.getCollection("campeonatos", Campeonato.class);
	
    @WebMethod
	public void campeonato_create(
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "fecha_inicio")Date fecha_inicio,
			@WebParam(name = "fecha_final")Date fecha_final){
			
		Campeonato campeonato = new Campeonato(nombre,fecha_inicio,fecha_final);
		collection.insertOne(campeonato);
	}
	
    @WebMethod
	public Campeonato campeonato_read(@WebParam(name = "id")String id){
		Campeonato campeonato = collection.find(eq("id", id)).first();
		return campeonato;
	}
	
	@WebMethod
	public Campeonato campeonato_readByName(@WebParam(name = "nombre")String nombre){
		Campeonato campeonato= collection.find(eq("nombre", nombre)).first();
		return campeonato;
	}
	
	@WebMethod
	public List<Campeonato> campeonato_readAll() {
		
		final List<Campeonato> campeonatos = new ArrayList<>();
		
		Block<Campeonato> saveBlock = new Block<Campeonato>() {
		    @Override
		    public void apply(Campeonato campeonato) {
		        campeonatos.add(campeonato);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return campeonatos;
		
	}
	
	@WebMethod
	public void campeonato_update(
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "fecha_inicio")Date fecha_inicio,
			@WebParam(name = "fecha_final")Date fecha_final){
		collection.updateOne(
				eq("nombre", nombre) , 
				combine(
						set("nombre",nombre), 
						set("fecha_inicio",fecha_inicio), 
						set("fecha_final",fecha_final)
						) 
				);
	}
	
	@WebMethod
	public void campeonato_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void campeonato_deleteByName(@WebParam(name = "id")String nombre){
		collection.deleteOne(eq("nombre",nombre));
	}

}

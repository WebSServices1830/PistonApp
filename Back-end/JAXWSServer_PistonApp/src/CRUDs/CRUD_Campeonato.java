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

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import clases_mongoDB.ClienteMongo;
import clases_negocio.Campeonato;
import clases_negocio.GranPremio;

public class CRUD_Campeonato {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Campeonato> collection = database.getCollection("campeonatos", Campeonato.class);
	
	public Campeonato campeonato_create(
			String nombre,
			Date fecha_inicio,
			Date fecha_final){
			
		Campeonato campeonato = new Campeonato(nombre,fecha_inicio,fecha_final);
		collection.insertOne(campeonato);
		
		return campeonato;
	}
	
	public Campeonato campeonato_read(String id){
		Campeonato campeonato = collection.find(eq("id_str", id)).first();
		return campeonato;
	}
	
	public Campeonato campeonato_readByName(String nombre){
		Campeonato campeonato= collection.find(eq("nombre", nombre)).first();
		return campeonato;
	}
	
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
	
	public void campeonato_update(
			String nombre,
			Date fecha_inicio,
			Date fecha_final){
		collection.updateOne(
				eq("nombre", nombre) , 
				combine(
						set("nombre",nombre), 
						set("fecha_inicio",fecha_inicio), 
						set("fecha_final",fecha_final)
						) 
				);
	}
	
	public void campeonato_delete(String id){
		collection.deleteOne(eq("id", id));
	}
	
	public void campeonato_deleteByName(String nombre){
		collection.deleteOne(eq("nombre",nombre));
	}
	
	public void campeonato_addGranPremio(
			String id_campeonato,
			String id_granPremio) {
		
		Campeonato campeonato = campeonato_read(id_campeonato);
		campeonato.getGran_premios().add(id_granPremio);
		
		collection.updateOne(
				eq("id_str", id_campeonato) , 
				combine(
						set("gran_premios",campeonato.getGran_premios())
						) 
				);
	}

}

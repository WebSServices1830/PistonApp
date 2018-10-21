package CRUDs;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import clases_mongoDB.ClienteMongo;
import clases_negocio.Apuesta;
import clases_negocio.Auto;
import clases_negocio.ComentarioPiloto;
import clases_negocio.Pista;
import clases_negocio.Record;

public class CRUD_Apuesta {
MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Apuesta> collection = database.getCollection("apuestas", Apuesta.class);
    
    public Apuesta apuesta_create(String usuario, String piloto, String granpremio, double monto) {
    	return new Apuesta(usuario,  piloto,  granpremio,  monto);
    }
	
    public void apuesta_create(Apuesta apuesta) {
    	collection.insertOne(apuesta);
    }
    
    public Apuesta apuestaUsuario_get(String usuario) {
    	Apuesta apuesta = collection.find(eq("usuario", usuario)).first();
    	return apuesta;
    }
   	
    
    public List<Apuesta> apuestas_GranPremio(String granpremio) {
		
		final List<Apuesta> apuestas = new ArrayList<>();
		
		Block<Apuesta> saveBlock = new Block<Apuesta>() {
		    @Override
		    public void apply(Apuesta apuesta) {
		        apuestas.add(apuesta);
		    }

			
		};
		
		collection.find(eq("granpremio",granpremio)).forEach(saveBlock);
		
		return apuestas;
		
	}
	
	public List<Apuesta> pista_getAll() {
		
		final List<Apuesta> apuestas = new ArrayList<>();
		
		Block<Apuesta> saveBlock = new Block<Apuesta>() {
		    @Override
		    public void apply(Apuesta apuesta) {
		        apuestas.add(apuesta);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return apuestas;
		
	}

   	
	public void delete_ApuestaUsuario(String usuario){
		collection.deleteOne(eq("usuario", usuario));
	}
	


}

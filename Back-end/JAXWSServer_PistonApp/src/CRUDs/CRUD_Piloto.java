package CRUDs;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

//MongoDB imports
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import clases_mongoDB.ClienteMongo;
import clases_negocio.Auto;
import clases_negocio.Piloto;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.client.gridfs.*;
import com.mongodb.client.gridfs.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.io.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import static com.mongodb.client.model.Filters.eq;

public class CRUD_Piloto {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Piloto> collection = database.getCollection("pilotos", Piloto.class);
	
    public void piloto_create(Piloto piloto) {
    	collection.insertOne(piloto);
    }
    
    public Piloto piloto_get(String id) {
    	Piloto piloto = collection.find(eq("id_str", id)).first();
    	return piloto;
    }
   	
    public Piloto piloto_getByName(String nombreCompleto) {
    	Piloto piloto = collection.find(eq("nombreCompleto", nombreCompleto)).first();
    	return piloto;
    }
    
    public boolean existePiloto(String nombreCompleto) {
    	Piloto piloto = piloto_getByName(nombreCompleto);
    	if(piloto == null) {
    		return false;
    	}
    	
    	return true;
    }
	
	public List<Piloto> piloto_getAll() {
		
		final List<Piloto> pilotos = new ArrayList<>();
		
		Block<Piloto> saveBlock = new Block<Piloto>() {
		    @Override
		    public void apply(Piloto piloto) {
		        pilotos.add(piloto);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return pilotos;
	}
	
	public List<Piloto> piloto_getAllBySearchParameter(String parametroBusquedaNombre) {
		
		final List<Piloto> pilotos = new ArrayList<>();
		
		Block<Piloto> saveBlock = new Block<Piloto>() {
		    @Override
		    public void apply(Piloto piloto) {
		        pilotos.add(piloto);
		    }
		};
		
		collection.find(regex("nombreCompleto", "(?i)(^.*" + parametroBusquedaNombre + ".*$)")).forEach(saveBlock);
		
		return pilotos;
	}

   	public void piloto_update(Piloto piloto){
   		collection.updateOne(
   				eq("id", piloto.getId()) , 
   				combine(
   						set("nombreCompleto",piloto.getNombreCompleto()), 
   						set("fecha_Nacimiento",piloto.getFecha_Nacimiento()), 
   						set("lugarNacimiento",piloto.getLugarNacimiento()),
   						set("foto_ref",piloto.getFoto_ref()),
   						set("cant_podiosTotales",piloto.getCant_podiosTotales()),
   						set("cant_puntosTotales", piloto.getCant_puntosTotales()),
   						set("cant_granPremiosIngresado", piloto.getCant_granPremiosIngresado())
   						)
   				);
   	}
   	
	public void piloto_delete(String id){
		collection.deleteOne(eq("id_str", id));
	}
	
	public void piloto_deleteByName(String nombreCompleto){
		collection.deleteOne(eq("nombreCompleto",nombreCompleto));
		
	}
	
	public void piloto_update_calificacion(String id, float calificacion){
   		collection.updateOne(
   				eq("id_str", id) , 
   				combine(
   						set("calificacion",calificacion)
   						)
   				);
   	}

}

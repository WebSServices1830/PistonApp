package co.javeriana.webservice;

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

@WebService(name="crud_piloto")
public class CRUD_Piloto {
	
	MongoClient mongoClient = MongoClients.create();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Piloto> collection = database.getCollection("pilotos", Piloto.class);
    
    // Create a gridFSBucket with a custom bucket name "files"
    GridFSBucket gridFSFilesBucket = GridFSBuckets.create(database, "files");
	
    @WebMethod
	public void create(
			@WebParam(name = "nombreCompleto")String nombreCompleto,
			@WebParam(name = "fecha_Nacimiento")Date fecha_Nacimiento,
			@WebParam(name = "lugarNacimiento")String lugarNacimiento,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "cant_podiosTotales")int cant_podiosTotales,
			@WebParam(name = "cant_puntosTotales")int cant_puntosTotales,
			@WebParam(name = "cant_granPremiosIngresado")int cant_granPremiosIngresado
			){
			
		Piloto piloto = new Piloto(nombreCompleto,fecha_Nacimiento,lugarNacimiento,foto_ref,cant_podiosTotales,cant_puntosTotales,cant_granPremiosIngresado);
		collection.insertOne(piloto);
	}
	
	@WebMethod
	public Piloto read(@WebParam(name = "id")String id){
		Piloto piloto = collection.find(eq("id", id)).first();
		return piloto;
	}
	
	@WebMethod
	public Piloto readByName(@WebParam(name = "id")String id){
		
		Piloto piloto = null;
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("pilotos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String name = doc.getString("nombreCompleto");
		            
		            if (name.equals(id)) {
		            	System.out.println("@info/: 'readByName'  ->  piloto: '" + name + "' encontrado.");
						return piloto = 
								new Piloto(
										doc.getString ("nombreCompleto"), 
										doc.getDate   ("fecha_Nacimiento"), 
										doc.getString ("lugarNacimiento"), 
										doc.getString ("foto_ref"), 
										doc.getInteger("cant_podiosTotales"), 
										doc.getInteger("cant_puntosTotales"), 
										doc.getInteger("cant_granPremiosIngresado"));
					}
		        }
		    } finally {
		    	cursor.close();
		    }
		} catch (MongoException e) {
		    e.printStackTrace();
		}
		
		return null;
		
	}
	
	@WebMethod
	public void update(
			@WebParam(name = "id")String id,
			@WebParam(name = "nombreCompleto")String nombreCompleto,
			@WebParam(name = "fecha_Nacimiento")Date fecha_Nacimiento,
			@WebParam(name = "lugarNacimiento")String lugarNacimiento,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "cant_podiosTotales")int cant_podiosTotales,
			@WebParam(name = "cant_puntosTotales")int cant_puntosTotales,
			@WebParam(name = "cant_granPremiosIngresado")int cant_granPremiosIngresado
			){
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("nombreCompleto",nombreCompleto), 
						set("fecha_Nacimiento",fecha_Nacimiento), 
						set("lugarNacimiento",lugarNacimiento),
						set("foto_ref",foto_ref),
						set("cant_podiosTotales",cant_podiosTotales),
						set("cant_puntosTotales",cant_granPremiosIngresado)
						) 
				);
	}
	
	
	
	@WebMethod
	public void updateFromAndroid(
			@WebParam(name = "nombreCompleto")String nombreCompleto,
			@WebParam(name = "fecha_Nacimiento")Date fecha_Nacimiento,
			@WebParam(name = "lugarNacimiento")String lugarNacimiento,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "cant_podiosTotales")int cant_podiosTotales,
			@WebParam(name = "cant_puntosTotales")int cant_puntosTotales,
			@WebParam(name = "cant_granPremiosIngresado")int cant_granPremiosIngresado
			){
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("pilotos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String name  = doc.getString("nombreCompleto");
		            String place = doc.getString("lugarNacimiento");
		            
		            if (name.equals(nombreCompleto)) {
						
						collection.updateOne(
								eq("nombreCompleto", name) , 
								combine(
										set("nombreCompleto",nombreCompleto), 
										set("fecha_Nacimiento",fecha_Nacimiento), 
										set("lugarNacimiento",lugarNacimiento),
										set("foto_ref",foto_ref),
										set("cant_podiosTotales",cant_podiosTotales),
										set("cant_puntosTotales",cant_granPremiosIngresado)
										) 
								);
					}
		        }
		        
		        System.out.println("@info/: 'updateFromAndroid'  ->  piloto: '" + nombreCompleto + "' actualizado.");
		        
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
	public boolean deleteByName(@WebParam(name = "id")String id){
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("pilotos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String name  = doc.getString("nombreCompleto");
		            
		            if (name.equals(id)) {
		            	collection.deleteOne(eq("nombreCompleto", id));
		        		System.out.println("@info/: 'deleteByName'  ->  piloto: '" + id + "' eliminado.");
		        		return true;
						
					}
		        }
		        
		    } finally {
		    	cursor.close();
		    }
		} catch (MongoException e) {
		    e.printStackTrace();
		}
		
		return false;
		
	}

}
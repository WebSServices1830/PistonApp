package co.javeriana.webservice;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

@WebService(name="crud_auto")
public class CRUD_Auto {
	
	MongoClient mongoClient = MongoClients.create();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Auto> collection = database.getCollection("autos", Auto.class);
    
    // Create a gridFSBucket with a custom bucket name "files"
    GridFSBucket gridFSFilesBucket = GridFSBuckets.create(database, "files");
	
    @WebMethod
	public void create(
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "pesoEnKg")double pesoEnKg,
			@WebParam(name = "ruedas")String ruedas,
			@WebParam(name = "combustible")String combustible,
			@WebParam(name = "foto_ref")String foto_ref){
			
		Auto auto = new Auto(nombre,pesoEnKg,ruedas,combustible,foto_ref);
		collection.insertOne(auto);
	}
	
	@WebMethod
	public Auto read(@WebParam(name = "id")String id){
		Auto auto = collection.find(eq("id", id)).first();
		return auto;
	}
	
	@WebMethod
	public Auto readByName(@WebParam(name = "id")String id){
		
		Auto auto = null;
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("autos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String name = doc.getString("modelo");
		            
		            if (name.equals(id)) {
		            	System.out.println("@info/: 'readByName'  ->  name: '" + name + "' encontrado.");
						return auto = 
								new Auto(
										doc.getString ("nombre"), 
										doc.getDouble ("pesoEnKg"), 
										doc.getString ("ruedas"), 
										doc.getString ("combustible"), 
										doc.getString("foto_ref"));
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
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "pesoEnKg")double pesoEnKg,
			@WebParam(name = "ruedas")String ruedas,
			@WebParam(name = "combustible")String combustible,
			@WebParam(name = "foto_ref")String foto_ref){
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("nombre",nombre), 
						set("pesoEnKg",pesoEnKg), 
						set("ruedas",ruedas),
						set("combustible",combustible),
						set("foto_ref",foto_ref)
						) 
				);
	}
	
	
	
	@WebMethod
	public void updateFromAndroid(
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "pesoEnKg")double pesoEnKg,
			@WebParam(name = "ruedas")String ruedas,
			@WebParam(name = "combustible")String combustible,
			@WebParam(name = "foto_ref")String foto_ref){
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("autos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String name  = doc.getString("nombre");
		            
		            if (name.equals(nombre)) {
						
						collection.updateOne(
								eq("nombre", nombre) , 
								combine(
										set("nombre",nombre), 
										set("pesoEnKg",pesoEnKg), 
										set("ruedas",ruedas),
										set("combustible",combustible),
										set("foto_ref",foto_ref)
										) 
								);
					}
		        }
		        
		        System.out.println("@info/: 'updateFromAndroid'  ->  auto: '" + nombre + "' actualizado.");
		        
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
		    MongoCollection < Document > collection = db.getCollection("autos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String name  = doc.getString("nombre");
		            
		            if (name.equals(id)) {
		            	collection.deleteOne(eq("nombre", id));
		        		System.out.println("@info/: 'deleteByName'  ->  auto: '" + id + "' eliminado.");
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

package co.javeriana.webservice;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;

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
			@WebParam(name = "foto")String foto,
			@WebParam(name = "chasis")String chasis,
			@WebParam(name = "electronica")String electronica,
			@WebParam(name = "neumaticos")String neumaticos,
			@WebParam(name = "frenos")String frenos,
			@WebParam(name = "defensas")String defensas,
			@WebParam(name = "modelo")String modelo){
			
		Auto auto = new Auto(foto,chasis,electronica,neumaticos,frenos,defensas,modelo);
		collection.insertOne(auto);
	}
	
	@WebMethod
	public Auto read(@WebParam(name = "id")String id){
		Auto auto = collection.find(eq("id", id)).first();
		return auto;
	}
	
	@WebMethod
	public Auto readByModel(@WebParam(name = "id")String id){
		
		Auto auto = null;
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("autos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String model = doc.getString("modelo");
		            
		            if (model.equals(id)) {
		            	System.out.println("@info/: 'readByModel'  ->  model: '" + model + "' encontrado.");
						return auto = 
								new Auto(
										doc.getString ("foto"), 
										doc.getString ("chasis"), 
										doc.getString ("electronica"), 
										doc.getString ("neumaticos"), 
										doc.getString("frenos"), 
										doc.getString("defensas"), 
										doc.getString("modelo"));
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
			@WebParam(name = "foto")String foto,
			@WebParam(name = "chasis")String chasis,
			@WebParam(name = "electronica")String electronica,
			@WebParam(name = "neumaticos")String neumaticos,
			@WebParam(name = "frenos")String frenos,
			@WebParam(name = "defensas")String defensas,
			@WebParam(name = "modelo")String modelo
			){
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("foto",foto), 
						set("chasis",chasis), 
						set("electronica",electronica),
						set("neumaticos",neumaticos),
						set("frenos",frenos),
						set("defensas",defensas),
						set("modelo",modelo)
						) 
				);
	}
	
	
	
	@WebMethod
	public void updateFromAndroid(
			@WebParam(name = "foto")String foto,
			@WebParam(name = "chasis")String chasis,
			@WebParam(name = "electronica")String electronica,
			@WebParam(name = "neumaticos")String neumaticos,
			@WebParam(name = "frenos")String frenos,
			@WebParam(name = "defensas")String defensas,
			@WebParam(name = "modelo")String modelo){
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("autos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String model  = doc.getString("modelo");
		            
		            if (model.equals(modelo)) {
						
						collection.updateOne(
								eq("modelo", model) , 
								combine(
										set("foto",foto), 
										set("chasis",chasis), 
										set("electronica",electronica),
										set("neumaticos",neumaticos),
										set("frenos",frenos),
										set("defensas",defensas),
										set("modelo",modelo)
										) 
								);
					}
		        }
		        
		        System.out.println("@info/: 'updateFromAndroid'  ->  auto: '" + modelo + "' actualizado.");
		        
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
	public boolean deleteByModel(@WebParam(name = "id")String id){
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("autos");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String model  = doc.getString("modelo");
		            
		            if (model.equals(id)) {
		            	collection.deleteOne(eq("modelo", id));
		        		System.out.println("@info/: 'deleteByModel'  ->  auto: '" + id + "' eliminado.");
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

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

@WebService(name="crud_escuderia")
public class CRUD_Escuderia {
	
MongoClient mongoClient = MongoClients.create();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Escuderia> collection = database.getCollection("escuderias", Escuderia.class);
	
    @WebMethod
	public void create(
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "lugarBase")String lugarBase,
			@WebParam(name = "jefeEquipo")String jefeEquipo,
			@WebParam(name = "jefeTecnico")String jefeTecnico,
			@WebParam(name = "chasis")String chasis,
			@WebParam(name = "cant_vecesEnPodio")int cant_vecesEnPodio,
			@WebParam(name = "cant_titulosCampeonato")int cant_TitulosCampeonato,
			@WebParam(name = "fotoEscudo_ref")String fotoEscudo_ref){
			
		Escuderia escuderia = new Escuderia(nombre, lugarBase, jefeTecnico, jefeEquipo, chasis,
				cant_vecesEnPodio, cant_TitulosCampeonato, fotoEscudo_ref);
		collection.insertOne(escuderia);
	}
	
	@WebMethod
	public Escuderia read(@WebParam(name = "id")String id){
		Escuderia escuderia = collection.find(eq("id", id)).first();
		return escuderia;
	}
	
	@WebMethod
	public Escuderia readByName(@WebParam(name = "id")String id){
		
		Escuderia escuderia = null;
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("escuderias");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            String name = doc.getString("nombre");
		            
		            if (name.equals(id)) {
		            	System.out.println("@info/: 'readByName'  ->  escuderia: '" + name + "' encontrado.");
						return escuderia = 
								new Escuderia(
										doc.getString ("nombre"), 
										doc.getString  ("lugarBase"), 
										doc.getString ("jefeEquipo"), 
										doc.getString ("jefeTecnico"), 
										doc.getString("chasis"), 
										doc.getInteger("cant_vecesEnPodio"), 
										doc.getInteger("cant_titulosCampeonato"),
										doc.getString("fotoEscudo_ref"));
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

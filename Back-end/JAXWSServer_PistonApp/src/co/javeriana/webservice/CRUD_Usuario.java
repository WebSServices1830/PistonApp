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

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;

@WebService(name="crud_usuario")
public class CRUD_Usuario {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Usuario> collection = database.getCollection("usuario", Usuario.class);
    
    @WebMethod
   	public void create(
   			@WebParam(name = "nombreUsuario")String nombreUsuario,
   			@WebParam(name = "contra")String contra,
   			@WebParam(name = "edad")int edad,
   			@WebParam(name = "descripcion")String descripcion,
   			@WebParam(name = "foto")String foto,
   			@WebParam(name = "admin")boolean admin,
   			@WebParam(name = "bolsillo")long bolsillo
   			){
   			
   		Usuario usuario = new Usuario(nombreUsuario,contra,edad,descripcion,foto, admin, bolsillo);
   		collection.insertOne(usuario);
   	}
   	
    @WebMethod
	public Usuario read(@WebParam(name = "id")String id){
		Usuario usuario = collection.find(eq("id", id)).first();
		return usuario;
	}
	
	@WebMethod
	public Usuario readByName(@WebParam(name = "nombreUsuario")String nombreUsuario){
		Usuario usuario = collection.find(eq("nombreUsuario", nombreUsuario)).first();
		return usuario;
	}
	
	@WebMethod
	public List<Usuario> readAll() {
		
		final List<Usuario> usuarios = new ArrayList<>();
		
		Block<Usuario> saveBlock = new Block<Usuario>() {
		    @Override
		    public void apply(Usuario usuario) {
		        usuarios.add(usuario);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return usuarios;
		
	}
   	
   	@WebMethod
   	public void update(
   			
   			@WebParam(name = "id")String id,
   			@WebParam(name = "nombreUsuario")String nombreUsuario,
   			@WebParam(name = "contra")String contra,
   			@WebParam(name = "edad")int edad,
   			@WebParam(name = "descripcion")String descripcion,
   			@WebParam(name = "foto")String foto,
   			@WebParam(name = "admin")boolean admin,
   			@WebParam(name = "bolsillo")long bolsillo
   			){
   		collection.updateOne(
   				eq("id", id) , 
   				combine(
   						set("nombreUsuario",nombreUsuario), 
   						set("contra",contra), 
   						set("edad",edad),
   						set("descripcion",descripcion),
   						set("foto",foto),
   						set("admin",admin),
   						set("bolsillo", bolsillo)
   						
   						) 
   				);
   	}
   	
   	
   	
   	@WebMethod
   	public void updateFromAndroid(
   			@WebParam(name = "nombreUsuario")String nombreUsuario,
   			@WebParam(name = "contra")String contra,
   			@WebParam(name = "edad")int edad,
   			@WebParam(name = "descripcion")String descripcion,
   			@WebParam(name = "foto")String foto,
   			@WebParam(name = "admin")boolean admin,
   			@WebParam(name = "bolsillo")long bolsillo
   			){
   		
   		try {
   		    MongoDatabase db = database;
   		    MongoCollection < Document > collection = db.getCollection("usuarios");
   		    MongoCursor < Document > cursor = collection.find().iterator();
   		    try {
   		        while (cursor.hasNext()) {
   		            Document doc = cursor.next();
   		            String name  = doc.getString("nombreUsuario");
   		            
   		            if (name.equals(nombreUsuario)) {
   						
   						collection.updateOne(
   								eq("nombreUsuario", name) , 
   								combine(
   										set("nombreUsuario",nombreUsuario), 
   				   						set("contra",contra), 
   				   						set("edad",edad),
   				   						set("descripcion",descripcion),
   				   						set("foto",foto),
   				   						set("admin",admin),
   				   						set("bolsillo", bolsillo)
   										) 
   								);
   					}
   		        }
   		        
   		        System.out.println("@info/: 'updateFromAndroid'  ->  piloto: '" + nombreUsuario + "' actualizado.");
   		        
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
	public void deleteByName(@WebParam(name = "id")String nombreUsuario){
		collection.deleteOne(eq("nombreUsuario",nombreUsuario));
		
	}


}

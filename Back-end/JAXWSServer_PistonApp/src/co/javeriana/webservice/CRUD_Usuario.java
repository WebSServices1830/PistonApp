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
   	public void usuario_create(
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
	public Usuario usuario_read(@WebParam(name = "id")String id){
		Usuario usuario = collection.find(eq("id", id)).first();
		return usuario;
	}
	
	@WebMethod
	public Usuario usuario_readByName(@WebParam(name = "nombreUsuario")String nombreUsuario){
		Usuario usuario = collection.find(eq("nombreUsuario", nombreUsuario)).first();
		return usuario;
	}
	
	@WebMethod
	public List<Usuario> usuario_readAll() {
		
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
   	public void usuario_update(
   			@WebParam(name = "nombreUsuario")String nombreUsuario,
   			@WebParam(name = "contra")String contra,
   			@WebParam(name = "edad")int edad,
   			@WebParam(name = "descripcion")String descripcion,
   			@WebParam(name = "foto")String foto,
   			@WebParam(name = "admin")boolean admin,
   			@WebParam(name = "bolsillo")long bolsillo
   			){
   		collection.updateOne(
   				eq("nombreUsuario", nombreUsuario) , 
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
	public void usuario_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void usuario_deleteByName(@WebParam(name = "id")String nombreUsuario){
		collection.deleteOne(eq("nombreUsuario",nombreUsuario));
		
	}


}

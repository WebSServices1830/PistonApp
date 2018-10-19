package CRUDs;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

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

import clases_mongoDB.ClienteMongo;
import clases_negocio.Usuario;

public class CRUD_Usuario {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Usuario> collection = database.getCollection("usuario", Usuario.class);
    
    
    public void usuario_create(Usuario usuario) {
    	collection.insertOne(usuario);
    }
    
    public Usuario usuario_get(String id) {
    	Usuario usuario = collection.find(eq("id", id)).first();
    	return usuario;
    }
   	
    public Usuario usuario_getByName(String nombreUsuario) {
    	Usuario usuario = collection.find(eq("nombreUsuario", nombreUsuario)).first();
    	return usuario;
    }
    
    public boolean existeUsuario(String nombreUsuario) {
    	Usuario usuario = usuario_getByName(nombreUsuario);
    	if(usuario == null) {
    		return false;
    	}
    	
    	return true;
    }
	
	public List<Usuario> usuario_getAll() {
		
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
   	
   	public void usuario_update(Usuario usuario){
   		collection.updateOne(
   				eq("id", usuario.getId()) , 
   				combine(
   						set("nombreUsuario",usuario.getNombreUsuario()), 
   						set("contra",usuario.getContra()), 
   						set("fechaNacimiento",usuario.getFechaNacimiento()),
   						set("urlFoto",usuario.getUrlFoto()),
   						set("admin",usuario.isAdmin()),
   						set("bolsillo", usuario.getBolsillo())
   						)
   				);
   	}
   	
	public void usuario_delete(String id){
		collection.deleteOne(eq("id", id));
	}
	
	public void usuario_deleteByName(String nombreUsuario){
		collection.deleteOne(eq("nombreUsuario",nombreUsuario));
		
	}


}

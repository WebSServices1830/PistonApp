package co.javeriana.webservice;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@WebService(name="crud_comentariopista")
public class CRUD_ComentarioPista {

MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<ComentarioPista> collection = database.getCollection("comentariopista", ComentarioPista.class);
	
    @WebMethod
	public void comentariopiloto_create(
			@WebParam(name = "contenido")String contenido,
			@WebParam(name = "calificacion")int calificacion,
			@WebParam(name = "pista")ObjectId pista,
			@WebParam(name = "usuario")ObjectId usuario
			){
			
		ComentarioPista comentario = new ComentarioPista(contenido,calificacion,pista,usuario);
		collection.insertOne(comentario);
	}
	
	@WebMethod
	public ComentarioPista comentariopista_read(@WebParam(name = "id")String id){
		ComentarioPista comentario = collection.find(eq("id", id)).first();
		return comentario;
	}

	
	@WebMethod
	public List<ComentarioPista> comentariopista_readAll() {
		
		final List<ComentarioPista> comentarios = new ArrayList<>();
		
		Block<ComentarioPista> saveBlock = new Block<ComentarioPista>() {
		    @Override
		    public void apply(ComentarioPista comentario) {
		        comentarios.add(comentario);
		    }

			
		};
		
		collection.find().forEach(saveBlock);
		
		return comentarios;
		
	}
	
	@WebMethod
	public void comentariopista_update(
			@WebParam(name = "id")String id,
			@WebParam(name = "contenido")String contenido,
			@WebParam(name = "calificacion")int calificacion,
			@WebParam(name = "pista")ObjectId pista,
			@WebParam(name = "usuario")ObjectId usuario
			){
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("contenido",contenido), 
						set("calificacion",calificacion), 
						set("pista",pista),
						set("usuario",usuario)
						) 
				);
	}
	
	
	
	@WebMethod
	public void comentariopiloto_updateFromAndroid(
			@WebParam(name = "contenido")String contenido,
			@WebParam(name = "calificacion")int calificacion,
			@WebParam(name = "pista")ObjectId pista,
			@WebParam(name = "usuario")ObjectId usuario
			){
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("comentariopista");
		    MongoCursor < Document > cursor = collection.find().iterator();
		    try {
		        while (cursor.hasNext()) {
		            Document doc = cursor.next();
		            ObjectId p  = doc.getObjectId("usuario");
		            
		            if (p.equals(usuario)) {
						
						collection.updateOne(
								eq("usuario", usuario) , 
								combine(
										set("contenido",contenido), 
										set("calificacion",calificacion), 
										set("pista",pista),
										set("usuario",usuario)
										) 
								);
					}
		        }
		        
		        System.out.println("@info/: 'updateFromAndroid'  ->  Comentario a piloto: '" + usuario+"--->"+ pista + "' actualizado.");
		        
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
	public void deleteByName(@WebParam(name = "id")ObjectId id){
		collection.deleteOne(eq("id",id));
		
	}
}

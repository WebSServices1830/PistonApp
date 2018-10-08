package co.javeriana.webservice;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalTime;
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

@WebService(name="crud_comentariopiloto")
public class CRUD_ComentarioPiloto {

MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<ComentarioPiloto> collection = database.getCollection("comentariopiloto", ComentarioPiloto.class);
	
    @WebMethod
	public void comentariopiloto_create(
			@WebParam(name = "contenido")String contenido,
			@WebParam(name = "calificacion")int calificacion,
			@WebParam(name = "usuario")ObjectId usuario,
			@WebParam(name = "piloto")ObjectId piloto
			){
			
		ComentarioPiloto comentario = new ComentarioPiloto(contenido,calificacion,usuario,piloto);
		collection.insertOne(comentario);
	}
	
	@WebMethod
	public ComentarioPiloto comentariopiloto_read(@WebParam(name = "id")String id){
		ComentarioPiloto comentario = collection.find(eq("id", id)).first();
		return comentario;
	}

	
	@WebMethod
	public List<ComentarioPiloto> comentariopiloto_readAll() {
		
		final List<ComentarioPiloto> comentarios = new ArrayList<>();
		
		Block<ComentarioPiloto> saveBlock = new Block<ComentarioPiloto>() {
		    @Override
		    public void apply(ComentarioPiloto comentario) {
		        comentarios.add(comentario);
		    }

			
		};
		
		collection.find().forEach(saveBlock);
		
		return comentarios;
		
	}
	
	@WebMethod
	public void comentariopiloto_update(
			@WebParam(name = "id")String id,
			@WebParam(name = "contenido")String contenido,
			@WebParam(name = "calificacion")int calificacion,
			@WebParam(name = "usuario")ObjectId usuario,
			@WebParam(name = "piloto")ObjectId piloto
			){
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("contenido",contenido), 
						set("calificacion",calificacion), 
						set("usuario",usuario),
						set("piloto",piloto)
						) 
				);
	}
	
	
	
	@WebMethod
	public void comentariopiloto_updateFromAndroid(
			@WebParam(name = "contenido")String contenido,
			@WebParam(name = "calificacion")int calificacion,
			@WebParam(name = "usuario")ObjectId usuario,
			@WebParam(name = "piloto")ObjectId piloto
			){
		
		try {
		    MongoDatabase db = database;
		    MongoCollection < Document > collection = db.getCollection("comentariopiloto");
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
										set("usuario",usuario),
										set("piloto",piloto)
										) 
								);
					}
		        }
		        
		        System.out.println("@info/: 'updateFromAndroid'  ->  Comentario a piloto: '" + usuario+"--->"+ piloto + "' actualizado.");
		        
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

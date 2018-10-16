package CRUDs;

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

import clases_mongoDB.ClienteMongo;
import clases_negocio.Escuderia;

@WebService(name="crud_escuderia")
public class CRUD_Escuderia {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Escuderia> collection = database.getCollection("escuderias", Escuderia.class);
	
    @WebMethod
	public void escuderia_create(
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "lugarBase")String lugarBase,
			@WebParam(name = "jefeEquipo")String jefeEquipo,
			@WebParam(name = "jefeTecnico")String jefeTecnico,
			@WebParam(name = "chasis")String chasis,
			@WebParam(name = "cant_vecesEnPodio")int cant_vecesEnPodio,
			@WebParam(name = "cant_TitulosCampeonato")int cant_TitulosCampeonato,
			@WebParam(name = "fotoEscudo_ref")String fotoEscudo_ref
			){
			
		Escuderia escuderia = new Escuderia(nombre,lugarBase,jefeEquipo,jefeTecnico,chasis,cant_vecesEnPodio,cant_TitulosCampeonato,fotoEscudo_ref);
		collection.insertOne(escuderia);
	}
	
	@WebMethod
	public Escuderia escuderia_read(@WebParam(name = "id")String id){
		Escuderia escuderia = collection.find(eq("id", id)).first();
		return escuderia;
	}
	
	@WebMethod
	public Escuderia escuderia_readByName(@WebParam(name = "nombre")String nombre){
		Escuderia escuderia = collection.find(eq("nombre", nombre)).first();
		return escuderia;
	}
	
	@WebMethod
	public List<Escuderia> escuderia_readAll() {
		
		final List<Escuderia> escuderias = new ArrayList<>();
		
		Block<Escuderia> saveBlock = new Block<Escuderia>() {
		    @Override
		    public void apply(Escuderia escuderia) {
		        escuderias.add(escuderia);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return escuderias;
		
	}
	
	@WebMethod
	public void escuderia_update(
			@WebParam(name = "nombre")String nombre,
			@WebParam(name = "lugarBase")String lugarBase,
			@WebParam(name = "jefeEquipo")String jefeEquipo,
			@WebParam(name = "jefeTecnico")String jefeTecnico,
			@WebParam(name = "chasis")String chasis,
			@WebParam(name = "cant_vecesEnPodio")int cant_vecesEnPodio,
			@WebParam(name = "cant_TitulosCampeonato")int cant_TitulosCampeonato,
			@WebParam(name = "fotoEscudo_ref")String fotoEscudo_ref
			){
		collection.updateOne(
				eq("nombre", nombre) , 
				combine(
						set("nombre",nombre), 
						set("lugarBase",lugarBase), 
						set("jefeEquipo",jefeEquipo),
						set("jefeTecnico",jefeTecnico),
						set("chasis",chasis),
						set("cant_vecesEnPodio",cant_vecesEnPodio),
						set("cant_TitulosCampeonato",cant_TitulosCampeonato),
						set("fotoEscudo_ref",fotoEscudo_ref)
						) 
				);
	}
	
	@WebMethod
	public void escuderia_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void escuderia_deleteByName(@WebParam(name = "nombre")String nombre){
		collection.deleteOne(eq("nombre",nombre));
		
	}

}

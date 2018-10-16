package CRUDs;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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

import clases_mongoDB.ClienteMongo;
import clases_negocio.GranPremio;

@WebService(name="crud_granpremio")
public class CRUD_GranPremio {
MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<GranPremio> collection = database.getCollection("grandespremios", GranPremio.class);
	
    @WebMethod
	public void granPremio_create(
			@WebParam(name = "fecha")Date fecha,
			@WebParam(name = "cantVueltas")int cantVueltas,
			@WebParam(name = "mejorVuelta")LocalTime mejorVuelta,
			@WebParam(name = "pista")ObjectId pista,
			@WebParam(name = "campeonato")ObjectId campeonato
			){
			
		GranPremio granPremio = new GranPremio(fecha,cantVueltas,mejorVuelta,pista,campeonato);
		collection.insertOne(granPremio);
	}
	
	@WebMethod
	public GranPremio granPremio_read(@WebParam(name = "id")String id){
		GranPremio granPremio = collection.find(eq("id", id)).first();
		return granPremio;
	}

	
	@WebMethod
	public List<GranPremio> granPremio_readAll() {
		
		final List<GranPremio> grandesPremios = new ArrayList<>();
		
		Block<GranPremio> saveBlock = new Block<GranPremio>() {
		    @Override
		    public void apply(GranPremio granPremio) {
		        grandesPremios.add(granPremio);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return grandesPremios;
		
	}
	
	@WebMethod
	public void granPremio_update(
			@WebParam(name = "fecha")Date fecha,
			@WebParam(name = "cantVueltas")int cantVueltas,
			@WebParam(name = "mejorVuelta")LocalTime mejorVuelta,
			@WebParam(name = "pista")ObjectId pista,
			@WebParam(name = "campeonato")ObjectId campeonato
			){
		collection.updateOne(
				eq("fecha", fecha) , 
				combine(
						set("fecha",fecha), 
						set("cantVueltas",cantVueltas), 
						set("mejorVuelta",mejorVuelta),
						set("pista",pista),
						set("campeonato",campeonato)
						) 
				);
	}
	
	@WebMethod
	public void granPremio_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void granPremio_deleteByName(@WebParam(name = "id")Date fecha){
		collection.deleteOne(eq("fecha",fecha));
		
	}
	
	@WebMethod
	public void granPremio_addClasificacion(
			@WebParam(name = "id")String id, 
			@WebParam(name = "idClasificacion")ObjectId idClasificacion
			){
		GranPremio granPremio= granPremio_read(id);
		granPremio.getClasificaciones().add(idClasificacion);
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("clasificaciones",granPremio.getClasificaciones())
						) 
				);
	}
	
}

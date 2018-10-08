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

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebService(name="crud_pista")
public class CRUD_Pista {
	
MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Pista> collection = database.getCollection("pistas", Pista.class);
	
    @WebMethod
	public void pista_create(
			@WebParam(name = "ciudad")String ciudad,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "nombreUltimoGanador")String nombreUltimoGanador,
			@WebParam(name = "numeroVueltas")int numeroVueltas,
			@WebParam(name = "distanciaCarreara_km")float distanciaCarreara_km,
			@WebParam(name = "longitudCircuito_km")float longitudCircuito_km,
			@WebParam(name = "record")ObjectId record,
			@WebParam(name = "granpremio")ObjectId granpremio){
			
		Pista pista = new Pista(ciudad,foto_ref,nombreUltimoGanador,numeroVueltas,distanciaCarreara_km,longitudCircuito_km,record,granpremio);
		collection.insertOne(pista);
	}
	
    @WebMethod
	public Pista pista_read(@WebParam(name = "id")String id){
		Pista pista = collection.find(eq("id", id)).first();
		return pista;
	}
	
	@WebMethod
	public Pista pista_readByCiudad(@WebParam(name = "ciudad")String ciudad){
		Pista pista= collection.find(eq("ciudad", ciudad)).first();
		return pista;
	}
	
	@WebMethod
	public List<Pista> pista_readAll() {
		
		final List<Pista> pistas = new ArrayList<>();
		
		Block<Pista> saveBlock = new Block<Pista>() {
		    @Override
		    public void apply(Pista pista) {
		        pistas.add(pista);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return pistas;
		
	}
	
	@WebMethod
	public void pista_update(
			@WebParam(name = "ciudad")String ciudad,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "nombreUltimoGanador")String nombreUltimoGanador,
			@WebParam(name = "numeroVueltas")int numeroVueltas,
			@WebParam(name = "distanciaCarreara_km")float distanciaCarreara_km,
			@WebParam(name = "longitudCircuito_km")float longitudCircuito_km,
			@WebParam(name = "record")ObjectId record,
			@WebParam(name = "granpremio")ObjectId granpremio){
		collection.updateOne(
				eq("ciudad", ciudad) , 
				combine(
						set("ciudad",ciudad), 
						set("foto_ref",foto_ref), 
						set("nombreUltimoGanador",nombreUltimoGanador),
						set("numeroVueltas",numeroVueltas),
						set("distanciaCarreara_km",distanciaCarreara_km), 
						set("longitudCircuito_km",longitudCircuito_km), 
						set("record",record),
						set("granpremio",granpremio)
						) 
				);
	}
	
	@WebMethod
	public void pista_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void pista_deleteByCiudad(@WebParam(name = "ciudad")String ciudad){
		collection.deleteOne(eq("ciudad",ciudad));
	}

}

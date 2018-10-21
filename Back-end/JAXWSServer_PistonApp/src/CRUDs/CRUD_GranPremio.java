package CRUDs;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

public class CRUD_GranPremio {
MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<GranPremio> collection = database.getCollection("grandespremios", GranPremio.class);
	
	public GranPremio granPremio_create(
			Date fecha,
			int cantVueltas,
			Date mejorVuelta,
			String pista,
			String campeonato
			){
			
		GranPremio granPremio = new GranPremio(fecha,cantVueltas,mejorVuelta,pista,campeonato);
		collection.insertOne(granPremio);
		return granPremio;
	}
	
	public GranPremio granPremio_read(@WebParam(name = "id")String id){
		GranPremio granPremio = collection.find(eq("id_str", id)).first();
		return granPremio;
	}
	
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
	
public List<GranPremio> granPremio_readAll_X_campeonato(String id_campeonato) {
		
		final List<GranPremio> grandesPremios = new ArrayList<>();
		
		Block<GranPremio> saveBlock = new Block<GranPremio>() {
		    @Override
		    public void apply(GranPremio granPremio) {
		        grandesPremios.add(granPremio);
		    }
		};
		
		collection.find(eq("id_campeonato",id_campeonato)).forEach(saveBlock);
		
		return grandesPremios;
		
	}
	
	public List<GranPremio> grandesPremios_X_Fecha(String id_campeonato){
		List<GranPremio> lista = this.granPremio_readAll_X_campeonato(id_campeonato);
	
		Collections.sort(lista, new Comparator<GranPremio>() {
			  public int compare(GranPremio o1, GranPremio o2) {
			      return o1.getFecha().compareTo(o2.getFecha());
			  }
			});
		
		return lista;
		
	}
	
	public void granPremio_update(
			Date fecha,
			int cantVueltas,
			LocalTime mejorVuelta,
			String pista,
			String campeonato
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
	
	public void granPremio_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	public void granPremio_deleteByName(@WebParam(name = "id")Date fecha){
		collection.deleteOne(eq("fecha",fecha));
		
	}
	
	public void granPremio_addClasificacion(
			String id, 
			String idClasificacion
			){
		GranPremio granPremio= granPremio_read(id);
		granPremio.getId_clasificaciones().add(idClasificacion);
		collection.updateOne(
				eq("id", id) , 
				combine(
						set("clasificaciones",granPremio.getId_clasificaciones())
						) 
				);
	}
	
}

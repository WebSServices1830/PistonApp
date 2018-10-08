package co.javeriana.webservice;

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

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebService(name="crud_record")
public class CRUD_Record {
	
MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Record> collection = database.getCollection("records", Record.class);
	
    @WebMethod
	public void record_create(
			@WebParam(name = "recordVuleta_tiempo")LocalTime recordVuleta_tiempo,
			@WebParam(name = "recordVuelta_piloto")String recordVuelta_piloto,
			@WebParam(name = "recordVuelta_anio")int recordVuelta_anio,
			@WebParam(name = "pista")ObjectId pista){
			
		Record record = new Record(recordVuleta_tiempo,recordVuelta_piloto,recordVuelta_anio,pista);
		collection.insertOne(record);
	}
	
    @WebMethod
	public Record record_read(@WebParam(name = "id")String id){
		Record record = collection.find(eq("id", id)).first();
		return record;
	}
	
	@WebMethod
	public Record record_readByName(@WebParam(name = "recordVuelta_piloto")String recordVuelta_piloto){
		Record record= collection.find(eq("recordVuelta_piloto", recordVuelta_piloto)).first();
		return record;
	}
	
	@WebMethod
	public List<Record> record_readAll() {
		
		final List<Record> records = new ArrayList<>();
		
		Block<Record> saveBlock = new Block<Record>() {
		    @Override
		    public void apply(Record record) {
		        records.add(record);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return records;
		
	}
	
	@WebMethod
	public void record_update(
			@WebParam(name = "recordVuleta_tiempo")LocalTime recordVuleta_tiempo,
			@WebParam(name = "recordVuelta_piloto")String recordVuelta_piloto,
			@WebParam(name = "recordVuelta_anio")int recordVuelta_anio,
			@WebParam(name = "pista")ObjectId pista){
		collection.updateOne(
				eq("recordVuelta_piloto", recordVuelta_piloto) , 
				combine(
						set("recordVuleta_tiempo",recordVuleta_tiempo), 
						set("recordVuelta_piloto",recordVuelta_piloto), 
						set("recordVuelta_anio",recordVuelta_anio),
						set("pista",pista)
						) 
				);
	}
	
	@WebMethod
	public void record_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void record_deleteByName(@WebParam(name = "recordVuelta_piloto")String recordVuelta_piloto){
		collection.deleteOne(eq("recordVuelta_piloto",recordVuelta_piloto));
	}

}

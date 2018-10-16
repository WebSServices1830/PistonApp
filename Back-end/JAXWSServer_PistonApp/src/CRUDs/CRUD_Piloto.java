package CRUDs;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

//MongoDB imports
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import clases_mongoDB.ClienteMongo;
import clases_negocio.Piloto;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.client.gridfs.*;
import com.mongodb.client.gridfs.model.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.io.*;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import static com.mongodb.client.model.Filters.eq;

@WebService(name="crud_piloto")
public class CRUD_Piloto {
	
	MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// create codec registry for POJOs
	CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	
	// get handle to "PistonAppDB" database
    MongoDatabase database = mongoClient.getDatabase("PistonAppDB").withCodecRegistry(pojoCodecRegistry);
    
    // get a handle to the "people" collection
    MongoCollection<Piloto> collection = database.getCollection("pilotos", Piloto.class);
	
    @WebMethod
	public void piloto_create(
			@WebParam(name = "nombreCompleto")String nombreCompleto,
			@WebParam(name = "fecha_Nacimiento")Date fecha_Nacimiento,
			@WebParam(name = "lugarNacimiento")String lugarNacimiento,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "cant_podiosTotales")int cant_podiosTotales,
			@WebParam(name = "cant_puntosTotales")int cant_puntosTotales,
			@WebParam(name = "cant_granPremiosIngresado")int cant_granPremiosIngresado
			){
			
		Piloto piloto = new Piloto(nombreCompleto,fecha_Nacimiento,lugarNacimiento,foto_ref,cant_podiosTotales,cant_puntosTotales,cant_granPremiosIngresado);
		collection.insertOne(piloto);
	}
	
	@WebMethod
	public Piloto piloto_read(@WebParam(name = "id")String id){
		Piloto piloto = collection.find(eq("id", id)).first();
		return piloto;
	}
	
	@WebMethod
	public Piloto piloto_readByName(@WebParam(name = "nombreCompleto")String nombreCompleto){
		Piloto piloto = collection.find(eq("nombreCompleto", nombreCompleto)).first();
		return piloto;
	}
	
	@WebMethod
	public List<Piloto> piloto_readAll() {
		
		final List<Piloto> pilotos = new ArrayList<>();
		
		Block<Piloto> saveBlock = new Block<Piloto>() {
		    @Override
		    public void apply(Piloto piloto) {
		        pilotos.add(piloto);
		    }
		};
		
		collection.find().forEach(saveBlock);
		
		return pilotos;
		
	}
	
	@WebMethod
	public void piloto_update(
			@WebParam(name = "nombreCompleto")String nombreCompleto,
			@WebParam(name = "fecha_Nacimiento")Date fecha_Nacimiento,
			@WebParam(name = "lugarNacimiento")String lugarNacimiento,
			@WebParam(name = "foto_ref")String foto_ref,
			@WebParam(name = "cant_podiosTotales")int cant_podiosTotales,
			@WebParam(name = "cant_puntosTotales")int cant_puntosTotales,
			@WebParam(name = "cant_granPremiosIngresado")int cant_granPremiosIngresado
			){
		collection.updateOne(
				eq("nombreCompleto", nombreCompleto) , 
				combine(
						set("nombreCompleto",nombreCompleto), 
						set("fecha_Nacimiento",fecha_Nacimiento), 
						set("lugarNacimiento",lugarNacimiento),
						set("foto_ref",foto_ref),
						set("cant_podiosTotales",cant_podiosTotales),
						set("cant_puntosTotales",cant_granPremiosIngresado)
						) 
				);
	}
	
	@WebMethod
	public void piloto_delete(@WebParam(name = "id")String id){
		collection.deleteOne(eq("id", id));
	}
	
	@WebMethod
	public void piloto_deleteByName(@WebParam(name = "id")String nombreCompleto){
		collection.deleteOne(eq("nombreCompleto",nombreCompleto));
		
	}

}

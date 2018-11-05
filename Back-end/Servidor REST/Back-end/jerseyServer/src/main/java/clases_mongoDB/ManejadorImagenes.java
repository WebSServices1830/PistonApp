package clases_mongoDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

public class ManejadorImagenes {
	static MongoClient mongoClient = ClienteMongo.getInstancia();
	
	// get handle to "PistonAppDB" database
    static MongoDatabase database = mongoClient.getDatabase("PistonAppDB");
    
    // Create a gridFSBucket with a custom bucket name "files"
    static GridFSBucket gridFSFilesBucket = GridFSBuckets.create(database, "almacenamiento");
    
    public static String saveImageIntoMongoDB(String photoPath, String name) throws IOException{
    	InputStream streamToUploadFrom = new FileInputStream(new File(photoPath));
        // Create some custom options
        GridFSUploadOptions options = new GridFSUploadOptions()
                                            .chunkSizeBytes(358400)
                                            .metadata(new Document("type", "photo"));

        ObjectId fileId = gridFSFilesBucket.uploadFromStream(name, streamToUploadFrom, options);
    	return fileId.toString();
    }
}

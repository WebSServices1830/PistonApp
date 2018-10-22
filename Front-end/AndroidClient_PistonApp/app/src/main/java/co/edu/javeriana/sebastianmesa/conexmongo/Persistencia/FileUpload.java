package co.edu.javeriana.sebastianmesa.conexmongo.Persistencia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileUpload {
    static MongoClient mongoClient = ClienteMongo.getInstancia();

    // get handle to "PistonAppDB" database
    static MongoDatabase database = mongoClient.getDatabase("PistonAppDB");

    // Create a gridFSBucket with a custom bucket name "files"
    static GridFSBucket gridFSFilesBucket = GridFSBuckets.create(database, "almacenamiento");

    Bitmap bitmap = null;

    public static String saveImageIntoMongoDB(Drawable imageDrawable, String fileName) throws IOException {

        GridFSUploadOptions options = new GridFSUploadOptions()
                .chunkSizeBytes(358400)
                .metadata(new Document("type", "photo"));

        GridFSUploadStream uploadStream = gridFSFilesBucket.openUploadStream(fileName, options);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageDrawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();

        uploadStream.write(imageInByte);
        uploadStream.close();

        ObjectId fileId = uploadStream.getObjectId();

        return fileId.toString();

    }


}

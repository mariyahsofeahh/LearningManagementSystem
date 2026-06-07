package lms.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import lms.db.MongoConnection; // References your centralized cloud cluster credentials helper
import org.bson.Document;
import org.bson.types.ObjectId;

public class LearningMaterialService {

    private final MongoCollection<Document> collection;

    public LearningMaterialService() {
        // 1. Extract the active pool singleton database instance from DBConnection
        MongoDatabase db = MongoConnection.getDatabase();
        
        // 2. Select target cloud collection (Atlas generates this dynamically if it doesn't exist)
        this.collection = db.getCollection("learning_materials");
    }

    /**
     * Executes material validation and data store sequences sequentially.
     */
    public boolean processAndStoreMaterial(String name, String type, String path) {
        
        // Validation Guard: Verifies incoming file parameter integrity
        if (name == null || name.trim().isEmpty() || path == null) {
            return false; 
        }
        
        try {
            // Package your metadata properties into a clean BSON document payload
            Document doc = new Document()
                    .append("file_name", name.trim())
                    .append("file_type", type)
                    .append("file_path", path)
                    .append("upload_date", new Date()); // Persists live current server timestamp

            // Route transaction payload out over the network into your AWS Cloud database cluster
            collection.insertOne(doc);
            return true; // Returns success mapping back up the execution chain
            
        } catch (Exception e) {
            System.err.println("MongoDB Error caught during material insertion pipeline:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches metadata for all materials sorted by the newest upload date.
     */
    public List<Map<String, String>> getAllMaterials() {
        List<Map<String, String>> materialsList = new ArrayList<>();
        
        try {
            // Stream documents utilizing Sorts.descending matching your upload timestamps
            MongoCursor<Document> cursor = collection.find()
                                                    .sort(Sorts.descending("upload_date"))
                                                    .iterator();
            try {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    Map<String, String> material = new HashMap<>();
                    
                    // Convert Hexadecimal cloud Object IDs to Strings so your frontend JSPs can render them
                    material.put("id", doc.getObjectId("_id").toString());
                    material.put("fileName", doc.getString("file_name"));
                    material.put("fileType", doc.getString("file_type"));
                    material.put("filePath", doc.getString("file_path"));
                    
                    // Parse Date entries safely into standard printable string indicators
                    if (doc.get("upload_date") != null) {
                        material.put("uploadDate", doc.getDate("upload_date").toString());
                    } else {
                        material.put("uploadDate", "");
                    }
                    
                    materialsList.add(material);
                }
            } finally {
                cursor.close(); // Crucial: Safely drop connection cursor to prevent server memory leaks
            }
        } catch (Exception e) {
            System.err.println("MongoDB Error fetching your materials catalog collection:");
            e.printStackTrace();
        }
        return materialsList;
    }

    /**
     * Retrieves data parameters matching a unique alphanumeric Hex ObjectId pointer string.
     */
    public Map<String, String> getMaterialById(String id) {
        try {
            // Re-instantiate your text identifier back into a proper native org.bson.types.ObjectId
            ObjectId objectId = new ObjectId(id);
            
            // Execute a rapid query using the eq() static criteria filter matching your primary key
            Document doc = collection.find(eq("_id", objectId)).first();
            
            if (doc != null) {
                Map<String, String> material = new HashMap<>();
                material.put("id", doc.getObjectId("_id").toString());
                material.put("fileName", doc.getString("file_name"));
                material.put("fileType", doc.getString("file_type"));
                material.put("filePath", doc.getString("file_path"));
                return material;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("The provided string format is invalid for MongoDB ObjectId transformation: " + id);
        } catch (Exception e) {
            System.err.println("Error pulling custom material record from Atlas cloud container:");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Purges a unique document metadata entry out of the cloud data tier permanently.
     */
    public void deleteMaterial(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            
            // Fire target record disposal across the cluster matching the specific ObjectId
            collection.deleteOne(eq("_id", objectId));
            System.out.println("Document with ID " + id + " cleared from MongoDB Atlas successfully.");
            
        } catch (Exception e) {
            System.err.println("NoSQL transaction error caught while executing deleteOne operational routines:");
            e.printStackTrace();
        }
    }
}
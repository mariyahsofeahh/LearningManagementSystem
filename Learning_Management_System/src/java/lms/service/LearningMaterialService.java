package lms.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import static com.mongodb.client.model.Filters.eq;

import java.text.SimpleDateFormat;
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
    public boolean processAndStoreMaterial(String courseCode, String name, String type, String path) {
        
        // Validation Guard: Verifies incoming file parameter integrity
        if (courseCode == null || courseCode.trim().isEmpty()
                || name == null || name.trim().isEmpty() || path == null) {
            return false; 
        }
        
        try {
            // Package your metadata properties into a clean BSON document payload
            Document doc = new Document()
                    .append("course_code", courseCode.trim())
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

    public boolean processAndStoreMaterial(String name, String type, String path) {
        return processAndStoreMaterial("general", name, type, path);
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
                    materialsList.add(mapDocumentToMaterial(cursor.next()));
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

    public List<Map<String, String>> getMaterialsByCourse(String courseCode) {
        List<Map<String, String>> materialsList = new ArrayList<>();

        if (courseCode == null || courseCode.trim().isEmpty()) {
            return materialsList;
        }

        try {
            MongoCursor<Document> cursor = collection.find(eq("course_code", courseCode.trim()))
                    .sort(Sorts.descending("upload_date"))
                    .iterator();
            try {
                while (cursor.hasNext()) {
                    materialsList.add(mapDocumentToMaterial(cursor.next()));
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            System.err.println("MongoDB Error fetching course materials:");
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
                material.put("courseCode", doc.getString("course_code"));
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

    private Map<String, String> mapDocumentToMaterial(Document doc) {
        Map<String, String> material = new HashMap<>();
        material.put("id", doc.getObjectId("_id").toString());
        material.put("fileName", doc.getString("file_name"));
        material.put("fileType", doc.getString("file_type"));
        material.put("filePath", doc.getString("file_path"));
        material.put("courseCode", doc.getString("course_code"));
        material.put("uploadDate", formatDate(doc.get("upload_date"), "yyyy-MM-dd HH:mm"));
        material.put("sortDate", formatDate(doc.get("upload_date"), "yyyy-MM-dd HH:mm:ss"));
        return material;
    }

    private String formatDate(Object value, String pattern) {
        if (value == null) {
            return "";
        }
        if (value instanceof Date) {
            return new SimpleDateFormat(pattern).format((Date) value);
        }
        return value.toString();
    }
}

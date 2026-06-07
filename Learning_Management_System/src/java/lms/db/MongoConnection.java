/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lms.db;

/**
 *
 * @author ASUS
 */
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized Database Connection Utility for MongoDB Atlas Cloud
 */
public class MongoConnection {

    // Static variables to hold our single instances throughout the app life cycle
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    // 1. Your exact MongoDB Atlas Cloud Connection URL (No local installation needed)
    private static final String CONNECTION_URI = "mongodb+srv://LMSUser:lms123@lmscluster.mr9nhmp.mongodb.net/?appName=LMSCluster";
    
    // 2. The name of the specific database inside your cloud cluster
    private static final String DATABASE_NAME = "lms_database"; 

    /**
     * Synchronized method to retrieve the live cloud database instance.
     * If the connection doesn't exist, it opens it. If it exists, it reuses it.
     */
    public static synchronized MongoDatabase getDatabase() {
        if (database == null) {
            try {
                // Mute the heavy background logs from MongoDB driver in your NetBeans output console
                Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
                
                System.out.println("Connecting to MongoDB Atlas Cloud Cluster...");
                
                // Establish the secure connection bridge across the internet to Atlas
                mongoClient = MongoClients.create(CONNECTION_URI);
                
                // Select the target database schema
                database = mongoClient.getDatabase(DATABASE_NAME);
                
                System.out.println("Connection Successful! Target Cloud DB: " + database.getName());
                
            } catch (Exception e) {
                System.err.println("CRITICAL: Failed to connect to MongoDB Atlas Cloud!");
                System.err.println("Error Message: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return database;
    }

    /**
     * Optional Utility: Safely close the network connection pools when shutting down the app.
     */
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            System.out.println("MongoDB Cloud connection safely disconnected.");
        }
    }
}

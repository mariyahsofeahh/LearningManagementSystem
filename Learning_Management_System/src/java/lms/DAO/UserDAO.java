/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lms.DAO;

/**
 *
 * @author DELL
 */
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import lms.db.MongoConnection; // Points to your cloud credentials helper
import org.bson.Document;

public class UserDAO {

    private final MongoCollection<Document> collection;

    public UserDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("users");
    }

    // Check if an email already exists in MongoDB Cloud
    public boolean emailExists(String email) {
        Document user = collection.find(eq("email", email)).first();
        return user != null;
    }

    // SIGN UP (Insert new User Document)
    public boolean registerUser(String name, String email, String password, String role) {
        try {
            Document newUser = new Document()
                    .append("full_name", name)
                    .append("email", email)
                    .append("password", password) // In production, hash this password!
                    .append("role", role);

            collection.insertOne(newUser);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // LOGIN (Find matching record)
    public Document authenticateUser(String email, String password) {
        // Query filter looks for match matching both inputs
        return collection.find(and(eq("email", email), eq("password", password))).first();
    }
}
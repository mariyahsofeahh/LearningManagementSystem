package lms.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import lms.model.Assignment;
import lms.db.MongoConnection; // Points to your cloud credentials file
import org.bson.Document;

public class AssignmentDAO {

    private final MongoCollection<Document> collection;

    public AssignmentDAO() {
        // 1. Get your active MongoDB Atlas cloud connection instance
        MongoDatabase db = MongoConnection.getDatabase();
        
        // 2. Select the "assignments" collection on the cloud
        this.collection = db.getCollection("assignments");
    }

    // CREATE (NoSQL Document Insertion)
    public boolean createAssignment(Assignment assignment) {
        try {
            // 3. Wrap your assignment properties nicely into a native BSON Document object
            Document doc = new Document()
                    .append("course_id", assignment.getCourseId())
                    .append("lecturer_id", assignment.getLecturerId())
                    .append("title", assignment.getTitle())
                    .append("description", assignment.getDescription())
                    .append("deadline", assignment.getDeadline());

            // 4. Send the document payload straight over the network into your cloud database
            collection.insertOne(doc);
            return true; 

        } catch (Exception e) {
            System.err.println("Error saving assignment to MongoDB Atlas:");
            e.printStackTrace();  
            return false;
        }
    }

    // GET LIST (NoSQL Query Filtering)
    public List<Assignment> getAssignmentsByLecturer(String lecturerId) {
        List<Assignment> list = new ArrayList<>();

        try {
            // 5. Use MongoDB's 'eq' filter to look up matching items instead of SQL WHERE clauses
            MongoCursor<Document> cursor = collection.find(eq("lecturer_id", lecturerId)).sort(Sorts.descending("_id")).iterator();

            // 6. Loop through MongoDB cloud data packets and map them to your Java model objects
            try {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    
                    Assignment a = new Assignment();
                    a.setCourseId(doc.getString("course_id"));
                    a.setLecturerId(doc.getString("lecturer_id"));
                    a.setTitle(doc.getString("title"));
                    a.setDescription(doc.getString("description"));
                    a.setDeadline(doc.getString("deadline"));

                    list.add(a);
                }
            } finally {
                cursor.close(); // Clean up memory streams immediately
            }

        } catch (Exception e) {
            System.err.println("Error retrieving assignments from MongoDB Atlas:");
            e.printStackTrace();
        }

        return list;
    }
}
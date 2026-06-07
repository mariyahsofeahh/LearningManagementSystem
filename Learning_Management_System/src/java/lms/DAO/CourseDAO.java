package lms.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import lms.db.MongoConnection;
import lms.model.Course; // Crucial import
import org.bson.Document;

public class CourseDAO {

    // 1. FIXES LINE 24: Returns a Java List of Course objects
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        try {
            MongoDatabase db = MongoConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("courses");
            MongoCursor<Document> cursor = collection.find().iterator();
            
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Course course = new Course();
                course.setTitle(doc.getString("title"));
                course.setDescription(doc.getString("description"));
                
                // Keep code consistent with your schema definitions
                course.setCourseCode(doc.getString("course_code") != null ? doc.getString("course_code") : doc.getObjectId("_id").toString());
                list.add(course);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. FIXES LINE 30: Finds a single course by its unique ID/Code
    public Course getCourseById(String classCode) {
        try {
            MongoDatabase db = MongoConnection.getDatabase();
            Document doc = db.getCollection("courses").find(eq("course_code", classCode)).first();
            
            if (doc != null) {
                Course course = new Course();
                course.setTitle(doc.getString("title"));
                course.setDescription(doc.getString("description"));
                course.setCourseCode(doc.getString("course_code"));
                return course;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. FIXES LINE 71: Inserts a Course object into MongoDB Atlas Cloud
    public boolean createCourse(Course course) {
        try {
            MongoDatabase db = MongoConnection.getDatabase();
            MongoCollection<Document> collection = db.getCollection("courses");
            
            // Generate a simple short code for your students to use when joining
            String randomCode = Long.toHexString(Double.doubleToLongBits(Math.random())).substring(0, 6);

            Document doc = new Document()
                    .append("course_code", randomCode)
                    .append("title", course.getTitle())
                    .append("description", course.getDescription())
                    .append("lecturer_id", course.getLecturerId());

            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // This method handles the student enrollment logic
    public boolean enrollStudentByCode(String studentId, String courseCode) {
        try {
            MongoDatabase db = MongoConnection.getDatabase();
            Document targetCourse = db.getCollection("courses").find(eq("course_code", courseCode)).first();
            
            if (targetCourse == null) {
                return false; // Code doesn't exist
            }

            Document newEnrollment = new Document()
                    .append("student_id", studentId)
                    .append("course_code", courseCode);

            db.getCollection("enrollments").insertOne(newEnrollment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Add this method inside your CourseDAO class to handle updates
public boolean updateCourse(Course course) {
    try {
        com.mongodb.client.MongoDatabase db = lms.db.MongoConnection.getDatabase();
        com.mongodb.client.MongoCollection<org.bson.Document> collection = db.getCollection("courses");
        
        // 1. Locate the document matching the classroom code string and push updates
        long modifiedCount = collection.updateOne(
            com.mongodb.client.model.Filters.eq("course_code", course.getCourseCode()),
            com.mongodb.client.model.Updates.combine(
                com.mongodb.client.model.Updates.set("title", course.getTitle()),
                com.mongodb.client.model.Updates.set("description", course.getDescription())
            )
        ).getModifiedCount();
        
        // Returns true if a document was successfully updated in MongoDB Atlas
        return modifiedCount > 0;
    } catch (Exception e) {
        System.err.println("Error syncing classroom updates to MongoDB:");
        e.printStackTrace();
        return false;
    }
}
}
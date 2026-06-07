package  lms.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Sorts;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lms.model.Course;
import lms.db.MongoConnection; // Points to your central cloud connection helper
import org.bson.Document;
import org.bson.types.ObjectId;

public class CourseDAO { 

    private final MongoCollection<Document> courseCollection;
    private final MongoCollection<Document> enrollmentCollection;

    public CourseDAO() {
        // 1. Get the live cloud connection instance
        MongoDatabase db = MongoConnection.getDatabase();
        
        // 2. Map your collections (MongoDB creates them on the fly if they don't exist)
        this.courseCollection = db.getCollection("courses");
        this.enrollmentCollection = db.getCollection("enrollments");
    }

    // ==========================================
    // CREATE COURSE WITH AUTO-GENERATED CLASS CODE
    // ==========================================
    public boolean createCourse(Course course) {
        try {
            // Generate unique 6-character code (Google Classroom style)
            String uniqueClassCode = UUID.randomUUID().toString().substring(0, 6).toLowerCase();

            // 3. Package your data fields neatly into a document payload
            Document doc = new Document()
                    .append("course_code", uniqueClassCode)
                    .append("title", course.getTitle())
                    .append("description", course.getDescription())
                    .append("lecturer_id", course.getLecturerId());

            // 4. Send payload across the internet straight to the cloud cluster
            courseCollection.insertOne(doc);
            return true;
            
        } catch (Exception e) {
            System.err.println("MongoDB Error creating course:");
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // GET ALL ACTIVE COURSES
    // ==========================================
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        try {
            // 5. Query courses sorted by internal timestamp ID descending
            MongoCursor<Document> cursor = courseCollection.find()
                                                          .sort(Sorts.descending("_id"))
                                                          .iterator();

            try {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    Course c = new Course();
                    
                    // Note: If courseId is an Int in your model, you can read a custom integer, 
                    // or map your model field to use String to support standard Hex ObjectIDs from MongoDB.
                    // If your model handles an Integer fallback, you can do: c.setCourseId(doc.getInteger("course_id"));
                    
                    c.setCourseCode(doc.getString("course_code"));
                    c.setTitle(doc.getString("title"));
                    c.setDescription(doc.getString("description"));
                    c.setLecturerId(doc.getInteger("lecturer_id"));
                    list.add(c);
                }
            } finally {
                cursor.close(); // Prevent network cursor memory leaks
            }
        } catch (Exception e) {
            System.err.println("MongoDB Error fetching all courses:");
            e.printStackTrace();
        }
        return list;
    }

    // ==========================================
    // GET COURSE BY ID (Using MongoDB hex code string or field match)
    // ==========================================
    public Course getCourseById(String courseCode) {
        Course c = null;
        try {
            // 6. Use eq() filter to find the document with a matching field string
            Document doc = courseCollection.find(eq("course_code", courseCode)).first();

            if (doc != null) {
                c = new Course();
                c.setCourseCode(doc.getString("course_code"));
                c.setTitle(doc.getString("title"));
                c.setDescription(doc.getString("description"));
                c.setLecturerId(doc.getInteger("lecturer_id"));
            }
        } catch (Exception e) {
            System.err.println("MongoDB Error finding course by code:");
            e.printStackTrace();
        }
        return c;
    }

    // ==========================================
    // JOIN CLASSROOM VIA UNIQUE GOOGLE CODE
    // ==========================================
    public boolean enrollStudentByCode(String studentId, String classCode) {
        try {
            String cleanCode = classCode.trim().toLowerCase();

            // Step A: Find the course document matching the unique entered classroom code
            Document courseDoc = courseCollection.find(eq("course_code", cleanCode)).first();

            if (courseDoc != null) {
                String courseCodeStr = courseDoc.getString("course_code");

                // Step B: Use combined 'and()' filtering to verify if student is already enrolled
                Document existingEnrollment = enrollmentCollection.find(
                    and(eq("student_id", studentId), eq("course_code", courseCodeStr))
                ).first();

                if (existingEnrollment != null) {
                    return true; // Already enrolled, trigger immediate safe navigation fallback
                }

                // Step C: Insert a new transaction log payload into the enrollment cluster layer
                Document enrollDoc = new Document()
                        .append("student_id", studentId)
                        .append("course_code", courseCodeStr);

                enrollmentCollection.insertOne(enrollDoc);
                return true;
            }
            
            System.out.println("Enrollment Failed: Classroom code not found.");
            return false;
            
        } catch (Exception e) {
            System.err.println("MongoDB Error handling enrollment:");
            e.printStackTrace();
            return false;
        }
    }
}
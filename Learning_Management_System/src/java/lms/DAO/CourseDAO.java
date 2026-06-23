package lms.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.List;
import lms.db.MongoConnection;
import lms.model.Course;
import org.bson.Document;

public class CourseDAO {

    //MongoDB collection for course information
    private final MongoCollection<Document> courseCollection;

    // MongoDB collection for student enrollment records
    private final MongoCollection<Document> enrollmentCollection;

    public CourseDAO() {
        // Get MongoDB Atlas database connection
        MongoDatabase db = MongoConnection.getDatabase();

        // Access courses collection
        this.courseCollection = db.getCollection("courses");

        // Access enrollments collection
        this.enrollmentCollection = db.getCollection("enrollments");
    }

    // Retrieve all courses available in the LMS system
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        try (MongoCursor<Document> cursor = courseCollection.find().iterator()) {
            while (cursor.hasNext()) {
                list.add(mapDocumentToCourse(cursor.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Retrieve courses managed by a specific lecturer
    public List<Course> getCoursesByLecturer(String lecturerId) {
        List<Course> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = courseCollection.find(eq("lecturer_id", lecturerId)).iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                list.add(mapDocumentToCourse(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Course> getCoursesByStudent(String studentId) {
        List<Course> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = enrollmentCollection.find(eq("student_id", studentId)).iterator()) {
            while (cursor.hasNext()) {
                String courseCode = cursor.next().getString("course_code");
                Document courseDoc = courseCollection.find(eq("course_code", courseCode)).first();
                if (courseDoc != null) {
                    list.add(mapDocumentToCourse(courseDoc));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Fetch details of a single course using its code (e.g., CSE3433)
    public Course getCourseByCode(String courseCode) {
        try {
            Document doc = courseCollection.find(eq("course_code", courseCode)).first();
            if (doc != null) {
                return mapDocumentToCourse(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create and store a brand-new course document in MongoDB
    public boolean createCourse(Course course) {
        try {
            Document doc = new Document()
                    // 🔑 Use a clear separate field for the unique join/class code token
                    .append("class_code", course.getCourseCode().trim().toLowerCase())
                    // 📚 Keep the structural academic curriculum code separate
                    .append("course_code", course.getCourseCode().trim())
                    .append("title", course.getTitle().trim())
                    .append("description", course.getDescription().trim())
                    .append("lecturer_id", course.getLecturerId());

            courseCollection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing course details block
    public boolean updateCourse(Course course) {
        try {
            courseCollection.updateOne(
                    eq("course_code", course.getCourseCode()),
                    combine(
                            set("title", course.getTitle()),
                            set("description", course.getDescription())
                    )
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Enroll a student using a unique classroom code log pointer
    public boolean enrollStudentByCode(String studentId, String courseCode) {
        try {
            Document courseDoc = courseCollection.find(eq("course_code", courseCode)).first();
            if (courseDoc == null) {
                return false;
            }

            Document enrollment = new Document()
                    .append("student_id", studentId)
                    .append("course_code", courseCode);

            enrollmentCollection.insertOne(enrollment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🌟 FIXED: Native MongoDB Course Deletion Logic
    public boolean deleteCourseByCode(String courseCode) {
        try {
            // 1. Remove the course from the courses collection
            long deletedCourses = courseCollection.deleteOne(eq("course_code", courseCode)).getDeletedCount();

            if (deletedCourses > 0) {
                // 2. Cascading delete: Remove all student enrollment records tied to this course code
                enrollmentCollection.deleteMany(eq("course_code", courseCode));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🌟 FIXED: Native MongoDB Student Unenrollment Logic
    public boolean unenrollStudent(String studentId, String courseCode) {
        try {
            // Find and delete the record where both student_id AND course_code match
            long deletedEnrollments = enrollmentCollection.deleteOne(
                    and(
                            eq("student_id", studentId),
                            eq("course_code", courseCode)
                    )
            ).getDeletedCount();

            return deletedEnrollments > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Course mapDocumentToCourse(Document doc) {
        Course course = new Course();
        if (doc.getObjectId("_id") != null) {
            course.setCourseId(doc.getObjectId("_id").toString());
        }
        course.setCourseCode(doc.getString("course_code"));
        course.setTitle(doc.getString("title"));
        course.setDescription(doc.getString("description"));
        course.setLecturerId(doc.getString("lecturer_id"));
        return course;
    }
}

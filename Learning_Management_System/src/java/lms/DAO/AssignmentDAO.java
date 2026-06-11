package lms.DAO;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lms.db.MongoConnection;
import lms.model.Assignment;
import org.bson.Document;
import org.bson.types.ObjectId;

public class AssignmentDAO {

    private final MongoCollection<Document> collection;

    public AssignmentDAO() {
        MongoDatabase db = MongoConnection.getDatabase();
        this.collection = db.getCollection("assignments");
    }

    // CREATE ASSIGNMENT
    public boolean createAssignment(Assignment a) {
        try {
            Document doc = new Document()
                    .append("course_code", a.getCourseCode())
                    .append("title", a.getTitle())
                    .append("description", a.getDescription())
                    .append("file_name", a.getFileName())
                    .append("file_path", a.getFilePath())
                    .append("deadline", a.getDeadline())
                    .append("lecturer_id", a.getLecturerId())
                    .append("lecturerName", a.getLecturerName()).append("created_at", new Date());

            collection.insertOne(doc);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET BY COURSE
    public List<Assignment> getAssignmentsByCourse(String courseCode) {
        List<Assignment> list = new ArrayList<>();

        try {
            FindIterable<Document> docs = collection.find(eq("course_code", courseCode))
                    .sort(Sorts.descending("created_at"));

            for (Document doc : docs) {
                list.add(mapDocumentToAssignment(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Legacy helper kept for existing code that expects raw Mongo documents
    public List<Document> getByCourse(String courseCode) {
        return collection.find(eq("course_code", courseCode))
                .into(new ArrayList<>());
    }

    // Retrieve assignments created by a lecturer
    public List<Assignment> getAssignmentsByLecturer(String lecturerId) {

        List<Assignment> list = new ArrayList<>();

        try {

            // Find all assignments that belong to this lecturer
            FindIterable<Document> docs
                    = collection.find(eq("lecturer_id", lecturerId))
                            .sort(Sorts.descending("created_at"));

            for (Document doc : docs) {

                list.add(mapDocumentToAssignment(doc));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Assignment getAssignmentById(String assignmentId) {

        try {

            Document doc
                    = collection.find(
                            eq("_id",
                                    new ObjectId(assignmentId)))
                            .first();

            if (doc != null) {

                return mapDocumentToAssignment(doc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Assignment mapDocumentToAssignment(Document doc) {
        Assignment assignment = new Assignment();

        assignment.setId(doc.getObjectId("_id").toString());
        assignment.setCourseCode(doc.getString("course_code"));
        assignment.setTitle(doc.getString("title"));
        assignment.setDescription(doc.getString("description"));
        assignment.setFileName(doc.getString("file_name"));
        assignment.setFilePath(doc.getString("file_path"));
        assignment.setDeadline(doc.getString("deadline"));
        assignment.setLecturerId(doc.getString("lecturer_id"));
        assignment.setLecturerName(doc.getString("lecturerName"));
        assignment.setCreatedAt(formatDate(doc.get("created_at")));

        return assignment;
    }

    private String formatDate(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Date) {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format((Date) value);
        }
        return value.toString();
    }
}

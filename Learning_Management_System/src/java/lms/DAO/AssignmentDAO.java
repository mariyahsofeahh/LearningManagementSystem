package lms.DAO;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import lms.db.MongoConnection;
import lms.model.Assignment;
import org.bson.Document;

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
                    .append("lecturer_id", a.getLecturerId());

            collection.insertOne(doc);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET BY COURSE
    public List<Document> getByCourse(String courseCode) {
        return collection.find(eq("course_code", courseCode))
                .into(new ArrayList<>());
    }

    public List<Assignment> getAssignmentsByLecturer(String lecturerId) {
        List<Assignment> list = new ArrayList<>();

        FindIterable<Document> docs = collection.find(eq("user_id", lecturerId));

        for (Document d : docs) {
            Assignment a = new Assignment();
            a.setId(d.getObjectId("_id").toString());
            a.setTitle(d.getString("title"));
            a.setLecturerId(d.getString("user_id")); // because same field
            list.add(a);
        }

        return list;

    }
}

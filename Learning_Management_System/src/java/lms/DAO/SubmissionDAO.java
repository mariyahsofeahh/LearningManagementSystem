/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lms.DAO;

/**
 *
 * @author ASUS
 */
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lms.db.MongoConnection;
import lms.model.Submission;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.combine;

import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

public class SubmissionDAO {

    private final MongoCollection<Document> collection;

    public SubmissionDAO() {

        MongoDatabase db
                = MongoConnection.getDatabase();

        this.collection
                = db.getCollection("submissions");
    }

    public boolean submitWork(Submission submission) {

        try {
            Document existingSubmission = collection.find(
                    and(
                            eq("assignment_id", submission.getAssignmentId()),
                            eq("student_id", submission.getStudentId())
                    ))
                    .first();

            if (existingSubmission != null) {
                collection.updateOne(
                        eq("_id", existingSubmission.getObjectId("_id")),
                        combine(
                                set("student_file_url", submission.getStudentFileUrl()),
                                set("grade", "Pending"),
                                set("feedback", "")
                        )
                );

                return true;
            }

            Document doc = new Document()
                    .append(
                            "assignment_id",
                            submission.getAssignmentId())
                    .append(
                            "student_id",
                            submission.getStudentId())
                    .append(
                            "student_file_url",
                            submission.getStudentFileUrl())
                    .append(
                            "grade",
                            "Pending")
                    .append(
                            "feedback",
                            "");

            collection.insertOne(doc);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
    // Retrieve all submissions for a specific assignment

    public List<Submission> getSubmissionsByAssignment(
            String assignmentId) {

        List<Submission> list = new ArrayList<>();

        try {

            FindIterable<Document> docs
                    = collection.find(
                            eq("assignment_id",
                                    assignmentId));

            for (Document doc : docs) {

                Submission s = new Submission();

                s.setSubmissionId(
                        doc.getObjectId("_id")
                                .toString());

                s.setAssignmentId(
                        doc.getString("assignment_id"));

                s.setStudentId(
                        doc.getString("student_id"));

                s.setStudentFileUrl(
                        doc.getString(
                                "student_file_url"));

                s.setGrade(
                        doc.getString("grade"));

                s.setFeedback(
                        doc.getString("feedback"));

                list.add(s);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    public boolean gradeSubmission(
            String submissionId,
            String grade,
            String feedback) {

        try {

            collection.updateOne(
                    eq("_id",
                            new ObjectId(submissionId)),
                    combine(
                            set("grade", grade),
                            set("feedback", feedback)
                    )
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public Submission getStudentSubmission(
            String assignmentId,
            String studentId) {

        try {

            Document doc
                    = collection.find(
                            and(
                                    eq("assignment_id",
                                            assignmentId),
                                    eq("student_id",
                                            studentId)
                            ))
                            .first();

            if (doc != null) {

                Submission s
                        = new Submission();

                s.setSubmissionId(
                        doc.getObjectId("_id")
                                .toString());

                s.setAssignmentId(
                        doc.getString(
                                "assignment_id"));

                s.setStudentId(
                        doc.getString(
                                "student_id"));

                s.setStudentFileUrl(
                        doc.getString(
                                "student_file_url"));

                s.setGrade(
                        doc.getString(
                                "grade"));

                s.setFeedback(
                        doc.getString(
                                "feedback"));

                return s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

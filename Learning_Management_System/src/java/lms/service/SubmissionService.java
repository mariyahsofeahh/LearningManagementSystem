package lms.service;

import java.util.List;
import lms.DAO.SubmissionDAO;
import lms.model.Submission;

public class SubmissionService {

    private final SubmissionDAO submissionDAO = new SubmissionDAO();

    public boolean submitWork(Submission submission) {
        return submission != null
                && !isBlank(submission.getAssignmentId())
                && !isBlank(submission.getStudentId())
                && !isBlank(submission.getStudentFileUrl())
                && submissionDAO.submitWork(submission);
    }

    public List<Submission> getSubmissionsByAssignment(String assignmentId) {
        return submissionDAO.getSubmissionsByAssignment(assignmentId);
    }

    public Submission getStudentSubmission(String assignmentId, String studentId) {
        return submissionDAO.getStudentSubmission(assignmentId, studentId);
    }

    public boolean gradeSubmission(String submissionId, String grade, String feedback) {
        return !isBlank(submissionId) && submissionDAO.gradeSubmission(submissionId, grade, feedback);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

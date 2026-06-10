package lms.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import lms.DAO.AssignmentDAO;
import lms.model.Assignment;

public class AssignmentService {

    private final AssignmentDAO assignmentDAO = new AssignmentDAO();

    public boolean createAssignment(Assignment assignment) {
        if (assignment == null || isBlank(assignment.getCourseCode()) || isBlank(assignment.getTitle())) {
            return false;
        }
        return assignmentDAO.createAssignment(assignment);
    }

    public List<Assignment> getAssignmentsByCourse(String courseCode) {
        return assignmentDAO.getAssignmentsByCourse(courseCode);
    }

    public List<Assignment> getAssignmentsByLecturer(String lecturerId) {
        return assignmentDAO.getAssignmentsByLecturer(lecturerId);
    }

    public Assignment getAssignmentById(String assignmentId) {
        return assignmentDAO.getAssignmentById(assignmentId);
    }

    public List<Map<String, String>> getTaskCardsByCourse(String courseCode) {
        List<Map<String, String>> cards = new ArrayList<>();
        for (Assignment assignment : getAssignmentsByCourse(courseCode)) {
            cards.add(toTaskCard(assignment));
        }
        return cards;
    }

    public List<Map<String, String>> getUpcomingTaskCards(String courseCode) {
        List<Map<String, String>> upcoming = new ArrayList<>();
        for (Map<String, String> card : getTaskCardsByCourse(courseCode)) {
            if (isUpcoming(card.get("dueDate"))) {
                upcoming.add(card);
            }
        }
        upcoming.sort(Comparator.comparing(card -> safe(card.get("dueDate"))));
        return upcoming;
    }

    private Map<String, String> toTaskCard(Assignment assignment) {
        Map<String, String> card = new HashMap<>();
        card.put("id", assignment.getId());
        card.put("title", assignment.getTitle());
        card.put("description", assignment.getDescription());
        card.put("dueDate", assignment.getDeadline());
        card.put("createdDate", assignment.getCreatedAt());
        card.put("sortDate", !isBlank(assignment.getCreatedAt()) ? assignment.getCreatedAt() : assignment.getDeadline());
        return card;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private boolean isUpcoming(String dueDate) {
        if (isBlank(dueDate)) {
            return false;
        }
        try {
            return !LocalDate.parse(dueDate).isBefore(LocalDate.now());
        } catch (Exception e) {
            return true;
        }
    }
}

package lms.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lms.DAO.CourseDAO;
import lms.model.Course;
import lms.model.CourseWorkspace;

public class CourseService {

    private final CourseDAO courseDAO = new CourseDAO();
    private final LearningMaterialService materialService = new LearningMaterialService();
    private final AssignmentService assignmentService = new AssignmentService();

    public List<Map<String, String>> getDashboardCourseCards(String userId, String userRole) {
        List<Course> courses = "lecturer".equalsIgnoreCase(userRole)
                ? courseDAO.getCoursesByLecturer(userId)
                : courseDAO.getCoursesByStudent(userId);

        List<Map<String, String>> cards = new ArrayList<>();
        for (Course course : courses) {
            Map<String, String> card = new HashMap<>();
            card.put("id", course.getCourseCode());
            card.put("name", course.getTitle());
            card.put("code", course.getCourseCode());
            card.put("classCode", course.getCourseCode());
            cards.add(card);
        }
        return cards;
    }

    public CourseWorkspace getCourseWorkspace(String courseCode) {
        Course course = courseDAO.getCourseByCode(courseCode);
        if (course == null) {
            return null;
        }

        CourseWorkspace workspace = new CourseWorkspace();
        workspace.setCourse(course);

        List<Map<String, String>> materials = materialService.getMaterialsByCourse(courseCode);
        List<Map<String, String>> tasks = assignmentService.getTaskCardsByCourse(courseCode);
        workspace.setMaterials(materials);
        workspace.setTasks(tasks);
        workspace.setUpcomingTasks(assignmentService.getUpcomingTaskCards(courseCode));
        workspace.setStreamItems(buildStream(materials, tasks));
        return workspace;
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public List<Course> getCoursesByLecturer(String lecturerId) {
        return courseDAO.getCoursesByLecturer(lecturerId);
    }

    public Course getCourseByCode(String courseCode) {
        return courseDAO.getCourseByCode(courseCode);
    }

    public boolean createCourse(Course course) {
        if (course == null || isBlank(course.getCourseCode()) || isBlank(course.getTitle())) {
            return false;
        }
        return courseDAO.createCourse(course);
    }

    public boolean updateCourse(Course course) {
        if (course == null || isBlank(course.getCourseCode()) || isBlank(course.getTitle())) {
            return false;
        }
        return courseDAO.updateCourse(course);
    }

    public boolean enrollStudentByCode(String studentId, String courseCode) {
        return !isBlank(studentId) && !isBlank(courseCode)
                && courseDAO.enrollStudentByCode(studentId, courseCode.trim());
    }

    public boolean deleteCourseByCode(String courseCode) {
        return !isBlank(courseCode) && courseDAO.deleteCourseByCode(courseCode.trim());
    }

    public boolean unenrollStudent(String studentId, String courseCode) {
        return !isBlank(studentId) && !isBlank(courseCode)
                && courseDAO.unenrollStudent(studentId, courseCode.trim());
    }

    private List<Map<String, String>> buildStream(List<Map<String, String>> materials, List<Map<String, String>> tasks) {
        List<Map<String, String>> stream = new ArrayList<>();

        for (Map<String, String> material : materials) {
            Map<String, String> item = new HashMap<>();
            item.put("type", "material");
            item.put("id", material.get("id"));
            item.put("title", material.get("fileName"));
            item.put("description", "Learning material uploaded");
            item.put("displayDate", material.get("uploadDate"));
            item.put("sortDate", material.get("sortDate"));
            item.put("fileType", material.get("fileType"));
            stream.add(item);
        }

        for (Map<String, String> task : tasks) {
            Map<String, String> item = new HashMap<>();
            item.put("type", "task");
            item.put("id", task.get("id"));
            item.put("title", task.get("title"));
            item.put("description", task.get("description"));
            item.put("displayDate", task.get("createdDate"));
            item.put("sortDate", task.get("sortDate"));
            item.put("dueDate", task.get("dueDate"));
            stream.add(item);
        }

        stream.sort(Comparator.comparing((Map<String, String> item) -> value(item.get("sortDate"))).reversed());
        return stream;
    }

    private String value(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

package lms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseWorkspace {

    private Course course;
    private List<Map<String, String>> materials = new ArrayList<>();
    private List<Map<String, String>> tasks = new ArrayList<>();
    private List<Map<String, String>> streamItems = new ArrayList<>();
    private List<Map<String, String>> upcomingTasks = new ArrayList<>();

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Map<String, String>> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Map<String, String>> materials) {
        this.materials = materials;
    }

    public List<Map<String, String>> getTasks() {
        return tasks;
    }

    public void setTasks(List<Map<String, String>> tasks) {
        this.tasks = tasks;
    }

    public List<Map<String, String>> getStreamItems() {
        return streamItems;
    }

    public void setStreamItems(List<Map<String, String>> streamItems) {
        this.streamItems = streamItems;
    }

    public List<Map<String, String>> getUpcomingTasks() {
        return upcomingTasks;
    }

    public void setUpcomingTasks(List<Map<String, String>> upcomingTasks) {
        this.upcomingTasks = upcomingTasks;
    }
}

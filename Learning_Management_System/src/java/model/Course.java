package model;

public class Course {
    private int courseId;
    private String courseCode;
    private String title;
    private String description;
    private int lecturerId;

    public Course() {}

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }
}
package lms.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.Course;
import lms.util.DBConnection;

public class CourseDAO {

    // ==========================================
    // CREATE COURSE WITH AUTO-GENERATED CLASS CODE
    // ==========================================
    public boolean createCourse(Course course) {
        boolean success = false;
        try {
            Connection conn = DBConnection.getConnection();
            
            // Generate unique 6-character code (Google Classroom style)
            String uniqueClassCode = UUID.randomUUID().toString().substring(0, 6).toLowerCase();

            String sql = "INSERT INTO courses (course_code, title, description, lecturer_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uniqueClassCode);
            ps.setString(2, course.getTitle());
            ps.setString(3, course.getDescription());
            ps.setInt(4, course.getLecturerId());

            success = ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // ==========================================
    // GET ALL ACTIVE COURSES
    // ==========================================
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM courses ORDER BY course_id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setTitle(rs.getString("title"));
                c.setDescription(rs.getString("description"));
                c.setLecturerId(rs.getInt("lecturer_id"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==========================================
    // GET COURSE BY ID (For Workspace Stream)
    // ==========================================
    public Course getCourseById(int id) {
        Course c = null;
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM courses WHERE course_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCourseCode(rs.getString("course_code"));
                c.setTitle(rs.getString("title"));
                c.setDescription(rs.getString("description"));
                c.setLecturerId(rs.getInt("lecturer_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    // ==========================================
    // JOIN CLASSROOM VIA UNIQUE GOOGLE CODE
    // ==========================================
    public boolean enrollStudentByCode(String studentId, String classCode) {
        boolean success = false;
        try {
            Connection conn = DBConnection.getConnection();

            // Step A: Find the course matching the entered code
            String findCourseSql = "SELECT course_id FROM courses WHERE course_code = ?";
            PreparedStatement psFind = conn.prepareStatement(findCourseSql);
            psFind.setString(1, classCode.trim().toLowerCase());
            ResultSet rs = psFind.executeQuery();

            if (rs.next()) {
                int courseId = rs.getInt("course_id");

                // Step B: Check if student is already enrolled to prevent duplicates
                String checkSql = "SELECT enrollment_id FROM enrollments WHERE student_id = ? AND course_id = ?";
                PreparedStatement psCheck = conn.prepareStatement(checkSql);
                psCheck.setString(1, studentId);
                psCheck.setInt(2, courseId);
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    return true; // Already enrolled, treat as successful navigation fallback
                }

                // Step C: Insert into junction enrollment schema
                String enrollSql = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
                PreparedStatement psEnroll = conn.prepareStatement(enrollSql);
                psEnroll.setString(1, studentId);
                psEnroll.setInt(2, courseId);

                success = psEnroll.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
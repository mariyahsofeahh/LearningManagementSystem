package lms.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lms.model.Assignment;
import lms.util.DBConnection;

public class AssignmentDAO {

    private Connection conn;

    public AssignmentDAO() {
        this.conn = DBConnection.getConnection();
    }

    // CREATE
    public boolean createAssignment(Assignment assignment) {

        boolean success = false;

        try {

            String sql = "INSERT INTO assignments "
                    + "(course_id, lecturer_id, title, description, deadline) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, assignment.getCourseId());
            ps.setString(2, assignment.getLecturerId());
            ps.setString(3, assignment.getTitle());
            ps.setString(4, assignment.getDescription());
            ps.setString(5, assignment.getDeadline());

            int row = ps.executeUpdate();

        return row > 0;

    } catch (Exception e) {
        e.printStackTrace();  // IMPORTANT: SHOW REAL ERROR
        return false;
    }
}

    // GET LIST
    public List<Assignment> getAssignmentsByLecturer(int lecturerId) {

        List<Assignment> list = new ArrayList<>();

        try {

            String sql = "SELECT * FROM assignments WHERE lecturer_id = ? ORDER BY assignment_id DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lecturerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Assignment a = new Assignment();
                a.setAssignmentId(rs.getInt("assignment_id"));
                a.setCourseId(rs.getString("course_id"));
                a.setLecturerId(rs.getString("lecturer_id"));
                a.setTitle(rs.getString("title"));
                a.setDescription(rs.getString("description"));
                a.setDeadline(rs.getString("deadline"));

                list.add(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
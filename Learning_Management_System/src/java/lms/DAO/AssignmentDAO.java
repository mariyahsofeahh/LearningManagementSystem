/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lms.DAO;

/**
 *
 * @author ASUS
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import model.Assignment;
import lms.util.DBConnection;
import java.sql.ResultSet;

public class AssignmentDAO {

    public boolean createAssignment(Assignment assignment) {

        boolean success = false;

        try {

            Connection conn = DBConnection.getConnection();

            String sql
                    = "INSERT INTO assignments "
                    + "(course_id, lecturer_id, title, description, deadline) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps
                    = conn.prepareStatement(sql);

            ps.setInt(1, assignment.getCourseId());
            ps.setInt(2, assignment.getLecturerId());
            ps.setString(3, assignment.getTitle());
            ps.setString(4, assignment.getDescription());
            ps.setString(5, assignment.getDeadline());

            success = ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return success;
    }

    // ===========================
    // GET ALL ASSIGNMENTS
    // ===========================
    public List<Assignment> getAllAssignments() {

        List<Assignment> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM assignment ORDER BY assignment_id DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Loop all results
            while (rs.next()) {

                Assignment a = new Assignment();

                a.setAssignmentId(rs.getInt("assignment_id"));
                a.setCourseId(rs.getInt("course_id"));
                a.setLecturerId(rs.getInt("lecturer_id"));
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

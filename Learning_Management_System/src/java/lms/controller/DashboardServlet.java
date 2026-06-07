package lms.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet
public class DashboardServlet extends HttpServlet {

    // Database Connection Parameters
    private final String dbUrl = "jdbc:mysql://localhost:3306/lms_database?useSSL=false&serverTimezone=UTC";
    private final String dbUser = "root";
    private final String dbPassword = ""; // Set your matching environment password if needed

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Strict Session Verification Check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        String courseIdParam = request.getParameter("courseId");

        try {
            // Load the MySQL Database Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // =========================================================================
            // SCENARIO A: Dynamic Interior Course Content Processing (Materials & Tasks Tabs)
            // =========================================================================
            if (courseIdParam != null) {
                int courseId = Integer.parseInt(courseIdParam);
                
                // Load core datasets matching this individual specific course ID criteria
                loadCourseWorkspace(request, courseId);
                
                // Dispatch straight forward down into your interior shared workspace file view
                request.getRequestDispatcher("course-workspace.jsp").forward(request, response);
                return;
            }

            // =========================================================================
            // SCENARIO B: Loading Central Dashboard Course List Matrix Maps
            // =========================================================================
            List<Map<String, String>> enrolledCourses = new ArrayList<>();
            String courseSelectionQuery = "SELECT c.id, c.course_name, c.course_code FROM courses c " +
                                          "JOIN enrollments e ON c.id = e.course_id WHERE e.user_id = ?";
            
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement(courseSelectionQuery)) {
                
                stmt.setInt(1, userId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> courseMap = new HashMap<>();
                        courseMap.put("id", String.valueOf(rs.getInt("id")));
                        courseMap.put("name", rs.getString("course_name"));
                        courseMap.put("code", rs.getString("course_code"));
                        enrolledCourses.add(courseMap);
                    }
                }
            }
            
            // Pass the extracted dynamic data maps context down to the presentation layer
            request.setAttribute("courses", enrolledCourses);
            
            // =========================================================================
            // ROLE-BASED DYNAMIC VIEW DISPATCHING ENGINE
            // =========================================================================
            if ("lecturer".equalsIgnoreCase(userRole)) {
                request.getRequestDispatcher("dashboard-lecturer.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("dashboard-student.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Dashboard operation routing failure.");
        }
    }

    /**
     * Helper routine method that pulls specific sub-module information arrays matching single courses.
     */
    private void loadCourseWorkspace(HttpServletRequest request, int courseId) throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            
            // Item 1: Fetch Course Header Meta-Data Label Descriptors
            String queryCourseMeta = "SELECT course_name, course_code FROM courses WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryCourseMeta)) {
                stmt.setInt(1, courseId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("selectedCourseName", rs.getString("course_name"));
                        request.setAttribute("selectedCourseCode", rs.getString("course_code"));
                        request.setAttribute("selectedCourseId", courseId);
                    }
                }
            }

            // Item 2: Pull Learning Materials mapped precisely to this Course Node
            List<Map<String, String>> materials = new ArrayList<>();
            String queryMaterials = "SELECT id, file_name, file_type, upload_date FROM learning_materials WHERE course_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryMaterials)) {
                stmt.setInt(1, courseId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> matItem = new HashMap<>();
                        matItem.put("id", String.valueOf(rs.getInt("id")));
                        matItem.put("fileName", rs.getString("file_name"));
                        matItem.put("fileType", rs.getString("file_type"));
                        matItem.put("uploadDate", String.valueOf(rs.getTimestamp("upload_date")));
                        materials.add(matItem);
                    }
                }
            }
            request.setAttribute("materials", materials);

            // Item 3: Pull Task (Assignment) Records mapped precisely to this Course Node
            List<Map<String, String>> tasks = new ArrayList<>();
            String queryTasks = "SELECT id, task_title, due_date FROM tasks WHERE course_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryTasks)) {
                stmt.setInt(1, courseId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> taskItem = new HashMap<>();
                        taskItem.put("id", String.valueOf(rs.getInt("id")));
                        taskItem.put("title", rs.getString("task_title"));
                        taskItem.put("dueDate", String.valueOf(rs.getTimestamp("due_date")));
                        tasks.add(taskItem);
                    }
                }
            }
            request.setAttribute("tasks", tasks);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Explicitly route accidental or target POST adjustments safely over to the query method engines
        doGet(request, response);
    }
}
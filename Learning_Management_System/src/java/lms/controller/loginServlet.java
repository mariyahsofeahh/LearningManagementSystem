package lms.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet
public class loginServlet extends HttpServlet {

    private final String dbUrl = "jdbc:mysql://localhost:3307/lmsdb?useSSL=false&serverTimezone=UTC";
    private final String dbUser = "root";
    private final String dbPassword = ""; // Keep blank to align with default local phpMyAdmin paths

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            if ("signup".equals(action)) {
                handleSignUp(request, response);
            } else if ("login".equals(action)) {
                handleLogin(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Database validation execution failure.");
            if ("signup".equals(action)) {
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }

    private void handleSignUp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        String query = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, role);
            
            stmt.executeUpdate();
            response.sendRedirect("login.jsp?regSuccess=true");
            
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            request.setAttribute("error", "An account with this email address already exists.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Instantiating structural session states
                    HttpSession session = request.getSession(true);
                    session.setAttribute("userId", rs.getInt("id"));
                    session.setAttribute("userName", rs.getString("full_name"));
                    session.setAttribute("userRole", rs.getString("role"));

                    String assignedRole = rs.getString("role");
                    response.sendRedirect("DashboardServlet");
                } else {
                    request.setAttribute("error", "The password or email parameters were incorrect.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }
        }
    }
}
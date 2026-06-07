package lms.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lms.DAO.UserDAO; // Importing your clean MongoDB operations wrapper
import org.bson.Document;

//@WebServlet(urlPatterns = { "/loginServlet" }) // Make sure the path pattern string matches your form action
public class loginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("signup".equals(action)) {
                handleSignUp(request, response);
            } else if ("login".equals(action)) {
                handleLogin(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected runtime compilation validation error occurred.");
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

        // Use custom validation rule method to mimic SQL Unique constraints checks
        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "An account with this email address already exists.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        boolean success = userDAO.registerUser(name, email, password, role);

        if (success) {
            response.sendRedirect("login.jsp?regSuccess=true");
        } else {
            request.setAttribute("error", "Failed to compile your credentials profile. Try again.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Query MongoDB via our isolated data tier class
        Document userDoc = userDAO.authenticateUser(email, password);

        if (userDoc != null) {
            // Instantiate your HTTP Session State Tracking mechanism
            HttpSession session = request.getSession(true);
            
            // CONVERTED: MongoDB unique Hex Object IDs read cleanly as Strings
            String stringId = userDoc.getObjectId("_id").toString(); 
            
            session.setAttribute("userId", stringId);
            session.setAttribute("userName", userDoc.getString("full_name"));
            session.setAttribute("userRole", userDoc.getString("role"));

            // Safely transfer flow over to your primary Dashboard landing handler view
            response.sendRedirect("DashboardServlet");
        } else {
            request.setAttribute("error", "The password or email parameters were incorrect.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
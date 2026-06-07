package lms.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lms.model.Assignment;
import lms.DAO.AssignmentDAO; // Pointing to your new MongoDB implementation file

//@WebServlet(urlPatterns = {
//    "/assignment/*"
//})
public class AssignmentServlet extends HttpServlet {

    // Instantiating our newly written MongoDB bridge layer
    private final AssignmentDAO repository = new AssignmentDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Servlet AssignmentServlet</title></head>");
            out.println("<body><h1>Servlet AssignmentServlet at " + request.getContextPath() + "</h1></body>");
            out.println("</html>");
        }
    }

    // ===========================
    // HANDLE GET REQUEST
    // ===========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.isEmpty()) {
            // Forward user to create assignment page
            request.getRequestDispatcher("/WEB-INF/lecturer/createAssignment.jsp")
                    .forward(request, response);

        } else if (path.equals("/list")) {

            HttpSession session = request.getSession();

            // Replaces numerical int ID mapping with String mapping for cloud system standard
            String lecturerId = "lecturer_test_01";

            // Un-comment this line when you activate real login logic:
            // String lecturerId = (String) session.getAttribute("userId");
            // Querying your live MongoDB Atlas cloud instance
            List<Assignment> assignmentList = repository.getAssignmentsByLecturer(lecturerId);
            request.setAttribute("assignments", assignmentList);

            // Forward to presentation list view page
            request.getRequestDispatcher("/WEB-INF/lecturer/assignmentList.jsp")
                    .forward(request, response);
        }
    }

    // ===========================
    // HANDLE POST REQUEST
    // ===========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        switch (path) {
            case "/create":
                createAssignment(request, response);
                break;
            case "/update":
                updateAssignment(request, response);
                break;
            case "/delete":
                deleteAssignment(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    // ==========================================
    // CREATE ASSIGNMENT METHOD (MongoDB Route)
    // ==========================================
    private void createAssignment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            // FIXED: Fetching the actual intended parameters from your form inputs
            String courseId = request.getParameter("courseId");
            String lecturerId = "lecturer_test_01"; // Fallback placeholder

            // Un-comment this line when session parameters are live:
            // String lecturerId = (String) request.getSession().getAttribute("userId");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String deadline = request.getParameter("deadline");

            // Populate your clean data transfer object model
            Assignment assignment = new Assignment();
            assignment.setCourseId(courseId);
            assignment.setLecturerId(lecturerId);
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);

            // Execute the create function over into your database access tier
            boolean success = repository.createAssignment(assignment);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/assignment/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/assignment?error=true");
            }

        } catch (Exception e) {
            System.err.println("Servlet exception caught during assignment upload pipeline:");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/assignment?error=true");
        }
    }

    private void updateAssignment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/assignment/list");
    }

    private void deleteAssignment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath() + "/assignment/list");
    }

    @Override
    public String getServletInfo() {
        return "Assignment Handler Controller Component";
    }
}

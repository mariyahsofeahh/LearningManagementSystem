/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package lms.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lms.DAO.AssignmentDAO;
import javax.servlet.http.*;
import model.Assignment;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author ASUS
 */
/**
 *
 * @author ASUS
 */
@WebServlet(urlPatterns = {
    "/assignment/*"
})
public class AssignmentServlet extends HttpServlet {
// Create DAO object (used to communicate with database)

    private AssignmentDAO dao = new AssignmentDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AssignmentServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AssignmentServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get extra path info after /assignment
        // Example:
        // /assignment          → open form
        // /assignment/list     → show assignment list
        String path = request.getPathInfo();

        // If no path or just "/", show create form
        if (path == null || path.equals("/") || path.isEmpty()) {
            // Forward user to create assignment page (JSP inside WEB-INF)
            request.getRequestDispatcher("/WEB-INF/lecturer/createAssignment.jsp")
                    .forward(request, response);

        } // If user requests list page
        else if (path.equals("/list")) {

            // 1. Get all assignments from database
            HttpSession session = request.getSession();

            int lecturerId
                    = (Integer) session.getAttribute("userId");

            request.setAttribute(
                    "assignments",
                    dao.getAssignmentsByLecturer(lecturerId));

            // 2. Forward to list JSP page
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

        // Get action from URL
        // Example: /create, /update, /delete
        String path = request.getPathInfo();

        // If path is empty → invalid request
        if (path == null) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        // Decide which action to perform
        switch (path) {

            // CREATE assignment
            case "/create":
                createAssignment(request, response);
                break;

            // UPDATE assignment (future feature)
            case "/update":
                updateAssignment(request, response);
                break;

            // DELETE assignment (future feature)
            case "/delete":
                deleteAssignment(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

//    // ===========================
//    // CREATE ASSIGNMENT METHOD
//    // ===========================
//    private void createAssignment(HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//
//        // 1. Get form data from JSP
//        int courseId = Integer.parseInt(request.getParameter("courseId"));
////        HttpSession session = request.getSession();
////
////        int lecturerId = (Integer) session.getAttribute("userId");
//
//int lecturerId = 1; // TEMPORARY for testing
//        String title = request.getParameter("title");
//        String description = request.getParameter("description");
//        String deadline = request.getParameter("deadline");
//
//        // 2. Create Assignment object (model)
//        Assignment assignment = new Assignment();
//        assignment.setCourseId(courseId);
//        assignment.setLecturerId(lecturerId);
//        assignment.setTitle(title);
//        assignment.setDescription(description);
//        assignment.setDeadline(deadline);
//
//        // 3. Send object to DAO (database layer)
//        boolean success = dao.createAssignment(assignment);
//
//        // 4. Redirect based on result
//        if (success) {
//
//            // If success → go to assignment list page
//            response.sendRedirect(request.getContextPath() + "/assignment/list");
//
//        } else {
//
//            // If failed → go back to form with error
//            response.sendRedirect(request.getContextPath() + "/assignment?error=true");
//        }
//    }
    //untuk sekejap je
    private void createAssignment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {

//        int courseId = Integer.parseInt(request.getParameter("courseId"));
//        int lecturerId = Integer.parseInt(request.getParameter("lecturerId"));
            String courseId = request.getParameter("title");
            String lecturerId = request.getParameter("description");

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String deadline = request.getParameter("deadline");

            Assignment assignment = new Assignment();
            assignment.setCourseId(courseId);
            assignment.setLecturerId(lecturerId);
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);

            boolean success = dao.createAssignment(assignment);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/assignment/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/assignment?error=true");
            }

        } catch (Exception e) {
            e.printStackTrace(); // VERY IMPORTANT
            response.sendRedirect(request.getContextPath() + "/assignment?error=true");
        }
    }

    // ===========================
    // UPDATE 
    // ===========================
    private void updateAssignment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // TODO: add update logic later
        response.sendRedirect(request.getContextPath() + "/assignment/list");
    }

    // ===========================
    // DELETE 
    // ===========================
    private void deleteAssignment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // TODO: add delete logic later
        response.sendRedirect(request.getContextPath() + "/assignment/list");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

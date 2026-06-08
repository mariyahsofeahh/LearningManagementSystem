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
import lms.model.Course;
//@WebServlet(urlPatterns = {
//    "/assignment/*"
//})

@WebServlet("/assignment/*")
public class AssignmentServlet extends HttpServlet {

    private final AssignmentDAO repository = new AssignmentDAO();

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.isEmpty()) {
            request.getRequestDispatcher("/WEB-INF/lecturer/createAssignment.jsp")
                    .forward(request, response);

        } else if (path.equals("/list")) {

            HttpSession session = request.getSession();
            String lecturerId = (String) session.getAttribute("userId");

            List<Assignment> assignmentList = repository.getAssignmentsByLecturer(lecturerId);
            request.setAttribute("assignments", assignmentList);

            request.getRequestDispatcher("/WEB-INF/lecturer/assignmentList.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null) {
            response.sendRedirect(request.getContextPath() + "/assignment/");
            return;
        }

        switch (path) {
            case "/create":
                createAssignment(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/assignment/");
        }
    }

    private void createAssignment(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            HttpSession session = request.getSession();
            String lecturerId = (String) session.getAttribute("userId");

            if (lecturerId == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
            String courseCode = request.getParameter("courseCode");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String deadline = request.getParameter("deadline");

            if (courseCode == null || courseCode.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/assignment?error=invalidCourse");
                return;
            }

            Assignment assignment = new Assignment();
            assignment.setCourseCode(courseCode);
            assignment.setLecturerId(lecturerId);
            assignment.setTitle(title);
            assignment.setDescription(description);
            assignment.setDeadline(deadline);

            boolean success = repository.createAssignment(assignment);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/assignment/list");
            } else {
                response.sendRedirect(request.getContextPath() + "/assignment?error=true");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/assignment?error=true");
        }
    }
}

package lms.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lms.DAO.CourseDAO; // Points to your newly updated MongoDB Data Tier
import lms.model.Course;

//@WebServlet(urlPatterns = { "/course/*" })
public class CourseServlet extends HttpServlet {

    // Initialize your MongoDB data wrapper bridge component
    private final CourseDAO repository = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.equals("/catalog")) {
            // Pull all dynamic cloud entries straight into your frontend dashboard view
            request.setAttribute("courses", repository.getAllCourses());
            request.getRequestDispatcher("/courseCatalog.jsp").forward(request, response);
        } 
        else if (path.equals("/view")) {
            // CONVERTED: Expecting a unique String code matching MongoDB Atlas documents
            String classCode = request.getParameter("id");
            
            Course course = repository.getCourseById(classCode);
            if (course != null) {
                request.setAttribute("course", course);
                request.getRequestDispatcher("/courseDetails.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/course/catalog?error=notfound");
            }
        } 
        else if (path.equals("/create")) {
            request.getRequestDispatcher("/createCourse.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        if (path == null) {
            response.sendRedirect(request.getContextPath() + "/course/catalog");
            return;
        }

        switch (path) {
            case "/create":
                registerNewCourse(request, response);
                break;
            case "/enroll":
                joinCourseViaCode(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/course/catalog");
        }
    }

    private void registerNewCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Course course = new Course();
        course.setTitle(request.getParameter("title"));
        course.setDescription(request.getParameter("description"));
        course.setLecturerId(75776); // Kept fallback matching your structural architecture expectation

        if (repository.createCourse(course)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=created");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/create?error=true");
        }
    }

    private void joinCourseViaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentId = request.getParameter("studentId"); 
        String code = request.getParameter("classCode");

        // Executes cross-collection join validation in MongoDB Atlas instantly
        if (repository.enrollStudentByCode(studentId, code)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=enrolled");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/catalog?error=invalidcode");
        }
    }
}
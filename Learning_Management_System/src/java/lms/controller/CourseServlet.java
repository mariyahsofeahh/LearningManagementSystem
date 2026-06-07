package lms.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lms.DAO.CourseDAO; 
import lms.model.Course;


public class CourseServlet extends HttpServlet {

    private final CourseDAO repository = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.equals("/catalog")) {
            request.setAttribute("courses", repository.getAllCourses());
            request.getRequestDispatcher("/courseCatalog.jsp").forward(request, response);
        } 
        else if (path.equals("/view")) {
    String classCode = request.getParameter("id");
    
    // 1. Fetch your course data object model structure
    Course course = repository.getCourseById(classCode);
    
    if (course != null) {
        request.setAttribute("course", course);
        
        // 2. CRITICAL: Invoke the workspace content loader before rendering the page view
        // If your loadCourseWorkspace method is only inside DashboardServlet, we can route directly to it:
        request.getRequestDispatcher("/DashboardServlet?courseId=" + classCode).forward(request, response);
    } else {
        response.sendRedirect(request.getContextPath() + "/course/catalog?error=notfound");
    }
}
        else if (path.equals("/create")) {
            request.getRequestDispatcher("/lecturer/createCourse.jsp").forward(request, response);
        }
        
        else if (path.equals("/edit")) {
            String classCode = request.getParameter("id");
            Course course = repository.getCourseById(classCode);
    
            if (course != null) {
                request.setAttribute("course", course);
                // Make sure you create this editCourse.jsp inside your web/lecturer directory!
                request.getRequestDispatcher("/lecturer/editCourse.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/course/catalog?success=updated");
            }
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
    // ADD THIS NEW CASE HERE:
    case "/update":
        updateCourseDetails(request, response);
        break;
    default:
        response.sendRedirect(request.getContextPath() + "/course/catalog");
}
    }

    private void registerNewCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Course course = new Course();
        course.setTitle(request.getParameter("title"));
        course.setDescription(request.getParameter("description"));
        course.setLecturerId(75776); 

        if (repository.createCourse(course)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=created");
        } else {
            // FIXED: Added ContextPath to prevent breaking relative directory chains on error redirects
            response.sendRedirect(request.getContextPath() + "/course/create?error=true");
        }
    }

    private void joinCourseViaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentId = request.getParameter("studentId"); 
        String code = request.getParameter("classCode");

        if (repository.enrollStudentByCode(studentId, code)) {
            // Success redirection target
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=enrolled");
        } else {
            // Error redirection target
            response.sendRedirect(request.getContextPath() + "/course/catalog?error=invalidcode");
        }
    }
    
    private void updateCourseDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Course course = new Course();
    course.setCourseCode(request.getParameter("classCode"));
    course.setTitle(request.getParameter("title"));
    course.setDescription(request.getParameter("description"));

    // Make sure your CourseDAO has an updateCourse method implemented!
    if (repository.updateCourse(course)) {
        response.sendRedirect(request.getContextPath() + "/course/catalog?success=updated");
    } else {
        response.sendRedirect(request.getContextPath() + "/course/catalog?error=updatefailed");
    }
}
    
}
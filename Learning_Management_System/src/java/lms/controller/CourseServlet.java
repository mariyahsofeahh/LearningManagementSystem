package lms.controller;

import java.io.IOException;
import java.util.List;        
import java.util.ArrayList;

// Restored strictly back to javax packages
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lms.DAO.CourseDAO;
import lms.model.Course;


public class CourseServlet extends HttpServlet {

    private final CourseDAO repository = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.equals("/catalog")) {
            HttpSession session = request.getSession();
            String userRole = (String) session.getAttribute("userRole");
    
            if ("lecturer".equalsIgnoreCase(userRole)) {
                String lecturerId = (String) session.getAttribute("userId");
                if (lecturerId == null || lecturerId.trim().isEmpty()) {
                    lecturerId = "75776"; 
                }
                request.setAttribute("courses", repository.getCoursesByLecturer(lecturerId));
            } else {
                request.setAttribute("courses", repository.getAllCourses());
            }
            request.getRequestDispatcher("/courseCatalog.jsp").forward(request, response);
            
        } else if (path.equals("/create")) {
            request.getRequestDispatcher("/lecturer/createCourse.jsp").forward(request, response);
            
        } else if (path.equals("/edit")) {
            String courseCode = request.getParameter("id");
            
            if (courseCode != null && !courseCode.trim().isEmpty()) {
                Course existingCourse = repository.getCourseByCode(courseCode);
                if (existingCourse != null) {
                    request.setAttribute("course", existingCourse);
                }
            }
            request.getRequestDispatcher("/lecturer/editCourse.jsp").forward(request, response);
            
        } else {
            response.sendRedirect(request.getContextPath() + "/course/catalog");
        }
    }
    
    private void registerNewCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Course course = new Course();
    
        course.setCourseCode(request.getParameter("courseCode"));
        course.setTitle(request.getParameter("title"));
        course.setDescription(request.getParameter("description"));
    
        String sessionUid = (String) request.getSession().getAttribute("userId");
        if (sessionUid == null || sessionUid.trim().isEmpty()) {
            sessionUid = "75776"; 
        }
        course.setLecturerId(sessionUid);
    
        if (repository.createCourse(course)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=created");
        } else {
            response.sendRedirect(request.getContextPath() + "/lecturer/createCourse.jsp?error=true");
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
            case "/update":
                updateCourseDetails(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/course/catalog");
        }
    }

    private void joinCourseViaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentId = request.getParameter("studentId");
        String code = request.getParameter("classCode");

        HttpSession session = request.getSession();
        session.setAttribute("userRole", "student");

        if (repository.enrollStudentByCode(studentId, code)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=enrolled");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/catalog?error=invalidcode");
        }
    }

    private void updateCourseDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Course course = new Course();
        course.setCourseCode(request.getParameter("classCode"));
        course.setTitle(request.getParameter("title"));
        course.setDescription(request.getParameter("description"));

        if (repository.updateCourse(course)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/catalog?error=updatefailed");
        }
    }
}
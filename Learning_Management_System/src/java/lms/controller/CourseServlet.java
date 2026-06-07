package lms.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lms.DAO.CourseDAO;
import lms.model.Course;

@WebServlet(urlPatterns = { "/course/*" })
public class CourseServlet extends HttpServlet {

    private CourseDAO dao = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.equals("/catalog")) {
            request.setAttribute("courses", dao.getAllCourses());
            request.getRequestDispatcher("/courseCatalog.jsp").forward(request, response);
        } 
        else if (path.equals("/view")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Course course = dao.getCourseById(id);
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
        course.setLecturerId(75776); // Lecturer context fallback identifier assignment

        if (dao.createCourse(course)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=created");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/create?error=true");
        }
    }

    private void joinCourseViaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentId = request.getParameter("studentId"); // E.g., "S76237"
        String code = request.getParameter("classCode");

        if (dao.enrollStudentByCode(studentId, code)) {
            response.sendRedirect(request.getContextPath() + "/course/catalog?success=enrolled");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/catalog?error=invalidcode");
        }
    }
}
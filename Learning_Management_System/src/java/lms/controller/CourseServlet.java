package lms.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lms.model.Course;
import lms.service.CourseService;

public class CourseServlet extends HttpServlet {

    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/") || path.equals("/catalog")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            String userRole = (String) session.getAttribute("userRole");
            if ("lecturer".equalsIgnoreCase(userRole)) {
                request.setAttribute("courses", courseService.getCoursesByLecturer((String) session.getAttribute("userId")));
            } else {
                request.setAttribute("courses", courseService.getAllCourses());
            }
            request.getRequestDispatcher("/courseCatalog.jsp").forward(request, response);
            return;
        }

        if (path.equals("/edit")) {
            String courseCode = request.getParameter("id");
            if (courseCode != null && !courseCode.trim().isEmpty()) {
                Course existingCourse = courseService.getCourseByCode(courseCode.trim());
                if (existingCourse != null) {
                    request.setAttribute("course", existingCourse);
                }
            }
            request.getRequestDispatcher("/lecturer/editCourse.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/DashboardServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        if (path == null) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
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
            case "/delete":
                removeCourseCompletely(request, response);
                break;
            case "/unenroll":
                studentDropCourse(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/DashboardServlet");
        }
    }

    private void registerNewCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"lecturer".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
            return;
        }

        String courseCode = request.getParameter("courseCode");
        String courseTitle = request.getParameter("courseName");
        if (isBlank(courseCode) || isBlank(courseTitle)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=failed");
            return;
        }

        if (courseService.getCourseByCode(courseCode.trim()) != null) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=duplicatecode");
            return;
        }

        Course newCourse = new Course();
        newCourse.setCourseCode(courseCode.trim());
        newCourse.setTitle(courseTitle.trim());
        newCourse.setLecturerId((String) session.getAttribute("userId"));

        String courseDesc = request.getParameter("courseDescription");
        newCourse.setDescription(!isBlank(courseDesc) ? courseDesc.trim() : "No description provided yet.");

        if (courseService.createCourse(newCourse)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=created");
        } else {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=failed");
        }
    }

    private void joinCourseViaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String studentId = session != null ? (String) session.getAttribute("userId") : null;
        String code = request.getParameter("classCode");

        if (courseService.enrollStudentByCode(studentId, code)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=enrolled");
        } else {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=invalidcode");
        }
    }

    private void updateCourseDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Course course = new Course();
        course.setCourseCode(request.getParameter("classCode"));
        course.setTitle(request.getParameter("title"));
        course.setDescription(request.getParameter("description"));

        if (courseService.updateCourse(course)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/edit?id=" + course.getCourseCode() + "&error=updatefailed");
        }
    }

    private void removeCourseCompletely(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"lecturer".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
            return;
        }

        String courseCode = request.getParameter("courseCode");
        if (courseService.deleteCourseByCode(courseCode)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=failed");
        }
    }

    private void studentDropCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String studentId = session != null ? (String) session.getAttribute("userId") : null;
        String courseCode = request.getParameter("classCode");

        if (courseService.unenrollStudent(studentId, courseCode)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=dropped");
        } else {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=dropfailed");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

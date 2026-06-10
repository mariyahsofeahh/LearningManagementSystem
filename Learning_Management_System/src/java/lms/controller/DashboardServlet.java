package lms.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lms.model.Course;
import lms.model.CourseWorkspace;
import lms.service.CourseService;

public class DashboardServlet extends HttpServlet {

    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        String courseCode = request.getParameter("courseId");

        try {
            if (courseCode != null && !courseCode.trim().isEmpty()) {
                CourseWorkspace workspace = courseService.getCourseWorkspace(courseCode.trim());
                if (workspace == null) {
                    response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=coursenotfound");
                    return;
                }

                Course course = workspace.getCourse();
                request.setAttribute("course", course);
                request.setAttribute("courseId", course.getCourseCode());
                request.setAttribute("selectedCourseId", course.getCourseCode());
                request.setAttribute("selectedCourseCode", course.getCourseCode());
                request.setAttribute("selectedCourseName", course.getTitle());
                request.setAttribute("selectedCourseDesc", course.getDescription());
                request.setAttribute("selectedLecturerId", course.getLecturerId());
                request.setAttribute("materials", workspace.getMaterials());
                request.setAttribute("tasks", workspace.getTasks());
                request.setAttribute("streamItems", workspace.getStreamItems());
                request.setAttribute("upcomingTasks", workspace.getUpcomingTasks());
                request.setAttribute("activePage", "stream");
                request.getRequestDispatcher("/courseDetails.jsp").forward(request, response);
                return;
            }

            List<Map<String, String>> courses = courseService.getDashboardCourseCards(userId, userRole);
            request.setAttribute("courses", courses);

            if ("lecturer".equalsIgnoreCase(userRole)) {
                request.getRequestDispatcher("/lecturer/dashboard-lecturer.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/student/dashboard-student.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Dashboard routing failure:");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Dashboard operation failed.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

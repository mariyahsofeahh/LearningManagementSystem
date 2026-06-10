package lms.controller;

import java.io.IOException;
import java.util.List;        
import java.util.ArrayList;

// Restored strictly back to javax packages
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // 🔑 Added for processing direct container requests
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
            // 🌟 Whitelists everything else (including accidental GET /create hits) straight back home!
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
        }
    }
    
    private void registerNewCourse(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
        // 1. Extract the values using the EXACT names from your modal form inputs!
        String courseCode = request.getParameter("courseCode");
        String courseTitle = request.getParameter("courseName"); // Modal uses 'courseName'
        String lecturerId = request.getParameter("lecturerId");
    
        // Safety check: if the modal fields are empty, return to dashboard with an error flag
        if (courseCode == null || courseCode.trim().isEmpty() || 
            courseTitle == null || courseTitle.trim().isEmpty()) {
        
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=failed");
            return;
        }

        try {
            // 1. 🌟 NEW RULE: Check if the course code already exists in your database collection
            // (Assuming your CourseDAO has a method like getCourseByCode or similar to lookup records)
            lms.model.Course duplicateCheck = repository.getCourseByCode(courseCode.trim());
    
            if (duplicateCheck != null) {
                // 🛑 CODE ALREADY TAKEN: Reject insertion immediately and bounce back with a custom error indicator flag
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=duplicatecode");
                return; 
            }

            // 2. If it's unique, proceed to build your Model Object safely
            lms.model.Course newCourse = new lms.model.Course();
            newCourse.setCourseCode(courseCode.trim());
            newCourse.setTitle(courseTitle.trim());
            newCourse.setLecturerId(lecturerId);
    
            // Read the description parameter from the form layout safely
            String courseDesc = request.getParameter("courseDescription");
            newCourse.setDescription(courseDesc != null ? courseDesc.trim() : "No description provided yet.");

            // 3. Save directly into your MongoDB cluster via your repository
            boolean isSaved = repository.createCourse(newCourse);

            if (isSaved) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=created");
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=failed");
        }
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

    private void joinCourseViaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentId = request.getParameter("studentId");
        String code = request.getParameter("classCode");

        HttpSession session = request.getSession();
        session.setAttribute("userRole", "student");

        if (repository.enrollStudentByCode(studentId, code)) {
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
        
        if (repository.updateCourse(course)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/course/edit?id=" + course.getCourseCode() + "&error=updatefailed");
        }
    }
    
    private void removeCourseCompletely(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String userRole = (session != null) ? (String) session.getAttribute("userRole") : null;
    
        // Security check: Only lecturers should be allowed to delete entire courses
        if (!"lecturer".equalsIgnoreCase(userRole)) {
            // ✅ UPDATED: Redirect to DashboardServlet instead of /course/catalog
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
            return;
        }

        // 1. Capture the parameter sent by your hidden form field
        String courseCode = request.getParameter("courseCode"); 

        if (courseCode != null && !courseCode.trim().isEmpty()) {
    
            // 2. Execute the MongoDB deletion script
            if (repository.deleteCourseByCode(courseCode)) {
        
                // ✅ SUCCESS PATH: Force the browser to bounce back to the main dashboard router
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=deleted");
                return; // Terminate execution immediately to prevent accidental falling through
        
            } else {
        
                // ❌ REPOSITORY FAILURE PATH: Bounce back with an error indicator flag
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=failed");
                return;
            }
        } else {
            // ⚠️ MISSING PARAMETER FALLBACK: Safety exit route back home if the variable string was empty
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
            return;
        }
    }
    
    private void studentDropCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String studentId = (session != null) ? (String) session.getAttribute("userId") : null;
        String courseCode = request.getParameter("classCode");

        if (studentId == null || courseCode == null) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=missingdata");
            return;
        }

        if (repository.unenrollStudent(studentId, courseCode)) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=dropped");
        } else {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=dropfailed");
        }
    }
}
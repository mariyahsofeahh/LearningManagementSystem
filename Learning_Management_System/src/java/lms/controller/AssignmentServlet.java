package lms.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lms.DAO.AssignmentDAO;
import lms.DAO.SubmissionDAO;
import lms.model.Assignment;
import lms.model.Submission;

@WebServlet("/assignment/*")
public class AssignmentServlet extends HttpServlet {

    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final SubmissionDAO submissionDAO = new SubmissionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getPathInfo();
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        if (path.equals("/createPage")) {
            // Direct request path navigation straight to assignment page builder
            request.getRequestDispatcher("/assignment/createAssignment.jsp").forward(request, response);
            
        } else if (path.equals("/view")) {
            String assignmentId = request.getParameter("id");
            Assignment task = assignmentDAO.getAssignmentById(assignmentId);
            request.setAttribute("assignment", task);

            if ("lecturer".equalsIgnoreCase(userRole)) {
                // Fetch all submitted files across students for evaluation routing
                List<Submission> submissionsList = submissionDAO.getSubmissionsByAssignment(assignmentId);
                request.setAttribute("submissions", submissionsList);
                request.getRequestDispatcher("/assignment/lecturerReview.jsp").forward(request, response);
            } else {
                // Fetch specific individual response data record matching logging student signature
                Submission studentRecord = submissionDAO.getStudentSubmission(assignmentId, userId);
                request.setAttribute("submission", studentRecord);
                request.getRequestDispatcher("/assignment/studentView.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getPathInfo();
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("userId");

        if (path.equals("/create")) {
            Assignment a = new Assignment();
            String courseCode = request.getParameter("courseCode");
            a.setCourseCode(courseCode);
            a.setTitle(request.getParameter("title"));
            a.setDescription(request.getParameter("description"));
            a.setDeadline(request.getParameter("deadline"));
            a.setLecturerId(userId);

            if (assignmentDAO.createAssignment(a)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode);
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=createfailed");
            }

        } else if (path.equals("/submit")) {
            String assignmentId = request.getParameter("assignmentId");
            String courseCode = request.getParameter("courseCode");
            
            // Mock S3 CDN reference bucket link output for simulation
            String mockUrl = "https://lms-bucket.s3.amazonaws.com/submissions/" + userId + "_" + assignmentId + ".pdf";

            Submission s = new Submission();
            s.setAssignmentId(assignmentId);
            s.setStudentId(userId);
            s.setStudentFileUrl(mockUrl);

            if (submissionDAO.submitWork(s)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&success=submitted");
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=failed");
            }

        } else if (path.equals("/grade")) {
            String subId = request.getParameter("submissionId");
            String courseCode = request.getParameter("courseCode");
            String grade = request.getParameter("grade");
            String feedback = request.getParameter("feedback");

            if (submissionDAO.gradeSubmission(subId, grade, feedback)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&success=graded");
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=failed");
            }
        }
    }
}
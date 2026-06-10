package lms.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lms.model.Assignment;
import lms.model.Submission;
import lms.service.AssignmentService;
import lms.service.SubmissionService;

@WebServlet("/assignment/*")
public class AssignmentServlet extends HttpServlet {

    private final AssignmentService assignmentService = new AssignmentService();
    private final SubmissionService submissionService = new SubmissionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null) {
            path = "/createPage";
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        if (path.equals("/createPage")) {
            if (!"lecturer".equalsIgnoreCase(userRole)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
                return;
            }

            String courseCode = request.getParameter("courseCode");
            if (courseCode == null || courseCode.trim().isEmpty()) {
                courseCode = request.getParameter("courseId");
            }

            request.setAttribute("courseCode", courseCode);
            request.getRequestDispatcher("/lecturer/createAssignment.jsp").forward(request, response);

        } else if (path.equals("/list")) {
            String courseCode = request.getParameter("courseCode");
            List<Assignment> assignments;

            if (courseCode != null && !courseCode.trim().isEmpty()) {
                assignments = assignmentService.getAssignmentsByCourse(courseCode);
            } else {
                assignments = assignmentService.getAssignmentsByLecturer(userId);
            }

            request.setAttribute("assignments", assignments);
            request.setAttribute("courseCode", courseCode);
            request.getRequestDispatcher("/lecturer/assignmentLIst.jsp").forward(request, response);

        } else if (path.equals("/view")) {
            String assignmentId = request.getParameter("id");
            Assignment task = assignmentService.getAssignmentById(assignmentId);

            if (task == null) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=assignmentnotfound");
                return;
            }

            request.setAttribute("assignment", task);

            if ("lecturer".equalsIgnoreCase(userRole)) {
                // Fetch all submitted files across students for evaluation routing
                List<Submission> submissionsList = submissionService.getSubmissionsByAssignment(assignmentId);
                request.setAttribute("submissions", submissionsList);
                request.getRequestDispatcher("/lecturer/lecturerReview.jsp").forward(request, response);
            } else {
                // Fetch specific individual response data record matching logging student signature
                Submission studentRecord = submissionService.getStudentSubmission(assignmentId, userId);
                request.setAttribute("submission", studentRecord);
                request.getRequestDispatcher("/student/assignmentView.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        if (path == null) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
            return;
        }

        if (path.equals("/create")) {
            if (!"lecturer".equalsIgnoreCase(userRole)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
                return;
            }

            Assignment a = new Assignment();
            String courseCode = request.getParameter("courseCode");
            a.setCourseCode(courseCode);
            a.setTitle(request.getParameter("title"));
            a.setDescription(request.getParameter("description"));
            a.setDeadline(request.getParameter("deadline"));
            a.setLecturerId(userId);

            if (assignmentService.createAssignment(a)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode);
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=createfailed");
            }

        } else if (path.equals("/submit")) {
            String assignmentId = request.getParameter("assignmentId");
            String courseCode = request.getParameter("courseCode");

            if (assignmentId == null || assignmentId.trim().isEmpty()
                    || courseCode == null || courseCode.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=missingassignment");
                return;
            }

            String fileUrl = request.getParameter("studentFileUrl");
            if (fileUrl == null || fileUrl.trim().isEmpty()) {
                fileUrl = "https://lms-bucket.s3.amazonaws.com/submissions/" + userId + "_" + assignmentId + ".pdf";
            }

            Submission s = new Submission();
            s.setAssignmentId(assignmentId);
            s.setStudentId(userId);
            s.setStudentFileUrl(fileUrl.trim());

            if (submissionService.submitWork(s)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&success=submitted");
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=failed");
            }

        } else if (path.equals("/grade")) {
            if (!"lecturer".equalsIgnoreCase(userRole)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
                return;
            }

            String subId = request.getParameter("submissionId");
            String courseCode = request.getParameter("courseCode");
            String grade = request.getParameter("grade");
            String feedback = request.getParameter("feedback");

            if (subId == null || subId.trim().isEmpty()
                    || courseCode == null || courseCode.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=missingsubmission");
                return;
            }

            if (submissionService.gradeSubmission(subId, grade, feedback)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&success=graded");
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=failed");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet");
        }
    }
}

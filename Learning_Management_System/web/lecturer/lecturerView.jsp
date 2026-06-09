<%-- 
    Document   : lecturerView
    Created on : 9 Jun 2026, 2:28:55 pm
    Author     : ASUS
--%>

<%-- 
    Document   : lecturerReview.jsp
    Created on : 9 Jun 2026, 12:36:22 am
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="lms.model.Assignment" %>
<%@ page import="lms.model.Submission" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Faculty Review Hub - eduSphere</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light p-5">
        <%
            Assignment task = (Assignment) request.getAttribute("assignment");
            List<Submission> submissions = (List<Submission>) request.getAttribute("submissions");
        %>
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <a href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= task.getCourseCode()%>" class="text-decoration-none small">← Back to Course</a>
                    <h1 class="fw-bold mt-1"><%= task.getTitle()%> — Submissions Review</h1>
                </div>
                <span class="badge bg-dark px-3 py-2">Course Node: <%= task.getCourseCode()%></span>
            </div>

            <div class="row g-4">
                <% if (submissions != null && !submissions.isEmpty()) {
                        for (Submission sub : submissions) {%>
                <div class="col-md-6">
                    <div class="card shadow-sm border-0 rounded-4 p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="fw-bold mb-0 text-secondary">Student ID: <%= sub.getStudentId()%></h5>
                            <span class="badge <%= "Pending".equals(sub.getGrade()) ? "bg-warning text-dark" : "bg-success"%>">
                                Status: <%= sub.getGrade()%>
                            </span>
                        </div>

                        <p class="small text-muted">Uploaded File Asset: 
                            <a href="<%= sub.getStudentFileUrl()%>" target="_blank" class="fw-semibold text-primary">View Submitted PDF 📄</a>
                        </p>

                        <hr>

                        <form action="${pageContext.request.contextPath}/assignment/grade" method="POST">
                            <input type="hidden" name="submissionId" value="<%= sub.getSubmissionId()%>">
                            <input type="hidden" name="courseCode" value="<%= task.getCourseCode()%>">

                            <div class="mb-3">
                                <label class="form-label small fw-bold">Assign Grade/Score</label>
                                <input type="text" name="grade" class="form-control form-control-sm" placeholder="e.g., A+, 85/100" value="<%= "Pending".equals(sub.getGrade()) ? "" : sub.getGrade()%>" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label small fw-bold">Feedback Comments</label>
                                <textarea name="feedback" class="form-control form-control-sm" rows="2" placeholder="Type comments to student..." required><%= sub.getFeedback()%></textarea>
                            </div>

                            <button type="submit" class="btn btn-dark btn-sm w-100 py-2">Save Appraisal Parameters</button>
                        </form>
                    </div>
                </div>
                <%  }
                } else { %>
                <div class="col-12 text-center p-5 card border-0 rounded-4 shadow-sm">
                    <p class="text-muted mb-0">No active student submission streaming payloads detected in pipeline cluster.</p>
                </div>
                <% }%>
            </div>
        </div>
    </body>
</html>

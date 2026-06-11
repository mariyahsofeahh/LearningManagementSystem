<%-- 
    Document   : assignmentView
    Created on : 9 Jun 2026, 2:28:07 pm
    Author     : ASUS
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lms.model.Assignment" %>
<%@ page import="lms.model.Submission" %>
<!DOCTYPE html>
<%
    Assignment assignment = (Assignment) request.getAttribute("assignment");
    Submission submission = (Submission) request.getAttribute("submission");
%>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title><%= assignment != null ? assignment.getTitle() : "Assignment"%></title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <div class="container py-5">
            <% if (assignment == null) { %>
            <div class="alert alert-danger">Assignment not found.</div>
            <% } else {%>
            <a href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= assignment.getCourseCode()%>" class="text-decoration-none small">Back to Course</a>

            <div class="card border-0 shadow-sm rounded-4 mt-3 mb-4">
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-start gap-3">
                        <div>
                            <h1 class="h3 fw-bold mb-2"><%= assignment.getTitle()%></h1>
                            <p class="text-muted mb-2"><%= assignment.getDescription() != null ? assignment.getDescription() : ""%></p>
                            <span class="badge text-bg-primary">Due: <%= assignment.getDeadline()%></span>
                        </div>
                        <span class="badge text-bg-dark"><%= assignment.getCourseCode()%></span>
                    </div>
                </div>
            </div>

            <div class="row g-4">
                <div class="col-lg-7">
                    <div class="card border-0 shadow-sm rounded-4 h-100">
                        <div class="card-body p-4">
                            <h2 class="h5 fw-bold mb-3">Submit Assignment</h2>
                            <form action="${pageContext.request.contextPath}/assignment/submit"
                                  method="post"
                                  enctype="multipart/form-data">                                <input type="hidden" name="assignmentId" value="<%= assignment.getId()%>">
                                <input type="hidden" name="courseCode" value="<%= assignment.getCourseCode()%>">
                                <div class="mb-3">
                                    <label class="form-label">Upload PDF File</label>

                                    <input type="file"
                                           name="pdfFile"
                                           class="form-control"
                                           accept=".pdf"
                                           required>

                                    <div class="form-text">
                                        Only PDF files are allowed.
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary px-4">Submit Work</button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-lg-5">
                    <div class="card border-0 shadow-sm rounded-4 h-100">
                        <div class="card-body p-4">
                            <h2 class="h5 fw-bold mb-3">Lecturer Feedback</h2>
                            <% if (submission == null) { %>
                            <p class="text-muted mb-0">No submission yet.</p>
                            <% } else {%>
                            <p class="mb-2"><b>Status:</b> <%= submission.getGrade() != null ? submission.getGrade() : "Pending"%></p>
                            <p class="mb-2"><b>Your file:</b> <a href="<%= submission.getStudentFileUrl()%>" target="_blank">Open submission</a></p>
                            <hr>
                            <p class="mb-1"><b>Feedback</b></p>
                            <p class="text-muted mb-0"><%= submission.getFeedback() != null && !submission.getFeedback().trim().isEmpty() ? submission.getFeedback() : "Feedback has not been added yet."%></p>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            <% }%>
        </div>
    </body>
</html>

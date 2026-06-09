<%-- 
    Document   : dashboard-student
    Created on : 4 Jun 2026, 2:38:45 pm
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Hub - eduSphere</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #fcfcfd; color: #101828; }
        .glass-nav { background: rgba(255, 255, 255, 0.8); backdrop-filter: blur(12px); border-bottom: 1px solid #eaecf0; }
        .course-card { 
            border: 1px solid #eaecf0; border-radius: 20px; background: #ffffff; 
            transition: all 0.25s ease; cursor: pointer; text-decoration: none; color: inherit;
            display: flex; flex-direction: column; justify-content: space-between;
        }
        .course-card:hover { transform: translateY(-4px); border-color: #444ce7; box-shadow: 0px 12px 16px -4px rgba(68, 76, 231, 0.08); }
        
        /* Drop button styling matching style themes */
        .btn-drop-inline {
            background: none; border: none; color: #d92d20; font-size: 14px;
            font-weight: 500; padding: 6px 12px; border-radius: 8px;
            transition: background 0.2s ease; display: inline-flex; align-items: center; gap: 4px;
        }
        .btn-drop-inline:hover { background: #fef3f2; color: #b42318; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg glass-nav sticky-top">
        <div class="container">
            <a class="navbar-brand fw-bold text-dark d-flex align-items-center" href="DashboardServlet">
                <span class="bg-primary text-white p-2 rounded-3 me-2 d-inline-flex"><i class="bi bi-layers-half"></i></span>
                <span>eduSphere</span>
            </a>
            <span class="navbar-text fw-medium text-dark"><i class="bi bi-mortarboard me-1"></i> Student: <%= session.getAttribute("userName") %></span>
        </div>
    </nav>

    <div class="container my-5">
    <% if(request.getParameter("success") != null && "enrolled".equals(request.getParameter("success"))) { %>
        <div class="alert alert-success alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> Successfully joined the class!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <%-- 🌟 ADDED ALERT NOTIFICATIONS TRACKING ACTIONS HERE --%>
    <% if(request.getParameter("success") != null && "dropped".equals(request.getParameter("success"))) { %>
        <div class="alert alert-success alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-info-circle-fill me-2"></i> Successfully unenrolled from the course workspace.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <% if(request.getParameter("error") != null && "invalidcode".equals(request.getParameter("error"))) { %>
        <div class="alert alert-danger alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> Code unrecognized. Verification failure.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <div class="row align-items-center mb-5 g-4">
        <div class="col-md-6">
            <h1 class="fw-bold tracking-tight mb-1">My Enrolled Courses</h1>
            <p class="text-muted mb-0">Select a module workspace node to review learning assets and tasks.</p>
        </div>
        <div class="col-md-6">
            <form action="<%= request.getContextPath() %>/course/enroll" method="POST" class="d-flex gap-2 justify-content-md-end">
                <input type="hidden" name="studentId" value="<%= session.getAttribute("userId") %>">
                
                <div class="input-group" style="max-width: 350px;">
                    <span class="input-group-text bg-white border-end-0 text-muted rounded-start-3"><i class="bi bi-key"></i></span>
                    <input type="text" name="classCode" class="form-control border-start-0 bg-white" placeholder="Enter Class Code (e.g. ddc26c)" required>
                </div>
                <button type="submit" class="btn btn-success rounded-3 px-4 fw-medium">Join Class</button>
            </form>
        </div>
    </div>

    <div class="row g-4">
        <% 
            List<Map<String, String>> courses = (List<Map<String, String>>) request.getAttribute("courses");
            if (courses != null && !courses.isEmpty()) {
                for (Map<String, String> course : courses) {
        %>
        <div class="col-xl-4 col-md-6">
            <a href="DashboardServlet?courseId=<%= course.get("id") %>" class="card course-card h-100 p-4">
                <div>
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <span class="badge bg-primary-subtle text-primary font-monospace rounded-pill small"><%= course.get("code") %></span>
                        <i class="bi bi-arrow-right-circle fs-4 text-muted"></i>
                    </div>
                    <h4 class="fw-bold text-dark mb-2"><%= course.get("name") %></h4>
                </div>
                
                <div class="d-flex align-items-center justify-content-between mt-auto pt-3 border-top border-light-subtle">
                    <p class="text-muted small mb-0">
                        <i class="bi bi-circle-fill text-success me-1" style="font-size:0.6rem;"></i> Synchronized
                    </p>
                    
                    <form action="<%= request.getContextPath() %>/course/unenroll" method="POST" 
                          onsubmit="return confirm('Are you sure you want to leave this classroom workspace module?');" 
                          onclick="event.stopPropagation();" style="display: inline;">
                        <input type="hidden" name="classCode" value="<%= course.get("classCode") != null ? course.get("classCode") : course.get("code") %>" />
                        <button type="submit" class="btn-drop-inline">
                            <i class="bi bi-box-arrow-right"></i> Drop
                        </button>
                    </form>
                </div>
            </a>
        </div>
        <% } } else { %>
            <div class="col-12 text-center py-5">
                <div class="text-muted fs-4 mb-2"><i class="bi bi-folder-x fs-1"></i></div>
                <p class="text-muted">You are not enrolled in any classes yet. Enter a class code above to begin!</p>
            </div>
        <% } %>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
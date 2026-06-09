<%-- 
    Document   : dashboard-lecturer
    Created on : 4 Jun 2026, 2:39:19 pm
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Faculty Control Panel - eduSphere</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #fcfcfd; color: #101828; }
        .glass-nav { background: rgba(255, 255, 255, 0.8); backdrop-filter: blur(12px); border-bottom: 1px solid #eaecf0; }
        .faculty-card { 
            border: 1px solid #eaecf0; border-radius: 20px; background: #ffffff; 
            transition: all 0.25s ease; cursor: pointer; text-decoration: none; color: inherit;
        }
        .faculty-card:hover { transform: translateY(-4px); border-color: #101828; box-shadow: 0px 12px 16px -4px rgba(16, 24, 40, 0.06); }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg glass-nav sticky-top">
        <div class="container">
            <a class="navbar-brand fw-bold text-dark d-flex align-items-center" href="DashboardServlet">
                <span class="bg-dark text-white p-2 rounded-3 me-2 d-inline-flex"><i class="bi bi-terminal-box-fill"></i></span>
                <span>eduSphere <span class="text-primary fw-light">Faculty</span></span>
            </a>
            <span class="navbar-text fw-medium text-dark"><i class="bi bi-person-workspace me-1"></i> Professor: <%= session.getAttribute("userName") %></span>
        </div>
    </nav>

    <div class="container my-5">
        <%-- Alert Notifications tracking status changes --%>
        <% if(request.getParameter("success") != null && "deleted".equals(request.getParameter("success"))) { %>
        <div class="alert alert-success alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> Course structure and all associated student enrollments permanently removed!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <% if(request.getParameter("error") != null && "deletefailed".equals(request.getParameter("error"))) { %>
        <div class="alert alert-danger alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> Failed to delete the course module. Please try again.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

        <div class="d-flex justify-content-between align-items-center mb-5">
            <div>
                <h1 class="fw-bold tracking-tight mb-1">Instructional Control Console</h1>
                <p class="text-muted mb-0">Manage assigned curriculum structures and evaluate student pipeline submissions.</p>
            </div>

            <a href="${pageContext.request.contextPath}/lecturer/createCourse.jsp" class="btn btn-dark rounded-3 px-4 py-2 fw-medium">
                <i class="bi bi-plus-lg me-2"></i>Create New Course
            </a>
        </div>

        <div class="row g-4">
            <% 
                List<Map<String, String>> courses = (List<Map<String, String>>) request.getAttribute("courses");
                if (courses != null && !courses.isEmpty()) {
                    for (Map<String, String> course : courses) {
            %>
            <div class="col-xl-4 col-md-6">
                <div class="card faculty-card h-100 p-4" onclick="window.location.href='DashboardServlet?courseId=<%= course.get("code") %>'">
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <span class="badge bg-dark text-white font-monospace rounded-pill small"><%= course.get("code") %></span>
                
                        <a href="${pageContext.request.contextPath}/course/edit?id=<%= course.get("code") %>" 
                            class="text-primary small fw-semibold" 
                            onclick="event.stopPropagation();">
                            Modify Settings <i class="bi bi-pencil-square ms-1"></i>
                        </a>
                    </div>
                    <h4 class="fw-bold text-dark mb-4"><%= course.get("name") %></h4>
                    
                    <%-- 🌟 UPDATED: Lower Footer Container managing View + Delete Form actions --%>
                    <div class="d-flex align-items-center justify-content-between mt-auto pt-3 border-top border-light-subtle">
                        <p class="text-muted small mb-0">
                            <i class="bi bi-people-fill me-1"></i> View Registered Students
                        </p>
                        
                        <%-- 🗑️ NEW: Native Form to invoke your backend delete Course method --%>
                        <form action="<%= request.getContextPath() %>/course/delete" method="POST" 
                              onsubmit="return confirm('CRITICAL WARNING:\nAre you sure you want to permanently delete this course? This action cannot be undone and will drop all enrolled students.');" 
                              onclick="event.stopPropagation();" style="display: inline; margin: 0;">
                            
                            <%-- Sends the dynamic code (e.g. ddc26c) to your updated MongoDB servlet handler --%>
                            <input type="hidden" name="courseCode" value="<%= course.get("code") %>" />
                            
                            <button type="submit" class="btn p-0 text-danger border-0 bg-transparent small d-flex align-items-center gap-1" style="font-size: 0.875rem; font-weight: 500;">
                                <i class="bi bi-trash3-fill"></i> Delete
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            <% } } else { %>
                <div class="col-12 text-center py-5">
                    <div class="text-muted fs-4 mb-2"><i class="bi bi-folder-x fs-1"></i></div>
                    <p class="text-muted">No assigned curriculum structures found in your active storage profile.</p>
                </div>
            <% } %>
        </div>
    </div>

    <%-- Include Bootstrap Bundle to allow dynamic alert dismissing via 'X' buttons --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
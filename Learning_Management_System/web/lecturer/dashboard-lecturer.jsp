<%-- 
    Document   : dashboard-lecturer
    Created on : 9 Jun 2026, 10:15:00 pm
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lecturer Portal - eduSphere</title>
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
        
        /* Delete button styling matching style themes */
        .btn-delete-inline {
            background: none; border: none; color: #d92d20; font-size: 14px;
            font-weight: 500; padding: 6px 12px; border-radius: 8px;
            transition: background 0.2s ease; display: inline-flex; align-items: center; gap: 4px;
        }
        .btn-delete-inline:hover { background: #fef3f2; color: #b42318; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg glass-nav sticky-top">
        <div class="container">
            <a class="navbar-brand fw-bold text-dark d-flex align-items-center" href="DashboardServlet">
                <span class="bg-primary text-white p-2 rounded-3 me-2 d-inline-flex"><i class="bi bi-layers-half"></i></span>
                <span>eduSphere</span>
            </a>
            <div class="d-flex align-items-center gap-3">
                <span class="navbar-text fw-medium text-dark"><i class="bi bi-person-badge me-1"></i> Lecturer: <%= session.getAttribute("userName") %></span>
                <a href="<%= request.getContextPath() %>/LogoutServlet" class="btn btn-outline-danger btn-sm rounded-2"><i class="bi bi-box-arrow-right"></i> Logout</a>
            </div>
        </div>
    </nav>

    <div class="container my-5">
    <%-- ALERT NOTIFICATIONS TRACKING ACTIONS HERE --%>
    <% if(request.getParameter("success") != null && "created".equals(request.getParameter("success"))) { %>
        <div class="alert alert-success alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> New course workspace created successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <% if(request.getParameter("success") != null && "deleted".equals(request.getParameter("success"))) { %>
        <div class="alert alert-success alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-info-circle-fill me-2"></i> Course successfully archived/removed.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <% if(request.getParameter("error") != null && "failed".equals(request.getParameter("error"))) { %>
        <div class="alert alert-danger alert-dismissible fade show rounded-4 mb-4" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> Action failed. Could not process database operation.
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>
    <% if(request.getParameter("error") != null && "duplicatecode".equals(request.getParameter("error"))) { %>
    <div class="alert alert-warning alert-dismissible fade show rounded-4 mb-4" role="alert">
        <i class="bi bi-exclamation-octagon-fill me-2"></i> 
        <strong>Registration Blocked:</strong> That classroom unique code is already assigned to another active workspace! Please choose a unique identifier.
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% } %>
    <div class="row align-items-center mb-5 g-4">
        <div class="col-md-6">
            <h1 class="fw-bold tracking-tight mb-1">My Managed Classrooms</h1>
            <p class="text-muted mb-0">Manage course parameters, publish tasks, and organize training resources.</p>
        </div>
        <div class="col-md-6 text-md-end">
            <button type="button" class="btn btn-primary rounded-3 px-4 fw-medium" data-bs-toggle="modal" data-bs-target="#createCourseModal">
                <i class="bi bi-plus-lg me-2"></i>Create Course
            </button>
        </div>
    </div>

    <div class="row g-4">
        <% 
            List<Map<String, String>> courses = (List<Map<String, String>>) request.getAttribute("courses");
            if (courses != null && !courses.isEmpty()) {
                for (Map<String, String> course : courses) {
        %>
        <div class="col-xl-4 col-md-6">
            <div class="card course-card h-100 p-4 position-relative" 
                onclick="window.location.href='DashboardServlet?courseId=<%= course.get("id") %>'">
        
                <div>
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <span class="badge bg-primary-subtle text-primary font-monospace rounded-pill small"><%= course.get("code") %></span>
                
                        <a href="<%= request.getContextPath() %>/course/edit?id=<%= course.get("id") %>" 
                            class="text-primary text-decoration-none fw-medium small"
                            onclick="event.stopPropagation();">
                            Manage Studio <i class="bi bi-gear-fill ms-1"></i>
                        </a>
                    </div>
            
                    <h4 class="fw-bold text-dark mb-2"><%= course.get("name") %></h4>
                </div>
        
                <div class="d-flex align-items-center justify-content-between mt-auto pt-3 border-top border-light-subtle">
                    <p class="text-muted small mb-0">
                        <i class="bi bi-people-fill text-secondary me-1"></i> View Registered Students
                    </p>
            
                    <form action="<%= request.getContextPath() %>/course/delete" method="POST" 
                        onsubmit="return confirm('Are you sure you want to permanently delete this course workspace? This deletes all structural materials associated with it.');" 
                        onclick="event.stopPropagation();" 
                        style="display: inline; margin: 0; padding: 0;">
                
                        <input type="hidden" name="courseCode" value="<%= course.get("code") %>" />
                        <button type="submit" class="btn-delete-inline" style="background: none; border: none; color: #d92d20; font-size: 14px; font-weight: 500;">
                            <i class="bi bi-trash3"></i> Delete
                        </button>
                    </form>
                </div>
            </div>
        </div>
        <% } } else { %>
            <div class="col-12 text-center py-5">
                <div class="text-muted fs-4 mb-2"><i class="bi bi-folder-plus fs-1"></i></div>
                <p class="text-muted">No course units have been configured yet. Click "Create Course" to get started!</p>
            </div>
        <% } %>
    </div>
</div>

<div class="modal fade" id="createCourseModal" tabindex="-1" aria-labelledby="createCourseModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow-lg">
            <div class="modal-header border-bottom-0 pb-0">
                <h5 class="modal-title fw-bold" id="createCourseModalLabel">Configure New Classroom Workspace</h5>
                <button type="button" class="btn-close" data-bs-with="modal" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="<%= request.getContextPath() %>/course/create" method="POST">
                <input type="hidden" name="lecturerId" value="<%= session.getAttribute("userId") %>">
                <div class="modal-body py-4">
                    <div class="mb-3">
                        <label class="form-label fw-semibold text-secondary small">Course Subject Title</label>
                        <input type="text" name="courseName" class="form-control rounded-3" placeholder="e.g. Advanced System Architecture" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold text-secondary small">Course Description</label>
                        <textarea name="courseDescription" class="form-control rounded-3" rows="2" placeholder="Brief overview of the course workspace parameters..."></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold text-secondary small">Classroom Unique Identification Code</label>
                        <input type="text" name="courseCode" class="form-control rounded-3 font-monospace" placeholder="e.g. CS-301" required>
                    </div>
                </div>
                <div class="modal-footer border-top-0 pt-0">
                    <button type="button" class="btn btn-light rounded-3" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-3 px-4">Publish Workspace</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
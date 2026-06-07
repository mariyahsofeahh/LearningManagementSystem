<%-- 
    Document   : header-lect
    Created on : 7 Jun 2026, 4:41:06 pm
    Author     : DELL
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Check if lecturer is currently inside a managed course context
    String lecturerCourseId = request.getParameter("courseId");
    if (lecturerCourseId == null) {
        lecturerCourseId = (String) request.getAttribute("courseId");
    }
    
    // Tracking active page layout element
    String lectActivePage = (String) request.getAttribute("activePage");
    if (lectActivePage == null) lectActivePage = "";
%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">

<style>
    .glass-nav-lect {
        background: rgba(255, 255, 255, 0.85);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border-bottom: 1px solid rgba(0, 0, 0, 0.08);
    }
    .nav-link-lect {
        font-weight: 500;
        color: #4b5563 !important;
        padding: 0.5rem 1rem !important;
        border-radius: 0.5rem;
        transition: all 0.2s ease;
    }
    .nav-link-lect:hover {
        background-color: rgba(25, 135, 84, 0.08); /* Dark Green accent for Faculty */
        color: #198754 !important;
    }
    .nav-link-lect.active {
        background-color: #198754;
        color: #ffffff !important;
    }
</style>

<nav class="navbar navbar-expand-lg glass-nav-lect sticky-top py-3">
    <div class="container">
        <a class="navbar-brand fw-bold text-dark d-flex align-items-center" href="LecturerDashboardServlet">
            <span class="bg-success text-white p-2 rounded-3 me-2 d-inline-flex shadow-sm">
                <i class="bi bi-layers-half"></i>
            </span>
            <span>eduSphere <span class="badge bg-light text-success border border-success-subtle font-monospace ms-1" style="font-size: 0.65rem;">FACULTY</span></span>
        </a>
        
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#lecturerNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="lecturerNavbar">
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0 gap-1">
                <% if (lecturerCourseId != null && !lecturerCourseId.isEmpty()) { %>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect" href="LecturerDashboardServlet">
                            <i class="bi bi-arrow-left-short"></i> All Courses
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect <%= lectActivePage.equals("materials") ? "active" : "" %>" href="LearningMaterialServlet?courseId=<%= lecturerCourseId %>">
                            <i class="bi bi-folder2-open me-1"></i> Materials
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect <%= lectActivePage.equals("tasks") ? "active" : "" %>" href="TaskManagementServlet?courseId=<%= lecturerCourseId %>">
                            <i class="bi bi-journal-text me-1"></i> Tasks
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect <%= lectActivePage.equals("people") ? "active" : "" %>" href="RosterServlet?courseId=<%= lecturerCourseId %>">
                            <i class="bi bi-people me-1"></i> People
                        </a>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect active" href="LecturerDashboardServlet">
                            <i class="bi bi-house-gear-fill me-1"></i> Management Hub
                        </a>
                    </li>
                <% } %>
            </ul>

            <div class="d-flex align-items-center gap-2">
                <% if (lecturerCourseId == null || lecturerCourseId.isEmpty()) { %>
                    <button class="btn btn-success rounded-pill px-3 btn-sm shadow-sm me-2" data-bs-toggle="modal" data-bs-target="#createCourseModal">
                        <i class="bi bi-plus-circle me-1"></i> Create Course
                    </button>
                <% } %>
                
                <span class="navbar-text fw-medium text-dark bg-light px-3 py-2 rounded-pill border d-inline-flex align-items-center">
                    <i class="bi bi-person-badge-fill text-success me-2"></i>
                    Lecturer: <%= session.getAttribute("userName") != null ? session.getAttribute("userName") : "Professor" %>
                </span>
                <a href="LogoutServlet" class="btn btn-outline-danger btn-sm rounded-pill px-3">
                    <i class="bi bi-box-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
</nav>

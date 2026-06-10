<%-- 
    Document   : header-stud
    Created on : 7 Jun 2026, 4:40:11 pm
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Safely check if a student is currently inside a specific course context
    String studentCourseId = request.getParameter("courseId");
    if (studentCourseId == null) {
        studentCourseId = (String) request.getAttribute("courseId");
    }
    
    // Determine current page for active states (you can set this attribute in your Servlets)
    String activePage = (String) request.getAttribute("activePage");
    if (activePage == null) activePage = "";
%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">

<style>
    .glass-nav {
        background: rgba(255, 255, 255, 0.85);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border-bottom: 1px solid rgba(0, 0, 0, 0.08);
    }
    .nav-link-modern {
        font-weight: 500;
        color: #4b5563 !important;
        padding: 0.5rem 1rem !important;
        border-radius: 0.5rem;
        transition: all 0.2s ease;
    }
    .nav-link-modern:hover {
        background-color: rgba(13, 110, 253, 0.08);
        color: #0d6efd !important;
    }
    .nav-link-modern.active {
        background-color: #0d6efd;
        color: #ffffff !important;
    }
</style>

<nav class="navbar navbar-expand-lg glass-nav sticky-top py-3">
    <div class="container">
        <a class="navbar-brand fw-bold text-dark d-flex align-items-center" href="${pageContext.request.contextPath}/DashboardServlet">
            <span class="bg-primary text-white p-2 rounded-3 me-2 d-inline-flex shadow-sm">
                <i class="bi bi-layers-half"></i>
            </span>
            <span class="tracking-tight">eduSphere</span>
        </a>
        
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#studentNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="studentNavbar">
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0 gap-1">
                <% if (studentCourseId != null && !studentCourseId.isEmpty()) { %>
                    <li class="nav-item">
                        <a class="nav-link nav-link-modern <%= activePage.equals("dashboard") ? "" : "" %>" href="${pageContext.request.contextPath}/DashboardServlet">
                            <i class="bi bi-house-door me-1"></i> My Courses
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-modern <%= activePage.equals("materials") ? "active" : "" %>" href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= studentCourseId %>#materials">
                            <i class="bi bi-file-earmark-text me-1"></i> Materials
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-modern <%= activePage.equals("tasks") ? "active" : "" %>" href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= studentCourseId %>#tasks">
                            <i class="bi bi-check2-square me-1"></i> Tasks
                        </a>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link nav-link-modern active" href="${pageContext.request.contextPath}/DashboardServlet">
                            <i class="bi bi-grid-1x2-fill me-1"></i> Academic Dashboard
                        </a>
                    </li>
                <% } %>
            </ul>

            <div class="d-flex align-items-center gap-3">
                <span class="navbar-text fw-medium text-dark bg-light px-3 py-2 rounded-pill border d-inline-flex align-items-center">
                    <i class="bi bi-mortarboard-fill text-primary me-2"></i>
                    Student: <%= session.getAttribute("userName") != null ? session.getAttribute("userName") : "User" %>
                </span>
                <a href="${pageContext.request.contextPath}/logoutServlet" class="btn btn-outline-danger btn-sm rounded-pill px-3">
                    <i class="bi bi-box-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
</nav>

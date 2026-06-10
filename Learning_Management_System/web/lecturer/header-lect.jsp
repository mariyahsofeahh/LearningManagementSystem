<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String lecturerCourseId = request.getParameter("courseId");
    if (lecturerCourseId == null) {
        lecturerCourseId = (String) request.getAttribute("courseId");
    }
    String lectActivePage = (String) request.getAttribute("activePage");
    if (lectActivePage == null) {
        lectActivePage = "";
    }
%>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">

<style>
    .glass-nav-lect {
        background: rgba(255, 255, 255, 0.9);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border-bottom: 1px solid rgba(0, 0, 0, 0.08);
    }
    .nav-link-lect {
        font-weight: 600;
        color: #4b5563 !important;
        padding: 0.5rem 1rem !important;
        border-radius: 0.5rem;
        transition: all 0.2s ease;
    }
    .nav-link-lect:hover {
        background-color: rgba(37, 99, 235, 0.08);
        color: #1d4ed8 !important;
    }
    .nav-link-lect.active {
        background-color: #1d4ed8;
        color: #ffffff !important;
    }
</style>

<nav class="navbar navbar-expand-lg glass-nav-lect sticky-top py-3">
    <div class="container">
        <a class="navbar-brand fw-bold text-dark d-flex align-items-center" href="${pageContext.request.contextPath}/DashboardServlet">
            <span class="bg-primary text-white p-2 rounded-3 me-2 d-inline-flex shadow-sm">
                <i class="bi bi-layers-half"></i>
            </span>
            <span>eduSphere <span class="badge bg-light text-primary border font-monospace ms-1" style="font-size: 0.65rem;">FACULTY</span></span>
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#lecturerNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="lecturerNavbar">
            <ul class="navbar-nav mx-auto mb-2 mb-lg-0 gap-1">
                <% if (lecturerCourseId != null && !lecturerCourseId.isEmpty()) { %>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect" href="${pageContext.request.contextPath}/DashboardServlet">
                            <i class="bi bi-arrow-left-short"></i> All Courses
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect <%= lectActivePage.equals("stream") ? "active" : "" %>" href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= lecturerCourseId %>#stream">
                            <i class="bi bi-activity me-1"></i> Stream
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect <%= lectActivePage.equals("materials") ? "active" : "" %>" href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= lecturerCourseId %>#materials">
                            <i class="bi bi-folder2-open me-1"></i> Materials
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect <%= lectActivePage.equals("tasks") ? "active" : "" %>" href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= lecturerCourseId %>#tasks">
                            <i class="bi bi-journal-text me-1"></i> Tasks
                        </a>
                    </li>
                <% } else { %>
                    <li class="nav-item">
                        <a class="nav-link nav-link-lect active" href="${pageContext.request.contextPath}/DashboardServlet">
                            <i class="bi bi-house-gear-fill me-1"></i> Management Hub
                        </a>
                    </li>
                <% } %>
            </ul>

            <div class="d-flex align-items-center gap-2">
                <span class="navbar-text fw-medium text-dark bg-light px-3 py-2 rounded-pill border d-inline-flex align-items-center">
                    <i class="bi bi-person-badge-fill text-primary me-2"></i>
                    Lecturer: <%= session.getAttribute("userName") != null ? session.getAttribute("userName") : "Professor" %>
                </span>
                <a href="${pageContext.request.contextPath}/logoutServlet" class="btn btn-outline-danger btn-sm rounded-pill px-3">
                    <i class="bi bi-box-arrow-right me-1"></i> Logout
                </a>
            </div>
        </div>
    </div>
</nav>

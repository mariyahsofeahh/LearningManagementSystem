<%-- 
    Document   : dashboard-lecturer
    Created on : 4 Jun 2026, 2:39:19 pm
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
        <div class="d-flex justify-content-between align-items-center mb-5">
            <div>
                <h1 class="fw-bold tracking-tight mb-1">Instructional Control Console</h1>
                <p class="text-muted mb-0">Manage assigned curriculum structures and evaluate student pipeline submissions.</p>
            </div>
            <button class="btn btn-dark rounded-3 px-4 py-2 fw-medium"><i class="bi bi-plus-lg me-2"></i>Create New Course</button>
        </div>

        <div class="row g-4">
            <% 
                List<Map<String, String>> courses = (List<Map<String, String>>) request.getAttribute("courses");
                if (courses != null && !courses.isEmpty()) {
                    for (Map<String, String> course : courses) {
            %>
            <div class="col-xl-4 col-md-6">
                <a href="DashboardServlet?courseId=<%= course.get("id") %>" class="card faculty-card h-100 p-4">
                    <div class="d-flex align-items-center justify-content-between mb-4">
                        <span class="badge bg-dark text-white font-monospace rounded-pill small"><%= course.get("code") %></span>
                        <span class="text-primary small fw-semibold">Manage Studio <i class="bi bi-gear ms-1"></i></span>
                    </div>
                    <h4 class="fw-bold text-dark mb-2"><%= course.get("name") %></h4>
                    <p class="text-muted small mt-auto mb-0"><i class="bi bi-people-fill me-1"></i> View Registered Students</p>
                </a>
            </div>
            <% } } %>
        </div>
    </div>
</body>
</html>
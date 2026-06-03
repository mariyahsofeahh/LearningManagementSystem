<%-- 
    Document   : material-student
    Created on : 3 Jun 2026, 3:55:09 pm
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student - Learning Materials</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="#"><i class="bi bi-mortarboard-fill me-2"></i>LMS Portal</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="#">Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link active" href="LearningMaterialServlet">Materials</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">Assignments</a></li>
                </ul>
                <span class="navbar-text text-white">
                    <i class="bi bi-person-circle me-1"></i> Student View
                </span>
            </div>
        </div>
    </nav>

    <div class="container my-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="bi bi-journal-bookmark-fill me-2 text-primary"></i>Course Materials</h2>
            <div class="input-group w-25">
                <input type="text" class="form-control" placeholder="Search materials...">
                <button class="btn btn-outline-secondary" type="button"><i class="bi bi-search"></i></button>
            </div>
        </div>

        <div class="card border-0 shadow-sm">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th class="ps-4">Resource Name</th>
                                <th>Type</th>
                                <th>Upload Date</th>
                                <th class="text-end pe-4">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                List<Map<String, String>> materials = (List<Map<String, String>>) request.getAttribute("materials");
                                if (materials != null && !materials.isEmpty()) {
                                    for (Map<String, String> material : materials) {
                                        String isVideo = material.get("fileType").contains("video") || material.get("fileName").endsWith(".mp4") ? "true" : "false";
                            %>
                            <tr>
                                <td class="ps-4 fw-semibold">
                                    <% if(isVideo.equals("true")) { %>
                                        <i class="bi bi-play-btn-fill text-danger me-2"></i>
                                    <% } else { %>
                                        <i class="bi bi-file-earmark-pdf-fill text-primary me-2"></i>
                                    <% } %>
                                    <%= material.get("fileName") %>
                                </td>
                                <td><span class="badge bg-secondary-subtle text-secondary text-uppercase"><%= material.get("fileType") %></span></td>
                                <td class="text-muted"><%= material.get("uploadDate") %></td>
                                <td class="text-end pe-4">
                                    <a href="LearningMaterialServlet?action=view&id=<%= material.get("id") %>" target="_blank" class="btn btn-sm btn-outline-primary">
                                        <i class="bi bi-eye-fill me-1"></i> View Material
                                    </a>
                                </td>
                            </tr>
                            <% 
                                    }
                                } else { 
                            %>
                            <tr>
                                <td colspan="4" class="text-center py-5 text-muted">
                                    <i class="bi bi-folder-x display-6 d-block mb-2"></i> No learning materials available.
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
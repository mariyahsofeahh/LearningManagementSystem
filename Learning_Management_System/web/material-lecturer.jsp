<%-- 
    Document   : material-lecturer
    Created on : 3 Jun 2026, 4:02:21 pm
    Author     : DELL
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lecturer - Manage Materials</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .upload-zone { border: 2px dashed #dee2e6; cursor: pointer; transition: background 0.2s; }
        .upload-zone:hover { background-color: #f8f9fa; border-color: #0d6efd; }
    </style>
</head>
<body class="bg-light">

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="#"><i class="bi bi-mortarboard-fill me-2"></i>LMS Portal</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="#">Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link active" href="LearningMaterialServlet">Materials Management</a></li>
                </ul>
                <span class="navbar-text text-white"><i class="bi bi-person-workspace me-1"></i> Lecturer View</span>
            </div>
        </div>
    </nav>

    <div class="container my-5">
        <% if(request.getParameter("success") != null) { %>
            <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
                <div class="toast show align-items-center text-white bg-success border-0" role="alert">
                    <div class="d-flex">
                        <div class="toast-body"><i class="bi bi-check-circle-fill me-2"></i> Action completed successfully!</div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                    </div>
                </div>
            </div>
        <% } %>

        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card border-0 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title mb-4">Upload New Resource</h5>
                        <form action="LearningMaterialServlet" method="POST" enctype="multipart/form-data">
                            <div class="upload-zone p-4 rounded text-center mb-3" onclick="document.getElementById('fileInput').click()">
                                <i class="bi bi-cloud-arrow-up-fill text-primary display-4 mb-2"></i>
                                <p class="small text-muted mb-0">Click or Drag & Drop material files here</p>
                                <input type="file" name="file" id="fileInput" class="d-none" required onchange="this.form.submit()">
                            </div>
                            <div class="text-center text-muted small"><i class="bi bi-hdd-fill me-1"></i>Stored Locally on Tomcat Server</div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-md-8">
                <div class="card border-0 shadow-sm">
                    <div class="card-body p-0">
                        <div class="p-3 border-bottom bg-light d-flex justify-content-between align-items-center">
                            <h5 class="mb-0">Active Resource Management Suite</h5>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead>
                                    <tr>
                                        <th class="ps-3">File Information</th>
                                        <th>Uploaded</th>
                                        <th class="text-end pe-3">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% 
                                        List<Map<String, String>> materials = (List<Map<String, String>>) request.getAttribute("materials");
                                        if (materials != null && !materials.isEmpty()) {
                                            for (Map<String, String> material : materials) {
                                    %>
                                    <tr>
                                        <td class="ps-3 fw-medium"><i class="bi bi-file-earmark-text text-secondary me-2"></i><%= material.get("fileName") %></td>
                                        <td class="text-muted small"><%= material.get("uploadDate") %></td>
                                        <td class="text-end pe-3">
                                            <a href="#" class="btn btn-sm btn-light text-primary me-1"><i class="bi bi-pencil-square"></i></a>
                                            <a href="LearningMaterialServlet?action=delete&id=<%= material.get("id") %>" class="btn btn-sm btn-light text-danger"><i class="bi bi-trash3-fill"></i></a>
                                        </td>
                                    </tr>
                                    <% 
                                            }
                                        } else { 
                                    %>
                                    <tr><td colspan="3" class="text-center py-4 text-muted">No materials found.</td></tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
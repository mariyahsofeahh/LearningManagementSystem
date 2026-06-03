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
    <title>Student Workspace - Learning Materials</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #fcfcfd; color: #101828; }
        .glass-nav { background: rgba(255, 255, 255, 0.8); backdrop-filter: blur(12px); border-bottom: 1px solid #eaecf0; }
        .search-wrapper .form-control { border-radius: 10px; border: 1px solid #d0d5dd; padding-left: 2.5rem; }
        .search-wrapper .bi-search { position: absolute; left: 1rem; top: 50%; transform: translateY(-50%); color: #667085; }
        
        /* Modernized Dashboard Card Matrix */
        .material-card { 
            border: 1px solid #eaecf0; 
            border-radius: 16px; 
            background: #ffffff; 
            transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1); 
        }
        .material-card:hover { 
            transform: translateY(-4px); 
            box-shadow: 0px 12px 16px -4px rgba(16, 24, 40, 0.08), 0px 4px 6px -2px rgba(16, 24, 40, 0.03); 
            border-color: #d0d5dd;
        }
        .icon-shape {
            width: 48px;
            height: 48px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 12px;
        }
        .bg-pdf { background-color: #fef3f2; color: #b42318; }
        .bg-video { background-color: #f0f5ff; color: #175cd3; }
        .btn-action { border-radius: 10px; font-weight: 500; transition: all 0.2s; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg glass-nav sticky-top">
        <div class="container">
            <a class="navbar-brand fw-700 text-dark d-flex align-items-center" href="#">
                <span class="bg-primary text-white p-2 rounded-3 me-2 d-inline-flex"><i class="bi bi-layers-half"></i></span>
                <span class="fw-bold tracking-tight">eduSphere</span>
            </a>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mx-auto bg-light p-1 rounded-pill px-3">
                    <li class="nav-item"><a class="nav-link text-muted px-3" href="#">Overview</a></li>
                    <li class="nav-item"><a class="nav-link active text-primary fw-semibold px-3" href="LearningMaterialServlet">Repository</a></li>
                    <li class="nav-item"><a class="nav-link text-muted px-3" href="#">Syllabus</a></li>
                </ul>
                <div class="d-flex align-items-center">
                    <div class="text-end me-3 d-none d-sm-block">
                        <p class="small fw-semibold mb-0">Alex Mercer</p>
                        <p class="text-muted mb-0 style" style="font-size: 0.75rem;">Undergraduate</p>
                    </div>
                    <img src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=100&q=80" class="rounded-circle border" width="40" height="40" alt="Avatar">
                </div>
            </div>
        </div>
    </nav>

    <div class="container my-5">
        <div class="row align-items-center mb-5 row-gap-3">
            <div class="col-md-6">
                <h1 class="fw-bold tracking-tight mb-1">Academic Repository</h1>
                <p class="text-muted mb-0">Access structural learning blueprints, video presentations, and course documentation.</p>
            </div>
            <div class="col-md-6">
                <div class="search-wrapper position-relative ms-md-auto" style="max-width: 400px;">
                    <i class="bi bi-search"></i>
                    <input type="text" class="form-control py-2.5 ps-5 shadow-sm bg-white" placeholder="Search parameters or file formats...">
                </div>
            </div>
        </div>

        <div class="row g-4">
            <% 
                List<Map<String, String>> materials = (List<Map<String, String>>) request.getAttribute("materials");
                if (materials != null && !materials.isEmpty()) {
                    for (Map<String, String> material : materials) {
                        boolean isVideo = material.get("fileType").contains("video") || material.get("fileName").endsWith(".mp4");
            %>
            <div class="col-xl-4 col-md-6">
                <div class="card material-card p-4 shadow-sm h-100">
                    <div class="d-flex align-items-start justify-content-between mb-4">
                        <div class="icon-shape <%= isVideo ? "bg-video" : "bg-pdf" %>">
                            <i class="bi <%= isVideo ? "bi-play-circle-fill" : "bi-file-earmark-pdf-fill" %> fs-4"></i>
                        </div>
                        <span class="badge rounded-pill bg-light text-dark border px-3 py-2 text-uppercase font-monospace" style="font-size: 0.7rem;">
                            <%= material.get("fileType").split("/")[material.get("fileType").split("/").length - 1] %>
                        </span>
                    </div>
                    
                    <h5 class="fw-bold mb-1 text-truncate" title="<%= material.get("fileName") %>">
                        <%= material.get("fileName") %>
                    </h5>
                    <p class="text-muted small mb-4"><i class="bi bi-calendar3 me-1"></i> Published: <%= material.get("uploadDate").substring(0, 10) %></p>
                    
                    <div class="mt-auto pt-3 border-top d-flex align-items-center justify-content-between">
                        <span class="text-muted small"><i class="bi bi-hdd me-1"></i> Cloud Ready</span>
                        <a href="LearningMaterialServlet?action=view&id=<%= material.get("id") %>" target="_blank" class="btn btn-outline-dark btn-sm btn-action px-4">
                            Launch Resource <i class="bi bi-arrow-up-right ms-1"></i>
                        </a>
                    </div>
                </div>
            </div>
            <% 
                    }
                } else { 
            %>
            <div class="col-100 text-center py-5">
                <div class="p-5 bg-white rounded-4 border border-dashed text-center max-width-md mx-auto" style="max-width: 500px;">
                    <div class="icon-shape bg-light mx-auto mb-3"><i class="bi bi-folder-x fs-3 text-muted"></i></div>
                    <h5 class="fw-bold">No active material vectors found</h5>
                    <p class="text-muted small">Your course lecturer hasn't added files to this directory node yet.</p>
                </div>
            </div>
            <% } %>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
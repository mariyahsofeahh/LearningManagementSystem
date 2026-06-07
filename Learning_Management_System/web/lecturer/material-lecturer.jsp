<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Faculty Portal - Resource Pipeline</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #fcfcfd; color: #101828; }
        .glass-nav { background: rgba(255, 255, 255, 0.8); backdrop-filter: blur(12px); border-bottom: 1px solid #eaecf0; }
        
        /* Modern Drag & Drop Zone */
        .drop-zone {
            border: 2px dashed #d0d5dd;
            background: #ffffff;
            border-radius: 16px;
            transition: all 0.2s ease-in-out;
        }
        .drop-zone:hover {
            border-color: #444ce7;
            background-color: #f5f6ff;
        }
        
        .table-card { border-radius: 16px; border: 1px solid #eaecf0; overflow: hidden; background: #ffffff; }
        .action-icon-btn {
            width: 36px; height: 36px; display: inline-flex; align-items: center; justify-content: center;
            border-radius: 8px; border: 1px solid #eaecf0; background: #fff; color: #475467; transition: all 0.2s;
        }
        .action-icon-btn:hover { background: #f9fafb; color: #101828; border-color: #d0d5dd; }
        .action-icon-btn.btn-delete:hover { background: #fef3f2; color: #b42318; border-color: #fda29b; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg glass-nav sticky-top">
        <div class="container">
            <a class="navbar-brand fw-bold text-dark d-flex align-items-center" href="#">
                <span class="bg-dark text-white p-2 rounded-3 me-2 d-inline-flex"><i class="bi bi-terminal-box-fill"></i></span>
                <span>eduSphere <span class="text-primary fw-light">Faculty</span></span>
            </a>
            <div class="d-flex align-items-center">
                <span class="badge bg-dark-subtle text-dark border px-3 py-2 rounded-pill me-3">Professor Mode</span>
                <img src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?auto=format&fit=crop&w=100&q=80" class="rounded-circle border" width="40" height="40">
            </div>
        </div>
    </nav>

    <div class="container my-5">
        <% if(request.getParameter("success") != null) { %>
            <div class="position-fixed bottom-0 end-0 p-4" style="z-index: 11">
                <div class="toast show border-0 shadow-lg p-2 rounded-3 bg-white" role="alert" style="border-left: 4px solid #039855 !important;">
                    <div class="d-flex align-items-center">
                        <div class="p-2 bg-success text-white rounded-circle me-3 ms-2 d-inline-flex"><i class="bi bi-check2"></i></div>
                        <div class="toast-body p-0 fw-medium">Core directory states synchronized successfully.</div>
                        <button type="button" class="btn-close ms-auto me-2" data-bs-dismiss="toast"></button>
                    </div>
                </div>
            </div>
        <% } %>

        <div class="row g-5">
            <div class="col-lg-4">
                <div class="sticky-top" style="top: 100px;">
                    <h4 class="fw-bold mb-1">Asset Intake Pipeline</h4>
                    <p class="text-muted small mb-4">Distribute lectures, scripts, matrices and visual material instantly down to standard client roles.</p>
                    
                    <form action="LearningMaterialServlet" method="POST" enctype="multipart/form-data">
                        <div class="drop-zone p-5 text-center shadow-sm cursor-pointer" onclick="document.getElementById('fileInput').click()">
                            <div class="icon-shape bg-light mx-auto mb-3 text-primary"><i class="bi bi-cloud-plus-fill fs-3"></i></div>
                            <h6 class="fw-semibold mb-1">Upload deployment package</h6>
                            <p class="text-muted small mb-0">Click to browse your workstation file tree</p>
                            <input type="file" name="file" id="fileInput" class="d-none" required onchange="this.form.submit()">
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-lg-8">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h4 class="fw-bold mb-0">Active Course Manifest</h4>
                        <p class="text-muted small mb-0">Auditing and modulating distributed educational files.</p>
                    </div>
                </div>

                <div class="card table-card shadow-sm">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="bg-light border-bottom">
                                <tr>
                                    <th class="ps-4 py-3 text-muted fw-semibold small">RESOURCE IDENTIFIER</th>
                                    <th class="py-3 text-muted fw-semibold small">INGESTION TIMESTEP</th>
                                    <th class="text-end pe-4 py-3 text-muted fw-semibold small">MUTATE</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% 
                                    List<Map<String, String>> materials = (List<Map<String, String>>) request.getAttribute("materials");
                                    if (materials != null && !materials.isEmpty()) {
                                        for (Map<String, String> material : materials) {
                                %>
                                <tr>
                                    <td class="ps-4 py-3.5">
                                        <div class="d-flex align-items-center">
                                            <div class="p-2 bg-light rounded-3 text-secondary me-3 d-inline-flex"><i class="bi bi-file-earmark-text-fill fs-5"></i></div>
                                            <span class="fw-semibold text-dark"><%= material.get("fileName") %></span>
                                        </div>
                                    </td>
                                    <td class="text-muted small"><%= material.get("uploadDate").substring(0, 16) %></td>
                                    <td class="text-end pe-4">
                                        <a href="#" class="action-icon-btn me-1"><i class="bi bi-pencil"></i></a>
                                        <a href="LearningMaterialServlet?action=delete&id=<%= material.get("id") %>" class="action-icon-btn btn-delete"><i class="bi bi-trash3"></i></a>
                                    </td>
                                </tr>
                                <% 
                                        }
                                    } else { 
                                    %>
                                <tr>
                                    <td colspan="3" class="text-center py-5 text-muted">
                                        <i class="bi bi-bezier2 display-6 d-block mb-2 text-black-50"></i> Pipeline directory empty.
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
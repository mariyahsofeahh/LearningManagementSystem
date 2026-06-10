<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%!
    private String safe(String value) {
        return value == null ? "" : value;
    }

    private boolean isMaterial(Map<String, String> item) {
        return "material".equalsIgnoreCase(safe(item.get("type")));
    }
%>
<%
    String courseTitle = safe((String) request.getAttribute("selectedCourseName"));
    String courseCode = safe((String) request.getAttribute("selectedCourseCode"));
    String description = safe((String) request.getAttribute("selectedCourseDesc"));
    String lecturerId = safe((String) request.getAttribute("selectedLecturerId"));
    String userRole = safe((String) session.getAttribute("userRole"));
    boolean isLecturer = "lecturer".equalsIgnoreCase(userRole);
    List<Map<String, String>> tasks = (List<Map<String, String>>) request.getAttribute("tasks");
    List<Map<String, String>> materials = (List<Map<String, String>>) request.getAttribute("materials");
    List<Map<String, String>> streamItems = (List<Map<String, String>>) request.getAttribute("streamItems");
    List<Map<String, String>> upcomingTasks = (List<Map<String, String>>) request.getAttribute("upcomingTasks");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= courseTitle %> - Course Workspace</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body { font-family: 'Inter', sans-serif; background: #f8fafc; color: #101828; }
        .glass-nav { background: rgba(255, 255, 255, 0.9); backdrop-filter: blur(12px); border-bottom: 1px solid #e4e7ec; }
        .brand-mark { width: 40px; height: 40px; border-radius: 10px; display: inline-flex; align-items: center; justify-content: center; background: #2563eb; color: #fff; }
        .course-hero { background: linear-gradient(135deg, #1d4ed8 0%, #0f766e 100%); border-radius: 18px; color: #fff; padding: 34px; min-height: 190px; display: flex; flex-direction: column; justify-content: space-between; }
        .course-pill { background: rgba(255,255,255,.16); border: 1px solid rgba(255,255,255,.28); border-radius: 999px; padding: 6px 12px; font-size: 13px; width: fit-content; }
        .tab-button { border: 0; background: transparent; color: #667085; font-weight: 700; padding: 14px 16px; border-bottom: 3px solid transparent; }
        .tab-button.active { color: #1d4ed8; border-bottom-color: #1d4ed8; }
        .workspace-section { display: none; }
        .workspace-section.active { display: block; }
        .panel { background: #fff; border: 1px solid #e4e7ec; border-radius: 14px; box-shadow: 0 8px 20px rgba(16, 24, 40, .04); }
        .item-icon { width: 46px; height: 46px; border-radius: 12px; display: inline-flex; align-items: center; justify-content: center; flex: 0 0 46px; }
        .icon-material { background: #eef4ff; color: #1d4ed8; }
        .icon-task { background: #ecfdf3; color: #027a48; }
        .empty-state { border: 1px dashed #cbd5e1; border-radius: 14px; padding: 30px; text-align: center; color: #667085; background: #fff; }
        .side-card { position: sticky; top: 92px; }
        .form-control, .form-select { border-radius: 10px; }
        .btn { border-radius: 10px; font-weight: 700; }
        @media (max-width: 991px) { .side-card { position: static; } .course-hero { padding: 24px; } }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg glass-nav sticky-top py-3">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center gap-2 fw-bold" href="${pageContext.request.contextPath}/DashboardServlet">
            <span class="brand-mark"><i class="bi bi-layers-half"></i></span>
            <span>eduSphere</span>
        </a>
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#courseNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="courseNavbar">
            <div class="navbar-nav mx-auto gap-1">
                <button class="tab-button active" data-tab="stream" type="button"><i class="bi bi-activity me-1"></i>Stream</button>
                <button class="tab-button" data-tab="materials" type="button"><i class="bi bi-folder2-open me-1"></i>Materials</button>
                <button class="tab-button" data-tab="tasks" type="button"><i class="bi bi-check2-square me-1"></i>Tasks</button>
                <button class="tab-button" data-tab="people" type="button"><i class="bi bi-people me-1"></i>People</button>
            </div>
            <div class="d-flex align-items-center gap-2">
                <a class="btn btn-light border" href="${pageContext.request.contextPath}/DashboardServlet">
                    <i class="bi bi-arrow-left me-1"></i>Courses
                </a>
                <a class="btn btn-outline-danger" href="${pageContext.request.contextPath}/logoutServlet" title="Logout">
                    <i class="bi bi-box-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
</nav>

<main class="container my-4 my-lg-5">
    <% if (request.getParameter("success") != null) { %>
        <div class="alert alert-success rounded-4 border-0 shadow-sm"><i class="bi bi-check-circle-fill me-2"></i>Action completed successfully.</div>
    <% } %>
    <% if (request.getParameter("error") != null) { %>
        <div class="alert alert-danger rounded-4 border-0 shadow-sm"><i class="bi bi-exclamation-triangle-fill me-2"></i>Something went wrong. Please check the form and try again.</div>
    <% } %>

    <section class="course-hero mb-4">
        <div class="d-flex flex-wrap justify-content-between align-items-start gap-3">
            <div>
                <div class="course-pill mb-3"><%= courseCode %></div>
                <h1 class="display-6 fw-bold mb-2"><%= courseTitle %></h1>
                <p class="mb-0 opacity-75" style="max-width: 720px;"><%= description %></p>
            </div>
            <% if (isLecturer) { %>
                <a class="btn btn-light" href="${pageContext.request.contextPath}/course/edit?id=<%= courseCode %>">
                    <i class="bi bi-pencil-square me-1"></i>Edit Course
                </a>
            <% } %>
        </div>
        <div class="d-flex flex-wrap gap-3 mt-4 small opacity-75">
            <span><i class="bi bi-person-badge me-1"></i>Lecturer ID: <%= lecturerId %></span>
            <span><i class="bi bi-shield-check me-1"></i><%= isLecturer ? "Lecturer workspace" : "Student workspace" %></span>
        </div>
    </section>

    <section id="tab-stream" class="workspace-section active">
        <div class="row g-4">
            <div class="col-lg-8">
                <% if (streamItems != null && !streamItems.isEmpty()) {
                    for (Map<String, String> item : streamItems) {
                        boolean materialItem = isMaterial(item);
                %>
                    <div class="panel p-4 mb-3">
                        <div class="d-flex gap-3">
                            <div class="item-icon <%= materialItem ? "icon-material" : "icon-task" %>">
                                <i class="bi <%= materialItem ? "bi-file-earmark-text" : "bi-clipboard-check" %> fs-4"></i>
                            </div>
                            <div class="flex-grow-1">
                                <div class="d-flex flex-wrap justify-content-between gap-2">
                                    <div>
                                        <span class="badge <%= materialItem ? "text-bg-primary" : "text-bg-success" %> mb-2"><%= materialItem ? "Material" : "Task" %></span>
                                        <h3 class="h5 fw-bold mb-1"><%= safe(item.get("title")) %></h3>
                                    </div>
                                    <span class="text-muted small"><%= safe(item.get("displayDate")) %></span>
                                </div>
                                <p class="text-muted mb-3"><%= safe(item.get("description")) %></p>
                                <% if (materialItem) { %>
                                    <a class="btn btn-outline-primary btn-sm" target="_blank" href="${pageContext.request.contextPath}/LearningMaterialServlet?action=view&id=<%= safe(item.get("id")) %>">
                                        <i class="bi bi-box-arrow-up-right me-1"></i>Open File
                                    </a>
                                <% } else { %>
                                    <div class="d-flex flex-wrap align-items-center gap-2">
                                        <span class="text-muted small"><i class="bi bi-calendar-event me-1"></i>Due: <%= safe(item.get("dueDate")) %></span>
                                        <a class="btn btn-outline-success btn-sm" href="${pageContext.request.contextPath}/assignment/view?id=<%= safe(item.get("id")) %>">
                                            <%= isLecturer ? "Review Submissions" : "Open Task" %>
                                        </a>
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                <%  }
                } else { %>
                    <div class="empty-state">No materials or tasks have been posted yet.</div>
                <% } %>
            </div>
            <div class="col-lg-4">
                <div class="panel side-card p-4">
                    <div class="d-flex align-items-center justify-content-between mb-3">
                        <h2 class="h5 fw-bold mb-0">Upcoming Tasks</h2>
                        <i class="bi bi-calendar2-week text-primary fs-4"></i>
                    </div>
                    <% if (upcomingTasks != null && !upcomingTasks.isEmpty()) {
                        for (Map<String, String> task : upcomingTasks) {
                    %>
                        <a class="d-block text-decoration-none text-dark border-top py-3" href="${pageContext.request.contextPath}/assignment/view?id=<%= safe(task.get("id")) %>">
                            <div class="fw-bold"><%= safe(task.get("title")) %></div>
                            <div class="small text-muted">Due: <%= safe(task.get("dueDate")) %></div>
                        </a>
                    <%  }
                    } else { %>
                        <p class="text-muted mb-0">No upcoming tasks.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </section>

    <section id="tab-materials" class="workspace-section">
        <div class="row g-4">
            <% if (isLecturer) { %>
            <div class="col-lg-4">
                <div class="panel side-card p-4">
                    <h2 class="h5 fw-bold mb-3">Upload Material</h2>
                    <form action="${pageContext.request.contextPath}/LearningMaterialServlet" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="courseCode" value="<%= courseCode %>">
                        <label class="form-label fw-semibold">Choose file</label>
                        <input class="form-control mb-3" type="file" name="file" required>
                        <button class="btn btn-primary w-100" type="submit">
                            <i class="bi bi-cloud-arrow-up me-1"></i>Upload File
                        </button>
                    </form>
                </div>
            </div>
            <% } %>
            <div class="<%= isLecturer ? "col-lg-8" : "col-12" %>">
                <div class="panel p-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h2 class="h4 fw-bold mb-0">Learning Materials</h2>
                        <span class="badge text-bg-light border"><%= materials != null ? materials.size() : 0 %> files</span>
                    </div>
                    <% if (materials != null && !materials.isEmpty()) {
                        for (Map<String, String> material : materials) {
                    %>
                        <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 border-top py-3">
                            <div class="d-flex align-items-center gap-3">
                                <div class="item-icon icon-material"><i class="bi bi-file-earmark-text fs-4"></i></div>
                                <div>
                                    <div class="fw-bold"><%= safe(material.get("fileName")) %></div>
                                    <div class="text-muted small">Uploaded: <%= safe(material.get("uploadDate")) %></div>
                                </div>
                            </div>
                            <div class="d-flex gap-2">
                                <a class="btn btn-outline-primary btn-sm" target="_blank" href="${pageContext.request.contextPath}/LearningMaterialServlet?action=view&id=<%= safe(material.get("id")) %>">
                                    Open
                                </a>
                                <% if (isLecturer) { %>
                                    <a class="btn btn-outline-danger btn-sm" href="${pageContext.request.contextPath}/LearningMaterialServlet?action=delete&id=<%= safe(material.get("id")) %>&courseId=<%= courseCode %>" onclick="return confirm('Delete this material?');">
                                        Delete
                                    </a>
                                <% } %>
                            </div>
                        </div>
                    <%  }
                    } else { %>
                        <div class="empty-state">No learning materials uploaded yet.</div>
                    <% } %>
                </div>
            </div>
        </div>
    </section>

    <section id="tab-tasks" class="workspace-section">
        <div class="row g-4">
            <% if (isLecturer) { %>
            <div class="col-lg-4">
                <div class="panel side-card p-4">
                    <h2 class="h5 fw-bold mb-3">Create Task</h2>
                    <form action="${pageContext.request.contextPath}/assignment/create" method="post">
                        <input type="hidden" name="courseCode" value="<%= courseCode %>">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Title</label>
                            <input class="form-control" type="text" name="title" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Description</label>
                            <textarea class="form-control" name="description" rows="4" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Deadline</label>
                            <input class="form-control" type="date" name="deadline" required>
                        </div>
                        <button class="btn btn-success w-100" type="submit"><i class="bi bi-plus-lg me-1"></i>Create Task</button>
                    </form>
                </div>
            </div>
            <% } %>
            <div class="<%= isLecturer ? "col-lg-8" : "col-12" %>">
                <div class="panel p-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h2 class="h4 fw-bold mb-0">Tasks</h2>
                        <span class="badge text-bg-light border"><%= tasks != null ? tasks.size() : 0 %> tasks</span>
                    </div>
                    <% if (tasks != null && !tasks.isEmpty()) {
                        for (Map<String, String> task : tasks) {
                    %>
                        <div class="border-top py-3">
                            <div class="d-flex flex-wrap justify-content-between align-items-start gap-3">
                                <div>
                                    <h3 class="h5 fw-bold mb-1"><%= safe(task.get("title")) %></h3>
                                    <p class="text-muted mb-2"><%= safe(task.get("description")) %></p>
                                    <span class="badge text-bg-light border"><i class="bi bi-calendar-event me-1"></i>Due: <%= safe(task.get("dueDate")) %></span>
                                </div>
                                <a class="btn btn-outline-success btn-sm" href="${pageContext.request.contextPath}/assignment/view?id=<%= safe(task.get("id")) %>">
                                    <%= isLecturer ? "View Submissions" : "Submit Task" %>
                                </a>
                            </div>
                        </div>
                    <%  }
                    } else { %>
                        <div class="empty-state">No tasks have been created yet.</div>
                    <% } %>
                </div>
            </div>
        </div>
    </section>

    <section id="tab-people" class="workspace-section">
        <div class="panel p-4">
            <h2 class="h4 fw-bold">People</h2>
            <div class="border-top mt-3 pt-3">
                <div class="d-flex align-items-center gap-3">
                    <div class="item-icon icon-task"><i class="bi bi-person-badge fs-4"></i></div>
                    <div>
                        <div class="fw-bold">Course Lecturer</div>
                        <div class="text-muted small">Lecturer ID: <%= lecturerId %></div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const buttons = document.querySelectorAll('.tab-button');
    const sections = document.querySelectorAll('.workspace-section');

    function activateTab(tab) {
        buttons.forEach(button => button.classList.toggle('active', button.dataset.tab === tab));
        sections.forEach(section => section.classList.toggle('active', section.id === 'tab-' + tab));
        if (history.replaceState) {
            history.replaceState(null, '', '#' + tab);
        }
    }

    buttons.forEach(button => button.addEventListener('click', () => activateTab(button.dataset.tab)));

    const initialTab = window.location.hash ? window.location.hash.substring(1) : 'stream';
    if (document.getElementById('tab-' + initialTab)) {
        activateTab(initialTab);
    }
</script>
</body>
</html>

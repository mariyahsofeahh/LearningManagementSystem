<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%
    String courseTitle = (String) request.getAttribute("selectedCourseName");
    String courseCode = (String) request.getAttribute("selectedCourseCode");
    String description = (String) request.getAttribute("selectedCourseDesc");
    String lecturerId = (String) request.getAttribute("selectedLecturerId");
    String userRole = (String) session.getAttribute("userRole");
    List<Map<String, String>> tasks = (List<Map<String, String>>) request.getAttribute("tasks");
    List<Map<String, String>> materials = (List<Map<String, String>>) request.getAttribute("materials");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= courseTitle %> - Stream Workspace</title>
    <style>
        :root { --primary: #1a73e8; --text-main: #3c4043; --text-sub: #5f6368; --border-color: #dadce0; }
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Roboto', Arial, sans-serif; }
        body { background-color: #ffffff; color: var(--text-main); }
        .navbar { display: flex; flex-direction: column; border-bottom: 1px solid var(--border-color); position: sticky; top: 0; background: white; z-index: 100; }
        .nav-top { display: flex; justify-content: space-between; align-items: center; padding: 0 24px; height: 50px; }
        .back-btn { text-decoration: none; color: var(--text-sub); font-size: 14px; }
        .tabs-container { display: flex; justify-content: center; height: 48px; }
        .tab { height: 100%; padding: 0 24px; display: flex; align-items: center; color: var(--text-sub); font-weight: 500; font-size: 14px; border-bottom: 3px solid transparent; cursor: pointer; }
        .tab.active { color: var(--primary); border-bottom-color: var(--primary); }
        .tab:hover { background-color: #f1f3f4; }
        .workspace { max-width: 900px; margin: 24px auto; padding: 0 24px; display: none; }
        .workspace.active-view { display: block; }
        .course-banner { background: var(--primary); color: white; border-radius: 8px; padding: 40px; margin-bottom: 24px; }
        .banner-title { font-size: 36px; font-weight: 400; }
        .stream-layout { display: grid; grid-template-columns: 200px 1fr; gap: 24px; }
        .announcement-box { border: 1px solid var(--border-color); border-radius: 8px; padding: 20px; margin-bottom: 16px; background: white; }
        .sidebar-box { border: 1px solid var(--border-color); border-radius: 8px; padding: 16px; font-size: 14px; }
        .btn-primary-link { display: inline-flex; align-items: center; gap: 8px; background: var(--primary); color: #fff; text-decoration: none; border-radius: 6px; padding: 10px 14px; font-size: 14px; font-weight: 500; }
        .task-card { border: 1px solid var(--border-color); border-radius: 8px; padding: 18px; margin-top: 14px; background: #fff; }
        .task-meta { color: var(--text-sub); font-size: 13px; margin-top: 8px; line-height: 1.5; }
        .empty-box { margin-top: 20px; border: 2px dashed var(--border-color); padding: 20px; text-align: center; color: var(--text-sub); border-radius: 8px; }
    </style>
</head>
<body>

<div class="navbar">
    <div class="nav-top">
        <a href="${pageContext.request.contextPath}/DashboardServlet" class="back-btn">Back to Classes</a>
        <div style="font-size: 12px; background: #e8f0fe; color: var(--primary); padding: 4px 8px; border-radius: 12px;">Active Course Workspace</div>
    </div>
    <div class="tabs-container">
        <div class="tab active" onclick="switchTab('stream', this)">Stream</div>
        <div class="tab" onclick="switchTab('classwork', this)">Classwork</div>
        <div class="tab" onclick="switchTab('people', this)">People</div>
    </div>
</div>

<div id="tab-stream" class="workspace active-view">
    <div class="course-banner">
        <h1 class="banner-title"><%= courseTitle %></h1>
        <div>Class Code: <%= courseCode %></div>
        <% if ("lecturer".equalsIgnoreCase(userRole)) { %>
            <a href="${pageContext.request.contextPath}/course/edit?id=<%= courseCode %>" style="color: white; text-decoration: underline; display:inline-block; margin-top:12px;">
                Edit Course
            </a>
        <% } %>
    </div>

    <div class="stream-layout">
        <div class="sidebar-box">
            <h4>Upcoming Tasks</h4>
            <div style="margin-top: 8px; font-size: 13px;">
                <%
                    if (tasks != null && !tasks.isEmpty()) {
                        for (Map<String, String> t : tasks) {
                %>
                    <p style="margin-bottom: 6px;"><b><%= t.get("title") %></b><br><span style="color:var(--text-sub);">Due: <%= t.get("dueDate") %></span></p>
                <%
                        }
                    } else {
                %>
                    <p style="color: var(--text-sub);">No assignments due soon.</p>
                <% } %>
            </div>
        </div>

        <div>
            <div class="announcement-box">
                <h3 style="margin-bottom:10px;">Course Overview</h3>
                <p style="color: var(--text-sub); font-size: 14px; line-height: 1.6;"><%= description %></p>
            </div>

            <div class="announcement-box">
                <h3 style="margin-bottom:10px;">Learning Materials</h3>
                <%
                    if (materials != null && !materials.isEmpty()) {
                        for (Map<String, String> m : materials) {
                %>
                    <p style="padding: 6px 0; font-size:14px;"><a href="#" style="color: var(--primary); text-decoration: none;"><%= m.get("fileName") %></a> (<%= m.get("fileType") %>)</p>
                <%
                        }
                    } else {
                %>
                    <p style="color: var(--text-sub); font-size:14px;">No files posted yet.</p>
                <% } %>
            </div>
        </div>
    </div>
</div>

<div id="tab-classwork" class="workspace">
    <div class="announcement-box">
        <div style="display:flex; justify-content:space-between; align-items:center; gap:16px; margin-bottom: 12px;">
            <h2>Assignments</h2>
            <% if ("lecturer".equalsIgnoreCase(userRole)) { %>
                <a class="btn-primary-link" href="${pageContext.request.contextPath}/assignment/createPage?courseCode=<%= courseCode %>">Create Assignment</a>
            <% } %>
        </div>

        <%
            if (tasks != null && !tasks.isEmpty()) {
                for (Map<String, String> t : tasks) {
        %>
            <div class="task-card">
                <div style="display:flex; justify-content:space-between; gap:16px; align-items:flex-start;">
                    <div>
                        <h3 style="font-size:18px; margin-bottom:6px;"><%= t.get("title") %></h3>
                        <p class="task-meta"><%= t.get("description") != null ? t.get("description") : "" %></p>
                        <p class="task-meta">Due: <b><%= t.get("dueDate") %></b></p>
                    </div>
                    <a href="${pageContext.request.contextPath}/assignment/view?id=<%= t.get("id") %>" style="color:var(--primary); font-size:14px; white-space:nowrap;">
                        <%= "lecturer".equalsIgnoreCase(userRole) ? "View submissions" : "Open assignment" %>
                    </a>
                </div>
            </div>
        <%
                }
            } else {
        %>
            <div class="empty-box">No assignments posted for this course yet.</div>
        <% } %>
    </div>

    <div class="announcement-box">
        <h2>Materials</h2>
        <div class="empty-box">
            <a href="${pageContext.request.contextPath}/LearningMaterialServlet" style="color:var(--primary); font-size:14px;">Go to Material Management Storage Vault</a>
        </div>
    </div>
</div>

<div id="tab-people" class="workspace">
    <div class="announcement-box">
        <h2>Classroom Directory</h2>
        <h3 style="border-bottom:1px solid var(--border-color); margin-top:15px; padding-bottom:5px; color:var(--primary);">Teachers</h3>
        <p style="padding: 10px 0;">Lecturer ID Profile Code: <b><%= lecturerId %></b></p>
    </div>
</div>

<script>
    function switchTab(tabId, element) {
        document.querySelectorAll('.workspace').forEach(el => el.classList.remove('active-view'));
        document.querySelectorAll('.tab').forEach(el => el.classList.remove('active'));
        document.getElementById('tab-' + tabId).classList.add('active-view');
        element.classList.add('active');
    }
</script>
</body>
</html>

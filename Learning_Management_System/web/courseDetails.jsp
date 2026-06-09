<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%
    String courseTitle = (String) request.getAttribute("selectedCourseName");
    String courseCode = (String) request.getAttribute("selectedCourseCode");
    String courseId = (String) request.getAttribute("selectedCourseId");
    String description = (String) request.getAttribute("selectedCourseDesc");
    String lecturerId = (String) request.getAttribute("selectedLecturerId");
    String userRole = (String) session.getAttribute("userRole");

    if (courseTitle == null || courseTitle.trim().isEmpty()) {
        courseTitle = "Course Workspace";
    }
    if (courseCode == null || courseCode.trim().isEmpty()) {
        courseCode = courseId != null ? courseId : "";
    }
    if (courseId == null || courseId.trim().isEmpty()) {
        courseId = courseCode;
    }
    if (description == null || description.trim().isEmpty()) {
        description = "No course description available.";
    }
    if (lecturerId == null || lecturerId.trim().isEmpty()) {
        lecturerId = "N/A";
    }

    String materialRole = "lecturer".equalsIgnoreCase(userRole) ? "lecturer" : "student";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= courseTitle %> - Stream Workspace</title>
    <style>
        :root {
            --primary: #1a73e8;
            --text-main: #3c4043;
            --text-sub: #5f6368;
            --border-color: #dadce0;
        }
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: "Roboto", Arial, sans-serif;
        }
        body {
            background-color: #ffffff;
            color: var(--text-main);
        }
        .navbar {
            display: flex;
            flex-direction: column;
            border-bottom: 1px solid var(--border-color);
            position: sticky;
            top: 0;
            background: white;
            z-index: 100;
        }
        .nav-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 24px;
            height: 50px;
        }
        .back-btn {
            text-decoration: none;
            color: var(--text-sub);
            font-size: 14px;
        }
        .tabs-container {
            display: flex;
            justify-content: center;
            height: 48px;
        }
        .tab {
            height: 100%;
            padding: 0 24px;
            display: flex;
            align-items: center;
            color: var(--text-sub);
            font-weight: 500;
            font-size: 14px;
            border-bottom: 3px solid transparent;
            cursor: pointer;
        }
        .tab.active {
            color: var(--primary);
            border-bottom-color: var(--primary);
        }
        .tab:hover {
            background-color: #f1f3f4;
        }
        .workspace {
            max-width: 900px;
            margin: 24px auto;
            padding: 0 24px;
            display: none;
        }
        .workspace.active-view {
            display: block;
        }
        .course-banner {
            background: var(--primary);
            color: white;
            border-radius: 8px;
            padding: 40px;
            margin-bottom: 24px;
        }
        .banner-title {
            font-size: 36px;
            font-weight: 400;
            margin-bottom: 8px;
        }
        .stream-layout {
            display: grid;
            grid-template-columns: 200px 1fr;
            gap: 24px;
        }
        .announcement-box {
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 16px;
            background: white;
        }
        .sidebar-box {
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 16px;
            font-size: 14px;
        }
        .edit-link {
            display: inline-block;
            margin-top: 12px;
            color: white;
            text-decoration: underline;
            font-size: 14px;
        }
        .muted {
            color: var(--text-sub);
        }
        .material-link {
            color: var(--primary);
            text-decoration: none;
        }
        .integration-link {
            color: var(--primary);
            font-size: 14px;
        }
        @media (max-width: 700px) {
            .stream-layout {
                grid-template-columns: 1fr;
            }
            .course-banner {
                padding: 28px;
            }
            .banner-title {
                font-size: 28px;
            }
        }
    </style>
</head>
<body>

<div class="navbar">
    <div class="nav-top">
        <a href="${pageContext.request.contextPath}/DashboardServlet" class="back-btn">&larr; Classes</a>
        <div style="font-size: 12px; background: #e8f0fe; color: var(--primary); padding: 4px 8px; border-radius: 12px;">
            Active Session Workspace
        </div>
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
            <a href="${pageContext.request.contextPath}/course/edit?id=<%= courseCode %>" class="edit-link">
                Edit Course
            </a>
        <% } %>
    </div>

    <div class="stream-layout">
        <div class="sidebar-box">
            <h4>Upcoming Tasks</h4>
            <div style="margin-top: 8px; font-size: 13px;">
                <%
                    List<Map<String, String>> tasks = (List<Map<String, String>>) request.getAttribute("tasks");
                    if (tasks != null && !tasks.isEmpty()) {
                        for (Map<String, String> task : tasks) {
                %>
                    <p style="margin-bottom: 10px;">
                        <b><%= task.get("title") %></b><br>
                        <span class="muted">Due: <%= task.get("dueDate") %></span>
                    </p>
                <%
                        }
                    } else {
                %>
                    <p class="muted">No assignments due soon.</p>
                <% } %>
            </div>
        </div>

        <div>
            <div class="announcement-box">
                <h3 style="margin-bottom:10px;">Syllabus Overview</h3>
                <p class="muted" style="font-size: 14px; line-height: 1.6;"><%= description %></p>
            </div>

            <div class="announcement-box">
                <h3 style="margin-bottom:10px;">Shared Class Materials</h3>
                <%
                    List<Map<String, String>> materials = (List<Map<String, String>>) request.getAttribute("materials");
                    if (materials != null && !materials.isEmpty()) {
                        for (Map<String, String> material : materials) {
                %>
                    <p style="padding: 6px 0; font-size:14px;">
                        <a href="${pageContext.request.contextPath}/LearningMaterialServlet?action=view&id=<%= material.get("id") %>&role=<%= materialRole %>"
                           class="material-link"
                           target="_blank">
                            <%= material.get("fileName") %>
                        </a>
                        (<%= material.get("fileType") %>)
                    </p>
                <%
                        }
                    } else {
                %>
                    <p class="muted" style="font-size:14px;">No files posted to this course yet.</p>
                <% } %>
            </div>
        </div>
    </div>
</div>

<div id="tab-classwork" class="workspace">
    <div class="announcement-box">
        <h2>Classwork Hub</h2>
        <div style="margin-top: 20px; border: 2px dashed var(--border-color); padding: 20px; text-align: center; color: var(--text-sub);">
            <p><b>Assignment Management</b></p>
            <a href="${pageContext.request.contextPath}/assignment/createPage?courseId=<%= courseId %>" class="integration-link">
                Go to Assignment Management Subsystem &rarr;
            </a>
        </div>

        <div style="margin-top: 20px; border: 2px dashed var(--border-color); padding: 20px; text-align: center; color: var(--text-sub);">
            <p><b>Learning Materials</b></p>
            <a href="${pageContext.request.contextPath}/LearningMaterialServlet?courseId=<%= courseId %>&role=<%= materialRole %>" class="integration-link">
                Go to Material Management Storage &rarr;
            </a>
        </div>
    </div>
</div>

<div id="tab-people" class="workspace">
    <div class="announcement-box">
        <h2>Classroom Directory</h2>
        <h3 style="border-bottom:1px solid var(--border-color); margin-top:15px; padding-bottom:5px; color:var(--primary);">
            Teachers
        </h3>
        <p style="padding: 10px 0;">Lecturer ID: <b><%= lecturerId %></b></p>
    </div>
</div>

<script>
    function switchTab(tabId, element) {
        document.querySelectorAll(".workspace").forEach(function (el) {
            el.classList.remove("active-view");
        });
        document.querySelectorAll(".tab").forEach(function (el) {
            el.classList.remove("active");
        });
        document.getElementById("tab-" + tabId).classList.add("active-view");
        element.classList.add("active");
    }
</script>
</body>
</html>

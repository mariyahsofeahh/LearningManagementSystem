<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="lms.model.Course" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Faculty Control Panel - eduSphere</title>
    <style>
        :root {
            --bg-primary: #ffffff;
            --bg-secondary: #f8f9fa;
            --text-main: #1d1d1f;
            --text-muted: #515154;
            --accent-blue: #0066cc;
            --border-light: #e5e5e7;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
        }

        body {
            background-color: var(--bg-primary);
            color: var(--text-main);
            padding: 40px 80px;
        }

        /* Top Header Styling */
        .top-navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-bottom: 20px;
            border-bottom: 1px solid var(--border-light);
            margin-bottom: 48px;
        }

        .brand-logo {
            font-size: 20px;
            font-weight: 600;
        }

        .brand-logo span {
            color: var(--accent-blue);
            font-weight: 400;
        }

        .user-profile {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 15px;
            color: var(--text-main);
            font-weight: 500;
        }

        /* Main Workspace Branding */
        .hero-section {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 40px;
        }

        .hero-title {
            font-size: 40px;
            font-weight: 700;
            letter-spacing: -0.5px;
            margin-bottom: 8px;
        }

        .hero-subtitle {
            font-size: 16px;
            color: var(--text-muted);
        }

        /* Buttons matching the clean design */
        .btn-action {
            background-color: #1c1c1e;
            color: white;
            text-decoration: none;
            padding: 12px 24px;
            border-radius: 8px;
            font-weight: 500;
            font-size: 15px;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            transition: background 0.2s ease;
        }

        .btn-action:hover {
            background-color: #2c2c2e;
        }

        /* Course Layout Grid */
        .course-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
            gap: 24px;
        }

        /* Clean Studio Cards */
        .course-card {
            background: var(--bg-primary);
            border: 1px solid var(--border-light);
            border-radius: 16px;
            padding: 28px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            min-height: 220px; /* Slight adjustment to comfortably fit the delete action footer */
            transition: box-shadow 0.2s ease, border-color 0.2s ease;
        }

        .course-card:hover {
            border-color: #d2d2d7;
            box-shadow: 0 4px 12px rgba(0,0,0,0.04);
        }

        .card-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
        }

        .course-tag {
            background: #1c1c1e;
            color: white;
            font-size: 11px;
            font-weight: 600;
            padding: 4px 8px;
            border-radius: 6px;
            text-transform: uppercase;
        }

        .manage-link {
            color: var(--accent-blue);
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .manage-link:hover {
            text-decoration: underline;
        }

        .course-name {
            font-size: 24px;
            font-weight: 600;
            line-height: 1.25;
            margin-bottom: 12px;
        }

        .card-footer-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 16px;
            padding-top: 12px;
            border-top: 1px dashed var(--border-light);
        }

        .student-count-wrapper {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            color: var(--text-muted);
        }

        /* Minimal Trash Button styling matching the theme layout */
        .btn-delete-inline {
            background: none;
            border: none;
            color: #ff3b30;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 4px 8px;
            border-radius: 6px;
            transition: background 0.2s ease;
        }

        .btn-delete-inline:hover {
            background: #ffe5e5;
        }

        .status-banner {
            padding: 14px;
            border-radius: 8px;
            margin-bottom: 24px;
            font-size: 14px;
            font-weight: 500;
        }
    </style>
</head>
<body>

<%
    // Fetch details from session safely
    String userRole = (String) session.getAttribute("userRole");
    String loggedInUserId = (String) session.getAttribute("userId");
    
    List<Course> courses = (List<Course>) request.getAttribute("courses");
%>

<div class="top-navbar">
    <div class="brand-logo">● eduSphere <span>Faculty</span></div>
    <div class="user-profile">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
        Professor: Hannah Delisha
    </div>
</div>

<% if("created".equals(request.getParameter("success"))) { %>
    <div class="status-banner" style="background: #e6f4ea; color: #137333;">🎉 New classroom node instantiated successfully!</div>
<% } else if("deleted".equals(request.getParameter("success"))) { %>
    <div class="status-banner" style="background: #e6f4ea; color: #137333;">🗑️ Course curriculum removed successfully.</div>
<% } %>

<div class="hero-section">
    <div>
        <h1 class="hero-title">Instructional Control Console</h1>
        <p class="hero-subtitle">Manage assigned curriculum structures and evaluate student pipeline submissions.</p>
    </div>
    
    <%-- Show 'Create' only if they are a lecturer --%>
    <% if ("lecturer".equalsIgnoreCase(userRole)) { %>
        <a href="${pageContext.request.contextPath}/course/create" class="btn-action">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
            Create New Course
        </a>
    <% } %>
</div>

<div class="course-grid">
    <% 
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
    %>
                <div class="course-card">
                    <div>
                        <div class="card-top">
                            <span class="course-tag"><%= course.getCourseCode() %></span>
                            
                            <% if ("lecturer".equalsIgnoreCase(userRole)) { %>
                                <a href="${pageContext.request.contextPath}/course/edit?id=<%= course.getCourseCode() %>" class="manage-link">
                                    Manage Studio 
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"></circle><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"></path></svg>
                                </a>
                            <% } %>
                        </div>
                        
                        <div class="course-name"><%= course.getTitle() %></div>
                    </div>

                    <div class="card-footer-actions">
                        <div class="student-count-wrapper">
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                            <a href="${pageContext.request.contextPath}/course/view?id=<%= course.getCourseCode() %>" style="color: var(--text-muted); text-decoration: none;">
                                View Registered Students
                            </a>
                        </div>

                        <%-- 🔑 ATTACH THE DELETE ACTION FORM HERE FOR LECTURERS ONLY --%>
                        <% if ("lecturer".equalsIgnoreCase(userRole)) { %>
                            <form action="${pageContext.request.contextPath}/course/delete" method="POST" onsubmit="return confirm('Are you absolute sure you want to permanently delete this course?');" style="display: inline;">
                                <input type="hidden" name="id" value="<%= course.getCourseCode() %>" />
                                <button type="submit" class="btn-delete-inline">
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
                                    Delete
                                </button>
                            </form>
                        <% } %>
                    </div>
                </div>
    <% 
            }
        } else {
    %>
        <div style="grid-column: 1 / -1; padding: 60px; text-align: center; border: 1px dashed var(--border-light); border-radius: 16px;">
            <p style="color: var(--text-muted); font-size: 15px;">No assigned curriculum structures found in your active storage profile.</p>
        </div>
    <% } %>
</div>

</body>
</html>
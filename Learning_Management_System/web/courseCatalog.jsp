<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="lms.model.Course" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Google Classroom - Dashboard</title>
    <style>
        :root { --primary-blue: #1a73e8; --border-grey: #dadce0; --text-dark: #3c4043; }
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Roboto', Arial, sans-serif; }
        body { background: #ffffff; color: var(--text-dark); padding: 24px; }
        .nav-container { display: flex; justify-content: space-between; align-items: center; background: #f8f9fa; border: 1px solid var(--border-grey); padding: 16px 32px; border-radius: 8px; margin-bottom: 32px; }
        .join-box input { padding: 10px; border: 1px solid var(--border-grey); border-radius: 4px; width: 220px; font-size: 14px; margin-right: 8px; }
        .btn { background: var(--primary-blue); color: white; border: none; padding: 10px 20px; border-radius: 4px; font-weight: 500; cursor: pointer; text-decoration: none; font-size: 14px; }
        .grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 24px; }
        .card { border: 1px solid var(--border-grey); border-radius: 8px; overflow: hidden; display: flex; flex-direction: column; background: #fff; }
        .card-header { background: #1e8e3e; color: white; padding: 24px; min-height: 110px; position: relative; }
        .card-title { font-size: 22px; font-weight: 400; margin-bottom: 6px; }
        .code-pill { background: rgba(255, 255, 255, 0.2); padding: 3px 8px; border-radius: 4px; font-size: 12px; font-family: monospace; letter-spacing: 1px; }
        .card-body { padding: 24px; flex-grow: 1; font-size: 14px; color: #5f6368; line-height: 1.5; }
        .card-footer { padding: 12px 24px; border-top: 1px solid var(--border-grey); background: #f8f9fa; display: flex; justify-content: flex-end; }
        .view-btn { color: var(--primary-blue); text-decoration: none; font-weight: 500; font-size: 14px; }
    </style>
</head>
<body>

<%
    // Fetch user details from the session variables setup during login
    String userRole = (String) session.getAttribute("userRole");
    String loggedInUserId = (String) session.getAttribute("userId");
    
    // Fallback if session happens to be empty during direct URL testing
    if (loggedInUserId == null) {
        loggedInUserId = "S76237"; 
    }
%>

<div class="nav-container">
    <form action="${pageContext.request.contextPath}/course/enroll" method="POST" class="join-box">
        <%-- FIXED: Changed hardcoded studentId to use the dynamic session value --%>
        <input type="hidden" name="studentId" value="<%= loggedInUserId %>"> 
        <input type="text" name="classCode" placeholder="Enter Class Code (e.g. ox7v2b)" required>
        <button type="submit" class="btn" style="background:#1e8e3e;">Join Class</button>
    </form>
    
    <%-- ROLE CONDITION CHECK: Only render the link button if the user is a lecturer --%>
    <% if ("lecturer".equalsIgnoreCase(userRole)) { %>
        <a href="${pageContext.request.contextPath}/course/create" class="btn">+ Create Class</a>
    <% } %>
</div>

<% if(request.getParameter("error") != null) { %>
    <p style="color: #d93025; font-size: 14px; margin-bottom: 20px; font-weight:500;">⚠️ Code unrecognized. Verification failure.</p>
<% } %>

<div class="grid">
    <% 
        List<Course> list = (List<Course>) request.getAttribute("courses");
        if (list != null && !list.isEmpty()) {
            for (Course c : list) {
    %>
            <div class="card">
                <div class="card-header">
                    <div class="card-title"><%= c.getTitle() %></div>
                    <span class="code-pill">Code: <%= c.getCourseCode() %></span>
                </div>
                <div class="card-body">
                    <%-- Added a null pointer safety check for descriptions --%>
                    <p>
                        <%= (c.getDescription() != null && c.getDescription().length() > 140) ? 
                            c.getDescription().substring(0, 140) + "..." : c.getDescription() %>
                    </p>
                </div>
                <div class="card-footer">
                    <a href="${pageContext.request.contextPath}/course/view?id=<%= c.getCourseCode() %>" class="view-btn">Open Class Stream →</a>
                </div>
            </div>
    <% 
            }
        } else {
    %>
        <p style="color: #5f6368;">No active classrooms found in your storage collection backend.</p>
    <% } %>
</div>

</body>
</html>
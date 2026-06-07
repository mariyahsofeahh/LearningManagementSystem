<%-- 
    Document   : editCourse
    Created on : 3 Jun 2026, 11:55:32 pm
    Author     : Mariyah
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lms.model.Course" %>
<% Course course = (Course) request.getAttribute("course"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LMS - Modify Class Settings</title>
    <style>
        :root { --primary: #1a73e8; --border-color: #dadce0; --text-main: #3c4043; }
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Roboto', Arial, sans-serif; }
        body { background-color: #f8f9fa; display: flex; justify-content: center; align-items: center; min-height: 100vh; }
        .form-card { background: white; border: 1px solid var(--border-color); border-radius: 8px; width: 100%; max-width: 520px; padding: 32px; box-shadow: 0 2px 6px rgba(0,0,0,0.15); }
        h1 { font-size: 24px; margin-bottom: 20px; font-weight: 400; }
        .form-group { margin-bottom: 20px; }
        label { display: block; font-size: 14px; font-weight: 500; margin-bottom: 8px; }
        input[type="text"], textarea { width: 100%; padding: 12px; border: 1px solid var(--border-color); border-radius: 4px; font-size: 14px; background: #f1f3f4; }
        .action-row { display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px; }
        .btn { padding: 10px 24px; font-size: 14px; font-weight: 500; border-radius: 4px; cursor: pointer; border: none; }
        .btn.cancel { background: transparent; color: #5f6368; }
        .btn.submit { background: #1e8e3e; color: white; }
    </style>
</head>
<body>

<div class="form-card">
    <h1>Edit Class Settings</h1>
    
    <form action="${pageContext.request.contextPath}/course/update" method="POST">
        <%-- FIXED: Changed 'name' to classCode and 'value' to getCourseCode() to sync with your servlet and MongoDB --%>
        <input type="hidden" name="classCode" value="<%= course.getCourseCode() %>">
        
        <div class="form-group">
            <label>Update Class Title</label>
            <input type="text" name="title" value="<%= course.getTitle() %>" required>
        </div>
        <div class="form-group">
            <label>Modify Syllabus Layout / Meta Description</label>
            <textarea name="description" rows="6" required><%= course.getDescription() %></textarea>
        </div>
        <div class="action-row">
            <%-- FIXED: Changed link parameter to id=course.getCourseCode() so the cancel button safely routes back to your dashboard view --%>
            <button type="button" class="btn cancel" onclick="window.location.href='${pageContext.request.contextPath}/course/view?id=<%= course.getCourseCode() %>'">Cancel</button>
            <button type="submit" class="btn submit">Save Details</button>
        </div>
    </form>
</div>

</body>
</html>

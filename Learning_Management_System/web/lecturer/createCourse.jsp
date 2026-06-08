<%-- 
    Document   : createCourse
    Created on : 3 Jun 2026, 11:55:00 pm
    Author     : Mariyah
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LMS - Setup New Stream Class</title>
    <style>
        :root { --primary: #1a73e8; --border-color: #dadce0; --text-main: #3c4043; }
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Roboto', Arial, sans-serif; }
        body { background-color: #f8f9fa; display: flex; justify-content: center; align-items: center; min-height: 100vh; }
        .form-card { background: white; border: 1px solid var(--border-color); border-radius: 8px; width: 100%; max-width: 520px; padding: 32px; box-shadow: 0 2px 6px rgba(0,0,0,0.15); }
        h1 { font-size: 24px; margin-bottom: 8px; font-weight: 400; }
        .form-group { margin-bottom: 20px; }
        label { display: block; font-size: 14px; font-weight: 500; margin-bottom: 8px; }
        input[type="text"], textarea { width: 100%; padding: 12px; border: 1px solid var(--border-color); border-radius: 4px; font-size: 14px; background: #f1f3f4; }
        .action-row { display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px; }
        .btn { padding: 10px 24px; font-size: 14px; font-weight: 500; border-radius: 4px; cursor: pointer; border: none; }
        .btn.cancel { background: transparent; color: #5f6368; }
        .btn.submit { background: var(--primary); color: white; }
    </style>
</head>
<body>

<div class="form-card">
    <h1>Create Class</h1>
    <p style="color:#5f6368; font-size:14px; margin-bottom:20px;">Deploy new active stream profile mappings onto the core data cluster tier.</p>
    
    <form action="${pageContext.request.contextPath}/course/create" method="POST">
        <div class="mb-3">
            <label>Course Code:</label>
            <input type="text" name="courseCode" class="form-control" placeholder="e.g., CSF3043" required>
        </div>
        <div class="mb-3">
            <label>Course Title:</label>
            <input type="text" name="title" class="form-control" placeholder="e.g., Object Oriented Programming" required>
        </div>
        <div class="mb-3">
            <label>Description:</label>
            <textarea name="description" class="form-control" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Create Course</button>
    </form>
</div>
                 
</body>
</html>

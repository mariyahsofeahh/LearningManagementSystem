<%-- 
    Document   : createAssignment
    Created on : 3 Jun 2026, 6:24:53?pm
    Author     : ASUS
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Create Assignment - eduSphere</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light p-5">
        <div class="container card shadow-sm p-4 rounded-4" style="max-width: 600px; margin: 0 auto;">
            <h2 class="fw-bold mb-1">Create New Assignment</h2>
            <p class="text-muted small mb-4">Deploy a new assessment task structure to the active class stream node.</p>

            <form action="${pageContext.request.contextPath}/assignment/create" method="POST">
                <input type="hidden" name="courseCode" value="<%= request.getParameter("courseCode")%>">

                <div class="mb-3">
                    <label class="form-label fw-semibold">Assignment Title</label>
                    <input type="text" name="title" class="form-control" placeholder="e.g., Lab Exercise 2: MVC Setup" required autocomplete="off">
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold">Instructions / Description</label>
                    <textarea name="description" class="form-control" rows="5" placeholder="Provide assignment instructions here..." required></textarea>
                </div>

                <div class="mb-4">
                    <label class="form-label fw-semibold">Due Date (Deadline)</label>
                    <input type="date" name="deadline" class="form-control" required>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2.5">Publish Task to Class Stream</button>
                <a href="${pageContext.request.contextPath}/DashboardServlet?courseId=<%= request.getParameter("courseCode")%>" class="btn btn-link w-100 text-center text-decoration-none mt-2">Cancel</a>
            </form>
        </div>
    </body>
</html>
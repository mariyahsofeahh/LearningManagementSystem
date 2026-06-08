<%-- 
    Document   : createAssignment
    Created on : 3 Jun 2026, 6:24:53?pm
    Author     : ASUS
--%>

<form action="${pageContext.request.contextPath}/assignment/create"
      method="POST"
      enctype="multipart/form-data">

    <h2>Create Assignment</h2>

    Course Code:
    <input type="text" name="courseCode" required>

    Title:
    <input type="text" name="title" required>

    Description:
    <textarea name="description"></textarea>

    Deadline:
    <input type="datetime-local" name="deadline" required>

    Upload File:
    <input type="file" name="file" required>

    <button type="submit">Create</button>
</form>
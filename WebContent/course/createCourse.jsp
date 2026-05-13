<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>LMS - Create New Course</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/style.css">
</head>
<body class="bg-light">
    <jsp:include page="../includes/header.jsp" />

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow border-0">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">Add New Course (Lecturer Portal)</h4>
                    </div>
                    <div class="card-body p-4">
                        <form action="CourseController" method="POST">
                            <div class="mb-3">
                                <label class="form-label">Course Title</label>
                                <input type="text" name="courseTitle" class="form-control" placeholder="e.g., Software Architecture" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Course Category</label>
                                <select class="form-select" name="category">
                                    <option value="CS">Computer Science</option>
                                    <option value="SE">Software Engineering</option>
                                    <option value="IT">Information Technology</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Course Description</label>
                                <textarea name="description" class="form-control" rows="4" placeholder="Briefly describe the syllabus..."></textarea>
                            </div>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">Create Course</button>
                                <a href="viewCourses.jsp" class="btn btn-outline-secondary">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../includes/footer.jsp" />
</body>
</html>
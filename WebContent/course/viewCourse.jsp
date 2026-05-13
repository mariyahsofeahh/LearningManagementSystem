<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>LMS - Available Courses</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/style.css">
</head>
<body class="bg-light">
    <jsp:include page="../includes/header.jsp" />

    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Explore Courses</h2>
            <div class="input-group w-25">
                <input type="text" class="form-control" placeholder="Search courses...">
                <button class="btn btn-outline-primary" type="button">Search</button>
            </div>
        </div>

        <div class="row">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-body">
                        <span class="badge bg-info mb-2 text-dark">CSF 3043</span>
                        <h5 class="card-title">Object Oriented Programming</h5>
                        <p class="card-text text-muted">Learn advanced Java concepts including inheritance, polymorphism, and design patterns.</p>
                    </div>
                    <div class="card-footer bg-transparent border-0 pb-3">
                        <button class="btn btn-success w-100" onclick="alert('Enrolled Successfully!')">Enroll Now</button>
                    </div>
                </div>
            </div>

            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm border-0">
                    <div class="card-body">
                        <span class="badge bg-warning mb-2 text-dark">CSE 3433</span>
                        <h5 class="card-title">Software Architecture</h5>
                        <p class="card-text text-muted">Focusing on Cloud-Native and Layered Architecture implementation in modern LMS.</p>
                    </div>
                    <div class="card-footer bg-transparent border-0 pb-3">
                        <button class="btn btn-success w-100" onclick="alert('Enrolled Successfully!')">Enroll Now</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="../includes/footer.jsp" />
</body>
</html>
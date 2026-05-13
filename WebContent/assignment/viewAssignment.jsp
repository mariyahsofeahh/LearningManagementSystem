<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LMS Assignment Module</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        body {
            background-color: #f4f6f9;
            font-family: Arial, sans-serif;
        }

        .sidebar {
            height: 100vh;
            background-color: #1e293b;
            color: white;
            padding-top: 20px;
        }

        .sidebar h3 {
            text-align: center;
            margin-bottom: 30px;
            font-weight: bold;
        }

        .sidebar a {
            display: block;
            color: white;
            padding: 12px 20px;
            text-decoration: none;
            transition: 0.3s;
        }

        .sidebar a:hover {
            background-color: #334155;
        }

        .topbar {
            background-color: white;
            padding: 15px 25px;
            border-bottom: 1px solid #ddd;
        }

        .card-box {
            border: none;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }

        .assignment-status {
            font-size: 14px;
            padding: 5px 12px;
            border-radius: 20px;
            color: white;
        }

        .submitted {
            background-color: #198754;
        }

        .pending {
            background-color: #dc3545;
        }

        .upload-box {
            border: 2px dashed #ccc;
            padding: 40px;
            text-align: center;
            border-radius: 10px;
            background-color: white;
        }

        .btn-upload {
            margin-top: 15px;
        }
    </style>
</head>

<body>

<div class="container-fluid">
    <div class="row">

        <!-- Sidebar -->
        <div class="col-md-2 sidebar">
            <h3>LMS</h3>

            <a href="#"><i class="bi bi-house-door"></i> Dashboard</a>
            <a href="#"><i class="bi bi-book"></i> Courses</a>
            <a href="#"><i class="bi bi-file-earmark-text"></i> Learning Materials</a>
            <a href="#"><i class="bi bi-journal-check"></i> Assignments</a>
            <a href="#"><i class="bi bi-person-circle"></i> Profile</a>
            <a href="#"><i class="bi bi-box-arrow-right"></i> Logout</a>
        </div>

        <!-- Main Content -->
        <div class="col-md-10 p-0">

            <!-- Topbar -->
            <div class="topbar d-flex justify-content-between align-items-center">
                <h4>Assignment Module</h4>
                <span>Welcome, Student</span>
            </div>

            <!-- Content -->
            <div class="p-4">

                <!-- Assignment Summary -->
                <div class="row mb-4">

                    <div class="col-md-4">
                        <div class="card card-box p-3">
                            <h5>Total Assignments</h5>
                            <h2>8</h2>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card card-box p-3">
                            <h5>Submitted</h5>
                            <h2>5</h2>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card card-box p-3">
                            <h5>Pending</h5>
                            <h2>3</h2>
                        </div>
                    </div>

                </div>

                <!-- Assignment Table -->
                <div class="card card-box p-4 mb-4">

                    <h5 class="mb-3">Assignment List</h5>

                    <table class="table table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>Assignment</th>
                                <th>Course</th>
                                <th>Due Date</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            <tr>
                                <td>Database Design Report</td>
                                <td>Database System</td>
                                <td>20 May 2026</td>
                                <td>
                                    <span class="assignment-status submitted">
                                        Submitted
                                    </span>
                                </td>
                                <td>
                                    <button class="btn btn-success btn-sm">
                                        View
                                    </button>
                                </td>
                            </tr>

                            <tr>
                                <td>Java GUI Project</td>
                                <td>Object Oriented Programming</td>
                                <td>25 May 2026</td>
                                <td>
                                    <span class="assignment-status pending">
                                        Pending
                                    </span>
                                </td>
                                <td>
                                    <button class="btn btn-primary btn-sm">
                                        Submit
                                    </button>
                                </td>
                            </tr>

                            <tr>
                                <td>Network Configuration Lab</td>
                                <td>Computer Network</td>
                                <td>28 May 2026</td>
                                <td>
                                    <span class="assignment-status pending">
                                        Pending
                                    </span>
                                </td>
                                <td>
                                    <button class="btn btn-primary btn-sm">
                                        Submit
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                </div>

                <!-- Upload Assignment -->
                <div class="card card-box p-4">

                    <h5 class="mb-3">Upload Assignment</h5>

                    <div class="upload-box">
                        <i class="bi bi-cloud-arrow-up fs-1"></i>

                        <p class="mt-3">
                            Drag & Drop your file here
                        </p>

                        <input type="file" class="form-control">

                        <button class="btn btn-primary btn-upload">
                            Upload Assignment
                        </button>
                    </div>

                </div>

            </div>

        </div>
    </div>
</div>

</body>
</html>
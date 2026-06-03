<%-- 
    Document   : createAssignment
    Created on : 3 Jun 2026, 6:24:53?pm
    Author     : ASUS
--%>

<%@page contentType="text/html"%>

<!DOCTYPE html>
<html>

    <head>

        <title>Create Assignment</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/style.css">

    </head>

    <body>

        <div class="container mt-5">

            <div class="card shadow">

                <div class="card-body">

                    <h2 class="page-title">
                        Create Assignment
                    </h2>

                    <form action="${pageContext.request.contextPath}/assignment/create"
                          method="post">

                        <div class="mb-3">

                            <label>Course ID</label>

                            <input type="text"
                                   class="form-control"
                                   name="courseId"
                                   required>

                        </div>

                        <div class="mb-3">

                            <label>Lecturer ID</label>

                            <input type="number"
                                   class="form-control"
                                   name="lecturerId"
                                   required>

                        </div>

                        <div class="mb-3">

                            <label>Title</label>

                            <input type="text"
                                   class="form-control"
                                   name="title">

                        </div>

                        <div class="mb-3">

                            <label>Description</label>

                            <textarea class="form-control"
                                      name="description">
                            </textarea>

                        </div>

                        <div class="mb-3">

                            <label>Deadline</label>

                            <input type="datetime-local"
                                   class="form-control"
                                   name="deadline">

                        </div>

                        <button class="btn btn-primary">

                            Create Assignment

                        </button>

                    </form>

                </div>

            </div>

        </div>

    </body>

</html>
<%-- 
    Document   : assignmentLIst
    Created on : 4 Jun 2026, 9:25:02 am
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Assignment"%>

<!DOCTYPE html>
<html>
    <head>

        <title>Assignments</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet">

    </head>

    <body class="bg-light">

        <div class="container mt-5">

            <!-- Page Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">

                <h2>Assignments</h2>

                <a href="${pageContext.request.contextPath}/assignment"
                   class="btn btn-primary">
                    + Create Assignment
                </a>

            </div>

            <%
                List<Assignment> assignments
                        = (List<Assignment>) request.getAttribute("assignments");
            %>

            <% if (assignments == null || assignments.isEmpty()) { %>

            <div class="alert alert-info">
                No assignments available.
            </div>

            <% } else { %>

            <div class="row">

                <% for (Assignment a : assignments) {%>

                <div class="col-md-6 mb-4">

                    <div class="card shadow-sm">

                        <div class="card-body">

                            <h5 class="card-title">
                                <%= a.getTitle()%>
                            </h5>

                            <p class="card-text">
                                <%= a.getDescription()%>
                            </p>

                            <p class="text-muted">
                                Deadline:
                                <%= a.getDeadline()%>
                            </p>

                            <div class="d-flex gap-2">

                                <a href="#"
                                   class="btn btn-outline-primary btn-sm">
                                    View
                                </a>

                                <a href="#"
                                   class="btn btn-outline-success btn-sm">
                                    Submissions
                                </a>

                                <a href="#"
                                   class="btn btn-outline-danger btn-sm">
                                    Delete
                                </a>

                            </div>

                        </div>

                    </div>

                </div>

                <% } %>

            </div>

            <% }%>

        </div>

    </body>
</html>

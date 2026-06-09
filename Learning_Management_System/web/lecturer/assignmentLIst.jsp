<%-- 
    Document   : assignmentLIst
    Created on : 4 Jun 2026, 9:25:02?am
    Author     : ASUS
--%>

<<%@page import="java.util.List"%>
<%@page import="lms.model.Assignment"%>

<!DOCTYPE html>
<html>
    <head>

        <title>Assignment List</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>

            body{
                font-family:'Inter',sans-serif;

                background: linear-gradient(
                    -45deg,
                    #fcefe9,
                    #fbf0f5,
                    #eef2f3,
                    #e3f2fd
                    );

                background-size:400% 400%;
                animation:fluidGradient 15s ease infinite;
            }

            @keyframes fluidGradient{
                0%{
                    background-position:0% 50%;
                }
                50%{
                    background-position:100% 50%;
                }
                100%{
                    background-position:0% 50%;
                }
            }

            .container-card{

                margin-top:50px;

                background:rgba(255,255,255,0.8);

                backdrop-filter:blur(20px);

                border-radius:24px;

                padding:30px;
            }

        </style>

    </head>

    <body>

        <div class="container">

            <div class="container-card">

                <h2 class="mb-4">
                    My Assignments
                </h2>

                <table class="table table-hover">

                    <thead>

                        <tr>
                            <th>Course Code</th>
                            <th>Title</th>
                            <th>Deadline</th>
                        </tr>

                    </thead>

                    <tbody>

                        <%

                            List<Assignment> assignments
                                    = (List<Assignment>) request.getAttribute("assignments");

                            if (assignments != null) {

                                for (Assignment a : assignments) {

                        %>

                        <tr>

                            <td><%= a.getCourseCode()%></td>

                            <td><%= a.getTitle()%></td>

                            <td><%= a.getDeadline()%></td>

                        </tr>

                        <%
                                }
                            }
                        %>

                    </tbody>

                </table>

            </div>

        </div>

    </body>
</html>
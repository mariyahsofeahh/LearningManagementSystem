<%-- 
    Document   : createAssignment
    Created on : 3 Jun 2026, 6:24:53?pm
    Author     : ASUS
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
    <head>
        <title>Create Assignment</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Bootstrap Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

        <style>

            body {
                font-family: 'Inter', sans-serif;
                color: #101828;
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;

                background: linear-gradient(
                    -45deg,
                    #fcefe9,
                    #fbf0f5,
                    #eef2f3,
                    #e3f2fd
                    );

                background-size: 400% 400%;
                animation: fluidGradient 15s ease infinite;
            }

            @keyframes fluidGradient {
                0%   {
                    background-position: 0% 50%;
                }
                50%  {
                    background-position: 100% 50%;
                }
                100% {
                    background-position: 0% 50%;
                }
            }

            .glass-card {
                width: 700px;

                background: rgba(255,255,255,0.75);

                backdrop-filter: blur(20px);
                -webkit-backdrop-filter: blur(20px);

                border: 1px solid rgba(255,255,255,0.5);

                border-radius: 24px;

                box-shadow:
                    0 20px 40px -10px
                    rgba(16,24,40,0.07);

                padding: 40px;
            }

            .input-group-custom {
                position: relative;
                margin-bottom: 20px;
            }

            .input-group-custom i {
                position: absolute;
                left: 18px;
                top: 50%;
                transform: translateY(-50%);
                color: #667085;
                z-index: 10;
            }

            .input-group-custom input,
            .input-group-custom textarea {

                width: 100%;

                border-radius: 14px;
                border: 1px solid #d0d5dd;

                padding: 12px 16px 12px 50px;

                background: rgba(255,255,255,0.8);
            }

            .input-group-custom textarea {
                height: 120px;
                resize: none;
            }

            .input-group-custom input:focus,
            .input-group-custom textarea:focus {

                outline: none;

                border-color: #444ce7;

                box-shadow:
                    0 0 0 4px
                    rgba(68,76,231,0.12);
            }

            .btn-modern-primary {

                width: 100%;

                background: #444ce7;
                color: white;

                border: none;

                border-radius: 14px;

                padding: 12px;

                font-weight: 600;
            }

            .btn-modern-primary:hover {

                background: #3538cd;

                transform: translateY(-1px);

                transition: .2s;
            }

        </style>
    </head>

    <body>

        <div class="glass-card">

            <h2 class="text-center mb-4">
                Create Assignment
            </h2>

            <form action="${pageContext.request.contextPath}/assignment/create"
                  method="post">

                <!-- Course Code -->

                <div class="input-group-custom">

                    <i class="bi bi-book"></i>

                    <input type="text"
                           name="courseCode"
                           placeholder="Course Code"
                           value="${courseCode}"
                           required>

                </div>

                <!-- Assignment Title -->

                <div class="input-group-custom">

                    <i class="bi bi-pencil-square"></i>

                    <input type="text"
                           name="title"
                           placeholder="Assignment Title"
                           required>

                </div>

                <!-- Description -->

                <div class="input-group-custom">

                    <i class="bi bi-card-text"></i>

                    <textarea
                        name="description"
                        placeholder="Assignment Description">
                    </textarea>

                </div>

                <!-- Deadline -->

                <div class="input-group-custom">

                    <i class="bi bi-calendar-event"></i>

                    <input type="date"
                           name="deadline"
                           required>

                </div>

                <button type="submit"
                        class="btn-modern-primary">

                    Create Assignment

                </button>

            </form>

        </div>

    </body>
</html>

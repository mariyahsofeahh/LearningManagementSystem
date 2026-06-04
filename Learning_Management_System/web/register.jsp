<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - eduSphere</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    
    <style>
        body { 
            font-family: 'Inter', sans-serif; color: #101828; min-vh: 100vh; display: flex; align-items: center;
            background: linear-gradient(-45deg, #e0e4ff, #f3f0ff, #f4fbfd, #eef2f3); background-size: 400% 400%; animation: fluidGradient 15s ease infinite;
        }
        @keyframes fluidGradient { 0% { background-position: 0% 50%; } 50% { background-position: 100% 50%; } 100% { background-position: 0% 50%; } }
        .glass-auth-card { 
            background: rgba(255, 255, 255, 0.75); backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.5); border-radius: 24px; box-shadow: 0 20px 40px -10px rgba(16, 24, 40, 0.07);
        }
        .input-group-custom { position: relative; margin-bottom: 1.25rem; }
        .input-group-custom i { position: absolute; left: 1.2rem; top: 50%; transform: translateY(-50%); color: #667085; z-index: 10; font-size: 1.1rem; }
        .input-group-custom .form-control { border-radius: 14px; border: 1px solid #d0d5dd; padding: 0.75rem 1rem 0.75rem 3rem; background: rgba(255, 255, 255, 0.8); font-size: 0.95rem; transition: all 0.2s ease; }
        .input-group-custom .form-control:focus { background: #ffffff; border-color: #444ce7; box-shadow: 0 0 0 4px rgba(68, 76, 231, 0.12); }
        .btn-modern-primary { background: #444ce7; color: #ffffff; border: none; border-radius: 14px; padding: 0.75rem 1rem; font-weight: 500; transition: all 0.2s; }
        .btn-modern-primary:hover { background: #3538cd; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(68, 76, 231, 0.2); }
        .role-option { display: none; }
        .role-label { border: 1px solid #eaecf0; border-radius: 14px; padding: 14px; cursor: pointer; background: rgba(255, 255, 255, 0.6); transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1); display: block; }
        .role-label:hover { background: #ffffff; border-color: #d0d5dd; }
        .role-option:checked + .role-label { border-color: #444ce7; background: #ffffff; color: #444ce7; font-weight: 600; box-shadow: 0 4px 12px -2px rgba(68, 76, 231, 0.08); }
    </style>
</head>
<body>

    <div class="container my-4">
        <div class="row justify-content-center">
            <div class="col-xl-5 col-lg-6 col-md-8">
                <div class="text-center mb-4">
                    <span class="bg-dark text-white p-2.5 rounded-3 d-inline-flex mb-3 shadow-sm"><i class="bi bi-layers-half fs-4"></i></span>
                    <h2 class="fw-bold tracking-tight mb-1">Create an account</h2>
                    <p class="text-muted small">Access your workspace profile suite seamlessly.</p>
                </div>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger border-0 rounded-4 shadow-sm small py-3 mb-4" role="alert">
                        <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <div class="card glass-auth-card p-4 p-sm-5 border-0">
                    <form action="LoginServlet" method="POST">
                        <input type="hidden" name="action" value="signup">
                        
                        <label class="form-label small fw-semibold text-secondary mb-1">Full Name</label>
                        <div class="input-group-custom">
                            <i class="bi bi-person"></i>
                            <input type="text" name="fullName" class="form-control" placeholder="Alex Mercer" required autocomplete="off">
                        </div>

                        <label class="form-label small fw-semibold text-secondary mb-1">Email Address</label>
                        <div class="input-group-custom">
                            <i class="bi bi-envelope"></i>
                            <input type="email" name="email" class="form-control" placeholder="alex@university.edu" required autocomplete="off">
                        </div>

                        <label class="form-label small fw-semibold text-secondary mb-1">Password</label>
                        <div class="input-group-custom">
                            <i class="bi bi-shield-lock"></i>
                            <input type="password" name="password" class="form-control" placeholder="••••••••" required>
                        </div>

                        <div class="mb-4">
                            <label class="form-label small fw-semibold text-secondary d-block mb-2">Account Allocation Type</label>
                            <div class="row g-3">
                                <div class="col-6">
                                    <input type="radio" name="role" value="student" id="roleStudent" class="role-option" checked>
                                    <label for="roleStudent" class="role-label text-center">
                                        <i class="bi bi-mortarboard fs-4 d-block mb-1 text-secondary"></i> Student Suite
                                    </label>
                                </div>
                                <div class="col-6">
                                    <input type="radio" name="role" value="lecturer" id="roleLecturer" class="role-option">
                                    <label for="roleLecturer" class="role-label text-center">
                                        <i class="bi bi-person-workspace fs-4 d-block mb-1 text-secondary"></i> Faculty Node
                                    </label>
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-modern-primary w-100 mb-4 py-2.5">Generate System Account</button>
                        <p class="text-center small text-muted mb-0">Already registered? <a href="login.jsp" class="text-primary text-decoration-none fw-semibold">Sign In</a></p>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
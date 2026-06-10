package lms.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lms.service.UserService;
import org.bson.Document;

public class loginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("signup".equals(action)) {
                handleSignUp(request, response);
            } else if ("login".equals(action)) {
                handleLogin(request, response);
            } else {
                response.sendRedirect("login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred.");
            if ("signup".equals(action)) {
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }
    }

    private void handleSignUp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (userService.register(name, email, password, role)) {
            response.sendRedirect("login.jsp?regSuccess=true");
        } else {
            request.setAttribute("error", "An account with this email address already exists.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Document user = userService.login(email, password);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getObjectId("_id").toString());
            session.setAttribute("userName", user.getString("full_name"));
            session.setAttribute("userRole", user.getString("role"));
            response.sendRedirect("DashboardServlet");
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

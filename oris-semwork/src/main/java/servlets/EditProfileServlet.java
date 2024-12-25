package servlets;

import models.User;
import services.UserService;
import utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


@WebServlet("/edit-profile")
public class EditProfileServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getConnection();
            userService = new UserService(connection);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize UserService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long userId = (session != null) ? (Long) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Optional<User> optionalUser = userService.findUserById(userId);
        if (!optionalUser.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = optionalUser.get();
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/edit-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long userId = (session != null) ? (Long) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Optional<User> optionalUser = userService.findUserById(userId);
        if (!optionalUser.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || email == null || email.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/edit-profile?error=validation");
            return;
        }

        User user = optionalUser.get();
        user.setUsername(username);
        user.setEmail(email);
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        if (userService.updateUser(user)) {
            response.sendRedirect(request.getContextPath() + "/profile?success=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/edit-profile?error=true");
        }
    }
}
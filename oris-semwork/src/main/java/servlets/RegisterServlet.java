package servlets;

import services.UserService;
import models.User;
import utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = DBConnection.getConnection()) {
            UserService userService = new UserService(connection);

            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String email = req.getParameter("email");

            if (username == null || username.isEmpty() ||
                    password == null || password.isEmpty() ||
                    email == null || email.isEmpty()) {
                req.setAttribute("error", "All fields are required.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            User newUser = new User(null, username, email, password, LocalDateTime.now(), 2L);
            boolean isRegistered = userService.register(newUser);

            if (isRegistered) {
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.setAttribute("error", "Registration failed. Username or email might already exist.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during user registration", e);
            req.setAttribute("error", "An unexpected error occurred. Please try again later.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}
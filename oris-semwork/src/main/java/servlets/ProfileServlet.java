package servlets;

import models.User;
import repositories.UserRepositoryImpl;
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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private UserRepositoryImpl userRepository;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getConnection();
            userRepository = new UserRepositoryImpl(connection);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize UserService", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long userId = (session != null) ? (Long) session.getAttribute("userId") : null;

        if (userId == null || !userRepository.findById(userId).isPresent()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = userRepository.findById(userId).get();
        System.out.println(user);
        request.setAttribute("user", user);
        System.out.println("User object in request: " + request.getAttribute("user"));
        System.out.println("Session ID: " + request.getSession(false).getId());
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}
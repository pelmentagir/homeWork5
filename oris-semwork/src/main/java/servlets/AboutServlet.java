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
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/about")
public class AboutServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            this.userService = new UserService(DBConnection.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Long userId = (Long) session.getAttribute("userId");

            if (userId != null) {
                Optional<User> user = userService.findUserById(userId);
                if (user.isPresent()) {
                    request.setAttribute("user", user.get());
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return;
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("pageTitle", "О нас");

        request.setAttribute("contentPage", "about.jsp");

        request.getRequestDispatcher("/WEB-INF/views/about.jsp").forward(request, response);
    }
}
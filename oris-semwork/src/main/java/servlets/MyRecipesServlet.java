package servlets;


import models.Recipe;
import repositories.RecipeRepositoryImpl;
import services.RecipeService;
import utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/my-recipes")
public class MyRecipesServlet extends HttpServlet {
    private RecipeService recipeService;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            connection = DBConnection.getConnection();
            recipeService = new RecipeService(
                    new RecipeRepositoryImpl(connection),
                    null,
                    null,
                    null
            );
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = (Long) request.getSession().getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<Recipe> recipes = recipeService.getUserRecipes(userId);
            request.setAttribute("recipes", recipes);
            request.getRequestDispatcher("/WEB-INF/views/my-recipes.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Ошибка при получении рецептов", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            handleDeleteRecipe(request, response);
        }
    }

    private void handleDeleteRecipe(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String recipeIdParam = request.getParameter("recipeId");

        if (recipeIdParam == null || recipeIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан ID рецепта");
            return;
        }

        try {
            Long recipeId = Long.parseLong(recipeIdParam);
            recipeService.deleteRecipe(recipeId);

            Long userId = (Long) request.getSession().getAttribute("userId");
            List<Recipe> recipes = recipeService.getUserRecipes(userId);
            request.setAttribute("recipes", recipes);

            response.sendRedirect(request.getContextPath() + "/my-recipes");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат ID рецепта");
        } catch (Exception e) {
            throw new ServletException("Ошибка при удалении рецепта", e);
        }
    }
}
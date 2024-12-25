package servlets;

import models.Category;
import models.Recipe;
import models.User;
import repositories.CategoryRepositoryImpl;
import repositories.IngredientRepositoryImpl;
import repositories.RecipeIngredientRepositoryImpl;
import repositories.RecipeRepositoryImpl;
import services.RecipeService;
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
import java.util.List;
import java.util.Optional;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private RecipeService recipeService;
    private UserService userService;

    @Override
    public void init() {
        try {
            this.recipeService = new RecipeService(
                    new RecipeRepositoryImpl(DBConnection.getConnection()),
                    new RecipeIngredientRepositoryImpl(DBConnection.getConnection()),
                    new IngredientRepositoryImpl(DBConnection.getConnection()),
                    new CategoryRepositoryImpl(DBConnection.getConnection())
            );
            this.userService = new UserService(DBConnection.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Recipe> recipes = recipeService.getAllRecipes();
        List<Recipe> topRecipes = recipeService.getTopRecipes();
        List<Category> categories = recipeService.getAllCategories();

        request.setAttribute("recipes", recipes);
        request.setAttribute("topRecipes", topRecipes);
        request.setAttribute("categories", categories);

        HttpSession session = request.getSession(false);
        Long userId = session != null ? (Long) session.getAttribute("userId") : null;

        User user = null;
        if (userId != null) {
            Optional<User> userOptional = userService.findUserById(userId);
            user = userOptional.orElse(null);
        }

        request.setAttribute("user", user);

        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
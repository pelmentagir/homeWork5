package servlets;

import models.Category;
import models.Ingredient;
import models.Recipe;
import models.RecipeIngredient;
import repositories.CategoryRepositoryImpl;
import repositories.IngredientRepositoryImpl;
import repositories.RecipeIngredientRepositoryImpl;
import repositories.RecipeRepositoryImpl;
import services.RecipeService;
import utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add-recipe")
@MultipartConfig
public class AddRecipeServlet extends HttpServlet {
    private RecipeService recipeService;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            connection = DBConnection.getConnection();
            recipeService = new RecipeService(
                    new RecipeRepositoryImpl(connection),
                    new RecipeIngredientRepositoryImpl(connection),
                    new IngredientRepositoryImpl(connection),
                    new CategoryRepositoryImpl(connection)
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
        List<Category> categories = recipeService.getAllCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/add-recipes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String title = request.getParameter("recipeName");
            String description = request.getParameter("description");
            Long categoryId = Long.valueOf(request.getParameter("categoryId"));
            Long userId = (Long) request.getSession().getAttribute("userId");

            Recipe recipe = new Recipe();
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setCategoryId(categoryId);
            recipe.setUserId(userId);
            recipe.setCreationDate(LocalDateTime.now());

            String[] ingredientNames = request.getParameterValues("ingredientName[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] units = request.getParameterValues("unit[]");

            if (ingredientNames == null || ingredientNames.length == 0) {
                throw new IllegalArgumentException("Recipe must have at least one ingredient.");
            }

            List<RecipeIngredient> recipeIngredients = new ArrayList<>();
            for (int i = 0; i < ingredientNames.length; i++) {
                Ingredient ingredient = recipeService.findOrCreateIngredient(ingredientNames[i]);

                RecipeIngredient recipeIngredient = new RecipeIngredient();
                recipeIngredient.setIngredientId(ingredient.getId());
                recipeIngredient.setQuantity(Double.parseDouble(quantities[i]));
                recipeIngredient.setUnit(units[i]);

                recipeIngredients.add(recipeIngredient);
            }

            recipeService.addRecipe(recipe, recipeIngredients);

            Long recipeId = recipe.getId();

            for (Part filePart : request.getParts()) {
                if ("recipeFiles".equals(filePart.getName()) && filePart.getSize() > 0) {
                    request.setAttribute("recipeId", recipeId);
                    request.setAttribute("file", filePart);
                    request.getRequestDispatcher("/upload-file").forward(request, response);
                }
            }

            response.sendRedirect(request.getContextPath() + "/recipes?id=" + recipeId);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred while adding the recipe: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/add-recipes.jsp").forward(request, response);
        }
    }
}
package servlets;

import models.Recipe;
import models.Review;
import models.User;
import repositories.*;
import services.RecipeService;
import services.ReviewService;
import services.UserService;
import utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/recipeDetail")
public class RecipeDetailServlet extends HttpServlet {
    private RecipeService recipeService;
    private ReviewService reviewService;

    public RecipeDetailServlet() {
        try {
            this.recipeService = new RecipeService(
                    new RecipeRepositoryImpl(DBConnection.getConnection()),
                    new RecipeIngredientRepositoryImpl(DBConnection.getConnection()),
                    new IngredientRepositoryImpl(DBConnection.getConnection()),
                    new CategoryRepositoryImpl(DBConnection.getConnection())
            );
            this.reviewService = new ReviewService(
                    new ReviewRepositoryImpl(DBConnection.getConnection())
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String recipeIdParam = request.getParameter("id");
        if (recipeIdParam == null || recipeIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Recipe ID is required");
            return;
        }

        try {
            Long recipeId = Long.valueOf(recipeIdParam);

            Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
            if (!recipeOpt.isPresent()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Recipe not found");
                return;
            }

            Recipe recipe = recipeOpt.get();
            List<Map<String, Object>> detailedIngredients = recipeService.getDetailedIngredientsByRecipeId(recipeId);

            List<Review> reviews = reviewService.getReviewsByRecipeId(recipeId);
            UserService userService = new UserService(DBConnection.getConnection());

            List<Map<String, Object>> enrichedReviews = reviews.stream().map(review -> {
                Map<String, Object> reviewMap = new HashMap<>();
                reviewMap.put("rating", review.getRating());
                reviewMap.put("text", review.getText());
                reviewMap.put("creationDate", review.getCreationDate());
                reviewMap.put("username", userService.findUserById(review.getUserId())
                        .map(User::getUsername)
                        .orElse("Unknown User"));
                return reviewMap;
            }).collect(Collectors.toList());

            request.setAttribute("recipe", recipe);
            request.setAttribute("ingredients", detailedIngredients);
            request.setAttribute("reviews", enrichedReviews);
            request.setAttribute("averageRating", reviewService.calculateAverageRating(recipeId));

            request.getRequestDispatcher("/WEB-INF/views/recipeDetail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid recipe ID format");
        } catch (Exception e) {
            throw new ServletException("Error retrieving recipe details", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long recipeId = Long.valueOf(request.getParameter("recipeId"));
            Long userId = (Long) request.getSession().getAttribute("userId");
            String text = request.getParameter("text");
            int rating = Integer.parseInt(request.getParameter("rating"));

            Optional<Recipe> recipeOpt = recipeService.getRecipeById(recipeId);
            if (!recipeOpt.isPresent()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot review your own recipe");
                return;
            }

            Review review = new Review(null, recipeId, userId, text, rating, null);
            reviewService.addReview(review);

            response.sendRedirect(request.getContextPath() + "/recipeDetail?id=" + recipeId);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error adding review");
        }
    }
}
<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 22.12.2024
  Time: 11:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>${recipe.title}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/recipe-detail.css">
</head>
<body>
<div class="container">
  <div class="header">
    <button class="back-button" onclick="goBack()">← Back</button>
  </div>
  <div class="content">
    <div class="image-container">
      <img src="https://via.placeholder.com/150" alt="Recipe Image" class="recipe-image">
    </div>
    <div class="details">
      <h1>${recipe.title}</h1>
      <p class="description">${recipe.description}</p>
      <h3>Ingredients:</h3>
      <ul>
        <c:forEach var="ingredient" items="${ingredients}">
          <li class="ingredient-item">
            <span class="ingredient-name">${ingredient["name"]}</span>
            <span class="ingredient-quantity">${ingredient["quantity"]}</span>
            <span class="ingredient-unit">${ingredient["unit"]}</span>
          </li>
        </c:forEach>
      </ul>
    </div>
  </div>
  <div class="reviews">
    <h3>Reviews</h3>
    <p>Average Rating:
      <span class="stars review-stars">
    <c:forEach begin="1" end="5" var="i">
      <span class="star">
        <c:choose>
          <c:when test="${i <= averageRating}">&#9733;</c:when>
          <c:otherwise>&#9734;</c:otherwise>
        </c:choose>
      </span>
    </c:forEach>
  </span>
    </p>
    <ul>
      <c:forEach var="review" items="${reviews}">
        <li>
          <strong>${review.username}:</strong>
          <span class="stars review-stars">
        <c:forEach var="i" begin="1" end="5">
          <span class="star">
            <c:choose>
              <c:when test="${i <= review.rating}">&#9733;</c:when>
              <c:otherwise>&#9734;</c:otherwise>
            </c:choose>
          </span>
        </c:forEach>
      </span>
          <p>${review.text}</p>
          <small>${review.creationDate}</small>
        </li>
      </c:forEach>
    </ul>

    <form id="reviewForm" action="${pageContext.request.contextPath}/recipeDetail" method="post">
      <input type="hidden" name="recipeId" value="${recipe.id}">
      <input type="hidden" name="rating" id="ratingValue">

      <label for="text">Review:</label>
      <textarea name="text" id="text" rows="4" required></textarea>
      <br>

      <div class="star-rating form-stars">
        <span class="star" data-value="5">&#9733;</span>
        <span class="star" data-value="4">&#9733;</span>
        <span class="star" data-value="3">&#9733;</span>
        <span class="star" data-value="2">&#9733;</span>
        <span class="star" data-value="1">&#9733;</span>
      </div>
      <br>
      <button type="submit">Submit Review</button>
    </form>
  </div>
</div>

<script src="${pageContext.request.contextPath}/js/recipe-detail.js"></script>
</body>
</html>
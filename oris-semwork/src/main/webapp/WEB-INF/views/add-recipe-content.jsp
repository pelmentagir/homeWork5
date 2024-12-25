<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 18.12.2024
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="add-recipe-container">
  <h1>Добавить рецепт</h1>
  <form
          class="recipe-form"
          action="${pageContext.request.contextPath}/add-recipe"
          method="post"
          enctype="multipart/form-data"
          accept-charset="UTF-8">

    <div class="form-group">
      <label for="recipeName">Название рецепта:</label>
      <input type="text" id="recipeName" name="recipeName" placeholder="Введите название" required>
    </div>

    <div class="form-group">
      <label for="category">Категория:</label>
      <select id="category" name="categoryId" required>
        <option value="" disabled selected>Выберите категорию</option>
        <c:forEach var="category" items="${categories}">
          <option value="${category.id}">${category.name}</option>
        </c:forEach>
      </select>
    </div>

    <h3>Ингредиенты</h3>
    <div id="ingredients" class="ingredients-container">
      <div class="ingredient form-group">
        <input type="text" name="ingredientName[]" placeholder="Название ингредиента" required>
        <input type="number" name="quantity[]" min="1" step="0.01" placeholder="Количество" required>
        <input type="text" name="unit[]" placeholder="Единица измерения (г, мл, ст.л.)" required>
      </div>
    </div>
    <button type="button" id="addIngredient">Добавить ингредиент</button>

    <div class="form-group">
      <label for="recipeFiles">Фотографии:</label>
      <input type="file" id="recipeFiles" name="recipeFiles" multiple>
    </div>

    <div class="form-group">
      <label for="description">Описание:</label>
      <textarea id="description" name="description" rows="6" placeholder="Опишите процесс приготовления" required></textarea>
    </div>

    <button type="submit" class="btn">Добавить рецепт</button>
  </form>
</div>
<script src="${pageContext.request.contextPath}/js/add-recipe.js"></script>
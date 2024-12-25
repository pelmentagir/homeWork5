<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 17.12.2024
  Time: 22:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="profile-content">
  <div class="white-container">
    <h2>Мои рецепты</h2>

    <c:if test="${not empty recipes}">
      <table class="recipe-table">
        <thead>
        <tr>
          <th>Название</th>
          <th>Описание</th>
          <th>Дата создания</th>
          <th>Действие</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="recipe" items="${recipes}">
          <tr>
            <td>${recipe.title}</td>
            <td class="description">${recipe.description}</td>
            <td>${recipe.creationDate}</td>
            <td>
              <a href="${pageContext.request.contextPath}/recipeDetail?id=${recipe.id}">Посмотреть</a>
              <form action="${pageContext.request.contextPath}/my-recipes" method="post" style="display:inline;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="recipeId" value="${recipe.id}">
                <button type="submit" class="delete-button">Удалить</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:if>

    <c:if test="${empty recipes}">
      <p class="no-recipes">У вас пока нет созданных рецептов.</p>
    </c:if>

    <a href="${pageContext.request.contextPath}/add-recipe" class="add-recipe">Добавить рецепт</a>
  </div>
</div>
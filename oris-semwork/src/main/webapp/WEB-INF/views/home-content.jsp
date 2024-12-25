<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 21.12.2024
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section>
  <h2>Популярные блюда</h2>
  <div class="recipe-grid">
    <c:forEach var="recipe" items="${topRecipes}">
      <div class="recipe-item">
        <img src="${pageContext.request.contextPath}/images/default.jpg" alt="${recipe.title}">
        <h3><a href="/recipe/${recipe.id}">${recipe.title}</a></h3>
      </div>
    </c:forEach>
  </div>
</section>

<section>
  <h2>Коллекция блюд</h2>
  <div class="recipe-grid">
    <c:forEach var="recipe" items="${recipes}">
      <div class="recipe-item">
        <img src="${pageContext.request.contextPath}/images/default.jpg" alt="${recipe.title}">
        <h3><a href="/recipe/${recipe.id}">${recipe.title}</a></h3>
      </div>
    </c:forEach>
  </div>
</section>

<section>
  <h2>Категории</h2>
  <ul>
    <c:forEach var="category" items="${categories}">
      <li><a href="/recipes?category=${category.id}">${category.name}</a></li>
    </c:forEach>
  </ul>
</section>


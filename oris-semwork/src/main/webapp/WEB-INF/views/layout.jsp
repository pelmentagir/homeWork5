<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 17.12.2024
  Time: 22:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><c:out value="${param.title}" /></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body>
<header>
  <h1>Сайт с рецептами</h1>
  <nav>
    <ul class="nav-list">
      <li><a href="${pageContext.request.contextPath}/home">Главная</a></li>
      <li class="category-item">
        <a href="/categories">Категории</a>
        <div class="subcategories">
          <ul>
            <li><a href="/recipes?category=soups">Супы</a></li>
            <li><a href="/recipes?category=desserts">Десерты</a></li>
            <li><a href="/recipes?category=salads">Салаты</a></li>
          </ul>
        </div>
      </li>
      <li><a href="${pageContext.request.contextPath}/about">О нас</a></li>
      <li><a href="${pageContext.request.contextPath}/profile">Профиль</a></li>
    </ul>
  </nav>
</header>

<div class="profile-container">

  <aside class="sidebar">
    <ul>
      <li><a href="${pageContext.request.contextPath}/profile">Профиль</a></li>
      <li><a href="${pageContext.request.contextPath}/my-recipes">Мои рецепты</a></li>
      <li><a href="${pageContext.request.contextPath}/add-recipe">Добавить рецепт</a></li>
      <li><a href="${pageContext.request.contextPath}/logout">Выйти</a></li>
    </ul>
  </aside>

  <section class="profile-content">
    <c:choose>
      <c:when test="${not empty param.contentPage}">
        <jsp:include page="${param.contentPage}" />
      </c:when>
      <c:otherwise>
        <p>Контент не найден</p>
      </c:otherwise>
    </c:choose>
  </section>
</div>

<footer>
  <p>© 2024 Сайт с рецептами. Все права защищены.</p>
</footer>
</body>
</html>
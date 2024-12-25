<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 21.12.2024
  Time: 18:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><c:out value="${pageTitle}"/></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<header>
  <h1>Сайт с рецептами</h1>
  <nav>
    <ul class="nav-list">
      <li><a href="/home" class="active">Главная</a></li>
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
      <c:choose>
        <c:when test="${not empty user}">
          <li><a href="${pageContext.request.contextPath}/profile">Профиль</a></li>
        </c:when>
        <c:otherwise>
          <li><a href="${pageContext.request.contextPath}/login">Войти</a></li>
        </c:otherwise>
      </c:choose>
    </ul>
  </nav>
</header>

<main>
  <jsp:include page="${param.contentPage}" />
</main>

<footer>
  <p>© 2024 Рецепты. Все права защищены.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/home.js"></script>
</body>
</html>

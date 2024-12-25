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
    <h2>Добро пожаловать, <c:out value="${user.username}" />!</h2>

    <ul class="profile-details">
      <li><strong>Имя:</strong> <c:out value="${user.username}" /></li>
      <li><strong>Email:</strong> <c:out value="${user.email}" /></li>
      <li><strong>Дата регистрации:</strong> <c:out value="${user.registrationDate}" /></li>
      <li><strong>Роль:</strong> <c:out value="${user.roleId}" /></li>
    </ul>

    <form action="${pageContext.request.contextPath}/edit-profile" method="get">
      <button type="submit">Редактировать</button>
    </form>
  </div>
</div>
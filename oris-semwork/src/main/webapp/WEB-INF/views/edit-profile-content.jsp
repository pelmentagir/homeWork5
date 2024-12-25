<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 19.12.2024
  Time: 20:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="edit-profile-form">
    <h2>Редактировать профиль</h2>

    <c:if test="${param.error == 'validation'}">
        <p class="error-message">Все поля должны быть заполнены!</p>
    </c:if>
    <c:if test="${param.error == 'true'}">
        <p class="error-message">Не удалось сохранить изменения. Попробуйте снова.</p>
    </c:if>
    <c:if test="${param.success == 'true'}">
        <p class="success-message">Профиль успешно обновлен!</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/edit-profile" method="post">
        <div class="form-group">
            <label for="username">Имя:</label>
            <input type="text" id="username" name="username" value="<c:out value='${user.username}' />" required>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" value="<c:out value='${user.email}' />" required>
        </div>
        <div class="form-group">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" value="<c:out value='${user.password}' />" required>
            <button type="button" onclick="togglePasswordVisibility()">Показать пароль</button>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Сохранить изменения</button>
            <a href="${pageContext.request.contextPath}/profile" class="btn cancel-button">Отмена</a>
        </div>
    </form>
</div>
<script src="${pageContext.request.contextPath}/js/profile.js"></script>
<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 13.12.2024
  Time: 14:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div class="container">
    <div class="form-container">
        <h1>Login</h1>
        <form action="login" method="post">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Login</button>
        </form>
        <p><%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %></p>
    </div>
    <div class="register-container">
        <div class="register-text">
            <p>Don't have an account?</p>
            <a href="register">Register here</a>
        </div>
    </div>
</div>
</body>
</html>
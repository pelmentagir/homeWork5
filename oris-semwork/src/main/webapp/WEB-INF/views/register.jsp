<%--
  Created by IntelliJ IDEA.
  User: tagirfajrusin
  Date: 13.12.2024
  Time: 15:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Register</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
<div class="register-page">
  <div class="register-container">
    <h1>Register</h1>
    <form action="register" method="post" class="register-form">
      <input type="text" name="username" placeholder="Username" required>
      <input type="password" name="password" placeholder="Password" required>
      <input type="email" name="email" placeholder="Email" required>
      <button type="submit">Register</button>
    </form>
    <p class="error-message">
      <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
    </p>
    <div class="login-redirect">
      <p>Already have an account? <a href="login">Login here</a>.</p>
    </div>
  </div>
</div>
</body>
</html>
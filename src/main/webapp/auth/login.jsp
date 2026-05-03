<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">
                <span class="brand-icon">&#9881;</span> GadgetZone
            </a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
            </ul>
        </div>
    </nav>

    <div class="auth-wrapper">
        <div class="auth-card">
            <h1>Welcome Back</h1>
            <p class="auth-subtitle">Sign in to your GadgetZone account</p>

            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="login">
                <div class="form-group">
                    <label class="form-label">Username</label>
                    <input type="text" name="username" class="form-control" value="${username}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" class="form-control" required>
                </div>
                <div class="form-group">
                    <label>
                        <input type="checkbox" name="remember"> Remember me
                    </label>
                </div>
                <button type="submit" class="btn btn-primary btn-block btn-lg">Sign In</button>
            </form>

            <div class="auth-footer">
                Don't have an account? <a href="${pageContext.request.contextPath}/auth?action=register">Register here</a>
            </div>
        </div>
    </div>

    <footer class="footer">
        <div class="container">
            <p>&copy; 2026 GadgetZone. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>

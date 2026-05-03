<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - GadgetZone</title>
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
            <h1>Create Account</h1>
            <p class="auth-subtitle">Join GadgetZone and start shopping</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="register">
                <div class="form-group">
                    <label class="form-label">Username *</label>
                    <input type="text" name="username" class="form-control" value="${regUsername}" required minlength="3" maxlength="50" pattern="[A-Za-z0-9_]+">
                    <span class="form-text">Letters, numbers, underscores only (3-50 chars)</span>
                </div>
                <div class="form-group">
                    <label class="form-label">Email *</label>
                    <input type="email" name="email" class="form-control" value="${regEmail}" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Full Name *</label>
                    <input type="text" name="fullName" class="form-control" value="${regFullName}" required pattern="[A-Za-z\s]+">
                    <span class="form-text">Letters and spaces only</span>
                </div>
                <div class="form-group">
                    <label class="form-label">Phone Number *</label>
                    <input type="tel" name="phone" class="form-control" value="${regPhone}" required pattern="[0-9]{10,15}">
                    <span class="form-text">10-15 digits</span>
                </div>
                <div class="form-group">
                    <label class="form-label">Password *</label>
                    <input type="password" name="password" class="form-control" required minlength="6">
                    <span class="form-text">Minimum 6 characters</span>
                </div>
                <div class="form-group">
                    <label class="form-label">Confirm Password *</label>
                    <input type="password" name="confirmPassword" class="form-control" required minlength="6">
                </div>
                <div class="form-group">
                    <label class="form-label">Address</label>
                    <textarea name="address" class="form-control" rows="2">${regAddress}</textarea>
                </div>
                <button type="submit" class="btn btn-primary btn-block btn-lg">Create Account</button>
            </form>

            <div class="auth-footer">
                Already have an account? <a href="${pageContext.request.contextPath}/auth?action=login">Sign in</a>
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

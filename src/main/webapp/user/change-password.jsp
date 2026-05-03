<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password - GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/profile">${sessionScope.userName}</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container" style="max-width:500px">
            <h1 class="mb-2">Change Password</h1>

            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="changePassword">
                        <div class="form-group">
                            <label class="form-label">Current Password *</label>
                            <input type="password" name="currentPassword" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">New Password *</label>
                            <input type="password" name="newPassword" class="form-control" required minlength="6">
                            <span class="form-text">Minimum 6 characters</span>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Confirm New Password *</label>
                            <input type="password" name="confirmPassword" class="form-control" required minlength="6">
                        </div>
                        <div class="d-flex gap-2 mt-2">
                            <button type="submit" class="btn btn-primary">Change Password</button>
                            <a href="${pageContext.request.contextPath}/profile?action=view" class="btn btn-outline">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="container"><p>&copy; 2026 GadgetZone.</p></div>
    </footer>
</body>
</html>

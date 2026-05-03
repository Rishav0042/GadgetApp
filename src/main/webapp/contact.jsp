<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <button class="navbar-toggle" onclick="toggleNav()">&#9776;</button>
            <ul class="navbar-nav" id="navbarNav">
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/about.jsp">About</a></li>
                <li><a href="${pageContext.request.contextPath}/contact" class="active">Contact</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li><a href="${pageContext.request.contextPath}/profile" class="nav-user">&#128100; ${sessionScope.userName}</a></li>
                        <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/auth?action=login">Login</a></li>
                        <li><a href="${pageContext.request.contextPath}/auth?action=register">Register</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container" style="max-width:700px">
            <h1 class="mb-2">Contact Us</h1>

            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>

            <div class="card mb-3">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/contact" method="post">
                        <div class="form-group">
                            <label class="form-label">Your Name *</label>
                            <input type="text" name="name" class="form-control" value="${contactName}" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Email Address *</label>
                            <input type="email" name="email" class="form-control" value="${contactEmail}" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Subject *</label>
                            <input type="text" name="subject" class="form-control" value="${contactSubject}" required>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Message *</label>
                            <textarea name="message" class="form-control" rows="5" required>${contactMessage}</textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-lg">Send Message</button>
                    </form>
                </div>
            </div>

            <div class="card">
                <div class="card-body">
                    <h3>Other Ways to Reach Us</h3>
                    <div class="mt-1">
                        <p><strong>&#128231; Email:</strong> support@gadgetzone.com</p>
                        <p><strong>&#128222; Phone:</strong> +977-1-4567890</p>
                        <p><strong>&#128205; Address:</strong> Pokhara, Nepal</p>
                        <p><strong>&#128337; Hours:</strong> Sunday - Friday, 10:00 AM - 6:00 PM</p>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="container">
            <p>&copy; 2026 GadgetZone. All rights reserved.</p>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/about.jsp">About</a></li>
                <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
            </ul>
        </div>
    </footer>

    <script>function toggleNav(){document.getElementById('navbarNav').classList.toggle('show');}</script>
</body>
</html>

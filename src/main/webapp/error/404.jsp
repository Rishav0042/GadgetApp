<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Page Not Found | GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container text-center" style="padding:4rem 1rem">
            <h1 style="font-size:4rem;color:var(--text-lighter)">404</h1>
            <h2>Page Not Found</h2>
            <p class="text-muted mt-1 mb-2">The page you are looking for does not exist or has been moved.</p>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">Go to Homepage</a>
        </div>
    </main>

    <footer class="footer">
        <div class="container"><p>&copy; 2026 GadgetZone.</p></div>
    </footer>
</body>
</html>

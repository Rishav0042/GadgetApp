<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <button class="navbar-toggle" onclick="toggleNav()">&#9776;</button>
            <ul class="navbar-nav" id="navbarNav">
                <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products" class="active">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/about.jsp">About</a></li>
                <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:if test="${sessionScope.userRole == 'admin'}">
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                        </c:if>
                        <li><a href="${pageContext.request.contextPath}/orders?action=cart">Cart</a></li>
                        <li><a href="${pageContext.request.contextPath}/wishlist?action=list">Wishlist</a></li>
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
        <div class="container">
            <h1 class="mb-2">${not empty currentCategory ? currentCategory.name : not empty keyword ? 'Search Results' : 'All Products'}</h1>

            <!-- Search -->
            <form action="${pageContext.request.contextPath}/products" method="get" class="search-bar">
                <input type="hidden" name="action" value="search">
                <input type="text" name="keyword" placeholder="Search by name, brand..." value="${keyword}">
                <button type="submit">Search</button>
            </form>

            <!-- Category Filter -->
            <div class="category-filter">
                <a href="${pageContext.request.contextPath}/products" class="${empty currentCategory && empty keyword ? 'active' : ''}">All</a>
                <c:forEach var="cat" items="${categories}">
                    <a href="${pageContext.request.contextPath}/products?action=category&id=${cat.id}" class="${not empty currentCategory && currentCategory.id == cat.id ? 'active' : ''}">${cat.name}</a>
                </c:forEach>
            </div>

            <c:if test="${not empty keyword}">
                <p class="mb-2 text-muted">Showing results for: <strong>${keyword}</strong></p>
            </c:if>

            <!-- Product Grid -->
            <c:choose>
                <c:when test="${not empty products}">
                    <div class="product-grid">
                        <c:forEach var="product" items="${products}">
                            <div class="product-card">
                                <div class="product-image">&#128241;</div>
                                <div class="product-info">
                                    <div class="product-name">${product.name}</div>
                                    <div class="product-brand">${product.brand}</div>
                                    <div class="product-price">$<fmt:formatNumber value="${product.price}" pattern="0.00"/></div>
                                    <div class="product-stock ${product.stock > 0 ? 'in-stock' : 'out-of-stock'}">
                                        <c:choose>
                                            <c:when test="${product.stock > 0}">${product.stock} in stock</c:when>
                                            <c:otherwise>Out of stock</c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="product-actions">
                                        <a href="${pageContext.request.contextPath}/products?action=detail&id=${product.id}" class="btn btn-sm btn-primary">View</a>
                                        <c:if test="${product.stock > 0 && not empty sessionScope.user}">
                                            <a href="${pageContext.request.contextPath}/orders?action=addToCart&productId=${product.id}" class="btn btn-sm btn-success">Add to Cart</a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="card">
                        <div class="card-body text-center">
                            <p class="text-muted">No products found.</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <footer class="footer">
        <div class="container">
            <p>&copy; 2026 GadgetZone. All rights reserved.</p>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/about.jsp">About</a></li>
                <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
            </ul>
        </div>
    </footer>

    <script>function toggleNav(){document.getElementById('navbarNav').classList.toggle('show');}</script>
</body>
</html>

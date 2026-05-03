<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Wishlist - GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/orders?action=cart">Cart</a></li>
                <li><a href="${pageContext.request.contextPath}/wishlist?action=list" class="active">Wishlist</a></li>
                <li><a href="${pageContext.request.contextPath}/profile">${sessionScope.userName}</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container">
            <h1 class="mb-2">My Wishlist</h1>

            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success">${sessionScope.success}</div>
                <c:set var="success" value="" scope="session" />
            </c:if>

            <c:choose>
                <c:when test="${not empty wishlistItems}">
                    <c:forEach var="item" items="${wishlistItems}">
                        <div class="cart-item">
                            <div class="item-image">&#128241;</div>
                            <div class="item-info">
                                <div class="item-name">${item.productName}</div>
                                <div class="text-muted">${item.productBrand}</div>
                                <div class="item-price">$<fmt:formatNumber value="${item.productPrice}" pattern="0.00"/></div>
                                <div class="${item.productStock > 0 ? 'text-success' : 'text-danger'}">
                                    <c:choose>
                                        <c:when test="${item.productStock > 0}">${item.productStock} in stock</c:when>
                                        <c:otherwise>Out of stock</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="item-actions">
                                <c:if test="${item.productStock > 0}">
                                    <a href="${pageContext.request.contextPath}/orders?action=addToCart&productId=${item.productId}" class="btn btn-sm btn-success">Add to Cart</a>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/wishlist?action=remove&productId=${item.productId}" class="btn btn-sm btn-danger">Remove</a>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="card">
                        <div class="card-body text-center">
                            <p class="text-muted mb-2">Your wishlist is empty.</p>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Products</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <footer class="footer">
        <div class="container"><p>&copy; 2026 GadgetZone.</p></div>
    </footer>
</body>
</html>

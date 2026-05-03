<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History - GadgetZone</title>
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
        <div class="container">
            <h1 class="mb-2">My Orders</h1>

            <c:choose>
                <c:when test="${not empty orders}">
                    <c:forEach var="order" items="${orders}">
                        <div class="card mb-2">
                            <div class="card-header flex-between">
                                <div>
                                    <strong>Order #${order.id}</strong>
                                    <span class="text-muted"> - ${order.orderDate}</span>
                                </div>
                                <span class="badge badge-${order.status == 'pending' ? 'warning' : order.status == 'delivered' ? 'success' : order.status == 'cancelled' ? 'danger' : 'info'}">${order.status}</span>
                            </div>
                            <div class="card-body flex-between">
                                <span>Total: <strong>$<fmt:formatNumber value="${order.totalAmount}" pattern="0.00"/></strong></span>
                                <a href="${pageContext.request.contextPath}/orders?action=detail&id=${order.id}" class="btn btn-sm btn-outline">View Details</a>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="card">
                        <div class="card-body text-center">
                            <p class="text-muted mb-2">You haven't placed any orders yet.</p>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Start Shopping</a>
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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order #${order.id} - GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/orders?action=history">My Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container">
            <div class="flex-between mb-2">
                <h1>Order #${order.id}</h1>
                <a href="${pageContext.request.contextPath}/orders?action=history" class="btn btn-outline">&larr; My Orders</a>
            </div>

            <div class="card mb-2">
                <div class="card-header flex-between">
                    <h3>Order Details</h3>
                    <span class="badge badge-${order.status == 'pending' ? 'warning' : order.status == 'delivered' ? 'success' : order.status == 'cancelled' ? 'danger' : 'info'}">${order.status}</span>
                </div>
                <div class="card-body">
                    <p><strong>Date:</strong> ${order.orderDate}</p>
                    <p><strong>Shipping Address:</strong> ${order.shippingAddress}</p>
                    <p><strong>Phone:</strong> ${order.phone}</p>
                </div>
            </div>

            <div class="card">
                <div class="card-header"><h3>Items</h3></div>
                <div class="card-body">
                    <div class="table-wrapper">
                        <table>
                            <thead>
                                <tr><th>Product</th><th>Price</th><th>Qty</th><th>Subtotal</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>${item.productName}</td>
                                        <td>$<fmt:formatNumber value="${item.price}" pattern="0.00"/></td>
                                        <td>${item.quantity}</td>
                                        <td>$<fmt:formatNumber value="${item.subtotal}" pattern="0.00"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="3" style="text-align:right;font-weight:600">Total</td>
                                    <td style="font-weight:700;color:var(--primary)">$<fmt:formatNumber value="${order.totalAmount}" pattern="0.00"/></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="container"><p>&copy; 2026 GadgetZone.</p></div>
    </footer>
</body>
</html>

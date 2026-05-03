<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders - GadgetZone Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="active">Orders</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container">
            <div class="admin-layout">
                <aside class="admin-sidebar">
                    <div class="sidebar-header">Admin Panel</div>
                    <ul class="sidebar-nav">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard">&#128202; Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/products">&#128230; Products</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/categories">&#128193; Categories</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/orders" class="active">&#128196; Orders</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users">&#128101; Users</a></li>
                    </ul>
                </aside>

                <div class="admin-content">
                    <h1 class="mb-2">Orders</h1>

                    <c:if test="${not empty sessionScope.success}">
                        <div class="alert alert-success">${sessionScope.success}</div>
                        <c:set var="success" value="" scope="session" />
                    </c:if>

                    <!-- Filter -->
                    <div class="category-filter mb-2">
                        <a href="${pageContext.request.contextPath}/admin/orders" class="${empty statusFilter ? 'active' : ''}">All</a>
                        <a href="${pageContext.request.contextPath}/admin/orders?status=pending" class="${statusFilter == 'pending' ? 'active' : ''}">Pending</a>
                        <a href="${pageContext.request.contextPath}/admin/orders?status=confirmed" class="${statusFilter == 'confirmed' ? 'active' : ''}">Confirmed</a>
                        <a href="${pageContext.request.contextPath}/admin/orders?status=shipped" class="${statusFilter == 'shipped' ? 'active' : ''}">Shipped</a>
                        <a href="${pageContext.request.contextPath}/admin/orders?status=delivered" class="${statusFilter == 'delivered' ? 'active' : ''}">Delivered</a>
                        <a href="${pageContext.request.contextPath}/admin/orders?status=cancelled" class="${statusFilter == 'cancelled' ? 'active' : ''}">Cancelled</a>
                    </div>

                    <div class="card">
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty orders}">
                                    <div class="table-wrapper">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>Customer</th>
                                                    <th>Amount</th>
                                                    <th>Status</th>
                                                    <th>Date</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="order" items="${orders}">
                                                    <tr>
                                                        <td>#${order.id}</td>
                                                        <td>${order.userName}<br><small class="text-muted">${order.userEmail}</small></td>
                                                        <td>$<fmt:formatNumber value="${order.totalAmount}" pattern="0.00"/></td>
                                                        <td><span class="badge badge-${order.status == 'pending' ? 'warning' : order.status == 'delivered' ? 'success' : order.status == 'cancelled' ? 'danger' : 'info'}">${order.status}</span></td>
                                                        <td>${order.orderDate}</td>
                                                        <td><a href="${pageContext.request.contextPath}/admin/orders?action=detail&id=${order.id}" class="btn btn-sm btn-outline">View</a></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted text-center">No orders found.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="container"><p>&copy; 2026 GadgetZone Admin.</p></div>
    </footer>
</body>
</html>

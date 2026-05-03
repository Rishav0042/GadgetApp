<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - GadgetZone</title>
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
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="active">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container">
            <div class="admin-layout">
                <!-- Sidebar -->
                <aside class="admin-sidebar">
                    <div class="sidebar-header">Admin Panel</div>
                    <ul class="sidebar-nav">
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="active">&#128202; Dashboard</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/products">&#128230; Products</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/categories">&#128193; Categories</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/orders">&#128196; Orders</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users">&#128101; Users</a></li>
                    </ul>
                </aside>

                <!-- Content -->
                <div class="admin-content">
                    <h1 class="mb-2">Dashboard</h1>

                    <c:if test="${not empty sessionScope.success}">
                        <div class="alert alert-success">${sessionScope.success}</div>
                        <c:set var="success" value="" scope="session" />
                    </c:if>

                    <!-- Stats -->
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-icon blue">&#128230;</div>
                            <div class="stat-info">
                                <div class="stat-value">${totalProducts}</div>
                                <div class="stat-label">Total Products</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon green">&#128176;</div>
                            <div class="stat-info">
                                <div class="stat-value">$${revenue}</div>
                                <div class="stat-label">Total Revenue</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon yellow">&#128196;</div>
                            <div class="stat-info">
                                <div class="stat-value">${totalOrders}</div>
                                <div class="stat-label">Total Orders</div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon red">&#128101;</div>
                            <div class="stat-info">
                                <div class="stat-value">${totalUsers}</div>
                                <div class="stat-label">Total Users</div>
                            </div>
                        </div>
                    </div>

                    <!-- Pending Orders Alert -->
                    <c:if test="${pendingOrders > 0}">
                        <div class="alert alert-warning">
                            You have <strong>${pendingOrders}</strong> pending order(s) to process.
                            <a href="${pageContext.request.contextPath}/admin/orders?status=pending">View Orders</a>
                        </div>
                    </c:if>

                    <!-- Recent Orders -->
                    <div class="card">
                        <div class="card-header">
                            <h3>Recent Orders</h3>
                            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-sm btn-outline">View All</a>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty recentOrders}">
                                    <div class="table-wrapper">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>Order ID</th>
                                                    <th>Customer</th>
                                                    <th>Amount</th>
                                                    <th>Status</th>
                                                    <th>Date</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="order" items="${recentOrders}">
                                                    <tr>
                                                        <td>#${order.id}</td>
                                                        <td>${order.userName}</td>
                                                        <td>$${order.totalAmount}</td>
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
                                    <p class="text-muted text-center">No orders yet.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="container">
            <p>&copy; 2026 GadgetZone Admin. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>

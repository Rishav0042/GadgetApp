<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - GadgetZone Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users" class="active">Users</a></li>
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
                        <li><a href="${pageContext.request.contextPath}/admin/orders">&#128196; Orders</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users" class="active">&#128101; Users</a></li>
                    </ul>
                </aside>

                <div class="admin-content">
                    <div class="flex-between mb-2">
                        <h1>${isPendingList ? 'Pending Registrations' : 'Users'}</h1>
                        <div class="d-flex gap-1">
                            <a href="${pageContext.request.contextPath}/admin/users" class="btn ${empty isPendingList ? 'btn-primary' : 'btn-outline'}">All Users</a>
                            <a href="${pageContext.request.contextPath}/admin/users?action=pending" class="btn ${isPendingList ? 'btn-warning' : 'btn-outline'}">Pending</a>
                        </div>
                    </div>

                    <c:if test="${not empty sessionScope.success}">
                        <div class="alert alert-success">${sessionScope.success}</div>
                        <c:set var="success" value="" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.error}">
                        <div class="alert alert-danger">${sessionScope.error}</div>
                        <c:set var="error" value="" scope="session" />
                    </c:if>

                    <div class="card">
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty users}">
                                    <div class="table-wrapper">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Username</th>
                                                    <th>Full Name</th>
                                                    <th>Email</th>
                                                    <th>Phone</th>
                                                    <th>Role</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="u" items="${users}">
                                                    <tr>
                                                        <td>${u.id}</td>
                                                        <td>${u.username}</td>
                                                        <td>${u.fullName}</td>
                                                        <td>${u.email}</td>
                                                        <td>${u.phone}</td>
                                                        <td><span class="badge badge-${u.role == 'admin' ? 'primary' : 'info'}">${u.role}</span></td>
                                                        <td><span class="badge badge-${u.status == 'active' ? 'success' : u.status == 'pending' ? 'warning' : 'danger'}">${u.status}</span></td>
                                                        <td>
                                                            <c:if test="${u.status == 'pending'}">
                                                                <a href="${pageContext.request.contextPath}/admin/users?action=approve&id=${u.id}" class="btn btn-sm btn-success">Approve</a>
                                                                <a href="${pageContext.request.contextPath}/admin/users?action=reject&id=${u.id}" class="btn btn-sm btn-danger" onclick="return confirm('Reject this user?')">Reject</a>
                                                            </c:if>
                                                            <c:if test="${u.status != 'pending' && u.role != 'admin'}">
                                                                <a href="${pageContext.request.contextPath}/admin/users?action=delete&id=${u.id}" class="btn btn-sm btn-danger" onclick="return confirm('Delete this user?')">Delete</a>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted text-center">${isPendingList ? 'No pending registrations.' : 'No users found.'}</p>
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

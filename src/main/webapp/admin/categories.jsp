<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories - GadgetZone Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/categories" class="active">Categories</a></li>
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
                        <li><a href="${pageContext.request.contextPath}/admin/categories" class="active">&#128193; Categories</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/orders">&#128196; Orders</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/users">&#128101; Users</a></li>
                    </ul>
                </aside>

                <div class="admin-content">
                    <div class="flex-between mb-2">
                        <h1>Categories</h1>
                        <a href="${pageContext.request.contextPath}/admin/categories?action=add" class="btn btn-primary">+ Add Category</a>
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
                                <c:when test="${not empty categories}">
                                    <div class="table-wrapper">
                                        <table>
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Name</th>
                                                    <th>Description</th>
                                                    <th>Products</th>
                                                    <th>Status</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="cat" items="${categories}">
                                                    <tr>
                                                        <td>${cat.id}</td>
                                                        <td>${cat.name}</td>
                                                        <td>${cat.description}</td>
                                                        <td>${cat.productCount}</td>
                                                        <td><span class="badge badge-${cat.status == 'active' ? 'success' : 'danger'}">${cat.status}</span></td>
                                                        <td>
                                                            <a href="${pageContext.request.contextPath}/admin/categories?action=edit&id=${cat.id}" class="btn btn-sm btn-primary">Edit</a>
                                                            <a href="${pageContext.request.contextPath}/admin/categories?action=delete&id=${cat.id}" class="btn btn-sm btn-danger" onclick="return confirm('Delete this category?')">Delete</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted text-center">No categories found.</p>
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

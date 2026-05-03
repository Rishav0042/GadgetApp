<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - GadgetZone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand"><span class="brand-icon">&#9881;</span> GadgetZone</a>
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/orders?action=cart">Cart</a></li>
                <li><a href="${pageContext.request.contextPath}/wishlist?action=list">Wishlist</a></li>
                <li><a href="${pageContext.request.contextPath}/profile" class="active">${sessionScope.userName}</a></li>
                <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container">
            <h1 class="mb-2">My Profile</h1>

            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success">${sessionScope.success}</div>
                <c:set var="success" value="" scope="session" />
            </c:if>

            <div class="d-flex gap-2" style="flex-wrap:wrap">
                <div style="flex:1;min-width:300px">
                    <div class="card">
                        <div class="card-header flex-between">
                            <h3>Profile Information</h3>
                            <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn btn-sm btn-primary">Edit</a>
                        </div>
                        <div class="card-body">
                            <p><strong>Username:</strong> ${profileUser.username}</p>
                            <p><strong>Full Name:</strong> ${profileUser.fullName}</p>
                            <p><strong>Email:</strong> ${profileUser.email}</p>
                            <p><strong>Phone:</strong> ${profileUser.phone}</p>
                            <p><strong>Address:</strong> ${profileUser.address}</p>
                            <p><strong>Role:</strong> <span class="badge badge-info">${profileUser.role}</span></p>
                            <p><strong>Status:</strong> <span class="badge badge-success">${profileUser.status}</span></p>
                            <p><strong>Member Since:</strong> ${profileUser.createdAt}</p>
                        </div>
                    </div>
                </div>
                <div style="flex:1;min-width:300px">
                    <div class="card">
                        <div class="card-header"><h3>Quick Links</h3></div>
                        <div class="card-body">
                            <a href="${pageContext.request.contextPath}/orders?action=history" class="btn btn-outline btn-block mb-1">My Orders</a>
                            <a href="${pageContext.request.contextPath}/wishlist?action=list" class="btn btn-outline btn-block mb-1">My Wishlist</a>
                            <a href="${pageContext.request.contextPath}/profile?action=changePassword" class="btn btn-outline btn-block">Change Password</a>
                        </div>
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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty product ? 'Add' : 'Edit'} Product - GadgetZone Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<nav class="navbar">
    <div class="container">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand">
            <span class="brand-icon">&#9881;</span> GadgetZone
        </a>
        <ul class="navbar-nav">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products" class="active">Products</a></li>
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
                    <li><a href="${pageContext.request.contextPath}/admin/products" class="active">&#128230; Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/categories">&#128193; Categories</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/orders">&#128196; Orders</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users">&#128101; Users</a></li>
                </ul>
            </aside>

            <div class="admin-content">
                <h1 class="mb-2">${empty product ? 'Add New Product' : 'Edit Product'}</h1>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <div class="card">
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/admin/products"
                              method="post"
                              enctype="multipart/form-data">

                            <input type="hidden" name="action" value="${empty product ? 'add' : 'edit'}">

                            <c:if test="${not empty product}">
                                <input type="hidden" name="id" value="${product.id}">
                            </c:if>

                            <div class="form-group">
                                <label class="form-label">Product Name *</label>
                                <input type="text" name="name" class="form-control" value="${product.name}" required>
                            </div>

                            <div class="form-group">
                                <label class="form-label">Description</label>
                                <textarea name="description" class="form-control" rows="4">${product.description}</textarea>
                            </div>

                            <div class="d-flex gap-2">
                                <div class="form-group" style="flex:1">
                                    <label class="form-label">Price *</label>
                                    <input type="number" step="0.01" min="0" name="price" class="form-control" value="${product.price}" required>
                                </div>

                                <div class="form-group" style="flex:1">
                                    <label class="form-label">Stock *</label>
                                    <input type="number" min="0" name="stock" class="form-control" value="${product.stock}" required>
                                </div>
                            </div>

                            <div class="d-flex gap-2">
                                <div class="form-group" style="flex:1">
                                    <label class="form-label">Category *</label>
                                    <select name="categoryId" class="form-control" required>
                                        <option value="">-- Select Category --</option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.id}" <c:if test="${not empty product && product.categoryId == cat.id}">selected</c:if>>
                                                ${cat.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group" style="flex:1">
                                    <label class="form-label">Brand</label>
                                    <input type="text" name="brand" class="form-control" value="${product.brand}">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="form-label">Product Image</label>
                                <input type="file" name="image" class="form-control" accept="image/*">
                                <small class="text-muted">Upload product image</small>
                            </div>

                            <c:if test="${not empty product}">
                                <div class="form-group">
                                    <label class="form-label">Status</label>
                                    <select name="status" class="form-control">
                                        <option value="active" <c:if test="${product.status == 'active'}">selected</c:if>>Active</option>
                                        <option value="inactive" <c:if test="${product.status == 'inactive'}">selected</c:if>>Inactive</option>
                                    </select>
                                </div>
                            </c:if>

                            <div class="d-flex gap-2 mt-2">
                                <button type="submit" class="btn btn-primary">
                                    ${empty product ? 'Add Product' : 'Update Product'}
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline">Cancel</a>
                            </div>

                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p>&copy; 2026 GadgetZone Admin.</p>
    </div>
</footer>

</body>
</html>
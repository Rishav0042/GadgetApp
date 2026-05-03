<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - GadgetZone</title>
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
                <c:if test="${not empty sessionScope.user}">
                    <li><a href="${pageContext.request.contextPath}/orders?action=cart">Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist?action=list">Wishlist</a></li>
                    <li><a href="${pageContext.request.contextPath}/auth?action=logout">Logout</a></li>
                </c:if>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div class="container">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline mb-2">&larr; Back to Products</a>

            <div class="d-flex gap-2" style="flex-wrap:wrap">
                <!-- Product Image -->
                <div style="flex:0 0 300px">
                    <div class="card">
                        <div style="height:300px;display:flex;align-items:center;justify-content:center;font-size:5rem;background:var(--primary-light);color:var(--primary);border-radius:var(--radius-lg) var(--radius-lg) 0 0">&#128241;</div>
                        <div class="card-body text-center">
                            <c:if test="${not empty sessionScope.user}">
                                <c:choose>
                                    <c:when test="${inWishlist}">
                                        <a href="${pageContext.request.contextPath}/wishlist?action=remove&productId=${product.id}" class="btn btn-warning btn-block">&#10084; In Wishlist</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/wishlist?action=add&productId=${product.id}" class="btn btn-outline btn-block">&#9825; Add to Wishlist</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </div>
                    </div>
                </div>

                <!-- Product Info -->
                <div style="flex:1;min-width:300px">
                    <h1>${product.name}</h1>
                    <p class="text-muted mb-1">Brand: <strong>${product.brand}</strong> | Category: <strong>${product.categoryName}</strong></p>

                    <!-- Rating -->
                    <div class="mb-1">
                        <div class="star-rating">
                            <c:forEach begin="1" end="5" var="i">
                                <span class="star ${i <= avgRating ? 'filled' : ''}">&#9733;</span>
                            </c:forEach>
                        </div>
                        <span class="text-muted">(${avgRating >= 0 ? String.format("%.1f", avgRating) : "0.0"} / 5)</span>
                    </div>

                    <div class="product-price" style="font-size:2rem;margin:0.5rem 0">$<fmt:formatNumber value="${product.price}" pattern="0.00"/></div>

                    <div class="product-stock ${product.stock > 0 ? 'in-stock' : 'out-of-stock'} mb-2" style="font-size:1rem">
                        <c:choose>
                            <c:when test="${product.stock > 0}">${product.stock} items in stock</c:when>
                            <c:otherwise>Out of stock</c:otherwise>
                        </c:choose>
                    </div>

                    <p class="mb-2">${product.description}</p>

                    <c:if test="${product.stock > 0 && not empty sessionScope.user}">
                        <form action="${pageContext.request.contextPath}/orders" method="get" class="d-flex gap-1 mb-2">
                            <input type="hidden" name="action" value="addToCart">
                            <input type="hidden" name="productId" value="${product.id}">
                            <div class="qty-input">
                                <button type="button" onclick="changeQty(-1)">-</button>
                                <input type="number" name="quantity" id="qty" value="1" min="1" max="${product.stock}">
                                <button type="button" onclick="changeQty(1)">+</button>
                            </div>
                            <button type="submit" class="btn btn-success btn-lg">Add to Cart</button>
                        </form>
                    </c:if>
                    <c:if test="${empty sessionScope.user}">
                        <p><a href="${pageContext.request.contextPath}/auth?action=login">Login</a> to add items to cart.</p>
                    </c:if>
                </div>
            </div>

            <!-- Reviews Section -->
            <div class="card mt-3">
                <div class="card-header">
                    <h3>Reviews (${not empty reviews ? reviews.size() : 0})</h3>
                </div>
                <div class="card-body">
                    <!-- Submit Review -->
                    <c:if test="${not empty sessionScope.user}">
                        <form action="${pageContext.request.contextPath}/products" method="post" class="mb-3">
                            <input type="hidden" name="action" value="review">
                            <input type="hidden" name="productId" value="${product.id}">
                            <div class="form-group">
                                <label class="form-label">Rating</label>
                                <select name="rating" class="form-control" style="max-width:150px">
                                    <option value="5">&#9733;&#9733;&#9733;&#9733;&#9733; (5)</option>
                                    <option value="4">&#9733;&#9733;&#9733;&#9733; (4)</option>
                                    <option value="3">&#9733;&#9733;&#9733; (3)</option>
                                    <option value="2">&#9733;&#9733; (2)</option>
                                    <option value="1">&#9733; (1)</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Comment</label>
                                <textarea name="comment" class="form-control" rows="2" required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit Review</button>
                        </form>
                    </c:if>

                    <!-- Review List -->
                    <c:choose>
                        <c:when test="${not empty reviews}">
                            <c:forEach var="review" items="${reviews}">
                                <div style="border-bottom:1px solid var(--border);padding:1rem 0">
                                    <div class="flex-between">
                                        <div>
                                            <strong>${review.userName}</strong>
                                            <div class="star-rating">
                                                <c:forEach begin="1" end="5" var="i">
                                                    <span class="star ${i <= review.rating ? 'filled' : ''}">&#9733;</span>
                                                </c:forEach>
                                            </div>
                                        </div>
                                        <span class="text-muted">${review.createdAt}</span>
                                    </div>
                                    <p class="mt-1">${review.comment}</p>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="text-muted">No reviews yet. Be the first to review!</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="container"><p>&copy; 2026 GadgetZone.</p></div>
    </footer>

    <script>
        function toggleNav(){document.getElementById('navbarNav').classList.toggle('show');}
        function changeQty(delta){
            var q=document.getElementById('qty');
            var v=parseInt(q.value)+delta;
            if(v>=1&&v<=parseInt(q.max))q.value=v;
        }
    </script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transaction Categories - Hamro Bank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <style>
        .category-list {
            margin-top: 20px;
        }

        .category-item {
            display: flex;
            align-items: center;
            padding: 10px;
            border-bottom: 1px solid #eee;
        }

        .category-color {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            margin-right: 10px;
        }

        .category-icon {
            width: 24px;
            height: 24px;
            margin-right: 10px;
            text-align: center;
        }

        .category-name {
            font-weight: bold;
            flex: 1;
        }

        .category-description {
            color: #666;
            flex: 2;
        }

        .category-actions {
            display: flex;
            gap: 10px;
        }

        .category-form {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .form-row {
            display: flex;
            gap: 15px;
            margin-bottom: 15px;
        }

        .form-group {
            flex: 1;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }

        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .color-preview {
            display: inline-block;
            width: 20px;
            height: 20px;
            border-radius: 50%;
            margin-left: 10px;
            vertical-align: middle;
        }

        .system-badge {
            background-color: #e0f7fa;
            color: #0097a7;
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 12px;
            margin-left: 10px;
        }

        .user-badge {
            background-color: #f1f8e9;
            color: #689f38;
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 12px;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <%@ include file="../common/header.jsp" %>

        <div class="main-content">
            <div class="dashboard-container">
                <%@ include file="../common/sidebar.jsp" %>

                <div class="dashboard-content">
                    <h1>Transaction Categories</h1>

                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <div class="category-form">
                            <h2>Add New Category</h2>
                            <form action="${pageContext.request.contextPath}/categories" method="post">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="name">Category Name</label>
                                        <input type="text" id="name" name="name" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="description">Description</label>
                                        <input type="text" id="description" name="description">
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="icon">Icon</label>
                                        <input type="text" id="icon" name="icon" placeholder="Icon name or code">
                                    </div>
                                    <div class="form-group">
                                        <label for="color">Color</label>
                                        <input type="color" id="color" name="color" value="#4CAF50">
                                        <span class="color-preview" id="colorPreview" style="background-color: #4CAF50;"></span>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary">Add Category</button>
                            </form>
                        </div>
                    </c:if>

                    <div class="category-list">
                        <h2>System Categories</h2>
                        <c:forEach var="category" items="${categories}">
                            <c:if test="${category.system}">
                                <div class="category-item">
                                    <div class="category-color" style="background-color: ${category.color};"></div>
                                    <div class="category-icon">${category.icon}</div>
                                    <div class="category-name">${category.name} <span class="system-badge">System</span></div>
                                    <div class="category-description">${category.description}</div>
                                </div>
                            </c:if>
                        </c:forEach>

                        <h2>User Categories</h2>
                        <c:forEach var="category" items="${categories}">
                            <c:if test="${!category.system}">
                                <div class="category-item">
                                    <div class="category-color" style="background-color: ${category.color};"></div>
                                    <div class="category-icon">${category.icon}</div>
                                    <div class="category-name">${category.name} <span class="user-badge">User</span></div>
                                    <div class="category-description">${category.description}</div>
                                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                        <div class="category-actions">
                                            <button class="btn btn-sm btn-secondary edit-category" data-id="${category.categoryId}">Edit</button>
                                            <button class="btn btn-sm btn-danger delete-category" data-id="${category.categoryId}">Delete</button>
                                        </div>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <%@ include file="../common/footer.jsp" %>
    </div>

    <script>
        // Color preview
        document.getElementById('color').addEventListener('input', function() {
            document.getElementById('colorPreview').style.backgroundColor = this.value;
        });

        // Edit category
        document.querySelectorAll('.edit-category').forEach(button => {
            button.addEventListener('click', function() {
                const categoryId = this.getAttribute('data-id');
                // Implement edit functionality
                alert('Edit category ' + categoryId + ' (Not implemented)');
            });
        });

        // Delete category
        document.querySelectorAll('.delete-category').forEach(button => {
            button.addEventListener('click', function() {
                if (confirm('Are you sure you want to delete this category?')) {
                    const categoryId = this.getAttribute('data-id');

                    fetch('${pageContext.request.contextPath}/categories/api/' + categoryId, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then(response => {
                        if (response.ok) {
                            // Reload the page to show updated categories
                            window.location.reload();
                        } else {
                            return response.json().then(data => {
                                throw new Error(data.error || 'Failed to delete category');
                            });
                        }
                    })
                    .catch(error => {
                        alert(error.message);
                    });
                }
            });
        });
    </script>
</body>
</html>

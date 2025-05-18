<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Add User" />
    <jsp:param name="page" value="admin-users" />
</jsp:include>

<section class="admin-section">
    <div class="section-header">
        <h2>Add New User</h2>
        <div class="section-actions">
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Back to Users</a>
        </div>
    </div>
    
    <div class="admin-content">
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/admin/users" method="post" class="admin-form">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" value="${username}" required>
                    <small>3-20 characters, letters, numbers, and underscores only</small>
                </div>
                
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                    <small>At least 8 characters with at least one digit, lowercase letter, uppercase letter, and special character</small>
                </div>
                
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" value="${email}" required>
                </div>
                
                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" id="fullName" name="fullName" value="${fullName}" required>
                </div>
                
                <div class="form-group">
                    <label for="role">Role</label>
                    <select id="role" name="role" required>
                        <option value="">Select a role</option>
                        <option value="CUSTOMER" ${role == 'CUSTOMER' ? 'selected' : ''}>Customer</option>
                        <option value="ADMIN" ${role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel" id="phone" name="phone" value="${phone}">
                    <small>Format: +9771234567890</small>
                </div>
                
                <div class="form-group">
                    <label for="address">Address</label>
                    <textarea id="address" name="address" rows="3">${address}</textarea>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Create User</button>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

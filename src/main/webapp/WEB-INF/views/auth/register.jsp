<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Register" />
    <jsp:param name="page" value="register" />
</jsp:include>

<section class="auth-section">
    <div class="auth-container">
        <div class="auth-header">
            <h2>Create a New Account</h2>
            <p>Fill in your details to register</p>
        </div>
        
        <form class="auth-form" action="${pageContext.request.contextPath}/register" method="post">
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
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
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
                <label for="phone">Phone Number</label>
                <input type="tel" id="phone" name="phone" value="${phone}">
                <small>Format: +9771234567890</small>
            </div>
            
            <div class="form-group">
                <label for="address">Address</label>
                <textarea id="address" name="address" rows="3">${address}</textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Register</button>
            </div>
            
            <div class="auth-links">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
            </div>
        </form>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

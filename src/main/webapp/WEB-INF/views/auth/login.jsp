<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Login" />
    <jsp:param name="page" value="login" />
</jsp:include>

<section class="auth-section">
    <div class="auth-container">
        <div class="auth-header">
            <h2>Login to Your Account</h2>
            <p>Enter your credentials to access your account</p>
        </div>
        
        <form class="auth-form" action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Login</button>
            </div>
            
            <div class="auth-links">
                <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Register</a></p>
            </div>
        </form>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

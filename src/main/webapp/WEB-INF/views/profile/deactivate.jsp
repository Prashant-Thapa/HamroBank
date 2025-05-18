<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Deactivate Account" />
    <jsp:param name="page" value="profile" />
</jsp:include>

<section class="deactivate-account-section">
    <div class="section-header">
        <h2>Deactivate Your Account</h2>
        <p>We're sorry to see you go</p>
    </div>
    
    <div class="deactivate-account-container">
        <div class="warning-box">
            <h3>Warning: This action cannot be undone!</h3>
            <p>Deactivating your account will:</p>
            <ul>
                <li>Make your account inaccessible</li>
                <li>Prevent you from logging in</li>
                <li>Keep your data in our system (for record-keeping purposes)</li>
            </ul>
            <p>If you wish to use our services again, you will need to contact customer support to reactivate your account.</p>
        </div>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-message">
                ${errorMessage}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/profile/deactivate" method="post" class="deactivate-form">
            <div class="form-group">
                <label for="password">Enter your password to confirm:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="form-group">
                <label for="confirmation">Type "DEACTIVATE" to confirm:</label>
                <input type="text" id="confirmation" name="confirmation" required>
            </div>
            
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-danger">Deactivate Account</button>
            </div>
        </form>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

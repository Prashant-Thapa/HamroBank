<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="Welcome" />
    <jsp:param name="page" value="home" />
</jsp:include>

<section class="hero-section">
    <div class="hero-content">
        <h1>Welcome to Hamro Bank</h1>
        <p>Your Trusted Banking Partner</p>
        <div class="hero-buttons">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Login</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary">Register</a>
        </div>
    </div>
</section>

<section class="features-section">
    <div class="container">
        <h2 class="section-title">Our Services</h2>
        <div class="features-grid">
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-account"></i>
                </div>
                <h3>Accounts</h3>
                <p>Open and manage various types of accounts including savings, checking, and fixed deposits.</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-transfer"></i>
                </div>
                <h3>Money Transfers</h3>
                <p>Transfer money between accounts quickly and securely with real-time updates.</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-security"></i>
                </div>
                <h3>Secure Banking</h3>
                <p>Bank with confidence knowing your information is protected with state-of-the-art security.</p>
            </div>

            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-support"></i>
                </div>
                <h3>24/7 Support</h3>
                <p>Our customer support team is available around the clock to assist you with any issues.</p>
            </div>
        </div>
    </div>
</section>

<section class="cta-section">
    <div class="container">
        <div class="cta-content">
            <h2>Ready to get started?</h2>
            <p>Join thousands of satisfied customers who trust Hamro Bank for their banking needs.</p>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Open an Account Today</a>
        </div>
    </div>
</section>

<jsp:include page="WEB-INF/views/common/footer.jsp" />
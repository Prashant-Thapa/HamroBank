<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Home - Hamro Bank" />
    <jsp:param name="page" value="home" />
</jsp:include>

<section class="hero-section">
    <div class="hero-content">
        <h1>Welcome to Hamro Bank</h1>
        <p>Your Trusted Banking Partner</p>
        <div class="hero-buttons">
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Open an Account</a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary">Login</a>
        </div>
    </div>
</section>

<section class="features-section">
    <div class="container">
        <h2 class="section-title">Why Choose Hamro Bank?</h2>
        
        <div class="features-grid">
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-security"></i>
                </div>
                <h3>Secure Banking</h3>
                <p>Your security is our priority. We use state-of-the-art encryption and security measures to protect your financial information.</p>
            </div>
            
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-mobile"></i>
                </div>
                <h3>Online Banking</h3>
                <p>Access your accounts anytime, anywhere with our easy-to-use online banking platform.</p>
            </div>
            
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-support"></i>
                </div>
                <h3>24/7 Support</h3>
                <p>Our customer support team is available round the clock to assist you with any banking needs.</p>
            </div>
            
            <div class="feature-card">
                <div class="feature-icon">
                    <i class="icon-rates"></i>
                </div>
                <h3>Competitive Rates</h3>
                <p>Enjoy competitive interest rates on savings accounts and loans.</p>
            </div>
        </div>
    </div>
</section>

<section class="services-preview-section">
    <div class="container">
        <h2 class="section-title">Our Services</h2>
        
        <div class="services-preview-grid">
            <div class="service-preview-card">
                <h3>Personal Banking</h3>
                <p>Manage your personal finances with our range of accounts, cards, and services designed to meet your everyday banking needs.</p>
                <a href="${pageContext.request.contextPath}/services#personal" class="btn btn-outline">Learn More</a>
            </div>
            
            <div class="service-preview-card">
                <h3>Business Banking</h3>
                <p>From small businesses to large corporations, we offer tailored financial solutions to help your business grow and succeed.</p>
                <a href="${pageContext.request.contextPath}/services#business" class="btn btn-outline">Learn More</a>
            </div>
            
            <div class="service-preview-card">
                <h3>Loans & Mortgages</h3>
                <p>Whether you're buying a home, a car, or need funds for personal reasons, we have loan options to suit your needs.</p>
                <a href="${pageContext.request.contextPath}/services#loans" class="btn btn-outline">Learn More</a>
            </div>
        </div>
    </div>
</section>

<section class="testimonials-section">
    <div class="container">
        <h2 class="section-title">What Our Customers Say</h2>
        
        <div class="testimonials-slider">
            <div class="testimonial">
                <div class="testimonial-content">
                    <p>"Hamro Bank has been my trusted financial partner for over 5 years. Their customer service is exceptional, and their online banking platform is user-friendly and secure."</p>
                </div>
                <div class="testimonial-author">
                    <div class="author-name">Rajesh Sharma</div>
                    <div class="author-title">Business Owner</div>
                </div>
            </div>
            
            <div class="testimonial">
                <div class="testimonial-content">
                    <p>"I got my first home loan from Hamro Bank, and the process was smooth and transparent. The staff was helpful and guided me through every step."</p>
                </div>
                <div class="testimonial-author">
                    <div class="author-name">Sita Thapa</div>
                    <div class="author-title">Teacher</div>
                </div>
            </div>
            
            <div class="testimonial">
                <div class="testimonial-content">
                    <p>"The mobile banking app is fantastic! I can manage all my accounts, pay bills, and transfer money with just a few taps. Highly recommended!"</p>
                </div>
                <div class="testimonial-author">
                    <div class="author-name">Anil Gurung</div>
                    <div class="author-title">Software Engineer</div>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="cta-section">
    <div class="container">
        <div class="cta-content">
            <h2>Ready to Experience Better Banking?</h2>
            <p>Join thousands of satisfied customers who trust Hamro Bank for their financial needs.</p>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Open an Account Today</a>
        </div>
    </div>
</section>

<jsp:include page="common/footer.jsp" />

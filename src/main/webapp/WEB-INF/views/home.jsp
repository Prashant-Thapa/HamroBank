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
        
        <div class="services-preview-grid" role="list">
            <article class="service-preview-card" role="listitem" tabindex="0" aria-label="Personal Banking Service">
                <h3>Personal Banking</h3>
                <p>Manage your personal finances with our range of accounts, cards, and services designed to meet your everyday banking needs.</p>
                <a href="${pageContext.request.contextPath}/services#personal" class="btn btn-outline" aria-describedby="personal-desc">Learn More</a>
            </article>
            
            <article class="service-preview-card" role="listitem" tabindex="0" aria-label="Business Banking Service">
                <h3>Business Banking</h3>
                <p>From small businesses to large corporations, we offer tailored financial solutions to help your business grow and succeed.</p>
                <a href="${pageContext.request.contextPath}/services#business" class="btn btn-outline" aria-describedby="business-desc">Learn More</a>
            </article>
            
            <article class="service-preview-card" role="listitem" tabindex="0" aria-label="Loans and Mortgages Service">
                <h3>Loans & Mortgages</h3>
                <p>Whether you're buying a home, a car, or need funds for personal reasons, we have loan options to suit your needs.</p>
                <a href="${pageContext.request.contextPath}/services#loans" class="btn btn-outline" aria-describedby="loans-desc">Learn More</a>
            </article>
        </div>
    </div>
</section>

<section class="testimonials-section" aria-label="Customer Testimonials">
    <div class="container">
        <h2 class="section-title">What Our Customers Say</h2>
        
        <div class="testimonials-slider" role="list">
            <blockquote class="testimonial" role="listitem" tabindex="0">
                <p>"Hamro Bank has been my trusted financial partner for over 5 years. Their customer service is exceptional, and their online banking platform is user-friendly and secure."</p>
                <footer class="testimonial-author">
                    <cite class="author-name">Rajesh Sharma</cite> — <span class="author-title">Business Owner</span>
                </footer>
            </blockquote>
            
            <blockquote class="testimonial" role="listitem" tabindex="0">
                <p>"I got my first home loan from Hamro Bank, and the process was smooth and transparent. The staff was helpful and guided me through every step."</p>
                <footer class="testimonial-author">
                    <cite class="author-name">Sita Thapa</cite> — <span class="author-title">Teacher</span>
                </footer>
            </blockquote>
            
            <blockquote class="testimonial" role="listitem" tabindex="0">
                <p>"The mobile banking app is fantastic! I can manage all my accounts, pay bills, and transfer money with just a few taps. Highly recommended!"</p>
                <footer class="testimonial-author">
                    <cite class="author-name">Anil Gurung</cite> — <span class="author-title">Software Engineer</span>
                </footer>
            </blockquote>

            <blockquote class="testimonial" role="listitem" tabindex="0">
                <p>"Hamro Bank’s investment advisory team helped me plan my finances better and start saving for my child's education. Their personalized support made a real difference."</p>
                <footer class="testimonial-author">
                    <cite class="author-name">Manisha K.C.</cite> — <span class="author-title">Civil Servant</span>
                </footer>
            </blockquote>


        </div>
    </div>
</section>

<jsp:include page="common/footer.jsp" />
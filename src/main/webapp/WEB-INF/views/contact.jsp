<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Contact Us - Hamro Bank" />
    <jsp:param name="page" value="contact" />
</jsp:include>

<section class="page-header">
    <div class="container">
        <h1>Contact Us</h1>
        <p>We're here to help with any questions you may have</p>
    </div>
</section>

<section class="contact-section">
    <div class="container">
        <div class="contact-grid">
            <div class="contact-info">
                <h2>Get in Touch</h2>
                <p>Our customer service team is available to assist you with any inquiries or concerns.</p>
                
                <div class="contact-methods">
                    <div class="contact-method">
                        <div class="method-icon">
                            <i class="icon-phone"></i>
                        </div>
                        <div class="method-details">
                            <h3>Phone</h3>
                            <p>+977-1-1234567</p>
                            <p class="text-muted">Monday to Friday: 9:00 AM - 5:00 PM</p>
                            <p class="text-muted">Saturday: 10:00 AM - 2:00 PM</p>
                        </div>
                    </div>
                    
                    <div class="contact-method">
                        <div class="method-icon">
                            <i class="icon-email"></i>
                        </div>
                        <div class="method-details">
                            <h3>Email</h3>
                            <p>info@hamrobank.com</p>
                            <p class="text-muted">We'll respond within 24 hours</p>
                        </div>
                    </div>
                    
                    <div class="contact-method">
                        <div class="method-icon">
                            <i class="icon-location"></i>
                        </div>
                        <div class="method-details">
                            <h3>Head Office</h3>
                            <p>123 Banking Street, Kathmandu, Nepal</p>
                            <p class="text-muted">Visit us during business hours</p>
                        </div>
                    </div>
                </div>
                
                <div class="branch-locator">
                    <h3>Find a Branch</h3>
                    <p>We have over 50 branches across Nepal. Use our branch locator to find the one nearest to you.</p>
                    <a href="#" class="btn btn-outline">Branch Locator</a>
                </div>
            </div>
            
            <div class="contact-form-container">
                <h2>Send Us a Message</h2>
                <p>Fill out the form below and we'll get back to you as soon as possible.</p>
                
                <c:if test="${not empty successMessage}">
                    <div class="success-message">
                        ${successMessage}
                    </div>
                </c:if>
                
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                        ${errorMessage}
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/contact" method="post" class="contact-form">
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" id="name" name="name" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="tel" id="phone" name="phone">
                    </div>
                    
                    <div class="form-group">
                        <label for="subject">Subject</label>
                        <select id="subject" name="subject" required>
                            <option value="">Select a subject</option>
                            <option value="General Inquiry">General Inquiry</option>
                            <option value="Account Information">Account Information</option>
                            <option value="Loan Information">Loan Information</option>
                            <option value="Technical Support">Technical Support</option>
                            <option value="Feedback">Feedback</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="message">Message</label>
                        <textarea id="message" name="message" rows="5" required></textarea>
                    </div>
                    
                    <div class="form-group">
                        <div class="checkbox-container">
                            <input type="checkbox" id="privacy" name="privacy" required>
                            <label for="privacy">I agree to the <a href="#">Privacy Policy</a> and consent to the processing of my personal data.</label>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Send Message</button>
                </form>
            </div>
        </div>
    </div>
</section>

<section class="map-section">
    <div class="container">
        <h2 class="section-title">Our Location</h2>
        <div class="map-container">
            <!-- Replace with actual map embed code -->
            <div class="map-placeholder">
                <p>Map of Hamro Bank Head Office</p>
                <p>123 Banking Street, Kathmandu, Nepal</p>
            </div>
        </div>
    </div>
</section>

<section class="faq-section">
    <div class="container">
        <h2 class="section-title">Frequently Asked Questions</h2>
        
        <div class="faq-container">
            <div class="faq-item">
                <div class="faq-question">
                    <h3>How do I open a new account?</h3>
                    <span class="faq-toggle">+</span>
                </div>
                <div class="faq-answer">
                    <p>You can open a new account online through our website or by visiting any of our branches. You'll need to provide identification documents and complete an application form.</p>
                </div>
            </div>
            
            <div class="faq-item">
                <div class="faq-question">
                    <h3>What do I do if I lose my debit card?</h3>
                    <span class="faq-toggle">+</span>
                </div>
                <div class="faq-answer">
                    <p>If you lose your debit card, please contact our customer service immediately at +977-1-1234567 to report the loss and block your card. We'll arrange for a replacement card to be issued.</p>
                </div>
            </div>
            
            <div class="faq-item">
                <div class="faq-question">
                    <h3>How can I check my account balance?</h3>
                    <span class="faq-toggle">+</span>
                </div>
                <div class="faq-answer">
                    <p>You can check your account balance through our online banking portal, mobile app, at any ATM, or by visiting a branch. You can also request balance information via SMS banking.</p>
                </div>
            </div>
            
            <div class="faq-item">
                <div class="faq-question">
                    <h3>What are the interest rates for loans?</h3>
                    <span class="faq-toggle">+</span>
                </div>
                <div class="faq-answer">
                    <p>Our loan interest rates vary depending on the type of loan, loan amount, and your credit history. Please contact our loan department or visit a branch for the most current rates and personalized offers.</p>
                </div>
            </div>
            
            <div class="faq-item">
                <div class="faq-question">
                    <h3>How do I reset my online banking password?</h3>
                    <span class="faq-toggle">+</span>
                </div>
                <div class="faq-answer">
                    <p>You can reset your online banking password by clicking on the "Forgot Password" link on the login page. You'll need to verify your identity through security questions or a verification code sent to your registered mobile number or email.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // FAQ toggle functionality
        const faqItems = document.querySelectorAll('.faq-item');
        
        faqItems.forEach(item => {
            const question = item.querySelector('.faq-question');
            const answer = item.querySelector('.faq-answer');
            const toggle = item.querySelector('.faq-toggle');
            
            question.addEventListener('click', function() {
                // Close all other answers
                faqItems.forEach(otherItem => {
                    if (otherItem !== item) {
                        otherItem.querySelector('.faq-answer').style.display = 'none';
                        otherItem.querySelector('.faq-toggle').textContent = '+';
                    }
                });
                
                // Toggle current answer
                if (answer.style.display === 'block') {
                    answer.style.display = 'none';
                    toggle.textContent = '+';
                } else {
                    answer.style.display = 'block';
                    toggle.textContent = '-';
                }
            });
        });
    });
</script>

<jsp:include page="common/footer.jsp" />

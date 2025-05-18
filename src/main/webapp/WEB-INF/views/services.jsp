<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Services - Hamro Bank" />
    <jsp:param name="page" value="services" />
</jsp:include>

<section class="page-header">
    <div class="container">
        <h1>Our Services</h1>
        <p>Comprehensive banking solutions for all your financial needs</p>
    </div>
</section>

<section class="services-nav-section">
    <div class="container">
        <div class="services-nav">
            <a href="#personal" class="service-nav-item">Personal Banking</a>
            <a href="#business" class="service-nav-item">Business Banking</a>
            <a href="#loans" class="service-nav-item">Loans & Mortgages</a>
            <a href="#digital" class="service-nav-item">Digital Banking</a>
            <a href="#investments" class="service-nav-item">Investments</a>
            <a href="#insurance" class="service-nav-item">Insurance</a>
        </div>
    </div>
</section>

<section id="personal" class="service-section">
    <div class="container">
        <div class="service-header">
            <h2>Personal Banking</h2>
            <p>Manage your personal finances with our range of accounts and services</p>
        </div>
        
        <div class="service-grid">
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-savings"></i>
                </div>
                <h3>Savings Accounts</h3>
                <p>Our savings accounts offer competitive interest rates to help your money grow. Choose from various options based on your needs and goals.</p>
                <ul class="service-features">
                    <li>Competitive interest rates</li>
                    <li>No minimum balance requirements</li>
                    <li>Free online and mobile banking</li>
                    <li>Monthly statements</li>
                </ul>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">Open Account</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-checking"></i>
                </div>
                <h3>Checking Accounts</h3>
                <p>Our checking accounts provide convenient access to your funds for everyday transactions with various benefits.</p>
                <ul class="service-features">
                    <li>Free debit card</li>
                    <li>Unlimited transactions</li>
                    <li>Bill pay services</li>
                    <li>Overdraft protection</li>
                </ul>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">Open Account</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-credit-card"></i>
                </div>
                <h3>Credit Cards</h3>
                <p>Our credit cards offer flexibility, rewards, and security for your purchases, both online and in-store.</p>
                <ul class="service-features">
                    <li>Cashback rewards</li>
                    <li>Travel benefits</li>
                    <li>Fraud protection</li>
                    <li>Flexible payment options</li>
                </ul>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">Apply Now</a>
            </div>
        </div>
    </div>
</section>

<section id="business" class="service-section">
    <div class="container">
        <div class="service-header">
            <h2>Business Banking</h2>
            <p>Financial solutions to help your business grow and succeed</p>
        </div>
        
        <div class="service-grid">
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-business-account"></i>
                </div>
                <h3>Business Accounts</h3>
                <p>Our business accounts are designed to meet the unique needs of businesses of all sizes, from startups to established enterprises.</p>
                <ul class="service-features">
                    <li>Business checking and savings</li>
                    <li>Merchant services</li>
                    <li>Cash management</li>
                    <li>Business credit cards</li>
                </ul>
                <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline">Contact Us</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-business-loan"></i>
                </div>
                <h3>Business Loans</h3>
                <p>Get the funding your business needs to start, expand, or manage cash flow with our flexible business loan options.</p>
                <ul class="service-features">
                    <li>Term loans</li>
                    <li>Lines of credit</li>
                    <li>Equipment financing</li>
                    <li>Commercial real estate loans</li>
                </ul>
                <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline">Learn More</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-payroll"></i>
                </div>
                <h3>Payroll Services</h3>
                <p>Simplify your payroll process with our comprehensive payroll services, designed to save you time and ensure accuracy.</p>
                <ul class="service-features">
                    <li>Direct deposit</li>
                    <li>Tax filing</li>
                    <li>Employee self-service</li>
                    <li>Reporting and analytics</li>
                </ul>
                <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline">Get Started</a>
            </div>
        </div>
    </div>
</section>

<section id="loans" class="service-section">
    <div class="container">
        <div class="service-header">
            <h2>Loans & Mortgages</h2>
            <p>Financing solutions for your personal and property needs</p>
        </div>
        
        <div class="service-grid">
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-home-loan"></i>
                </div>
                <h3>Home Loans</h3>
                <p>Make your dream of homeownership a reality with our competitive mortgage options and personalized guidance.</p>
                <ul class="service-features">
                    <li>Fixed and adjustable rates</li>
                    <li>First-time homebuyer programs</li>
                    <li>Refinancing options</li>
                    <li>Home equity loans</li>
                </ul>
                <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline">Apply Now</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-auto-loan"></i>
                </div>
                <h3>Auto Loans</h3>
                <p>Finance your next vehicle purchase with our competitive auto loan rates and flexible terms.</p>
                <ul class="service-features">
                    <li>New and used vehicle financing</li>
                    <li>Competitive rates</li>
                    <li>Flexible repayment terms</li>
                    <li>Quick approval process</li>
                </ul>
                <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline">Apply Now</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-personal-loan"></i>
                </div>
                <h3>Personal Loans</h3>
                <p>Get the funds you need for life's expenses, from debt consolidation to home improvements or unexpected costs.</p>
                <ul class="service-features">
                    <li>Unsecured loans</li>
                    <li>Fixed interest rates</li>
                    <li>Flexible loan amounts</li>
                    <li>Easy application process</li>
                </ul>
                <a href="${pageContext.request.contextPath}/contact" class="btn btn-outline">Apply Now</a>
            </div>
        </div>
    </div>
</section>

<section id="digital" class="service-section">
    <div class="container">
        <div class="service-header">
            <h2>Digital Banking</h2>
            <p>Convenient banking services at your fingertips</p>
        </div>
        
        <div class="service-grid">
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-online-banking"></i>
                </div>
                <h3>Online Banking</h3>
                <p>Access your accounts, pay bills, transfer funds, and more from your computer or mobile device.</p>
                <ul class="service-features">
                    <li>24/7 account access</li>
                    <li>Bill payment services</li>
                    <li>Fund transfers</li>
                    <li>Account alerts</li>
                </ul>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Login</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-mobile-banking"></i>
                </div>
                <h3>Mobile Banking</h3>
                <p>Bank on the go with our user-friendly mobile app, designed to make banking convenient and secure.</p>
                <ul class="service-features">
                    <li>Mobile check deposit</li>
                    <li>Fingerprint and face ID login</li>
                    <li>ATM locator</li>
                    <li>Card controls</li>
                </ul>
                <a href="#" class="btn btn-outline">Download App</a>
            </div>
            
            <div class="service-card">
                <div class="service-icon">
                    <i class="icon-e-statements"></i>
                </div>
                <h3>E-Statements</h3>
                <p>Reduce paper waste and access your statements securely online with our e-statement service.</p>
                <ul class="service-features">
                    <li>Paperless statements</li>
                    <li>Secure online access</li>
                    <li>Statement history</li>
                    <li>Download and print options</li>
                </ul>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Enroll Now</a>
            </div>
        </div>
    </div>
</section>

<section class="cta-section">
    <div class="container">
        <div class="cta-content">
            <h2>Ready to Get Started?</h2>
            <p>Contact our team to learn more about our services or open an account today.</p>
            <div class="cta-buttons">
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary">Open an Account</a>
                <a href="${pageContext.request.contextPath}/contact" class="btn btn-secondary">Contact Us</a>
            </div>
        </div>
    </div>
</section>

<jsp:include page="common/footer.jsp" />

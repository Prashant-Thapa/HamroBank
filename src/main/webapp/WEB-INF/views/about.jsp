e<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="About Us - Hamro Bank" />
    <jsp:param name="page" value="about" />
</jsp:include>

<section class="page-header">
    <div class="container">
        <h1>About Us</h1>
        <p>Learn more about Hamro Bank and our mission</p>
    </div>
</section>

<section class="about-intro-section">
    <div class="container">
        <div class="about-intro-content">
            <div class="about-intro-text">
                <h2>Our Story</h2>
                <p>Hamro Bank was founded in 2010 with a simple mission: to provide accessible, reliable, and customer-focused banking services to the people of Nepal. What started as a small local bank has now grown into one of the most trusted financial institutions in the country.</p>
                <p>Over the years, we have continuously evolved to meet the changing needs of our customers while staying true to our core values of integrity, excellence, and community service.</p>
            </div>
            <div class="about-intro-image">
                <img src="${pageContext.request.contextPath}/images/about-bank.jpg" alt="Hamro Bank Building">
            </div>
        </div>
    </div>
</section>

<section class="mission-vision-section">
    <div class="container">
        <div class="mission-vision-grid">
            <div class="mission-card">
                <h2>Our Mission</h2>
                <p>To empower our customers with innovative financial solutions that help them achieve their goals and secure their future.</p>
            </div>
            
            <div class="vision-card">
                <h2>Our Vision</h2>
                <p>To be the most trusted and preferred banking partner, known for our customer-centric approach, technological innovation, and contribution to community development.</p>
            </div>
        </div>
    </div>
</section>

<section class="values-section">
    <div class="container">
        <h2 class="section-title">Our Core Values</h2>
        
        <div class="values-grid">
            <div class="value-card">
                <div class="value-icon">
                    <i class="icon-integrity"></i>
                </div>
                <h3>Integrity</h3>
                <p>We conduct our business with the highest ethical standards, ensuring transparency and honesty in all our dealings.</p>
            </div>
            
            <div class="value-card">
                <div class="value-icon">
                    <i class="icon-excellence"></i>
                </div>
                <h3>Excellence</h3>
                <p>We strive for excellence in everything we do, continuously improving our services to exceed customer expectations.</p>
            </div>
            
            <div class="value-card">
                <div class="value-icon">
                    <i class="icon-innovation"></i>
                </div>
                <h3>Innovation</h3>
                <p>We embrace technological advancements and innovative solutions to enhance the banking experience for our customers.</p>
            </div>
            
            <div class="value-card">
                <div class="value-icon">
                    <i class="icon-community"></i>
                </div>
                <h3>Community</h3>
                <p>We are committed to giving back to the communities we serve through various social initiatives and sustainable practices.</p>
            </div>
        </div>
    </div>
</section>

<section class="team-section">
    <div class="container">
        <h2 class="section-title">Our Leadership Team</h2>
        
        <div class="team-grid">
            <div class="team-member">
                <div class="member-image">
                    <img src="${pageContext.request.contextPath}/images/ceo.jpg" alt="CEO">
                </div>
                <h3>Prashant Thapa</h3>
                <p class="member-title">Chief Executive Officer</p>
                <p class="member-bio">With over 25 years of experience in the banking industry, Binod leads our organization with vision and strategic insight.</p>
            </div>
            
            <div class="team-member">
                <div class="member-image">
                    <img src="${pageContext.request.contextPath}/images/cfo.jpg" alt="CFO">
                </div>
                <h3>Sahajadi</h3>
                <p class="member-title">Chief Financial Officer</p>
                <p class="member-bio">Sunita brings her extensive financial expertise to ensure the bank's fiscal health and sustainable growth.</p>
            </div>
            
            <div class="team-member">
                <div class="member-image">
                    <img src="${pageContext.request.contextPath}/images/cto.jpg" alt="CTO">
                </div>
                <h3>Sandhya Phuyal</h3>
                <p class="member-title">Chief Technology Officer</p>
                <p class="member-bio">Amar leads our digital transformation initiatives, focusing on innovative solutions for enhanced customer experience.</p>
            </div>
            
            <div class="team-member">
                <div class="member-image">
                    <img src="${pageContext.request.contextPath}/images/coo.jpg" alt="COO">
                </div>
                <h3>Ankita Pokharel</h3>
                <p class="member-title">Chief Operations Officer</p>
                <p class="member-bio">Priya oversees our day-to-day operations, ensuring efficiency and excellence in service delivery.</p>
            </div>
        </div>
    </div>
</section>

<section class="milestones-section">
    <div class="container">
        <h2 class="section-title">Our Milestones</h2>
        
        <div class="timeline">
            <div class="timeline-item">
                <div class="timeline-year">2010</div>
                <div class="timeline-content">
                    <h3>Foundation</h3>
                    <p>Hamro Bank was established with its first branch in Kathmandu.</p>
                </div>
            </div>
            
            <div class="timeline-item">
                <div class="timeline-year">2013</div>
                <div class="timeline-content">
                    <h3>Expansion</h3>
                    <p>Opened 10 new branches across major cities in Nepal.</p>
                </div>
            </div>
            
            <div class="timeline-item">
                <div class="timeline-year">2015</div>
                <div class="timeline-content">
                    <h3>Digital Banking</h3>
                    <p>Launched our first online banking platform and mobile app.</p>
                </div>
            </div>
            
            <div class="timeline-item">
                <div class="timeline-year">2018</div>
                <div class="timeline-content">
                    <h3>Award Recognition</h3>
                    <p>Received "Best Digital Bank" award for our innovative online services.</p>
                </div>
            </div>
            
            <div class="timeline-item">
                <div class="timeline-year">2020</div>
                <div class="timeline-content">
                    <h3>10th Anniversary</h3>
                    <p>Celebrated a decade of service with 50+ branches nationwide.</p>
                </div>
            </div>
            
            <div class="timeline-item">
                <div class="timeline-year">2023</div>
                <div class="timeline-content">
                    <h3>Sustainability Initiative</h3>
                    <p>Launched our green banking program and committed to carbon neutrality by 2030.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="common/footer.jsp" />

        </div>
    </main>

    <footer class="main-footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-logo">
                    <h2>Hamro Bank</h2>
                    <p>Your Trusted Banking Partner</p>
                </div>
                <div class="footer-links">
                    <h3>Quick Links</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/about">About Us</a></li>
                        <li><a href="${pageContext.request.contextPath}/services">Services</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                    </ul>
                </div>
                <div class="footer-contact">
                    <h3>Contact Us</h3>
                    <p>123 Banking Street, Kathmandu, Nepal</p>
                    <p>Email: info@hamrobank.com</p>
                    <p>Phone: +977-1-1234567</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2025 Hamro Bank. All rights reserved.</p>
            </div>
        </div>
    </footer>

    <script>
        // Hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                setTimeout(function() {
                    alert.style.opacity = '0';
                    setTimeout(function() {
                        alert.style.display = 'none';
                    }, 500);
                }, 5000);
            });
        });
    </script>

    <!-- Dropdown menu functionality -->
    <script src="${pageContext.request.contextPath}/js/dropdown.js"></script>

    <!-- Fallback dropdown functionality -->
    <script>
        // Direct click handler for user menu
        document.addEventListener('DOMContentLoaded', function() {
            var userMenus = document.querySelectorAll('.user-menu > a');
            userMenus.forEach(function(menuLink) {
                menuLink.addEventListener('click', function(e) {
                    e.preventDefault();
                    var dropdown = this.parentNode.querySelector('.dropdown');
                    if (dropdown) {
                        // Toggle dropdown visibility
                        if (dropdown.style.display === 'block') {
                            dropdown.style.display = 'none';
                        } else {
                            dropdown.style.display = 'block';
                        }
                    }
                });
            });
        });
    </script>
</body>
</html>

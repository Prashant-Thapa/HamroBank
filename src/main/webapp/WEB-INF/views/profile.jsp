<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="My Profile" />
    <jsp:param name="page" value="profile" />
</jsp:include>

<section class="profile-section">
    <div class="section-header">
        <h2>My Profile</h2>
        <p>View and update your personal information</p>
    </div>

    <div class="profile-content">
        <div class="profile-sidebar">
            <div class="profile-picture-container">
                <div class="profile-picture">
                    <c:choose>
                        <c:when test="${empty user.profilePicture}">
                            <img src="${pageContext.request.contextPath}/profile-picture" alt="Profile Picture">
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/profile-picture/${user.profilePicture}" alt="Profile Picture">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="profile-info">
                    <h3>${user.fullName}</h3>
                    <p class="username">@${user.username}</p>
                    <p class="role"><span class="badge ${user.role}">${user.role}</span></p>
                </div>
            </div>

            <div class="profile-stats">
                <div class="stat">
                    <span class="label">Member Since</span>
                    <span class="value"><fmt:formatDate value="${user.createdAt}" pattern="MMMM yyyy" /></span>
                </div>
                <div class="stat">
                    <span class="label">Last Login</span>
                    <span class="value">
                        <c:choose>
                            <c:when test="${not empty user.lastLogin}">
                                <fmt:formatDate value="${user.lastLogin}" pattern="yyyy-MM-dd HH:mm" />
                            </c:when>
                            <c:otherwise>
                                Never
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>

        <div class="profile-form-container">
            <form class="profile-form" action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
                <div class="form-tabs">
                    <button type="button" class="tab-btn active" data-tab="personal-info">Personal Information</button>
                    <button type="button" class="tab-btn" data-tab="profile-picture">Profile Picture</button>
                    <button type="button" class="tab-btn" data-tab="change-password">Change Password</button>
                </div>

                <div class="tab-content active" id="personal-info">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" value="${user.username}" disabled>
                        <small>Username cannot be changed</small>
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" value="${user.email}" required>
                    </div>

                    <div class="form-group">
                        <label for="fullName">Full Name</label>
                        <input type="text" id="fullName" name="fullName" value="${user.fullName}" required>
                    </div>

                    <div class="form-group">
                        <label for="phone">Phone Number</label>
                        <input type="tel" id="phone" name="phone" value="${user.phone}">
                        <small>Format: +9771234567890</small>
                    </div>

                    <div class="form-group">
                        <label for="address">Address</label>
                        <textarea id="address" name="address" rows="3">${user.address}</textarea>
                    </div>
                </div>

                <div class="tab-content" id="profile-picture">
                    <div class="form-group">
                        <label for="profilePicture">Upload New Profile Picture</label>
                        <input type="file" id="profilePicture" name="profilePicture" accept="image/*">
                        <small>Maximum file size: 2MB. Allowed formats: JPG, JPEG, PNG, GIF</small>
                    </div>

                    <div class="profile-picture-preview">
                        <h4>Current Profile Picture</h4>
                        <div class="preview-container">
                            <c:choose>
                                <c:when test="${empty user.profilePicture}">
                                    <img src="${pageContext.request.contextPath}/profile-picture" alt="Profile Picture">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/profile-picture/${user.profilePicture}" alt="Profile Picture">
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="tab-content" id="change-password">
                    <div class="form-group">
                        <label for="currentPassword">Current Password</label>
                        <input type="password" id="currentPassword" name="currentPassword">
                    </div>

                    <div class="form-group">
                        <label for="newPassword">New Password</label>
                        <input type="password" id="newPassword" name="newPassword">
                        <small>At least 8 characters with at least one digit, lowercase letter, uppercase letter, and special character</small>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirm New Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword">
                    </div>

                    <div class="password-info">
                        <p>Leave password fields empty if you don't want to change your password.</p>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">Cancel</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/customer/dashboard" class="btn btn-secondary">Cancel</a>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="account-actions">
                    <hr>
                    <h4>Account Actions</h4>
                    <p>If you no longer wish to use our services, you can deactivate your account.</p>
                    <a href="${pageContext.request.contextPath}/profile/deactivate" class="btn btn-danger">Deactivate Account</a>
                </div>
            </form>
        </div>
    </div>
</section>

<script>
    // Tab switching functionality
    document.addEventListener('DOMContentLoaded', function() {
        const tabButtons = document.querySelectorAll('.tab-btn');
        const tabContents = document.querySelectorAll('.tab-content');

        tabButtons.forEach(button => {
            button.addEventListener('click', function() {
                // Remove active class from all buttons and contents
                tabButtons.forEach(btn => btn.classList.remove('active'));
                tabContents.forEach(content => content.classList.remove('active'));

                // Add active class to clicked button
                this.classList.add('active');

                // Show corresponding content
                const tabId = this.getAttribute('data-tab');
                document.getElementById(tabId).classList.add('active');
            });
        });

        // Profile picture preview
        const profilePictureInput = document.getElementById('profilePicture');
        if (profilePictureInput) {
            profilePictureInput.addEventListener('change', function() {
                if (this.files && this.files[0]) {
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        const previewContainer = document.querySelector('.preview-container img');
                        if (previewContainer) {
                            previewContainer.src = e.target.result;
                        }
                    };
                    reader.readAsDataURL(this.files[0]);
                }
            });
        }
    });
</script>

<jsp:include page="common/footer.jsp" />

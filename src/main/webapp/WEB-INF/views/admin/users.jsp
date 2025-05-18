<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="User Management" />
    <jsp:param name="page" value="admin-users" />
</jsp:include>

<section class="admin-section">
    <div class="section-header">
        <h2>User Management</h2>
        <div class="section-actions">
            <a href="${pageContext.request.contextPath}/admin/users?action=add" class="btn btn-primary">Add New User</a>
        </div>
    </div>

    <div class="admin-content">
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Full Name</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Last Login</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.userId}</td>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.fullName}</td>
                            <td><span class="badge ${user.role}">${user.role}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.active}">
                                        <span class="badge active">Active</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge inactive">Inactive</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty user.lastLogin}">
                                        <fmt:formatDate value="${user.lastLogin}" pattern="yyyy-MM-dd HH:mm" />
                                    </c:when>
                                    <c:otherwise>
                                        Never
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/users?action=edit&id=${user.userId}" class="btn btn-sm btn-secondary">Edit</a>

                                <c:if test="${sessionScope.user.userId != user.userId}">
                                    <!-- Account Status Toggle -->
                                    <c:choose>
                                        <c:when test="${user.active}">
                                            <form action="${pageContext.request.contextPath}/admin/users/status" method="post" class="inline-form" onsubmit="return confirm('Are you sure you want to deactivate this user account?');">
                                                <input type="hidden" name="userId" value="${user.userId}">
                                                <input type="hidden" name="action" value="deactivate">
                                                <button type="submit" class="btn btn-sm btn-warning">Deactivate</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="${pageContext.request.contextPath}/admin/users/status" method="post" class="inline-form" onsubmit="return confirm('Are you sure you want to activate this user account?');">
                                                <input type="hidden" name="userId" value="${user.userId}">
                                                <input type="hidden" name="action" value="activate">
                                                <button type="submit" class="btn btn-sm btn-success">Activate</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>

                                    <!-- Delete User -->
                                    <form action="${pageContext.request.contextPath}/admin/users" method="post" class="inline-form" onsubmit="return confirm('Are you sure you want to delete this user?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${user.userId}">
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="9" class="no-data">No users found</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

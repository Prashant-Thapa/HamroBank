<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Account Management" />
    <jsp:param name="page" value="admin-accounts" />
</jsp:include>

<section class="admin-section">
    <div class="section-header">
        <h2>Account Management</h2>
        <div class="section-actions">
            <a href="${pageContext.request.contextPath}/admin/accounts?action=add" class="btn btn-primary">Add New Account</a>
        </div>
    </div>
    
    <div class="admin-content">
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Account Number</th>
                        <th>User</th>
                        <th>Type</th>
                        <th>Balance</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="account" items="${accounts}">
                        <tr>
                            <td>${account.accountId}</td>
                            <td>${account.accountNumber}</td>
                            <td>${account.userId}</td>
                            <td>${account.accountType}</td>
                            <td class="amount">$<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/></td>
                            <td><span class="badge ${account.status}">${account.status}</span></td>
                            <td><fmt:formatDate value="${account.createdAt}" pattern="yyyy-MM-dd" /></td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/accounts?action=edit&id=${account.accountId}" class="btn btn-sm btn-secondary">Edit</a>
                                <form action="${pageContext.request.contextPath}/admin/accounts" method="post" class="inline-form" onsubmit="return confirm('Are you sure you want to delete this account?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${account.accountId}">
                                    <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty accounts}">
                        <tr>
                            <td colspan="8" class="no-data">No accounts found</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

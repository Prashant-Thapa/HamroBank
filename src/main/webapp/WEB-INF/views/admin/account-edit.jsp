<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Edit Account" />
    <jsp:param name="page" value="admin-accounts" />
</jsp:include>

<section class="admin-section">
    <div class="section-header">
        <h2>Edit Account</h2>
        <div class="section-actions">
            <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-secondary">Back to Accounts</a>
        </div>
    </div>
    
    <div class="admin-content">
        <div class="form-container">
            <div class="account-info">
                <div class="info-item">
                    <span class="label">Account Number:</span>
                    <span class="value">${account.accountNumber}</span>
                </div>
                <div class="info-item">
                    <span class="label">User:</span>
                    <span class="value">${accountUser.username} - ${accountUser.fullName}</span>
                </div>
                <div class="info-item">
                    <span class="label">Created:</span>
                    <span class="value"><fmt:formatDate value="${account.createdAt}" pattern="yyyy-MM-dd HH:mm" /></span>
                </div>
            </div>
            
            <form action="${pageContext.request.contextPath}/admin/accounts" method="post" class="admin-form">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="accountId" value="${account.accountId}">
                
                <div class="form-group">
                    <label for="accountType">Account Type</label>
                    <select id="accountType" name="accountType" required>
                        <option value="SAVINGS" ${account.accountType == 'SAVINGS' ? 'selected' : ''}>Savings</option>
                        <option value="CHECKING" ${account.accountType == 'CHECKING' ? 'selected' : ''}>Checking</option>
                        <option value="FIXED_DEPOSIT" ${account.accountType == 'FIXED_DEPOSIT' ? 'selected' : ''}>Fixed Deposit</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="balance">Balance</label>
                    <div class="amount-input">
                        <span class="currency-symbol">$</span>
                        <input type="number" id="balance" name="balance" value="${account.balance}" step="0.01" min="0" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status" required>
                        <option value="ACTIVE" ${account.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                        <option value="INACTIVE" ${account.status == 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                        <option value="SUSPENDED" ${account.status == 'SUSPENDED' ? 'selected' : ''}>Suspended</option>
                    </select>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Update Account</button>
                    <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

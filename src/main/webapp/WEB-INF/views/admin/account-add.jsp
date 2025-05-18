<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Add Account" />
    <jsp:param name="page" value="admin-accounts" />
</jsp:include>

<section class="admin-section">
    <div class="section-header">
        <h2>Add New Account</h2>
        <div class="section-actions">
            <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-secondary">Back to Accounts</a>
        </div>
    </div>
    
    <div class="admin-content">
        <div class="form-container">
            <form action="${pageContext.request.contextPath}/admin/accounts" method="post" class="admin-form">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label for="userId">User</label>
                    <select id="userId" name="userId" required>
                        <option value="">Select a user</option>
                        <c:forEach var="customer" items="${customers}">
                            <option value="${customer.userId}" ${userId == customer.userId ? 'selected' : ''}>
                                ${customer.username} - ${customer.fullName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="accountType">Account Type</label>
                    <select id="accountType" name="accountType" required>
                        <option value="">Select an account type</option>
                        <option value="SAVINGS" ${accountType == 'SAVINGS' ? 'selected' : ''}>Savings</option>
                        <option value="CHECKING" ${accountType == 'CHECKING' ? 'selected' : ''}>Checking</option>
                        <option value="FIXED_DEPOSIT" ${accountType == 'FIXED_DEPOSIT' ? 'selected' : ''}>Fixed Deposit</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="initialBalance">Initial Balance</label>
                    <div class="amount-input">
                        <span class="currency-symbol">$</span>
                        <input type="number" id="initialBalance" name="initialBalance" value="${initialBalance}" step="0.01" min="0">
                    </div>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Create Account</button>
                    <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

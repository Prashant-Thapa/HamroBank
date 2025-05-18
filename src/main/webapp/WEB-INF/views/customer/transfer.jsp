<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Transfer Funds" />
    <jsp:param name="page" value="transfer" />
</jsp:include>

<section class="transfer-section">
    <div class="section-header">
        <h2>Transfer Funds</h2>
        <p>Transfer money between accounts</p>
    </div>
    
    <div class="transfer-content">
        <c:if test="${empty accounts}">
            <div class="no-accounts">
                <p>You don't have any accounts to transfer from. Please contact an administrator to create an account for you.</p>
            </div>
        </c:if>
        
        <c:if test="${not empty accounts}">
            <form class="transfer-form" action="${pageContext.request.contextPath}/customer/transfer" method="post">
                <div class="form-group">
                    <label for="sourceAccountId">From Account</label>
                    <select id="sourceAccountId" name="sourceAccountId" required>
                        <option value="">Select an account</option>
                        <c:forEach var="account" items="${accounts}">
                            <c:if test="${account.status == 'ACTIVE'}">
                                <option value="${account.accountId}" ${sourceAccountId == account.accountId ? 'selected' : ''}>
                                    ${account.accountType} - ${account.accountNumber} - $<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/>
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="destinationAccountNumber">To Account Number</label>
                    <input type="text" id="destinationAccountNumber" name="destinationAccountNumber" value="${destinationAccountNumber}" required>
                    <small>Enter the 10-digit account number of the recipient</small>
                </div>
                
                <div class="form-group">
                    <label for="amount">Amount</label>
                    <div class="amount-input">
                        <span class="currency-symbol">$</span>
                        <input type="number" id="amount" name="amount" value="${amount}" step="0.01" min="0.01" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="description">Description (Optional)</label>
                    <textarea id="description" name="description" rows="3">${description}</textarea>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Transfer</button>
                    <a href="${pageContext.request.contextPath}/customer/dashboard" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
            
            <div class="transfer-info">
                <h3>Transfer Information</h3>
                <ul>
                    <li>Transfers are processed immediately.</li>
                    <li>You can only transfer funds from your active accounts.</li>
                    <li>Make sure the destination account number is correct.</li>
                    <li>You cannot transfer more than your available balance.</li>
                    <li>A receipt will be available after the transfer is completed.</li>
                </ul>
            </div>
        </c:if>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

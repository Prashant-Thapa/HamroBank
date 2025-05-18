<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Transaction History" />
    <jsp:param name="page" value="transactions" />
</jsp:include>

<section class="transactions-section">
    <div class="section-header">
        <h2>Transaction History</h2>
        <p>View your transaction history and download receipts</p>
    </div>
    
    <div class="transactions-content">
        <c:if test="${empty accounts}">
            <div class="no-accounts">
                <p>You don't have any accounts to view transactions for. Please contact an administrator to create an account for you.</p>
            </div>
        </c:if>
        
        <c:if test="${not empty accounts}">
            <div class="account-selector">
                <form action="${pageContext.request.contextPath}/customer/transactions" method="get">
                    <div class="form-group">
                        <label for="accountId">Select Account</label>
                        <select id="accountId" name="accountId" onchange="this.form.submit()">
                            <c:forEach var="account" items="${accounts}">
                                <option value="${account.accountId}" ${selectedAccount.accountId == account.accountId ? 'selected' : ''}>
                                    ${account.accountType} - ${account.accountNumber} - $<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </div>
            
            <div class="transactions-table-container">
                <table class="transactions-table">
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Reference</th>
                            <th>Type</th>
                            <th>From/To</th>
                            <th>Amount</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="transaction" items="${transactions}">
                            <tr>
                                <td><fmt:formatDate value="${transaction.transactionDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                <td>${transaction.referenceNumber}</td>
                                <td>${transaction.transactionType}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${transaction.sourceAccountId == selectedAccount.accountId}">
                                            To: ${transaction.destinationAccountNumber}
                                        </c:when>
                                        <c:when test="${transaction.destinationAccountId == selectedAccount.accountId}">
                                            From: ${transaction.sourceAccountNumber}
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="amount ${transaction.sourceAccountId == selectedAccount.accountId ? 'outgoing' : 'incoming'}">
                                    <c:choose>
                                        <c:when test="${transaction.sourceAccountId == selectedAccount.accountId}">
                                            -$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/>
                                        </c:when>
                                        <c:otherwise>
                                            +$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${transaction.description}</td>
                                <td class="status ${transaction.status}">${transaction.status}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/customer/receipt?id=${transaction.transactionId}" class="btn btn-sm btn-secondary" target="_blank">Receipt</a>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty transactions}">
                            <tr>
                                <td colspan="8" class="no-data">No transactions found for this account</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

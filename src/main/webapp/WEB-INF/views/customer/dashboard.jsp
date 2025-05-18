<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Dashboard" />
    <jsp:param name="page" value="dashboard" />
</jsp:include>

<section class="dashboard-section">
    <div class="dashboard-header">
        <h2>Welcome, ${sessionScope.user.fullName}</h2>
        <p>Here's an overview of your accounts and recent transactions</p>
    </div>
    
    <div class="dashboard-content">
        <div class="dashboard-cards">
            <c:forEach var="account" items="${accounts}">
                <div class="card account-card">
                    <div class="card-header">
                        <h3>${account.accountType} Account</h3>
                        <span class="account-status ${account.status}">${account.status}</span>
                    </div>
                    <div class="card-body">
                        <div class="account-number">
                            <span class="label">Account Number:</span>
                            <span class="value">${account.accountNumber}</span>
                        </div>
                        <div class="account-balance">
                            <span class="label">Balance:</span>
                            <span class="value">$<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/></span>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/customer/transactions?accountId=${account.accountId}" class="btn btn-secondary">View Transactions</a>
                        <a href="${pageContext.request.contextPath}/customer/transfer?sourceAccountId=${account.accountId}" class="btn btn-primary">Transfer</a>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty accounts}">
                <div class="no-accounts">
                    <p>You don't have any accounts yet. Please contact an administrator to create an account for you.</p>
                </div>
            </c:if>
        </div>
        
        <c:if test="${not empty primaryAccount}">
            <div class="recent-transactions">
                <div class="section-header">
                    <h3>Recent Transactions</h3>
                    <a href="${pageContext.request.contextPath}/customer/transactions?accountId=${primaryAccount.accountId}" class="view-all">View All</a>
                </div>
                
                <div class="transactions-table-container">
                    <table class="transactions-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Description</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="transaction" items="${recentTransactions}">
                                <tr>
                                    <td><fmt:formatDate value="${transaction.transactionDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                    <td>${transaction.transactionType}</td>
                                    <td class="amount ${transaction.sourceAccountId == primaryAccount.accountId ? 'outgoing' : 'incoming'}">
                                        <c:choose>
                                            <c:when test="${transaction.sourceAccountId == primaryAccount.accountId}">
                                                -$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/>
                                            </c:when>
                                            <c:otherwise>
                                                +$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${transaction.description}</td>
                                    <td class="status ${transaction.status}">${transaction.status}</td>
                                </tr>
                            </c:forEach>
                            
                            <c:if test="${empty recentTransactions}">
                                <tr>
                                    <td colspan="5" class="no-data">No recent transactions</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

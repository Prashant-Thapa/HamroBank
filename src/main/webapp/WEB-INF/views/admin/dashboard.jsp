<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Admin Dashboard" />
    <jsp:param name="page" value="admin-dashboard" />
</jsp:include>

<section class="admin-dashboard-section">
    <div class="section-header">
        <h2>Admin Dashboard</h2>
        <p>Overview of the banking system</p>
    </div>

    <div class="dashboard-stats">
        <div class="stat-card">
            <div class="stat-icon">
                <i class="icon-users"></i>
            </div>
            <div class="stat-content">
                <h3>Total Customers</h3>
                <p class="stat-value">${totalCustomers}</p>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-icon">
                <i class="icon-accounts"></i>
            </div>
            <div class="stat-content">
                <h3>Total Accounts</h3>
                <p class="stat-value">${totalAccounts}</p>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-icon">
                <i class="icon-money"></i>
            </div>
            <div class="stat-content">
                <h3>Total Balance</h3>
                <p class="stat-value">$<fmt:formatNumber value="${totalBalance}" pattern="#,##0.00"/></p>
            </div>
        </div>
    </div>

    <!-- Transactions Section -->
    <div class="dashboard-section">
        <div class="section-header">
            <h3>Recent Transactions</h3>
            <div class="header-actions">
                <a href="${pageContext.request.contextPath}/admin/create-sample-transaction" class="btn btn-sm btn-primary">Create Sample Transaction</a>
                <a href="${pageContext.request.contextPath}/admin/transactions" class="view-all">View All</a>
            </div>
        </div>

        <div class="transactions-table-container">
            <table class="transactions-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Reference</th>
                        <th>Type</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Amount</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="transaction" items="${recentTransactions}">
                        <tr>
                            <td><fmt:formatDate value="${transaction.transactionDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                            <td>${transaction.referenceNumber}</td>
                            <td>${transaction.transactionType}</td>
                            <td>${transaction.sourceAccountNumber}</td>
                            <td>${transaction.destinationAccountNumber}</td>
                            <td class="amount">
                                $<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/>
                            </td>
                            <td class="status ${transaction.status}">${transaction.status}</td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty recentTransactions}">
                        <tr>
                            <td colspan="7" class="no-data">No recent transactions</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Activity Logs Section -->
    <div class="dashboard-section">
        <div class="section-header">
            <h3>Recent Activity Logs</h3>
            <a href="${pageContext.request.contextPath}/admin/logs" class="view-all">View All</a>
        </div>

        <div class="logs-table-container">
            <table class="logs-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>User</th>
                        <th>Activity</th>
                        <th>Description</th>
                        <th>IP Address</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${recentLogs}">
                        <tr>
                            <td><fmt:formatDate value="${log.createdAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                            <td>${log.username}</td>
                            <td>${log.activityType}</td>
                            <td>${log.description}</td>
                            <td>${log.ipAddress}</td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty recentLogs}">
                        <tr>
                            <td colspan="5" class="no-data">No recent activity logs</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Scheduled Transactions - Hamro Bank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <style>
        .scheduled-transaction-list {
            margin-top: 20px;
        }
        
        .scheduled-transaction-item {
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 15px;
            margin-bottom: 15px;
        }
        
        .scheduled-transaction-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
        }
        
        .scheduled-transaction-title {
            font-weight: bold;
            font-size: 18px;
        }
        
        .scheduled-transaction-status {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        
        .status-active {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
        
        .status-paused {
            background-color: #fff8e1;
            color: #ff8f00;
        }
        
        .status-completed {
            background-color: #e0f2f1;
            color: #00796b;
        }
        
        .status-failed {
            background-color: #ffebee;
            color: #c62828;
        }
        
        .scheduled-transaction-details {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
            margin-bottom: 10px;
        }
        
        .detail-item {
            display: flex;
            flex-direction: column;
        }
        
        .detail-label {
            font-size: 12px;
            color: #666;
        }
        
        .detail-value {
            font-weight: bold;
        }
        
        .scheduled-transaction-actions {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }
        
        .scheduled-transaction-form {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .form-row {
            display: flex;
            gap: 15px;
            margin-bottom: 15px;
        }
        
        .form-group {
            flex: 1;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        
        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .empty-state {
            text-align: center;
            padding: 40px;
            background-color: #f9f9f9;
            border-radius: 5px;
            margin-top: 20px;
        }
        
        .empty-state-icon {
            font-size: 48px;
            color: #ccc;
            margin-bottom: 10px;
        }
        
        .empty-state-text {
            font-size: 18px;
            color: #666;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <%@ include file="../common/header.jsp" %>
        
        <div class="main-content">
            <div class="dashboard-container">
                <%@ include file="../common/sidebar.jsp" %>
                
                <div class="dashboard-content">
                    <h1>Scheduled Transactions</h1>
                    
                    <div class="scheduled-transaction-form">
                        <h2>Create New Scheduled Transaction</h2>
                        <form action="${pageContext.request.contextPath}/scheduled-transactions" method="post">
                            <div class="form-row">
                                <div class="form-group">
                                    <label for="sourceAccountId">From Account</label>
                                    <select id="sourceAccountId" name="sourceAccountId" required>
                                        <option value="">Select Account</option>
                                        <c:forEach var="account" items="${userAccounts}">
                                            <option value="${account.accountId}">${account.accountNumber} - ${account.accountType} (${account.balance})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="destinationAccountId">To Account</label>
                                    <input type="text" id="destinationAccountId" name="destinationAccountId" required placeholder="Enter account number">
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <label for="amount">Amount</label>
                                    <input type="number" id="amount" name="amount" step="0.01" min="0.01" required>
                                </div>
                                <div class="form-group">
                                    <label for="frequency">Frequency</label>
                                    <select id="frequency" name="frequency" required>
                                        <option value="ONCE">One Time</option>
                                        <option value="DAILY">Daily</option>
                                        <option value="WEEKLY">Weekly</option>
                                        <option value="MONTHLY">Monthly</option>
                                        <option value="QUARTERLY">Quarterly</option>
                                        <option value="YEARLY">Yearly</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <label for="startDate">Start Date</label>
                                    <input type="date" id="startDate" name="startDate" required min="${java.time.LocalDate.now()}">
                                </div>
                                <div class="form-group">
                                    <label for="endDate">End Date (Optional)</label>
                                    <input type="date" id="endDate" name="endDate" min="${java.time.LocalDate.now()}">
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <label for="description">Description</label>
                                    <textarea id="description" name="description" rows="2" required></textarea>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary">Create Scheduled Transaction</button>
                        </form>
                    </div>
                    
                    <div class="scheduled-transaction-list">
                        <h2>Your Scheduled Transactions</h2>
                        
                        <c:choose>
                            <c:when test="${empty scheduledTransactions}">
                                <div class="empty-state">
                                    <div class="empty-state-icon">📅</div>
                                    <div class="empty-state-text">You don't have any scheduled transactions yet.</div>
                                    <p>Create a scheduled transaction to automate your regular payments.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="transaction" items="${scheduledTransactions}">
                                    <div class="scheduled-transaction-item">
                                        <div class="scheduled-transaction-header">
                                            <div class="scheduled-transaction-title">${transaction.description}</div>
                                            <div class="scheduled-transaction-status status-${transaction.status.toString().toLowerCase()}">${transaction.status}</div>
                                        </div>
                                        <div class="scheduled-transaction-details">
                                            <div class="detail-item">
                                                <span class="detail-label">Amount</span>
                                                <span class="detail-value">$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/></span>
                                            </div>
                                            <div class="detail-item">
                                                <span class="detail-label">Frequency</span>
                                                <span class="detail-value">${transaction.frequency}</span>
                                            </div>
                                            <div class="detail-item">
                                                <span class="detail-label">Next Execution</span>
                                                <span class="detail-value">${transaction.nextExecutionDate}</span>
                                            </div>
                                            <div class="detail-item">
                                                <span class="detail-label">End Date</span>
                                                <span class="detail-value">${transaction.endDate != null ? transaction.endDate : 'No end date'}</span>
                                            </div>
                                        </div>
                                        <div class="scheduled-transaction-actions">
                                            <c:if test="${transaction.status == 'ACTIVE'}">
                                                <button class="btn btn-sm btn-warning pause-transaction" data-id="${transaction.scheduledTxId}">Pause</button>
                                            </c:if>
                                            <c:if test="${transaction.status == 'PAUSED'}">
                                                <button class="btn btn-sm btn-success resume-transaction" data-id="${transaction.scheduledTxId}">Resume</button>
                                            </c:if>
                                            <button class="btn btn-sm btn-danger delete-transaction" data-id="${transaction.scheduledTxId}">Delete</button>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <%@ include file="../common/footer.jsp" %>
    </div>
    
    <script>
        // Set minimum date for date inputs
        document.addEventListener('DOMContentLoaded', function() {
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('startDate').min = today;
            document.getElementById('endDate').min = today;
            
            // Set default start date to today
            document.getElementById('startDate').value = today;
        });
        
        // Pause transaction
        document.querySelectorAll('.pause-transaction').forEach(button => {
            button.addEventListener('click', function() {
                if (confirm('Are you sure you want to pause this scheduled transaction?')) {
                    const transactionId = this.getAttribute('data-id');
                    
                    fetch('${pageContext.request.contextPath}/scheduled-transactions/api/status/' + transactionId, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            status: 'PAUSED'
                        })
                    })
                    .then(response => {
                        if (response.ok) {
                            // Reload the page to show updated status
                            window.location.reload();
                        } else {
                            return response.json().then(data => {
                                throw new Error(data.error || 'Failed to pause transaction');
                            });
                        }
                    })
                    .catch(error => {
                        alert(error.message);
                    });
                }
            });
        });
        
        // Resume transaction
        document.querySelectorAll('.resume-transaction').forEach(button => {
            button.addEventListener('click', function() {
                if (confirm('Are you sure you want to resume this scheduled transaction?')) {
                    const transactionId = this.getAttribute('data-id');
                    
                    fetch('${pageContext.request.contextPath}/scheduled-transactions/api/status/' + transactionId, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            status: 'ACTIVE'
                        })
                    })
                    .then(response => {
                        if (response.ok) {
                            // Reload the page to show updated status
                            window.location.reload();
                        } else {
                            return response.json().then(data => {
                                throw new Error(data.error || 'Failed to resume transaction');
                            });
                        }
                    })
                    .catch(error => {
                        alert(error.message);
                    });
                }
            });
        });
        
        // Delete transaction
        document.querySelectorAll('.delete-transaction').forEach(button => {
            button.addEventListener('click', function() {
                if (confirm('Are you sure you want to delete this scheduled transaction?')) {
                    const transactionId = this.getAttribute('data-id');
                    
                    fetch('${pageContext.request.contextPath}/scheduled-transactions/api/' + transactionId, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then(response => {
                        if (response.ok) {
                            // Reload the page to show updated list
                            window.location.reload();
                        } else {
                            return response.json().then(data => {
                                throw new Error(data.error || 'Failed to delete transaction');
                            });
                        }
                    })
                    .catch(error => {
                        alert(error.message);
                    });
                }
            });
        });
    </script>
</body>
</html>

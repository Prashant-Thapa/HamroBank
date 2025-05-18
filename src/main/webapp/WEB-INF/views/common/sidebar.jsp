<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="sidebar">
    <div class="sidebar-header">
        <h3>Hamro Bank</h3>
    </div>

    <div class="sidebar-menu">
        <ul>
            <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>

            <c:if test="${sessionScope.user.role == 'CUSTOMER'}">
                <li><a href="${pageContext.request.contextPath}/accounts"><i class="fas fa-wallet"></i> Accounts</a></li>
                <li><a href="${pageContext.request.contextPath}/transactions"><i class="fas fa-exchange-alt"></i> Transactions</a></li>
                <li><a href="${pageContext.request.contextPath}/scheduled-transactions"><i class="fas fa-calendar-alt"></i> Scheduled Transactions</a></li>
                <li><a href="${pageContext.request.contextPath}/savings-goals"><i class="fas fa-piggy-bank"></i> Savings Goals</a></li>
                <li><a href="${pageContext.request.contextPath}/budgets"><i class="fas fa-chart-pie"></i> Budgets</a></li>
                <li><a href="${pageContext.request.contextPath}/notifications"><i class="fas fa-bell"></i> Notifications</a></li>
            </c:if>

            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                <li><a href="${pageContext.request.contextPath}/admin/users"><i class="fas fa-users"></i> Users</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/accounts"><i class="fas fa-wallet"></i> Accounts</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/transactions"><i class="fas fa-exchange-alt"></i> Transactions</a></li>
                <li><a href="${pageContext.request.contextPath}/categories"><i class="fas fa-tags"></i> Transaction Categories</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/reports"><i class="fas fa-chart-bar"></i> Reports</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/settings"><i class="fas fa-cog"></i> Settings</a></li>
            </c:if>

            <li><a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user"></i> Profile</a></li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
        </ul>
    </div>
</div>

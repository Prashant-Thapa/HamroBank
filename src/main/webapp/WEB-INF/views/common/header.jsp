<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} - Hamro Bank</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/${param.page}.css">
    <style>
        /* Ensure dropdowns are hidden immediately on page load */
        .dropdown { display: none; }
    </style>
</head>
<body>
    <header class="main-header">
        <div class="container">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/">
                    <img src="${pageContext.request.contextPath}/images/logo.svg" alt="Hamro Bank Logo" width="200" height="60">
                </a>
            </div>
            <nav class="main-nav">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/" class="${param.page == 'home' ? 'active' : ''}">Home</a></li>
                            <li><a href="${pageContext.request.contextPath}/about" class="${param.page == 'about' ? 'active' : ''}">About Us</a></li>
                            <li><a href="${pageContext.request.contextPath}/services" class="${param.page == 'services' ? 'active' : ''}">Services</a></li>
                            <li><a href="${pageContext.request.contextPath}/contact" class="${param.page == 'contact' ? 'active' : ''}">Contact</a></li>
                            <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                            <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
                        </ul>
                    </c:when>
                    <c:when test="${sessionScope.user.role == 'ADMIN'}">
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/users">Users</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/accounts">Accounts</a></li>
                            <li><a href="${pageContext.request.contextPath}/admin/logs">Activity Logs</a></li>
                            <li><a href="${pageContext.request.contextPath}/about" class="${param.page == 'about' ? 'active' : ''}">About</a></li>
                            <li><a href="${pageContext.request.contextPath}/services" class="${param.page == 'services' ? 'active' : ''}">Services</a></li>
                            <li><a href="${pageContext.request.contextPath}/contact" class="${param.page == 'contact' ? 'active' : ''}">Contact</a></li>
                            <li class="user-menu">
                                <a href="#">
                                    <span class="user-avatar">
                                        <c:choose>
                                            <c:when test="${empty sessionScope.user.profilePicture}">
                                                <img src="${pageContext.request.contextPath}/profile-picture" alt="Profile">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/profile-picture/${sessionScope.user.profilePicture}" alt="Profile">
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                    ${sessionScope.user.username} ▼
                                </a>
                                <ul class="dropdown">
                                    <li><a href="${pageContext.request.contextPath}/profile" onclick="window.location.href='${pageContext.request.contextPath}/profile'; return false;">My Profile</a></li>
                                    <li><a href="${pageContext.request.contextPath}/logout" onclick="window.location.href='${pageContext.request.contextPath}/logout'; return false;">Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                    </c:when>
                    <c:otherwise>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/customer/dashboard">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/customer/transfer">Transfer</a></li>
                            <li><a href="${pageContext.request.contextPath}/customer/transactions">Transactions</a></li>
                            <li><a href="${pageContext.request.contextPath}/about" class="${param.page == 'about' ? 'active' : ''}">About</a></li>
                            <li><a href="${pageContext.request.contextPath}/services" class="${param.page == 'services' ? 'active' : ''}">Services</a></li>
                            <li><a href="${pageContext.request.contextPath}/contact" class="${param.page == 'contact' ? 'active' : ''}">Contact</a></li>
                            <li class="user-menu">
                                <a href="#">
                                    <span class="user-avatar">
                                        <c:choose>
                                            <c:when test="${empty sessionScope.user.profilePicture}">
                                                <img src="${pageContext.request.contextPath}/profile-picture" alt="Profile">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/profile-picture/${sessionScope.user.profilePicture}" alt="Profile">
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                    ${sessionScope.user.username} ▼
                                </a>
                                <ul class="dropdown">
                                    <li><a href="${pageContext.request.contextPath}/profile" onclick="window.location.href='${pageContext.request.contextPath}/profile'; return false;">My Profile</a></li>
                                    <li><a href="${pageContext.request.contextPath}/logout" onclick="window.location.href='${pageContext.request.contextPath}/logout'; return false;">Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </header>

    <main class="main-content">
        <div class="container">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <p>${errorMessage}</p>
                </div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">
                    <p>${successMessage}</p>
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success">
                    <p>${sessionScope.successMessage}</p>
                    <c:remove var="successMessage" scope="session" />
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-error">
                    <p>${sessionScope.errorMessage}</p>
                    <c:remove var="errorMessage" scope="session" />
                </div>
            </c:if>

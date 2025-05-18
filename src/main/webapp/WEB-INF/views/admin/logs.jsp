<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Activity Logs" />
    <jsp:param name="page" value="admin-logs" />
</jsp:include>

<section class="admin-section">
    <div class="section-header">
        <h2>Activity Logs</h2>
        <c:if test="${not empty filterType}">
            <div class="filter-info">
                <c:choose>
                    <c:when test="${filterType == 'user'}">
                        <p>Showing logs for User ID: ${filterId}</p>
                    </c:when>
                    <c:when test="${filterType == 'activity'}">
                        <p>Showing logs for Activity Type: ${filterId}</p>
                    </c:when>
                </c:choose>
                <a href="${pageContext.request.contextPath}/admin/logs" class="btn btn-sm btn-secondary">Clear Filter</a>
            </div>
        </c:if>
    </div>
    
    <div class="admin-content">
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Date</th>
                        <th>User</th>
                        <th>Activity Type</th>
                        <th>Description</th>
                        <th>IP Address</th>
                        <th>User Agent</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="log" items="${logs}">
                        <tr>
                            <td>${log.logId}</td>
                            <td><fmt:formatDate value="${log.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty log.username}">
                                        <a href="${pageContext.request.contextPath}/admin/logs?userId=${log.userId}">${log.username}</a>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">System</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><a href="${pageContext.request.contextPath}/admin/logs?type=${log.activityType}">${log.activityType}</a></td>
                            <td>${log.description}</td>
                            <td>${log.ipAddress}</td>
                            <td class="user-agent">${log.userAgent}</td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty logs}">
                        <tr>
                            <td colspan="7" class="no-data">No activity logs found</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>

<jsp:include page="../common/footer.jsp" />

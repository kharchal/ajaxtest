<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<jsp:useBean id="manager" class="com.larditrans.PageBean" scope="request">
    <jsp:setProperty name="manager" property="request" value="${pageContext.request}"/>
    <jsp:setProperty name="manager" property="response" value="${pageContext.response}"/>
</jsp:useBean>
<c:set var="cmd" value="${param.cmd}"/>
<c:choose>
    <c:when test="${cmd eq 'get.table'}">
        ${manager.table()}
    </c:when>
    <c:when test="${cmd eq 'get.edit'}">
        ${manager.edit()}
    </c:when>
    <c:when test="${cmd eq 'edit.save'}">
        ${manager.save()}
    </c:when>
    <c:when test="${cmd eq 'reboot'}">
        ${manager.reboot()}
    </c:when>
</c:choose>


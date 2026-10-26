<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var="signInUrl" value="/sign-in" />
<c:url var="signUpUrl" value="/sign-up" />

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ошибка</title>
    <link rel="stylesheet" href="<c:url value='/css/auth.css'/>">
</head>
<body>

<div class="auth-card">
    <h1 class="error-title">Произошла ошибка</h1>

    <div class="error-msg-box">
        <c:choose>
            <c:when test="${not empty param.msg}">
                <c:out value="${param.msg}" />
            </c:when>
            <c:otherwise>
                Что-то пошло не так. Попробуйте еще раз.
            </c:otherwise>
        </c:choose>
    </div>

    <div class="error-actions">
        <a href="javascript:history.back()" class="btn-secondary">Назад</a>
        <a href="${signInUrl}" class="btn-primary">На главную входа</a>
    </div>
</div>

</body>
</html>
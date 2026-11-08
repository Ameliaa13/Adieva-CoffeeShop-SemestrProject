<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:url var="signInUrl" value="/sign-in"/>
<c:url var="signUpUrl" value="/sign-up"/>
<c:url var="homeUrl" value="/"/>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход</title>

    <link rel="stylesheet" href="<c:url value='/css/auth.css'/>">
</head>
<body>

<div class="auth-card">
    <h1>Вход</h1>

    <c:if test="${not empty error}">
        <div class="error-msg">
            <c:out value="${error}"/>
        </div>
    </c:if>

    <c:if test="${not empty param.msg}">
        <div class="success-msg">
            <c:out value="${param.msg}"/>
        </div>
    </c:if>

    <form method="post" action="${signInUrl}">
        <div class="form-group">
            <label for="email">Логин</label>
            <input type="text"
                   id="email"
                   name="email"
                   value="<c:out value='${email}'/>"
                   placeholder="Введите логин"
                   required>
        </div>

        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password"
                   id="password"
                   name="password"
                   placeholder="Введите пароль"
                   required>
        </div>

        <button type="submit">Войти</button>
    </form>

    <div class="link-text">
        Нет аккаунта? <a href="${signUpUrl}">Зарегистрироваться</a>
    </div>

    <a class="btn-secondary" href="${homeUrl}">
        Вернуться на главную
    </a>
</div>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var = "signInUrl" value="/sign-in" />
<c:url var="signUpUrl" value="/sign-up" />

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale = 1.0">
    <title>Sign in</title>

    <link rel="stylesheet" href="<c:url value='/css/auth.css'/>">
</head>

<body>

<div class="auth-card">
    <h1>Вход</h1>


    <c:if test="${not empty error}">
        <div class="error-msg">
            <c:out value="${error}" />
        </div>
    </c:if>


    <form method="post" action="${signInUrl}">
        <div class="form-group">
            <label for="username">Логин</label>

            <input type="text" id="username" name="username"
                value="<c:out value='${username}'/>"
                placeholder="Введите логин" required>

        </div>

        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" id="password" name="password"
                placeholder="Введите пароль" required>
        </div>

        <button type="submit">Войти</button>
    </form>

    <div class="link-text">Нет аккаунта? <a href="${signUpUrl}">Зарегистрироваться</a> </div>

</div>


</body>
</html>

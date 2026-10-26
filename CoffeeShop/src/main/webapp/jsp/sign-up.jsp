
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var="signUpUrl" value="/sign-up"/>
<c:url var="signInUrl" value="/sign-in" />

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale = 1.0">
    <title>Sign Up</title>

    <link rel = "stylesheet" href="<c:url value='/css/auth.css'/>">

</head>
<body>

<div class="auth-card">
    <h1>Регистрация</h1>

    <c:if test="${not empty error}">
        <div class="error-msg">
            <c:out value="${error}" />
        </div>

    </c:if>

    <c:if test="${not empty param.msg}">
        <div class="success-msg" style="color: green; background-color: #e6ffe6; padding: 10px; border-radius: 5px; margin-bottom: 15px; text-align: center;">
            <c:out value="${param.msg}" />
        </div>
    </c:if>

    <form method="post" action="${signUpUrl}">
        <div class="form-group">
            <label for="email">Почта</label>
            <input type="text" id="email" name="email"
                value="<c:out value='${email}'/>"
                placeholder="Введите почту" required>
        </div>

        <div class="form-group">
            <label for="firstName">Имя</label>
            <input type="text" id="firstName" name="first_name"
            value="<c:out value='${firstName}'/>"
            placeholder = "Введите имя" required>

        </div>

        <div class="form-group">
            <label for="lastName">Фамилия</label>
            <input type="text" id="lastName" name="last_name"
                   value="<c:out value='${lastName}'/>"
                   placeholder = "Введите фамилию" required>
        </div>

        <div class="form-group">
            <label for="birthDate">Дата рождения</label>
            <input type="date" id="birthDate" name="birth_date"
                   value="<c:out value='${birthDate}'/>" required>

        </div>

        <div class="form-group">
            <label for="password">Пароль</label>
            <input type="password" id="password" name="password"
                placeholder="Введите пароль" required>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Повторите пароль</label>
            <input type="password" id="confirmPassword" name="confirmPassword"
                   placeholder="Повторите пароль" required>
        </div>

        <button type="submit">Зарегистрироваться</button>
    </form>

    <div class="link-text">Уже есть аккаунт? <a href="${signInUrl}">Войти</a></div>
</div>


</body>
</html>

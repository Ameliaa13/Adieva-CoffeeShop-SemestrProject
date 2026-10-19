
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var = "signInUrl" value="/sign-in" />
<c:url var="signUpUrl" value="/sign-up" />

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale = 1.0">
    <title>Sign in</title>

    <style>
        body{
            font-family: Arial, sans-serif;
            background: #c5afaf;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .auth-card{
            background: #ffffff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            width: 300px;
        }
        h1{
            text-align: center;
            color: #333;
            font-size: 24px;
        }
        .form-group{
            margin-bottom: 15px;
        }
        label{
            display: block;
            margin-bottom: 5px;
            color: #666;
            font-size: 14px;
        }
        input{
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc; border-radius: 4px;
            box-sizing: border-box;
        }
        button{
            width: 100%;
            padding: 10px;
            background: #6f4e37; color: white;
            border: none; border-radius: 4px;
            cursor: pointer;
            font-size: 16px; margin-top: 10px;
        }
        button:hover{
            background: #5a3d2b;
        }
        .error-msg{
            color: #d9534f; background: #f2dede;
            border: 1px solid #ebccd1; padding: 10px;
            border-radius: 4px; margin-bottom: 15px;
            font-size: 14px; text-align: center;
        }
        .link-text { text-align: center; margin-top: 15px; font-size: 13px; }
        .link-text a { color: #6f4e37; text-decoration: none; }

    </style>
</head>

<body>

<div class="auth-card">
    <h1>Вход</h1>

    <%-- БЛОК ОШИБКИ (JSTL) --%>
    <c:if test="${not empty error}">
        <div class="error-msg">
            <c:out value="${error}" />
        </div>
    </c:if>

    <%-- ФОРМА --%>
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<header class = "site-header">
    <div class="header-inner">

        <a href="<c:url value='/'/>" class="logo">
            <span class="logo-name">НАЗВАНИЕ</span>
            <span class="logo-subtitle">КОФЕ • КНИГИ • КИНО</span>
        </a>

        <nav class="main-nav">
            <a href="<c:url value='/'/>">Главная</a>
            <a href="<c:url value='/menu'/>">Меню</a>
            <a href="<c:url value='/events'/>">Мероприятия</a>
            <a href="<c:url value='/about'/>">О нас</a>
        </nav>

        <div class="header-actions">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <a href="<c:url value='/profile'/>" class="header-link">Профиль</a>
                </c:when>

                <c:otherwise>
                    <a href="<c:url value='/sign-in'/>" class="login-button">Войти</a>
                </c:otherwise>
            </c:choose>
            <a href="<c:url value='/cart'/>" class="cart-link">Корзина</a>

        </div>

    </div>

</header>
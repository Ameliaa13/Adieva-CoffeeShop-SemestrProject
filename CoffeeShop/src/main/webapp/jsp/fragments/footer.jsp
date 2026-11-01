<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<footer class="site-footer">

    <div class="footer-inner">
        <div class="footer-brand">
            <a href="<c:url value='/'/>" class="logo footer-logo">
                <span class="logo-name">НАЗВАНИЕ</span>
                <span class="logo-subtitle">КОФЕ • КНИГИ • КИНО</span>
            </a>

            <p>
                Больше, чем просто кофе.
                Место, где встречаются любимые истории,
                уютные вечера и новые знакомства.
            </p>
        </div>

        <div class="footer-column">
            <h3>Навигация</h3>

            <a href="<c:url value='/'/>">Главная</a>
            <a href="<c:url value='/menu'/>">Меню</a>
            <a href="<c:url value='/events'/>">Мероприятия</a>
            <a href="<c:url value='/about'/>">О нас</a>
        </div>

        <div class="footer-column">
            <h3>Контакты</h3>

            <p>Казань, укажи адрес кофейни</p>
            <p>Ежедневно: 09:00–22:00</p>
            <p>Телефон: укажи номер</p>
        </div>

    </div>

    <div class="footer-bottom">
        <span>© 2026 «Кофейня»</span>
        <span>Место, где истории оживают.</span>
    </div>
</footer>
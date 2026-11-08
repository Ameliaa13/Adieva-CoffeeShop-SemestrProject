<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<footer class="site-footer">

    <div class="footer-inner">
        <div class="footer-brand">
            <a href="<c:url value='/'/>" class="logo footer-logo">
                <span class="logo-name">A-melia-n Beans</span>
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
        </div>

        <div class="footer-column">
            <h3>Контакты</h3>

            <p>Казань, ул. Кремлевская,35</p>
            <p>Ежедневно: 08:00–23:00</p>
            <p>Телефон: 8-800-555-35-35</p>
        </div>

    </div>

    <div class="footer-bottom">
        <span>© 2026 «Кофейня»</span>
        <span>Место, где истории оживают.</span>
    </div>
</footer>
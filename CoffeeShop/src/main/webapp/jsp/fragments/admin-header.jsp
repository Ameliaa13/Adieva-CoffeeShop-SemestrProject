<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<header class = "site-header">
  <div class="header-inner">

    <a href="<c:url value='/'/>" class="logo">
      <img src="<c:url value='/img/logo.png'/>" alt="Amelia-n Beans" class="logo-image">
    </a>
    <a href="<c:url value='/'/>" class="logo">
      <span class="logo-name">A-melia-n Beans</span>
      <span class="logo-subtitle">КОФЕ • КНИГИ • КИНО</span>
    </a>

    <nav class="main-nav">
      <a href="<c:url value='/'/>">Главная</a>
      <a href="<c:url value='/menu'/>">Меню</a>
      <a href="<c:url value='/promotions'/>">Акции</a>
      <a href="<c:url value='/events'/>">Мероприятия</a>
      <a href="<c:url value='/admin'/>">Панель Админа</a>
    </nav>

    <div class="header-actions">
      <c:choose>
        <c:when test="${not empty sessionScope.user}">
          <a href="<c:url value='/profile'/>" class="header-link">
            Профиль
          </a>

          <form action="<c:url value='/logout'/>"
                method="post"
                class="logout-form">
            <button type="submit" class="header-link logout-button">
              Выйти
            </button>
          </form>
        </c:when>

        <c:otherwise>
          <a href="<c:url value='/sign-in'/>" class="login-button">
            Профиль
          </a>
        </c:otherwise>
      </c:choose>

    </div>

  </div>

</header>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Панель администратора</title>

  <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/admin-header.jsp"/>

<main class="admin-home">
  <section class="admin-home-intro">
    <p class="admin-eyebrow">Управление кофейней</p>

    <h1>
      Панель администратора
      <c:if test="${not empty sessionScope.user.firstName}">
        <span>, <c:out value="${sessionScope.user.firstName}"/></span>
      </c:if>
    </h1>

    <p class="admin-home-description">
      Здесь можно управлять товарами меню, категориями, мероприятиями и акциями.
    </p>
  </section>

  <section class="admin-dashboard" aria-label="Разделы панели администратора">

    <a class="admin-dashboard-card" href="<c:url value='/admin/menu'/>">
      <span class="admin-dashboard-icon" aria-hidden="true">☕</span>
      <span class="admin-dashboard-title">Меню</span>
      <span class="admin-dashboard-description">
                Добавляй товары, изменяй цены, описание и КБЖУ.
            </span>
      <span class="admin-dashboard-link">Управлять меню →</span>
    </a>

    <a class="admin-dashboard-card" href="<c:url value='/admin/categories'/>">
      <span class="admin-dashboard-icon" aria-hidden="true">▤</span>
      <span class="admin-dashboard-title">Категории</span>
      <span class="admin-dashboard-description">
                Создавай категории и меняй их названия.
            </span>
      <span class="admin-dashboard-link">Управлять категориями →</span>
    </a>

    <a class="admin-dashboard-card" href="<c:url value='/admin/events'/>">
      <span class="admin-dashboard-icon" aria-hidden="true">♬</span>
      <span class="admin-dashboard-title">Мероприятия</span>
      <span class="admin-dashboard-description">
                Создавай события, задавай дату, статус и число мест.
            </span>
      <span class="admin-dashboard-link">Управлять мероприятиями →</span>
    </a>

    <a class="admin-dashboard-card" href="<c:url value='/admin/promotions'/>">
      <span class="admin-dashboard-icon" aria-hidden="true">%</span>
      <span class="admin-dashboard-title">Акции</span>
      <span class="admin-dashboard-description">
                Добавляй акции и указывай период их проведения.
            </span>
      <span class="admin-dashboard-link">Управлять акциями →</span>
    </a>

  </section>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
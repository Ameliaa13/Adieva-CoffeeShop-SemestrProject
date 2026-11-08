<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Заказ оформлен</title>

  <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/cart.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="order-complete-page">
  <section class="order-complete-card">
    <p class="order-complete-icon" aria-hidden="true">☕</p>

    <p class="order-complete-eyebrow">Заказ принят</p>

    <h1>Спасибо за заказ!</h1>

    <p class="order-complete-number">
      Номер заказа:
      <strong>№<c:out value="${order.id}"/></strong>
    </p>

    <p class="order-complete-date">
      <c:out value="${order.formattedCreatedAt}"/>
    </p>

    <div class="order-complete-items">
      <c:forEach var="item" items="${order.items}">
        <div class="order-complete-item">
                    <span>
                        <c:out value="${item.itemName}"/> ×
                        <c:out value="${item.quantity}"/>
                    </span>

          <strong>
            <fmt:formatNumber
                    value="${item.lineTotal}"
                    minFractionDigits="2"
                    maxFractionDigits="2"/> ₽
          </strong>
        </div>
      </c:forEach>
    </div>

    <p class="order-complete-total">
      Итого:
      <strong>
        <fmt:formatNumber
                value="${order.totalPrice}"
                minFractionDigits="2"
                maxFractionDigits="2"/> ₽
      </strong>
    </p>

    <div class="order-complete-actions">
      <a class="cart-button" href="<c:url value='/menu'/>">
        Вернуться в меню
      </a>

      <a class="cart-button cart-button-secondary"
         href="<c:url value='/profile'/>">
        История заказов
      </a>
    </div>
  </section>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Корзина</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/cart.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="cart-page">
    <h1>Корзина</h1>

    <c:if test="${param.checkout eq 'empty'}">
        <p class="cart-checkout-message">
            Корзина пуста — сначала добавь товары.
        </p>
    </c:if>

    <c:if test="${param.checkout eq 'error'}">
        <p class="cart-checkout-message cart-checkout-error">
            Не удалось оформить заказ. Товары остались в корзине, попробуй ещё раз.
        </p>
    </c:if>

    <c:choose>
        <c:when test="${empty cartItems}">
            <section class="cart-empty">
                <p class="cart-empty-icon">☕</p>
                <h2>Корзина пока пуста</h2>
                <p>Выбери что-нибудь вкусное в нашем меню.</p>
                <a class="cart-button" href="<c:url value='/menu'/>">
                    Перейти в меню
                </a>
            </section>
        </c:when>

        <c:otherwise>
            <section class="cart-list">
                <c:set var="total" value="0"/>

                <c:forEach var="cartItem" items="${cartItems}">
                    <c:set
                            var="itemTotal"
                            value="${cartItem.menuItemModel.price * cartItem.quantity}"/>
                    <c:set var="total" value="${total + itemTotal}"/>

                    <article class="cart-item">
                        <a class="cart-item-image"
                           href="<c:url value='/menu/item'>
                                    <c:param name='id' value='${cartItem.menuItemId}'/>
                                 </c:url>">
                            <c:choose>
                                <c:when test="${not empty cartItem.menuItemModel.imageUrl}">
                                    <img
                                            src="<c:url value='${cartItem.menuItemModel.imageUrl}'/>"
                                            alt="<c:out value='${cartItem.menuItemModel.name}'/>">
                                </c:when>
                                <c:otherwise>
                                    <span>Фото скоро появится</span>
                                </c:otherwise>
                            </c:choose>
                        </a>

                        <div class="cart-item-info">
                            <h2>
                                <a href="<c:url value='/menu/item'>
                                            <c:param name='id' value='${cartItem.menuItemId}'/>
                                         </c:url>">
                                    <c:out value="${cartItem.menuItemModel.name}"/>
                                </a>
                            </h2>

                            <p class="cart-item-price">
                                <fmt:formatNumber
                                        value="${cartItem.menuItemModel.price}"
                                        minFractionDigits="2"
                                        maxFractionDigits="2"/> ₽ за шт.
                            </p>

                            <p class="cart-item-total">
                                Сумма:
                                <fmt:formatNumber
                                        value="${itemTotal}"
                                        minFractionDigits="2"
                                        maxFractionDigits="2"/> ₽
                            </p>
                        </div>

                        <form class="cart-quantity-form"
                              action="<c:url value='/cart'/>"
                              method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="itemId"
                                   value="${cartItem.menuItemId}">

                            <label for="quantity-${cartItem.menuItemId}">
                                Количество
                            </label>
                            <input
                                    id="quantity-${cartItem.menuItemId}"
                                    type="number"
                                    name="quantity"
                                    min="1"
                                    value="${cartItem.quantity}"
                                    required>

                            <button class="cart-button cart-button-secondary"
                                    type="submit">
                                Обновить
                            </button>
                        </form>

                        <form action="<c:url value='/cart'/>" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="itemId"
                                   value="${cartItem.menuItemId}">

                            <button class="cart-remove-button" type="submit">
                                Удалить
                            </button>
                        </form>
                    </article>
                </c:forEach>
            </section>

            <section class="cart-summary">
                <p>
                    Итого:
                    <strong>
                        <fmt:formatNumber
                                value="${total}"
                                minFractionDigits="2"
                                maxFractionDigits="2"/> ₽
                    </strong>
                </p>

                <div class="cart-summary-actions">
                    <a class="cart-button" href="<c:url value='/menu'/>">
                        Продолжить выбор
                    </a>

                    <form action="<c:url value='/cart'/>" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        <input type="hidden" name="action" value="clear">
                        <button class="cart-button cart-button-secondary"
                                type="submit">
                            Очистить корзину
                        </button>
                    </form>

                    <form action="<c:url value='/checkout'/>" method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                        <button class="cart-button checkout-button" type="submit">
                            Оформить заказ
                        </button>
                    </form>

                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
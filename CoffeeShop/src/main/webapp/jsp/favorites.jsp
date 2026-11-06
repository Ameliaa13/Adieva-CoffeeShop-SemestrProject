<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Избранное</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/favorites.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="favorites-page">
    <header class="favorites-heading">
        <p class="favorites-eyebrow">Твои любимые позиции</p>
        <h1>Избранное</h1>
        <p>Сохрани напитки и десерты, которые захочется заказать снова.</p>
    </header>

    <c:choose>
        <c:when test="${empty favorites}">
            <section class="favorites-empty">
                <div class="favorites-empty-icon">♡</div>
                <h2>Здесь пока пусто</h2>
                <p>
                    Добавь понравившийся товар в избранное —
                    и он появится на этой странице.
                </p>

                <a class="favorites-button" href="<c:url value='/menu'/>">
                    Перейти в меню
                </a>
            </section>
        </c:when>

        <c:otherwise>
            <section class="favorites-grid">
                <c:forEach var="favorite" items="${favorites}">
                    <c:url var="itemUrl" value="/menu/item">
                        <c:param name="id" value="${favorite.menuItemId}"/>
                    </c:url>

                    <article class="favorite-card">
                        <a class="favorite-card-image-link" href="${itemUrl}">
                            <c:choose>
                                <c:when test="${not empty favorite.menuItemModel.imageUrl}">
                                    <img
                                            class="favorite-card-image"
                                            src="<c:url value='${favorite.menuItemModel.imageUrl}'/>"
                                            alt="<c:out value='${favorite.menuItemModel.name}'/>">
                                </c:when>
                                <c:otherwise>
                                    <div class="favorite-card-image-placeholder">
                                        Фото скоро появится
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </a>

                        <div class="favorite-card-info">
                            <h2>
                                <a href="${itemUrl}">
                                    <c:out value="${favorite.menuItemModel.name}"/>
                                </a>
                            </h2>

                            <p class="favorite-card-price">
                                <fmt:formatNumber
                                        value="${favorite.menuItemModel.price}"
                                        minFractionDigits="2"
                                        maxFractionDigits="2"/> ₽
                            </p>

                            <div class="favorite-card-actions">
                                <form class="favorite-toggle-form"
                                      action="<c:url value='/favorites'/>"
                                      method="post">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden"
                                           name="itemId"
                                           value="${favorite.menuItemId}">

                                    <button class="favorite-toggle-button"
                                            type="submit"
                                            title="Убрать из избранного"
                                            aria-label="Убрать из избранного">
                                        ♥
                                    </button>
                                </form>

                                <form class="favorite-cart-form"
                                      action="<c:url value='/cart'/>"
                                      method="post">
                                    <input type="hidden" name="action" value="add">
                                    <input type="hidden"
                                           name="itemId"
                                           value="${favorite.menuItemId}">
                                    <input type="hidden"
                                           name="returnTo"
                                           value="favorites">

                                    <button class="add-to-cart-button"
                                            type="submit">
                                        Добавить в корзину
                                    </button>
                                </form>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </section>
        </c:otherwise>
    </c:choose>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
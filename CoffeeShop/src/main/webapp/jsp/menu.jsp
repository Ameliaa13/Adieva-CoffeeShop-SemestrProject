<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Меню кофейни</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="menu-container">
    <h1>Наше меню</h1>

    <c:forEach var="category" items="${categories}">
        <section class="category-section">
            <h2 class="category-title">
                <c:out value="${category.name}"/>
            </h2>

            <div class="menu-grid">
                <c:set var="hasItems" value="false"/>

                <c:forEach var="item" items="${menuItems}">
                    <c:if test="${item.categoryId == category.id}">
                        <c:set var="hasItems" value="true"/>

                        <c:url var="itemUrl" value="/menu/item">
                            <c:param name="id" value="${item.id}"/>
                        </c:url>

                        <article class="menu-card">
                            <div class="menu-card-image-wrap">
                                <a href="${itemUrl}">
                                    <c:choose>
                                        <c:when test="${not empty item.imageUrl}">
                                            <img
                                                    src="<c:url value='${item.imageUrl}'/>"
                                                    alt="<c:out value='${item.name}'/>"
                                                    class="menu-img">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="menu-img-placeholder">
                                                Фото скоро появится
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </a>

                                <form class="favorite-form"
                                      action="<c:url value='/favorites'/>"
                                      method="post">
                                    <input type="hidden"
                                           name="itemId"
                                           value="${item.id}">
                                    <input type="hidden"
                                           name="returnTo"
                                           value="menu">

                                    <c:choose>
                                        <c:when test="${favoriteItemIds[item.id]}">
                                            <input type="hidden"
                                                   name="action"
                                                   value="delete">
                                            <button class="favorite-button is-favorite"
                                                    type="submit"
                                                    title="Убрать из избранного"
                                                    aria-label="Убрать из избранного">
                                                ♥
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden"
                                                   name="action"
                                                   value="add">
                                            <button class="favorite-button"
                                                    type="submit"
                                                    title="Добавить в избранное"
                                                    aria-label="Добавить в избранное">
                                                ♡
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </div>

                            <a class="menu-card-link" href="${itemUrl}">
                                <h3><c:out value="${item.name}"/></h3>
                            </a>

                            <p class="price">
                                <c:out value="${item.price}"/> ₽
                            </p>

                            <div class="menu-card-cart">
                                <c:choose>
                                    <c:when test="${not empty cartQuantities[item.id]}">
                                        <span class="cart-count">
                                            В корзине: ${cartQuantities[item.id]} шт.
                                        </span>

                                        <div class="quantity-controls">
                                            <form action="<c:url value='/cart'/>"
                                                  method="post">
                                                <input type="hidden"
                                                       name="itemId"
                                                       value="${item.id}">
                                                <input type="hidden"
                                                       name="returnTo"
                                                       value="menu">

                                                <c:choose>
                                                    <c:when test="${cartQuantities[item.id] > 1}">
                                                        <input type="hidden"
                                                               name="action"
                                                               value="update">
                                                        <input type="hidden"
                                                               name="quantity"
                                                               value="${cartQuantities[item.id] - 1}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="hidden"
                                                               name="action"
                                                               value="delete">
                                                    </c:otherwise>
                                                </c:choose>

                                                <button class="quantity-button"
                                                        type="submit"
                                                        aria-label="Уменьшить количество">
                                                    −
                                                </button>
                                            </form>

                                            <span class="quantity-value">
                                                    ${cartQuantities[item.id]}
                                            </span>

                                            <form action="<c:url value='/cart'/>"
                                                  method="post">
                                                <input type="hidden"
                                                       name="action"
                                                       value="add">
                                                <input type="hidden"
                                                       name="itemId"
                                                       value="${item.id}">
                                                <input type="hidden"
                                                       name="returnTo"
                                                       value="menu">

                                                <button class="quantity-button"
                                                        type="submit"
                                                        aria-label="Увеличить количество">
                                                    +
                                                </button>
                                            </form>
                                        </div>
                                    </c:when>

                                    <c:otherwise>
                                        <form action="<c:url value='/cart'/>"
                                              method="post">
                                            <input type="hidden"
                                                   name="action"
                                                   value="add">
                                            <input type="hidden"
                                                   name="itemId"
                                                   value="${item.id}">
                                            <input type="hidden"
                                                   name="returnTo"
                                                   value="menu">

                                            <button class="add-to-cart-button"
                                                    type="submit">
                                                В корзину
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </article>
                    </c:if>
                </c:forEach>

                <c:if test="${not hasItems}">
                    <p class="no-items">В этой категории пока нет товаров.</p>
                </c:if>
            </div>
        </section>
    </c:forEach>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><c:out value="${menuItem.name}"/> — кофейня</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
</head>
<body class="product-page-body">

<c:import url="/jsp/fragments/header.jsp"/>

<main class="product-page">
    <nav class="product-breadcrumb">
        <a href="<c:url value='/menu'/>">Меню</a>
        <span>/</span>
        <c:out value="${menuItem.name}"/>
    </nav>

    <c:choose>
        <c:when test="${not empty menuItem}">
            <article class="product-details">
                <div class="product-image-container">
                    <c:choose>
                        <c:when test="${not empty menuItem.imageUrl}">
                            <img
                                    src="<c:url value='${menuItem.imageUrl}'/>"
                                    alt="<c:out value='${menuItem.name}'/>"
                                    class="product-image">
                        </c:when>
                        <c:otherwise>
                            <div class="product-image-placeholder">
                                Фото скоро появится
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="product-info">
                    <h1><c:out value="${menuItem.name}"/></h1>

                    <p class="product-description">
                        <c:out value="${menuItem.description}"/>
                    </p>

                    <p class="product-price">
                        <fmt:formatNumber
                                value="${menuItem.price}"
                                minFractionDigits="2"
                                maxFractionDigits="2"/> ₽
                    </p>

                    <section class="nutrition">
                        <h2>Пищевая ценность на 100 мл</h2>

                        <div class="nutrition-table-wrapper">
                            <table class="nutrition-table">
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>Ккал</th>
                                    <th>Белки</th>
                                    <th>Жиры</th>
                                    <th>Углеводы</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th>На 100 мл</th>
                                    <td>
                                        <fmt:formatNumber
                                                value="${menuItem.calories}"
                                                minFractionDigits="0"
                                                maxFractionDigits="2"/>
                                    </td>
                                    <td>
                                        <fmt:formatNumber
                                                value="${menuItem.proteins}"
                                                minFractionDigits="0"
                                                maxFractionDigits="2"/> г
                                    </td>
                                    <td>
                                        <fmt:formatNumber
                                                value="${menuItem.fats}"
                                                minFractionDigits="0"
                                                maxFractionDigits="2"/> г
                                    </td>
                                    <td>
                                        <fmt:formatNumber
                                                value="${menuItem.carbohydrates}"
                                                minFractionDigits="0"
                                                maxFractionDigits="2"/> г
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </section>
                </div>
            </article>
        </c:when>

        <c:otherwise>
            <p class="product-not-found">Товар не найден.</p>
        </c:otherwise>
    </c:choose>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
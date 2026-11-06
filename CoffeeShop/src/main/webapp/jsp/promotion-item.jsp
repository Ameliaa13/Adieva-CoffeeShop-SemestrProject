<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${promotion.title}"/> — акция кофейни</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/promotion-item.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="promotion-page">
    <a class="promotion-back" href="<c:url value='/promotions'/>">
        ← Все акции
    </a>

    <article class="promotion-detail">
        <c:if test="${not empty promotion.imageUrl}">
            <div class="promotion-detail-image-wrap">
                <img class="promotion-detail-image"
                     src="<c:url value='${promotion.imageUrl}'/>"
                     alt="<c:out value='${promotion.title}'/>">
            </div>
        </c:if>

        <div class="promotion-detail-content">
            <p class="promotion-detail-eyebrow">Специальное предложение</p>

            <h1><c:out value="${promotion.title}"/></h1>

            <c:if test="${not empty promotion.shortDescription}">
                <p class="promotion-detail-short">
                    <c:out value="${promotion.shortDescription}"/>
                </p>
            </c:if>

            <div class="promotion-detail-description">
                <c:out value="${promotion.description}"/>
            </div>

            <p class="promotion-detail-period">
                Акция действует с
                <strong><c:out value="${promotion.startDate}"/></strong>
                по
                <strong><c:out value="${promotion.endDate}"/></strong>
            </p>

            <a class="promotion-detail-button" href="<c:url value='/menu'/>">
                Перейти в меню
            </a>
        </div>
    </article>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Акции кофейни</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/promotions.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="promotions-page">
    <header class="promotions-heading">
        <p class="promotions-eyebrow">Специальные предложения</p>
        <h1>Наши акции</h1>
        <p>Заглядывай за кофе и не пропускай приятные предложения.</p>
    </header>

    <c:choose>
        <c:when test="${empty promotions}">
            <p class="promotions-empty">
                Сейчас активных акций нет. Загляни сюда немного позже!
            </p>
        </c:when>

        <c:otherwise>
            <div class="promotion-list">
                <c:forEach var="promotion" items="${promotions}">
                    <c:url var="promotionUrl" value="/promotion">
                        <c:param name="id" value="${promotion.id}"/>
                    </c:url>

                    <a class="promotion-card" href="${promotionUrl}">
                        <c:if test="${not empty promotion.imageUrl}">
                            <img class="promotion-image"
                                 src="<c:url value='${promotion.imageUrl}'/>"
                                 alt="">
                        </c:if>

                        <span class="promotion-overlay" aria-hidden="true"></span>

                        <div class="promotion-info">
                            <p class="promotion-label">Акция</p>

                            <h2>
                                <c:out value="${promotion.title}"/>
                            </h2>

                            <p class="promotion-description">
                                <c:out value="${promotion.shortDescription}"/>
                            </p>

                            <span class="promotion-more">Подробнее →</span>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
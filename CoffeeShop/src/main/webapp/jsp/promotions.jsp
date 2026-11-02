<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Акции кофейни</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/events.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/header.jsp"/>

<main class="events-page">
    <header class="events-heading">
        <p class="events-eyebrow">Специальные предложения</p>
        <h1>Наши акции</h1>
        <p>Заглядывай за кофе и не пропускай приятные предложения.</p>
    </header>

    <c:choose>
        <c:when test="${empty promotions}">
            <p class="events-empty">
                Сейчас активных акций нет. Загляните сюда немного позже!
            </p>
        </c:when>

        <c:otherwise>
            <div class="event-list">
                <c:forEach var="promotion" items="${promotions}">
                    <c:url var="promotionUrl" value="/promotion">
                        <c:param name="id" value="${promotion.id}"/>
                    </c:url>

                    <a class="event-card" href="${promotionUrl}">
                        <div class="event-image-container">
                            <c:choose>
                                <c:when test="${not empty promotion.imageUrl}">
                                    <img
                                            src="<c:url value='${promotion.imageUrl}'/>"
                                            alt="<c:out value='${promotion.title}'/>"
                                            class="event-image">
                                </c:when>

                                <c:otherwise>
                                    <div class="event-image-placeholder">
                                        Фото акции
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="event-info">
                            <p class="event-eyebrow">Акция</p>

                            <h2><c:out value="${promotion.title}"/></h2>

                            <p class="event-description">
                                <c:out value="${promotion.shortDescription}"/>
                            </p>

                            <p class="promotion-more">Подробнее →</p>
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
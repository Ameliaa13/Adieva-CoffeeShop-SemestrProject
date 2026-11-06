
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Мероприятия кофейни</title>
    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/events.css'/>">
</head>
<body>
<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="events-page">
    <header class="events-heading">
        <p class="events-eyebrow">Кофе · книги · кино</p>
        <h1>Мероприятия</h1>
        <p>Встречаемся в кофейне за чашкой кофе и хорошей историей.</p>
    </header>

    <c:choose>
        <c:when test="${empty events}">
            <p class="events-empty">
                Пока нет запланированных мероприятий. Загляните сюда позже!
            </p>
        </c:when>

        <c:otherwise>
            <div class="event-list">
                <c:forEach var="event" items="${events}">

                    <c:url var="eventUrl" value="/event">
                        <c:param name="id" value="${event.id}"/>
                    </c:url>

                    <a class="event-card" href="${eventUrl}">
                        <div class="event-image-container">
                            <c:choose>
                                <c:when test="${not empty event.imageUrl}">
                                    <img
                                        src="<c:url value='${event.imageUrl}'/>"
                                        alt = "<c:out value='${event.title}'/>"
                                        class="event-image">
                                </c:when>
                                <c:otherwise>
                                    <div class="event-image-placeholder">
                                        Фото мероприятия
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="event-info">
                            <p class="event-eyebrow">Событие в кофейне</p>
                            <h2><c:out value="${event.title}"/> </h2>

                            <div class="event-details">
                                <p>
                                    <span>Дата</span>
                                    <strong><c:out value="${event.formattedDate}"/> </strong>
                                </p>
                                <p>
                                    <span>Начало</span>
                                    <strong><c:out value="${event.formattedTime}"/></strong>
                                </p>

                                <c:if test="${not empty event.durationMinutes}">
                                    <p>
                                        <span>Длительность</span>
                                        <strong>
                                            <c:out value="${event.durationMinutes}"/> мин.
                                        </strong>
                                    </p>
                                </c:if>

                                <p>
                                    <span>Мест</span>
                                    <strong>
                                        До <c:out value="${event.capacity}"/> участников
                                    </strong>
                                </p>
                            </div>


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

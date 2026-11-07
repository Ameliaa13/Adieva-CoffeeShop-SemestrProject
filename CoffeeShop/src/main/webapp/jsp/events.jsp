<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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

                    <%-- Дополнительная проверка на JSP-уровне.
                         Сервлет тоже должен отфильтровывать секретные события для гостей. --%>
                    <c:if test="${event.visibility ne 'SECRET' or not empty sessionScope.user}">

                        <c:choose>
                            <c:when test="${event.visibility eq 'SECRET'}">
                                <c:url var="eventUrl" value="/secret-event">
                                    <c:param name="id" value="${event.id}"/>
                                </c:url>

                                <a class="event-card event-card-secret"
                                   href="${eventUrl}"
                                   aria-label="Открыть загадку секретного мероприятия">

                                    <div class="event-image-container">
                                        <div class="event-secret-placeholder"
                                             aria-hidden="true">?</div>
                                    </div>

                                    <div class="event-info">
                                        <p class="event-eyebrow">
                                            Секретное мероприятие
                                        </p>

                                        <h2>TOP SECRET</h2>

                                        <p class="event-secret-description">
                                            Открой загадку и попробуй угадать тему.
                                        </p>

                                        <div class="event-details">
                                            <p>
                                                <span>Дата</span>
                                                <strong>
                                                    <c:out value="${event.formattedDate}"/>
                                                </strong>
                                            </p>

                                            <p>
                                                <span>Начало</span>
                                                <strong>
                                                    <c:out value="${event.formattedTime}"/>
                                                </strong>
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
                            </c:when>

                            <c:otherwise>
                                <c:url var="eventUrl" value="/event">
                                    <c:param name="id" value="${event.id}"/>
                                </c:url>

                                <a class="event-card" href="${eventUrl}">
                                    <div class="event-image-container">
                                        <c:choose>
                                            <c:when test="${not empty event.imageUrl}">
                                                <img
                                                        src="<c:url value='${event.imageUrl}'/>"
                                                        alt="<c:out value='${event.title}'/>"
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
                                        <p class="event-eyebrow">
                                            Событие в кофейне
                                        </p>

                                        <h2><c:out value="${event.title}"/></h2>

                                        <div class="event-details">
                                            <p>
                                                <span>Дата</span>
                                                <strong>
                                                    <c:out value="${event.formattedDate}"/>
                                                </strong>
                                            </p>

                                            <p>
                                                <span>Начало</span>
                                                <strong>
                                                    <c:out value="${event.formattedTime}"/>
                                                </strong>
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
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
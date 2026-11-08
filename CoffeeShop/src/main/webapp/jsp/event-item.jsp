<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><c:out value="${event.title}"/> — мероприятие кофейни</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/event-item.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="event-detail-page">
    <nav class="event-breadcrumb" aria-label="Навигация">
        <a href="<c:url value='/'/>">Главная</a>
        <span aria-hidden="true">/</span>
        <a href="<c:url value='/events'/>">Мероприятия</a>
        <span aria-hidden="true">/</span>
        <span><c:out value="${event.title}"/></span>
    </nav>

    <article class="event-detail-card">
        <div class="event-detail-image-wrap">
            <c:choose>
                <c:when test="${not empty event.imageUrl}">
                    <img class="event-detail-image"
                         src="<c:url value='${event.imageUrl}'/>"
                         alt="<c:out value='${event.title}'/>">
                </c:when>

                <c:otherwise>
                    <div class="event-detail-image-placeholder">
                        <span aria-hidden="true">☕</span>
                        <span>Фото мероприятия</span>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="event-detail-content">
            <p class="event-detail-eyebrow">Событие в кофейне</p>

            <h1><c:out value="${event.title}"/></h1>

            <div class="event-detail-facts">
                <div class="event-fact">
                    <span class="event-fact-label">Дата</span>
                    <strong><c:out value="${event.formattedDate}"/></strong>
                </div>

                <div class="event-fact">
                    <span class="event-fact-label">Начало</span>
                    <strong><c:out value="${event.formattedTime}"/></strong>
                </div>

                <c:if test="${not empty event.durationMinutes}">
                    <div class="event-fact">
                        <span class="event-fact-label">Длительность</span>
                        <strong><c:out value="${event.durationMinutes}"/> мин.</strong>
                    </div>
                </c:if>

                <div class="event-fact">
                    <span class="event-fact-label">Свободные места</span>
                    <strong>
                        <c:out value="${freePlaces}"/>
                        <c:choose>
                            <c:when test="${freePlaces == 1}">место</c:when>
                            <c:when test="${freePlaces >= 2 and freePlaces <= 4}">места</c:when>
                            <c:otherwise>мест</c:otherwise>
                        </c:choose>
                    </strong>
                </div>
            </div>

            <section class="event-detail-description">
                <h2>О мероприятии</h2>

                <p>
                    <c:out value="${event.description}"/>
                </p>
            </section>
            <c:if test="${not empty registrationMessage}">
                <div class="registration-message" role="status">
                    <c:out value="${registrationMessage}"/>
                </div>
            </c:if>

            <div class="event-registration">
                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <p class="event-registration-note">
                            Чтобы записаться, <a href="<c:url value='/sign-in'/>">войдите в аккаунт</a>.
                        </p>
                    </c:when>

                    <c:when test="${registered}">
                        <p class="event-registration-note">
                            Ты уже записана на это мероприятие.
                        </p>

                        <form action="<c:url value='/event'/>" method="post">
                            <input type="hidden" name="id" value="<c:out value='${event.id}'/>">
                            <input type="hidden" name="action" value="cancel">
                            <button class="event-cancel-button" type="submit">
                                Отменить запись
                            </button>
                        </form>
                    </c:when>

                    <c:when test="${canRegister}">
                        <form action="<c:url value='/event'/>" method="post">
                            <input type="hidden" name="id" value="<c:out value='${event.id}'/>">
                            <input type="hidden" name="action" value="register">
                            <button class="event-detail-button" type="submit">
                                Записаться на мероприятие
                            </button>
                        </form>
                    </c:when>

                    <c:otherwise>
                        <p class="event-registration-note">
                            Запись закрыта или свободных мест нет.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>

            <a class="event-detail-button" href="<c:url value='/events'/>">
                Вернуться к афише
            </a>
        </div>
    </article>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
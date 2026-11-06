<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Мой профиль</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/profile.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="profile-page">
    <header class="profile-heading">
        <p class="profile-eyebrow">Личный кабинет</p>
        <h1>Мой профиль</h1>
        <p>Здесь можно обновить свои данные и посмотреть мероприятия, на которые ты записалась.</p>
    </header>

    <c:if test="${not empty errorMessage}">
        <div class="profile-message profile-message-error" role="alert">
            <c:out value="${errorMessage}"/>
        </div>
    </c:if>

    <c:if test="${param.updated eq 'true'}">
        <div class="profile-message profile-message-success" role="status">
            Данные профиля обновлены.
        </div>
    </c:if>

    <section class="profile-panel">
        <h2>Личные данные</h2>

        <form class="profile-form" action="<c:url value='/profile'/>" method="post">
            <label class="profile-field">
                <span>Имя</span>
                <input
                        type="text"
                        name="firstName"
                        maxlength="50"
                        required
                        value="<c:out value='${not empty param.firstName ? param.firstName : profile.firstName}'/>">
            </label>

            <label class="profile-field">
                <span>Фамилия</span>
                <input
                        type="text"
                        name="lastName"
                        maxlength="50"
                        required
                        value="<c:out value='${not empty param.lastName ? param.lastName : profile.lastName}'/>">
            </label>

            <label class="profile-field">
                <span>Email</span>
                <input
                        type="email"
                        name="email"
                        maxlength="50"
                        required
                        value="<c:out value='${not empty param.email ? param.email : profile.email}'/>">
            </label>

            <label class="profile-field">
                <span>Дата рождения</span>
                <input
                        type="date"
                        name="birthDate"
                        required
                        value="<c:out value='${not empty param.birthDate ? param.birthDate : profile.birthDate}'/>">
            </label>

            <button class="profile-submit" type="submit">Сохранить изменения</button>
        </form>
    </section>

    <section class="profile-panel profile-events">
        <div class="profile-section-heading">
            <div>
                <p class="profile-eyebrow">Твои планы</p>
                <h2>Мероприятия, на которые ты записана</h2>
            </div>
            <a class="profile-secondary-link" href="<c:url value='/events'/>">
                Посмотреть все мероприятия
            </a>
        </div>

        <c:choose>
            <c:when test="${empty registeredEvents}">
                <p class="profile-empty">
                    Пока нет записей на мероприятия.
                </p>
            </c:when>

            <c:otherwise>
                <div class="registered-event-list">
                    <c:forEach var="event" items="${registeredEvents}">
                        <c:url var="eventUrl" value="/event">
                            <c:param name="id" value="${event.id}"/>
                        </c:url>

                        <a class="registered-event-card" href="${eventUrl}">
                            <c:choose>
                                <c:when test="${not empty event.imageUrl}">
                                    <img
                                            class="registered-event-image"
                                            src="<c:url value='${event.imageUrl}'/>"
                                            alt="<c:out value='${event.title}'/>">
                                </c:when>
                                <c:otherwise>
                                    <div class="registered-event-placeholder">☕</div>
                                </c:otherwise>
                            </c:choose>

                            <span class="registered-event-info">
                                <strong><c:out value="${event.title}"/></strong>
                                <span>
                                    <c:out value="${event.formattedDate}"/>
                                    ·
                                    <c:out value="${event.formattedTime}"/>
                                </span>
                                <span class="registered-event-action">Подробнее →</span>
                            </span>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
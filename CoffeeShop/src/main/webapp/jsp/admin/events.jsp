<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление мероприятиями</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/admin-header.jsp"/>

<main class="admin-page">
    <div class="admin-page-heading">
        <div>
            <p class="admin-eyebrow">Панель администратора</p>
            <h1>Управление мероприятиями</h1>
        </div>

        <a class="admin-back-link" href="<c:url value='/admin'/>">
            ← Вернуться в панель
        </a>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="admin-message admin-message-error">
            <c:out value="${errorMessage}"/>
        </div>
    </c:if>

    <c:set var="selectedVisibility"
           value="${param.visibility != null
                   ? param.visibility
                   : (not empty editingEvent ? editingEvent.visibility : 'OPEN')}"/>

    <c:set var="selectedStatus"
           value="${param.status != null
                   ? param.status
                   : (not empty editingEvent ? editingEvent.status : 'PLANNED')}"/>

    <section class="admin-panel">
        <h2>
            <c:choose>
                <c:when test="${not empty editingEvent}">
                    Изменить мероприятие
                </c:when>
                <c:otherwise>
                    Добавить мероприятие
                </c:otherwise>
            </c:choose>
        </h2>

        <form action="<c:url value='/admin/events'/>"
              method="post"
              class="admin-form">

            <input type="hidden" name="action" value="save">

            <c:if test="${not empty editingEvent}">
                <input type="hidden" name="id" value="${editingEvent.id}">
            </c:if>

            <label class="admin-field">
                <span>Название</span>
                <input type="text"
                       name="title"
                       maxlength="50"
                       required
                       value="<c:out value='${param.title != null ? param.title : editingEvent.title}'/>">
            </label>

            <label class="admin-field">
                <span>Дата и время</span>
                <input type="datetime-local"
                       name="startTime"
                       step="1"
                       required
                       value="<c:out value='${param.startTime != null ? param.startTime : editingEvent.startTime}'/>">
            </label>

            <label class="admin-field">
                <span>Длительность, минут</span>
                <input type="number"
                       name="durationMinutes"
                       min="0"
                       step="1"
                       value="<c:out value='${param.durationMinutes != null ? param.durationMinutes : editingEvent.durationMinutes}'/>">
            </label>

            <label class="admin-field">
                <span>Количество мест</span>
                <input type="number"
                       name="capacity"
                       min="1"
                       max="15"
                       step="1"
                       required
                       value="<c:out value='${param.capacity != null ? param.capacity : (not empty editingEvent ? editingEvent.capacity : 15)}'/>">
            </label>

            <label class="admin-field">
                <span>Видимость</span>
                <select name="visibility" required>
                    <option value="OPEN"
                    ${selectedVisibility == 'OPEN' ? 'selected' : ''}>
                        Открытое
                    </option>
                    <option value="SECRET"
                    ${selectedVisibility == 'SECRET' ? 'selected' : ''}>
                        Секретное
                    </option>
                </select>
            </label>

            <label class="admin-field">
                <span>Статус</span>
                <select name="status" required>
                    <option value="PLANNED"
                    ${selectedStatus == 'PLANNED' ? 'selected' : ''}>
                        Запланировано
                    </option>
                    <option value="CANCELLED"
                    ${selectedStatus == 'CANCELLED' ? 'selected' : ''}>
                        Отменено
                    </option>
                    <option value="COMPLETED"
                    ${selectedStatus == 'COMPLETED' ? 'selected' : ''}>
                        Завершено
                    </option>
                </select>
            </label>

            <label class="admin-field admin-field-wide">
                <span>Ссылка на изображение</span>
                <input type="text"
                       name="imageUrl"
                       maxlength="500"
                       placeholder="/img/events/event.jpg"
                       value="<c:out value='${param.imageUrl != null ? param.imageUrl : editingEvent.imageUrl}'/>">
            </label>

            <label class="admin-field admin-field-wide">
                <span>Описание</span>
                <textarea name="description"
                          rows="4"><c:out value="${param.description != null ? param.description : editingEvent.description}"/></textarea>
            </label>

            <div class="admin-form-actions admin-field-wide">
                <button class="admin-button" type="submit">
                    <c:choose>
                        <c:when test="${not empty editingEvent}">
                            Сохранить изменения
                        </c:when>
                        <c:otherwise>
                            Добавить мероприятие
                        </c:otherwise>
                    </c:choose>
                </button>

                <c:if test="${not empty editingEvent}">
                    <a class="admin-button admin-button-secondary"
                       href="<c:url value='/admin/events'/>">
                        Отмена
                    </a>
                </c:if>
            </div>
        </form>
    </section>

    <section class="admin-panel">
        <h2>Список мероприятий</h2>

        <c:choose>
            <c:when test="${empty events}">
                <p class="admin-empty">Мероприятий пока нет.</p>
            </c:when>
            <c:otherwise>
                <div class="admin-table-wrapper">
                    <table class="admin-table">
                        <thead>
                        <tr>
                            <th>Название</th>
                            <th>Дата и время</th>
                            <th>Места</th>
                            <th>Видимость</th>
                            <th>Статус</th>
                            <th>Действия</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="event" items="${events}">
                            <tr>
                                <td><strong><c:out value="${event.title}"/></strong></td>

                                <td>
                                    <c:out value="${event.formattedDate}"/>
                                    ·
                                    <c:out value="${event.formattedTime}"/>
                                </td>

                                <td><c:out value="${event.capacity}"/> / 15</td>
                                <td><c:out value="${event.visibility}"/></td>
                                <td><c:out value="${event.status}"/></td>

                                <td>
                                    <div class="admin-row-actions">
                                        <c:url var="editUrl" value="/admin/events">
                                            <c:param name="id" value="${event.id}"/>
                                        </c:url>

                                        <a class="admin-button admin-button-small"
                                           href="${editUrl}">
                                            Изменить
                                        </a>

                                        <form action="<c:url value='/admin/events'/>"
                                              method="post"
                                              onsubmit="return confirm('Удалить это мероприятие?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${event.id}">

                                            <button class="admin-button admin-button-danger admin-button-small"
                                                    type="submit">
                                                Удалить
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
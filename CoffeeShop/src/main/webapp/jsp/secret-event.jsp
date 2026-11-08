<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Секретное мероприятие</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/secret-event.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/role-header.jsp"/>

<main class="secret-page">
    <a class="secret-back-link" href="<c:url value='/events'/>">
        ← Вернуться к афише
    </a>

    <section class="secret-card">
        <div class="secret-badge">TOP SECRET</div>
        <p class="secret-eyebrow">Секретное мероприятие</p>
        <h1>Угадай, что мы задумали</h1>
        <p class="secret-description">
            Прочитай загадку и попробуй отгадать мероприятие.
        </p>

        <blockquote class="secret-riddle">
            <c:out value="${riddle.riddleText}"/>
        </blockquote>

        <c:if test="${not empty resultMessage}">
            <p class="secret-result" role="status">
                <c:out value="${resultMessage}"/>
            </p>
        </c:if>

        <form class="secret-answer-form"
              action="<c:url value='/secret-event'/>"
              method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <input type="hidden" name="id" value="<c:out value='${event.id}'/>">
            <input type="hidden" name="action" value="answer">

            <label for="answer">Твой ответ</label>
            <input id="answer"
                   name="answer"
                   type="text"
                   maxlength="200"
                   autocomplete="off"
                   required
                   placeholder="Название книги, имя автора и т.п.">

            <button class="secret-primary-button" type="submit">
                Проверить ответ
            </button>
        </form>

        <form class="secret-regenerate-form"
              action="<c:url value='/secret-event'/>"
              method="post">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <input type="hidden" name="id" value="<c:out value='${event.id}'/>">
            <input type="hidden" name="action" value="regenerate">

            <button class="secret-secondary-button" type="submit">
                Переоформить загадку
            </button>
        </form>
    </section>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
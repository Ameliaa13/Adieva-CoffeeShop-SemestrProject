<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Управление акциями</title>

  <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/admin-header.jsp"/>

<main class="admin-page">
  <div class="admin-page-heading">
    <div>
      <p class="admin-eyebrow">Панель администратора</p>
      <h1>Управление акциями</h1>
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

  <section class="admin-panel">
    <h2>
      <c:choose>
        <c:when test="${not empty editingPromotion}">
          Изменить акцию
        </c:when>
        <c:otherwise>
          Добавить акцию
        </c:otherwise>
      </c:choose>
    </h2>

    <form action="<c:url value='/admin/promotions'/>"
          method="post"
          class="admin-form">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

      <input type="hidden" name="action" value="save">

      <c:if test="${not empty editingPromotion}">
        <input type="hidden"
               name="id"
               value="${editingPromotion.id}">
      </c:if>

      <label class="admin-field">
        <span>Название</span>
        <input type="text"
               name="title"
               maxlength="100"
               required
               value="<c:out value='${param.title != null ? param.title : editingPromotion.title}'/>">
      </label>

      <label class="admin-field">
        <span>Краткое описание</span>
        <input type="text"
               name="shortDescription"
               maxlength="250"
               value="<c:out value='${param.shortDescription != null ? param.shortDescription : editingPromotion.shortDescription}'/>">
      </label>

      <label class="admin-field">
        <span>Дата начала</span>
        <input type="date"
               name="startDate"
               required
               value="<c:out value='${param.startDate != null ? param.startDate : editingPromotion.startDate}'/>">
      </label>

      <label class="admin-field">
        <span>Дата окончания</span>
        <input type="date"
               name="endDate"
               required
               value="<c:out value='${param.endDate != null ? param.endDate : editingPromotion.endDate}'/>">
      </label>

      <label class="admin-field admin-field-wide">
        <span>Ссылка на изображение</span>
        <input type="text"
               name="imageUrl"
               maxlength="256"
               placeholder="/img/promotions/promotion.jpg"
               value="<c:out value='${param.imageUrl != null ? param.imageUrl : editingPromotion.imageUrl}'/>">
      </label>

      <label class="admin-field admin-field-wide">
        <span>Полное описание</span>
        <textarea name="description"
                  rows="5"
                  required><c:out value="${param.description != null ? param.description : editingPromotion.description}"/></textarea>
      </label>

      <label class="admin-field admin-field-wide">
                <span>
                    <input type="checkbox"
                           name="active"
                           value="true"
                    <c:if test="${(param.action == null and editingPromotion.active)
                                      or (param.action != null and param.active != null)}">
                           checked
                    </c:if>>
                    Акция активна
                </span>
      </label>

      <div class="admin-form-actions admin-field-wide">
        <button class="admin-button" type="submit">
          <c:choose>
            <c:when test="${not empty editingPromotion}">
              Сохранить изменения
            </c:when>
            <c:otherwise>
              Добавить акцию
            </c:otherwise>
          </c:choose>
        </button>

        <c:if test="${not empty editingPromotion}">
          <a class="admin-button admin-button-secondary"
             href="<c:url value='/admin/promotions'/>">
            Отмена
          </a>
        </c:if>
      </div>
    </form>
  </section>

  <section class="admin-panel">
    <h2>Список акций</h2>

    <c:choose>
      <c:when test="${empty promotions}">
        <p class="admin-empty">Акций пока нет.</p>
      </c:when>
      <c:otherwise>
        <div class="admin-table-wrapper">
          <table class="admin-table">
            <thead>
            <tr>
              <th>Название</th>
              <th>Период</th>
              <th>Статус</th>
              <th>Действия</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="promotion" items="${promotions}">
              <tr>
                <td><strong><c:out value="${promotion.title}"/></strong></td>

                <td>
                  <c:out value="${promotion.startDate}"/>
                  —
                  <c:out value="${promotion.endDate}"/>
                </td>

                <td>
                  <c:choose>
                    <c:when test="${promotion.active}">
                      Активна
                    </c:when>
                    <c:otherwise>
                      Неактивна
                    </c:otherwise>
                  </c:choose>
                </td>

                <td>
                  <div class="admin-row-actions">
                    <c:url var="editUrl" value="/admin/promotions">
                      <c:param name="id" value="${promotion.id}"/>
                    </c:url>

                    <a class="admin-button admin-button-small"
                       href="${editUrl}">
                      Изменить
                    </a>

                    <form action="<c:url value='/admin/promotions'/>"
                          method="post"
                          onsubmit="return confirm('Удалить эту акцию?');">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                      <input type="hidden" name="action" value="delete">
                      <input type="hidden" name="id" value="${promotion.id}">

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
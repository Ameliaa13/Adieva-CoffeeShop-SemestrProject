<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Управление категориями</title>

  <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
  <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/admin-header.jsp"/>

<main class="admin-page">
  <div class="admin-page-heading">
    <div>
      <p class="admin-eyebrow">Панель администратора</p>
      <h1>Управление категориями</h1>
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
        <c:when test="${not empty editingCategory}">
          Изменить категорию
        </c:when>
        <c:otherwise>
          Добавить категорию
        </c:otherwise>
      </c:choose>
    </h2>

    <form action="<c:url value='/admin/categories'/>"
          method="post"
          class="admin-form">

      <input type="hidden" name="action" value="save">

      <c:if test="${not empty editingCategory}">
        <input type="hidden"
               name="id"
               value="${editingCategory.id}">
      </c:if>

      <label class="admin-field admin-field-wide">
        <span>Название категории</span>
        <input type="text"
               name="name"
               maxlength="50"
               required
               value="<c:out value='${param.name != null ? param.name : editingCategory.name}'/>">
      </label>

      <div class="admin-form-actions admin-field-wide">
        <button class="admin-button" type="submit">
          <c:choose>
            <c:when test="${not empty editingCategory}">
              Сохранить изменения
            </c:when>
            <c:otherwise>
              Добавить категорию
            </c:otherwise>
          </c:choose>
        </button>

        <c:if test="${not empty editingCategory}">
          <a class="admin-button admin-button-secondary"
             href="<c:url value='/admin/categories'/>">
            Отмена
          </a>
        </c:if>
      </div>
    </form>
  </section>

  <section class="admin-panel">
    <h2>Категории меню</h2>

    <c:choose>
      <c:when test="${empty categories}">
        <p class="admin-empty">Пока категорий нет.</p>
      </c:when>

      <c:otherwise>
        <div class="admin-table-wrapper">
          <table class="admin-table">
            <thead>
            <tr>
              <th>Название</th>
              <th>Действия</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="category" items="${categories}">
              <tr>
                <td>
                  <strong>
                    <c:out value="${category.name}"/>
                  </strong>
                </td>

                <td>
                  <div class="admin-row-actions">
                    <c:url var="editUrl" value="/admin/categories">
                      <c:param name="id" value="${category.id}"/>
                    </c:url>

                    <a class="admin-button admin-button-small"
                       href="${editUrl}">
                      Изменить
                    </a>

                    <form action="<c:url value='/admin/categories'/>"
                          method="post"
                          onsubmit="return confirm('Удалить эту категорию?');">
                      <input type="hidden"
                             name="action"
                             value="delete">
                      <input type="hidden"
                             name="id"
                             value="${category.id}">

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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Управление меню</title>

    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/admin.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/admin-header.jsp"/>

<main class="admin-page">
    <div class="admin-page-heading">
        <div>
            <p class="admin-eyebrow">Панель администратора</p>
            <h1>Управление меню</h1>
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
                <c:when test="${not empty editingItem}">
                    Редактировать товар
                </c:when>
                <c:otherwise>
                    Добавить товар
                </c:otherwise>
            </c:choose>
        </h2>

        <form action="<c:url value='/admin/menu'/>"
              method="post"
              class="admin-form">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

            <input type="hidden"
                   name="action"
                   value="save">

            <c:if test="${not empty editingItem}">
                <input type="hidden"
                       name="id"
                       value="${editingItem.id}">
            </c:if>

            <label class="admin-field">
                <span>Название товара</span>
                <input type="text"
                       name="name"
                       maxlength="100"
                       required
                       value="<c:out value='${not empty param.name ? param.name : editingItem.name}'/>">
            </label>

            <label class="admin-field">
                <span>Категория</span>
                <select name="categoryId" required>
                    <option value="">Выбери категорию</option>

                    <c:forEach var="category" items="${categories}">
                        <c:set var="selectedCategoryId"
                               value="${not empty param.categoryId
                                   ? param.categoryId
                                   : editingItem.categoryId}"/>

                        <option value="${category.id}"
                            ${selectedCategoryId == category.id ? 'selected' : ''}>
                            <c:out value="${category.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </label>

            <label class="admin-field">
                <span>Цена, ₽</span>
                <input type="number"
                       name="price"
                       min="0.01"
                       step="0.01"
                       required
                       value="<c:out value='${not empty param.price ? param.price : editingItem.price}'/>">
            </label>

            <label class="admin-field admin-field-wide">
                <span>Описание</span>
                <textarea name="description"
                          rows="4"><c:out value="${not empty param.description ? param.description : editingItem.description}"/></textarea>
            </label>

            <label class="admin-field admin-field-wide">
                <span>Путь к изображению</span>
                <input type="text"
                       name="imageUrl"
                       maxlength="256"
                       placeholder="/img/menu/coffee.jpg"
                       value="<c:out value='${not empty param.imageUrl ? param.imageUrl : editingItem.imageUrl}'/>">
            </label>

            <div class="admin-form-section admin-field-wide">
                <h3>Пищевая ценность на 100 мл</h3>
                <p>Укажи числа. Например: 41 или 2.17.</p>
            </div>

            <label class="admin-field">
                <span>Калории, ккал</span>
                <input type="number"
                       name="calories"
                       min="0"
                       step="0.01"
                       required
                       value="<c:out value='${not empty param.calories ? param.calories : editingItem.calories}'/>">
            </label>

            <label class="admin-field">
                <span>Белки, г</span>
                <input type="number"
                       name="proteins"
                       min="0"
                       step="0.01"
                       required
                       value="<c:out value='${not empty param.proteins ? param.proteins : editingItem.proteins}'/>">
            </label>

            <label class="admin-field">
                <span>Жиры, г</span>
                <input type="number"
                       name="fats"
                       min="0"
                       step="0.01"
                       required
                       value="<c:out value='${not empty param.fats ? param.fats : editingItem.fats}'/>">
            </label>

            <label class="admin-field">
                <span>Углеводы, г</span>
                <input type="number"
                       name="carbohydrates"
                       min="0"
                       step="0.01"
                       required
                       value="<c:out value='${not empty param.carbohydrates ? param.carbohydrates : editingItem.carbohydrates}'/>">
            </label>

            <div class="admin-form-actions admin-field-wide">
                <button class="admin-button" type="submit">
                    <c:choose>
                        <c:when test="${not empty editingItem}">
                            Сохранить изменения
                        </c:when>
                        <c:otherwise>
                            Добавить товар
                        </c:otherwise>
                    </c:choose>
                </button>

                <c:if test="${not empty editingItem}">
                    <a class="admin-button admin-button-secondary"
                       href="<c:url value='/admin/menu'/>">
                        Отмена
                    </a>
                </c:if>
            </div>
        </form>
    </section>

    <section class="admin-panel">
        <h2>Товары в меню</h2>

        <form action="<c:url value='/admin/menu'/>"
              method="get"
              class="admin-menu-filter">

            <label for="filterCategoryId">Показать категорию</label>

            <select id="filterCategoryId" name="filterCategoryId">
                <option value="">Все категории</option>

                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}"
                            <c:if test="${selectedCategoryId == category.id}">selected</c:if>>
                        <c:out value="${category.name}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="admin-button" type="submit">Показать</button>

            <a class="admin-button admin-button-secondary"
               href="<c:url value='/admin/menu'/>">
                Сбросить
            </a>
        </form>

        <c:choose>
            <c:when test="${empty menuItems}">
                <p class="admin-empty">Пока товаров нет.</p>
            </c:when>
            <c:otherwise>
                <div class="admin-table-wrapper">
                    <table class="admin-table">
                        <thead>
                        <tr>
                            <th>Фото</th>
                            <th>Название</th>
                            <th>Категория</th>
                            <th>Цена</th>
                            <th>КБЖУ на 100 мл</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${menuItems}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty item.imageUrl}">
                                            <img class="admin-product-image"
                                                 src="<c:url value='${item.imageUrl}'/>"
                                                 alt="<c:out value='${item.name}'/>">
                                        </c:when>
                                        <c:otherwise>
                                            <span class="admin-no-image">Нет фото</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <strong><c:out value="${item.name}"/></strong>
                                </td>
                                <td>
                                    <c:forEach var="category" items="${categories}">
                                        <c:if test="${category.id == item.categoryId}">
                                            <c:out value="${category.name}"/>
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${item.price}"
                                                      minFractionDigits="2"
                                                      maxFractionDigits="2"/> ₽
                                </td>

                                <td class="admin-nutrition">
                                    <span>К: <c:out value="${item.calories}"/></span>
                                    <span>Б: <c:out value="${item.proteins}"/> г</span>
                                    <span>Ж: <c:out value="${item.fats}"/> г</span>
                                    <span>У: <c:out value="${item.carbohydrates}"/> г</span>
                                </td>

                                <td>
                                    <div class="admin-row-actions">
                                        <c:url var="editUrl" value="/admin/menu">
                                            <c:param name="id" value="${item.id}"/>
                                        </c:url>

                                        <a class="admin-button admin-button-small"
                                           href="${editUrl}">
                                            Изменить
                                        </a>

                                        <form action="<c:url value='/admin/menu'/>"
                                              method="post"
                                              onsubmit="return confirm('Удалить этот товар?');">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                            <input type="hidden"
                                                   name="action"
                                                   value="delete">
                                            <input type="hidden"
                                                   name="id"
                                                   value="${item.id}">

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
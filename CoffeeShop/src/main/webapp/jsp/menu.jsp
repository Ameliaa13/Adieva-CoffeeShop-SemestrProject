<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Меню кофейни</title>
    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
</head>
<body>

<c:import url="/jsp/fragments/header.jsp"/>

<div class="menu-container">
    <h1>Наше меню</h1>

    <c:forEach var="category" items="${categories}">
        <div class="category-section">
            <h2 class="category-title">
                <c:out value="${category.name}"/>
            </h2>

            <div class="menu-grid">
                <c:set var="hasItems" value="false"/>

                <c:forEach var="item" items="${menuItems}">
                    <c:if test="${item.categoryId == category.id}">
                        <c:set var="hasItems" value="true"/>

                        <c:url var="itemUrl" value="/menu/item">
                            <c:param name="id" value="${item.id}"/>
                        </c:url>

                        <a class="menu-card" href="${itemUrl}">
                            <c:if test="${not empty item.imageUrl}">
                                <img
                                        src="<c:url value='${item.imageUrl}'/>"
                                        alt="<c:out value='${item.name}'/>"
                                        class="menu-img">
                            </c:if>

                            <div class="menu-info">
                                <h3><c:out value="${item.name}"/></h3>
                            </div>

                            <div class="price">
                                <c:out value="${item.price}"/> ₽
                            </div>
                        </a>
                    </c:if>
                </c:forEach>

                <c:if test="${not hasItems}">
                    <p class="no-items">В этой категории пока нет товаров.</p>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
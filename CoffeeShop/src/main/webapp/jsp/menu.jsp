<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Меню кофейни</title>
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
</head>
<body>

<div class="menu-container">
    <h1>Наше меню</h1>

    <c:forEach var="category" items="${categories}">
        <div class="category-section">
            <h2 class="category-title"><c:out value="${category.name}" /></h2>

            <div class="menu-grid">
                <c:set var="hasItems" value="false" />

                <c:forEach var="item" items="${menuItems}">
                    <c:if test="${item.categoryId == category.id}">
                        <c:set var="hasItems" value="true" />
                        <div class="menu-card">
                            <c:if test="${not empty item.imageUrl}">
                                <img src="<c:url value='${item.imageUrl}'/>" alt="<c:out value='${item.name}'/>" class="menu-img">
                            </c:if>

                            <div class="menu-info">
                                <h3><c:out value="${item.name}" /></h3>
                                <p><c:out value="${item.description}" /></p>
                            </div>

                            <div>
                                <div class="price">
                                    <c:out value="${item.price}" /> ₽
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>

                <c:if test="${!hasItems}">
                    <p class="no-items">В этой категории пока нет товаров.</p>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>

</body>
</html>
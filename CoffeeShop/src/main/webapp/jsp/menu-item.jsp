<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${menuItem.name}"/> — кофейня</title>
    <link rel="stylesheet" href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
</head>
<body class="product-page-body">

<c:import url="/jsp/fragments/header.jsp"/>

<main class="product-page">
    <nav class="product-breadcrumb">
        <a href="<c:url value='/menu'/>">Меню</a>
        <span>/</span>
        <c:out value="${menuItem.name}"/>
    </nav>

    <article class="product-details">
        <div class="product-image-container">
            <c:choose>
                <c:when test="${not empty menuItem.imageUrl}">
                    <img
                            src="<c:url value='${menuItem.imageUrl}'/>"
                            alt="<c:out value='${menuItem.name}'/>"
                            class="product-image">
                </c:when>
                <c:otherwise>
                    <div class="product-image-placeholder">
                        Фото скоро появится
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="product-info">
            <h1><c:out value="${menuItem.name}"/></h1>

            <p class="product-description">
                <c:out value="${menuItem.description}"/>
            </p>

            <p class="product-price">
                <c:out value="${menuItem.price}"/> ₽
            </p>
        </div>
    </article>
</main>

<c:import url="/jsp/fragments/footer.jsp"/>

</body>
</html>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:choose>
  <c:when test="${not empty sessionScope.user
                    and sessionScope.user.role eq 'ADMIN'}">
    <c:import url="/jsp/fragments/admin-header.jsp"/>
  </c:when>

  <c:otherwise>
    <c:import url="/jsp/fragments/header.jsp"/>
  </c:otherwise>
</c:choose>
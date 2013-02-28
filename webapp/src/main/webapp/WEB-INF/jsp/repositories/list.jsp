<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
  <head>
      <%@ include file="../../../html_head.jspf" %>
  </head>

  <body>

    <jsp:include page="../../../header.jsp">
        <jsp:param name="current" value="listrepo"/>
    </jsp:include>

    <div class="container">

    <table class="table table-striped table-bordered">
      <thead>
        <tr>
            <th>Name</th>
            <th>Location</th>
            <th>Type</th>
            <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${repositories}" var="repository">
            <tr><td>${repository.name}</td><td>${repository.location}</td><td>${repository.type}</td>
                <td>
                    <c:choose>
                        <c:when test="${sessionScope.repository != null && sessionScope.repository.name == repository.name}">
                            Selected
                        </c:when>
                        <c:otherwise>
                            <form:form modelAttribute="repository" method="POST" action="/repositories/select.do" style="margin: 0">
                                <input type="hidden" name="name" value="${repository.name}"/>
                                <input type="hidden" name="location" value="${repository.location}"/>
                                <input type="hidden" name="type" value="${repository.type}"/>
                                <button type="submit" class="btn btn-mini btn-primary">Select</button>
                            </form:form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
      </tbody>
      </table>


      <%@ include file="../../../footer.jsp" %>

    </div> <!-- /container -->

    <!-- javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/js/jquery/jquery.js"></script>
    <script src="/bootstrap/js/bootstrap.js"></script>

  </body>
</html>
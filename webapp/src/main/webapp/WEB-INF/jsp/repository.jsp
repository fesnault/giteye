<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
      <%@ include file="../../html_head.jspf" %>
  </head>

  <body>

    <jsp:include page="../../header.jsp">
        <jsp:param name="current" value="home"/>
    </jsp:include>

    <div class="container">

    <div class="well">
    <table width="100%">
      <tr><td>Repository</td><td><b>${repository.path}</b></td></tr>
      <tr><td>Current branch</td><td><b>${repository.branch}</b></td></tr>
      <tr><td>Head</td><td><b>${repository.head}</b></td></tr>
      </table>
    </div>

      <table class="table table-striped table-bordered">
      <thead>
        <tr>
        <th>Commit</th>
        <th>Message</th>
        <th>Committer</th>
        </tr>
      </thead>
      <tbody>
      <c:forEach items="${log}" var="commit">
          <tr><td>${commit.id}</td><td>${commit.message}</td><td>${commit.committerName}</td></tr>
        </c:forEach>
      </tbody>
      </table>

      <hr>

      <footer>
        <p>&copy; Phoenix 2013</p>
      </footer>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>

  </body>
</html>
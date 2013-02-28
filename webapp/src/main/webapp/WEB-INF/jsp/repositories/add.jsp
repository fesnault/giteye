<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
      <%@ include file="../../../html_head.jspf" %>
  </head>

  <body>

    <jsp:include page="../../../header.jsp">
        <jsp:param name="current" value="addrepo"/>
    </jsp:include>

    <div class="container">

        <form class="form-horizontal" action="/repositories/add.do" method="POST">
          <legend>New Git repository</legend>
          <div class="control-group">
            <label class="control-label" for="type">Repository type</label>
            <div class="controls">
                <select id="type" name="type">
                  <option>LOCAL</option>
                  <option>REMOTE</option>
                </select>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" for="location">Location</label>
            <div class="controls">
              <input type="text" name="location" placeholder="Repository location" id="location">
            </div>
          </div>
          <div class="control-group">
              <label class="control-label" for="name">Name</label>
              <div class="controls">
                <input type="text" name="name" placeholder="Repository name" id="name">
              </div>
          </div>
          <div class="control-group">
              <div class="controls">
                <button type="submit" class="btn btn-primary">Add repository</button>
              </div>
          </div>

        </form>

      <%@ include file="../../../footer.jsp" %>

    </div> <!-- /container -->

    <!-- javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/js/jquery/jquery.js"></script>
    <script src="/bootstrap/js/bootstrap.js"></script>

  </body>
</html>
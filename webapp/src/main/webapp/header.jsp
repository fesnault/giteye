<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#">GitEye</a>
          <div class="nav-collapse collapse">
              <p class="navbar-text pull-right">
                <a href="#" class="navbar-link">&nbsp;<i>${sessionScope.repository == null ? "no repository selected" : repository.displayName}</i></a>
              </p>
              <ul class="nav">
                <c:choose>
                    <c:when test="${param.current == 'home'}">
                        <li  class="active">
                    </c:when>
                    <c:otherwise>
                        <li>
                    </c:otherwise>
                </c:choose>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Repositories<b class="caret"></b></a>
                    <ul class="dropdown-menu">
                      <li><a href="/repository.do">Current</a></li>
                      <li class="divider"></li>
                      <li class="nav-header">Tools</li>
                      <li><a href="/log.do">Log</a></li>
                    </ul>
                  </li>
              <!--a href="#">Repositories</a></li-->


            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
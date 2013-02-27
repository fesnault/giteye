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
                Working repository<a href="#" class="navbar-link">&nbsp;<i>${selectedRepository == null ? "none" : selectedRepository}</i></a>
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
              <a href="#">Repositories</a></li>

              <!--li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="#">Action</a></li>
                  <li><a href="#">Another action</a></li>
                  <li><a href="#">Something else here</a></li>
                  <li class="divider"></li>
                  <li class="nav-header">Nav header</li>
                  <li><a href="#">Separated link</a></li>
                  <li><a href="#">One more separated link</a></li>
                </ul>
              </li-->
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
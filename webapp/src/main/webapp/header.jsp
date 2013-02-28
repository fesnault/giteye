<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="/index.jsp">GitEye</a>

            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    <a href="/repositories/list.do" class="navbar-link">
                        &nbsp;<i>${sessionScope.repository == null ? "no repository selected" : repository.name}</i></a>
                </p>
                <ul class="nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Repositories<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="/repositories/add.do">Add a repository</a></li>
                            <li><a href="/repositories/list.do">List repositories</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Tools<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="/git/branches.do">List branches</a></li>
                            <li><a href="/git/log.do">Show flat log</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
            <!--/.nav-collapse -->
        </div>
    </div>
</div>
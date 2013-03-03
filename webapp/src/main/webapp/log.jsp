<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="html_head.jspf" %>
    <link href="/css/logstyle.css" rel="stylesheet" media="screen"/>
</head>

<body>

<jsp:include page="header.jsp">
    <jsp:param name="current" value="home"/>
</jsp:include>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span3">
            <div class="well sidebar-nav">
                <ul class="nav nav-list">
                    <li class="nav-header">Zoom</li>
                    <li><a href="#" onclick="zoomin();">Zoom in</a></li>
                    <li><a href="#" onclick="zoomout();">Zoom out</a></li>
                </ul>
            </div> <!--/.well -->

        </div><!--/span-->

        <div class="span6">
            <table class="table table-bordered table-striped" width="100%">
                <tr>
                    <td width="10%">Id</td>
                    <td id="commit-id"></td>
                </tr>
                <tr>
                    <td width="10%">Author</td>
                    <td id="commit-author"></td>
                </tr>
                <tr>
                    <td width="10%">Message</td>
                    <td id="commit-message"></td>
                </tr>
                <tr>
                    <td width="10%">Date</td>
                    <td id="commit-date"></td>
                </tr>
            </table>
            <br/>
            <div>
                <table class="table table-bordered" width="100%">
                <tr><td id="svgcontainer"><!-- Here comes the svg --></td></tr>
                </table>
            </div>
        </div><!--/span-->

    </div><!--/row-->

</div> <!--container-->



<%@ include file="footer.jsp" %>

</div> <!-- /container -->

<!-- javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="/js/jquery/jquery.js"></script>
<script src="/bootstrap/js/bootstrap.js"></script>

<script src="/js/d3/d3.v3.js"></script>
<script type="text/javascript" src="/js/logscript.js"></script>

</body>
</html>
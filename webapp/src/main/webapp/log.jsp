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
        <div class="span12">
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
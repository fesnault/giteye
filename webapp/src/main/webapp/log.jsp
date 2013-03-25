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

    <div class="row-fluid">
    	<div class="span6">
	    	<table class="table table-condensed" width="100%">
	    		<thead>
	    			<th>Informations</th>
	    		</thead>
	    		<tbody>
		    		<tr><td>SHA1</td><td id="sha1"></td></tr>
					<tr><td>Author</td><td id="author"></td></tr>
					<tr><td>Date</td><td id="date"></td></tr>
					<tr><td>Message</td><td id="message"></td></tr>
				</tbody>
	    	</table>
	    </div>
	    <div class="span6">
	    	<table class="table" style="margin-bottom: 0px;"><thead><th>Files</th></thead></table>
	    	<div  style="overflow: auto; height: 200px;">
		    	<table class="table">
		    		<tbody id="diff">

		    		</tbody>
		    	</table>
	    	</div>
	    </div>
    </div>

    <div id="fileDiffModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <i <h3 id="fileDiffModalTitle">File diff</h3>
	  </div>
	  <div class="modal-body">
	  	<blockquote>
	  		<p id="fileDiffName"></p>
	  	</blockquote>
	    <span id="fileDiffContents"></span>
	  </div>
	  <div class="modal-footer">
	    <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Close</button>
	  </div>
	</div>

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
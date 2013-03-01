<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="html_head.jspf" %>
    <style>

        .node {
            stroke: #fff;
            stroke-width: 2px;
        }

        .link {
            fill: none;
            stroke: #000;
        }

    </style>
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

        <div class="span9">
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
        </div><!--/span-->

        <div class="span9">
        <div id="svgcontainer">
            <!-- Here comes the svg -->
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

<script>


    // START
    /*var tree = d3.layout.tree()
     .size([width - 20, height - 20]);

     var root = {},
     nodes = tree(root);

     root.parent = root;
     root.px = root.x;
     root.py = root.y;

     var diagonal = d3.svg.diagonal();

     var svg = d3.select("body").append("svg")
     .attr("width", width)
     .attr("height", height)
     .append("g")
     .attr("transform", "translate(10,10)");

     var node = svg.selectAll(".node"),
     link = svg.selectAll(".link");

     var duration = 750,
     timer = setInterval(update, duration);

     function update() {
     if (nodes.length >= 500) return clearInterval(timer);

     // Add a new node to a random parent.
     var n = {id: nodes.length},
     p = nodes[Math.random() * nodes.length | 0];
     if (p.children) p.children.push(n); else p.children = [n];
     nodes.push(n);

     // Recompute the layout and data join.
     node = node.data(tree.nodes(root), function(d) { return d.id; });
     link = link.data(tree.links(nodes), function(d) { return d.source.id + "-" + d.target.id; });

     // Add entering nodes in the parent’s old position.
     node.enter().append("circle")
     .attr("class", "node")
     .attr("r", 4)
     .attr("cx", function(d) { return d.parent.px; })
     .attr("cy", function(d) { return d.parent.py; });

     // Add entering links in the parent’s old position.
     link.enter().insert("path", ".node")
     .attr("class", "link")
     .attr("d", function(d) {
     var o = {x: d.source.px, y: d.source.py};
     return diagonal({source: o, target: o});
     });

     // Transition nodes and links to their new positions.
     var t = svg.transition()
     .duration(duration);

     t.selectAll(".link")
     .attr("d", diagonal);

     t.selectAll(".node")
     .attr("cx", function(d) { return d.px = d.x; })
     .attr("cy", function(d) { return d.py = d.y; });
     }*/
    // END

    var width = 900;
    var height = 550;
    var commitData;
    var min, max;

    var scaler;

    // The svg container
    var svg = d3.select("#svgcontainer").append("svg")
            .attr("width", width + 100)
            .attr("height", height + 100)
            .style("border", "1px solid black");




    function displayInfo(element) {
        $("#commit-id").text(element.id);
        $("#commit-author").text(element.committerName);
        $("#commit-message").text(element.message);
        $("#commit-date").text(element.date);
    }

    function zoomin() {
        max = max / 2;
        redraw();
    }

    function zoomout() {
        max = max * 2;
        redraw();
    }


    function redraw() {

        // Update…
        svg.selectAll("circle")
          .data(commitData)
          .transition()
          .duration(1000);
        alert("redrawn");

     }

    d3.json("/git/test.do", function (data) {
        commitData = data.commits;


        max = d3.max(commitData,
                function (d) {
                    return d.date;
                }
        );
        min = d3.min(commitData,
                function (d) {
                    return d.date;
                }
        );

        // scaler - scales the commits dates base on the idea that they are displayed vertically. So the scale range uses the height as a max.
        scaler = d3.scale.linear()
                .domain([min, max])
                .range([10, height + 10]);


        // Add a circle for each commit
        var commits = svg.selectAll("circle").data(commitData).enter();

        var lineFunction = d3.svg.line()
                .x(function (d) {
                    return 500;
                })
                .y(function (d) {
                    return scaler(d.date);
                })
                .interpolate("basis");

        var lineGraph = svg.append("path")
                .attr("d", lineFunction(commitData))
                .attr("stroke", "#000000")
                .attr("stroke-width", 1);

        var commitsAttrs = commits.append("circle").attr("id",function (d) {
            return d.id;
        }).attr("title", function (d) {
            return d.id;
        })
                .attr("cx", 500)
                .attr("cy", function (d) {
                    return scaler(d.date);
                })
                .attr("r", 5)
                .style("fill", "#0000FF")
                .on("mouseover", function (d) {
                    displayInfo(d);
                });
        /*.on("click", function(d) { displayInfo(d); })

         .on("mouseout", function(d) { hideToolTip(d); });*/

        //svg.append("svg:g").attr("id", "graph").attr("transform", "translate(5,5)");

        /*var jsonRectangles = [
         { "x_axis": 10, "y_axis": 10, "height": 20, "width":20, "color" : "#11FF11" },
         { "x_axis": 1200, "y_axis": 40, "height": 20, "width":20, "color" : "#FF1111" },
         { "x_axis": 70, "y_axis": 70, "height": 20, "width":20, "color" : "#1111FF" }];

         var rectangles = svg.selectAll("rect").data(jsonRectangles).enter().append("rect");

         var rectAttributes = rectangles
         .attr("x", function (d) { return d.x_axis;} )
         .attr("y", function (d) { return d.y_axis;} )
         .attr("width", function (d) { return d.width;} )
         .attr("height", function (d) { return d.height;} )
         .style("fill", function (d) { return d.color;} )*/


    });

</script>

</body>
</html>
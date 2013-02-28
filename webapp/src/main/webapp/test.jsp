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

    <div class="container">

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div id="svgcontainer">
        <!-- Here comes the svg -->
      </div>


        <%@ include file="footer.jsp" %>

      </div> <!-- /container -->

    <!-- javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/js/jquery/jquery.js"></script>
    <script src="/bootstrap/js/bootstrap.js"></script>

    <script src="/js/d3/d3.v3.min.js"></script>

    <script>
       var width = 900;
       var height = 550;

        // START
        var tree = d3.layout.tree()
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
        }
        // END
       d3.json("/git/test.do", function(data) {
            var max = d3.max(data.date);
            var min = d3.min(data.date);
            var svg = d3.select("#svgcontainer").append("svg").attr("width", 1000).attr("height", 600).style("border", "1px solid black") ;
            svg.append("svg:g").attr("id", "graph").attr("transform", "translate(5,5)");


       });

    </script>

  </body>
</html>
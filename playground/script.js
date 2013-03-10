var min=0, max=0, previousMin=0, previousMax=0, commitPadding=4,width=1300,height=500,margin=10,padding=100,bgcolor="#FFFFFF"
svg=d3.select("#svgcontainer")
.append("svg").attr("class", "workspace");

var svgGroup = svg.insert("g");
var linksGroup = svgGroup.insert("g").attr("class", "links-group");
var commitsGroup = svgGroup.insert("g").attr("class", "commits-group");
 
// The data join should be done on the commits ids.

function key(d) {
  return d.id;
}

function formatMillisecondDate(d) {
	var format = d3.time.format("%Y, %b %d");
	if (d instanceof Date) {
		return format(d);
	}
	return format(new Date(d));
}

var delay = 500;

//setInterval(function() {updateData(); }, 1000);

var repository;
var currentCommits;
var originalCommits;
// scalers - scales the commits dates base on the idea that they are displayed vertically. So the scale range uses the height as a max.
// X is scaled by the number of branches (from 1 to 10) - TODO : make it dynamic from the branch count in the data.
var x = d3.scale.linear().domain([0,25]).range([margin,width-margin]);
// Y is scaled by the commit date
//var y = d3.scale.linear().domain([min, max]).range([height-margin, margin]);
var y = d3.scale.linear().domain([min, max]).range([margin, height-margin]);

var line = d3.svg.diagonal().projection( function (d) { return [d.x, d.y*commitPadding] } );
var initline = d3.svg.diagonal().projection( function (d) {
  if (d.y < min) {
    return [d.x, margin];
  } else if (d.y > max) {
    return [d.x, height-margin];
  }
  return [d.x, margin];} 
);

// buttons
var resetButton = d3.select("svg").append("g").attr("class","button reset");
resetButton.append("circle")
      .attr("cx", 1000)
      .attr("cy", 200)
      .attr("r", 30)
      .on("click", resetZoom);
resetButton.append("text")
        .attr("x", 1000)
        .attr("y", 200)
        .text("Reset")
        .on("click", resetZoom);
var zoomButton = d3.select("svg").append("g").attr("class","button zoom");
zoomButton.append("circle")
      .attr("cx", 1000)
      .attr("cy", 270)
      .attr("r", 30)
      .on("click", initBrush);
zoomButton.append("text")
        .attr("x", 1000)
        .attr("y", 270)
        .text("Zoom")
        .on("click", initBrush);

function updateData() {
	if (currentCommits.length > 50) {
		currentCommits = currentCommits.slice(1, currentCommits.length);
		redraw(currentCommits);
	}
}

d3.json("data.json", function(jrep) {
	repository = jrep;
	originalCommits = repository.commits;
	currentCommits = originalCommits;
	redraw(currentCommits);
});

function click(commit) {
	alert('Clicked commit '+commit.id);
}

var brush;

function initBrush() {
  brush = svgGroup.append("g")
    .attr("class", "brush");
  brush.call(d3.svg.brush().x(x).y(y)
    .on("brushstart", brushstart)
    .on("brush", brushmove)
    .on("brushend", brushend));
}

function resetBrush() {
  if (brush !== undefined) {
    brush.remove();
  }
}


function brushstart() {
  svgGroup.classed("selecting", true);
}

function brushmove() {
  var brushRect = d3.select("g.brush .extent");
  var bx = (+brushRect.attr("x"))-30;
  var by = (+brushRect.attr("y"));
  var bw = (+brushRect.attr("width"));
  var bh = (+brushRect.attr("height"));
  var bx2 = (+(bx+bw));
  var by2 = (+(by+bh));  
  
  d3.selectAll(".circle").classed("selected", function(d) {
  	var cx = x(d.lane);
  	var cy = y (+d.position);
  	if (cx > bx && cx < bx2 && cy > by && cy < by2) {
  		return true;
  	}
  	return false;
 });
  var linkContainer = d3.selectAll(".link").classed("selected", function(d) {
  	var sx = d.source.x;
  	var sy = d.source.y;
  	var tx = d.target.x;
  	var ty = d.target.y;
  	if (sx > bx && sx < bx2 && sy > by && sy < by2) {
  		if (tx > bx && tx < bx2 && ty > by && ty < by2) {
  			return true;
  		}
  	}
  	return false;
 });
}

function brushend() {
  svgGroup.classed("selecting", false);
  var brushRect = d3.select("g.brush .extent");
  var bw = (+brushRect.attr("width"));
  var bh = (+brushRect.attr("height"));
  if (bw > 10 && bh > 10) {
  	zoom();
  }
}

function zoom() {
  var selectedCommitsSelection = svgGroup.selectAll(".circle.selected")[0];
  if (selectedCommitsSelection.length === 0) { return; }
  var selectedCommits = [];
  for (var i=0; i< selectedCommitsSelection.length; i++) {
    selectedCommits.push(selectedCommitsSelection[i].__data__);
  }
  svgGroup.selectAll(".selected").classed("selected", false);
  resetBrush();
  redraw(selectedCommits);
}

function resetZoom() {
  //console.log("dblclick");
  redraw(originalCommits);
}

function redraw(commits) {
  previousMax = max;
  previousMin = min;
	max = d3.max(commits, function(d) { return d.position;});
  min = d3.min(commits, function(d) { return d.position;});
	y = d3.scale.linear().domain([min, max]).range([margin, height-margin]);

	var nodes = [];
  var links = [];
  commits.forEach(function(d, i) {
      var source = {"id": d.id ,"x": x(d.lane), "y": y(d.position)};
      nodes.push(d);
      if (d.children !== null) {
        d.children.forEach(function(c, i) {
        	var target = {"id": c.id ,"x": x(c.lane), "y": y(c.position)};
            links.push({"id": source.id + "-" + target.id ,"source": source, "target": target});
        });
  	}
  });

	
	var linksSelection = linksGroup.selectAll("path").data(links, key);
  var linksEnterSelection = linksSelection.enter();
  var linksExitingSelection = linksSelection.exit();

  var enteringLinks = linksEnterSelection.append("path").attr("class", "link").attr("d", initline)
      .attr("transform", "translate(30,0)");
  enteringLinks.transition()
      .duration(delay)
      .style("opacity", 1);

  
  var commitsSelection = commitsGroup.selectAll(".circle").data(nodes, key);
  var commitsEnterSelection = commitsSelection.enter().append("g").attr("class", "circle");
  var commitsExitingSelection = commitsSelection.exit();
	

  var enteringCommits = commitsEnterSelection.append("circle")
      .attr("class", function (d) {
        if (d.parentCount===0) {
          return "commit root";
        } else if (d.childCount === 0) {
          return "commit tip";
        }
        return "commit";
      })
      .attr("cx", function(d) {return x(d.lane);})
      .attr("cy", function(d) { 
        if (d.position < previousMin) {
          return margin;
        } else if (d.position > previousMax) {
          return height-margin;
        }
      })
      .attr("r", 4)
      .attr("transform", "translate(30,0)")
      .on("click", function(d) { click(d); });
  enteringCommits.transition()
      .duration(delay)
      .style("opacity", 1);

  
	

  commitsExitingSelection.transition().style("opacity", 1e-6)
      .duration(delay)
      .remove();
  linksExitingSelection.transition().style("opacity", 1e-6)
      .duration(delay)
      .remove();


  var t = svg.transition().duration(delay);
	t.selectAll("circle.commit").attr("cy", function(d) { return y(+d.position)*commitPadding; });
	t.selectAll("path").attr("d", line);

  linksExitingSelection.transition().style("opacity", 1e-6).remove();


}
var min=0, max=0, previousMin=0, previousMax=0, commitPadding=4,width=1300,height=500,margin=10,padding=100,bgcolor="#FFFFFF", maxAllowedLane = 25,
ypan=0;

var maxPaddingHeight = 0;
var minPaddingHeight = 0;
var originalTranslate = 0;
var currentPan = 0;
// scalers - scales the commits dates base on the idea that they are displayed vertically. So the scale range uses the height as a max.
// X is scaled by the number of branches (from 1 to 10) - TODO : make it dynamic from the branch count in the data.
// Y is scaled by the commit date
var x = d3.scale.linear().domain([0,maxAllowedLane]).range([margin,width-margin]);
var y = d3.scale.linear().domain([min, max]).range([margin, height-margin]);
var svg=d3.select("#svgcontainer")
.append("svg").attr("class", "workspace");

svg.append("clipPath")
    .attr("id", "clipMessage")
  .append("rect")
    .attr("x", 5)
    .attr("y", 5)
    .attr("width", 10)
    .attr("height", 10);
svg.append("clipPath")
    .attr("id", "clipName")
  .append("rect")
    .attr("x", 5)
    .attr("y", 5)
    .attr("width", 10)
    .attr("height", 10);

  
//.on("mousedown", mousedown)
//.on("mousemove", mousemove)
//.on("mouseup", mouseup)


function doit(d) {
  console.log(d);
}

var svgGroup = svg.insert("g").attr("class", "log");
  
var linksGroup = svgGroup.insert("g").attr("class", "links-group");
var commitsGroup = svgGroup.insert("g").attr("class", "commits-group");
var infosGroup = svgGroup.insert("g").attr("class", "infos-group");
var origin;


function mousedown() {
  origin = d3.mouse(svgGroup.node());
}

function mousemove() {
      if (!origin) { return; }
        var m = d3.mouse(svgGroup.node());
        ypan = ypan + (m[1]-origin[1]);
        convertedPan = y.invert(ypan);
        maxPaddingHeight = y(max)- (height-2*margin);
        if (convertedPan >= 0) { ypan = y(0); }
        if (convertedPan <= -( maxPaddingHeight )) { ypan = -(y( maxPaddingHeight )); }
        d3.select(".log").attr("transform", "translate(0,"+ypan+")");
          //dragged.x = x.invert(Math.max(0, Math.min(width, m[0])));
          //dragged.y = y.invert(Math.max(0, Math.min(height, m[1])));
          //d3.select(".log").attr("transform", "translate(0,"+ypan+")");
       // update();
    }
 
    function mouseup() {
      if (!origin) { return; }
        mousemove();
        origin = null;
    }


 
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
var branches;


var line = d3.svg.diagonal().projection( function (d) { return [d.x, d.y] } );
var initline = d3.svg.diagonal().projection( function (d) {
  if (d.y < min) {
    return [d.x, margin];
  } else if (d.y > max) {
    return [d.x, height-margin];
  }
  return [d.x, margin];} 
);

// buttons
//var resetButton = d3.select("svg").append("g").attr("class","button reset");
//resetButton.append("circle")
//      .attr("cx", 1000)
//      .attr("cy", 200)
//      .attr("r", 30)
//      .on("click", resetZoom);
//resetButton.append("text")
//        .attr("x", 1000)
//        .attr("y", 200)
//        .text("Reset")
//        .on("click", resetZoom);
//var zoomButton = d3.select("svg").append("g").attr("class","button zoom");
//zoomButton.append("circle")
//      .attr("cx", 1000)
//      .attr("cy", 270)
//      .attr("r", 30)
//      .on("click", initBrush);
//zoomButton.append("text")
//        .attr("x", 1000)
//        .attr("y", 270)
//        .text("Zoom")
//        .on("click", initBrush);

function updateData() {
	if (currentCommits.length > 50) {
		currentCommits = currentCommits.slice(1, currentCommits.length);
		redraw(currentCommits);
	}
}

var maxLane = 0;

d3.json("data.json", function(jrep) {
	repository = jrep;
	currentCommits = repository.commits;
  branches = repository.branches;
  max = d3.max(currentCommits, function(d) { return d.position;});
  min = d3.min(currentCommits, function(d) { return d.position;});
  maxPaddingHeight = (((height-margin)*commitPadding)-height)+2*margin;
  minPaddingHeight = 0;
  y = d3.scale.linear().domain([min, max]).range([margin, (height-margin)*commitPadding]);
	redraw(currentCommits);
});


function click(commit) {
	//alert('Clicked commit '+commit.id);
  var clickedCommit = getCommit(commit.id);
  if (clickedCommit !== null) {
    alert(clickedCommit.message);
  }
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



//var zoom = d3.behavior.zoom().on('zoom', pan);
//svg.call(zoom);

svg.on("wheel", mozPan);
svg.on("mousewheel", miscPan)
function miscPan() {
  //console.log(d3.event);
  // IE : MouseWheelEvent
  // Opera : MouseEvent
  // Chrome / Safari : WheelEvent wheelDeltaY ?
}
function mozPan() {
  // Firefox (Gecko 17+) : wheel
  // deltaY in delta mode 0, amount of pixels
  //TODO ABSOLUTELY !!!!! DO IT DO IT DO IT DO IT : try this : http://www.sitepoint.com/html5-javascript-mouse-wheel/
  var delta = +d3.event.deltaY;
  currentPan = currentPan+delta;
  if (currentPan < (-maxPaddingHeight)) {
    currentPan = -maxPaddingHeight;
  } else if (currentPan > 0) {
    currentPan = 0;
  }
  svgGroup.attr("transform", "translate(0, "+currentPan+")");
  //console.log(currentPan);
}


/*function pan() {
  console.log(d3.mouse);
  var currentTranslate = zoom.translate()[1];
  var panDirection = 1;
  if (currentTranslate - originalTranslate < 0) {
    panDirection = -1;
  } 
  
  var panFactor = 25;
  originalTranslate = currentTranslate;
  currentPan = currentPan+panDirection*panFactor;
  if (currentPan > maxPaddingHeight) { currentPan = maxPaddingHeight; }
  else if (currentPan < minPaddingHeight) { currentPan = minPaddingHeight; }
  svgGroup.transition().duration(0.1).attr("transform", "translate(0, " + -currentPan + ")");

}*/
//svg.call(d3.behavior.zoom().scaleExtent([1,1])
//      .on("zoom", function() {
        
        //convertedPan = y.invert(ypan);
        //var maxPaddingHeight = (max*commitPadding) - y.invert(height-4*margin);
        //  if (convertedPan >= 0) { ypan = y(0); }
        //  if (convertedPan <= -( maxPaddingHeight )) { ypan = -(y( maxPaddingHeight )); }
        //d3.select(".log").attr("transform", "translate(0,"+ypan+")");
        //var currentTranslate = d3.event.translate[1];
        //var delta = currentTranslate - originalTranslate;
        //originalTranslate = currentTranslate;
        //currentPan = currentPan + delta;
        //var convertedPan = y.invert(currentPan);
        
        //if (currentTranslate <= -(maxPaddingHeight)) { currentTranslate = -(maxPaddingHeight); d3.event.translate[1] = y(currentTranslate); }
//        console.log("ypan : "+ypan);
        //svgGroup.attr("transform", "translate(0, " + d3.event.translate[1] + ")");
//      }
//    )
//  );

function zoom2() {
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

function getCommit(id) {
  for (var i=0; i< currentCommits.length; i++) {
    if (currentCommits[i].id == id) {
      return currentCommits[i];
    }
  }
  return null;
}

function redraw(commits) {
  previousMax = max;
  previousMin = min;
	maxLane = d3.max(commits, function(d) { return d.lane;})+1;
  svg.select("#clipMessage").select("rect")
    .attr("width", x(maxAllowedLane-5)-margin)
    .attr("height", y(max));
  svg.select("#clipName").select("rect")
    .attr("width", x(maxAllowedLane-2)-margin)
    .attr("height", y(max));
  

	var nodes = [];
  var links = [];
  var infos = [];
  commits.forEach(function(d, i) {
      var source = {"id": d.id ,"x": x(d.lane), "y": y(d.position)};
      nodes.push(d);
      //var info = {"id": d.id ,"committer": d.committerName, "date": d.date, "x": x(d.lane)+margin, "y": y(d.position)};
      var info = {
        "id": d.id,
        "message": d.message,
        "author": d.authorName,
        "date": formatMillisecondDate(d.date),
        "x": x(d.lane)+margin,
        "y": y(d.position)
      };
      infos.push(info);
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

  
	var infosSelection = infosGroup.selectAll(".info").data(infos, key);
  var infosEnterSelection = infosSelection.enter().append("g").attr("class", "info")
    .on('mouseover', function(){d3.select(this).classed("selected", true);})
    .on('mouseout', function(){d3.select(this).classed("selected", false);});;
  var infosExitingSelection = infosSelection.exit(); 

  infosEnterSelection.append("rect").attr("class", "infoHolder")
      .attr("x", function(d) { return x(maxLane); })
      .attr("y", function(d) { return d.y - margin- commitPadding + 2; })
      .attr("width", function(d) { return x(maxLane+1); })
      .attr("height", function(d) { return (2*margin)+commitPadding; } );
  infosEnterSelection.append("line").attr("class", "infoLine")
      .attr("x1", function(d) { return d.x+x(0.5); })
      .attr("y1", function(d) { return d.y; })
      .attr("x2", function(d) { return x(maxLane+1); })
      .attr("y2", function(d) { return d.y; });
  var textTable = infosEnterSelection.append("text").attr("class", "commitInfo")
      .attr("x", function(d) { return x(maxLane+1)+margin; })
      .attr("y", function(d) { return d.y; })
      .attr("dy", ".30em")
  textTable.append("tspan")
      .attr("x", function(d) { return x(maxLane+1)+margin; })
      .text(function (d) { return d.id.substring(0,7); })
  textTable.append("tspan")
      .attr("x", function(d) { return x(maxLane+3)+margin; })
      .attr("clip-path", "url(#clipMessage)")
      .text(function (d) { return d.message; })
  textTable.append("tspan")
      .attr("x", function(d) { return x(maxAllowedLane-5)+margin; })
      .attr("clip-path", "url(#clipName)")
      .text(function (d) { return d.author; });
  textTable.append("tspan")
      .attr("x", function(d) { return x(maxAllowedLane-2)+margin; })
      .text(function (d) { return d.date; });
  //infosEnterSelection.append("text").attr("class", "commitInfo")
  //    .attr("x", function(d) { return x(maxLane+3)+margin; })
  //    .attr("y", function(d) { return d.y; })
  //    .attr("dy", ".30em")
  //    .text(function (d) { return (d.message.length > 40)? d.message.substring(0, 38)+"...": d.message; });
  infosEnterSelection.transition()
      .duration(delay);


  commitsExitingSelection.transition().style("opacity", 1e-6)
      .duration(delay)
      .remove();
  linksExitingSelection.transition().style("opacity", 1e-6)
      .duration(delay)
      .remove();
  infosExitingSelection.transition().style("opacity", 1e-6)
      .duration(delay)
      .remove();


  var t = svg.transition().duration(delay);
	t.selectAll("circle.commit").attr("cy", function(d) { return y(+d.position); });
	t.selectAll("path").attr("d", line);
  t.selectAll(".info.infoLine").attr("y1", function(d) { return d.y; }).attr("y2", function(d) { return d.y; });

  linksExitingSelection.transition().style("opacity", 1e-6).remove();

  


}
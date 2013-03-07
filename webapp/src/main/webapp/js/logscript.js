var min=0, max=0,width=890,height=500,margin=10,padding=100,bgcolor="#FFFFFF"
svg=d3.select("#svgcontainer")
.append("svg")
.attr("width",width)
.attr("height",height)
//.style("border", "solid black 1px")
.style("background", bgcolor);

// The data join should be done on the commits ids.
function key(d) {
	return d.id;
}

function displayInfo(element) {
    $("#commit-id").text(element.id);
    $("#commit-author").text(element.committerName);
    $("#commit-message").text(element.message);
    $("#commit-date").text(element.date);
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

//setInterval(updateData, 1000);

var repository;
var currentCommits;
var originalCommits;
// scalers - scales the commits dates base on the idea that they are displayed vertically. So the scale range uses the height as a max.
// X is scaled by the number of branches (from 1 to 10) - TODO : make it dynamic from the branch count in the data.
var x = d3.scale.linear().domain([0,25]).range([padding+margin,width-margin]);
// Y is scaled by the commit date
//var y = d3.scale.linear().domain([min, max]).range([height-margin, margin]);
var y = d3.scale.linear().domain([min, max]).range([height-margin, margin]);


function updateData() {
	if (currentCommits.length > 1) {
		currentCommits = currentCommits.slice(1, currentCommits.length);
		redraw(currentCommits);
	}
}

d3.json("/git/json/log.do", function(jrep) {
	repository = jrep;
	originalCommits = repository.commits;
	currentCommits = originalCommits;
	redraw(currentCommits);
});

/*var yAxis = d3.svg.axis()
	.scale(y)
	.orient("left")
	.ticks(10)
	.tickFormat(formatMillisecondDate);
var axisElement = svg.append("g")
	    .attr("class", "axis");*/
	    //.attr("transform", "translate(" + 80 + ",0)");

function redraw(commits) {

	var max = commits.length;
    var min = 0;
	y = d3.scale.linear().domain([min, max]).range([height-margin, margin]);



    // Axis
    //yAxis.scale(y);
    //axisElement.call(yAxis);


	var nodes = [];
    var links = [];
    commits.forEach(function(d, i) {
        var source = {"x": x(d.lane), "y": y(d.position)};
        nodes.push(d);
        if (d.children !== null) {
	        d.children.forEach(function(c, i) {
	        	var target = {"x": x(c.lane), "y": y(c.position)};
	            links.push({"source": source, "target": target});
	        });
    	}
    });
    //var line = d3.svg.line()
    //	.attr("x1", function(d) {return d.source.x; })
    //	.attr("y1", function(d) {return d.source.y; })
    //	.attr("x2", function(d) {return d.target.x; })
    //	.attr("y2", function(d) {return d.target.y; })

    var diagonal = d3.svg.diagonal().projection(function(d) {
    	return [d.x, d.y];
    });

	var linksSelection = svg.selectAll(".link").data(links);
	var linksEnterSelection = linksSelection.enter();
	var linksExitingSelection = linksSelection.exit();

    var enteringLinks = linksEnterSelection.append("path")
        .attr("class", "link arrow")
		.attr("marker-end", "url(#arrow)")
        .attr("d", diagonal)
        .style("stroke", "#000000")
        .style("stroke-width", "2px")
        .style("fill", bgcolor);

	// Create a selection of circles for commits
	var commitsSelection = svg.selectAll(".circle").data(nodes, key);
	var commitsEnterSelection = commitsSelection.enter().append("g").attr("class", "circle");
	var commitsExitingSelection = commitsSelection.exit();

	// Create a circle for each commit and set it in the top left angle
	var enteringCommits = commitsEnterSelection.append("circle")
			.attr("id", function(d) {return d.id;})
			.attr("cx", function(d) {return x(d.lane);})
			.attr("cy", function(d) {return y(+d.position);})
			.attr("r", 4)
			.style("fill", "#999999")
			.style("stroke", "#333333") // 99AAFF
			.style("stroke-width", "2px");

	enteringLinks.transition()
      .duration(delay)
      .style("opacity", 1);

    enteringCommits.transition()
      .duration(delay)
      .style("opacity", 1);

    commitsExitingSelection.transition()
      .duration(delay)
      .style("opacity", 1e-6)
      .remove();

    commitsSelection
		.transition()
        .duration(delay)
        .style("opacity", 1);

    var t = svg.transition()
		.duration(delay);


	t.selectAll("circle")
		.attr("cy", function(d) { return y(+d.position); });

	t.selectAll("path").attr("d", diagonal);


    linksExitingSelection.transition()
      .duration(delay)
      .style("opacity", 1e-6)
      .remove();





	// transition function
	//svg.selectAll("circle").transition().duration(1000)
    //		.attr("cx",)
    //		.attr("cy",);
}
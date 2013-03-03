var width=915,height=500,margin=10,padding=100,
svg=d3.select("#svgcontainer")
.append("svg")
.attr("width",width)
.attr("height",height);

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

function formatMillisecondDate(d) {
	var format = d3.time.format("%Y, %b %d");
	return format(new Date(d));
}

d3.json("/git/json/log.do", function(data) {

	var max = d3.max(data.commits,
            function (d) {
                return d.date;
            }
    );
    var min = d3.min(data.commits,
            function (d) {
                return d.date;
            }
    );

    // scalers - scales the commits dates base on the idea that they are displayed vertically. So the scale range uses the height as a max.
    // X is scaled by the number of branches (from 1 to 10) - TODO : make it dynamic from the branch count in the data.
	var x = d3.scale.linear().domain([0,50]).range([padding+margin,width-margin]);
	// Y is scaled by the commit date
	var y = d3.scale.linear().domain([min, max]).range([height-margin, margin]);

    // Axis
  	var yAxis = d3.svg.axis()
		.scale(y)
		.orient("left")
		.ticks(10)
		.tickFormat(formatMillisecondDate);
    svg.append("g")
	    .attr("class", "axis")
	    .attr("transform", "translate(" + 80 + ",0)")
	    .call(yAxis);


	var nodes = [];
    var links = [];
    data.commits.forEach(function(d, i) {
        var source = {"x": x(d.lane), "y": y(+d.date)};
        nodes.push(d);
        if (d.parents !== null) {
	        d.parents.forEach(function(c, i) {
	        	var target = {"x": x(c.lane), "y": y(+c.date)};
	            links.push({"source": source, "target": target});
	        });
    	}
    });
    var diagonal = d3.svg.diagonal().projection(function(d) { return [d.x, d.y]; });

	var linksSelection = svg.selectAll(".link")
        .data(links)
        .enter();

    linksSelection.append("path")
        .attr("class", "link")
        .attr("d", diagonal)
        .style("stroke", "#99AAFF")
        .style("stroke-width", "2px")
        .style("fill", "#FFFFFF");

	// Create a selection of circles for commits
	var commitsSelection = svg.selectAll(".circle").data(nodes, key).enter().append("g").attr("class", "circle");;

	// Create a circle for each commit and set it in the top left angle
	commitsSelection.append("circle")
			.attr("cx", function(d) {return x(d.lane);})
			.attr("cy", function(d) {return y(+d.date);})
			.attr("r", 4)
			.style("fill", "#FFFFFF")
			.style("stroke", "#8888FF")
			.style("stroke-width", "2px");



	// transition function
	//svg.selectAll("circle").transition().duration(1000)
    //		.attr("cx",)
    //		.attr("cy",);
});
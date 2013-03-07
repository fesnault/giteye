var min=0, max=0,width=960,height=500,margin=10,padding=100,bgcolor="#FFFFFF";

// Creates canvas 320 × 200 at 10, 50
var paper = Raphael("workplace",width, height);
paper.rect(0, 0, width-1, height-1).attr({"stroke": "#666", "stroke-width": "1"});

// Creates circle at x = 50, y = 40, with radius 10
var circle = paper.circle(50, 40, 10);
// Sets the fill attribute of the circle to red (#f00)
circle.attr("fill", "#f00");

// Sets the stroke attribute of the circle to white
circle.attr("stroke", "#fff");
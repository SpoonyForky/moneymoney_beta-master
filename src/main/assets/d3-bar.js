var margin = {top: 40, right: 20, bottom: 30, left: 40},
    width = window.outerWidth - margin.left - margin.right,
    height = window.outerHeight - margin.top - margin.bottom;

var formatPercent = d3.format(".0%");

var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .1);

var y = d3.scale.linear()
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickFormat(formatPercent);

var tip = d3.tip()
  .attr('class', 'd3-tip')
  .offset([-10, 0])
  .html(function(d) {
    return "<strong>Frequency:</strong> <span style='color:red'>" + d.frequency + "</span>";
  })

var svg = d3.select("body").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

svg.call(tip);


    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://moneymoney.zapto.org:8080",
        async: true,
        data: "{}",
        success: function (data){

             //console.log("http://moneymoney.zapto.org:8080");
                var descArray = new Array();
                var amtArray = new Array();
                var p = [];
                var count = 0;

                for(var i = 0; i < data.length; i++){
                  if (data[i].type == "expense"){
                      p[count] = data[i];
                      p[count] = data[i];

                      console.log(count);
                      count = count+1;
                  }
                }


            x.domain(p.map(function(p) { return p.desc; }));
            y.domain([0, d3.max(p,(function(p) { return p.amount; }))]);

 //this adds an axies
             svg.append("g")
                 .attr("class", "x axis")
                 .attr("transform", "translate(0," +height+ ")")
             .call(xAxis)
             .selectAll("text")
             .style("text-anchor", "end")
             .attr("dx", "-.8em")
             .attr("dy", "-.55em")
             .attr("transform", "rotate(-90)");

                 //work on y
             svg.append("g")
             .attr("class", "y axis")
         .call(yAxis)
         .append("text")
         .attr("transform", "rotate(-90)")
         .attr("y", 5)
         .attr("dy", ".71em")
         .style("text-anchor", "end")
         .text("Amount");

             ///Add bar chart

             svg.selectAll("bar")
             .data(p)
             .enter().append("rect")
             .attr("class", "bar")
            .attr("x", function (p) { return x(p.desc); })
            //     .attr("x", function (descArry) { return x(descArry); })
             .attr("width", x.rangeBand())
             .attr("y", function (p) { return y(p.amount); })
           //       .attr("y", function (amntArry) { return y(amntArry); })
            .attr("height", function (p) { return height - y(p.amount); });
           //      .attr("height", function (amntArry) { return height - y(amntArry); });



             ///
             //var xAxis = d3.svg.axis();
             //xAxis.orient('bottom').scale()
   } });
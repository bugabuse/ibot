google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(loadChart);

function loadChart() {

    console.log("Load shit");
    http.get(host + '/history/saleschart/')
        .success(function(data) {
           // console.log("Witam!" + data.chart);
            drawChart(data.chart);
        });
}

function drawChart(rows) {
   
   /* var data = new google.visualization.DataTable();

    data.addColumn('string', 'Name');
    data.addColumn('number', 'Median price');
    data.addColumn('number', 'Amount sold(in millions)');

    data.addRows(rows);*/

    var data = google.visualization.arrayToDataTable(rows);

    var options = {

        focusTarget:'category',
        backgroundColor: {
            stroke:'#000000'
        },

        hAxis: {
            
            baselineColor:'red',
            textStyle: {
                color: '#ffffff',
                opacity: 0.8,
            }
        },
        vAxis: {
            format:'currency',
            gridlines : {
                color:'#2c3e50',
            },

            textStyle: {
                color: '#ffffff',
                opacity: 0.8
            }
        },

        legend: {
            textStyle: {
                color: '#ffffff',
                opacity: 0.8
            }
        },

        series: {
            0: {
                type: 'line'
            },
            1: {
                type: 'line',
                color: 'red',
                visibleInLegend:false,
                targetAxisIndex:100,
                lineWidth: 0
            }
        },

        lineWidth:3,
        curveType: 'function',
        backgroundColor: '#0a0f16'
    };

    var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));


    chart.draw(data, options);
}



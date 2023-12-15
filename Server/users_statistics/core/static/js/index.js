// //EXAMPLE GRAPH

// // Initialize the echarts instance based on the prepared dom
// var myChart = echarts.init(document.getElementById('main'));

// // Specify the configuration items and data for the chart
// var option = {
//   title: {
//     text: 'ECharts Getting Started Example'
//   },
//   tooltip: {},
//   legend: {
//     data: ['sales']
//   },
//   xAxis: {
//     data: ['Shirts', 'Cardigans', 'Chiffons', 'Pants', 'Heels', 'Socks']
//   },
//   yAxis: {},
//   series: [
//     {
//       name: 'sales',
//       type: 'bar',
//       data: [5, 20, 36, 10, 10, 20]
//     }
//   ]
// };

// // Display the chart using the configuration items and data just specified.
// myChart.setOption(option);

// //  console.log(document.getElementById('ages-distribution-graph-data'));

var fallsPerAgeDistributionGraphData;

document.addEventListener("DOMContentLoaded", function() {
  var fallsPerAgeDistributionGraphDataElement = document.getElementById('falls-per-age-distribution-graph-data');
  fallsPerAgeDistributionGraphData = JSON.parse(fallsPerAgeDistributionGraphDataElement.getAttribute('data-chart-data').replace(/\'/g, '"'));

  var fallsPerAgeDistributionGraph = echarts.init(document.getElementById('falls-per-age-distribution-graph'));

  fallsPerAgeDistributionGraphOption = {
    title: {
      text: 'Number of falls per age',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: function(params) {
        // Customize the tooltip content
        return 'Falls at age ' + params.name + ': <strong>' + params.value + '</strong>';
      },
    },
    series: [
      {
        name: 'Falls at age',
        type: 'pie',
        avoidLabelOverlap: false,
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
        },
        labelLine: {
          show: false
        },
        data: fallsPerAgeDistributionGraphData
      }
    ]
  };

  console.log(fallsPerAgeDistributionGraphData);
  fallsPerAgeDistributionGraph.setOption(fallsPerAgeDistributionGraphOption);
});
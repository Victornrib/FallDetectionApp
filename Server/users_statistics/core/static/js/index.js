document.addEventListener("DOMContentLoaded", function() {
  setFallsPerAgeLineGraph();
  setFallsPerSexBarGraph();
  setFallsPerMovementDisorderDistributionGraph();
});


function setFallsPerAgeLineGraph() {
  var fallsPerAgeLineGraph = echarts.init(document.getElementById('falls-per-age-line-graph'));
  var fallsPerAgeLineGraphDataElement = document.getElementById('falls-per-age-line-graph-data');
  var fallsPerAgeLineGraphData = JSON.parse(fallsPerAgeLineGraphDataElement.getAttribute('data-chart-data').replace(/\'/g, '"'));

  fallsPerAgeLineGraphOption = {
    title: {
      text: 'Number of falls per age',
      left: 'center',
      textStyle: {
        fontSize: 22
      }
    },
    tooltip: {
      trigger: 'item',
      textStyle: {
        fontSize: '16',
      },
    },
    xAxis: {
      name: 'Age',
      nameTextStyle: {
        fontSize: 14
      },
      type: 'category',
      data: fallsPerAgeLineGraphData.ages,
      axisLabel: {
        fontSize: 16
      }
    },
    yAxis: {
      name: 'Falls',
      nameTextStyle: {
        fontSize: 16
      },
      type: 'value',
      axisLabel: {
        fontSize: 16
      }
    },
    series: [
      {
        name: 'Falls',
        nameTextStyle: {
          fontSize: 16
        },
        lineStyle: {
          width: 3
        },
        symbolSize: 12,
        data: fallsPerAgeLineGraphData.falls,
        type: 'line',
      }
    ]
  };

  fallsPerAgeLineGraph.setOption(fallsPerAgeLineGraphOption);
}


function setFallsPerSexBarGraph() {
  var fallsPerSexBarGraph = echarts.init(document.getElementById('falls-per-sex-bar-graph'));
  var fallsPerSexBarGraphDataElement = document.getElementById('falls-per-sex-bar-graph-data');
  var fallsPerSexBarGraphData = JSON.parse(fallsPerSexBarGraphDataElement.getAttribute('data-chart-data').replace(/\'/g, '"'));

  fallsPerSexBarGraphOption = {
    title: {
      text: 'Number of falls per sex',
      left: 'center',
      textStyle: {
        fontSize: 22
      }
    },
    tooltip: {
      trigger: 'item',
      textStyle: {
        fontSize: '16',
      },
    },
    xAxis: {
      name: 'Sex',
      nameTextStyle: {
        fontSize: 16
      },
      type: 'category',
      data: fallsPerSexBarGraphData.sexes,
      axisLabel: {
        fontSize: 16
      }
    },
    yAxis: {
      name: 'Falls',
      nameTextStyle: {
        fontSize: 16
      },
      type: 'value',
      axisLabel: {
        fontSize: 16
      }
    },
    series: [
      {
        name: 'Falls',
        nameTextStyle: {
          fontSize: 16
        },
        data: fallsPerSexBarGraphData.falls,
        type: 'bar'
      }
    ]
  };

  fallsPerSexBarGraph.setOption(fallsPerSexBarGraphOption);
}


function setFallsPerMovementDisorderDistributionGraph() {
  var fallsPerMovementDisorderDistributionGraph = echarts.init(document.getElementById('falls-per-movement-disorder-distribution-graph'));
  var fallsPerMovementDisorderDistributionGraphDataElement = document.getElementById('falls-per-movement-disorder-distribution-graph-data');
  var fallsPerMovementDisorderDistributionGraphData = JSON.parse(fallsPerMovementDisorderDistributionGraphDataElement.getAttribute('data-chart-data').replace(/\'/g, '"'));

  fallsPerMovementDisorderDistributionGraphOption = {
    title: {
      text: 'Number of falls per movement disorder',
      left: 'center',
      textStyle: {
        fontSize: '22',
      },
    },
    tooltip: {
      trigger: 'item',
      textStyle: {
        fontSize: '16',
      },
      formatter: function(params) {
        var colorCircle = '<span style="display:inline-block;margin-right:5px;border-radius:50%;width:10px;height:10px;background-color:' + params.color + '"></span>';
        var tooltipContent = 'Falls<br>' + colorCircle + params.name + ': <b>' + params.value + '</b> (' + params.percent + '%)';
        return tooltipContent;
      },
    },
    label: {
      show: true,
      fontSize: 16,
    },
    series: [
      {
        name: 'Falls',
        nameTextStyle: {
          fontSize: 16
        },
        type: 'pie',
        radius: '50%',
        data: fallsPerMovementDisorderDistributionGraphData,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  };

  fallsPerMovementDisorderDistributionGraph.setOption(fallsPerMovementDisorderDistributionGraphOption);
}








// var fallsPerAgeDistributionGraphData;

// document.addEventListener("DOMContentLoaded", function() {
//   var fallsPerAgeDistributionGraphDataElement = document.getElementById('falls-per-age-distribution-graph-data');
//   fallsPerAgeDistributionGraphData = JSON.parse(fallsPerAgeDistributionGraphDataElement.getAttribute('data-chart-data').replace(/\'/g, '"'));

//   var fallsPerAgeDistributionGraph = echarts.init(document.getElementById('falls-per-age-distribution-graph'));

//   fallsPerAgeDistributionGraphOption = {
//     title: {
//       text: 'Number of falls per age',
//       left: 'center'
//     },
//     tooltip: {
//       trigger: 'item',
//       formatter: function(params) {
//         // Customize the tooltip content
//         return 'Falls at age ' + params.name + ': <strong>' + params.value + '</strong>';
//       },
//     },
//     series: [
//       {
//         name: 'Falls at age',
//         type: 'pie',
//         avoidLabelOverlap: false,
//         itemStyle: {
//           borderColor: '#fff',
//           borderWidth: 2
//         },
//         label: {
//           show: false,
//         },
//         labelLine: {
//           show: false
//         },
//         data: fallsPerAgeDistributionGraphData
//       }
//     ]
//   };

//   console.log(fallsPerAgeDistributionGraphData);
//   fallsPerAgeDistributionGraph.setOption(fallsPerAgeDistributionGraphOption);
// });
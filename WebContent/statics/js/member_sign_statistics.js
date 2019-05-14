$(function(){
	
	//获取本周周一的日期
	var now = new Date();//今天的日期
	var toDay = now.getDay();//今天星期几
	var days = toDay - 1;//今天和本周一差几天
	var millSecondOfDay = 24*60*60*1000;//一天的毫秒数
	var mondayOfWeek = new Date(now.getTime()-(days*millSecondOfDay));//本周一的日期
	var mondayOfWeekF = dateFormat(mondayOfWeek);//格式化后的本周一日期 格式为yyyy-MM-dd
	
	var firstDate = new Date(now.getFullYear(), now.getMonth(), 1);//当月的第一天日期
	var firstDateF = dateFormat(firstDate);//格式化后的当月第一天日期 格式为yyyy-MM-dd
	var daysOfMonth = getMonthDays(now.getFullYear(), now.getMonth());
	
	 /**
	  * 获取某月天数
	  */
	  function getMonthDays(nowyear,myMonth)
	  {
	     var monthStartDate = new Date(nowyear, myMonth, 1);
	     var monthEndDate = new Date(nowyear, myMonth + 1, 1);
	     var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);//格式转换
	     return days;
	   }
	
	/**
	 * 日期格式化成 yyyy-MM-dd
	 */
	function dateFormat(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var d = date.getDate();
		return year + '-' + (month>9?month:('0'+month)) + '-' + (d>9?d:('0'+d));
	}
	
	$('#member_sign_statistics_table').bootstrapTable({
		url:'statistics/getMemberSignStatistics.do?mondayDate='+mondayOfWeekF+'&firstDateOfMonth='+firstDateF+'&daysOfMonth='+daysOfMonth,
		pagination: true,
		clickToSelect: true ,
		method:'GET',
		sidePagination: 'client',
		pageSize: 10,
		onLoadSuccess:function(){
			
		}
	});
	
	/**
	 * 获取周出勤人数和缺勤人数
	 */
	$.ajax({
		url:'statistics/getWeeklyAttendanceAndNot.do',
		type:'POST',
		data:{'mondayDate' : mondayOfWeekF},
		dataType:'json',
		success:function(result){
			
			//图表数据
			var areaChartData = result;
			
			var barChartCanvas                   = $('#barChart').get(0).getContext('2d')
		    var barChart                         = new Chart(barChartCanvas)
		    var barChartData                     = areaChartData
		    barChartData.datasets[1].fillColor   = '#00a65a'
		    barChartData.datasets[1].strokeColor = '#00a65a'
		    barChartData.datasets[1].pointColor  = '#00a65a'
		    var barChartOptions                  = {
		      //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
		      scaleBeginAtZero        : true,
		      //Boolean - Whether grid lines are shown across the chart
		      scaleShowGridLines      : true,
		      //String - Colour of the grid lines
		      scaleGridLineColor      : 'rgba(0,0,0,.05)',
		      //Number - Width of the grid lines
		      scaleGridLineWidth      : 1,
		      //Boolean - Whether to show horizontal lines (except X axis)
		      scaleShowHorizontalLines: true,
		      //Boolean - Whether to show vertical lines (except Y axis)
		      scaleShowVerticalLines  : true,
		      //Boolean - If there is a stroke on each bar
		      barShowStroke           : true,
		      //Number - Pixel width of the bar stroke
		      barStrokeWidth          : 2,
		      //Number - Spacing between each of the X value sets
		      barValueSpacing         : 5,
		      //Number - Spacing between data sets within X values
		      barDatasetSpacing       : 1,
		      //String - A legend template
		      legendTemplate          : '<ul class="<%=name.toLowerCase()%>-legend"><% for (var i=0; i<datasets.length; i++){%><li><span style="background-color:<%=datasets[i].fillColor%>"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>',
		      //Boolean - whether to make the chart responsive
		      responsive              : true,
		      maintainAspectRatio     : true
		    }

		    barChartOptions.datasetFill = false;
		    barChart.Bar(barChartData, barChartOptions);
			
			
			
		}
	});
	
	
	/*//图标数据
	var areaChartData = {
	  labels  : ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
	  datasets: [
	    {
	      label               : 'Electronics',
	      fillColor           : 'rgba(210, 214, 222, 1)',
	      strokeColor         : 'rgba(210, 214, 222, 1)',
	      pointColor          : 'rgba(210, 214, 222, 1)',
	      pointStrokeColor    : '#c1c7d1',
	      pointHighlightFill  : '#fff',
	      pointHighlightStroke: 'rgba(220,220,220,1)',
	      data                : [65, 59, 80, 81, 56, 55, 40]
	    },
	    {
	      label               : 'Digital Goods',
	      fillColor           : 'rgba(60,141,188,0.9)',
	      strokeColor         : 'rgba(60,141,188,0.8)',
	      pointColor          : '#3b8bba',
	      pointStrokeColor    : 'rgba(60,141,188,1)',
	      pointHighlightFill  : '#fff',
	      pointHighlightStroke: 'rgba(60,141,188,1)',
	      data                : [28, 48, 40, 19, 86, 27, 90]
	    }
	  ]
	}
	
	var barChartCanvas                   = $('#barChart').get(0).getContext('2d')
    var barChart                         = new Chart(barChartCanvas)
    var barChartData                     = areaChartData
    barChartData.datasets[1].fillColor   = '#00a65a'
    barChartData.datasets[1].strokeColor = '#00a65a'
    barChartData.datasets[1].pointColor  = '#00a65a'
    var barChartOptions                  = {
      //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
      scaleBeginAtZero        : true,
      //Boolean - Whether grid lines are shown across the chart
      scaleShowGridLines      : true,
      //String - Colour of the grid lines
      scaleGridLineColor      : 'rgba(0,0,0,.05)',
      //Number - Width of the grid lines
      scaleGridLineWidth      : 1,
      //Boolean - Whether to show horizontal lines (except X axis)
      scaleShowHorizontalLines: true,
      //Boolean - Whether to show vertical lines (except Y axis)
      scaleShowVerticalLines  : true,
      //Boolean - If there is a stroke on each bar
      barShowStroke           : true,
      //Number - Pixel width of the bar stroke
      barStrokeWidth          : 2,
      //Number - Spacing between each of the X value sets
      barValueSpacing         : 5,
      //Number - Spacing between data sets within X values
      barDatasetSpacing       : 1,
      //String - A legend template
      legendTemplate          : '<ul class="<%=name.toLowerCase()%>-legend"><% for (var i=0; i<datasets.length; i++){%><li><span style="background-color:<%=datasets[i].fillColor%>"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>',
      //Boolean - whether to make the chart responsive
      responsive              : true,
      maintainAspectRatio     : true
    }

    barChartOptions.datasetFill = false;
    barChart.Bar(barChartData, barChartOptions);*/
	
	
});
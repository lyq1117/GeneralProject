$(function(){
	
	/**
	 * 查询工程完成度、延期率、待完成工程、已完成工程、延期工程、工程总数
	 */
	$.ajax({
		url:'statistics/getProjectSurvey.do',
		type:'POST',
		dataType:'json',
		success: function(result){
			var complete = result.complete;
			var notComplete = result.notComplete;
			var delay = result.delay;
			var allCount = complete.length + notComplete.length + delay.length;
			
			var completePercentage = (complete.length / allCount * 100).toFixed(0);
			var delayPercentage = (delay.length / allCount * 100).toFixed(0);
			
			
			//赋值前端
			$('#project_survey_completePercentage').val(completePercentage).trigger('change');
			$('#project_survey_delayPercentage').val(delayPercentage).trigger('change');
			$('#project_survey_notCompleteCount').text(notComplete.length);
			$('#project_survey_delayCount').text(delay.length);
			$('#project_survey_completeCount').text(complete.length);
			$('#project_survey_totalCount').text(allCount);
		}
	});
	
	//获取本周周一的日期
	var now = new Date();//今天的日期
	var toDay = now.getDay();//今天星期几
	var days = toDay - 1;//今天和本周一差几天
	var millSecondOfDay = 24*60*60*1000;//一天的毫秒数
	var mondayOfWeek = new Date(now.getTime()-(days*millSecondOfDay));//本周一的日期
	var mondayOfWeekF = dateFormat(mondayOfWeek);//格式化后的本周一日期 格式为yyyy-MM-dd
	
	/**
	 * 日期格式化成 yyyy-MM-dd
	 */
	function dateFormat(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var d = date.getDate();
		return year + '-' + (month>9?month:('0'+month)) + '-' + (d>9?d:('0'+d));
	}
	
	/**
	 * 查询企业每日新增工程和完成工程
	 */
	$.ajax({
		url:'statistics/getEnterpriseNewAndCompleteProject.do',
		type:'POST',
		data:{'mondayDate' : mondayOfWeekF},
		dataType:'json',
		success:function(result){
			
			// LINE CHART
		    var line = new Morris.Line({
		      element: 'project_survey_line-chart',
		      resize: true,
		      data: result,
		      xkey: 'x',
		      ykeys: ['create','complete'],
		      labels: ['新增工程', '完成工程'],
		      lineColors: ['#3c8dbc', '#00a65a'],
		      hideHover: 'auto'
		    });
			
		}
	});
	
	/*// LINE CHART
    var line = new Morris.Line({
      element: 'project_survey_line-chart',
      resize: true,
      data: [
        {"x": '2019-05-07', "count": "1314.1314"},
        {"x": '2019-05-06', "count": "2778"},
        {"x": '2019-05-05', "count": "4912"},
        {"x": '2019-05-08', "count": "3767"},
        {"x": '2019-05-10', "count": "6810", "pp" :'155'},
        {"x": '2019-05-09', "count": "5670"},
        {"x": '2019-05-11', "count": "4820", "pp" : "123"}
      ],
      xkey: 'x',
      ykeys: ['count','pp'],
      labels: ['剩余任务数量', '工程'],
      lineColors: ['#3c8dbc', '#00a65a'],
      hideHover: 'auto'
    });*/
	
	
});
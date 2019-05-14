$(function(){
	
	/**
	 * 加载项目进度统计表格
	 */
	$('#project_progress_statistics_table').bootstrapTable({
		url:'statistics/getProjectProgressStatistics.do',
		pagination: false,
		onLoadSuccess:function(){
			$('.project_progress_statistics_leader').bind('mouseover', function(){
				//alert($(this).attr('leaderId') + '-' + $(this).attr('leaderName'));
				var event = window.event;
				$('#project_progress_statistics_floatWin').css('left', event.pageX+10+'px');
				$('#project_progress_statistics_floatWin').css('top', event.pageY+10+'px');
				$('#project_progress_statistics_floatWin').fadeIn('slow');
				$('#project_progress_statistics_floatWin').text($(this).attr('leaderId') + '-' + $(this).attr('leaderName'));
			});
			
			$('.project_progress_statistics_leader').bind('mouseout', function(){
				$('#project_progress_statistics_floatWin').hide();
			});
		}
	});
	
	
});
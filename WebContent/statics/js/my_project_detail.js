$(function(){
	
	$('#my_project_detail_save').bind('click',function(){
		//alert('click save...');
		var projectId = $('#my_project_detail_id').val();
		var projectName = $('#my_project_detail_projectName').val();
		var projectDescription = $('#my_project_detail_projectDescription').val();
		var projectCreateTime = $('#my_project_detail_createTime').val();
		var projectDuration = $('#my_project_detail_duration').val();
		var projectStatus = $('#my_project_detail_status').val();
		/*alert(projectId + '-' + projectName + '-' + projectDescription + '-' + 
				projectCreateTime + '-' + projectDuration + '-' + projectStatus);*/
		if(isEmpty(projectId) || isEmpty(projectName) || isEmpty(projectDescription) || 
				isEmpty(projectCreateTime) || isEmpty(projectDuration) || 
				isEmpty(projectStatus) ){
			alert('工程信息不能为空');
			return;
		}
		//发送ajax请求，更新工程信息
		$.ajax({
			url:'task/updateProject.do',
			type:'POST',
			data:{'projectId':projectId,
				  'projectName':projectName,
				  'projectDescription':projectDescription,
				  'projectCreateTime':projectCreateTime,
				  'projectDuration':projectDuration,
				  'projectStatus':projectStatus},
			dataType:'json',
			success:function(result){
				if(result.result == "true")
					alert('修改成功!');
				else
					alert('修改失败!');
			}
		});
	});
	
	//判断字符串是否空
	function isEmpty(str){
		return str==null || str=="" ;
	}
	
	//添加成员按钮
	$('#my_project_detail_addMember').bind('click', function(){
		//alert('添加成员按钮');
		//工程id
		var projectId = $('#my_project_detail_id').val();
		//发送ajax，查询通讯录(只查不是该工程成员的用户)
		$.ajax({
			url:'task/getMembersNotInProject.do',
			type:'POST',
			data:{'projectId' : projectId},
			dataType:'json',
			success: function(result){
				var html = '';
				$.each(result, function(i, n){
					html += '<li><a href="javascript:void(0)" class="my_project_detail_addMember_window_eachMember" name="' + n.username + '" style="color:gray">&nbsp;' + n.username + '-' + n.name + '<span class="pull-left badge bg-green">' + n.name.substring(0,1) + '</span></a></li>';
				});
				//添加不是工程成员的用户列表
				$('#my_project_detail_addMember_window_ul').html(html);
				//点击用户，把用户添加进工程的事件
				$('.my_project_detail_addMember_window_eachMember').bind('click', function(){
					var username = $(this).attr('name');
					//发送ajax，把用户添加进工程成员
					$.ajax({
						url:'task/addUserProject.do',
						type:'POST',
						data:{'username' : username,
							  'projectId' : projectId},
						dataType:'json',
						success: function(result){
							if(result.result == 'true'){
								alert('添加成员成功!');
								//添加成功后，关闭通讯录窗口
								$('#my_project_detail_addMember_window').modal('hide');
								//刷新工程成员表格
								$('#my_project_detail_membersTable').bootstrapTable('refresh');
							}
							else{
								alert('添加成员失败!');
								//关闭通讯录窗口
								$('#my_project_detail_addMember_window').modal('hide');
							}
						}
					});
				});
			}
		});
		//打开通讯录窗口
		$('#my_project_detail_addMember_window').modal('show');
	});
	
	//点击项目进度
	$('#my_project_detail_progress').bind('click', function(){
		
		$('#my_project_detail_progress').removeClass('navigation_bar_active');
		$('#my_project_detail_progress').removeClass('navigation_bar');
		$('#my_project_detail_progress').addClass('navigation_bar_active');
		
		$('#my_project_detail_weekly').removeClass('navigation_bar_active');
		$('#my_project_detail_weekly').removeClass('navigation_bar');
		$('#my_project_detail_weekly').addClass('navigation_bar');
		
		$('#my_project_detail_notice').removeClass('navigation_bar_active');
		$('#my_project_detail_notice').removeClass('navigation_bar');
		$('#my_project_detail_notice').addClass('navigation_bar');
		
		$('#my_project_detail_weekly_panel').hide();//隐藏“任务周报”面板
		$('#my_project_detail_progress_panel').show();//显示“项目进度”面板
		$('#my_project_detail_notice_panel').hide();//隐藏“成员出勤”面板
	});
	//点击任务周报
	$('#my_project_detail_weekly').bind('click', function(){
		
		$('#my_project_detail_progress').removeClass('navigation_bar_active');
		$('#my_project_detail_progress').removeClass('navigation_bar');
		$('#my_project_detail_progress').addClass('navigation_bar');
		
		$('#my_project_detail_weekly').removeClass('navigation_bar_active');
		$('#my_project_detail_weekly').removeClass('navigation_bar');
		$('#my_project_detail_weekly').addClass('navigation_bar_active');
		
		$('#my_project_detail_notice').removeClass('navigation_bar_active');
		$('#my_project_detail_notice').removeClass('navigation_bar');
		$('#my_project_detail_notice').addClass('navigation_bar');
		
		$('#my_project_detail_progress_panel').hide();//隐藏“项目进度”面板
		$('#my_project_detail_weekly_panel').show();//显示“任务周报”面板
		$('#my_project_detail_notice_panel').hide();//隐藏“成员出勤”面板
	});
	//点击项目公告
	$('#my_project_detail_notice').bind('click', function(){
		$('#my_project_detail_progress').removeClass('navigation_bar_active');
		$('#my_project_detail_progress').removeClass('navigation_bar');
		$('#my_project_detail_progress').addClass('navigation_bar');
		
		$('#my_project_detail_weekly').removeClass('navigation_bar_active');
		$('#my_project_detail_weekly').removeClass('navigation_bar');
		$('#my_project_detail_weekly').addClass('navigation_bar');
		
		$('#my_project_detail_notice').removeClass('navigation_bar_active');
		$('#my_project_detail_notice').removeClass('navigation_bar');
		$('#my_project_detail_notice').addClass('navigation_bar_active');
		
		$('#my_project_detail_progress_panel').hide();//隐藏“项目进度”面板
		$('#my_project_detail_weekly_panel').hide();//隐藏“任务周报”面板
		$('#my_project_detail_notice_panel').show();//显示“成员出勤”面板
	});
	
	//获取本周周一的日期
	var now = new Date();//今天的日期
	var toDay = now.getDay();//今天星期几
	var days = toDay - 1;//今天和本周一差几天
	var millSecondOfDay = 24*60*60*1000;//一天的毫秒数
	var mondayOfWeek = new Date(now.getTime()-(days*millSecondOfDay));//本周一的日期
	var mondayOfWeekF = dateFormat(mondayOfWeek);//格式化后的本周一日期 格式为yyyy-MM-dd
	var projectId = $('#my_project_detail_id').val();//工程id
	
	
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
	 * 查询本周的每天剩余的任务数
	 * 并且绘制图表
	 */
	$.ajax({
		url:'task/getBlocksRemainOfWeek.do',
		type:'POST',
		data:{'mondayDate' : mondayOfWeekF,
			  'projectId' : projectId},
		dataType:'json',
		success:function(result){
			// LINE CHART
		    var line = new Morris.Line({
		      element: 'my_project_detail_line-chart',
		      resize: true,
		      data: result/*[
		        {"x": '2019-05-07', "count": "1314.1314"},
		        {"x": '2019-05-06', "count": "2778"},
		        {"x": '2019-05-05', "count": "4912"},
		        {"x": '2019-05-10', "count": "3767"},
		        {"x": '2019-05-22', "count": "6810"},
		        {"x": '2019-05-12', "count": "5670"},
		        {"x": '2019-05-13', "count": "4820"},
		        {"x": '2019-05-14', "count": "15073"},
		        {"x": '2019-05-15', "count": "10687"},
		        {"x": '2019-05-16', "count": "8432"}
		      ]*/,
		      xkey: 'x',
		      ykeys: ['count'],
		      labels: ['剩余任务数量'],
		      lineColors: ['#3c8dbc'],
		      hideHover: 'auto'
		    });
		    
		  
		    
		}
	});
	
	
	/**
	 * 查询某工程中已完成、未完成、延期任务分别的数目
	 * 并且绘制图表
	 */
	$.ajax({
		url:'task/getCompleteOrNotCountBlocksOfProject.do',
		type:'POST',
		data:{'projectId' : projectId},
		dataType:'json',
		success:function(result){
			var completeCount = 0;
			var notCompleteCount = 0;
			var delayCount = 0;
			var totalCount = 0;
			$.each(result, function(i, n){
				if(n.label == '已完成'){
					completeCount = parseInt(n.value);
				}
				if(n.label == '未完成'){
					notCompleteCount = parseInt(n.value);
				}
				if(n.label == '延期'){
					delayCount = parseInt(n.value);
				}
			});
			totalCount = completeCount + notCompleteCount + delayCount;
			var completePercentage = (completeCount/totalCount*100).toFixed(0);//完成度 保留0位小数
			var delayPercentage = (delayCount/totalCount*100).toFixed(0);//延迟率 保留0位小数
			//任务完成度赋值 显示
			$('#my_project_detail_completePercentage').val(completePercentage).trigger('change');
			//任务延迟率赋值 显示
			$('#my_project_detail_delayPercentage').val(delayPercentage).trigger('change');
			
			//任务未完成个数赋值
			$('#my_project_detail_notCompleteCount').text(notCompleteCount);
			//完成个数赋值
			$('#my_project_detail_completeCount').text(completeCount);
			//延期个数赋值
			$('#my_project_detail_delayCount').text(delayCount);
			//总数任务数赋值
			$('#my_project_detail_totalCount').text(totalCount);
			
			//DONUT CHART
		    var donut = new Morris.Donut({
		      element: 'my_project_detail_sales-chart',
		      resize: true,
		      colors: ["#3c8dbc", "#f56954", "#00a65a"],
		      data: result/*[
		        {"label": "Download Sales", "value": "12"},
		        {"label": "In-Store Sales", "value": "30"},
		        {"label": "Mail-Order Sales", "value": "20"}
		      ]*/,
		      hideHover: 'auto'
		    });
		}
	});
	
	/**
	 * 查询本周新增任务、延期任务、完成任务
	 */
	$.ajax({
		url:'task/getWeeklyBlocks.do',
		type:'POST',
		data:{'mondayDate' : mondayOfWeekF,
			  'projectId' : projectId},
		dataType:'json',
		success: function(result){
			var completeBlocks = result.completeBlocks;
			var delayBlocks = result.delayBlocks;
			var newBlocks = result.newBlocks;
			//数目分别赋值给前端
			$('#my_project_detail_weekly_completeBlocksCount').text(completeBlocks.length);
			$('#my_project_detail_weekly_delayBlocksCount').text(delayBlocks.length);
			$('#my_project_detail_weekly_newBlocksCount').text(newBlocks.length);
			//本周新增任务，放入ToDoList
			$.each(newBlocks, function(i, n){
				var html = '<li><span class="handle"><i class="fa fa-plus-circle"></i></span><span class="text">'+n.description+'</span><small class="label label-warning"><i class="fa fa-clock-o"></i> '+millisecond2Date(n.createTime)+'开始</small></li>';
				$('#my_project_detail_weekly_newToDoList').append(html);
			});
			//本周完成的任务，放入ToDoList
			$.each(completeBlocks, function(i,n ){
				var endTime = getEndDate(n.createTime, n.realDuration);
				var html = '<li><span class="handle"><i class="fa fa-check-square-o"></i></span><span class="text">'+n.description+'</span><small class="label label-success"><i class="fa fa-clock-o"></i> '+millisecond2Date(endTime.getTime())+'完成</small></li>';
				$('#my_project_detail_weekly_completeToDoList').append(html);
			});
			//本周延期任务，放入ToDoList
			$.each(delayBlocks, function(i, n){
				var endTime = getEndDate(n.createTime, n.duration);
				var html = '<li><span class="handle"><i class="fa fa-clock-o"></i></span><span class="text">'+n.description+'</span><small class="label label-danger"><i class="fa fa-clock-o"></i> '+millisecond2Date(endTime.getTime())+'截止</small></li>';
				$('#my_project_detail_weekly_delayToDoList').append(html);
			});
		}
	});
	
	//根据开始日期和工期，获取结束日期
	function getEndDate(createTime, duration){
		var createTimeD = new Date(createTime);
		var millSecondOfDay = 24*60*60*1000;//一天的毫秒数
		return new Date(createTimeD.getTime() + millSecondOfDay * duration);
	}
	
	//毫秒转化成yyyy-MM-dd的日期
	function millisecond2Date(milliSecond){
		var date = new Date(milliSecond);
		return date.getFullYear() + '-' + ((date.getMonth()+1)>9 ? (date.getMonth()+1) : '0' + (date.getMonth()+1))
			   + '-' + (date.getDate()>9 ? date.getDate() : '0' + date.getDate() );
	}
	
	/**
	 * 获取项目公告
	 */
	$.ajax({
		url:'notice/getProjectNotices.do',
		type:'POST',
		data:{'projectId' : projectId},
		dataType:'json',
		success: function(result){
			$.each(result, function(i, n){
				var html = '<div class="col-md-12"><ul class="timeline"><li class="time-label"><span class="bg-red"><i class="fa fa-bullhorn"></i>'+n.title+'</span></li><li><i class="fa fa-comments bg-yellow"></i><div class="timeline-item"><span class="time"><i class="fa fa-clock-o"></i> '+millisecond2Date(n.time)+'</span><h3 class="timeline-header"><a href="#">'+n.user.name+'</a> '+n.user.username+'</h3><div class="timeline-body">'+n.content+'</div></div></li></ul></div>';
				$('#my_project_detail_notice_panel_list').append(html);
			});
		}
	});
	
});
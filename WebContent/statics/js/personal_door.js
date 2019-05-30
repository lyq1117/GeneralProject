$(function(){
	
	/**
	 * 获取当前用户资料
	 */
	$.ajax({
		url:'door/getUserInfo.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			$('#personal_door_icon').attr('src', result.icon);
			$('#personal_door_name').text(result.name);//成员名字
			$('#personal_door_username').text(result.username);//成员用户名
			$('#personal_door_deptName').text(result.dept.name);//部门名字
			$('#personal_door_tel').text(result.tel);//成员电话
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 获取我的审批表格数据
	 */
	$('#personal_door_myApprovalsTable').bootstrapTable({
		url:'door/getMyApprovalTable.do',
		columns:[{
			field:'id',
			title:'审批编号'
		},{
			field:'title',
			title:'标题'
		},{
			field:'status',
			title:'状态'
		}],
		pagination:true,
		pageSize:2,
		sidePagination:'client',
		onLoadError:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	//待我审批点击事件
	$('#personal_door_waitMeApproval').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/application/approval.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//待我签到点击事件
	$('#personal_door_waitMeSign').bind('click', function(){
		window.location.href = "/GeneralProject/page/index.html"
		$("#index_main_content").css('padding','0px');
	});
	
	/**
	 * 获取我的公告
	 */
	$.ajax({
		url:'door/getMyNotices.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			$.each(result, function(i, n){
				var html = '<div class="row personal_door_hoverCursor" style="padding:10px;width:95%;margin: 0 auto;border-bottom: solid 1px rgb(235,235,235)"><div class="col-md-9" style="color:rgb(194,194,194);">'+n.title+'</div><div class="col-md-3">'+n.status+'</div></div>';
				$('#personal_door_myNotices').append(html);
			});
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 初始化今日任务表格
	 */
	$('#personal_door_todayBlocksTable').bootstrapTable({
		url:'door/getTodayBlocksTable.do',
		columns:[{
			field : 'description',
			title : '任务描述'
		},{
			field : 'projectName',
			title : '所属工程'
		},{
			field : 'blockLeader',
			title : '任务组长'
		}],
		pagination:true,
		pageSize:2,
		sidePagination:'client',
		onLoadError:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
});
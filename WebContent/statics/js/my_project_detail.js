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
	
});
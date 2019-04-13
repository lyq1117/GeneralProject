$(function(){
	$('#my_block_detail_save').bind('click', function(){
		//alert('click save..');
		var blockId = $('#my_block_detail_id').val();
		var description = $('#my_block_detail_blockDescription').val();
		var createTime = $('#my_block_detail_createTime').val();
		var duration = $('#my_block_detail_duration').val();
		var status = $('#my_block_detail_status').val();
		//发送ajax保存工程小块(即任务)信息
		$.ajax({
			url:'task/updateBlock.do',
			type:'POST',
			data:{'blockId' : blockId,
				  'description' : description,
				  'createTime' : createTime,
				  'duration' : duration,
				  'status' : status},
			dataType:'json',
			success:function(result){
				if(result.result == 'true')
					alert('更新成功!');
				else
					alert('更新失败!');
			}
		});
	});
	
	//添加工程小块成员按钮事件
	$('#my_block_detail_addMember').bind('click', function(){
		var blockId = $('#my_block_detail_id').val();
		var projectId = $('#my_block_detail_projectId').val();
		//alert(blockId + '---' + projectId);
		//发送ajax，获取不是工程小块(任务)成员的工程成员集合
		$.ajax({
			url:'task/getMembersNotInBlcokInProject.do',
			type:'POST',
			data:{'blockId' : blockId,
				  'projectId' : projectId},
			dataType:'json',
			success: function(result){
				var html = '';
				$.each(result, function(i, n){
					html += '<li><a href="javascript:void(0)" class="my_block_detail_addMember_window_eachMember" name="' + n.username + '" style="color:gray">&nbsp;' + n.username + '-' + n.name + '<span class="pull-left badge bg-green">' + n.name.substring(0,1) + '</span></a></li>';
				});
				//添加不是工程小块成员但是工程成员的用户列表
				$('#my_block_detail_addMember_window_ul').html(html);
				
				//点击用户添加进任务成员
				$('.my_block_detail_addMember_window_eachMember').bind('click', function(){
					var username = $(this).attr('name');
					//alert(username + '...' + blockId);
					//发送ajax,添加用户-工程小块(任务)关联
					$.ajax({
						url:'task/addUserBlock.do',
						type:'POST',
						data:{'username' : username,
							  'blockId' : blockId},
						dataType:'json',
						success: function(result){
							if(result.result == 'true'){
								alert('添加成功!');
								//关闭窗口
								$('#my_block_detail_addMember_window').modal('hide');
								//刷新表格
								$('#my_block_detail_membersTable').bootstrapTable('refresh');
							}
							else{
								alert('添加失败!');
							}
						}
					});
				});
			}
		});
		$('#my_block_detail_addMember_window').modal('show');
	});
	
	
	
});
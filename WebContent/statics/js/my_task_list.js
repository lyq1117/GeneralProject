$(function(){
	/**
	 * 获取当前用户所负责的任务列表
	 */
	$('#my_task_list_table').bootstrapTable({
		url:'task/getBlocksTableByLeaderId.do',
		columns:[{
			field:'id',
			title:'编号'
		},{
			field:'description',
			title:'任务描述'
		},{
			field:'createTime',
			title:'创建日期'
		},{
			field:'duration',
			title:'预计工期'
		},{
			field:'project',
			title:'所属工程'
		},{
			field:'leader',
			title:"负责人"
		}],
		onLoadSuccess:function(){
			
		},
		onLoadError:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		},
		onClickRow:function(row, $element, field){
			//任务id
			var blockId = row.id;
			$('#index_main_content').load('/GeneralProject/page/task/my_block_detail.html', function(){
				//发送ajax获取工程小块信息
				$.ajax({
					url:'task/getBlockById.do',
					type:'POST',
					data:{'blockId' : blockId},
					dataType:'json',
					success: function(result){
						//alert(JSON.stringify(result));
						$('#my_block_detail_id').val(result.id);
						$('#my_block_detail_blockDescription').val(result.description);
						$('#my_block_detail_createTime').val(millisecond2Date(result.createTime));
						$('#my_block_detail_duration').val(result.duration);
						$('#my_block_detail_status').val(result.status);
						$('#my_block_detail_leader').val(result.leader.username + '-' + result.leader.name);
						
						//当任务状态为1--废弃  或者2--正常结束完成  所有信息不可以更改。
						if(result.status == 2 || result.status == 1){
							$('#my_block_detail_blockDescription').prop('disabled', true);
							$('#my_block_detail_createTime').prop('disabled', true);
							$('#my_block_detail_duration').prop('disabled', true)
							$('#my_block_detail_status').prop('disabled', true);
							$('#my_block_detail_leader').prop('disabled', true);
							$('#my_block_detail_save').hide();
							$('#my_block_detail_addMember').hide();
						}
						
						var projectId = result.projectId;
						//发送ajax，通过工程id查询工程信息
						$.ajax({
							url:'task/getProjectById.do',
							type:'POST',
							data:{'projectId' : projectId},
							dataType:'json',
							success: function(result2){
								$('#my_block_detail_projectId').val(JSON.parse(result2.project).id)
								$('#my_block_detail_projectName').val(JSON.parse(result2.project).name);
							}
						});
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
				//加载工程小块(即任务)成员表格
				$('#my_block_detail_membersTable').bootstrapTable({
					url:'task/getMembersOfBlock.do?blockId=' + blockId,
					pagination: true,
					clickToSelect: true ,
					sidePagination: 'client',
					pageSize: 5,
					onLoadSuccess : function(){
						//添加删除成员按钮事件
						$('.my_block_detail_deleteBtn').bind('click',function(){
//							alert($(this).attr('name') + '--' + blockId);
							var username = $(this).attr('name');
							//发送ajax，删除用户-工程小块(任务)关联
							$.ajax({
								url:'task/deleteUserBlock.do',
								type:'POST',
								data:{'username' : username,
									  'blockId'  : blockId },
								dataType:'json',
								success:function(result){
									if(result.result == 'true'){
										alert('删除成功!');
										//刷新表格
										$('#my_block_detail_membersTable').bootstrapTable('refresh');
									}
									else
										alert('删除失败!');
								}
							});
						});
					},
					onLoadError:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
				
			});
			
		}
	});
	
	
	//毫秒转化成yyyy-MM-dd的日期
	function millisecond2Date(milliSecond){
		var date = new Date(milliSecond);
		return date.getFullYear() + '-' + ((date.getMonth()+1)>9 ? (date.getMonth()+1) : '0' + (date.getMonth()+1))
			   + '-' + (date.getDate()>9 ? date.getDate() : '0' + date.getDate() );
	}
	
});
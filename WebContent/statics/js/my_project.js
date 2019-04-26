$(function(){
	
	
	//expandable点击事件
	$('button[data-widget="collapse"]').bind('click',function(){
		var parent = $($($(this).parent()).parent()).parent();
		if($(parent).hasClass('collapsed-box'))
			$(parent).removeClass('collapsed-box');
		else
			$(parent).addClass('collapsed-box');
	});
	
	//ajax加载当前用户的工程
	$.ajax({
		url:'task/getMyProjectList.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			addProject(result);
		}
	});
	
	//看板视图添加事件
	function addProject(result){
		$.each(result, function(i, n){
			var project = JSON.parse(n.project);
			var blocks = JSON.parse(n.blocks);
			var html = '<div class="col-md-3"><div class="box box-primary"><div class="box-header with-border"><h3 class="box-title"><a href="#" name="' + project.id + '" class="my_project_view_edit_project">' + project.name + '</a></h3><a href="javascript:void(0)" name="' + project.id + '" class="pull-right my_project_view_gantt"><i class="glyphicon glyphicon-align-left"></i><a></div><div class="box-body">';
			$.each(blocks, function(i2, n2){
				html += '<div class="info-box"><span class="info-box-icon bg-aqua"><i class="fa fa-list-alt"></i></span><div class="info-box-content"><a href="javascript:void(0)" class="my_project_view_edit_block" name="' + n2.id + '"><span class="info-box-number">' + n2.description + '</span></a><span class="info-box-text" style="color:rgb(253,203,53);"><i class="fa fa-clock-o"></i>' + millisecond2Date(n2.createTime) + ' 开始</span><a name="' + n2.id + '" class="my_project_view_block" data-toggle="modal" href="javascript:void(0)"><span data-toggle="tooltip" class="badge bg-green">' + n2.leader.name.substring(0,1) + '</span></a></div></div>';              
			});
			html += '</div><div class="box-footer"> <a href="#" name="' + project.id + '" class="small-box-footer my_project_view_add_block">添加任务 <i class="fa fa-plus-circle"></i></a> </div></div></div>';
			$('#my_project_view_row').append(html);
		});
		
		//点击工程小块的负责人，可切换负责人
		$('.my_project_view_block').bind('click',function(){
			//打开模态框
			$('#my_project_block_leader').modal();
			//暂存工程小块id
			var blockId = $(this).attr('name');
			$.ajax({
				url:'task/getUsersByBlockId.do',
				type:'POST',
				data:{'blockId' : $(this).attr('name')},
				dataType:'json',
				success:function(result){
					//展示团队列表
					showTeam(result);
					
					//添加点击事件，点击团队成员切换工程小块负责人
					$('.my_project_block_leader_team_change').bind('click', function(){
						//alert($(this).attr('name') + ' ' + blockId);
						var username = $(this).attr('name');
						//发送ajax，更改工程小块负责人
						$.ajax({
							url:'task/updateBlockLeader.do',
							type:'POST',
							data:{'username' : username,
								  'blockId'  : blockId},
							dataType:'text',
							success: function(result){
								//alert(result);
								//关闭切换负责人窗口
								$('#my_project_block_leader').modal('hide');
								setTimeout(function(){
									//重新载入主界面的content
									$('#index_main_content').load('/GeneralProject/page/task/my_project.html');
								},400);
							}
						});
					});
				}
			});
			
			//展示团队列表
			function showTeam(result){
				$('#my_project_block_leader_team').html('');
				var html = '<ul class="nav nav-stacked">';
				$.each(result, function(i, n){
					html += '<li><a href="#" name="' + n.username + '" class="my_project_block_leader_team_change" style="color:gray">&nbsp;' + n.username + '-' + n.name + '<span class="pull-left badge bg-green">' + n.name.substring(0,1) + '</span></a></li>';
				});
				html += '</ul>';
				$('#my_project_block_leader_team').append(html);
			}
			
			
		});
		
		//点击下方“添加任务”按钮事件
		$('.my_project_view_add_block').bind('click', function(){
			//工程id
			var projectId = $(this).attr('name');
			$('#my_project_view_addBlock').modal('show');
			//确定按钮事件
			$('#my_project_view_addBlock_confirmBtn').bind('click', function(){
				var description = $('#my_project_view_addBlock_description').val();
				var createTime = $('#my_project_view_addBlock_createTime').val();
				var duration = $('#my_project_view_addBlock_duration').val();
				var status = $('#my_project_view_addBlock_status').val();
				//发送ajax，保存任务信息
				$.ajax({
					url:'task/addBlock.do',
					type:'POST',
					data:{'projectId' : projectId,
						  'description' : description,
						  'createTime' : createTime,
						  'duration' : duration,
						  'status' : status},
					dataType:'json',
					success:function(result){
						if(result.result == 'true'){
							alert('添加成功!');
							//关闭窗口
							$('#my_project_view_addBlock').modal('hide');
							setTimeout(function(){
								//重新加载内容
								$('#index_main_content').load('/GeneralProject/page/task/my_project.html');
							},400);
						}
						else{
							alert('添加失败!');
							//关闭窗口
							$('#my_project_view_addBlock').modal('hide');
						}
					}
				});
			});
		});
		
		//点击工程标题，显示编辑工程界面
		$('.my_project_view_edit_project').bind('click', function(){
			var projectId = $(this).attr('name');//点击的工程的id
			$('#my_project_view').load('/GeneralProject/page/task/my_project_detail.html',null,function(){
				//发送ajax，根据工程id获取工程信息
				$.ajax({
					url:'task/getProjectById.do',
					type:'POST',
					data:{'projectId' : projectId},
					dataType:'json',
					success:function(result){
						var project = JSON.parse(result.project)
						var projectId = project.id;
						var projectName = project.name;
						var projectDescription = project.description;
						var projectCreateTime = millisecond2Date(project.createTime);
						var projectDuration = project.duration;
						var projectStatus = project.status;
						/*alert(projectDescription + '-' + millisecond2Date(projectCreateTime) + '-' + 
							projectDuration + '-' + projectStatus);*/
						$('#my_project_detail_id').val(projectId);
						$('#my_project_detail_projectName').val(projectName);
						$('#my_project_detail_projectDescription').val(projectDescription);
						$('#my_project_detail_createTime').val(projectCreateTime);
						$('#my_project_detail_duration').val(projectDuration);
						$('#my_project_detail_status').val(projectStatus);
						var projectLeader = project.user;
						$('#my_project_detail_leader').val(projectLeader.username + '-' + projectLeader.name);
					}
				});
				
				//初始化成员列表表格
				$('#my_project_detail_membersTable').bootstrapTable({
					url:'task/getMembersOfProject.do?projectId='+projectId,
					pagination: true,
					clickToSelect: true ,
					sidePagination: 'client',
					pageSize: 5,
					onLoadSuccess : function(){
						//添加删除成员按钮事件
						$('.my_project_detail_deleteBtn').bind('click',function(){
							//alert($(this).attr('name') + '--' + projectId);
							var username = $(this).attr('name');
							//发送ajax，将用户从工程成员中删除
							$.ajax({
								url:'task/deleteUserProject.do',
								type:'POST',
								data:{'username' : username,
									  'projectId' : projectId},
								dataType:'json',
								success: function(result){
									if(result.result == 'true'){
										alert('删除成功!');
										//刷新工程成员表格
										$('#my_project_detail_membersTable').bootstrapTable('refresh');
									}
									else{
										alert('删除失败!');
									}
								}
							});
						});
					}
				});
				
			});
		});
		
		//点击工程小块描述，显示编辑工程小块窗口
		$('.my_project_view_edit_block').bind('click', function(){
			//工程小块(即任务)的id
			var blockId = $(this).attr('name');
			$('#my_project_view').load('/GeneralProject/page/task/my_block_detail.html', function(){
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
					}
				});
				//发送ajax,获取工程小块(即任务)成员
				/*$.ajax({
					url:'task/getMembersOfBlock.do',
					type:'POST',
					data:{'blockId':blockId},
					dataType:'json',
					success:function(result){
						alert(JSON.stringify(result));
					}
				});*/
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
					}
				});
				
			});
		});
		
		//点击项目上的甘特图
		$('.my_project_view_gantt').bind('click', function(){
			var projectId = $(this).attr('name');
			$('#my_project_view').load('/GeneralProject/page/task/project_gantt.html',function(){
				//加载完成，把projectId赋值到project_gantt.html页面上
				$('#project_gantt_projectId').val(projectId);
			});
		});
	}
	
	
	
	//毫秒转化成yyyy-MM-dd的日期
	function millisecond2Date(milliSecond){
		var date = new Date(milliSecond);
		return date.getFullYear() + '-' + ((date.getMonth()+1)>9 ? (date.getMonth()+1) : '0' + (date.getMonth()+1))
			   + '-' + (date.getDate()>9 ? date.getDate() : '0' + date.getDate() );
	}
	
	//获取用户信息
	$.ajax({
		type:"POST",
		url:"getLoginUser.do",
		dataType:"json",
		success:function(result){
			$("#my_project_view_new_name").text(result.name.substring(0,1));
			/*$('#index_user_img').attr('src', result.icon);
			$('#index_user_img_big').attr('src', result.icon);*/
		}
	});
	
	//添加工程中选择创建时间事件
	$('#my_project_view_new_create_time').bind('click', function(){
		//打开选择创建时间的模态框
		$('#my_project_view_new_create_time_modal').modal('show');
		//日期控件初始化
		$('#my_project_view_create_time_modal_ct').datepicker({
			format:'yyyy-mm-dd'
		});
		$('#my_project_view_create_time_modal_ct').val('');
		$('#my_project_view_create_time_modal_duration').val('');
	});
	//确定按钮事件
	$('#my_project_view_create_time_modal_confirm').bind('click', function(){
		var createTime = $('#my_project_view_create_time_modal_ct').val();
		var duration = $('#my_project_view_create_time_modal_duration').val();
		if(isEmpty(createTime) || isEmpty(duration)){
			alert('不能为空');
			return;
		}
		//alert(createTime + ' ' + duration);
		$('#my_project_view_new_create_time').append(createTime + ' ' + duration);
		//模态框关闭
		$('#my_project_view_new_create_time_modal').modal('hide');
	});
	
	//判断字符串空
	function isEmpty(str){
		return str==null||str=='';
	}
	
	//新建工程的确定按钮
	$('#my_project_view_new_confirm_btn').bind('click', function(){
		var projectName = $('#my_project_view_new_project_name').val();
		var createTimeStr = $('#my_project_view_new_create_time').text();
		var createTime = createTimeStr.split(' ')[0];
		var duration = createTimeStr.split(' ')[1];
		//alert(projectName + '*' + createTime + '*' + duration);
		if(isEmpty(projectName) || isEmpty(createTime) || isEmpty(duration)){
			alert("信息不能空");
			return;
		}
		//发送ajax请求，添加工程
		$.ajax({
			url:'task/addProject.do',
			type:'POST',
			data:{'projectName' : projectName,
				  'createTime' : createTime,
				  'duration' : duration},
			dataType:'text',
			success:function(result){
				alert(result);
				$('#index_main_content').load('/GeneralProject/page/task/my_project.html');
			}
		});
	});
	
	
	//加载工程附件
	function loadProjectFiles(){
		$.ajax({
			url:'task/getMyProjectList.do',
			async: false,
			type:'POST',
			dataType:'json',
			success: function(result){
				$('#my_project_projectFile').html('');
				$.each(result, function(i, n){
					//alert(JSON.stringify(n));
					var project = JSON.parse(n.project);
					var html = '';
					html += '<div class="col-md-6"><div class="box box-primary"><div class="box-header with-border"><h3 class="box-title">' + project.name + '</h3><div class="pull-right"><form action="file/fileUpload.do" name="my_project_fileUpload_form_' + project.id + '" id="my_project_fileUpload_form_' + project.id + '" method="POST" enctype="multipart/form-data"><input type="file" value="添加附件" name="my_project_uploadFile" projectId="'+project.id+'" class="my_project_uploadFile" multiple="multiple"><input type="hidden" name="projectId" value="'+project.id+'"/></form></div></div><div class="box-body" style="height:150px;overflow-y: scroll;"><ul class="products-list product-list-in-box" id="my_project_fileList_' + project.id + '">';
					//发送ajax获取项目的附件文件集合
					$.ajax({
						url:'file/getProjectFiles.do',
						data:{'projectId' : project.id},
						async: false,
						dataType:'json',
						success:function(result){
							//alert(JSON.stringify(result));
							if(result.result == 'false'){
								html += '</ul></div></div></div>';
							}
							else{
								$.each(result, function(ii,nn){
									//alert(nn.name);
									html += '<li class="item"><div class="product-img"><img src="/GeneralProject/statics/image/file.jpg" alt="Product Image"></div><div class="product-info"><a href="file/fileDownload.do?projectId=' + project.id + '&fileName=' + nn.name + '" class="product-title">' + nn.name + '</a><a href="javascript:void(0);" class="my_project_file_delete" name="'+project.id+'&'+nn.name+'"><span class="label label-danger pull-right">x</span></a></div></li>';
								});
								html += '</ul></div></div></div>';
							}
							
							
						}
					});
					//$('#my_project_projectFile').html('');
					$('#my_project_projectFile').append(html);
				});
				
				
			}
		});
	}
	//调用加载工程附件方法
	loadProjectFiles();
	
	
	//监听文件选择框
	$('.my_project_uploadFile').bind('change', function(){
		//alert('选择文件' + $(this).attr('name'));
		//$('#my_project_fileUpload_form_' + $(this).attr('projectId')).submit();
		var formData = new FormData($('#my_project_fileUpload_form_' + $(this).attr('projectId'))[0]);
		$.ajax({
			url:'file/fileUpload.do',
			type:'POST',
			data : formData,
			dataType:'json',
			processData: false,
			contentType: false,
			success: function(result){
				//alert(result.result);
				if(result.result == 'true'){
					alert('添加附件成功');
					//重新加载我的项目界面并且，激活附件tab pane
					$("#index_main_content").load("/GeneralProject/page/task/my_project.html", function(){
						$('#my_project_tabUl_fj').click();
					});
				}
				else{
					alert('添加附件失败');
					//loadProjectFiles();
				}
			}
		});
	});
	
	//删除文件(附件)按钮
	$('.my_project_file_delete').bind('click', function(){
		var arr = $(this).attr('name').split('&');
		var projectId = arr[0];
		var fileName = arr[1];
		$.ajax({
			url:'file/deleteFile.do',
			type:'POST',
			data:{'projectId' : projectId,
				  'fileName' : fileName},
			dataType:'json',
			success:function(result){
				//alert(JSON.stringify(result));
				if(result.result == 'true'){
					alert('删除附件成功');
					//重新加载我的项目界面并且，激活附件tab pane
					$('#index_main_content').load("/GeneralProject/page/task/my_project.html", function(){
						$('#my_project_tabUl_fj').click();
					});
				}
				else{
					alert('删除附件失败');
					//loadProjectFiles();
				}
			}
		});
	});
	
	
});



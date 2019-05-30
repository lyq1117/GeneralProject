$(function(){
	
	/**
	 * 获取部门列表
	 */
	$('#dept_manage_table').bootstrapTable({
		url:'system/getDeptListTable.do',
		columns:[{
			field:'id',
			title:'编号'
		},{
			field:'name',
			title:'名称'
		},{
			field:'location',
			title:'办公地点'
		},{
			field:'leader',
			title:'部长'
		},{
			field:'status',
			title:'状态'
		},{
			field:'option',
			title:'操作'
		}],
		onLoadSuccess:function(){
			//编辑按钮点击事件
			$('.dept_manage_edit').bind('click', function(){
				var deptId = $(this).attr('deptId');
				//打开编辑部门信息窗口
				$('#dept_manage_editWin').modal('show');
				//发送ajax查询部门成员中拥有部长角色和总经理角色的成员集合
				$.ajax({
					url:'system/getDeptMembersOwnDeptLeaderRoleOrManagerRole.do',
					type:'POST',
					async:false,
					data:{'deptId' : deptId},
					dataType:'json',
					success:function(result){
						$('#dept_manage_editWin_leaderSelect').html('');
						$('#dept_manage_editWin_leaderSelect').append('<option value=""></option>');
						//遍历部门成员集合
						$.each(result, function(i, n){
							var html = '<option value="'+n.username+'">'+n.username+'-'+n.name+'</option>';
							$('#dept_manage_editWin_leaderSelect').append(html);
						});
						
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
				//发送ajax查询部门信息
				$.ajax({
					url:'system/getDeptById.do',
					type:'POST',
					async:false,
					data:{'deptId' : deptId},
					dataType:'json',
					success:function(result){
						//部门id赋值
						$('#dept_manage_editWin_id').val(result.id);
						//部门名称赋值
						$('#dept_manage_editWin_name').val(result.name);
						//办公地点赋值
						$('#dept_manage_editWin_location').val(result.location);
						//部长id赋值
						$('#dept_manage_editWin_leaderSelect').val(result.leader_id);
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
			});
			
			//启用部门按钮点击事件
			$('.dept_manage_open').bind('click', function(){
				//alert($(this).attr('deptId') + '-open');
				var deptId = $(this).attr('deptId');
				//发送ajax请求启用部门
				$.ajax({
					url:'system/changeDeptStatus.do',
					type:'POST',
					data:{'deptId' : deptId,
						  'status' : 0},
					dataType:'json',
					success:function(result){
						alert(result.result);
						//刷新表格
						$('#dept_manage_table').bootstrapTable('refresh');
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
			});
			
			//禁用部门按钮点击事件
			$('.dept_manage_close').bind('click', function(){
				//alert($(this).attr('deptId') + '-close');
				var deptId = $(this).attr('deptId');
				//发送ajax请求禁用部门
				$.ajax({
					url:'system/changeDeptStatus.do',
					type:'POST',
					data:{'deptId' : deptId,
						  'status' : 1},
					dataType:'json',
					success:function(result){
						alert(result.result);
						//刷新表格
						$('#dept_manage_table').bootstrapTable('refresh');
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
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
	
	//编辑部门信息窗口的保存按钮
	$('#dept_manage_editWin_saveBtn').bind('click', function(){
		var deptId = $('#dept_manage_editWin_id').val();
		var deptName = $('#dept_manage_editWin_name').val();
		var location = $('#dept_manage_editWin_location').val();
		var leaderId = $('#dept_manage_editWin_leaderSelect').val();
		if(deptName == '' ||
		   location == ''){
			alert('信息不能为空！');
			return;
		}
		//alert(deptId + deptName + location + leaderId);
		//发送ajax修改部门信息
		$.ajax({
			url:'system/saveDeptInfo.do',
			type:'POST',
			async:false,
			data:{'deptId' : deptId,
				  'deptName' : deptName,
				  'location' : location,
				  'leaderId' : leaderId},
			dataType:'json',
			success:function(result){
				alert(result.result);
				//刷新表格
				$('#dept_manage_table').bootstrapTable('refresh');
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
	});
	
	//新增部门点击事件
	$('#dept_manage_addBtn').bind('click', function(){
		//alert('add dept');
		//显示新增部门窗口
		$('#dept_manage_addWin').modal('show');
	});
	
	//新增按钮事件
	$('#dept_manage_addWin_saveBtn').bind('click', function(){
		var deptName = $('#dept_manage_addWin_name').val();
		var location = $('#dept_manage_addWin_location').val();
		var leaderId = $('#dept_manage_addWin_leaderSelect').val();
		if(deptName == '' ||
		   location == ''){
			alert('信息不能为空！');
			return;
		}
		//发送ajax请求新增部门
		$.ajax({
			url:'system/addDept.do',
			type:'POST',
			data:{'deptName' : deptName,
				  'location' : location},
			dataType:'json',
			success:function(result){
				alert(result.result);
				//刷新表格
				$('#dept_manage_table').bootstrapTable('refresh');
			}
		});
	});
	
});
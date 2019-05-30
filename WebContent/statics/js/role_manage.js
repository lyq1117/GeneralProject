$(function(){
	
	$('#role_manage_roleSelect').bind('change', function(){
		//切换角色，重新加载表格数据
		$('#role_manage_table').bootstrapTable('refresh');
	});
	
	/**
	 * 获取角色权限表格
	 */
	$('#role_manage_table').bootstrapTable({
		url:'system/getRoleManageTable.do',
		queryParams: function(params){
			var param = {
					'roleId' : $('#role_manage_roleSelect').val()
			}
			return param;
		},
		columns:[{
			field:'rootMenu',
			title:'根菜单'
		},{
			field:'linkMenu',
			title:'权限'
		}],
		onLoadSuccess:function(){
			
			//根菜单复选框的选中和取消选中事件
			$('.role_manage_rootCheck').bind('change', function(){
				var menuId = $(this).attr('menuId');
				var isCheck = $(this).prop('checked');
				var roleId = $('#role_manage_roleSelect').val();
				
				//勾选根菜单
				if(isCheck == true){
					//勾选根菜单
					$.ajax({
						url:'system/addRoleMenu.do',
						type:'POST',
						data:{'roleId' : roleId,
							  'menuId' : menuId},
						dataType:'json',
						success:function(result){
							if(result.result == true){
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
							if(result.result == false){
								alert('勾选根菜单失败！！');
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
						},
						error:function(){
							//没有权限
							$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
							$("#index_main_content").css('padding','');
						}
					});
					
				}else{//取消勾选根菜单
					$.ajax({
						url:'system/cancelRootMenu.do',
						type:'POST',
						data:{'roleId' : roleId,
							  'menuId' : menuId},
						dataType:'json',
						success:function(result){
							if(result.result == true){
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
							if(result.result == false){
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
						},
						error:function(){
							//没有权限
							$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
							$("#index_main_content").css('padding','');
						}
					});
				}
				
			});
			
			//链接菜单复选框选中和取消选中事件
			$('.role_manage_linkCheck').bind('change', function(){
				var menuId = $(this).attr('menuId');
				var isCheck = $(this).prop('checked');
				var roleId = $('#role_manage_roleSelect').val();
				
				//勾选链接菜单
				if(isCheck == true){
					//勾选链接菜单
					$.ajax({
						url:'system/addRoleMenu.do',
						type:'POST',
						data:{'roleId' : roleId,
							  'menuId' : menuId},
						dataType:'json',
						success:function(result){
							if(result.result == true){
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
							if(result.result == false){
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
						},
						error:function(){
							//没有权限
							$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
							$("#index_main_content").css('padding','');
						}
					});
					
					
				}else{//取消勾选链接菜单
					$.ajax({
						url:'system/cancelLinkMenu.do',
						type:'POST',
						data:{'roleId' : roleId,
							  'menuId' : menuId},
						dataType:'json',
						success:function(result){
							if(result.result == true){
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
							if(result.result == false){
								//刷新表格
								$('#role_manage_table').bootstrapTable('refresh');
							}
						},
						error:function(){
							//没有权限
							$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
							$("#index_main_content").css('padding','');
						}
					});
					
					
					
				}
			});
			
		},
		onLoadError:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
});
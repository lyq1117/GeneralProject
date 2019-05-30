$(function(){
	/**
	 * 初始化模态窗口中的角色选项
	 */
	$.ajax({
		url:'system/getAllRoles.do',
		type:'POST',
		async:false,
		dataType:'json',
		success:function(result){
			//alert(JSON.stringify(result));
			$.each(result, function(i, n){
				if(n.id != 'R-101' &&
				   n.id != 'R-103' &&
				   n.id != 'R-105' &&
				   n.id != 'R-100'){
					var html = '<div class="row"><div class="checkbox"><label><input type="checkbox" value="'+n.id+'">'+n.name+'</label></div></div>';
					$('#user_manage_giveRolesWin_bRoles').append(html);
				}
			});
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 初始化用户表格
	 */
	$('#user_manage_table').bootstrapTable({
		url:'system/getUsersTable.do',
		columns:[{
			field : 'id',
			title : '用户名'
		},{
			field : 'name',
			title : '真实姓名'
		},{
			field : 'icon',
			title : '头像地址'
		},{
			field : 'tel',
			title : '电话'
		},{
			field : 'status',
			title : '账号状态'
		},{
			field : 'deptId',
			title : '所属部门编号'
		},{
			field : 'deptName',
			title : '所属部门名称'
		},{
			field : 'role',
			title : '角色'
		},{
			field : 'option',
			title : '操作'
		}],
		onLoadSuccess:function(){
			//分配角色点击事件
			$('.user_manage_giveRoles').bind('click', function(){
				//获取点击的用户的id
				var userId = $(this).attr('userId');
				//清空复选框
				var checkboxs = $('#user_manage_giveRolesWin_bRoles input[type="checkbox"]');
				$.each(checkboxs, function(i, n){
					//$(n).removeAttr('checked');
					$(n).prop('checked', false);
				});
				//清空单选框
				var radios = $('#user_manage_giveRolesWin_aRoles input[type="radio"]');
				$.each(radios, function(i, n){
					//$(n).removeAttr('checked');
					$(n).prop('checked', false);
				})
				
				//角色分配窗口的用户id隐藏域赋值
				$('#user_manage_giveRolesWin_userId').val(userId);
				//显示分配角色窗口
				$('#user_manage_giveRolesWin').modal('show');
				
				//查询用户具有的角色
				$.ajax({
					url:'system/getRolesByUserId.do',
					type:'POST',
					data:{'userId' : userId},
					dataType:'json',
					success:function(result){
						$.each(result, function(ii, nn){
							$.each(checkboxs, function(i, n){
								if(nn.roleId == $(n).val()){
									//alert(nn.roleId + '-' + $(n).val() + '===' + (nn.roleId == $(n).val()));
									//alert('t');
									$(n).prop('checked', true);
								}
							});
							if(nn.roleId == 'R-101' ||
							   nn.roleId == 'R-103' ||
							   nn.roleId == 'R-105'){
								$('#user_manage_giveRolesWin_aRoles input[value="'+nn.roleId+'"]').prop('checked', true);
							}
							
						});
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
				
			});
			
			/**
			 * 编辑按钮点击事件
			 */
			$('.user_manage_eidt').bind('click', function(){
				var userId = $(this).attr('userId');
				//清空窗口里的文本框信息
				$('#user_manage_editWin_userId').val('');
				$('#user_manage_editWin_userName').val('');
				$('#user_manage_editWin_icon').val('');
				$('#user_manage_editWin_tel').val('');
				$('#user_manage_editWin_oldPwd').val('');
				$('#user_manage_editWin_newPwd').val('');
				$('#user_manage_editWin_newPwd2').val('');
				
				//根据用户名获取用户信息
				$.ajax({
					url:'system/getUserInfo.do',
					type:'POST',
					data:{'userId' : userId},
					dataType:'json',
					success:function(result){
						//用户名赋值到文本框
						$('#user_manage_editWin_userId').val(result.username);
						//真实姓名赋值到文本框
						$('#user_manage_editWin_userName').val(result.name);
						//头像地址复制到文本框
						$('#user_manage_editWin_icon').val(result.icon);
						//电话赋值到文本框
						$('#user_manage_editWin_tel').val(result.tel);
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
				//显示编辑信息窗口
				$('#user_manage_editWin').modal('show');
				
			});
			
			/**
			 * 启用用户按钮点击事件
			 */
			$('.user_manage_open').bind('click', function(){
				var userId = $(this).attr('userId');
				//发送ajax启用用户
				$.ajax({
					url:'system/openUser.do',
					type:'POST',
					data:{'userId' : userId},
					dataType:'json',
					success:function(result){
						alert(result.result);
						//刷新表格
						$('#user_manage_table').bootstrapTable('refresh');
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				});
			});
			
			/**
			 * 禁用用户按钮点击事件
			 */
			$('.user_manage_delete').bind('click', function(){
				var userId = $(this).attr('userId');
				//alert('禁用'+$(this).attr('userId'));
				//发送ajax禁用用户
				$.ajax({
					url:'system/banUser.do',
					type:'POST',
					data:{'userId':userId},
					dataType:'json',
					success:function(result){
						alert(result.result);
						//刷新表格
						$('#user_manage_table').bootstrapTable('refresh');
					},
					error:function(){
						//没有权限
						$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
						$("#index_main_content").css('padding','');
					}
				})
				
			});
			
			
			
		},
		onLoadError:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	
	//编辑信息窗口中的保存按钮事件
	$('#user_manage_editWin_saveBtn').bind('click', function(){
		//先判断3个密码框是不是空的，如果为空就不包含改密码的操作
		var oldPwd = $('#user_manage_editWin_oldPwd').val();
		var newPwd = $('#user_manage_editWin_newPwd').val();
		var newPwd2 = $('#user_manage_editWin_newPwd2').val();
		var name = $('#user_manage_editWin_userName').val();//真实姓名
		var icon = $('#user_manage_editWin_icon').val();//头像地址
		var tel = $('#user_manage_editWin_tel').val();//电话
		var userId = $('#user_manage_editWin_userId').val();//用户名
		
		var isChangePwdFlag = false;//默认是不改密码的
		if(oldPwd == '' && newPwd == '' && newPwd2 == ''){
			isChangePwdFlag = false;
		}
		else{
			isChangePwdFlag = true;//有改密码的操作
		}
		//如果是有改密码的操作
		if(isChangePwdFlag == true){
			//先验证三个密码框是不是有空
			if(oldPwd == '' || newPwd =='' || newPwd2 ==''){
				alert('请填写完整密码信息!');
				return ;
			}
			if(newPwd != newPwd2){
				alert('确认密码不正确!');
				return ;
			}
		}
		//发送ajax保存信息
		$.ajax({
			url:'system/editUserinfo.do',
			type:'POST',
			data:{'isChangePwdFlag' : isChangePwdFlag,
				  'userId' : userId,
				  'oldPwd' : oldPwd,
				  'newPwd' : newPwd,
				  'name' : name,
				  'icon' : icon,
				  'tel' : tel},
			dataType:'json',
			success:function(result){
				alert(result.result);
				//刷新表格
				$('#user_manage_table').bootstrapTable('refresh');
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
		
	});
	
	/**
	 * 分配角色窗口中的确定按钮
	 */
	$('#user_manage_giveRolesWin_confirmBtn').bind('click', function(){
		//复选框结合
		var checkboxs = $('#user_manage_giveRolesWin_bRoles input[type="checkbox"]');
		//alert(checkboxs.length);
		//单选框集合
		var radios = $('#user_manage_giveRolesWin_aRoles input[type="radio"]');
		
		var arr = [];
		var index = 0;
		//遍历单选框，把选中选项放入数组
		$.each(radios, function(i, n){
			if($(n).prop('checked') == true){
				arr.push($(n).val());
			}
		})
		//遍历复选框，把选中选项放入数组
		$.each(checkboxs, function(i, n){
			if($(n).prop('checked') == true){
				arr.push($(n).val());
			}
		});
		
		//发送ajax，更改角色
		$.ajax({
			url:'system/changeRole.do',
			type:'POST',
			traditional: true,//传数组 这里设置为true
			data:{'roles' : arr,
				  'userId' : $('#user_manage_giveRolesWin_userId').val()},
			dataType:'json',
			success:function(result){
				alert(result.result);
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
		
	});
	
	
	/**
	 * 打开添加用户窗口按钮
	 */
	$('#user_manager_addUserBtn').bind('click', function(){
		$('#user_manage_addWin').modal('show');
		//查询部门列表
		$.ajax({
			url:'system/getDeptList.do',
			type:'POST',
			dataType:'json',
			success:function(result){
				$.each(result, function(i, n){
					var html = '<option value="'+n.id+'">'+n.name+'</option>';
					$('#user_manage_addWin_deptSelect').append(html);
				});
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
	});
	
	/**
	 * 添加用户按钮
	 */
	$('#user_manage_addWin_addBtn').bind('click', function(){
		var userId = $('#user_manage_addWin_userId').val();
		var name = $('#user_manage_addWin_userName').val();
		var pwd = $('#user_manage_addWin_pwd').val();
		var pwd2 = $('#user_manage_addWin_pwd2').val();
		var icon = $('#user_manage_addWin_icon').val();
		var tel = $('#user_manage_addWin_tel').val();
		var deptId = $('#user_manage_addWin_deptSelect').val();
		if(pwd != pwd2){
			alert('输入的两次密码不相同！');
			return ;
		}
		if(userId == '' ||
		   name == '' ||
		   pwd == '' ||
		   pwd2 == '' ||
		   icon == '' ||
		   tel == ''){
			alert('信息不能为空');
			return ;
		}
		//发送ajax请求保存用户信息
		$.ajax({
			url:'system/addUser.do',
			type:'POST',
			data:{'userId' : userId,
				  'name'   : name,
				  'pwd'    : pwd,
				  'pwd2'   : pwd2,
				  'icon'   : icon,
				  'tel'    : tel,
				  'deptId' : deptId},
			dataType:'json',
			success:function(result){
				alert(result.result);
				//刷新表格
				$('#user_manage_table').bootstrapTable('refresh');
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
	});
	
});
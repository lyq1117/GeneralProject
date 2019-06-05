$(function(){
	
	//左边导航 发起审批、我申请的、我审批的 点击切换事件
	$('.approval-left-bar-item').bind('click', function(){
		var parentHtml = $(this).parent();
		var brothers = $(parentHtml).children();
		console.dir(brothers);
		$.each(brothers, function(i, n){
			$(n).removeClass('approval-left-bar-item-active');
		});
		
		//是否被激活的标记
		var flag = $(this).hasClass('approval-left-bar-item-active');
		if(!flag){
			$(this).addClass('approval-left-bar-item-active');
		}
		
		var target = $($(this).attr('target'));//对应的目标面板
		var targetParent = $(target).parent();//目标面板的父亲
		var targetBrothers = $(targetParent).children();//目标面板的兄弟集合
		$.each(targetBrothers, function(i, n){
			$(n).hide();//所有面板隐藏
		})
		$(target).show();//显示目标面板
	});
	
	/**
	 * 获取人事审批的模板
	 */
	$.ajax({
		url:'application/getRenshiApprovalModal.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			renshiModal = result;//把认识审批所有模板赋值成全局变量 result里面有title和items
			$.each(result, function(i, n){
				var html = '<div class="col-md-3 approval-modal approval-modal-renshi" title="'+n.title+'"><img src="/GeneralProject/statics/image/approval/renshi.png">'+n.title+'</div>';
				$('#approval_renshiModals').append(html);
			});
			
			$('.approval-modal-renshi').bind('click', function(){
				var titleStr = $(this).attr('title');
				//遍历人事审批模板
				$.each(renshiModal, function(i, n){
					//如果标题相等，就用该模板下的items
					if(n.title == titleStr){
						//alert(JSON.stringify(n.items))
						$('#approval_modal_title').text(n.title);//审批模板的标特赋值
						$('#approval_modal_modalType').val('0');//审批模板的类型为0-人事审批
						$('#approval_modal').modal('show');//显示审批填写模态窗口
						//清空模态窗口的body部分
						$('#approval_modal_body').html('');
						//遍历items
						$.each(n.items, function(ii, nn){
							var html = '<div class="form-group"><label for="inputEmail3" class="col-sm-2 control-label">'+nn+'</label><div class="col-sm-10"><input type="text" class="form-control" placeholder="'+nn+'"></div></div>';
							$('#approval_modal_body').append(html);
						});
					}
				});
			});
			
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 获取财务审批模板
	 */
	$.ajax({
		url:'application/getCaiwuApprovalModal.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			caiwuModal = result;//把认识审批所有模板赋值成全局变量 result里面有title和items
			$.each(result, function(i, n){
				var html = '<div class="col-md-3 approval-modal approval-modal-caiwu" title="'+n.title+'"><img src="/GeneralProject/statics/image/approval/caiwu.png">'+n.title+'</div>';
				$('#approval_caiwuModals').append(html);
			});
			
			$('.approval-modal-caiwu').bind('click', function(){
				var titleStr = $(this).attr('title');
				//遍历财务审批模板
				$.each(caiwuModal, function(i, n){
					//如果标题相等，就用该模板下的items
					if(n.title == titleStr){
						//alert(JSON.stringify(n.items))
						$('#approval_modal_title').text(n.title);//审批模板的标特赋值
						$('#approval_modal_modalType').val('1');//审批模板的类型为1-财务审批
						$('#approval_modal').modal('show');//显示审批填写模态窗口
						//清空模态窗口的body部分
						$('#approval_modal_body').html('');
						//遍历items
						$.each(n.items, function(ii, nn){
							var html = '<div class="form-group"><label for="inputEmail3" class="col-sm-2 control-label">'+nn+'</label><div class="col-sm-10"><input type="text" class="form-control" placeholder="'+nn+'"></div></div>';
							$('#approval_modal_body').append(html);
						});
					}
				});
			});
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 获取行政审批模板
	 */
	$.ajax({
		url:'application/getXingzhengApprovalModal.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			xingzhengModal = result;//把认识审批所有模板赋值成全局变量 result里面有title和items
			$.each(result, function(i, n){
				var html = '<div class="col-md-3 approval-modal approval-modal-xingzheng" title="'+n.title+'"><img src="/GeneralProject/statics/image/approval/xingzheng.png">'+n.title+'</div>';
				$('#approval_xingzhengModals').append(html);
			});
			
			$('.approval-modal-xingzheng').bind('click', function(){
				var titleStr = $(this).attr('title');
				//遍历行政审批模板
				$.each(xingzhengModal, function(i, n){
					//如果标题相等，就用该模板下的items
					if(n.title == titleStr){
						//alert(JSON.stringify(n.items))
						$('#approval_modal_title').text(n.title);//审批模板的标特赋值
						$('#approval_modal_modalType').val('2');//审批模板的类型为2-行政审批
						$('#approval_modal').modal('show');//显示审批填写模态窗口
						//清空模态窗口的body部分
						$('#approval_modal_body').html('');
						//遍历items
						$.each(n.items, function(ii, nn){
							var html = '<div class="form-group"><label for="inputEmail3" class="col-sm-2 control-label">'+nn+'</label><div class="col-sm-10"><input type="text" class="form-control" placeholder="'+nn+'"></div></div>';
							$('#approval_modal_body').append(html);
						});
					}
				});
			});
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 填写审批窗口的确定按钮
	 */
	$('#approval_mocal_confirmBtn').bind('click', function(){
		var inputs = $('#approval_modal_body input[type="text"]');
		var title = $('#approval_modal_title').text();
		var content = '';
		var flagNull = 0;
		$.each(inputs, function(i, n){
			if($(n).val() == '' || $(n).val() == null || $(n).val() == undefined){
				flagNull = 1;
			}
			content += $(n).attr('placeholder') + ':' + $(n).val() + '\n';
		});
		if(flagNull == 1){
			alert('请填写完整信息!');
			return ;
		}
		var type = $('#approval_modal_modalType').val();//审批类型(0-人事审批，1-财务审批，3-行政审批)
		
		//alert(type);
		/**
		 * 发送ajax，保存审批信息
		 */
		$.ajax({
			url:'application/addApproval.do',
			type:'POST',
			data:{'title' : title,
				  'content' : content,
				  'type' : type},
			dataType:'json',
			success:function(result){
				alert(result.result);
				//刷新我审批的表格
				$('#approval_wspdPanel_table').bootstrapTable('refresh');
				//刷新我申请的表格
				$('#approval_wsqdPanel_table').bootstrapTable('refresh');
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
	});
	
	
	
	/**
	 * 我审批的面板中 获取我审批的表格
	 */
	$('#approval_wspdPanel_table').bootstrapTable({
		url:'application/getWspdTable.do',
		columns:[{
			field : 'id',
			title : '审批编号'
		},{
			field : 'type',
			title : '类型'
		},{
			field : 'submitUser',
			title : '申请人'
		},{
			field : 'submitDate',
			title : '申请时间'
		},{
			field : 'status',
			title : '状态'
		}],
		onClickRow:function(row, $element, field){
			//发送ajax查询审批具体内容
			$.ajax({
				url:'application/getApprovalById.do',
				type:'POST',
				data:{'approvalId' : row.id},
				dataType:'json',
				success:function(result){
					if(result.status == 1){//已同意的审批
						$('#approval_detailModal_wspd_confirmBtn').hide();
						$('#approval_detailModal_wspd_rejectBtn').hide();
						$('#approval_detailModal_wspd_markPic').attr('src','/GeneralProject/statics/image/approval/spty.png');
					}
					else if(result.status == 2){//已拒绝的审批
						$('#approval_detailModal_wspd_confirmBtn').hide();
						$('#approval_detailModal_wspd_rejectBtn').hide();
						$('#approval_detailModal_wspd_markPic').attr('src','/GeneralProject/statics/image/approval/spjj.png');
					}
					else if(result.status == 0){//待审批的审批
						$('#approval_detailModal_wspd_confirmBtn').show();
						$('#approval_detailModal_wspd_rejectBtn').show();
						$('#approval_detailModal_wspd_markPic').attr('src','/GeneralProject/statics/image/approval/dsp.png');
					}
					$('#approval_detailModal_wspd_title').text(result.title);//审批标题赋值
					$('#approval_detailModal_wspd_id').text(result.id);//审批编号赋值
					$('#approval_detailModal_wspd_submitUserId').text(result.submitUser.username);//提交审批人的用户名
					$('#approval_detailModal_wspd_submitUserName').text(result.submitUser.name);//提交审批人的姓名
					$('#approval_detailModal_wspd_submitUserDeptName').text(result.submitUser.dept.name);//提交审批人的部门名
					$('#approval_detailModal_wspd_submitDate').text(result.submitDate);//审批提交日期
					$('#approval_detailModal_wspd_content').text(result.content);//审批内容
					
				},
				error:function(){
					//没有权限
					$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
					$("#index_main_content").css('padding','');
				}
			});
			$('#approval_detailModal_wspd').modal('show');//显示我审批的审批的详细内容
		},
		onLoadError:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	//我审批的 里面的同意审批按钮
	$('#approval_detailModal_wspd_confirmBtn').bind('click', function(){
		$.ajax({
			url:'application/changeApprovalStatus.do',
			type:'POST',
			data:{'approvalId' : $('#approval_detailModal_wspd_id').text(),
				  'status' : '1'},
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
		//刷新我审批的表格
		$('#approval_wspdPanel_table').bootstrapTable('refresh');
		//刷新我申请的表格
		$('#approval_wsqdPanel_table').bootstrapTable('refresh');
		//关闭我审批的模态窗口
		$('#approval_detailModal_wspd').modal('hide');
	});
	//我审批的 里面的拒绝审批按钮
	$('#approval_detailModal_wspd_rejectBtn').bind('click', function(){
		$.ajax({
			url:'application/changeApprovalStatus.do',
			type:'POST',
			data:{'approvalId' : $('#approval_detailModal_wspd_id').text(),
				  'status' : '2'},
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
		//刷新我审批的表格
		$('#approval_wspdPanel_table').bootstrapTable('refresh');
		//刷新我申请的表格
		$('#approval_wsqdPanel_table').bootstrapTable('refresh');
		//关闭我审批的模态窗口
		$('#approval_detailModal_wspd').modal('hide');
	});
	
	//我申请的的面板中 获取我申请的表格
	$('#approval_wsqdPanel_table').bootstrapTable({
		url:'application/getWsqdTable.do',
		columns:[{
			field : 'id',
			title : '审批编号'
		},{
			field : 'type',
			title : '类型'
		},{
			field : 'submitUser',
			title : '申请人'
		},{
			field : 'submitDate',
			title : '申请时间'
		},{
			field : 'status',
			title : '状态'
		}],
		onClickRow:function(row, $element, field){
			//发送ajax查询审批具体内容
			$.ajax({
				url:'application/getApprovalById.do',
				type:'POST',
				data:{'approvalId' : row.id},
				dataType:'json',
				success:function(result){
					if(result.status == 1){//已同意的审批
						$('#approval_detailModal_wsqd_markPic').attr('src','/GeneralProject/statics/image/approval/spty.png');
					}
					else if(result.status == 2){//已拒绝的审批
						$('#approval_detailModal_wsqd_markPic').attr('src','/GeneralProject/statics/image/approval/spjj.png');
					}
					else if(result.status == 0){//待审批的审批
						$('#approval_detailModal_wsqd_markPic').attr('src','/GeneralProject/statics/image/approval/dsp.png');
					}
					$('#approval_detailModal_wsqd_title').text(result.title);//审批标题赋值
					$('#approval_detailModal_wsqd_id').text(result.id);//审批编号赋值
					$('#approval_detailModal_wsqd_submitUserId').text(result.submitUser.username);//提交审批人的用户名
					$('#approval_detailModal_wsqd_submitUserName').text(result.submitUser.name);//提交审批人的姓名
					$('#approval_detailModal_wsqd_submitUserDeptName').text(result.submitUser.dept.name);//提交审批人的部门名
					$('#approval_detailModal_wsqd_submitDate').text(result.submitDate);//审批提交日期
					$('#approval_detailModal_wsqd_content').text(result.content);//审批内容
				},
				error:function(){
					//没有权限
					$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
					$("#index_main_content").css('padding','');
				}
			});
			$('#approval_detailModal_wsqd').modal('show');//点击我申请的审批信息，显示我申请的审批的详细信息窗口
		},
		onLoadError:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
});
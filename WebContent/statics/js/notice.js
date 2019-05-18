$(function(){
	//编辑器
	$('.textarea').wysihtml5()
	var submitTextArea = CKEDITOR.replace('notice_submit_modalWin_textArea');
	var lookTextArea = CKEDITOR.replace('notice_look_modalWin_textArea');
	
	//点击全部公告、我发送的切换面板
	$('.notice_tab_bar').bind('click', function(){
		var parentHtml = $(this).parent();
		var brothers = $(parentHtml).children();
		//所有兄弟包括自己，取消激活状态
		$.each(brothers, function(i, n){
			$(n).removeClass('notice_tab_bar_active');
		});
		//再激活自己
		$(this).addClass('notice_tab_bar_active');
		
		//再获取自己对应的面板
		var myPanel = $($(this).attr('target'));
		//面板父亲
		var panelParent = $(myPanel).parent();
		//面板兄弟
		var panelBrothers = $(panelParent).children();
		//遍历面板兄弟并且全部面板隐藏
		$.each(panelBrothers, function(i, n){
			$(n).hide();
		})
		//再显示自己这个面板
		$(myPanel).show();
	});
	
	//发布公告按钮
	$('#notice_submitBtn').bind('click', function(){
		$('#notice_submit_modalWin').modal('show');//显示发布公告模态窗口
	});
	
	//发布窗口中的发布按钮
	$('#notice_submit_modalWin_submitBtn').bind('click', function(){
		var title = $('#notice_submit_modalWin_title').val();//公告标题
		var content = submitTextArea.getData();//公告内容
		if(title=='' || content==''){
			alert('标题或内容不能为空!');
			return ;
		}
		var status = $('input[name="notice_submit_modalWin_status"]:checked').val();//公告状态
		/**
		 * 发送ajax，保存公告
		 */
		$.ajax({
			url:'application/submitNotice.do',
			type:'POST',
			data:{'title':title,
				  'content' : content,
				  'status' : status},
			dataType:'json',
			success:function(result){
				$('#notice_submit_modalWin').modal('hide');//关闭模态窗口
				alert(result.result);//提示成功或者失败
				//刷新所有公告表格
				$('#notice_allNoticeTable').bootstrapTable('refresh');
				//刷新我发送的公告表格
				$('#notice_myNoticeTable').bootstrapTable('refresh');
			}
		});
	});
	
	/**
	 * 初始化所有公告表格
	 */
	$('#notice_allNoticeTable').bootstrapTable({
		url:'application/getAllNoticeTable.do',
		columns:[{
			field : 'id',
			title : '编号',
			width : 50
		},{
			field : 'title',
			title : '标特',
			width : 200
		},{
			field : 'submitUser',
			title : '发布人'
		},{
			field : 'submitDate',
			title : '发布时间'
		},{
			field : 'status',
			title : '状态'
		}],
		onLoadSuccess:function(){
			
		},
		onClickRow: function(row, $element, field){
			var id = row.id;
			/**
			 * 发送ajax获取公告
			 */
			$.ajax({
				url:'application/getNoticeById.do',
				type:'POST',
				data:{'id' : id},
				dataType:'json',
				success:function(result){
					$('#notice_look_modalWin_title').val(result.title);
					lookTextArea.setData(result.content);
					if(result.status == 1){
						$('#notice_look_modalWin_jinji').show();
						$('#notice_look_modalWin_yiban').hide();
					}else if(result.status == 2){
						$('#notice_look_modalWin_jinji').hide();
						$('#notice_look_modalWin_yiban').show();
					}
				}
			});
			
			//显示查看公告的模态框
			$('#notice_look_modalWin').modal('show');
		}
	});
	
	/**
	 * 初始化我发送的公告表格
	 */
	$('#notice_myNoticeTable').bootstrapTable({
		url:'application/getMyNoticeTable.do',
		columns:[{
			field : 'id',
			title : '编号',
			width : 50
		},{
			field : 'title',
			title : '标特',
			width : 200
		},{
			field : 'submitUser',
			title : '发布人'
		},{
			field : 'submitDate',
			title : '发布时间'
		},{
			field : 'status',
			title : '状态'
		}],
		onLoadSuccess:function(){
			
		},
		onClickRow:function(row, $element, field){
			var id = row.id;
			/**
			 * 发送ajax获取公告
			 */
			$.ajax({
				url:'application/getNoticeById.do',
				type:'POST',
				data:{'id' : id},
				dataType:'json',
				success:function(result){
					$('#notice_look_modalWin_title').val(result.title);
					lookTextArea.setData(result.content);
					if(result.status == 1){
						$('#notice_look_modalWin_jinji').show();
						$('#notice_look_modalWin_yiban').hide();
					}else if(result.status == 2){
						$('#notice_look_modalWin_jinji').hide();
						$('#notice_look_modalWin_yiban').show();
					}
				}
			});
			
			//显示查看公告的模态框
			$('#notice_look_modalWin').modal('show');
		}
	});
	
});
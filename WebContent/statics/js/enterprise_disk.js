$(function(){
	/**
	 * 获取企业网盘根目录
	 */
	$.ajax({
		url:'file/getEnterpriseRoot.do',
		type:'POST',
		async:false,//同步
		dataType:'json',
		success:function(result){
			//把根路径放到隐藏域上
			$('#enterprise_disk_hidePath').val(result.root);
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 初始化表格
	 */
	$('#enterprise_disk_table').bootstrapTable({
		url:'file/getFiles.do',
		mthod:'post',
		queryParams: function(params){
			var param = {
					'dictionaryPath' : $('#enterprise_disk_hidePath').val()
			}
			return param;
		},
		columns:[{
			field : 'name',
			title : '文件名',
			width : '500'
		},{
			field : 'size',
			title : '大小'
		},{
			field : 'uploadUser',
			title : '上传人'
		},{
			field : 'uploadDate',
			title : '上传时间'
		},{
			field : 'delOption',
			title : ''
		}],
		onLoadSuccess: function(){
			//监听点击文件夹按钮
			$('.enterprise_disk_dicBtn').bind('click', function(){
				//点击文件夹，改变当前路径隐藏域
				$('#enterprise_disk_hidePath').val($('#enterprise_disk_hidePath').val() + '\\' + $(this).text());
				$('#enterprise_disk_breadcrumb').text($('#enterprise_disk_breadcrumb').text()+' > '+$(this).text());
				//刷新表格
				$('#enterprise_disk_table').bootstrapTable('refresh');
			});
			
			//监听删除文件按钮
			$('.disk_delBtn').bind('click', function(){
				$.ajax({
					url:'file/delete.do',
					type:'POST',
					data:{'fileId':$(this).attr('fileId')},
					dataType:'json',
					success:function(result){
						alert(result.result);
						//刷新表格
						$('#enterprise_disk_table').bootstrapTable('refresh');
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
	
	/**
	 * 表面按钮触发真实的上传按钮
	 */
	$('#enterprise_disk_uploadBtn2').bind('click', function(){
		$('#enterprise_disk_uploadBtn1').click();
	});
	
	/**
	 * 选择了文件后，开始上传
	 */
	$('#enterprise_disk_uploadBtn1').bind('change', function(){
		var formData = new FormData($('#enterprise_disk_uploadForm')[0]);
		$.ajax({
			url:'file/upload.do',
			type:'POST',
			data : formData,
			dataType:'json',
			processData: false,
			contentType: false,
			success: function(result){
				alert(result.result);
				//刷新表格
				$('#enterprise_disk_table').bootstrapTable('refresh');
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
	});
	
	/**
	 * 新建文件夹模态框中的确定按钮
	 */
	$('#enterprise_disk_newDicModal_confirmBtn').bind('click', function(){
		var dicName = $('#enterprise_disk_newDicModal_dicName').val();
		var dicNamePath = $('#enterprise_disk_hidePath').val() + '\\' + dicName;
		$.ajax({
			url:'file/newDictionary.do',
			type:'POST',
			data:{'dicNamePath' : dicNamePath,
				  'dicName' : dicName},
			dataType:'json',
			success:function(result){
				alert(result.result);
				$('#enterprise_disk_table').bootstrapTable('refresh');
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
	});
	
});
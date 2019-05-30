$(function(){
	
	/**
	 * 获取个人网盘的目录，没有的话就创建
	 */
	$.ajax({
		url:'file/getPersonalRoot.do',
		type:'POST',
		async:false,
		dataType:'json',
		success:function(result){
			$('#personal_disk_hidePath').val(result.root);
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	/**
	 * 通过根目录初始化bootstrapTable
	 */
	$('#personal_disk_table').bootstrapTable({
		url:'file/getFiles.do',
		type:'post',
		queryParams : function(params){
			var param = {
					'dictionaryPath' : $('#personal_disk_hidePath').val()
			};
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
		onLoadSuccess:function(){
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
						$('#personal_disk_table').bootstrapTable('refresh');
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
	
	//表面上传按钮触发实际上传按钮
	$('#personal_disk_uploadBtn2').bind('click', function(){
		$('#personal_disk_uploadBtn1').click();
	});
	
	$('#personal_disk_uploadBtn1').bind('change', function(){
		var formData = new FormData($('#personal_disk_uploadForm')[0]);
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
				$('#personal_disk_table').bootstrapTable('refresh');
			},
			error:function(){
				//没有权限
				$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
				$("#index_main_content").css('padding','');
			}
		});
	});
	
});
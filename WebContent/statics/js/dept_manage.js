$(function(){
	
	/**
	 * 获取部门列表
	 */
	$.ajax({
		url:'system/getDeptList.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			$.each(result, function(i, n){
				var html = '<option value="'+n.id+'">'+n.name+'</option>';
				$('#dept_manage_deptSelect').append(html);
			});
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
});
$(function(){
	//获取用户信息
	$.ajax({
		type:"POST",
		url:"getLoginUser.do",
		dataType:"json",
		success:function(result){
			$("#index_user_tip").text(result.name);
			$('#index_user_img').attr('src', result.icon);
			$('#index_user_img_big').attr('src', result.icon);
		}
	});
	
	//加载我的任务界面
	$("#index_myTask").bind("click",function(){
		$("#index_main_content").load("task/my_task.html");
	});
	
	//加载项目甘特图界面
	$("#index_gante").bind("click",function(){
		$("#index_main_content").load("task/my_gante.html");
		$('#index_header_tip').text('甘特图');
	});
	
	//加载项目界面
	$("#index_myProject").bind("click",function(){
		$("#index_main_content").load("/GeneralProject/page/task/my_project.html");
		//$('#index_header_tip').html('<i class="fa fa-suitcase"></i>&nbsp;我的项目');
		//$('#index_main_header').hide();
	});
	
	$('#index_myMessage').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/message/my_message.html");
	});
	
});	
	
	
	

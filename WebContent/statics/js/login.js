$(function(){
	$("#login_btn").bind("click",function(){
		$.ajax({
			type:"POST",
			url:"/GeneralProject/page/login.do",
			data:{"username":$("#login_username").val(),
				  "pwd":$("#login_pwd").val()},
			dataType:"json",
			success:function(result){
				if(result.result=="true")
					window.location.href="/GeneralProject/page/index.html";
				else{
					alert(result.result);
				}
			}
		});
	});
});
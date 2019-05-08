$(function(){
	
	//发送ajax获取所有用户
	$.ajax({
		url:'getUserList.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			
			
			//发送ajax获取服务器地址
			$.ajax({
				url:'message/getSocketPath.do',
				type:'POST',
				dataType:'json',
				success:function(result){
					
					var socketPath = result.socketPath;
					//alert(socketPath);
					//alert("ws://"+socketPath+"/ws");   ws://localhost:8081/GeneralProject//ws
					var webSocket = new WebSocket("ws://"+socketPath+"/ws");
					webSocket.onopen = function(event){
					    console.log("连接成功");
					    console.log(event);
					};
					webSocket.onerror = function(event){
					    console.log("连接失败");
					    console.log(event);
					};
					webSocket.onclose = function(event){
					    console.log("Socket连接断开");
					    console.log(event);
					};
					webSocket.onmessage = function(event){
						console.dir(event);
					    //接受来自服务器的消息
					    var data = JSON.parse(event.data);
					    
					    if(data.messageId == 0){//若接受的消息是广播在线用户集合
					    	//在线用户数组
					    	var onlineUsers = JSON.parse(data.messageText);
					    	
					    	var userLis = $('.my_message_online_users_eachUser_img');
					    	//给每个头像图片变黑白
					    	$.each(userLis, function(i, n){
					    		if(!$(n).hasClass('gray'))
					    			$(n).addClass('gray');
					    	});
					    	//在线用户取消黑白，改为彩色
					    	$.each(userLis, function(i, n){
					    		$.each(onlineUsers, function(ii, nn){
					    			if(nn.username == $(n).attr('name').split('&')[0])
					    				$(n).removeClass('gray');
					    		});
					    	});
					    	
					    }
					    else if(data.messageId == 1){//若接受的消息是发送的信息
					    	//收到信息后，封装send按钮的toWho属性
					    	$('#my_message_chat_send').attr('toWho', data.fromId.split('-')[0]+'&'+data.fromName);
					    	//收到信息后，聊天框改为“与...聊天中”
					    	$('#my_message_chat_title').text('与 ' + data.fromName + '(' + data.fromId.split('-')[0] + ') 聊天中...');
					    	var html = '<div class="direct-chat-msg"><div class="direct-chat-info clearfix"><span class="direct-chat-name pull-left">'+data.fromId.split('-')[0]+'('+data.fromName+')'+'</span><span class="direct-chat-timestamp pull-right">'+data.messageDate+'</span></div><img class="direct-chat-img" src="'+data.fromId.split('-')[1]+'" alt="Message User Image"><div class="direct-chat-text">'+data.messageText+'</div></div>';
					    	$('#my_message_chat_chatPanel').append(html);
					    }
					} 
					
					/*$('#toLucy').bind('click', function(){
						var data = {};//新建data对象，并规定属性名与相应的值
			            data['fromId'] = 'peter';
			            data['fromName'] = '林冲';
			            data['toId'] = 'lucy';
			            data['messageText'] = 'peter send to lucy';
			            webSocket.send(JSON.stringify(data));//将对象封装成JSON后发送至服务器
						
					});
					
					$('#toPeter').bind('click', function(){
						var data = {};//新建data对象，并规定属性名与相应的值
			            data['fromId'] = 'lucy';
			            data['fromName'] = '李春';
			            data['toId'] = 'peter';
			            data['messageText'] = 'lucy send to peter';
			            webSocket.send(JSON.stringify(data));//将对象封装成JSON后发送至服务器
						
					});*/
					
					//点击发送按钮，发送websocket
					$('#my_message_chat_send').bind('click', function(){
						var toWho = $(this).attr('toWho').split('&')[0];
						var toName = $(this).attr('toWho').split('&')[1];
						var content = $('#my_message_chat_sendContent').val();
						var fromWho = '';
						var fromName = '';
						var fromIcon = '';
						
						//清空输入框
						$('#my_message_chat_sendContent').val('');
						
						//同步获取用户信息
						$.ajax({
							type:"POST",
							url:"getLoginUser.do",
							dataType:"json",
							async:false,
							success:function(result){
								fromWho = result.username;
								fromName = result.name;
								fromIcon = result.icon;
							}
						});
						
						//自己的页面显示一条自己发送的信息
						var html = '<div class="direct-chat-msg right"><div class="direct-chat-info clearfix"><span class="direct-chat-name pull-right">我</span><span class="direct-chat-timestamp pull-left">23 Jan 2:05 pm</span></div><img class="direct-chat-img" src="'+fromIcon+'" alt="Message User Image"><div class="direct-chat-text">'+content+'</div></div>';
						$('#my_message_chat_chatPanel').append(html);
						
						//发送信息
						var data = {};//新建data对象，并规定属性名与相应的值
						data['messageId'] = 1;//发送消息id设置为1
			            data['fromId'] = fromWho + '-' + fromIcon;
			            data['fromName'] = fromName;
			            data['toId'] = toWho;
			            data['toName'] = toName;
			            data['messageText'] = content;
			            webSocket.send(JSON.stringify(data));//将对象封装成JSON后发送至服务器            
			            
					});
					
				}
			});
			
			
			$('#my_message_online_users_ul').html('');
			//遍历所有用户
			$.each(result, function(i, n){
				var html = '<li><img src="'+n.icon+'" name="'+n.username+'&'+n.name+'" class="gray my_message_online_users_eachUser_img" alt="用户头像"><a class="users-list-name my_message_online_users_eachUser" name="'+n.username+'&'+n.name+'" href="javascript:void(0)">'+n.name+'</a><span class="users-list-date">'+n.username+'</span></li>';
				$('#my_message_online_users_ul').append(html);
			});
			
			//点击每个在线用户，右边聊天窗口切换对应用户
			$('.my_message_online_users_eachUser').bind('click', function(){
				var attrName = $(this).attr('name');
				var username = attrName.split('&')[0];
				var name = attrName.split('&')[1];
				$('#my_message_chat_title').text('与 ' + name + '(' + username + ') 聊天中...');
				$('#my_message_chat_send').attr('toWho', attrName);
			});
			
			
			
			
		}
	});
	
	

	
	
});
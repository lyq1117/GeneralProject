$(function(){
	
	
	//expandable点击事件
	$('button[data-widget="collapse"]').bind('click',function(){
		var parent = $($($(this).parent()).parent()).parent();
		if($(parent).hasClass('collapsed-box'))
			$(parent).removeClass('collapsed-box');
		else
			$(parent).addClass('collapsed-box');
	});
	
	//ajax加载当前用户的工程
	$.ajax({
		url:'task/getMyProjectList.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			addProject(result);
		}
	});
	
	//看板视图添加事件
	function addProject(result){
		$.each(result, function(i, n){
			var project = JSON.parse(n.project);
			var blocks = JSON.parse(n.blocks);
			var html = '<div class="col-md-3"><div class="box box-primary"><div class="box-header with-border"><h3 class="box-title"><a href="#" name="' + project.id + '" class="my_project_view_edit_project">' + project.name + '</a></h3><a href="javascript:void(0)" name="' + project.id + '" class="pull-right my_project_view_gantt"><i class="glyphicon glyphicon-align-left"></i><a></div><div class="box-body" style="height:300px;overflow-y:scroll;">';
			$.each(blocks, function(i2, n2){
				html += '<div class="info-box"><span class="info-box-icon bg-aqua"><i class="fa fa-list-alt"></i></span><div class="info-box-content"><a href="javascript:void(0)" class="my_project_view_edit_block" name="' + n2.id + '"><span class="info-box-number">' + n2.description + '</span></a><span class="info-box-text" style="color:rgb(253,203,53);"><i class="fa fa-clock-o"></i>' + millisecond2Date(n2.createTime) + ' 开始</span><a name="' + n2.id + '" class="my_project_view_block" data-toggle="modal" href="javascript:void(0)"><span data-toggle="tooltip" class="badge bg-green">' + n2.leader.name.substring(0,1) + '</span></a></div></div>';              
			});
			html += '</div><div class="box-footer"> <a href="#" name="' + project.id + '" class="small-box-footer my_project_view_add_block">添加任务 <i class="fa fa-plus-circle"></i></a> </div></div></div>';
			$('#my_project_view_row').append(html);
		});
		
		//点击工程小块的负责人，可切换负责人
		$('.my_project_view_block').bind('click',function(){
			//打开模态框
			$('#my_project_block_leader').modal();
			//暂存工程小块id
			var blockId = $(this).attr('name');
			$.ajax({
				url:'task/getUsersByBlockId.do',
				type:'POST',
				data:{'blockId' : $(this).attr('name')},
				dataType:'json',
				success:function(result){
					//展示团队列表
					showTeam(result);
					
					//添加点击事件，点击团队成员切换工程小块负责人
					$('.my_project_block_leader_team_change').bind('click', function(){
						//alert($(this).attr('name') + ' ' + blockId);
						var username = $(this).attr('name');
						//发送ajax，更改工程小块负责人
						$.ajax({
							url:'task/updateBlockLeader.do',
							type:'POST',
							data:{'username' : username,
								  'blockId'  : blockId},
							dataType:'text',
							success: function(result){
								//alert(result);
								//关闭切换负责人窗口
								$('#my_project_block_leader').modal('hide');
								setTimeout(function(){
									//重新载入主界面的content
									$('#index_main_content').load('/GeneralProject/page/task/my_project.html');
								},400);
							}
						});
					});
				}
			});
			
			//展示团队列表
			function showTeam(result){
				$('#my_project_block_leader_team').html('');
				var html = '<ul class="nav nav-stacked">';
				$.each(result, function(i, n){
					html += '<li><a href="#" name="' + n.username + '" class="my_project_block_leader_team_change" style="color:gray">&nbsp;' + n.username + '-' + n.name + '<span class="pull-left badge bg-green">' + n.name.substring(0,1) + '</span></a></li>';
				});
				html += '</ul>';
				$('#my_project_block_leader_team').append(html);
			}
			
			
		});
		
		//点击下方“添加任务”按钮事件
		$('.my_project_view_add_block').bind('click', function(){
			//工程id
			var projectId = $(this).attr('name');
			$('#my_project_view_addBlock').modal('show');
			//确定按钮事件
			$('#my_project_view_addBlock_confirmBtn').bind('click', function(){
				var description = $('#my_project_view_addBlock_description').val();
				var createTime = $('#my_project_view_addBlock_createTime').val();
				var duration = $('#my_project_view_addBlock_duration').val();
				var status = $('#my_project_view_addBlock_status').val();
				//发送ajax，保存任务信息
				$.ajax({
					url:'task/addBlock.do',
					type:'POST',
					data:{'projectId' : projectId,
						  'description' : description,
						  'createTime' : createTime,
						  'duration' : duration,
						  'status' : status},
					dataType:'json',
					success:function(result){
						if(result.result == 'true'){
							alert('添加成功!');
							//关闭窗口
							$('#my_project_view_addBlock').modal('hide');
							setTimeout(function(){
								//重新加载内容
								$('#index_main_content').load('/GeneralProject/page/task/my_project.html');
							},400);
						}
						else{
							alert('添加失败!');
							//关闭窗口
							$('#my_project_view_addBlock').modal('hide');
						}
					}
				});
			});
		});
		
		//点击工程标题，显示编辑工程界面
		$('.my_project_view_edit_project').bind('click', function(){
			var projectId = $(this).attr('name');//点击的工程的id
			$('#my_project_view').load('/GeneralProject/page/task/my_project_detail.html',null,function(){
				//发送ajax，根据工程id获取工程信息
				$.ajax({
					url:'task/getProjectById.do',
					type:'POST',
					async:false,
					data:{'projectId' : projectId},
					dataType:'json',
					success:function(result){
						var project = JSON.parse(result.project)
						var projectId = project.id;
						var projectName = project.name;
						var projectDescription = project.description;
						var projectCreateTime = millisecond2Date(project.createTime);
						var projectDuration = project.duration;
						var projectStatus = project.status;
						/*alert(projectDescription + '-' + millisecond2Date(projectCreateTime) + '-' + 
							projectDuration + '-' + projectStatus);*/
						$('#my_project_detail_id').val(projectId);
						$('#my_project_detail_projectName').val(projectName);
						$('#my_project_detail_projectDescription').val(projectDescription);
						$('#my_project_detail_createTime').val(projectCreateTime);
						$('#my_project_detail_duration').val(projectDuration);
						$('#my_project_detail_status').val(projectStatus);
						var projectLeader = project.user;
						$('#my_project_detail_leader').val(projectLeader.username + '-' + projectLeader.name);
					}
				});
				
				//初始化成员列表表格
				$('#my_project_detail_membersTable').bootstrapTable({
					url:'task/getMembersOfProject.do?projectId='+projectId,
					pagination: true,
					clickToSelect: true ,
					sidePagination: 'client',
					pageSize: 5,
					onLoadSuccess : function(){
						//添加删除成员按钮事件
						$('.my_project_detail_deleteBtn').bind('click',function(){
							//alert($(this).attr('name') + '--' + projectId);
							var username = $(this).attr('name');
							//发送ajax，将用户从工程成员中删除
							$.ajax({
								url:'task/deleteUserProject.do',
								type:'POST',
								data:{'username' : username,
									  'projectId' : projectId},
								dataType:'json',
								success: function(result){
									if(result.result == 'true'){
										alert('删除成功!');
										//刷新工程成员表格
										$('#my_project_detail_membersTable').bootstrapTable('refresh');
									}
									else{
										alert('删除失败!');
									}
								}
							});
						});
					}
				});
				
			});
		});
		
		//点击工程小块描述，显示编辑工程小块窗口
		$('.my_project_view_edit_block').bind('click', function(){
			//工程小块(即任务)的id
			var blockId = $(this).attr('name');
			$('#my_project_view').load('/GeneralProject/page/task/my_block_detail.html', function(){
				//发送ajax获取工程小块信息
				$.ajax({
					url:'task/getBlockById.do',
					type:'POST',
					data:{'blockId' : blockId},
					dataType:'json',
					success: function(result){
						//alert(JSON.stringify(result));
						$('#my_block_detail_id').val(result.id);
						$('#my_block_detail_blockDescription').val(result.description);
						$('#my_block_detail_createTime').val(millisecond2Date(result.createTime));
						$('#my_block_detail_duration').val(result.duration);
						$('#my_block_detail_status').val(result.status);
						$('#my_block_detail_leader').val(result.leader.username + '-' + result.leader.name);
						var projectId = result.projectId;
						//发送ajax，通过工程id查询工程信息
						$.ajax({
							url:'task/getProjectById.do',
							type:'POST',
							data:{'projectId' : projectId},
							dataType:'json',
							success: function(result2){
								$('#my_block_detail_projectId').val(JSON.parse(result2.project).id)
								$('#my_block_detail_projectName').val(JSON.parse(result2.project).name);
							}
						});
					}
				});
				//发送ajax,获取工程小块(即任务)成员
				/*$.ajax({
					url:'task/getMembersOfBlock.do',
					type:'POST',
					data:{'blockId':blockId},
					dataType:'json',
					success:function(result){
						alert(JSON.stringify(result));
					}
				});*/
				$('#my_block_detail_membersTable').bootstrapTable({
					url:'task/getMembersOfBlock.do?blockId=' + blockId,
					pagination: true,
					clickToSelect: true ,
					sidePagination: 'client',
					pageSize: 5,
					onLoadSuccess : function(){
						//添加删除成员按钮事件
						$('.my_block_detail_deleteBtn').bind('click',function(){
//							alert($(this).attr('name') + '--' + blockId);
							var username = $(this).attr('name');
							//发送ajax，删除用户-工程小块(任务)关联
							$.ajax({
								url:'task/deleteUserBlock.do',
								type:'POST',
								data:{'username' : username,
									  'blockId'  : blockId },
								dataType:'json',
								success:function(result){
									if(result.result == 'true'){
										alert('删除成功!');
										//刷新表格
										$('#my_block_detail_membersTable').bootstrapTable('refresh');
									}
									else
										alert('删除失败!');
								}
							});
						});
					}
				});
				
			});
		});
		
		//点击项目上的甘特图
		$('.my_project_view_gantt').bind('click', function(){
			var projectId = $(this).attr('name');
			$('#my_project_view').load('/GeneralProject/page/task/project_gantt.html',function(){
				//加载完成，把projectId赋值到project_gantt.html页面上
				$('#project_gantt_projectId').val(projectId);
			});
		});
	}
	
	
	
	//毫秒转化成yyyy-MM-dd的日期
	function millisecond2Date(milliSecond){
		var date = new Date(milliSecond);
		return date.getFullYear() + '-' + ((date.getMonth()+1)>9 ? (date.getMonth()+1) : '0' + (date.getMonth()+1))
			   + '-' + (date.getDate()>9 ? date.getDate() : '0' + date.getDate() );
	}
	
	//获取用户信息
	$.ajax({
		type:"POST",
		url:"getLoginUser.do",
		dataType:"json",
		success:function(result){
			$("#my_project_view_new_name").text(result.name.substring(0,1));
			/*$('#index_user_img').attr('src', result.icon);
			$('#index_user_img_big').attr('src', result.icon);*/
		}
	});
	
	//添加工程中选择创建时间事件
	$('#my_project_view_new_create_time').bind('click', function(){
		//打开选择创建时间的模态框
		$('#my_project_view_new_create_time_modal').modal('show');
		//日期控件初始化
		$('#my_project_view_create_time_modal_ct').datepicker({
			format:'yyyy-mm-dd'
		});
		$('#my_project_view_create_time_modal_ct').val('');
		$('#my_project_view_create_time_modal_duration').val('');
	});
	//初始化省份、城市数组
	var cityArray = new Array(); //定义 城市 数据数组

	cityArray[0] = new Array("北京市","东城|西城|崇文|宣武|朝阳|丰台|石景山|海淀|门头沟|房山|通州|顺义|昌平|大兴|平谷|怀柔|密云|延庆");

	cityArray[1] = new Array("上海市","黄浦|卢湾|徐汇|长宁|静安|普陀|闸北|虹口|杨浦|闵行|宝山|嘉定|浦东|金山|松江|青浦|南汇|奉贤|崇明");

	cityArray[2] = new Array("天津市","和平|东丽|河东|西青|河西|津南|南开|北辰|河北|武清|红挢|塘沽|汉沽|大港|宁河|静海|宝坻|蓟县");

	cityArray[3] = new Array("重庆市","万州|涪陵|渝中|大渡口|江北|沙坪坝|九龙坡|南岸|北碚|万盛|双挢|渝北|巴南|黔江|长寿|綦江|潼南|铜梁 |大足|荣昌|壁山|梁平|城口|丰都|垫江|武隆|忠县|开县|云阳|奉节|巫山|巫溪|石柱|秀山|酉阳|彭水|江津|合川|永川|南川");

	cityArray[4] = new Array("河北省","石家庄|邯郸|邢台|保定|张家口|承德|廊坊|唐山|秦皇岛|沧州|衡水");

	cityArray[5] = new Array("山西省","太原|大同|阳泉|长治|晋城|朔州|吕梁|忻州|晋中|临汾|运城");

	cityArray[6] = new Array("内蒙古自治区","呼和浩特|包头|乌海|赤峰|呼伦贝尔盟|阿拉善盟|哲里木盟|兴安盟|乌兰察布盟|锡林郭勒盟|巴彦淖尔盟|伊克昭盟");

	cityArray[7] = new Array("辽宁省","沈阳|大连|鞍山|抚顺|本溪|丹东|锦州|营口|阜新|辽阳|盘锦|铁岭|朝阳|葫芦岛");

	cityArray[8] = new Array("吉林省","长春|吉林|四平|辽源|通化|白山|松原|白城|延边");

	cityArray[9] = new Array("黑龙江省","哈尔滨|齐齐哈尔|牡丹江|佳木斯|大庆|绥化|鹤岗|鸡西|黑河|双鸭山|伊春|七台河|大兴安岭");

	cityArray[10] = new Array("江苏省","南京|镇江|苏州|南通|扬州|盐城|徐州|连云港|常州|无锡|宿迁|泰州|淮安");

	cityArray[11] = new Array("浙江省","杭州|宁波|温州|嘉兴|湖州|绍兴|金华|衢州|舟山|台州|丽水");

	cityArray[12] = new Array("安徽省","合肥|芜湖|蚌埠|马鞍山|淮北|铜陵|安庆|黄山|滁州|宿州|池州|淮南|巢湖|阜阳|六安|宣城|亳州");

	cityArray[13] = new Array("福建省","福州|厦门|莆田|三明|泉州|漳州|南平|龙岩|宁德");

	cityArray[14] = new Array("江西省","南昌市|景德镇|九江|鹰潭|萍乡|新馀|赣州|吉安|宜春|抚州|上饶");

	cityArray[15] = new Array("山东省","济南|青岛|淄博|枣庄|东营|烟台|潍坊|济宁|泰安|威海|日照|莱芜|临沂|德州|聊城|滨州|菏泽");

	cityArray[16] = new Array("河南省","郑州|开封|洛阳|平顶山|安阳|鹤壁|新乡|焦作|濮阳|许昌|漯河|三门峡|南阳|商丘|信阳|周口|驻马店|济源");

	cityArray[17] = new Array("湖北省","武汉|宜昌|荆州|襄樊|黄石|荆门|黄冈|十堰|恩施|潜江|天门|仙桃|随州|咸宁|孝感|鄂州");

	cityArray[18] = new Array("湖南省","长沙|常德|株洲|湘潭|衡阳|岳阳|邵阳|益阳|娄底|怀化|郴州|永州|湘西|张家界");

	cityArray[19] = new Array("广东省","广州|深圳|珠海|汕头|东莞|中山|佛山|韶关|江门|湛江|茂名|肇庆|惠州|梅州|汕尾|河源|阳江|清远|潮州|揭阳|云浮");

	cityArray[20] = new Array("广西壮族自治区","南宁|柳州|桂林|梧州|北海|防城港|钦州|贵港|玉林|南宁地区|柳州地区|贺州|百色|河池");

	cityArray[21] = new Array("海南省","海口|三亚");

	cityArray[22] = new Array("四川省","成都|绵阳|德阳|自贡|攀枝花|广元|内江|乐山|南充|宜宾|广安|达川|雅安|眉山|甘孜|凉山|泸州");

	cityArray[23] = new Array("贵州省","贵阳|六盘水|遵义|安顺|铜仁|黔西南|毕节|黔东南|黔南");

	cityArray[24] = new Array("云南省","昆明|大理|曲靖|玉溪|昭通|楚雄|红河|文山|思茅|西双版纳|保山|德宏|丽江|怒江|迪庆|临沧");

	cityArray[25] = new Array("西藏自治区","拉萨|日喀则|山南|林芝|昌都|阿里|那曲");

	cityArray[26] = new Array("陕西省","西安|宝鸡|咸阳|铜川|渭南|延安|榆林|汉中|安康|商洛");

	cityArray[27] = new Array("甘肃省","兰州|嘉峪关|金昌|白银|天水|酒泉|张掖|武威|定西|陇南|平凉|庆阳|临夏|甘南");

	cityArray[28] = new Array("宁夏回族自治区","银川|石嘴山|吴忠|固原");

	cityArray[29] = new Array("青海省","西宁|海东|海南|海北|黄南|玉树|果洛|海西");

	cityArray[30] = new Array("新疆维吾尔族自治区","乌鲁木齐|石河子|克拉玛依|伊犁|巴音郭勒|昌吉|克孜勒苏柯尔克孜|博尔塔拉|吐鲁番|哈密|喀什|和田|阿克苏");

	cityArray[31] = new Array("香港特别行政区","香港特别行政区");

	cityArray[32] = new Array("澳门特别行政区","澳门特别行政区");

	cityArray[33] = new Array("台湾省","台北|高雄|台中|台南|屏东|南投|云林|新竹|彰化|苗栗|嘉义|花莲|桃园|宜兰|基隆|台东|金门|马祖|澎湖");
	//添加工程中选择地点
	$('#my_project_view_new_project_location').bind('click', function(){
		$('#my_project_view_new_project_locationWin').show();
		//先清空
		$('#my_project_view_new_project_locationWin_province').html('');
		$('#my_project_view_new_project_locationWin_city').html('');
		//遍历城市数组，在左边框列出省份
		$.each(cityArray, function(i, n){
			var html = '<option class="my_project_view_new_project_locationWin_province_option" index="' + i + '">' + n[0] + '</option>';
			$('#my_project_view_new_project_locationWin_province').append(html);
		});
		//省份选项的点击事件
		$('.my_project_view_new_project_locationWin_province_option').bind('click', function(){
			var citys = cityArray[$(this).attr('index')][1].split('|');
			//先清空
			$('#my_project_view_new_project_locationWin_city').html('');
			$.each(citys, function(i, n){
				var html = '<option class="my_project_view_new_project_locationWin_city_option" index="' + i + '">' + n + '</option>';
				$('#my_project_view_new_project_locationWin_city').append(html);
			});
			//城市选项的点击事件
			$('.my_project_view_new_project_locationWin_city_option').bind('click', function(){
				$('#my_project_view_new_project_location').val($(this).text());
				$('#my_project_view_new_project_locationWin').hide();
			});
		});
	});
	//添加工程中选择地点窗口的鼠标移开事件
	$('#my_project_view_new_project_locationWin').bind('mouseleave', function(){
		$(this).hide();
	});
	//选择创建时间和工期的确定按钮事件
	$('#my_project_view_create_time_modal_confirm').bind('click', function(){
		var createTime = $('#my_project_view_create_time_modal_ct').val();
		var duration = $('#my_project_view_create_time_modal_duration').val();
		if(isEmpty(createTime) || isEmpty(duration)){
			alert('不能为空');
			return;
		}
		//alert(createTime + ' ' + duration);
		$('#my_project_view_new_create_time').append(createTime + ' ' + duration);
		//模态框关闭
		$('#my_project_view_new_create_time_modal').modal('hide');
	});
	
	//判断字符串空
	function isEmpty(str){
		return str==null||str=='';
	}
	
	//新建工程的确定按钮
	$('#my_project_view_new_confirm_btn').bind('click', function(){
		var projectName = $('#my_project_view_new_project_name').val();
		var createTimeStr = $('#my_project_view_new_create_time').text();
		var createTime = createTimeStr.split(' ')[0];
		var duration = createTimeStr.split(' ')[1];
		var location = $('#my_project_view_new_project_location').val();
		
		
		
		//alert(projectName + '*' + createTime + '*' + duration);
		if(isEmpty(projectName) || isEmpty(createTime) || isEmpty(duration) || isEmpty(location)){
			alert("信息不能空");
			return;
		}
		
		//调用百度api，根据地点获取经纬度
		var myGeo = new BMap.Geocoder();
		myGeo.getPoint(location, function(point){      
		    if (point) {     
		    	
		    	//发送ajax请求，添加工程
				$.ajax({
					url:'task/addProject.do',
					type:'POST',
					data:{'projectName' : projectName,
						  'createTime' : createTime,
						  'duration' : duration,
						  'location' : location,
						  'lng' : point.lng,
						  'lat' : point.lat},
					dataType:'text',
					success:function(result){
						alert(result);
						$('#index_main_content').load('/GeneralProject/page/task/my_project.html');
					}
				});
		    	
		    }
		 });
		
		
		
	});
	
	
	//加载工程附件
	function loadProjectFiles(){
		$.ajax({
			url:'task/getMyProjectList.do',
			async: false,
			type:'POST',
			dataType:'json',
			success: function(result){
				$('#my_project_projectFile').html('');
				$.each(result, function(i, n){
					//alert(JSON.stringify(n));
					var project = JSON.parse(n.project);
					var html = '';
					html += '<div class="col-md-6"><div class="box box-primary"><div class="box-header with-border"><h3 class="box-title">' + project.name + '</h3><div class="pull-right"><form action="file/fileUpload.do" name="my_project_fileUpload_form_' + project.id + '" id="my_project_fileUpload_form_' + project.id + '" method="POST" enctype="multipart/form-data"><input type="file" value="添加附件" name="my_project_uploadFile" projectId="'+project.id+'" class="my_project_uploadFile" multiple="multiple"><input type="hidden" name="projectId" value="'+project.id+'"/></form></div></div><div class="box-body" style="height:150px;overflow-y: scroll;"><ul class="products-list product-list-in-box" id="my_project_fileList_' + project.id + '">';
					//发送ajax获取项目的附件文件集合
					$.ajax({
						url:'file/getProjectFiles.do',
						data:{'projectId' : project.id},
						async: false,
						dataType:'json',
						success:function(result){
							//alert(JSON.stringify(result));
							if(result.result == 'false'){
								html += '</ul></div></div></div>';
							}
							else{
								$.each(result, function(ii,nn){
									//alert(nn.name);
									html += '<li class="item"><div class="product-img"><img src="/GeneralProject/statics/image/file.jpg" alt="Product Image"></div><div class="product-info"><a href="file/fileDownload.do?projectId=' + project.id + '&fileName=' + nn.name + '" class="product-title">' + nn.name + '</a><a href="javascript:void(0);" class="my_project_file_delete" name="'+project.id+'&'+nn.name+'"><span class="label label-danger pull-right">x</span></a></div></li>';
								});
								html += '</ul></div></div></div>';
							}
							
							
						}
					});
					//$('#my_project_projectFile').html('');
					$('#my_project_projectFile').append(html);
				});
				
				
			}
		});
	}
	//调用加载工程附件方法
	loadProjectFiles();
	
	
	//监听文件选择框
	$('.my_project_uploadFile').bind('change', function(){
		//alert('选择文件' + $(this).attr('name'));
		//$('#my_project_fileUpload_form_' + $(this).attr('projectId')).submit();
		var formData = new FormData($('#my_project_fileUpload_form_' + $(this).attr('projectId'))[0]);
		$.ajax({
			url:'file/fileUpload.do',
			type:'POST',
			data : formData,
			dataType:'json',
			processData: false,
			contentType: false,
			success: function(result){
				//alert(result.result);
				if(result.result == 'true'){
					alert('添加附件成功');
					//重新加载我的项目界面并且，激活附件tab pane
					$("#index_main_content").load("/GeneralProject/page/task/my_project.html", function(){
						$('#my_project_tabUl_fj').click();
					});
				}
				else{
					alert('添加附件失败');
					//loadProjectFiles();
				}
			}
		});
	});
	
	//删除文件(附件)按钮
	$('.my_project_file_delete').bind('click', function(){
		var arr = $(this).attr('name').split('&');
		var projectId = arr[0];
		var fileName = arr[1];
		$.ajax({
			url:'file/deleteFile.do',
			type:'POST',
			data:{'projectId' : projectId,
				  'fileName' : fileName},
			dataType:'json',
			success:function(result){
				//alert(JSON.stringify(result));
				if(result.result == 'true'){
					alert('删除附件成功');
					//重新加载我的项目界面并且，激活附件tab pane
					$('#index_main_content').load("/GeneralProject/page/task/my_project.html", function(){
						$('#my_project_tabUl_fj').click();
					});
				}
				else{
					alert('删除附件失败');
					//loadProjectFiles();
				}
			}
		});
	});
	
	
});



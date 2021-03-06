$(document).ready(function(){
	//获取用户信息
	$.ajax({
		type:"POST",
		url:"getLoginUser.do",
		dataType:"json",
		success:function(result){
			$("#index_user_tip").text(result.name);
			$('#index_user_img').attr('src', result.icon);
			$('#index_user_img_big').attr('src', result.icon);
			$('#index_user_idAndName').html(result.name+'<small>' + result.username + '</small>');
		}
	});
	
	/**
	 * 获取左边菜单栏
	 */
	$.ajax({
		url:'getLeftMenu.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			//alert(JSON.stringify(result));
			//遍历
			$.each(result, function(i, n){
				var html = '';
				html += '<li class="treeview"><a href="#"><i class="'+n.root.icon+'"></i> <span>'+n.root.name+'</span><span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span></a><ul class="treeview-menu">';
				//遍历根菜单的子菜单
				$.each(n.children, function(ii, nn){
					html += '<li><a href="javascript:void(0)" class="index_leftMenuBar_item" url="'+nn.url+'" id="index_personal_door"><i class="'+nn.icon+'"></i> <span>'+nn.name+'</span></a></li>';
				});
				html += '</ul></li>';
				//将没一级菜单添加到左边菜单栏
				$('#index_leftMenuBar').append(html);
			});
			
			//点击事件
			$('.index_leftMenuBar_item').bind('click', function(){
				$("#index_main_content").load($(this).attr('url'));
				$("#index_main_content").css('padding','0px');
			});
		}
	});
	
	//加载我的任务界面
	$("#index_myTask").bind("click",function(){
		$("#index_main_content").load("/GeneralProject/page/task/my_task.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载项目甘特图界面
	$("#index_gante").bind("click",function(){
		$("#index_main_content").load("task/my_gante.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载项目界面
	$("#index_myProject").bind("click",function(){
		$("#index_main_content").load("/GeneralProject/page/task/my_project.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载消息界面
	$('#index_myMessage').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/message/my_message.html");
		$("#index_main_content").css('padding','');
	});
	
	//加载企业工程概况
	$('#index_projectSurvey').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/statistics/project_survey.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载企业任务周报
	$('#index_enterpriceBlockWeekly').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/statistics/block_weekly.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载工程进度统计
	$('#index_projectProgressStatistics').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/statistics/project_progress_statistics.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载员工出勤统计
	$('#index_memberSignStatistics').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/statistics/member_sign_statistics.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载企业网盘
	$('#index_enterpriseDisk').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/disk/enterprise_disk.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载个人网盘
	$('#index_personalDisk').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/disk/personal_disk.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载审批页面
	$('#index_approval').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/application/approval.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载公告页面
	$('#index_notice').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/application/notice.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载个人门户页面
	$('#index_personal_door').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/door/personal_door.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载公司门户页面
	$('#index_enterprise_door').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/door/enterprise_door.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载用户管理界面
	$('#index_userManage').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/system/user_manage.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载角色管理界面
	$('#index_roleManage').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/system/role_manage.html");
		$("#index_main_content").css('padding','0px');
	});
	
	//加载部门管理界面
	$('#index_deptManage').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/system/dept_manage.html");
		$("#index_main_content").css('padding','0px');
	});
	
	
	//发送ajax获取公司所有项目
	$.ajax({
		url:'task/getAllProjects.do',
		type:'POST',
		dataType:'json',
		success: function(result){
			var markersData = new Array();
			
			$.each(result, function(i, n){
				//alert(JSON.stringify(n));
				var obj = new Object();
				var project = JSON.parse(n.project);
				var latAndLng = new Array();
				latAndLng[0] = project.lat;
				latAndLng[1] = project.lng;
				obj.latLng = latAndLng;
				obj.name = project.name + '-' + project.location;
				obj.id = project.id;
				markersData.push(obj);
			});
			
			$('#index_world-map-markers').vectorMap({
			    map              : 'world_mill_en',
			    normalizeFunction: 'polynomial',
			    hoverOpacity     : 0.7,
			    hoverColor       : false,
			    backgroundColor  : 'transparent',
			    regionStyle      : {
			      initial      : {
			        fill            : 'rgba(210, 214, 222, 1)',
			        'fill-opacity'  : 1,
			        stroke          : 'none',
			        'stroke-width'  : 0,
			        'stroke-opacity': 1
			      },
			      hover        : {
			        'fill-opacity': 0.7,
			        cursor        : 'pointer'
			      },
			      selected     : {
			        fill: 'yellow'
			      },
			      selectedHover: {}
			    },
			    markerStyle      : {
			      initial: {
			        fill  : '#00a65a',
			        stroke: '#111'
			      }
			    },
			    markers          : markersData,
			    onMarkerClick:function(e, code){
			    	//alert('click marker');
			    },
			    onMarkerOver:function(e, code){
			    	//清空内容
			    	$('#floatWin').html('');
			    	
			    	var event = window.event;
			    	//控制提示框的位置
			    	$('#floatWin').css('left', event.pageX+10+'px');
			    	$('#floatWin').css('top', event.pageY+10+'px');
			    	
			    	$('#floatWin').fadeIn('slow');//渐入显示提示框
			    	
			    	//发送ajax查询工程信息
			    	$.ajax({
			    		url:'task/getProjectById.do',
			    		type:'POST',
			    		data:{'projectId' : markersData[code].id},
			    		dataType:'json',
			    		success: function(result){
			    			var project = JSON.parse(result.project);
			    			var blocks = JSON.parse(result.blocks);
			    			var completeCount = 0;//记录完成的任务(工程小块)个数
			    			$.each(blocks, function(i, n){
			    				if(n.status == 2)
			    					completeCount++;
			    			});
			    			var percentage = completeCount/blocks.length*100; //计算完成百分比
			    			$('#floatWin').append('<a class="index_floatWin_title" href="javascritp:void(0)">'+project.name+'</a>' + '<br>');
			    			$('#floatWin').append('——' + (project.description==undefined?'':project.description) + '<br>');
			    			$('#floatWin').append(millisecond2Date(project.createTime) + '    工期' + project.duration + '天<br>');
			    			var progressHtml = '进度:<div class="progress progress-sm active"><div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="' + percentage + '" aria-valuemin="0" aria-valuemax="100" style="width: ' + percentage + '%"><span class="sr-only">20% Complete</span></div></div>';
			    			$('#floatWin').append(progressHtml);
			    			
			    			//点击地图中项目名称，跳转我的项目列表
			    			$('.index_floatWin_title').bind('click', function(){
			    				$("#index_main_content").load("/GeneralProject/page/task/my_project.html");
			    				$("#index_main_content").css('padding','0px');
			    			});
			    		}
			    	});
			    	
			    	$('#floatWin').bind('mouseleave',function(){
			    		$('#floatWin').hide();
			    	});
			    	
			    	
			    	
			    },
			    onMarkerOut:function(e, code){
			    	//$('#floatWin').hide();
			    }
			  });
			
		}
	});
	
	
	/*var markersData = [
    	{ latLng: [39.910925, 116.413384], name: '北京' },
	      { latLng: [41.90, 12.45], name: 'Vatican City' },
	      { latLng: [43.73, 7.41], name: 'Monaco' },
	      { latLng: [-0.52, 166.93], name: 'Nauru' },
	      { latLng: [-8.51, 179.21], name: 'Tuvalu' },
	      { latLng: [43.93, 12.46], name: 'San Marino' },
	      { latLng: [47.14, 9.52], name: 'Liechtenstein' },
	      { latLng: [7.11, 171.06], name: 'Marshall Islands' },
	      { latLng: [17.3, -62.73], name: 'Saint Kitts and Nevis' },
	      { latLng: [3.2, 73.22], name: 'Maldives' },
	      { latLng: [35.88, 14.5], name: 'Malta' },
	      { latLng: [12.05, -61.75], name: 'Grenada' },
	      { latLng: [13.16, -61.23], name: 'Saint Vincent and the Grenadines' },
	      { latLng: [13.16, -59.55], name: 'Barbados' },
	      { latLng: [17.11, -61.85], name: 'Antigua and Barbuda' },
	      { latLng: [-4.61, 55.45], name: 'Seychelles' },
	      { latLng: [7.35, 134.46], name: 'Palau' },
	      { latLng: [42.5, 1.51], name: 'Andorra' },
	      { latLng: [14.01, -60.98], name: 'Saint Lucia' },
	      { latLng: [6.91, 158.18], name: 'Federated States of Micronesia' },
	      { latLng: [1.3, 103.8], name: 'Singapore' },
	      { latLng: [1.46, 173.03], name: 'Kiribati' },
	      { latLng: [-21.13, -175.2], name: 'Tonga' },
	      { latLng: [15.3, -61.38], name: 'Dominica' },
	      { latLng: [-20.2, 57.5], name: 'Mauritius' },
	      { latLng: [26.02, 50.55], name: 'Bahrain' },
	      { latLng: [0.33, 6.73], name: 'São Tomé and Príncipe' }
	    ];*/
	
	
	//通过地址查询经纬度
	/*$('#queryL').bind('click', function(){
		var myGeo = new BMap.Geocoder();
		myGeo.getPoint("成都", function(point){      
		    if (point) {      
		        alert(point.lng + '-' + point.lat);    
		    }      
		 });
	});*/
	
	$('.sparkbar').each(function () {
	    var $this = $(this);
	    $this.sparkline('html', {
	      type    : 'bar',
	      height  : $this.data('height') ? $this.data('height') : '30',
	      barColor: $this.data('color')
	    });
	  });
	
	//毫秒转化成yyyy-MM-dd的日期
	function millisecond2Date(milliSecond){
		var date = new Date(milliSecond);
		return date.getFullYear() + '-' + ((date.getMonth()+1)>9 ? (date.getMonth()+1) : '0' + (date.getMonth()+1))
			   + '-' + (date.getDate()>9 ? date.getDate() : '0' + date.getDate() );
	}
	
	/**
	 * 获取工程总数和任务总数和员工数
	 */
	$.ajax({
		url:'getIndexTitleData.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			//alert(JSON.stringify(result));
			$('#index_title_projectCount').text(result.projectCount);//工程数
			$('#index_title_userCount').text(result.userCount);//用户数
			$('#index_title_blockCount').text(result.blockCount);//任务数
		}
	});
	
	/**
	 * 签到按钮
	 */
	$('#index_signBtn').bind('click', function(){
		$.ajax({
			url:'userSign.do',
			type:'POST',
			dataType:'json',
			success:function(result){
				//alert(result.result);
				if(result.result == "true"){
					alert('签到成功!');
					$('#index_signBtn').addClass('index_signBtn_disActive');
					$('#index_signBtn').unbind();
				}
				else{
					alert('签到失败!');
				}
			}
		});
	});
	
	/**
	 * 查询当前用户今日是否签过到
	 */
	$.ajax({
		url:'isUserSigned.do',
		type:'POST',
		dataType:'json',
		success:function(result){
			if(result.result == false){//没签过到
				$('#index_signBtn').removeClass('index_signBtn_disActive');
				
			}
			else{//签过到
				$('#index_signBtn').addClass('index_signBtn_disActive');
				$('#index_signBtn').unbind();
			}
		}
	});
	
	var now = new Date();//今天的日期
	var toDayDateStr = dateFormat(now);//格式化后的本周一日期 格式为yyyy-MM-dd
	$('#index_todayDateStr').text(toDayDateStr);//签到按钮上的日期显示
	//获取本周周一的日期
	/**
	 * 日期格式化成 yyyy-MM-dd
	 */
	function dateFormat(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var d = date.getDate();
		return year + '-' + (month>9?month:('0'+month)) + '-' + (d>9?d:('0'+d));
	}
	
	/**
	 * 用户下弹菜单的个人门户按钮
	 */
	$('#index_userDropdown_perDoorBtn').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/door/personal_door.html");
		$("#index_main_content").css('padding','0px');
	});
	
	/**
	 * 用户下弹菜单的企业门户按钮
	 */
	$('#index_userDropdown_entDoorBtn').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/door/enterprise_door.html");
		$("#index_main_content").css('padding','0px');
	});
	
	/**
	 * 用户下弹菜单的及时通信按钮
	 */
	$('#index_userDropdown_messageBtn').bind('click', function(){
		$("#index_main_content").load("/GeneralProject/page/message/my_message.html");
		$("#index_main_content").css('padding','');
	});
});	
	
	
	

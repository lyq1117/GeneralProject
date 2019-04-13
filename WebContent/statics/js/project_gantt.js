$(function(){
	
	var now = new Date();//当前日期
	var nowTime = now.getTime(); //当前日期毫秒
	var day = now.getDay();//当前星期
	var oneDayTime = 24*60*60*1000 ;//一天的毫秒数 
	
	//显示本周周一
	var MondayTime = nowTime - (day-1)*oneDayTime ; 
	
	//显示本周周日
	var SundayTime =  nowTime + (7-day)*oneDayTime ; 
	
	//初始化日期时间
	var monday = new Date(MondayTime);
	var sunday = new Date(SundayTime);
	
	
	var startYear = 2018;
	var endYear = 2022;
	
	var years = new Array();
	var yearsIndex = 0;
	
	for(var i=startYear; i<=endYear; i++){
		var year = new Object();
		var months = new Array();
		for(var j=1; j<=12; j++){
			//月对象
			var month = new Object();
			//月名称
			month.name = j;
			var dayNumber = getDaysOfMonth(i, j);
			//初始化当月天数数组
			var days = new Array();
			//遍历当月天数
			for(var k=1; k<=dayNumber; k++){
				days[k-1] = k;
			}
			//将本月天数装进月对象
			month.days = days;
			months[j-1] = month;
		}
		//年名称
		year.name = i;
		//月列表
		year.months = months;
		years[yearsIndex++] = year;
	}
	
	console.dir(years);
	
	//今日零点的时间
	var nowDateZero = new Date(now.getFullYear(),now.getMonth(),now.getDate());
	var nowDateZeroTenDaysAfter = new Date(now.getFullYear(),now.getMonth(),now.getDate()+20);
	//遍历日期
	$.each(years, function(i, year){
		$.each(year.months, function(ii, month){
			$.each(month.days, function(iii,day){
				if(year.name==now.getFullYear()){
					if(day==1){
						$('#my_gante_table_right_head_year').append('<th colspan="'+getDaysOfMonth(year.name, month.name)+'">'+year.name+'年'+formatDate(month.name)+'月'+'</th>');
					}
					$('#my_gante_table_right_head_date').append('<th>'+formatDate(day)+'</th>');
				}
				else{
					if(day==1)
						$('#my_gante_table_right_head_year').append('<th style="display:none;" colspan="'+getDaysOfMonth(year.name, month.name)+'">'+year.name+'年'+formatDate(month.name)+'月'+'</th>');
					$('#my_gante_table_right_head_date').append('<th style="display:none;">'+formatDate(day)+'</th>');
				}
			});
		});
	});
	
	//格式化date(日)
	function formatDate(date){
		return date<10 ? ('0'+date) : date;
	}
	
	$('#my_gante_right_column').bind('scroll', function(){
		//滚动条滑动到最右端，显示下一年
		if($(this).scrollLeft()+$(this).width()+50 > 
				$('#my_gante_right_column')[0].scrollWidth){
			//获取已显示日期的最后一天的th
			var lastVisibleCell = $('#my_gante_table_right_head_date > th:visible:last')[0];
			var lastCell = parseInt(lastVisibleCell.cellIndex);
			//已显示年份的最后一年的Cell
			var yearLastVisibleCell = $('#my_gante_table_right_head_year > th:visible:last')[0];
			//已显示年份的最后一年
			var lastYear = parseInt(yearLastVisibleCell.innerText.substr(0, 4));
			//日的th显示
			//如果下一年是闰年
			if((lastYear+1)%4==0){
				for(var i=lastCell+1; i<=lastCell+366; i++){
					//日期列头th的显示
					$($('#my_gante_table_right_head_date > th')[i]).show();
				}
			}
			//下一年是平年
			else{
				for(var i=lastCell+1; i<=lastCell+365; i++){
					$($('#my_gante_table_right_head_date > th')[i]).show();
				}
			}
			//日的td显示
			$.each($('#my_gante_table_right_body > tr'), function(i, n){
				//如果下一年是闰年
				if((lastYear+1)%4==0){
					for(var i=lastCell+1; i<=lastCell+366; i++){
						$(n.children[i]).show();
					}
				}
				//如果下一年是平年
				else{
					for(var i=lastCell+1; i<=lastCell+365; i++){
						$(n.children[i]).show();
					}
				}
			});
			//年月的th显示
			$.each($('#my_gante_table_right_head_year > th'), function(i, n){
				if(n.cellIndex > yearLastVisibleCell.cellIndex && 
				   n.cellIndex <= (yearLastVisibleCell.cellIndex+12)){
					$(n).show();
				}
			});
		}
		//滚动条滑动到最左端，显示上一年
		else if($(this).scrollLeft() == 0){
			//获取已显示日期的第一天的th
			var firstVisibleCell = $('#my_gante_table_right_head_date > th:visible:first')[0];
			var firstCell = parseInt(firstVisibleCell.cellIndex);
			//已显示年份的最前一年的Cell
			var yearFirstVisibleCell = $('#my_gante_table_right_head_year > th:visible:first')[0];
			//已显示年份的最前一年
			var firstYear = parseInt(yearFirstVisibleCell.innerText.substr(0, 4));
			console.dir(firstYear);
			//日的th显示
			//如果前一年是闰年
			if((firstYear-1)%4==0){
				for(var i=firstCell-366; i<firstCell; i++){
					$($('#my_gante_table_right_head_date > th')[i]).show();
				}
			}
			//如果前一年是平年
			else{
				for(var i=firstVisibleCell.cellIndex-365; i<firstVisibleCell.cellIndex; i++){
					$($('#my_gante_table_right_head_date > th')[i]).show();
				}
			}
			//日的td显示
			$.each($('#my_gante_table_right_body > tr'), function(i, n){
				//如果是闰年
				if((firstYear-1)%4==0){
					for(var i=firstCell-366; i<firstCell; i++){
						$(n.children[i]).show();
					}
				}
				//如果是平年
				else{
					for(var i=firstCell-365; i<firstCell; i++){
						$(n.children[i]).show();
					}
				}
			});
			//年月的th显示
			$.each($('#my_gante_table_right_head_year > th'), function(i, n){
				if(n.cellIndex >= yearFirstVisibleCell.cellIndex-12 && 
				   n.cellIndex < yearFirstVisibleCell.cellIndex){
					$(n).show();
				}
			});
		}
	});
	
	//获取某年某月的天数
	function getDaysOfMonth(year, month){
	    var d = new Date(year, month, 0);
	    return d.getDate();
	}
	
	
	//发送ajax,查询工程信息,形成甘特图
	$.ajax({
		url:'task/getProjectById.do',
		type:'POST',
		data:{'projectId' : $('#project_gantt_projectId').val()},
		dataType:'json',
		success:function(result){
			var blocks = JSON.parse(result.blocks);
			$.each(blocks, function(i, n){
				var createTime = new Date(n.createTime);
				var millOfDay = 24*60*60*1000;
				var endTime = new Date(createTime.getTime()+(n.duration*millOfDay));
				//右边表格添加行
				var rowContent = '';
				$.each(years, function(i, year){
					$.each(year.months, function(ii, month){
						$.each(month.days, function(iii, day){
							var date = new Date(year.name + '-' + month.name + '-' + day);
							if(year.name==now.getFullYear()){
								if(date>=createTime && date<endTime)
									rowContent += '<td><div class="gante_block"></div></td>';
								else
									rowContent += '<td><div></div></td>';
							}
							else{
								if(date>=createTime && date<endTime)
									rowContent += '<td><div style="display:none;" class="gante_block"></div></td>';
								else
									rowContent += '<td style="display:none;"><div></div></td>';
							}
						});
					});
				});
				$('#my_gante_table_right_body').append('<tr>'+rowContent+'</tr>');
				
				//左边表格添加行
				var rowContent = '<td>' + n.description + '</td><td>' + n.leader.username + '-' + n.leader.name + '</td><td><div class="input-group my_gante_date_range"><div class="input-group-addon"><i class="fa fa-calendar"></i></div><input type="text" autoUpdateInput="false" value="' + date2Str(createTime) + ' - ' + date2Str(endTime) + '" class="form-control pull-right my_gante_reservation"></div></td>';
				$('#my_gante_table_left_body').append('<tr>'+rowContent+'</tr>');
				
			});
			
			//右边表格添加11-blocks.length行
			for(var i=0; i<11-blocks.length; i++){
				//$('#my_gante_table_right').append('<tr>');
				var rowContent = '';
				$.each(years, function(i, year){
					$.each(year.months, function(ii, month){
						$.each(month.days, function(iii, day){
							if(year.name==now.getFullYear()){
								rowContent += '<td><div></div></td>';
							}
							else{
								rowContent += '<td style="display:none;"><div></div></td>';
							}
						});
					});
				});
				$('#my_gante_table_right_body').append('<tr>'+rowContent+'</tr>');
			}
			//左边表格添加11-blocks.length行
			for(var i=0; i<11-blocks.length; i++){
				var rowContent = '<td></td><td></td><td><div class="input-group my_gante_date_range"><div class="input-group-addon"><i class="fa fa-calendar"></i></div><input type="text" autoUpdateInput="false" class="form-control pull-right my_gante_reservation"></div></td>';
				$('#my_gante_table_left_body').append('<tr>'+rowContent+'</tr>');
			}
			
			
			
			//如果左右表格都有行数据，再添加鼠标的经过事件
			if($('#my_gante_table_left_body > tr').length!=0 &&
					$('#my_gante_table_right_body > tr').length!=0){
				//左边表格添加移入、移出事件
				$.each($('#my_gante_table_left_body > tr'), function(i, n){
					$(n).mouseover(function(){
						var rowIndex = $(this)[0].rowIndex - 1;
						$(this)[0].style.borderBottom = 'solid 2px #00a65a';
						$('#my_gante_table_right_body > tr')[rowIndex].style.borderBottom = 'solid 2px #00a65a';
					});
					$(n).mouseout(function(){
						var rowIndex = $(this)[0].rowIndex - 1;
						$(this)[0].style.borderBottom = '';
						$('#my_gante_table_right_body > tr')[rowIndex].style.borderBottom = '';
					});
				});
				//右边表格添加移入、移出事件
				$.each($('#my_gante_table_right_body > tr'), function(i, n){
					$(n).mouseover(function(){
						var rowIndex = $(this)[0].rowIndex - 2;
						$(this)[0].style.borderBottom = 'solid 2px #00a65a';
						$('#my_gante_table_left_body > tr')[rowIndex].style.borderBottom = 'solid 2px #00a65a';
					});
					$(n).mouseout(function(){
						var rowIndex = $(this)[0].rowIndex - 2;
						$(this)[0].style.borderBottom = '';
						$('#my_gante_table_left_body > tr')[rowIndex].style.borderBottom = '';
					});
				});
			}
			
			
			//Date range picker的设置
			$.each($('.my_gante_reservation'), function(i, n){
				$(n).daterangepicker({
					dateLimit: {days: 300},
					autoUpdateInput: false,//false:设置默认值为空,但是带来一个问题:
										   //点击确定日期不显示,所以改了daterangepicker.js
										   //的源代码1406行添加了autoUpdateInput=true
					locale:{
						format: 'YYYY.MM.DD',//设置显示格式
						applyLabel: '确定',
						cancelLabel: '取消',
						daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
						monthNames: ['一月', '二月', '三月', '四月', '五月', '六月',
							                 '七月', '八月', '九月', '十月', '十一月', '十二月'
							        ],
					}
				});
			});
			
		}
	});
	
	/*//右边表格添加15行
	for(var i=0; i<11; i++){
		//$('#my_gante_table_right').append('<tr>');
		var rowContent = '';
		$.each(years, function(i, year){
			$.each(year.months, function(ii, month){
				$.each(month.days, function(iii, day){
					if(year.name==now.getFullYear()){
						if(year.name=='2019' &&
						   month.name=='4' &&
						   day=='13')
							rowContent += '<td><div class="gante_block"></div></td>';
						else
							rowContent += '<td><div></div></td>';
					}
					else{
						if(year.name=='2019' &&
						   month.name=='4' &&
						   day=='13')
							rowContent += '<td><div style="display:none;" class="gante_block"></div></td>';
						else
							rowContent += '<td style="display:none;"><div></div></td>';
					}
				});
			});
		});
		$('#my_gante_table_right_body').append('<tr>'+rowContent+'</tr>');
	}
	//左边表格添加15行
	for(var i=0; i<11; i++){
		var rowContent = '<td></td><td></td><td><div class="input-group my_gante_date_range"><div class="input-group-addon"><i class="fa fa-calendar"></i></div><input type="text" autoUpdateInput="false" class="form-control pull-right my_gante_reservation"></div></td>';
		$('#my_gante_table_left_body').append('<tr>'+rowContent+'</tr>');
	}*/
	
	
	/*//如果左右表格都有行数据，再添加鼠标的经过事件
	if($('#my_gante_table_left_body > tr').length!=0 &&
			$('#my_gante_table_right_body > tr').length!=0){
		//左边表格添加移入、移出事件
		$.each($('#my_gante_table_left_body > tr'), function(i, n){
			$(n).mouseover(function(){
				var rowIndex = $(this)[0].rowIndex - 1;
				$(this)[0].style.borderBottom = 'solid 2px #00a65a';
				$('#my_gante_table_right_body > tr')[rowIndex].style.borderBottom = 'solid 2px #00a65a';
			});
			$(n).mouseout(function(){
				var rowIndex = $(this)[0].rowIndex - 1;
				$(this)[0].style.borderBottom = '';
				$('#my_gante_table_right_body > tr')[rowIndex].style.borderBottom = '';
			});
		});
		//右边表格添加移入、移出事件
		$.each($('#my_gante_table_right_body > tr'), function(i, n){
			$(n).mouseover(function(){
				var rowIndex = $(this)[0].rowIndex - 2;
				$(this)[0].style.borderBottom = 'solid 2px #00a65a';
				$('#my_gante_table_left_body > tr')[rowIndex].style.borderBottom = 'solid 2px #00a65a';
			});
			$(n).mouseout(function(){
				var rowIndex = $(this)[0].rowIndex - 2;
				$(this)[0].style.borderBottom = '';
				$('#my_gante_table_left_body > tr')[rowIndex].style.borderBottom = '';
			});
		});
	}*/
	
	
	
	
	function date2Str(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		return year + '.' + (month<10?('0'+month):month) + '.' + (day<10?('0'+day):day);
	}
	
});
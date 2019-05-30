$(function(){
	
	//获取本周周一的日期
	var now = new Date();//今天的日期
	var toDay = now.getDay();//今天星期几
	var days = toDay - 1;//今天和本周一差几天
	var millSecondOfDay = 24*60*60*1000;//一天的毫秒数
	var mondayOfWeek = new Date(now.getTime()-(days*millSecondOfDay));//本周一的日期
	var mondayOfWeekF = dateFormat(mondayOfWeek);//格式化后的本周一日期 格式为yyyy-MM-dd
	
	/**
	 * 日期格式化成 yyyy-MM-dd
	 */
	function dateFormat(date){
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var d = date.getDate();
		return year + '-' + (month>9?month:('0'+month)) + '-' + (d>9?d:('0'+d));
	}
	
	//根据开始日期和工期，获取结束日期
	function getEndDate(createTime, duration){
		var createTimeD = new Date(createTime);
		var millSecondOfDay = 24*60*60*1000;//一天的毫秒数
		return new Date(createTimeD.getTime() + millSecondOfDay * duration);
	}
	
	//毫秒转化成yyyy-MM-dd的日期
	function millisecond2Date(milliSecond){
		var date = new Date(milliSecond);
		return date.getFullYear() + '-' + ((date.getMonth()+1)>9 ? (date.getMonth()+1) : '0' + (date.getMonth()+1))
			   + '-' + (date.getDate()>9 ? date.getDate() : '0' + date.getDate() );
	}
	
	/**
	 * 获取企业任务(工程小块)周报
	 */
	$.ajax({
		url:'statistics/getEnterpriseBlockWeekly.do',
		type:'POST',
		data:{'mondayDate' : mondayOfWeekF},
		dataType:'json',
		success: function(result){
			//处理json返回数据中有$ref
			result = FastJson.format(result);
			
			var completeBlocks = result.completeBlocks;
			var delayBlocks = result.delayBlocks;
			var newBlocks = result.newBlocks;
			//alert(completeBlocks.length + ' ' + delayBlocks.length + ' ' + newBlocks.length);
			$('#block_weekly_delayBlocksCount').text(delayBlocks.length);
			$('#block_weekly_completeBlocksCount').text(completeBlocks.length);
			$('#block_weekly_newBlocksCount').text(newBlocks.length);
			
			//本周企业完成任务插入进ToDoList
			$.each(completeBlocks, function(i, n){
				var realEndDate = dateFormat(getEndDate(millisecond2Date(n.createTime), n.realDuration));
				var html = '<li><span class="handle"><i class="fa fa-rocket"></i></span><span class="text">'+n.description+'</span><small class="label label-success"><i class="fa fa-clock-o"></i>'+realEndDate+' 完成</small></li>';
				$('#block_weekly_completeToDoList').append(html);
			});
			//本周企业延期任务插入ToDoList
			$.each(delayBlocks, function(i, n){
				var endDate = dateFormat(getEndDate(millisecond2Date(n.createTime), n.duration));
				var html = '<li><span class="handle"><i class="fa fa-rocket"></i></span><span class="text">'+n.description+'</span><small class="label label-danger"><i class="fa fa-clock-o"></i>'+endDate+' 截止</small></li>';
				$('#block_weekly_delayToDoList').append(html);
			});
			//本周企业新增任务插入ToDoList
			$.each(newBlocks, function(i, n){
				var html = '<li><span class="handle"><i class="fa fa-rocket"></i></span><span class="text">'+n.description+'</span><small class="label label-warning"><i class="fa fa-clock-o"></i>'+millisecond2Date(n.createTime)+' 创建</small></li>';
				$('#block_weekly_newToDoList').append(html);
			});
		},
		error:function(){
			//没有权限
			$("#index_main_content").load("/GeneralProject/page/unauthorized.html");
			$("#index_main_content").css('padding','');
		}
	});
	
	
	//处理json返回数据中有$ref
	var FastJson = {
			        isArray : function(a) {
			            return "object" == typeof a
			                    && "[object array]" == Object.prototype.toString.call(a)
			                            .toLowerCase();
			        },
			        isObject : function(a) {
			            return "object" == typeof a
			                    && "[object object]" == Object.prototype.toString.call(a)
			                            .toLowerCase();
			        },
			        format : function(a) {
			            if (null == a)
			                return null;
			            "string" == typeof a && (a = eval("(" + a + ")"));
			            return this._format(a, a, null, null, null);
			        },
			        _randomId : function() {
			            return "randomId_" + parseInt(1E9 * Math.random());
			        },
			        _getJsonValue : function(a, c) {
			            var d = this._randomId(), b;
			            b = "" + ("function " + d + "(root){") + ("return root." + c + ";");
			            b += "}";
			            b += "";
			            var e = document.createElement("script");
			            e.id = d;
			            e.text = b;
			            document.body.appendChild(e);
			            d = window[d](a);
			            e.parentNode.removeChild(e);
			            return d;
			        },
			        _format : function(a, c, d, b, e) {
			            d || (d = "");
			            if (this.isObject(c)) {
			                if (c.$ref) {
			                    var g = c.$ref;
			                    0 == g.indexOf("$.")
			                            && (b[e] = this._getJsonValue(a, g.substring(2)));
			                    return
			                }
			                for ( var f in c)
			                    b = d, "" != b && (b += "."), g = c[f], b += f, this
			                            ._format(a, g, b, c, f);
			            } else if (this.isArray(c))
			                for (f in c)
			                    b = d, g = c[f], b = b + "[" + f + "]", this._format(a, g,
			                            b, c, f);
			            return a;
			        }
			    };
		
	
});
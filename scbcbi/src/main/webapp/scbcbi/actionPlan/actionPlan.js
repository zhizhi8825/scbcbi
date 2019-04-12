document.write('<script src="/scbcbi/actionPlan/aaa.js"><\/script>');
 
$(function(){
	var $grid = $("#grid");
	var columns;
	var now = new Date();
	var planMouseDown = false;  //记录是否在计划的cell那里按下了鼠标
	var firstDownPlanField;  //记录第一个点下的计划field名
	var firstDownPlanName;  //记录第一个点下的计划Div的名字
	
	//设置范围输入框的格式
	var startShowMonth = $("#start_v_显示范围");
	if(startShowMonth.length>0) {
		startShowMonth[0].onfocus = function(){
			var end=$dp.$('end_v_显示范围');
			WdatePicker({
				onpicked:function(){
					end.focus();
					//重新加载表格头
					var startYearMonth = startShowMonth.val();
					var endYearMonth = endShowMonth.val();
					if(startYearMonth && endYearMonth) {
						initHead($grid,startYearMonth,endYearMonth);
					}
				},
				dateFmt:'yyyy-MM'
			});
		};
		//时间设置为今年1月
		startShowMonth.val(now.getFullYear()+"-01");
	}
	var endShowMonth = $("#end_v_显示范围");
	if(endShowMonth.length>0) {
		endShowMonth[0].onfocus = function(){
			WdatePicker({
				minDate:'#F{$dp.$D(\'start_v_显示范围\')}',
				dateFmt:'yyyy-MM',
				onpicked:function(){
					//重新加载表格头
					var startYearMonth = startShowMonth.val();
					var endYearMonth = endShowMonth.val();
					if(startYearMonth && endYearMonth) {
						initHead($grid,startYearMonth,endYearMonth);
					}
				}
			})
		};
		//时间设置为今年12月
		endShowMonth.val(now.getFullYear()+"-12");
	}
	
	//在列表界面的时候才会有datagrid，所以要在这里加判断，不然在编辑窗口会报错
	if($grid && $grid.length>0) {
		columns = $grid.datagrid("options").columns; //把原始列信息记录下来
		var startYearMonth = startShowMonth.val();
		var endYearMonth = endShowMonth.val();
		initHead($grid,startYearMonth,endYearMonth);
		
		//$.data(grid, 'datagrid').options.onSelect=function(){alert(111)};
		$grid.datagrid({
			onLoadSuccess:function(data){
				//设置计划的cell显示,并给他们加事件
				$(".planCellCls").parents("td").mousedown(mousedownPlanTd);
				$(".planCellCls").parents("td").mousemove(mousemovePlanTd);
			}
		});
	}
	
	
	//初始化表头显示
	function initHead ($grid,startYearMonth,endYearMonth) {
		
		//处理年月的列信息
		var startYear = eval(startYearMonth.substr(0,4));
		var startMonth = eval(startYearMonth.substr(5));
		var endYear = eval(endYearMonth.substr(0,4));
		var endMonth = eval(endYearMonth.substr(5));
		//循环年月，处理出计划的第二、三行头信息
		var secondPlanHead = [];
		var thirdPlanHead = [];
		var startMonthIndex = startMonth; //开始的时候以开始月起
		var endMonthIndex = 12;
		var jiDu;
		for(var i=startYear;i<=endYear;i++) {
			if(i==endYear) {
				//结束的时候以结束月止
				endMonthIndex = endMonth;
			}
			for(var j=startMonthIndex;j<=endMonthIndex;j++) {
				thirdPlanHead.push({title:j+"月",field:i+""+(j>=10?j:"0"+j),width:30,formatter:planFormatter});
				if(j>=1 && j<=3) {
					jiDu = 1;
				} else if(j>=4 && j<=6){
					jiDu = 2;
				} else if (j>=7 && j<=9) {
					jiDu = 3;
				} else if (j>=10 && j<=12) {
					jiDu = 4;
				}
				
				//是1、4、7、10月的时候，就新建一个季度
				if(secondPlanHead.length==0){
					secondPlanHead.push({title:i+"-Q"+jiDu,colspan:1});
				} else if(j==1 || j==4 || j==7 || j==10){
					//细节，如果最后一个季度只有一个月，那就设置下这个月的列的宽度，以免出现数据的列对不齐
					if(secondPlanHead[secondPlanHead.length-1].colspan==1) {
						thirdPlanHead[thirdPlanHead.length-1].width=60;
					}
					
					secondPlanHead.push({title:i+"-Q"+jiDu,colspan:1});
				} else {
					//否则的话，就把最后一个季度加一个合并列
					secondPlanHead[secondPlanHead.length-1].colspan++;
				}
			}
			
			startMonthIndex=1;
		}
		
		//细节，如果最后一个季度只有一个月，那就设置下这个月的列的宽度，以免出现数据的列对不齐
		if(secondPlanHead[secondPlanHead.length-1].colspan==1) {
			thirdPlanHead[thirdPlanHead.length-1].width=60;
		}
		
		//处理列信息，组装成合并的样式
		//第一行
		var firstRowHead = [{title:"IT行动项",colspan:6},{title:"计划",colspan:thirdPlanHead.length},{title:"实际",colspan:9},{title:" ",colspan:2}];
		
		//把原始列拆成两部分，分别为IT行动项和实际
		var itActionColumns = columns[0].slice(0,6); //取出前面的6列
		var realityColumns = columns[0].slice(6); //取出第6列后面的所有列
		//循环下这两个集合，设置它们rowspan都为2
		for(var i=0;i<itActionColumns.length;i++) {
			itActionColumns[i].rowspan = 2;
		}
		for(var i=0;i<realityColumns.length;i++) {
			realityColumns[i].rowspan = 2;
		}
		
		//第二行
		var secondRowHead = [];
		secondRowHead = secondRowHead.concat(itActionColumns,secondPlanHead,realityColumns); //目前就只有计划一行
		
		//第三行
		var thirdRowHead = [];
		thirdRowHead = thirdRowHead.concat(thirdPlanHead);
		
		//把上面处理出来的三行组装到一起
		var columnsTemp = [firstRowHead,secondRowHead,thirdRowHead];
		
		$grid.datagrid({
			columns:columnsTemp
		}); 
		$grid.datagrid("reload");
	}
	
	//计划的格式化方法
	function planFormatter (value,row,index) {
		//给cell加div
		var html = "<div name='planCellName"+index+"' class='planCellCls'>&nbsp;</div>";
		return html;
	}
	
	//点击计划td的事件方法
	function mousedownPlanTd () {
		planMouseDown = true;
		firstDownPlanField = $(this).attr("field");
		firstDownPlanName = $(this).find(".planCellCls").attr("name"); 
	}
	
	//鼠标在计划td上移动的事件方法
	function mousemovePlanTd () {
		if(planMouseDown) {
			var nowPlanName = $(this).find(".planCellCls").attr("name"); 
			if(nowPlanName == firstDownPlanName) {
				//$(this).css("background-color","red");
				$(this).addClass("actionPlanSelected");
			}
		}
		console.log(planMouseDown);
	}
	
	//清除计划td的样式
	function clearPlanTdCss () {
		
	}
	
	//鼠标松开的时候
	$(document).mouseup(function(e){
		planMouseDown = false;
	});
	
});
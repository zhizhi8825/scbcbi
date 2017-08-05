/**
 * 传一个对象数组来，根据key取对象中的字段组成数组返回,如传objects=[{"id":1},{"id":2}],key="id"， 处理结果为[1,2]
 * 
 * @param objects
 * @param key
 * @returns {Array}
 */
function getArrayFromObjects(objects, key) {
	var result = [];
	for ( var i = 0; i < objects.length; i++) {
		var value = objects[i][key];
		if (value) {
			result.push(value);
		}
	}
	return result;
}

/**
 * 传一个对象集合来，处理成url中参数的格式，如传objects=[{"id":1},{"id":2}],key="id"， 处理结果为
 * &id=1&id=2
 * 
 * @param objects
 * @param key
 * @param name
 *            传到后台去的时候的名字，如果没设置就默认用key
 * @returns {String}
 */
function getUrlDataFromObjects(objects, key, name) {
	var result = "";
	for ( var i = 0; i < objects.length; i++) {
		var value = objects[i][key];
		if (value) {
			if (name) {
				result += "&" + name + "=" + value;
			} else {
				result += "&" + key + "=" + value;
			}
		}
	}
	return result;
}

/**
 * 把一个对象转成URL的格式
 * @param object
 * @returns {String}
 */
function getUrlDataFromObject(object) {
	var result = "";
	for(var key in object) {
		result += "&" + key + "=" + object[key];
	}
	
	return result;
}

/**
 * 给对象的每一个key前面加指定的字符串，如obj={"id",1,"name":"ken"},str="user."，返回结果为
 * {"user.id":1,"user.name":"ken"}
 * 
 * @param obj
 * @param str
 * @returns {___anonymous767_768}
 */
function addStrToBeforeKey(obj, str) {
	var result = {};

	if (obj) {
		for ( var key in obj) {
			result[str + key] = obj[key];
		}
	}

	return result;
}

/**
 * 处理数据成逗号分隔的字符串 [a,b,c] 成 a,b,c
 * @param array
 * @returns {String}
 */
function arrayToStr(array) {
	var str = "";
	for(var i=0;i<array.length;i++) {
		if(i==0) {
			str=array[i];
		} else {
			str += ","+array[i];
		}
	}
	return str;
}

/**
 * 小写数字转大写
 */
function numberLowerToUpper(dValue, maxDec) {
	// 验证输入金额数值或数值字符串：
	dValue = dValue.toString().replace(/,/g, "");
	dValue = dValue.replace(/^0+/, ""); // 金额数值转字符、移除逗号、移除前导零
	if (dValue == "") {
		return "零元整";
	} // （错误：金额为空！）
	else if (isNaN(dValue)) {
		return "错误：金额不是合法的数值！";
	}

	var minus = ""; // 负数的符号“-”的大写：“负”字。可自定义字符，如“（负）”。
	var CN_SYMBOL = ""; // 币种名称（如“人民币”，默认空）
	if (dValue.length > 1) {
		if (dValue.indexOf('-') == 0) {
			dValue = dValue.replace("-", "");
			minus = "（负数）";
		} // 处理负数符号“-”
		if (dValue.indexOf('+') == 0) {
			dValue = dValue.replace("+", "");
		} // 处理前导正数符号“+”（无实际意义）
	}

	// 变量定义：
	var vInt = "";
	var vDec = ""; // 字符串：金额的整数部分、小数部分
	var resAIW; // 字符串：要输出的结果
	var parts; // 数组（整数部分.小数部分），length=1时则仅为整数。
	var digits, radices, bigRadices, decimals; // 数组：数字（0~9——零~玖）；基（十进制记数系统中每个数字位的基是10——拾,佰,仟）；大基（万,亿,兆,京,垓,杼,穰,沟,涧,正）；辅币
												// （元以下，角/分/厘/毫/丝）。
	var zeroCount; // 零计数
	var i, p, d; // 循环因子；前一位数字；当前位数字。
	var quotient, modulus; // 整数部分计算用：商数、模数。

	// 金额数值转换为字符，分割整数部分和小数部分：整数、小数分开来搞（小数部分有可能四舍五入后对整数部分有进位）。
	var NoneDecLen = (typeof (maxDec) == "undefined" || maxDec == null
			|| Number(maxDec) < 0 || Number(maxDec) > 5); // 是否未指定有效小数位（true/false）
	parts = dValue.split('.'); // 数组赋值：（整数部分.小数部分），Array的length=1则仅为整数。
	if (parts.length > 1) {
		vInt = parts[0];
		vDec = parts[1]; // 变量赋值：金额的整数部分、小数部分

		if (NoneDecLen) {
			maxDec = vDec.length > 5 ? 5 : vDec.length;
		} // 未指定有效小数位参数值时，自动取实际小数位长但不超5。
		var rDec = Number("0." + vDec);
		rDec *= Math.pow(10, maxDec);
		rDec = Math.round(Math.abs(rDec));
		rDec /= Math.pow(10, maxDec); // 小数四舍五入
		var aIntDec = rDec.toString().split('.');
		if (Number(aIntDec[0]) == 1) {
			vInt = (Number(vInt) + 1).toString();
		} // 小数部分四舍五入后有可能向整数部分的个位进位（值1）
		if (aIntDec.length > 1) {
			vDec = aIntDec[1];
		} else {
			vDec = "";
		}
	} else {
		vInt = dValue;
		vDec = "";
		if (NoneDecLen) {
			maxDec = 0;
		}
	}
	if (vInt.length > 44) {
		return "错误：金额值太大了！整数位长【" + vInt.length.toString()
				+ "】超过了上限——44位/千正/10^43（注：1正=1万涧=1亿亿亿亿亿，10^40）！";
	}

	// 准备各字符数组 Prepare the characters corresponding to the digits:
	digits = new Array("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"); // 零~玖
	radices = new Array("", "拾", "佰", "仟"); // 拾,佰,仟
	bigRadices = new Array("", "万", "亿", "兆", "京", "垓", "杼", "穰", "沟", "涧", "正"); // 万,亿,兆,京,垓,杼,穰,沟,涧,正
	decimals = new Array("角", "分", "厘", "毫", "丝"); // 角/分/厘/毫/丝

	resAIW = ""; // 开始处理

	// 处理整数部分（如果有）
	if (Number(vInt) > 0) {
		zeroCount = 0;
		for (i = 0; i < vInt.length; i++) {
			p = vInt.length - i - 1;
			d = vInt.substr(i, 1);
			quotient = p / 4;
			modulus = p % 4;
			if (d == "0") {
				zeroCount++;
			} else {
				if (zeroCount > 0) {
					resAIW += digits[0];
				}
				zeroCount = 0;
				resAIW += digits[Number(d)] + radices[modulus];
			}
			if (modulus == 0 && zeroCount < 4) {
				resAIW += bigRadices[quotient];
			}
		}
		resAIW += "元";
	}

	// 处理小数部分（如果有）
	for (i = 0; i < vDec.length; i++) {
		d = vDec.substr(i, 1);
		if (d != "0") {
			resAIW += digits[Number(d)] + decimals[i];
		}
	}

	// 处理结果
	if (resAIW == "") {
		resAIW = "零" + "元";
	} // 零元
	if (vDec == "") {
		resAIW += "整";
	} // ...元整
	resAIW = CN_SYMBOL + minus + resAIW; // 人民币/负......元角分/整
	return resAIW;
};

//将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
//fmt默认为 yyyy-MM-dd 格式
//例子：
//dateFormat(new Date(),'yyyy-MM-dd') ==> 2006-07-02
//(new Date()).format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
//author: huangth
function dateFormat (date,fmt) {
	if(!fmt){
		fmt = "yyyy-MM-dd";
	}
	
	var o = {
			"M+" : date.getMonth() + 1, // 月份
			"d+" : date.getDate(), // 日
			"h+" : date.getHours(), // 小时
			"m+" : date.getMinutes(), // 分
			"s+" : date.getSeconds(), // 秒
			"q+" : Math.floor((date.getMonth() + 3) / 3), // 季度
			"S" : date.getMilliseconds()
		// 毫秒
		};
		if (/(y+)/.test(fmt))
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(fmt))
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
						: (("00" + o[k]).substr(("" + o[k]).length)));
		return fmt;
};

/**计算字符数**/
function countCharacters(str){
    var totalCount = 0;  
    if(!str){
	    for (var i=0; i<str.length; i++) {  
	        var c = str.charCodeAt(i);  
	    if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {  
	           totalCount ++;  
	        }else {     
	           totalCount += 2;  
	        }  
	    }
    }
    return totalCount;
}

/**
 * 格式化对象，指定位数，当不够时就在前面补指定的字符
 */
function fillUp (obj,length,replaceChar){
	var result = "";
	
	if(!length){
		length = 8;
	}
	
	if(!replaceChar){
		replaceChar = '0';
	}
	
	if(obj){
		var str = obj.toString();
		for(var i=length - str.length;i>0;i--){
			result += replaceChar;
		}
		result += str;
	} else {
		return obj;
	}
	
	return result;
}

/**
 * 保留两位小数，不够的后面补零
 * @param val
 * @returns {String}
 */
function keepFloat(val){
	var result = "";
	if( val != undefined && val != ""){
		var str = val.toString();
		var strs = str.split(".");
		
		if(strs.length<2){
			result = str + ".00";
		} else {
			if(strs[1].length<2){
				result = str + "0";
			} else {
				result = str;
			}
		}
	}
	
	return result;
}

function format2OR5(v){
	if(!v){
		return 0;
	}
	var s = v.toString();
	var idx = s.indexOf(".");
	if(idx != -1){
		var y = s.substr(idx+1);
		if(y.length > 2){
			return parseFloat(s).toFixed(5);
		}else{
			return parseFloat(s).toFixed(2);			
		}
	}else{
		return parseFloat(s).toFixed(2);
	}
}

/**
 * 验证单选
 */
function oneSelectValid(records){
	if(records.length<1){
		$.messager.alert("提示","请选择要操作的项");
		return false;
	}else if(records.length>1){
		$.messager.alert("提示","该操作只能为单选");
		return false;
	}else{
		return true;
	}
}

/**
 * 验证多选
 */
function selectValid(records){
	if(records.length>0){
		return true;
	}else{
		$.messager.alert("提示","请选择要操作的项");
		return false;
	}
}

/**
 * 验证全选
 */
function selectOneOrAllValid(selectRows, allRows){
	if(selectRows.length != 1 && selectRows.length < allRows.length){
		$.messager.alert("提示","请单选或全选要操作的项");
		return false;
	}else{
		return true;
	}
}

/**
 * 把对象中值为null的都删掉
 * @param obj
 */
function objectRemoveNull (obj) {
	var result = {};
	
	if(obj){
		for ( var key in obj) {
			if(obj[key] != null) {
				result[key] = obj[key];
			}
		}
	}
	
	return result;
}

function showToast(msg){
	$.messager.show({
		msg:msg,
		timeout:1000,
		showType:"fade",
		style:{
			"text-align":"center",
			"vertical-align":"middle",
			"font-size":"large"
		}
	});
}

//显示遮罩层
function showLoading() {
	if($(".myLoaderDiv").length == 0) {
		var html = "";
		html += "<div class=\"myLoaderDiv\">";
		html += "	<div style=\"width:100%;height:100%;position: absolute;top:0px; left:0px; background-color:#0099CC; z-index:9998; ";
		html += "	filter:alpha(opacity=30);opacity:0.3; -moz-opacity:0.3;\"></div>";
		html += "	<div style=\"position: absolute;bottom:0px;right:0px;width:55%;height:50%;z-index:9999;\"><img src='/synFileClient/images/main/loading.gif'></img></div>";
		html += "	</div>";
		$("body").append(html);
	}
}

//隐藏遮罩层
function hideLoading() {
	if($(".myLoaderDiv").length > 0) {
		$(".myLoaderDiv").remove();
	}
}

//把对象里的每一个数值都处理成三位加一个逗号的形式
function addObjectCommaToNumber(object) {
	var val;
	for(var key in object) {
		if($.isNumeric(object[key])){
			val = object[key]+"";
			val = val.split('').reverse().join('').replace(/(\d{3})/g,'$1,').replace(/\,$/,'').replace(/\,-$/,'-').split('').reverse().join('');
			object[key] = val;
		}
	}
}

/*
* grid 要进行操作的datagrid
*
* 总的合并的信息
* colMsg[{
*	colName : 要进行合并的列名
*	followCol : 在colName合并的前提下进行合并的列信息对象数组
*	isCount : 合并的时候是否以行数做为显示值
*	isSum : 合并时是否计算所有要合并的数值总和做为显示值
*	subMerge : 是否在已指定的主列合并的前提下进行自己更细的合并
*	notMerge : true为做isCount或isSum的计算，false为只做合并
* }]
*
* 指定合并的信息
* appointOpiont {
*	startRow 开始行索引
*	endRow 结束行索引
* }
*/
function mergeCells(grid,colMsg,appointOpiont){
	var rows = grid.datagrid("getRows");
	var startRow = 0;   //处理合并操作的范围
	var endRow = rows.length-1;

	if(colMsg.length>0 && rows.length>0){
		//循环要合并的列信息
		for(var colMsgIndex=0; colMsgIndex<colMsg.length; colMsgIndex++){
			var colName = colMsg[colMsgIndex].colName;

			//如果传来有指定的合并信息，就设置一下指定的信息
			if(appointOpiont){
				startRow = appointOpiont.startRow;
				endRow = appointOpiont.endRow;
			}

			var startMerge = startRow;
			var endMerge = endRow;

			if(colMsg[colMsgIndex].subMerge){
				endMerge = startRow;
			}
			
			if(colMsg[colMsgIndex].subMerge){  //需要在目前的这个startRow,endRow里进行细细的合并
				var colValue = rows[startRow][colName];
				for(var rowIndex=startRow; rowIndex<endRow+1 && rowIndex<rows.length; rowIndex++){
					if(rows[rowIndex][colName]!=colValue || rowIndex==endRow){
						if(rows[rowIndex][colName]==colValue && rowIndex==endRow){
							endMerge = rowIndex;
						}

						if(colMsg[colMsgIndex].notMerge){
							if(colMsg[colMsgIndex].isCount){
								for(var i=startMerge;i<=endMerge;i++){
									rows[i][colName] = endMerge-startMerge+1;
									grid.datagrid("refreshRow",i);
								}
								if(rows[rowIndex][colName]!=colValue && rowIndex==endRow){
									rows[rowIndex][colName] = 1;
									grid.datagrid("refreshRow",rowIndex);
								}
							}else if(colMsg[colMsgIndex].isSum){
								var sum = 0;
								for(var i=startMerge;i<=endMerge;i++){
									var shu = rows[i][colName];
										if(!shu){
										shu = 0;
									}
									sum += eval(shu);
								}

								for(var i=startMerge;i<=endMerge;i++){
									rows[i][colName] = sum;
									grid.datagrid("refreshRow",i);
								}
							}else if(colMsg[colMsgIndex].skuCount){
								for(var i=startMerge;i<=endMerge;i++){
									rows[i][colName] = rows[i]["qty"]*rows[i]["orderCount"];
									grid.datagrid("refreshRow",i);
								}orderCount
							}else if(colMsg[colMsgIndex].orderCount){
								var orderCount = 1;
								var orderNo = rows[startMerge]["aux1"];
								for(var i=startMerge;i<=endMerge;i++){
									if(rows[i]["aux1"] != orderNo){
										orderCount++;
									}
								}
								
								for(var i=startMerge;i<=endMerge;i++){
									rows[i][colName] = orderCount;
									grid.datagrid("refreshRow",i);
								}
							}
						}
						

						if(colMsg[colMsgIndex].followCol && colMsg[colMsgIndex].followCol.length>0){
							mergeCells(grid,colMsg[colMsgIndex].followCol,{"startRow":startMerge,"endRow":endMerge});
							if(rows[rowIndex][colName]!=colValue && rowIndex==endRow){
								mergeCells(grid,colMsg[colMsgIndex].followCol,{"startRow":rowIndex,"endRow":rowIndex});
							}
						}
						
						grid.datagrid("mergeCells",{
							index:startMerge,
							field:colName,
							rowspan:endMerge-startMerge+1,
							colspan:0
						});
						
						startMerge=rowIndex;
						colValue=rows[rowIndex][colName];
					}

					endMerge = rowIndex;
				}
			}else{   //根据传来的startRow,endRow直接合并
				if(colMsg[colMsgIndex].notMerge){
					if(colMsg[colMsgIndex].isCount){
						for(var i=startMerge;i<=endMerge;i++){
							rows[i][colName] = endMerge-startMerge+1;
							grid.datagrid("refreshRow",i);
						}
					}else if(colMsg[colMsgIndex].isSum){
						var sum = 0;
						for(var i=startMerge;i<=endMerge;i++){
							var shu = rows[i][colName];
								if(!shu){
								shu = 0;
							}
							sum += eval(shu);
						}

						for(var i=startMerge;i<=endMerge;i++){
							rows[i][colName] = sum;
							grid.datagrid("refreshRow",i); 
						}
					}else if(colMsg[colMsgIndex].skuCount){
						for(var i=startMerge;i<=endMerge;i++){
							rows[i][colName] = rows[i]["qty"]*rows[i]["orderCount"];
							grid.datagrid("refreshRow",i);
						}
					}else if(colMsg[colMsgIndex].orderCount){
						var orderCount = 1;
						var orderNo = rows[startMerge]["aux1"];
						for(var i=startMerge;i<=endMerge;i++){
							if(rows[i]["aux1"] != orderNo){
								orderCount++;
							}
						}
						
						for(var i=startMerge;i<=endMerge;i++){
							rows[i][colName] = orderCount;
							grid.datagrid("refreshRow",i);
						}
					}
				}
				

				if(colMsg[colMsgIndex].followCol && colMsg[colMsgIndex].followCol.length>0){
					mergeCells(grid,colMsg[colMsgIndex].followCol,{"startRow":startMerge,"endRow":endMerge});
				}
				
				grid.datagrid("mergeCells",{
					index:startMerge,
					field:colName,
					rowspan:endMerge-startMerge+1,
					colspan:0
				});
			}
		}
	}
}

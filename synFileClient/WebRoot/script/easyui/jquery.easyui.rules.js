(function($){
	$.extend($.fn.validatebox.defaults.rules,{
		limit : {
			validator : function(value,param){
				return value.length >= param[0] && value.length <= param[1];
			},
			message : "请输入 {0}-{1} 个字符."
		},
		compare : {
			validator : function(value,param){
				//示例：<input type="text" class="easyui-validatebox" name="password2" validType="compare['password']" data-options="required:true"/>
				//注意传参的方式是用中括号[]，参数是其它元素的ID
				var v1 = $("#"+param[0]).val();
				return value == v1;
			},
			message : "两次输入的值不同"
		},
		maxLength : {
			validator : function(value,param){
				return value.length <= param[0];
			},
			message : "不能超过 {0} 个字符"
		},
		minLength : {
			validator : function(value,param){
				return value.length >= param[0];
			},
			message : "不能少于 {0} 个字符"
		},
		fixedLength:{
			validator : function(value,param){
				return value.length == param[0];
			},
			message : "输入数字长度必须为{0}"
			
		},
		onlyNumber : {
			validator : function(value,param){
				return /^[0-9]*$/.test(value);
			},
			message : "只能输入数字"
		},
		telephone : {
			validator : function(value,param){
				return /(^[0-9]{3,4}\-[0-9]{7,8}$)|(^[0-9]{11}$)/.test(value);
			},
			message : "格式:区号-号码 或 手机号"
		},
		onlyFloat : {
			validator : function(value,param){
				return /^\-?\d*(\.?\d+)?$|^0$/.test(value);
			},
			message : "请输入正确的数据"
		},
		roundLength : {
			validator : function(value,param){
				var f = /^(\w{15})$|^(\w{17})$|^(\w{18})$|^(\w{20})$/.test(value);
				return f;
			},
			message : "税号只能由15、17、18、20位数字或字母组成" 
		}
	});
})(jQuery);
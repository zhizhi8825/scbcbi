/**
 * 扩展easyui一些控件的方法或事件
 */
(function(){
	
	/********** form **********/
	$.extend($.fn.form.methods, {
		setData : function(jq, data) {
			return jq.each(function() {
				var $form = $(this);
				$form.data("data", data);
				$form.find("input[name], textarea[name],span[name]").each(function() {
					var $input = $(this);
					var name = $input.attr("name");
					var value = data[name] != undefined && data[name] != null ? data[name] : "";
					if ($input.hasClass("combo-value")) {
						$numberInput= $input.parent().parent().find("input[numberboxname='" + name + "']");
						$input = $input.parent().parent().find("input[comboname='" + name + "'],select[comboname='" + name + "']");
						if ($input.hasClass("easyui-combobox") || $input.hasClass("combobox-f") || $input.hasClass("textbox-value")) {
							$input.combobox("setValue", value);
						} else if ($input.hasClass("easyui-combogrid") || $input.hasClass("combogrid-f")) {
							$input.combogrid("setValue", value);
						} else if ($input.hasClass("easyui-datebox")) {
							$input.datebox("setValue", value);
						} else if ($input.hasClass("easyui-datetimebox")) {
							$input.datetimebox("setValue", value);
						} else if ($input.hasClass("easyui-combotree") || $input.hasClass("combotree-f")) {
							$input.combotree("setValue", value);
						} else if ($numberInput.hasClass("easyui-numberbox") || $numberInput.hasClass("numberbox-f")) {
							$numberInput.numberbox("setValue", value);
						} else {
							//$.messager.alert("Not supported combo type ", $input.attr("class"), "warning");
						}
					} else if ($input.hasClass("textbox-value")) {
						$numberInput= $input.parent().parent().find("input[numberboxname='" + name + "']");
						$input = $input.parent().parent().find("input[comboname='" + name + "'],select[comboname='" + name + "']");
						if ($input.hasClass("easyui-combobox") || $input.hasClass("combobox-f")) {
							$input.combobox("setValue", value);
						} else if ($input.hasClass("easyui-combogrid") || $input.hasClass("combogrid-f")) {
							$input.combogrid("setValue", value);
						} else if ($input.hasClass("easyui-datebox")) {
							$input.datebox("setValue", value);
						} else if ($input.hasClass("easyui-datetimebox")) {
							$input.datetimebox("setValue", value);
						} else if ($input.hasClass("easyui-combotree") || $input.hasClass("combotree-f")) {
							$input.combotree("setValue", value);
						} else if ($numberInput.hasClass("easyui-numberbox") || $numberInput.hasClass("numberbox-f")) {
							$numberInput.numberbox("setValue", value);
						} else {
							//$.messager.alert("Not supported combo type ", $input.attr("class"), "warning");
						}
					} else if ($input.attr("type") == "checkbox") {
						if(value && value == 1){
							$input.prop("checked","checked");
						} else {
							$input.prop("checked",null);
						}
					} else if (this.nodeName == "SPAN") {
						$input.html(value);
					} else {
						this.value = value;
					}
				});
			});
		},
		
		getData : function(jq) {
			var $form = $(jq[0]);
			var data = $.extend({}, $form.data("data"));
			$form.find("input[name], textarea[name],span[name]").each(function() {
				var $input = $(this);
				var name = $input.attr("name");
				var value = this.value;
				if (value == "") {
					value = null;
				}
				
				//如果是checkbox控件
				if($input.attr("type") == "checkbox"){
					if($input.prop("checked")){
						data[name] = 1;
					}else{
						data[name] = 0;
					}
				} else if($input.attr("type") == "radio"){
					if($input.prop("checked")){
						data[name] = $input.val();
					}
				} else if (this.nodeName == "SPAN") {
					data[name] = $input.html();
				}  else{
					data[name] = value;
				}
			});
			return data;
		},
		
		disableAssembly : function(jq, disable) {
			return jq.each(function() {
				var $form = $(this);
				$form.find("input[name], textarea[name],span[name]").each(function() {
					var $input = $(this);
					var name = $input.attr("name");
					if ($input.hasClass("combo-value")) {
						$input = $input.parent().parent().find("input[comboname='" + name + "']");
						if ($input.hasClass("easyui-combobox") || $input.hasClass("combobox-f")) {
							$input.combobox("disable", disable);
						} else if ($input.hasClass("easyui-combogrid") || $input.hasClass("combogrid-f")) {
							$input.combogrid("disable", disable);
						} else if ($input.hasClass("easyui-datebox")) {
							$input.datebox("disable", disable);
						} else if ($input.hasClass("easyui-datetimebox")) {
							$input.datetimebox("disable", disable);
						} else if ($input.hasClass("easyui-combotree") || $input.hasClass("combotree-f")) {
							$input.combotree("disable", disable);
						}
					} else {
						$input.attr("disabled","disabled");
					}
				});
			});
		},
		
		enableAssembly : function(jq, enable) {
			return jq.each(function() {
				var $form = $(this);
				$form.find("input[name], textarea[name],span[name]").each(function() {
					var $input = $(this);
					var name = $input.attr("name");
					if ($input.hasClass("combo-value")) {
						$input = $input.parent().parent().find("input[comboname='" + name + "']");
						if ($input.hasClass("easyui-combobox") || $input.hasClass("combobox-f")) {
							$input.combobox("enable", enable);
						} else if ($input.hasClass("easyui-combogrid") || $input.hasClass("combogrid-f")) {
							$input.combogrid("enable", enable);
						} else if ($input.hasClass("easyui-datebox")) {
							$input.datebox("enable", enable);
						} else if ($input.hasClass("easyui-datetimebox")) {
							$input.datetimebox("enable", enable);
						} else if ($input.hasClass("easyui-combotree") || $input.hasClass("combotree-f")) {
							$input.combotree("enable", enable);
						}
					} else {
						$input.removeAttr("disabled");
					}
				});
			});
		},
		
		fitHeight : function(jq) {
			return jq.each(function() {
				var $form = $(this);
				if ($form.data("fittedHeight")) {
					return;
				}
				if ($form.find("table").size() == 0) {
					return;
				}
				if ($form.closest(".panel:hidden").size() != 0) {
					return;
				}
				var $panel = $form.closest("div[region]");
				if ($panel.size() > 0) {
					var $div = $("<div/>");
					$div.append($panel.children()).appendTo($panel);
					var height = $div.height() + 4;
					if (! $panel.panel("options").noheader && $panel.panel("options").title) {
						height += 26;
					}
					$panel.panel("resize", {height : height});
					$panel.closest(".easyui-layout").layout("resize");
					$form.data("fittedHeight", true);
				}
			});
		}

	});
	
	/******** tree *********/
	$.extend($.fn.tree.methods,{
	    getCheckedExt: function(jq){
	        var checked = $(jq).tree("getChecked");
	        var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
	        $.each(checkbox2,function(){
	            var node = $.extend({}, $.data(this, "tree-node"), {
	                target : this
	            });
	            checked.push(node);
	        });
	        return checked;
	    },
	    getSolidExt:function(jq){
	        var checked =[];
	        var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
	        $.each(checkbox2,function(){
	            var node = $.extend({}, $.data(this, "tree-node"), {
	                target : this
	            });
	            checked.push(node);
	        });
	        return checked;
	    },
	    uncheckAll:function(jq){
	    	var checked = $(jq).tree("getChecked");
	        $.each(checked,function(i,o){
	        	$(jq).tree("uncheck",o.target);
	        });
	    },
	    checkNodes:function(jq,ids){
	    	var node;
	        $.each(ids,function(i,o){
	        	node = $(jq).tree("find",o);
	        	if($(jq).tree("isLeaf",node.target)){
	        		$(jq).tree("check",node.target);
	        	}
	        });
	    }
	});
	
	/******** datagrid *********/
	var datagridDefaults = {
		editors : {
			datetimebox : {
				init : function(container, options) {
					var editor = $("<input type='text'>").appendTo(container);
					editor.datetimebox(options);
					return editor;
				},
				destroy : function(target) {
					$(target).datetimebox("destroy");
				},
				getValue : function(target) {
					return $(target).datetimebox("getValue");
				},
				setValue : function(target, value) {
					$(target).datetimebox("setValue", value);
				},
				resize : function(target, width) {
					$(target).datetimebox("resize", width);
				}
			},
			combogrid : {
				init : function(container, options) {
					var editor = $("<input type='text'>").appendTo(container);
					editor.combogrid(options);
					return editor;
				},
				destroy : function(target) {
					$(target).combogrid("destroy");
				},
				getValue : function(target) {
					return $(target).combogrid("getValue");
				},
				setValue : function(target, value) {
					$(target).combogrid("setValue", value);
				},
				resize : function(target, width) {
					$(target).combogrid("resize", width);
				}
			}
		}
	};
	
	$.extend(true, $.fn.datagrid.defaults, datagridDefaults);
	
	$.fn.datagrid.defaults.editors.combobox.getValue = function(target) {
		var opts=$(target).combobox("options");
		//取到所属的datagrid跟当前行索引
		var datagridObj = $(target).parents(".datagrid-view2").next();
		var rowIndex = eval($(target).parents(".datagrid-row-editing").attr("datagrid-row-index"));
		
		//取出当前行数据
		var row = $(datagridObj).datagrid("getRows")[rowIndex];
		
		if(opts.multiple){
			if(opts.idField){
				row[opts.idField] = $(target).combobox("getValues").join(opts.separator);
			}
			
			if(opts.returnText) {
				return $(target).combobox("getText");
			} else {
				return $(target).combobox("getValues").join(opts.separator);
			}
		}else{
			if(opts.idField){
				row[opts.idField] = $(target).combobox("getValue");
			}
		
			if(opts.returnText) {
				return $(target).combobox("getText");
			} else {
				return $(target).combobox("getValue");
			}
		}
	};
	
	$.fn.datagrid.defaults.editors.combobox.setValue = function(target,value) {
		var opts=$(target).combobox("options");
		
		//取到所属的datagrid跟当前行索引
		var datagridObj = $(target).parents(".datagrid-view2").next();
		var rowIndex = eval($(target).parents(".datagrid-row-editing").attr("datagrid-row-index"));
		
		//取出当前行数据
		var row = $(datagridObj).datagrid("getRows")[rowIndex];
		
		if(opts.multiple){
			if(opts.returnText) {
				 $(target).combobox("setText",value);
			} else {
				$(target).combobox("setValues",value.split(opts.separator));
			}
		}else{
			if(opts.returnText) {
				$(target).combobox("setText",value);
			} else {
				$(target).combobox("setValue",value);
			}
		}
		
		if(opts.idField){
			$(target).combobox("setValue",row[opts.idField]);
		}
	};
	
	$.extend($.fn.datagrid.methods,{
	    clickRow: function(jq,rowIndex){
	    	jq.prev().find("tr.datagrid-row[datagrid-row-index]")[rowIndex].click();
	    	//jq.prev().find("tr#datagrid-row-r1-2-"+rowIndex+".datagrid-row").click();
	    }
	});
	
	/******** datebox *********/
	$.extend($.fn.datebox.methods,{
		setDateNow : function (jq) {
			return jq.each(function() {
				var now = new Date();
				var year = now.getFullYear();
				var month = now.getMonth()+1;
				var date = now.getDate();
				
				month = month<10 ? "0"+month : month;
				date = date<10 ? "0"+date : date;
				
				var str = year+"-"+month+"-"+date;
				$(this).datebox("setValue",str);
			});
		}
	});
	
})();
$(function () {
	$("#shop_shopName").validatebox({
		required : true, 
		missingMessage : '请输入店铺名称',
	});

	$("#shop_connectPerson").validatebox({
		required : true, 
		missingMessage : '请输入联系人',
	});

	$("#shop_connectPhone").validatebox({
		required : true, 
		missingMessage : '请输入联系电话',
	});

	$("#shop_address").validatebox({
		required : true, 
		missingMessage : '请输入店铺地址',
	});

	//单击添加按钮
	$("#shopAddButton").click(function () {
		//验证表单 
		if(!$("#shopAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		} else {
			$("#shopAddForm").form({
			    url:"Shop/add",
			    onSubmit: function(){
					if($("#shopAddForm").form("validate"))  { 
	                	$.messager.progress({
							text : "正在提交数据中...",
						}); 
	                	return true;
	                } else {
	                    return false;
	                }
			    },
			    success:function(data){
			    	$.messager.progress("close");
                    //此处data={"Success":true}是字符串
                	var obj = jQuery.parseJSON(data); 
                    if(obj.success){ 
                        $.messager.alert("消息","保存成功！");
                        $(".messager-window").css("z-index",10000);
                        $("#shopAddForm").form("clear");
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    }
			    }
			});
			//提交表单
			$("#shopAddForm").submit();
		}
	});

	//单击清空按钮
	$("#shopClearButton").click(function () { 
		$("#shopAddForm").form("clear"); 
	});
});

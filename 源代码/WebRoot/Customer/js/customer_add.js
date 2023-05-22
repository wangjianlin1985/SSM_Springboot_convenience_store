$(function () {
	$("#customer_name").validatebox({
		required : true, 
		missingMessage : '请输入姓名',
	});

	$("#customer_gender").validatebox({
		required : true, 
		missingMessage : '请输入性别',
	});

	$("#customer_birthDate").datebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	$("#customer_telephone").validatebox({
		required : true, 
		missingMessage : '请输入联系电话',
	});

	$("#customer_regTime").datetimebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	//单击添加按钮
	$("#customerAddButton").click(function () {
		//验证表单 
		if(!$("#customerAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		} else {
			$("#customerAddForm").form({
			    url:"Customer/add",
			    onSubmit: function(){
					if($("#customerAddForm").form("validate"))  { 
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
                        $("#customerAddForm").form("clear");
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    }
			    }
			});
			//提交表单
			$("#customerAddForm").submit();
		}
	});

	//单击清空按钮
	$("#customerClearButton").click(function () { 
		$("#customerAddForm").form("clear"); 
	});
});

$(function () {
	$.ajax({
		url : "Customer/" + $("#customer_customerId_edit").val() + "/update",
		type : "get",
		data : {
			//customerId : $("#customer_customerId_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (customer, response, status) {
			$.messager.progress("close");
			if (customer) { 
				$("#customer_customerId_edit").val(customer.customerId);
				$("#customer_customerId_edit").validatebox({
					required : true,
					missingMessage : "请输入客户id",
					editable: false
				});
				$("#customer_name_edit").val(customer.name);
				$("#customer_name_edit").validatebox({
					required : true,
					missingMessage : "请输入姓名",
				});
				$("#customer_gender_edit").val(customer.gender);
				$("#customer_gender_edit").validatebox({
					required : true,
					missingMessage : "请输入性别",
				});
				$("#customer_birthDate_edit").datebox({
					value: customer.birthDate,
					required: true,
					showSeconds: true,
				});
				$("#customer_telephone_edit").val(customer.telephone);
				$("#customer_telephone_edit").validatebox({
					required : true,
					missingMessage : "请输入联系电话",
				});
				$("#customer_address_edit").val(customer.address);
				$("#customer_regTime_edit").datetimebox({
					value: customer.regTime,
					required: true,
					showSeconds: true,
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#customerModifyButton").click(function(){ 
		if ($("#customerEditForm").form("validate")) {
			$("#customerEditForm").form({
			    url:"Customer/" +  $("#customer_customerId_edit").val() + "/update",
			    onSubmit: function(){
					if($("#customerEditForm").form("validate"))  {
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
                	var obj = jQuery.parseJSON(data);
                    if(obj.success){
                        $.messager.alert("消息","信息修改成功！");
                        $(".messager-window").css("z-index",10000);
                        //location.href="frontlist";
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    } 
			    }
			});
			//提交表单
			$("#customerEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});

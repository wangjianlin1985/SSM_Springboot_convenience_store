$(function () {
	$.ajax({
		url : "Manager/" + $("#manager_managerUserName_edit").val() + "/update",
		type : "get",
		data : {
			//managerUserName : $("#manager_managerUserName_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (manager, response, status) {
			$.messager.progress("close");
			if (manager) { 
				$("#manager_managerUserName_edit").val(manager.managerUserName);
				$("#manager_managerUserName_edit").validatebox({
					required : true,
					missingMessage : "请输入用户名",
					editable: false
				});
				$("#manager_password_edit").val(manager.password);
				$("#manager_password_edit").validatebox({
					required : true,
					missingMessage : "请输入登录密码",
				});
				$("#manager_shopObj_shopId_edit").combobox({
					url:"Shop/listAll",
					valueField:"shopId",
					textField:"shopName",
					panelHeight: "auto",
					editable: false, //不允许手动输入 
					onLoadSuccess: function () { //数据加载完毕事件
						$("#manager_shopObj_shopId_edit").combobox("select", manager.shopObjPri);
						//var data = $("#manager_shopObj_shopId_edit").combobox("getData"); 
						//if (data.length > 0) {
							//$("#manager_shopObj_shopId_edit").combobox("select", data[0].shopId);
						//}
					}
				});
				$("#manager_name_edit").val(manager.name);
				$("#manager_name_edit").validatebox({
					required : true,
					missingMessage : "请输入姓名",
				});
				$("#manager_sex_edit").val(manager.sex);
				$("#manager_sex_edit").validatebox({
					required : true,
					missingMessage : "请输入性别",
				});
				$("#manager_birthday_edit").datebox({
					value: manager.birthday,
					required: true,
					showSeconds: true,
				});
				$("#manager_userPhoto").val(manager.userPhoto);
				$("#manager_userPhotoImg").attr("src", manager.userPhoto);
				$("#manager_telephone_edit").val(manager.telephone);
				$("#manager_telephone_edit").validatebox({
					required : true,
					missingMessage : "请输入联系电话",
				});
				$("#manager_memo_edit").val(manager.memo);
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#managerModifyButton").click(function(){ 
		if ($("#managerEditForm").form("validate")) {
			$("#managerEditForm").form({
			    url:"Manager/" +  $("#manager_managerUserName_edit").val() + "/update",
			    onSubmit: function(){
					if($("#managerEditForm").form("validate"))  {
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
			$("#managerEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});

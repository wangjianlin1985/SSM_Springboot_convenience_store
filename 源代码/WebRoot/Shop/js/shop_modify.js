$(function () {
	$.ajax({
		url : "Shop/" + $("#shop_shopId_edit").val() + "/update",
		type : "get",
		data : {
			//shopId : $("#shop_shopId_edit").val(),
		},
		beforeSend : function () {
			$.messager.progress({
				text : "正在获取中...",
			});
		},
		success : function (shop, response, status) {
			$.messager.progress("close");
			if (shop) { 
				$("#shop_shopId_edit").val(shop.shopId);
				$("#shop_shopId_edit").validatebox({
					required : true,
					missingMessage : "请输入店铺id",
					editable: false
				});
				$("#shop_shopName_edit").val(shop.shopName);
				$("#shop_shopName_edit").validatebox({
					required : true,
					missingMessage : "请输入店铺名称",
				});
				$("#shop_connectPerson_edit").val(shop.connectPerson);
				$("#shop_connectPerson_edit").validatebox({
					required : true,
					missingMessage : "请输入联系人",
				});
				$("#shop_connectPhone_edit").val(shop.connectPhone);
				$("#shop_connectPhone_edit").validatebox({
					required : true,
					missingMessage : "请输入联系电话",
				});
				$("#shop_address_edit").val(shop.address);
				$("#shop_address_edit").validatebox({
					required : true,
					missingMessage : "请输入店铺地址",
				});
			} else {
				$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
				$(".messager-window").css("z-index",10000);
			}
		}
	});

	$("#shopModifyButton").click(function(){ 
		if ($("#shopEditForm").form("validate")) {
			$("#shopEditForm").form({
			    url:"Shop/" +  $("#shop_shopId_edit").val() + "/update",
			    onSubmit: function(){
					if($("#shopEditForm").form("validate"))  {
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
			$("#shopEditForm").submit();
		} else {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		}
	});
});

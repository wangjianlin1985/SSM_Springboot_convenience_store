var shop_manage_tool = null; 
$(function () { 
	initShopManageTool(); //建立Shop管理对象
	shop_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#shop_manage").datagrid({
		url : 'Shop/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "shopId",
		sortOrder : "desc",
		toolbar : "#shop_manage_tool",
		columns : [[
			{
				field : "shopId",
				title : "店铺id",
				width : 70,
			},
			{
				field : "shopName",
				title : "店铺名称",
				width : 140,
			},
			{
				field : "connectPerson",
				title : "联系人",
				width : 140,
			},
			{
				field : "connectPhone",
				title : "联系电话",
				width : 140,
			},
		]],
	});

	$("#shopEditDiv").dialog({
		title : "修改管理",
		top: "50px",
		width : 700,
		height : 515,
		modal : true,
		closed : true,
		iconCls : "icon-edit-new",
		buttons : [{
			text : "提交",
			iconCls : "icon-edit-new",
			handler : function () {
				if ($("#shopEditForm").form("validate")) {
					//验证表单 
					if(!$("#shopEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#shopEditForm").form({
						    url:"Shop/" + $("#shop_shopId_edit").val() + "/update",
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
						    	console.log(data);
			                	var obj = jQuery.parseJSON(data);
			                    if(obj.success){
			                        $.messager.alert("消息","信息修改成功！");
			                        $("#shopEditDiv").dialog("close");
			                        shop_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#shopEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#shopEditDiv").dialog("close");
				$("#shopEditForm").form("reset"); 
			},
		}],
	});
});

function initShopManageTool() {
	shop_manage_tool = {
		init: function() {
		},
		reload : function () {
			$("#shop_manage").datagrid("reload");
		},
		redo : function () {
			$("#shop_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#shop_manage").datagrid("options").queryParams;
			queryParams["shopName"] = $("#shopName").val();
			queryParams["connectPerson"] = $("#connectPerson").val();
			queryParams["connectPhone"] = $("#connectPhone").val();
			$("#shop_manage").datagrid("options").queryParams=queryParams; 
			$("#shop_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#shopQueryForm").form({
			    url:"Shop/OutToExcel",
			});
			//提交表单
			$("#shopQueryForm").submit();
		},
		remove : function () {
			var rows = $("#shop_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var shopIds = [];
						for (var i = 0; i < rows.length; i ++) {
							shopIds.push(rows[i].shopId);
						}
						$.ajax({
							type : "POST",
							url : "Shop/deletes",
							data : {
								shopIds : shopIds.join(","),
							},
							beforeSend : function () {
								$("#shop_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#shop_manage").datagrid("loaded");
									$("#shop_manage").datagrid("load");
									$("#shop_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#shop_manage").datagrid("loaded");
									$("#shop_manage").datagrid("load");
									$("#shop_manage").datagrid("unselectAll");
									$.messager.alert("消息",data.message);
								}
							},
						});
					}
				});
			} else {
				$.messager.alert("提示", "请选择要删除的记录！", "info");
			}
		},
		edit : function () {
			var rows = $("#shop_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Shop/" + rows[0].shopId +  "/update",
					type : "get",
					data : {
						//shopId : rows[0].shopId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (shop, response, status) {
						$.messager.progress("close");
						if (shop) { 
							$("#shopEditDiv").dialog("open");
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
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}

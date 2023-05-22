var transfer_manage_tool = null; 
$(function () { 
	initTransferManageTool(); //建立Transfer管理对象
	transfer_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#transfer_manage").datagrid({
		url : 'Transfer/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "transferId",
		sortOrder : "desc",
		toolbar : "#transfer_manage_tool",
		columns : [[
			{
				field : "transferId",
				title : "调拨id",
				width : 70,
			},
			{
				field : "productObj",
				title : "调拨商品",
				width : 140,
			},
			{
				field : "transferCount",
				title : "调拨数量",
				width : 70,
			},
			{
				field : "shopObj1",
				title : "调出店铺",
				width : 140,
			},
			{
				field : "shopObj2",
				title : "调入店铺",
				width : 140,
			},
			{
				field : "transferTime",
				title : "调拨时间",
				width : 140,
			},
		]],
	});

	$("#transferEditDiv").dialog({
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
				if ($("#transferEditForm").form("validate")) {
					//验证表单 
					if(!$("#transferEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#transferEditForm").form({
						    url:"Transfer/" + $("#transfer_transferId_edit").val() + "/update",
						    onSubmit: function(){
								if($("#transferEditForm").form("validate"))  {
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
			                        $("#transferEditDiv").dialog("close");
			                        transfer_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#transferEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#transferEditDiv").dialog("close");
				$("#transferEditForm").form("reset"); 
			},
		}],
	});
});

function initTransferManageTool() {
	transfer_manage_tool = {
		init: function() {
			$.ajax({
				url : "Product/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#productObj_productId_query").combobox({ 
					    valueField:"productId",
					    textField:"productName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{productId:0,productName:"不限制"});
					$("#productObj_productId_query").combobox("loadData",data); 
				}
			});
			$.ajax({
				url : "Shop/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#shopObj1_shopId_query").combobox({ 
					    valueField:"shopId",
					    textField:"shopName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{shopId:0,shopName:"不限制"});
					$("#shopObj1_shopId_query").combobox("loadData",data); 
				}
			});
			$.ajax({
				url : "Shop/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#shopObj2_shopId_query").combobox({ 
					    valueField:"shopId",
					    textField:"shopName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{shopId:0,shopName:"不限制"});
					$("#shopObj2_shopId_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#transfer_manage").datagrid("reload");
		},
		redo : function () {
			$("#transfer_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#transfer_manage").datagrid("options").queryParams;
			queryParams["productObj.productId"] = $("#productObj_productId_query").combobox("getValue");
			queryParams["shopObj1.shopId"] = $("#shopObj1_shopId_query").combobox("getValue");
			queryParams["shopObj2.shopId"] = $("#shopObj2_shopId_query").combobox("getValue");
			queryParams["transferTime"] = $("#transferTime").datebox("getValue"); 
			$("#transfer_manage").datagrid("options").queryParams=queryParams; 
			$("#transfer_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#transferQueryForm").form({
			    url:"Transfer/OutToExcel",
			});
			//提交表单
			$("#transferQueryForm").submit();
		},
		remove : function () {
			var rows = $("#transfer_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var transferIds = [];
						for (var i = 0; i < rows.length; i ++) {
							transferIds.push(rows[i].transferId);
						}
						$.ajax({
							type : "POST",
							url : "Transfer/deletes",
							data : {
								transferIds : transferIds.join(","),
							},
							beforeSend : function () {
								$("#transfer_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#transfer_manage").datagrid("loaded");
									$("#transfer_manage").datagrid("load");
									$("#transfer_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#transfer_manage").datagrid("loaded");
									$("#transfer_manage").datagrid("load");
									$("#transfer_manage").datagrid("unselectAll");
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
			var rows = $("#transfer_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Transfer/" + rows[0].transferId +  "/update",
					type : "get",
					data : {
						//transferId : rows[0].transferId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (transfer, response, status) {
						$.messager.progress("close");
						if (transfer) { 
							$("#transferEditDiv").dialog("open");
							$("#transfer_transferId_edit").val(transfer.transferId);
							$("#transfer_transferId_edit").validatebox({
								required : true,
								missingMessage : "请输入调拨id",
								editable: false
							});
							$("#transfer_productObj_productId_edit").combobox({
								url:"Product/listAll",
							    valueField:"productId",
							    textField:"productName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#transfer_productObj_productId_edit").combobox("select", transfer.productObjPri);
									//var data = $("#transfer_productObj_productId_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#transfer_productObj_productId_edit").combobox("select", data[0].productId);
						            //}
								}
							});
							$("#transfer_transferCount_edit").val(transfer.transferCount);
							$("#transfer_transferCount_edit").validatebox({
								required : true,
								validType : "integer",
								missingMessage : "请输入调拨数量",
								invalidMessage : "调拨数量输入不对",
							});
							$("#transfer_shopObj1_shopId_edit").combobox({
								url:"Shop/listAll",
							    valueField:"shopId",
							    textField:"shopName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#transfer_shopObj1_shopId_edit").combobox("select", transfer.shopObj1Pri);
									//var data = $("#transfer_shopObj1_shopId_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#transfer_shopObj1_shopId_edit").combobox("select", data[0].shopId);
						            //}
								}
							});
							$("#transfer_shopObj2_shopId_edit").combobox({
								url:"Shop/listAll",
							    valueField:"shopId",
							    textField:"shopName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#transfer_shopObj2_shopId_edit").combobox("select", transfer.shopObj2Pri);
									//var data = $("#transfer_shopObj2_shopId_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#transfer_shopObj2_shopId_edit").combobox("select", data[0].shopId);
						            //}
								}
							});
							$("#transfer_transferTime_edit").datetimebox({
								value: transfer.transferTime,
							    required: true,
							    showSeconds: true,
							});
							$("#transfer_memo_edit").val(transfer.memo);
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

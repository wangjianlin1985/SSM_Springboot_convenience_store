var customer_manage_tool = null; 
$(function () { 
	initCustomerManageTool(); //建立Customer管理对象
	customer_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#customer_manage").datagrid({
		url : 'Customer/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "customerId",
		sortOrder : "desc",
		toolbar : "#customer_manage_tool",
		columns : [[
			{
				field : "customerId",
				title : "客户id",
				width : 70,
			},
			{
				field : "name",
				title : "姓名",
				width : 140,
			},
			{
				field : "gender",
				title : "性别",
				width : 140,
			},
			{
				field : "birthDate",
				title : "出生日期",
				width : 140,
			},
			{
				field : "telephone",
				title : "联系电话",
				width : 140,
			},
			{
				field : "regTime",
				title : "加入时间",
				width : 140,
			},
		]],
	});

	$("#customerEditDiv").dialog({
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
				if ($("#customerEditForm").form("validate")) {
					//验证表单 
					if(!$("#customerEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#customerEditForm").form({
						    url:"Customer/" + $("#customer_customerId_edit").val() + "/update",
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
						    	console.log(data);
			                	var obj = jQuery.parseJSON(data);
			                    if(obj.success){
			                        $.messager.alert("消息","信息修改成功！");
			                        $("#customerEditDiv").dialog("close");
			                        customer_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#customerEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#customerEditDiv").dialog("close");
				$("#customerEditForm").form("reset"); 
			},
		}],
	});
});

function initCustomerManageTool() {
	customer_manage_tool = {
		init: function() {
		},
		reload : function () {
			$("#customer_manage").datagrid("reload");
		},
		redo : function () {
			$("#customer_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#customer_manage").datagrid("options").queryParams;
			queryParams["name"] = $("#name").val();
			queryParams["birthDate"] = $("#birthDate").datebox("getValue"); 
			queryParams["telephone"] = $("#telephone").val();
			queryParams["regTime"] = $("#regTime").datebox("getValue"); 
			$("#customer_manage").datagrid("options").queryParams=queryParams; 
			$("#customer_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#customerQueryForm").form({
			    url:"Customer/OutToExcel",
			});
			//提交表单
			$("#customerQueryForm").submit();
		},
		remove : function () {
			var rows = $("#customer_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var customerIds = [];
						for (var i = 0; i < rows.length; i ++) {
							customerIds.push(rows[i].customerId);
						}
						$.ajax({
							type : "POST",
							url : "Customer/deletes",
							data : {
								customerIds : customerIds.join(","),
							},
							beforeSend : function () {
								$("#customer_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#customer_manage").datagrid("loaded");
									$("#customer_manage").datagrid("load");
									$("#customer_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#customer_manage").datagrid("loaded");
									$("#customer_manage").datagrid("load");
									$("#customer_manage").datagrid("unselectAll");
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
			var rows = $("#customer_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Customer/" + rows[0].customerId +  "/update",
					type : "get",
					data : {
						//customerId : rows[0].customerId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (customer, response, status) {
						$.messager.progress("close");
						if (customer) { 
							$("#customerEditDiv").dialog("open");
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
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}

var product_manage_tool = null; 
$(function () { 
	initProductManageTool(); //建立Product管理对象
	product_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#product_manage").datagrid({
		url : 'Product/list',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "productId",
		sortOrder : "desc",
		toolbar : "#product_manage_tool",
		columns : [[
			{
				field : "productId",
				title : "商品id",
				width : 70,
			},
			{
				field : "productClassObj",
				title : "商品类别",
				width : 140,
			},
			{
				field : "productName",
				title : "商品名称",
				width : 140,
			},
			{
				field : "mainImage",
				title : "商品主图",
				width : "70px",
				height: "65px",
				formatter: function(val,row) {
					return "<img src='" + val + "' width='65px' height='55px' />";
				}
 			},
			{
				field : "price",
				title : "商品价格",
				width : 70,
			},
			{
				field : "addTime",
				title : "发布时间",
				width : 140,
			},
		]],
	});

	$("#productEditDiv").dialog({
		title : "详情查看",
		top: "10px",
		width : 1000,
		height : 600,
		modal : true,
		closed : true,
		iconCls : "icon-edit-new",
		buttons : [{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#productEditDiv").dialog("close");
				$("#productEditForm").form("reset"); 
			},
		}],
	});
});

function initProductManageTool() {
	product_manage_tool = {
		init: function() {
			$.ajax({
				url : "ProductClass/listAll",
				type : "post",
				success : function (data, response, status) {
					$("#productClassObj_productClassId_query").combobox({ 
					    valueField:"productClassId",
					    textField:"prodcutClassName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{productClassId:0,prodcutClassName:"不限制"});
					$("#productClassObj_productClassId_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#product_manage").datagrid("reload");
		},
		redo : function () {
			$("#product_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#product_manage").datagrid("options").queryParams;
			queryParams["productClassObj.productClassId"] = $("#productClassObj_productClassId_query").combobox("getValue");
			queryParams["productName"] = $("#productName").val();
			queryParams["addTime"] = $("#addTime").datebox("getValue"); 
			$("#product_manage").datagrid("options").queryParams=queryParams; 
			$("#product_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#productQueryForm").form({
			    url:"Product/OutToExcel",
			});
			//提交表单
			$("#productQueryForm").submit();
		},
		remove : function () {
			var rows = $("#product_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var productIds = [];
						for (var i = 0; i < rows.length; i ++) {
							productIds.push(rows[i].productId);
						}
						$.ajax({
							type : "POST",
							url : "Product/deletes",
							data : {
								productIds : productIds.join(","),
							},
							beforeSend : function () {
								$("#product_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#product_manage").datagrid("loaded");
									$("#product_manage").datagrid("load");
									$("#product_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#product_manage").datagrid("loaded");
									$("#product_manage").datagrid("load");
									$("#product_manage").datagrid("unselectAll");
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
			var rows = $("#product_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Product/" + rows[0].productId +  "/update",
					type : "get",
					data : {
						//productId : rows[0].productId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (product, response, status) {
						$.messager.progress("close");
						if (product) { 
							$("#productEditDiv").dialog("open");
							$("#product_productId_edit").val(product.productId);
							$("#product_productId_edit").validatebox({
								required : true,
								missingMessage : "请输入商品id",
								editable: false
							});
							$("#product_productClassObj_productClassId_edit").combobox({
								url:"ProductClass/listAll",
							    valueField:"productClassId",
							    textField:"prodcutClassName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#product_productClassObj_productClassId_edit").combobox("select", product.productClassObjPri);
									//var data = $("#product_productClassObj_productClassId_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#product_productClassObj_productClassId_edit").combobox("select", data[0].productClassId);
						            //}
								}
							});
							$("#product_productName_edit").val(product.productName);
							$("#product_productName_edit").validatebox({
								required : true,
								missingMessage : "请输入商品名称",
							});
							$("#product_mainImage").val(product.mainImage);
							$("#product_mainImageImg").attr("src", product.mainImage);
							$("#product_price_edit").val(product.price);
							$("#product_price_edit").validatebox({
								required : true,
								validType : "number",
								missingMessage : "请输入商品价格",
								invalidMessage : "商品价格输入不对",
							});
							product_productDesc_editor.setContent(product.productDesc, false);
							$("#product_addTime_edit").datetimebox({
								value: product.addTime,
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

<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/transfer.css" /> 

<div id="transfer_manage"></div>
<div id="transfer_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="transfer_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="transfer_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="transfer_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="transfer_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="transfer_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="transferQueryForm" method="post">
			调拨商品：<input class="textbox" type="text" id="productObj_productId_query" name="productObj.productId" style="width: auto"/>
			调出店铺：<input class="textbox" type="text" id="shopObj1_shopId_query" name="shopObj1.shopId" style="width: auto"/>
			调入店铺：<input class="textbox" type="text" id="shopObj2_shopId_query" name="shopObj2.shopId" style="width: auto"/>
			调拨时间：<input type="text" id="transferTime" name="transferTime" class="easyui-datebox" editable="false" style="width:100px">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="transfer_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="transferEditDiv">
	<form id="transferEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">调拨id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="transfer_transferId_edit" name="transfer.transferId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">调拨商品:</span>
			<span class="inputControl">
				<input class="textbox"  id="transfer_productObj_productId_edit" name="transfer.productObj.productId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">调拨数量:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="transfer_transferCount_edit" name="transfer.transferCount" style="width:80px" />

			</span>

		</div>
		<div>
			<span class="label">调出店铺:</span>
			<span class="inputControl">
				<input class="textbox"  id="transfer_shopObj1_shopId_edit" name="transfer.shopObj1.shopId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">调入店铺:</span>
			<span class="inputControl">
				<input class="textbox"  id="transfer_shopObj2_shopId_edit" name="transfer.shopObj2.shopId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">调拨时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="transfer_transferTime_edit" name="transfer.transferTime" />

			</span>

		</div>
		<div>
			<span class="label">调拨备注:</span>
			<span class="inputControl">
				<textarea id="transfer_memo_edit" name="transfer.memo" rows="8" cols="60"></textarea>

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Transfer/js/transfer_manage.js"></script> 

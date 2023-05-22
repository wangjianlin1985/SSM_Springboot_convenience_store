<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/shop.css" /> 

<div id="shop_manage"></div>
<div id="shop_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="shop_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="shop_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="shop_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="shop_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="shop_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="shopQueryForm" method="post">
			店铺名称：<input type="text" class="textbox" id="shopName" name="shopName" style="width:110px" />
			联系人：<input type="text" class="textbox" id="connectPerson" name="connectPerson" style="width:110px" />
			联系电话：<input type="text" class="textbox" id="connectPhone" name="connectPhone" style="width:110px" />
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="shop_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="shopEditDiv">
	<form id="shopEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">店铺id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="shop_shopId_edit" name="shop.shopId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">店铺名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="shop_shopName_edit" name="shop.shopName" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">联系人:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="shop_connectPerson_edit" name="shop.connectPerson" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">联系电话:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="shop_connectPhone_edit" name="shop.connectPhone" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">店铺地址:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="shop_address_edit" name="shop.address" style="width:200px" />

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="Shop/js/shop_manage.js"></script> 

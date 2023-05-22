<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/supplier.css" />
<div id="supplier_editDiv">
	<form id="supplierEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">供应商id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="supplier_supplierId_edit" name="supplier.supplierId" value="<%=request.getParameter("supplierId") %>" style="width:200px" />
			</span>
		</div>

		<div>
			<span class="label">供应商名称:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="supplier_supplierName_edit" name="supplier.supplierName" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">法人代表:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="supplier_farendaibiao_edit" name="supplier.farendaibiao" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">供应商电话:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="supplier_telephone_edit" name="supplier.telephone" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">供应商地址:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="supplier_address_edit" name="supplier.address" style="width:200px" />

			</span>

		</div>
		<div class="operation">
			<a id="supplierModifyButton" class="easyui-linkbutton">更新</a> 
		</div>
	</form>
</div>
<script src="${pageContext.request.contextPath}/Supplier/js/supplier_modify.js"></script> 

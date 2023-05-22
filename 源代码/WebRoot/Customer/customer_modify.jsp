<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/customer.css" />
<div id="customer_editDiv">
	<form id="customerEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">客户id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_customerId_edit" name="customer.customerId" value="<%=request.getParameter("customerId") %>" style="width:200px" />
			</span>
		</div>

		<div>
			<span class="label">姓名:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_name_edit" name="customer.name" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">性别:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_gender_edit" name="customer.gender" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">出生日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_birthDate_edit" name="customer.birthDate" />

			</span>

		</div>
		<div>
			<span class="label">联系电话:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_telephone_edit" name="customer.telephone" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">家庭地址:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_address_edit" name="customer.address" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">加入时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_regTime_edit" name="customer.regTime" />

			</span>

		</div>
		<div class="operation">
			<a id="customerModifyButton" class="easyui-linkbutton">更新</a> 
		</div>
	</form>
</div>
<script src="${pageContext.request.contextPath}/Customer/js/customer_modify.js"></script> 

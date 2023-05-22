<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/customer.css" />
<div id="customerAddDiv">
	<form id="customerAddForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">姓名:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_name" name="customer.name" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">性别:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_gender" name="customer.gender" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">出生日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_birthDate" name="customer.birthDate" />

			</span>

		</div>
		<div>
			<span class="label">联系电话:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_telephone" name="customer.telephone" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">家庭地址:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_address" name="customer.address" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">加入时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="customer_regTime" name="customer.regTime" />

			</span>

		</div>
		<div class="operation">
			<a id="customerAddButton" class="easyui-linkbutton">添加</a>
			<a id="customerClearButton" class="easyui-linkbutton">重填</a>
		</div> 
	</form>
</div>
<script src="${pageContext.request.contextPath}/Customer/js/customer_add.js"></script> 

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/manager.css" />
<div id="manager_editDiv">
	<form id="managerEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">用户名:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="manager_managerUserName_edit" name="manager.managerUserName" value="<%=request.getParameter("managerUserName") %>" style="width:200px" />
			</span>
		</div>

		<div>
			<span class="label">登录密码:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="manager_password_edit" name="manager.password" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">负责店铺:</span>
			<span class="inputControl">
				<input class="textbox"  id="manager_shopObj_shopId_edit" name="manager.shopObj.shopId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">姓名:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="manager_name_edit" name="manager.name" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">性别:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="manager_sex_edit" name="manager.sex" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">出生日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="manager_birthday_edit" name="manager.birthday" />

			</span>

		</div>
		<div>
			<span class="label">管理照片:</span>
			<span class="inputControl">
				<img id="manager_userPhotoImg" width="200px" border="0px"/><br/>
    			<input type="hidden" id="manager_userPhoto" name="manager.userPhoto"/>
				<input id="userPhotoFile" name="userPhotoFile" type="file" size="50" />
			</span>
		</div>
		<div>
			<span class="label">联系电话:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="manager_telephone_edit" name="manager.telephone" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">备注信息:</span>
			<span class="inputControl">
				<textarea id="manager_memo_edit" name="manager.memo" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div class="operation">
			<a id="managerModifyButton" class="easyui-linkbutton">更新</a> 
		</div>
	</form>
</div>
<script src="${pageContext.request.contextPath}/Manager/js/manager_modify.js"></script> 

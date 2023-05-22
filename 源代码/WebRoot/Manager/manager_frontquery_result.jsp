<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Manager" %>
<%@ page import="com.chengxusheji.po.Shop" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Manager> managerList = (List<Manager>)request.getAttribute("managerList");
    //获取所有的shopObj信息
    List<Shop> shopList = (List<Shop>)request.getAttribute("shopList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String managerUserName = (String)request.getAttribute("managerUserName"); //用户名查询关键字
    Shop shopObj = (Shop)request.getAttribute("shopObj");
    String name = (String)request.getAttribute("name"); //姓名查询关键字
    String birthday = (String)request.getAttribute("birthday"); //出生日期查询关键字
    String telephone = (String)request.getAttribute("telephone"); //联系电话查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>店铺管理员查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>Manager/frontlist">店铺管理员信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>Manager/manager_frontAdd.jsp" style="display:none;">添加店铺管理员</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<managerList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		Manager manager = managerList.get(i); //获取到店铺管理员对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>Manager/<%=manager.getManagerUserName() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=manager.getUserPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		用户名:<%=manager.getManagerUserName() %>
			     	</div>
			     	<div class="field">
	            		负责店铺:<%=manager.getShopObj().getShopName() %>
			     	</div>
			     	<div class="field">
	            		姓名:<%=manager.getName() %>
			     	</div>
			     	<div class="field">
	            		性别:<%=manager.getSex() %>
			     	</div>
			     	<div class="field">
	            		出生日期:<%=manager.getBirthday() %>
			     	</div>
			     	<div class="field">
	            		联系电话:<%=manager.getTelephone() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>Manager/<%=manager.getManagerUserName() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="managerEdit('<%=manager.getManagerUserName() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="managerDelete('<%=manager.getManagerUserName() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>店铺管理员查询</h1>
		</div>
		<form name="managerQueryForm" id="managerQueryForm" action="<%=basePath %>Manager/frontlist" class="mar_t15" method="post">
			<div class="form-group">
				<label for="managerUserName">用户名:</label>
				<input type="text" id="managerUserName" name="managerUserName" value="<%=managerUserName %>" class="form-control" placeholder="请输入用户名">
			</div>
            <div class="form-group">
            	<label for="shopObj_shopId">负责店铺：</label>
                <select id="shopObj_shopId" name="shopObj.shopId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(Shop shopTemp:shopList) {
	 					String selected = "";
 					if(shopObj!=null && shopObj.getShopId()!=null && shopObj.getShopId().intValue()==shopTemp.getShopId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=shopTemp.getShopId() %>" <%=selected %>><%=shopTemp.getShopName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="name">姓名:</label>
				<input type="text" id="name" name="name" value="<%=name %>" class="form-control" placeholder="请输入姓名">
			</div>
			<div class="form-group">
				<label for="birthday">出生日期:</label>
				<input type="text" id="birthday" name="birthday" class="form-control"  placeholder="请选择出生日期" value="<%=birthday %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="telephone">联系电话:</label>
				<input type="text" id="telephone" name="telephone" value="<%=telephone %>" class="form-control" placeholder="请输入联系电话">
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="managerEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;店铺管理员信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="managerEditForm" id="managerEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="manager_managerUserName_edit" class="col-md-3 text-right">用户名:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="manager_managerUserName_edit" name="manager.managerUserName" class="form-control" placeholder="请输入用户名" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="manager_password_edit" class="col-md-3 text-right">登录密码:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="manager_password_edit" name="manager.password" class="form-control" placeholder="请输入登录密码">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="manager_shopObj_shopId_edit" class="col-md-3 text-right">负责店铺:</label>
		  	 <div class="col-md-9">
			    <select id="manager_shopObj_shopId_edit" name="manager.shopObj.shopId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="manager_name_edit" class="col-md-3 text-right">姓名:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="manager_name_edit" name="manager.name" class="form-control" placeholder="请输入姓名">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="manager_sex_edit" class="col-md-3 text-right">性别:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="manager_sex_edit" name="manager.sex" class="form-control" placeholder="请输入性别">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="manager_birthday_edit" class="col-md-3 text-right">出生日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date manager_birthday_edit col-md-12" data-link-field="manager_birthday_edit" data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="manager_birthday_edit" name="manager.birthday" size="16" type="text" value="" placeholder="请选择出生日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="manager_userPhoto_edit" class="col-md-3 text-right">管理照片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="manager_userPhotoImg" border="0px"/><br/>
			    <input type="hidden" id="manager_userPhoto" name="manager.userPhoto"/>
			    <input id="userPhotoFile" name="userPhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="manager_telephone_edit" class="col-md-3 text-right">联系电话:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="manager_telephone_edit" name="manager.telephone" class="form-control" placeholder="请输入联系电话">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="manager_memo_edit" class="col-md-3 text-right">备注信息:</label>
		  	 <div class="col-md-9">
			    <textarea id="manager_memo_edit" name="manager.memo" rows="8" class="form-control" placeholder="请输入备注信息"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#managerEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxManagerModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.managerQueryForm.currentPage.value = currentPage;
    document.managerQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.managerQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.managerQueryForm.currentPage.value = pageValue;
    documentmanagerQueryForm.submit();
}

/*弹出修改店铺管理员界面并初始化数据*/
function managerEdit(managerUserName) {
	$.ajax({
		url :  basePath + "Manager/" + managerUserName + "/update",
		type : "get",
		dataType: "json",
		success : function (manager, response, status) {
			if (manager) {
				$("#manager_managerUserName_edit").val(manager.managerUserName);
				$("#manager_password_edit").val(manager.password);
				$.ajax({
					url: basePath + "Shop/listAll",
					type: "get",
					success: function(shops,response,status) { 
						$("#manager_shopObj_shopId_edit").empty();
						var html="";
		        		$(shops).each(function(i,shop){
		        			html += "<option value='" + shop.shopId + "'>" + shop.shopName + "</option>";
		        		});
		        		$("#manager_shopObj_shopId_edit").html(html);
		        		$("#manager_shopObj_shopId_edit").val(manager.shopObjPri);
					}
				});
				$("#manager_name_edit").val(manager.name);
				$("#manager_sex_edit").val(manager.sex);
				$("#manager_birthday_edit").val(manager.birthday);
				$("#manager_userPhoto").val(manager.userPhoto);
				$("#manager_userPhotoImg").attr("src", basePath +　manager.userPhoto);
				$("#manager_telephone_edit").val(manager.telephone);
				$("#manager_memo_edit").val(manager.memo);
				$('#managerEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除店铺管理员信息*/
function managerDelete(managerUserName) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Manager/deletes",
			data : {
				managerUserNames : managerUserName,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#managerQueryForm").submit();
					//location.href= basePath + "Manager/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交店铺管理员信息表单给服务器端修改*/
function ajaxManagerModify() {
	$.ajax({
		url :  basePath + "Manager/" + $("#manager_managerUserName_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#managerEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#managerQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

    /*出生日期组件*/
    $('.manager_birthday_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd',
    	minView: 2,
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>


<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>客户添加</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<jsp:include page="../header.jsp"></jsp:include>
<div class="container">
	<div class="row">
		<div class="col-md-12 wow fadeInUp" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li role="presentation" ><a href="<%=basePath %>Customer/frontlist">客户列表</a></li>
			    	<li role="presentation" class="active"><a href="#customerAdd" aria-controls="customerAdd" role="tab" data-toggle="tab">添加客户</a></li>
				</ul>
				<!-- Tab panes -->
				<div class="tab-content">
				    <div role="tabpanel" class="tab-pane" id="customerList">
				    </div>
				    <div role="tabpanel" class="tab-pane active" id="customerAdd"> 
				      	<form class="form-horizontal" name="customerAddForm" id="customerAddForm" enctype="multipart/form-data" method="post"  class="mar_t15">
						  <div class="form-group">
						  	 <label for="customer_name" class="col-md-2 text-right">姓名:</label>
						  	 <div class="col-md-8">
							    <input type="text" id="customer_name" name="customer.name" class="form-control" placeholder="请输入姓名">
							 </div>
						  </div>
						  <div class="form-group">
						  	 <label for="customer_gender" class="col-md-2 text-right">性别:</label>
						  	 <div class="col-md-8">
							    <input type="text" id="customer_gender" name="customer.gender" class="form-control" placeholder="请输入性别">
							 </div>
						  </div>
						  <div class="form-group">
						  	 <label for="customer_birthDateDiv" class="col-md-2 text-right">出生日期:</label>
						  	 <div class="col-md-8">
				                <div id="customer_birthDateDiv" class="input-group date customer_birthDate col-md-12" data-link-field="customer_birthDate" data-link-format="yyyy-mm-dd">
				                    <input class="form-control" id="customer_birthDate" name="customer.birthDate" size="16" type="text" value="" placeholder="请选择出生日期" readonly>
				                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
				                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				                </div>
						  	 </div>
						  </div>
						  <div class="form-group">
						  	 <label for="customer_telephone" class="col-md-2 text-right">联系电话:</label>
						  	 <div class="col-md-8">
							    <input type="text" id="customer_telephone" name="customer.telephone" class="form-control" placeholder="请输入联系电话">
							 </div>
						  </div>
						  <div class="form-group">
						  	 <label for="customer_address" class="col-md-2 text-right">家庭地址:</label>
						  	 <div class="col-md-8">
							    <input type="text" id="customer_address" name="customer.address" class="form-control" placeholder="请输入家庭地址">
							 </div>
						  </div>
						  <div class="form-group">
						  	 <label for="customer_regTimeDiv" class="col-md-2 text-right">加入时间:</label>
						  	 <div class="col-md-8">
				                <div id="customer_regTimeDiv" class="input-group date customer_regTime col-md-12" data-link-field="customer_regTime">
				                    <input class="form-control" id="customer_regTime" name="customer.regTime" size="16" type="text" value="" placeholder="请选择加入时间" readonly>
				                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
				                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				                </div>
						  	 </div>
						  </div>
				          <div class="form-group">
				             <span class="col-md-2""></span>
				             <span onclick="ajaxCustomerAdd();" class="btn btn-primary bottom5 top5">添加</span>
				          </div>
						</form> 
				        <style>#customerAddForm .form-group {margin:10px;}  </style>
					</div>
				</div>
			</div>
		</div>
	</div> 
</div>

<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrapvalidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script>
var basePath = "<%=basePath%>";
	//提交添加客户信息
	function ajaxCustomerAdd() { 
		//提交之前先验证表单
		$("#customerAddForm").data('bootstrapValidator').validate();
		if(!$("#customerAddForm").data('bootstrapValidator').isValid()){
			return;
		}
		jQuery.ajax({
			type : "post",
			url : basePath + "Customer/add",
			dataType : "json" , 
			data: new FormData($("#customerAddForm")[0]),
			success : function(obj) {
				if(obj.success){ 
					alert("保存成功！");
					$("#customerAddForm").find("input").val("");
					$("#customerAddForm").find("textarea").val("");
				} else {
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
	//验证客户添加表单字段
	$('#customerAddForm').bootstrapValidator({
		feedbackIcons: {
			valid: 'glyphicon glyphicon-ok',
			invalid: 'glyphicon glyphicon-remove',
			validating: 'glyphicon glyphicon-refresh'
		},
		fields: {
			"customer.name": {
				validators: {
					notEmpty: {
						message: "姓名不能为空",
					}
				}
			},
			"customer.gender": {
				validators: {
					notEmpty: {
						message: "性别不能为空",
					}
				}
			},
			"customer.birthDate": {
				validators: {
					notEmpty: {
						message: "出生日期不能为空",
					}
				}
			},
			"customer.telephone": {
				validators: {
					notEmpty: {
						message: "联系电话不能为空",
					}
				}
			},
			"customer.regTime": {
				validators: {
					notEmpty: {
						message: "加入时间不能为空",
					}
				}
			},
		}
	}); 
	//出生日期组件
	$('#customer_birthDateDiv').datetimepicker({
		language:  'zh-CN',  //显示语言
		format: 'yyyy-mm-dd',
		minView: 2,
		weekStart: 1,
		todayBtn:  1,
		autoclose: 1,
		minuteStep: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0
	}).on('hide',function(e) {
		//下面这行代码解决日期组件改变日期后不验证的问题
		$('#customerAddForm').data('bootstrapValidator').updateStatus('customer.birthDate', 'NOT_VALIDATED',null).validateField('customer.birthDate');
	});
	//加入时间组件
	$('#customer_regTimeDiv').datetimepicker({
		language:  'zh-CN',  //显示语言
		format: 'yyyy-mm-dd hh:ii:ss',
		weekStart: 1,
		todayBtn:  1,
		autoclose: 1,
		minuteStep: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0
	}).on('hide',function(e) {
		//下面这行代码解决日期组件改变日期后不验证的问题
		$('#customerAddForm').data('bootstrapValidator').updateStatus('customer.regTime', 'NOT_VALIDATED',null).validateField('customer.regTime');
	});
})
</script>
</body>
</html>

<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Transfer" %>
<%@ page import="com.chengxusheji.po.Product" %>
<%@ page import="com.chengxusheji.po.Shop" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Transfer> transferList = (List<Transfer>)request.getAttribute("transferList");
    //获取所有的productObj信息
    List<Product> productList = (List<Product>)request.getAttribute("productList");
    //获取所有的shopObj2信息
    List<Shop> shopList = (List<Shop>)request.getAttribute("shopList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Product productObj = (Product)request.getAttribute("productObj");
    Shop shopObj1 = (Shop)request.getAttribute("shopObj1");
    Shop shopObj2 = (Shop)request.getAttribute("shopObj2");
    String transferTime = (String)request.getAttribute("transferTime"); //调拨时间查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>商品调拨查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#transferListPanel" aria-controls="transferListPanel" role="tab" data-toggle="tab">商品调拨列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Transfer/transfer_frontAdd.jsp" style="display:none;">添加商品调拨</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="transferListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>调拨id</td><td>调拨商品</td><td>调拨数量</td><td>调出店铺</td><td>调入店铺</td><td>调拨时间</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<transferList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Transfer transfer = transferList.get(i); //获取到商品调拨对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=transfer.getTransferId() %></td>
 											<td><%=transfer.getProductObj().getProductName() %></td>
 											<td><%=transfer.getTransferCount() %></td>
 											<td><%=transfer.getShopObj1().getShopName() %></td>
 											<td><%=transfer.getShopObj2().getShopName() %></td>
 											<td><%=transfer.getTransferTime() %></td>
 											<td>
 												<a href="<%=basePath  %>Transfer/<%=transfer.getTransferId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="transferEdit('<%=transfer.getTransferId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="transferDelete('<%=transfer.getTransferId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

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
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>商品调拨查询</h1>
		</div>
		<form name="transferQueryForm" id="transferQueryForm" action="<%=basePath %>Transfer/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="productObj_productId">调拨商品：</label>
                <select id="productObj_productId" name="productObj.productId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(Product productTemp:productList) {
	 					String selected = "";
 					if(productObj!=null && productObj.getProductId()!=null && productObj.getProductId().intValue()==productTemp.getProductId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=productTemp.getProductId() %>" <%=selected %>><%=productTemp.getProductName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <div class="form-group">
            	<label for="shopObj1_shopId">调出店铺：</label>
                <select id="shopObj1_shopId" name="shopObj1.shopId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(Shop shopTemp:shopList) {
	 					String selected = "";
 					if(shopObj1!=null && shopObj1.getShopId()!=null && shopObj1.getShopId().intValue()==shopTemp.getShopId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=shopTemp.getShopId() %>" <%=selected %>><%=shopTemp.getShopName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
            <div class="form-group">
            	<label for="shopObj2_shopId">调入店铺：</label>
                <select id="shopObj2_shopId" name="shopObj2.shopId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(Shop shopTemp:shopList) {
	 					String selected = "";
 					if(shopObj2!=null && shopObj2.getShopId()!=null && shopObj2.getShopId().intValue()==shopTemp.getShopId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=shopTemp.getShopId() %>" <%=selected %>><%=shopTemp.getShopName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="transferTime">调拨时间:</label>
				<input type="text" id="transferTime" name="transferTime" class="form-control"  placeholder="请选择调拨时间" value="<%=transferTime %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="transferEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;商品调拨信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="transferEditForm" id="transferEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="transfer_transferId_edit" class="col-md-3 text-right">调拨id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="transfer_transferId_edit" name="transfer.transferId" class="form-control" placeholder="请输入调拨id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="transfer_productObj_productId_edit" class="col-md-3 text-right">调拨商品:</label>
		  	 <div class="col-md-9">
			    <select id="transfer_productObj_productId_edit" name="transfer.productObj.productId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="transfer_transferCount_edit" class="col-md-3 text-right">调拨数量:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="transfer_transferCount_edit" name="transfer.transferCount" class="form-control" placeholder="请输入调拨数量">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="transfer_shopObj1_shopId_edit" class="col-md-3 text-right">调出店铺:</label>
		  	 <div class="col-md-9">
			    <select id="transfer_shopObj1_shopId_edit" name="transfer.shopObj1.shopId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="transfer_shopObj2_shopId_edit" class="col-md-3 text-right">调入店铺:</label>
		  	 <div class="col-md-9">
			    <select id="transfer_shopObj2_shopId_edit" name="transfer.shopObj2.shopId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="transfer_transferTime_edit" class="col-md-3 text-right">调拨时间:</label>
		  	 <div class="col-md-9">
                <div class="input-group date transfer_transferTime_edit col-md-12" data-link-field="transfer_transferTime_edit">
                    <input class="form-control" id="transfer_transferTime_edit" name="transfer.transferTime" size="16" type="text" value="" placeholder="请选择调拨时间" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="transfer_memo_edit" class="col-md-3 text-right">调拨备注:</label>
		  	 <div class="col-md-9">
			    <textarea id="transfer_memo_edit" name="transfer.memo" rows="8" class="form-control" placeholder="请输入调拨备注"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#transferEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxTransferModify();">提交</button>
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
    document.transferQueryForm.currentPage.value = currentPage;
    document.transferQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.transferQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.transferQueryForm.currentPage.value = pageValue;
    documenttransferQueryForm.submit();
}

/*弹出修改商品调拨界面并初始化数据*/
function transferEdit(transferId) {
	$.ajax({
		url :  basePath + "Transfer/" + transferId + "/update",
		type : "get",
		dataType: "json",
		success : function (transfer, response, status) {
			if (transfer) {
				$("#transfer_transferId_edit").val(transfer.transferId);
				$.ajax({
					url: basePath + "Product/listAll",
					type: "get",
					success: function(products,response,status) { 
						$("#transfer_productObj_productId_edit").empty();
						var html="";
		        		$(products).each(function(i,product){
		        			html += "<option value='" + product.productId + "'>" + product.productName + "</option>";
		        		});
		        		$("#transfer_productObj_productId_edit").html(html);
		        		$("#transfer_productObj_productId_edit").val(transfer.productObjPri);
					}
				});
				$("#transfer_transferCount_edit").val(transfer.transferCount);
				$.ajax({
					url: basePath + "Shop/listAll",
					type: "get",
					success: function(shops,response,status) { 
						$("#transfer_shopObj1_shopId_edit").empty();
						var html="";
		        		$(shops).each(function(i,shop){
		        			html += "<option value='" + shop.shopId + "'>" + shop.shopName + "</option>";
		        		});
		        		$("#transfer_shopObj1_shopId_edit").html(html);
		        		$("#transfer_shopObj1_shopId_edit").val(transfer.shopObj1Pri);
					}
				});
				$.ajax({
					url: basePath + "Shop/listAll",
					type: "get",
					success: function(shops,response,status) { 
						$("#transfer_shopObj2_shopId_edit").empty();
						var html="";
		        		$(shops).each(function(i,shop){
		        			html += "<option value='" + shop.shopId + "'>" + shop.shopName + "</option>";
		        		});
		        		$("#transfer_shopObj2_shopId_edit").html(html);
		        		$("#transfer_shopObj2_shopId_edit").val(transfer.shopObj2Pri);
					}
				});
				$("#transfer_transferTime_edit").val(transfer.transferTime);
				$("#transfer_memo_edit").val(transfer.memo);
				$('#transferEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除商品调拨信息*/
function transferDelete(transferId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Transfer/deletes",
			data : {
				transferIds : transferId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#transferQueryForm").submit();
					//location.href= basePath + "Transfer/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交商品调拨信息表单给服务器端修改*/
function ajaxTransferModify() {
	$.ajax({
		url :  basePath + "Transfer/" + $("#transfer_transferId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#transferEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#transferQueryForm").submit();
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

    /*调拨时间组件*/
    $('.transfer_transferTime_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd hh:ii:ss',
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


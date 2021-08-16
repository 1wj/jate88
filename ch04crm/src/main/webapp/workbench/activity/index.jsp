<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<%--bootStrap引用--%>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<%--日历插件--%>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<%--分页按钮插件--%>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>



	<script type="text/javascript">

	$(function(){
		//为【创建】按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function (){

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"

			});
			/*
            操作模态窗口的方式：
                需要操作的 【模态窗口】 的jQuery对象，调用model方法，为该方式传递参数
                show：打开模态窗口，hide：关闭模态窗口
         */
			//走后台，目的是为了取得用户信息列表，为所有者下拉框铺值
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"post",
				dataType:"json",
				success : function (data){

					//[{"id":?,"name":?....},{},{}]
					var html="<option></option>";
					$.each(data,function (i,n){
						//遍历出来的每一个n，就是每一个user对象
						html += "<option value='"+n.id+"'>"+n.name+"</option>"
					})
					$("#create-owner").html(html);
					/*下拉框默认选中张三
					* 取得当前登录用户的id
					* 在js中使用el表达式，el表达式一定要套用在字符串中
					* */
					var id="${user.id}";
					$("#create-owner").val(id);
					//当所有下拉框处理完毕之后，展现模态窗口
					$("#createActivityModal").modal("show");
				}
			})
		})

		//为【保存】按钮绑定事件，执行添加操作
		$("#saveBtn").click(function (){
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
						"owner" : $.trim($("#create-owner").val()),
						"name" : $.trim($("#create-name").val()),
						"startDate" : $.trim($("#create-startDate").val()),
						"endDate" : $.trim($("#create-endDate").val()),
						"cost" : $.trim($("#create-cost").val()),
						"description" : $.trim($("#create-description").val())
					},
				type: "post",
				dataType: "json",
				success : function (data){
					/*
						data {"success":true/false}
					 */
					if(data.success){
						//添加成功后 刷新市场活动列表信息(局部刷新)
						//清空添加操作模态窗口的数据  ，$("#acitvityAddForm").submit()是无效的，一个坑，所以用dom对象
						var ss=$("#activityAddForm")[0];
						ss.reset();

						//关闭模态窗口
						$("#createActivityModal").modal("hide");
						pageList(1,2);
					}else {
						alert("添加市场活动列表失败");
					}
				}
			})
		})

	/*	【页面加载完毕后】触发一个方法【分页】，页面加载完毕后，就会弹出默认展开列表的 【第一页】，【每页两条】*/
		pageList(1,2);

		//为【查询】【按钮】绑定事件，触发pageList方法
		$("#searchBtn").click(function (){
			/*
				点击查询按钮的时候，我们应该将搜索框中的信息保存起来，保存到隐藏域中
			 */
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1,2);
		})

		//为【全选】的复选框绑定事件
		$("#qx").click(function (){
			$("input[name=xz]").prop("checked",this.checked);
		});

		/*  【单选按钮】点亮【全选】按钮
			因为动态生成的元素，是不能以普通绑定事件的形式来进行操作的，动态生成的元素，我们要以【on方法】的形式来触发事件
			语法：$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jQuery对象，回调函数)
		 */
		$("#activityBody").on("click",$("input[name=xz]"),function (){
			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);
		})

		//为【删除按钮】绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function (){
			var $xz=$("input[name=xz]:checked");
			if($xz.length==0){
				alert("请选择要删除的记录");
			//肯定选了，参数不知道有几条
			}else {
				//url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx...
				//拼接参数
				if(confirm("确定要删除所选中的记录吗？")){
					var param="";
					for (var i = 0; i < $xz.length; i++) {
						param+="id="+$($xz[i]).val();
						if(i < $xz.length-1){
							param+="&";
						}
					}
					$.ajax({
						url :"workbench/activity/delete.do",
						type : "post",
						data : param,
						dataType : "json",
						success : function (data){
							// data:{"success": true/false}
							if(data.success){

								pageList(1,2);
							}else {
								alert("删除市场活动失败");
							}
						}
					})
				}
			}

		})

		//为【修改按钮绑定事件】，打开修改操作的模态窗口
		$("#editBtn").click(function (){
			var $xz= $("input[name=xz]:checked");

			if($xz.length==0){
				alert("请选择需要修改的记录");
			}else if($xz.length > 1){
				alert("只能选择一条记录进行修改");
			}else {
				//  date：{"uList":[{用户1},{2}],"a":{市场活动}}
				var id=$xz.val();

				$.ajax({
					url : "workbench/activity/getUserListAndActivity.do",
					data : {
						"id" : id
					},
					type : "get",
					dataType : "json",
					success : function (data){
						alert("成功进入");
						//处理所有者下拉框
						var html="<option></option>";
						$.each(data.uList,function (i,n){//每一个n就是一个用户对象
							html+="<option selected value='"+n.id+"'>"+n.name+"</option>";
						})
						$("#edit-owner").html(html);

						//处理单条activity
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);

						//打开修改的模态窗口
						$("#editActivityModal").modal("show");
					}
				})

			}

		})
	});

	/*
		pageList方法：就是发出ajax请求，从后台取的最新的市场活动信息列表数据，通过响应回来的数据，局部刷新市场活动信息列表。
			①：点击左侧菜单中“市场活动”超链接  ②：添加，修改，删除后，需要刷新
			③点击查询按钮的时候               ④点击分页组件的时候
	 */
		function pageList(pageNo,pageSize){
			//将全新的复选框的√干掉
			$("#qx").prop("checked",false);
			//alert(1);
			//查询前，将隐藏域中保存的信息取出啦，重新赋值到搜素框中
			$("#search-name").val($.trim($("#hidden-name").val()));
			$("#search-owner").val($.trim($("#hidden-owner").val()));
			$("#search-startDate").val($.trim($("#hidden-startDate").val()));
			$("#search-endDate").val($.trim($("#hidden-endDate").val()));

			$.ajax({
				url : "workbench/activity/pageList.do",
				type: "get",
				data: {
					"pageNo": pageNo,
					"pageSize": pageSize,
					"name" : $.trim($("#search-name").val()),
					"owner" : $.trim($("#search-owner").val()),
					"startDate" : $.trim($("#search-startDate").val()),
					"endDate" : $.trim($("#search-endDate").val())
				},
				dateType : "json",
				success : function (data){

					/*
						我们需要的；	[{市场活动1},{},{}]
						分页插件需要的查询出来的总记录数： {"total":100}
						合起来： {“total”:100,"dataList":[{市场活动1},{},{}]}
					 */
					var html="";
					//每一个n就是一个市场活动对象
					$.each(data.dataList,function (i,n){

						html+='<tr class="active">';
						html+='<td><input type="checkbox" value="'+n.id+'" name="xz" /></td>';
						html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.jsp\';">'+n.name+'</a></td>';
						html+='<td>'+n.owner+'</td>';
						html+='<td>'+n.startDate+'</td>';
						html+='<td>'+n.endDate+'</td>';
						html+='</tr>';
					})
					$("#activityBody").html(html);
					//计算总页数
					var totalPages = data.total % pageSize == 0 ? data.total/pageSize : parseInt(data.total/pageSize)+1;
					//数据处理完毕后，结合分页查询，对前端展现分页信息
					$("#activityPage").bs_pagination({
						currentPage: pageNo, // 页码
						rowsPerPage: pageSize, // 每页显示的记录条数
						maxRowsPerPage: 20, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数

						visiblePageLinks: 3, // 显示几个卡片

						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,
						/*该回调函数是在点击分页组件的时候触发的*/
						onChangePage : function(event, data){
							pageList(data.currentPage , data.rowsPerPage);
						}
					});


				}
			})

		}
</script>
</head>
<body>

	<%--隐藏域项目经验--%>
		<input type="hidden" id="hidden-name">
		<input type="hidden" id="hidden-owner">
		<input type="hidden" id="hidden-startDate">
		<input type="hidden" id="hidden-endDate">


	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
						<input type="reset">
						<div class="form-group">		<%--create-marketActivityOwner 我改了id--%>
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name"><%--create-marketActivityName--%>
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate"><%--create-startTime--%>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate"><%--create-endTime--%>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea><%--create-describe--%>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--
						data-dismiss="modal"
							表示关闭模态窗口
					-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time " id="edit-startDate" ><%--value="2020-10-10"--%>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" ><%--value="2020-10-20"--%>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%--
									关于文本域textarea：
										①一定要以标签对的形式来呈现，正常情况下要紧紧的挨着
										②textarea虽然是以标签对的形式来呈现的，但是它也是属于表单元素范畴
										我们所有的对于textarea的取值和赋值，应该统一使用val()方法，(而不是html()方法)
								--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button><%--关闭模态窗口data-dismiss="modal"--%>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表123</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		<%--模糊查询--%>
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" /><%-- id="startTime" --%>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate" /><%-- id="endTime"--%>
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>

			<%--三个按钮 创建，修改，删除--%>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<%--
						点击创建按钮，观察两个属性和属性值
						data-toggle="modal"：
							表示触发该按钮，将要打开一个模态窗口
						data-target="#createActivityModal"：
							表示要打开哪个模态窗口，通过#id的形式找到该窗口

						现在我们是以属性和属性值的方式写在了button元素中，用来打开模态窗口
						但是这样做是有问题的：
							问题在于没有办法对按钮的功能进行扩充
						所以未来的实际项目开发，对于触发模态窗口的操作，一定不要写死在元素当中
						应该由我们自己写js代码来操作
					--%>
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary"  id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<%--市场活动列表展示--%>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					</tbody>
				</table>
			</div>
			<%--分页按钮--%>
			<div style="height: 50px; position: relative;top: 30px;">
			<%--之前的不用了，用新的--%>
				<div id="activityPage">

				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>
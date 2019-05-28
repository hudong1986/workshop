<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!-- PAGE LEVEL STYLES -->
<link href="<s:url value="/assets" />/css/bootstrap-fileupload.min.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="<s:url value="/assets" />/bootstrapvalidator/css/bootstrapValidator.css" />
<!-- PAGE LEVEL SCRIPTS -->
<script src="<s:url value="/assets" />/js/bootstrap-fileupload.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/bootstrapvalidator/js/bootstrapValidator.js"></script>

<script>
	function getAllCheck() {
		var list = "";
		var checkboxs = document.getElementsByName("ischeck");
		for (var i = 0; i < checkboxs.length; i++) {
			var e = checkboxs[i];
			if (e.checked) {

				list += e.value + ",";

			}
		}

		if (list != "") {
			return list.substr(0, list.length - 1);
		} else {
			return "";
		}
	}

	function SelectAll() {
		var checkboxs = document.getElementsByName("ischeck");
		for (var i = 0; i < checkboxs.length; i++) {
			var e = checkboxs[i];
			e.checked = !e.checked;
		}
	}

	var editId;
	function del(id) {
		editId = id;
		if (confirm("删除将不能恢复，确认要删除吗？")) {
			$.post("<s:url value="/goods_in_out/del" />", {
				"id" : id,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("当前操作部分成功");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		}

	}

	$(function() {
		
		//新增信息
		$("#btn_add").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#addForm').data('bootstrapValidator').validate();
			if (!$('#addForm').data('bootstrapValidator').isValid()) {
				return;
			}

			$.post("<s:url value="/goods_in_out/add" />", {
				"add_direction":$("#add_direction").val(),
				"add_batchno":$("#add_batchno").val(),
				"add_product_id":$("#add_product_id").val(),
				"add_num":$("#add_num").val(),
				"add_total":$("#add_total").val(),
				"add_out_business_name":$("#add_out_business_name").val(),
				"add_out_business_address":$("#add_out_business_address").val(),
				"add_out_business_phone":$("#add_out_business_phone").val()
			}, function(data, status) {
				if (data.code == 1) {
					alert("添加成功");
					$("#searchForm").submit();
				} else {
					$("#add_msg").html(data.retMsg);
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		});
		
		//新增管理员前台校验  
		$("#addForm").bootstrapValidator({
			message : 'This value is not valid',
			//反馈图标  
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {

				add_batchno : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}

					}
				},
				add_num : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}

					}
				},
				add_total : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}

					}
				}
			}
		});
	});
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">产品出入库列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/goods_in_out/list" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="searchForm">
						<div class="form-group">
							产品去向： <select class="form-control" name="direction">
								<option value="出库"
									${direction=="出库" ? "selected=selected" : "" }>出库</option>
								<option value="入库"
									${direction=="入库" ? "selected=selected" : "" }>入库</option>
							</select>

						</div>

						<div class="form-group">
							产品类型： <select class="form-control" name="product_id">
								<option value="-1" ${product_id==-1 ? "selected=selected" : "" }>全部</option>
								<c:forEach var="item" items="${productlist }">

									<option value="${item.id }"
										${product_id==item.id ? "selected=selected" : "" }>${item.productName }</option>
								</c:forEach>

							</select>

						</div>
						<div class="form-group">
							日期：从<input type="text" class="form-control" value="${beg_time}"
								placeholder="" name="beg_time" id="beg_time"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							到<input type="text" class="form-control" value="${end_time}"
								placeholder="" name="end_time" id="end_time"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />

						</div>

						<div class="form-group">
							批次号：<input type="text" class="form-control" value="${batchNo}"
								placeholder="" name="batchNo" id="batchNo" />
						</div>

						<button type="submit" class="btn btn-default">查询</button>

					</sf:form>

				</div>
				<div class="panel-heading form-inline">
					<sec:authorize
						access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')">
						<button type="button" id="add" class="btn btn-sm btn-default"
						data-toggle="modal" data-target="#addModal"
						>添加</button>
					</sec:authorize>

				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>

									<th><a
										href="<s:url value="/goods_in_out/list?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>批次号</th>
									<th>商品名</th>
									<th>数量</th>
									<th>总额(元)</th>
									<th>出库/入库</th>
									<th>对方厂家</th>
									<th>对方厂家地址</th>
									<th>对方厂家电话</th>
									<th>添加人</th>
									<th><a
										href="<s:url value="/goods_in_out/list?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>添加时间
									</a></th>
									<th>#</th>

								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>

												<td><c:out value="${item.id}" /></td>
												<td><c:out value="${item.batchno}" /></td>
												<td><c:out value="${item.productName}" /></td>
												<td><c:out value="${item.num}" /></td>
												<td><c:out value="${item.total}" /></td>
												<td><c:out value="${item.direction}" /></td>
												<td><c:out value="${item.outBusinessName}" /></td>
												<td><c:out value="${item.outBusinessAddress}" /></td>
												<td><c:out value="${item.outBusinessPhone}" /></td>
												<td><c:out value="${item.addUserName}" /></td>
												<td><fmt:formatDate value="${item.addtime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><sec:authorize
														access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')">
														<button type="button" onclick="del('${item.id}')"
															class="btn btn-xs btn-default">删除</button>
													</sec:authorize></td>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan=10>对不起，未找到有效数据！</td>
										</tr>
									</c:otherwise>
								</c:choose>



							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- End  Basic Table  -->
			<div class="col-md-8">
				总条数:${pager.totalCount},当前显示条目:${pager.pageSize}</div>
			<div class="col-md-4">
				<nav aria-label="...">
					<ul class="pager">
						<li><a
							href="<s:url value="/goods_in_out/list?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/goods_in_out/list?${pager.nextPageString}" />">下一页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>

	<div class="modal fade" id="addModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">产品出入库</h4>
				</div>
				<div class="modal-body" id="addForm">
					<div class="form-group">
						<label class="control-label">产品去向</label> <select
							class="form-control" name="add_direction" id="add_direction">
							<option value="出库">出库</option>
							<option value="入库">入库</option>
						</select>

					</div>
					<div class="form-group">
						<label class="control-label">产品类型</label> <select
							class="form-control" name="add_product_id" id="add_product_id">
							<c:forEach var="item" items="${productlist }">
								<option value="${item.id }">${item.productName }</option>
							</c:forEach>

						</select>
					</div>

					<div class="form-group">
						<label class="control-label">批次号</label> <input
							class="form-control" type="text" maxLength="30" id="add_batchno"
							name="add_batchno">
					</div>
					<div class="form-group">
						<label class="control-label">数量</label> <input
							class="form-control" type="text" maxLength="5" id="add_num"
							name="add_num">
					</div>
					<div class="form-group">
						<label class="control-label">总金额(元)</label> <input
							class="form-control" type="text" maxLength="6" id="add_total"
							name="add_total">
					</div>
					<div class="form-group">
						<label class="control-label">对方厂家</label> <input
							class="form-control" type="text" maxLength="6"
							id="add_out_business_name" name="add_out_business_name">
					</div>
					<div class="form-group">
						<label class="control-label">对方厂家地址</label> <input
							class="form-control" type="text" maxLength="6"
							id="add_out_business_address" name="add_out_business_address">
					</div>
					<div class="form-group">
						<label class="control-label">对方厂家电话</label> <input
							class="form-control" type="text" maxLength="50"
							id="add_out_business_phone" name="add_out_business_phone">
					</div>


					<div id="add_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_add">提交</button>
				</div>
			</div>
		</div>
	</div>
</div>
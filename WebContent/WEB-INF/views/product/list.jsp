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
	function edit(id, name, outin) {
		editId = id;
		$("#productname").val(name);
		$("#edit_outin").val(outin);

		$("#editModal").modal('show');
	}

	$(function() {

		//新增信息
		$("#btn_add").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#addForm').data('bootstrapValidator').validate();
			if (!$('#addForm').data('bootstrapValidator').isValid()) {
				return;
			}

			$.post("<s:url value="/product/add" />", {
				"type":$("#add_type").val(),
				"name" : $("#add_productname").val(),
				"out_in" : $("#add_outin").val(),

				"${_csrf.parameterName}" : "${_csrf.token}"
			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					window.location.reload(true);
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		});
		
		//修改信息
		$("#btn_edit").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#editForm').data('bootstrapValidator').validate();
			if (!$('#editForm').data('bootstrapValidator').isValid()) {
				return;
			}

			$.post("<s:url value="/product/edit" />", {
				"id" : editId,
				"out_in" : $("#edit_outin").val(),

				"${_csrf.parameterName}" : "${_csrf.token}"
			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					window.location.reload(true);
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		});

		//新增管理员前台校验  
		$("#editForm").bootstrapValidator({
			message : 'This value is not valid',
			//反馈图标  
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {

				edit_outin : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}

					}
				}
			}
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

				add_productname : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}

					}
				},
				edit_outin : {
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
			<h1 class="page-head-line">产品列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">

				<div class="panel-heading">
				<sec:authorize access="hasAnyRole('HrManager','HrAssistant')">
					<button type="button" id="btnAdd" class="btn btn-default"
						data-toggle="modal" data-target="#addModal">添加</button>
						</sec:authorize>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th>ID</th>
									<th>产品类型</th>
									<th>产品名</th>
									<th>工人每件提成(元)</th>
									<th>#</th>

								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>
												<td><c:out value="${item.id}" /></td>
												<td><c:out value="${item.type==0 ? '自生产' : '从外部进'}" /></td>
												<td><c:out value="${item.productName}" /></td>
												<td><c:out value="${item.outIn}" /></td>
												<td><c:if test="${item.type==0 }">


														<sec:authorize
															access="hasAnyRole('HrManager','HrAssistant')">
															<button type="button"
																onclick="edit('${item.id}','${item.productName}','${item.outIn}')"
																class="btn btn-xs btn-default">修改提成</button>
														</sec:authorize>
													</c:if></td>
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

		</div>
	</div>

	<div class="modal fade" id="addModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">添加新产品</h4>
				</div>
				<div class="modal-body" id="addForm">
					<div class="form-group">
						<label class="control-label">产品分类</label> <select
							class="form-control" name="add_type" id="add_type">
							<option value="0">自生产</option>
							<option value="1">从外部进</option>
						</select>
					</div>

					<div class="form-group">
						<label class="control-label">产品名</label> <input
							class="form-control" type="text" maxLength="20"
							id="add_productname"  name="add_productname">
					</div>
					<div class="form-group">
						<label class="control-label">提成(元/件)</label> <input
							class="form-control" type="text" maxlength="3" id="add_outin" 
							placeholder="如（1、0.5等）,外部进的产品此项不填" name="add_outin">
					</div>


					<div id="upload_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_add">提交</button>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="editModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">修改产品的记件提成</h4>
				</div>
				<div class="modal-body" id="editForm">

					<div class="form-group">
						<label class="control-label">产品名</label> <input
							class="form-control" type="text" id="productname" disabled>
					</div>
					<div class="form-group">
						<label class="control-label">工人生产提成(元/件)</label> <input
							class="form-control" type="text" maxlength="3" id="edit_outin"
							placeholder="如（1、0.5等）" name="edit_outin">
					</div>


					<div id="upload_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_edit">提交</button>
				</div>
			</div>
		</div>
	</div>
</div>
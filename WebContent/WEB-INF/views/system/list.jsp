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
	function edit(id,key,value,remark){
		editId=id;
		$("#edit_key").val(key);
		$("#edit_value").val(value);
		$("#edit_remark").val(remark);
		
		$("#editModal").modal('show');
	}
	 
	$(function() {
		 
		
		//修改个人信息
		$("#btn_edit").click(function() {
			
			/*手动验证表单，当是普通按钮时。*/
			$('#editForm').data('bootstrapValidator').validate();
			if (!$('#editForm').data('bootstrapValidator').isValid()) {
				return;
			}
			
			$.post("<s:url value="/system/edit" />", {
				"id" : editId,
				"edit_value" : $("#edit_value").val(),
				"edit_remark" : $("#edit_remark").val(),
				 
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
				 
				edit_value : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}
						 
					}
				},
				add_fix_money : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						},
						/* stringLength : {
							min : 11,
							max : 11,
							message : '手机号长度为11位'
						}, */
						regexp : {
							regexp : '^[1-9]{0,5}$',
							message : '0-5位整数'
						}
					}
				},
				edit_remark : {
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
			<h1 class="page-head-line">系统参数</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				 
				
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th>ID</th>
									<th>参数名</th>
									<th>参数值</th>
									<th>参数描述</th>
									<th>#</th>

								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>
												<td><c:out value="${item.id}" /></td>
												<td><c:out value="${item.sysKey}" /></td>
												<td><c:out value="${item.sysValue}" /></td>
												<td><c:out value="${item.remark}" /></td>
												<td><sec:authorize
														access="hasAnyRole('HrManager','HrAssistant')">
														<button type="button"
															onclick="edit('${item.id}','${item.sysKey}','${item.sysValue}','${item.remark}')"
															class="btn btn-xs btn-default">修改</button>
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
			 
		</div>
	</div>

	 


	<div class="modal fade" id="editModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">修改参数</h4>
				</div>
				<div class="modal-body" id="editForm">

					<div class="form-group">
						<label class="control-label">参数KEY</label> <input
							class="form-control" type="text" maxlength="20" id="edit_key"
							placeholder="" name="edit_key" disabled>
					</div>
					
					<div class="form-group">
						<label class="control-label">参数值</label> <input
							class="form-control" type="text" maxlength="20" id="edit_value"
							placeholder="" name="edit_value" required>
					</div>
					 
					 	<div class="form-group">
						<label class="control-label">参数备注</label> <input
							class="form-control" type="text" maxlength="100" id="edit_remark"
							placeholder="" name="edit_remark" required>
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
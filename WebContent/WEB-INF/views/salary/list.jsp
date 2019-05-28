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
			$.post("<s:url value="/salary/del" />", {
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
	
	function get(id) {
		editId = id;
		 
			$.post("<s:url value="/salary/get" />", {
				"id" : id,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				 $("#edit_employee").val(data.userName);
				 $("#edit_fix_money").val(data.fixMoney);
				 $("#edit_fix_des").val(data.fixMoneyDes);
				 $("#edit_jijian_money").val(data.achievement);
				 $("#edit_jijian_money_des").val(data.achievementDes);
				 $("#edit_addwork_money").val(data.addWork);
				 $("#edit_addwork_money_des").val(data.addWorkDes);
				 $("#edit_reward_money").val(data.rewardMoney);
				 $("#edit_reward_money_des").val(data.rewardDes);
				 $("#edit_fuli_money").val(data.fuliMoney);
				 $("#edit_fuli_money_des").val(data.fuliDes);
				 $("#edit_holiday_money").val(data.holidayReduce);
				 $("#edit_holiday_money_des").val(data.holidayDes);
				 $("#edit_other_add_money").val(data.otherAdd);
				 $("#edit_other_add_money_des").val(data.otherAddDes);
				 $("#edit_other_reduce_money").val(data.otherReduce);
				 $("#edit_other_reduce_money_des").val(data.otherReduceDes); 

				
				$('#editSalaryModal').modal('show');
			}).error(function(data) {
				alert("请求失败!");
			});
	}
	

	$(function() {
		
		$("#edit_salary").click(function() {

			/* 手动验证表单，当是普通按钮时。 */
			$('#editSalaryForm').data('bootstrapValidator').validate();
			if (!$('#editSalaryForm').data('bootstrapValidator').isValid()) {
				return;
			}

			// 开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
			var formData = new FormData();

			 formData.append("id", editId);
			 formData.append("edit_fix_money",$("#edit_fix_money").val());
			 formData.append("edit_fix_des",$("#edit_fix_des").val());
			 formData.append("edit_jijian_money",$("#edit_jijian_money").val());
			 formData.append("edit_jijian_money_des",$("#edit_jijian_money_des").val());
			 formData.append("edit_addwork_money",$("#edit_addwork_money").val());
			 formData.append("edit_addwork_money_des",$("#edit_addwork_money_des").val());
			 formData.append("edit_reward_money",$("#edit_reward_money").val());
			 formData.append("edit_reward_money_des",$("#edit_reward_money_des").val());
			 formData.append("edit_fuli_money",$("#edit_fuli_money").val());
			 formData.append("edit_fuli_money_des",$("#edit_fuli_money_des").val());;
			 formData.append("edit_holiday_money",$("#edit_holiday_money").val());
			 formData.append("edit_holiday_money_des",$("#edit_holiday_money_des").val());
			 formData.append("edit_other_add_money",$("#edit_other_add_money").val());
			 formData.append("edit_other_add_money_des",$("#edit_other_add_money_des").val());
			 formData.append("edit_other_reduce_money",$("#edit_other_reduce_money").val());
			 formData.append("edit_other_reduce_money_des",$("#edit_other_reduce_money_des").val());

			$.ajax({
				url : "<s:url value="/salary/edit" />",
				type : 'POST',
				data : formData,
				// 告诉jQuery不要去处理发送的数据
				processData : false,
				// 告诉jQuery不要去设置Content-Type请求头
				contentType : false,
				beforeSend : function() {
					$("#salary_msg").html("正在修改，请不要关闭弹出框........");
				},
				success : function(data) {
					if (data.code == 1) {
						alert("修改成功！");
						$("#searchForm").submit();
					} else {
						$("#salary_msg").html(data.retMsg);
					}
				},
				error : function(responseStr) {
					$("#salary_msg").html("提交失败");
				}
			});

		});
		// 新增管理员前台校验
		$("#editSalaryForm").bootstrapValidator({
			message : 'This value is not valid',
			// 反馈图标
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				edit_fix_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				edit_jijian_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				edit_addwork_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				edit_reward_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				edit_fuli_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				edit_holiday_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				edit_other_add_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				edit_other_reduce_money : {
					message : '输入无效',
					validators : {
						notEmpty : {
							message : '金额不能为空'
						}
					}
				},
				 
			}
		});
	});
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">员工薪水</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/salary/list" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="searchForm">

						<div class="form-group">

							<select class="form-control" name="up_down_id">
								 
								<c:forEach var="item" items="${mydept }">
									<option value="${item.upDownId }"
										${up_down_id==item.upDownId ? "selected=selected" : "" }>${item.deptName }</option>
								</c:forEach>

							</select>

						</div>

						<div class="form-group">
							<label for="phone"></label> <input type="text"
								class="form-control" id="phone" name="phone" value="${phone}"
								placeholder="请输入手机号">
						</div>
						<div class="form-group">
							<label for="user_name"></label> <input type="text"
								class="form-control" id="user_name" name="user_name"
								value="${user_name}" placeholder="请输入姓名">
						</div>

						<div class="form-group">
							<label for="cus_name">月份</label> <input type="text"
								class="form-control" value="${obj_month}" placeholder=""
								name="obj_month" id="obj_month"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM'});" />
							 
						</div>
						 
						<button type="submit" class="btn btn-default">查询</button>
					<s:url value="/salary/export" var="exportUrl">
						<s:param name="up_down_id" value="${up_down_id }" />
						<s:param name="phone" value="${phone }" />
						<s:param name="obj_month" value="${obj_month }" />
						<s:param name="user_name" value="${user_name }" />
					</s:url>
					<a href="${exportUrl }" target="_blank"><button type="button"
							class="btn btn-default">导出到Excel</button></a>
					</sf:form>

				</div>
				 
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th><input type="checkbox" value="${cus.id}"
										onclick="SelectAll();" /></th>
									<th><a
										href="<s:url value="/salary/list?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>月份</th>
									<th>部门</th>
									<th>手机号</th>
									<th>姓名</th>
									<th>基本薪水</th>
									<th>基本薪水说明</th>
									<th>记件提成</th>
									<th>记件详情</th>
									<th>加班工资</th>
									<th>加班详情</th>
									<th>奖励</th>
									<th>奖励详情</th>
									<th>事假扣钱</th>
									<th>事假详情</th>
									<th>福利</th>
									<th>福利详情</th>
									<th>其它加薪资</th>
									<th>其它加薪资详情</th>
									<th>其它扣薪资</th>
									<th>其它扣薪资详情</th>
									<th>总额(元)</th>
									<th>添加人</th>
									<th>#</th>

								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>
												<td><input type="checkbox" value="${item.id}"
													name="ischeck" /></td>
												<td><c:out value="${item.id}" /></td>
												<td><c:out value="${item.month}" /></td>
												<td><c:out value="${item.deptName}" /></td>
												<td><c:out value="${item.phone}" /></td>
												<td><c:out value="${item.userName}" /></td>
												<td><c:out value="${item.fixMoney}" /></td>
												<td><c:out value="${item.fixMoneyDes}" /></td>
												<td><c:out value="${item.achievement}" /></td>
												<td><c:out value="${item.achievementDes}" /></td>
												<td><c:out value="${item.addWork}" /></td>
												<td><c:out value="${item.addWorkDes}" /></td>
												<td><c:out value="${item.rewardMoney}" /></td>
												<td><c:out value="${item.rewardDes}" /></td>
												<td><c:out value="${item.holidayReduce}" /></td>
												<td><c:out value="${item.holidayDes}" /></td>
												<td><c:out value="${item.fuliMoney}" /></td>
												<td><c:out value="${item.fuliDes}" /></td>
												<td><c:out value="${item.otherAdd}" /></td>
												<td><c:out value="${item.otherAddDes}" /></td>
												<td><c:out value="${item.otherReduce}" /></td>
												<td><c:out value="${item.otherReduceDes}" /></td>
												<td><c:out value="${item.total}" /></td>
												<td><c:out value="${item.addUserName}" /></td>
												<td><sec:authorize
														access="hasAnyRole('HrManager','HrAssistant')">
														<button type="button" onclick="del('${item.id}')"
															class="btn btn-xs btn-default">删除</button>
														<button type="button" onclick="get('${item.id}')"
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
			<div class="col-md-8">
				总条数:${pager.totalCount},当前显示条目:${pager.pageSize}</div>
			<div class="col-md-4">
				<nav aria-label="...">
					<ul class="pager">
						<li><a
							href="<s:url value="/salary/list?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/salary/list?${pager.nextPageString}" />">下一页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>

<div class="modal fade" id="editSalaryModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">修改薪水</h4>
				</div>
				<div class="modal-body" id="editSalaryForm">
					<div class="form-group">
						<label for="control-label">当前员工</label> 
						 <input type="text"
							class="form-control"  placeholder="" id="edit_employee" disabled />
						 
					</div>
					 <div class="form-group">
						<label class="control-label">基本薪水(元)</label> <input
							class="form-control" type="text"  
							id="edit_fix_money" name="edit_fix_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">基本薪水说明</label> <input
							class="form-control" type="text"  
							id="edit_fix_des" name="edit_fix_des" placeholder="">
				  	</div>
				  	 <div class="form-group">
						<label class="control-label">记件提成(元)</label> <input
							class="form-control" type="text"  
							id="edit_jijian_money" name="edit_jijian_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">记件提成详情</label>
						 <textarea
							class="form-control"   rows="5"
							id="edit_jijian_money_des" name="edit_jijian_money_des" placeholder="" ></textarea>
				  	</div>
				  	 <div class="form-group">
						<label class="control-label">加班工资(元)</label> <input
							class="form-control" type="text"  
							id="edit_addwork_money" name="edit_addwork_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">加班工资详情</label>  <textarea
							class="form-control"  rows="5"
							id="edit_addwork_money_des" name="edit_addwork_money_des" placeholder=""></textarea>
				  	</div>
				  	 <div class="form-group">
						<label class="control-label">奖励(元)</label> <input
							class="form-control" type="text"  
							id="edit_reward_money" name="edit_reward_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">奖励详情</label> <input
							class="form-control" type="text"  
							id="edit_reward_money_des" name="edit_reward_money_des" placeholder="">
				  	</div>
				  	 <div class="form-group">
						<label class="control-label">福利元)</label> <input
							class="form-control" type="text"  
							id="edit_fuli_money" name="edit_fuli_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">福利详情</label> <input
							class="form-control" type="text"  
							id="edit_fuli_money_des" name="edit_fuli_money_des" placeholder="">
				  	</div>
				  	 <div class="form-group">
						<label class="control-label">事假扣钱(元)</label> <input
							class="form-control" type="text"  
							id="edit_holiday_money" name="edit_holiday_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">事假详情</label> <input
							class="form-control" type="text"  
							id="edit_holiday_money_des" name="edit_holiday_money_des" placeholder="">
				  	</div>
				  	 <div class="form-group">
						<label class="control-label">其它加工资(元)</label> <input
							class="form-control" type="text"  
							id="edit_other_add_money" name="edit_other_add_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">其它加工资说明</label> <input
							class="form-control" type="text"  
							id="edit_other_add_money_des" name="edit_other_add_money_des" placeholder="">
				  	</div>
				  	 <div class="form-group">
						<label class="control-label">其它扣工资(元)</label> <input
							class="form-control" type="text"  
							id="edit_other_reduce_money" name="edit_other_reduce_money" placeholder="">
					</div>
					 <div class="form-group">
						<label class="control-label">其它扣工资说明</label> <input
							class="form-control" type="text"  
							id="edit_other_reduce_money_des" name="edit_other_reduce_money_des" placeholder="">
				  	</div>
					 

					<div id="salary_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="edit_salary">提交</button>
				</div>
			</div>
		</div>
	</div>
</div>
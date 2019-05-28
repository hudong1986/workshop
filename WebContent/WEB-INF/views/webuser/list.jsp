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
<script type="text/javascript"
	src="<s:url value="/assets" />/js/ptuser.js"></script>
<script type="text/javascript"
	src="<s:url value="/assets" />/js/attendance.js"></script>	
<script type="text/javascript"
	src="<s:url value="/assets" />/js/reward.js"></script>	
<script type="text/javascript"
	src="<s:url value="/assets" />/js/finishStatistic.js"></script>	
<script type="text/javascript"
	src="<s:url value="/assets" />/js/salary.js"></script>	
<script>
    var selectedIds;
	var leaveMoreUrl="<s:url value="/user/leaveMore" />";
	var resetPwdMoreUrl="<s:url value="/user/resetPwdMore" />";
	var userEditUrl= "<s:url value="/user/edit" />";
	var userAddUrl="<s:url value="/user/add" />";
	var attendanceAddUrl="<s:url value="/attendance/add" />";
	var attendanceUrl="<s:url value="/attendance/list" />";
	var rewardAddUrl="<s:url value="/reward/add" />";
	var rewardUrl="<s:url value="/reward/list" />";
	var finishStatisticUrl="<s:url value="/finish_statistic/list" />";
	var finishStatisticAddUrl="<s:url value="/finish_statistic/add" />";
	var salaryUrl="<s:url value="/salary/list" />";
	var salaryAddUrl="<s:url value="/salary/add" />";
	
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
	function edit(id,name,dept,role,fix_money,fix_days,jobname,state){
		editId=id;
		$("#edit_name").val(name);
		$("#reset_deptId").val(dept);
		$("#reset_role_code").val(role);
		$("#edit_fix_money").val(fix_money);
		$("#edit_fix_days").val(fix_days);
		$("#edit_job_name").val(jobname);
		$("#edit_state").val(state);
		
		$("#editModal").modal('show');
	}
	 
	 
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">员工列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/user/list" var="submit_rul" />
					<sf:form class="form-inline" action="${submit_rul }" method="post"
						id="searchForm">

						<div class="form-group">
							部门：
							<select class="form-control" name="up_down_id">
								 
								<c:forEach var="item" items="${mydept }">
									<option value="${item.upDownId }"
										${up_down_id==item.upDownId ? "selected=selected" : "" }>${item.deptName }</option>
								</c:forEach>

							</select>

						</div>

						<div class="form-group">
							手机号： <input type="text"
								class="form-control" id="phone" name="phone" value="${phone}"
								placeholder="请输入手机号">
						</div>
						<div class="form-group">
							姓名： <input type="text"
								class="form-control" id="user_name" name="user_name"
								value="${user_name}" placeholder="请输入姓名">
						</div>


						<div class="form-group">
							在职状态：
							<select class="form-control" name="state">
								<option value="0" ${state=="0" ? "selected=selected" : "" }>在职</option>
								<option value="1" ${state=="1" ? "selected=selected" : "" }>离职</option>

							</select>

						</div>
						<div class="form-group">
							 每页显示数量： <select class="form-control"
								name="currentPageSize">
								<option value="20"
									${currentPageSize=="20" ? "selected=selected" : "" }>20</option>
								<option value="50"
									${currentPageSize=="50" ? "selected=selected" : "" }>50</option>
								<option value="100"
									${currentPageSize=="100" ? "selected=selected" : "" }>100</option>
								<option value="200"
									${currentPageSize=="200" ? "selected=selected" : "" }>200</option>
								<option value="300"
									${currentPageSize=="300" ? "selected=selected" : "" }>300</option>
								<option value="500"
									${currentPageSize=="500" ? "selected=selected" : "" }>500</option>

							</select>

						</div>

						<button type="submit" class="btn btn-default">查询</button>

					</sf:form>

				</div>
				<div class="panel-heading form-inline">
					<sec:authorize access="hasAnyRole('HrManager','HrAssistant')">
						<button type="button" id="addEmp" class="btn btn-sm btn-default"
							data-toggle="modal" data-target="#addModal">添加新员工</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('HrManager','HrAssistant')">
						<button type="button" id="resetPwdMore"
							class="btn btn-sm btn-default">批量重置密码</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('HrManager','HrAssistant')">
						<button type="button" id="addSalary"
							class="btn btn-sm btn-default">计算员工上月薪资</button>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('HrManager','HrAssistant')">
						<button type="button" id="addReward"
							class="btn btn-sm btn-default">添加员工奖励</button>
					</sec:authorize>

					<sec:authorize
						access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')">
						<button type="button" id="addAttendance"
							class="btn btn-sm btn-default">添加考勤</button>
					</sec:authorize>
					<sec:authorize
						access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')">
						<button type="button" id="addJijian"
							class="btn btn-sm btn-default">添加工人记件</button>
					</sec:authorize>


				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead class="theadStyle">
								<tr>
									<th><input type="checkbox" value="${cus.id}"
										onclick="SelectAll();" /></th>
									<th><a
										href="<s:url value="/user/list?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>姓名</th>
									<th>手机号</th>
									<th>部门</th>
									<th>角色</th>
									<th>职位描述</th>
									<th>固定工资</th>
									<th>保底工作天数</th>
									<th>状态</th>
									<th>登录时间</th>
									<th><a
										href="<s:url value="/user/list?${pager.orderString3}" />">
											<li class="glyphicon ${pager.orderString3.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>入职时间
									</a></th>
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
												<td><c:out value="${item.realName}" /></td>
												<td><c:out value="${item.phone}" /></td>
												<td><c:out value="${item.deptName}" /></td>
												<td><c:out value="${item.roleName}" /></td>
												<td><c:out value="${item.jobName}" /></td>
												<td><c:out value="${item.fixMoney}" /></td>
												<td><c:out value="${item.workDays}" /></td>
												<td><c:out value="${item.state==0 ? '在职' : '离职'}" /></td>
												<td><fmt:formatDate value="${item.loginTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><fmt:formatDate value="${item.addTime}"
														pattern="yyyy/MM/dd HH:mm:ss" /></td>
												<td><sec:authorize
														access="hasAnyRole('HrManager','HrAssistant')">
														<button type="button"
															onclick="edit('${item.id}','${item.realName}','${item.deptId}',
						'${item.roleCode}','${item.fixMoney}','${item.workDays}','${item.jobName}','${item.state }')"
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
							href="<s:url value="/user/list?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/user/list?${pager.nextPageString}" />">下一页</a></li>
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
					<h4 class="modal-title" id="myModalLabel">添加新员工</h4>
				</div>
				<div class="modal-body" id="addEmpForm">

					<div class="form-group">
						<label class="control-label">性别</label> <select
							class="form-control" id="add_sex">
							<option value="1">男</option>
							<option value="0">女</option>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">姓名</label> <input
							class="form-control" type="text" maxlength="10"
							id="add_real_name" name="add_real_name" placeholder="请输入员工姓名">
					</div>
					<div class="form-group">
						<label class="control-label">手机号</label> <input
							class="form-control" type="text" maxlength="11" id="add_phone"
							placeholder="请输入11位手机号" name="add_phone">
					</div>

					<div class="form-group">
						<label class="control-label">密码</label> <input
							class="form-control" type="text" placeholder="请输入不超过10位的密码"
							id="add_pwd" maxlength="10" name="add_pwd">
					</div>
					<div class="form-group">
						<label class="control-label">职位描述</label> <input
							class="form-control" type="text" maxlength="20" id="add_job_name"
							placeholder="例如电工、包装工等" name="add_job_name">
					</div>
					<div class="form-group">
						<label class="control-label">固定薪水</label> <input
							class="form-control" type="text" maxlength="5" id="add_fix_money"
							placeholder="0" name="add_fix_money">
					</div>
					<div class="form-group">
						<label class="control-label">固定薪水要求的工作天数</label> <select
							class="form-control" id="add_fix_days">
							<option value="22">22</option>
							<option value="22">26</option>
							<option value="22">28</option>
						</select>
					</div>

					<div class="form-group">
						<label class="control-label">部门</label> <select
							class="form-control" id="add_deptId">
							<c:forEach var="item" items="${mydept }">
								<option value="${item.id }">${item.deptName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">角色</label> <select
							class="form-control" id="add_role_code">
							<c:forEach var="item" items="${roleList }">
								<option value="${item.roleCode }">${item.roleName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label for="cus_name">入职时间</label> <input type="text"
							class="form-control"  placeholder=""
							name="add_work_time" id="add_work_time"
							onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />

					</div>
					<div class="form-group">
						<label class="control-label col-lg-4">预览</label>
						<div class="">
							<div class="fileupload fileupload-new" data-provides="fileupload">
								<div class="fileupload-new thumbnail"
									style="width: 200px; height: 150px;">
									<img src="<s:url value="/assets" />/img/demoUpload.jpg" alt="" />
								</div>
								<div class="fileupload-preview fileupload-exists thumbnail"
									style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
								<div>
									<span class="btn btn-file btn-primary"><span
										class="fileupload-new">选择头像图片</span><span
										class="fileupload-exists">重新选择</span><input type="file"
										id="file"></span> <a href="#"
										class="btn btn-danger fileupload-exists"
										data-dismiss="fileupload">移除</a> <span>(注意:不选择将使用默认头像)</span>
								</div>
							</div>
						</div>
					</div>

					<div id="user_add_msg" style="color: red"></div>
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
					<h4 class="modal-title" id="myModalLabel">修改员工相关信息</h4>
				</div>
				<div class="modal-body">

					<div class="form-group">
						<label class="control-label">姓名</label> <input
							class="form-control" type="text" maxlength="20" id="edit_name"
							placeholder="" name="edit_name" required>
					</div>
					<div class="form-group">
						<label class="control-label">部门</label> <select
							class="form-control" id="reset_deptId">
							<c:forEach var="item" items="${deptList }">
								<option value="${item.id }">${item.deptName }</option>
							</c:forEach>
						</select>
					</div>
					
					<div class="form-group">
						<label class="control-label">状态</label> <select
							class="form-control" id="edit_state">
								<option value="0">在职</option>
								<option value="1">离职</option>
						</select>
					</div>
					
					<div class="form-group">
						<label class="control-label">角色</label> <select
							class="form-control" id="reset_role_code">
							<c:forEach var="item" items="${roleList }">
								<option value="${item.roleCode }">${item.roleName }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">职位描述</label> <input
							class="form-control" type="text" maxlength="20"
							id="edit_job_name" placeholder="例如电工、包装工等" name="edit_job_name"
							required>
					</div>
					<div class="form-group">
						<label class="control-label">固定薪水</label> <input
							class="form-control" type="text" maxlength="5"
							id="edit_fix_money" placeholder="要求输入输入" name="edit_fix_money"
							required>
					</div>
					<div class="form-group">
						<label class="control-label">固定薪水要求的工作天数</label> <select
							class="form-control" id="edit_fix_days">
							<option value="22">22</option>
							<option value="22">26</option>
							<option value="22">28</option>
						</select>
					</div>
					<div id="user_edit_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="btn_resetDept">提交</button>
				</div>
			</div>
		</div>
	</div>

    <div class="modal fade" id="addAttendanceModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">添加考勤</h4>
				</div>
				<div class="modal-body" id="addAttendanceForm">

					<div class="form-group">
						<label class="control-label">类型</label> <select
							class="form-control" id="add_attd_type">
							<option value="1">请假</option>
							<option value="2">加班</option>
						</select>
					</div>
					<div class="form-group">
						<label for="cus_name">时间</label> 
						 from<input type="text"
							class="form-control"  placeholder=""
							name="add_attd_begtime" id="add_attd_begtime"
							onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
						 to<input type="text"
							class="form-control"  placeholder=""
							name="add_attd_endtime" id="add_attd_endtime"
							onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />

					</div>
					 <div class="form-group">
						<label class="control-label">备注说明</label> <input
							class="form-control" type="text" maxlength="100"
							id="add_attd_remark" name="add_attd_remark" placeholder="请输入备注说明">
					</div>
					 

					<div id="Attendance_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="add_attd">提交</button>
				</div>
			</div>
		</div>
	</div>

 <div class="modal fade" id="addRewardModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">添加奖励</h4>
				</div>
				<div class="modal-body" id="addRewardForm">
					<div class="form-group">
						<label for="control-label">归属月份</label> 
						 <input type="text"
							class="form-control"  placeholder=""
							name="add_reward_objmonth" id="add_reward_objmonth"
							onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM'});" />
						 
					</div>
					 <div class="form-group">
						<label class="control-label">金额(元)</label> <input
							class="form-control" type="text" maxlength="5"
							id="add_reward_money" name="add_reward_money" placeholder="请输出整数奖励金额">
					</div>
					
					 <div class="form-group">
						<label class="control-label">备注说明</label> <input
							class="form-control" type="text" maxlength="100"
							id="add_reward_remark" name="add_reward_remark" placeholder="例如红包、优秀员工等">
					</div>
					 

					<div id="reward_msg" style="color: red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="add_reward">提交</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="addFinishStatisticModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">添加工人记件</h4>
				</div>
				<div class="modal-body" id="addFinishStatisticForm">
					<div class="form-group">
						<label for="control-label">选择产品</label> 
						  <select class="form-control" id="add_finishStatistic_product_id">
								 
								<c:forEach var="item" items="${productlist }">
									<c:if test="${item.type==0 }">
									<option value="${item.id }">${item.productName }</option>
									</c:if>
								</c:forEach>

							</select>
						 
					</div>
					 <div class="form-group">
						<label class="control-label">数量</label> <input
							class="form-control" type="text" maxlength="5" value=""
							id="add_finishStatistic_num" name="add_finishStatistic_num" placeholder="">
					</div>
					
					<div id="finishStatistic_msg" style="color:red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="add_finishStatistic">提交</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="addSalaryModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title">计算工资</h4>
				</div>
				<div class="modal-body" >
					<div id="salary_msg" style="color:red"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="add_salary">开始计算</button>
				</div>
			</div>
		</div>
	</div>


</div>
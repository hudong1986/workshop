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
			$.post("<s:url value="/attendance/del" />", {
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

	});
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">员工奖励</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/reward/list" var="submit_rul" />
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

					</sf:form>

				</div>
				<div class="panel-heading form-inline">
					<sec:authorize
						access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')">
						<a href="<s:url value="/user/list" />">
							<button type="button" id="add" class="btn btn-sm btn-default">添加</button>
						</a>
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
										href="<s:url value="/reward/list?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>手机号</th>
									<th>姓名</th>
									<th>部门</th>
									<th>金额(元)</th>
									<th>月份</th>
									<th>备注</th>
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
												<td><c:out value="${item.phone}" /></td>
												<td><c:out value="${item.userName}" /></td>
												<td><c:out value="${item.deptName}" /></td>
												<td><c:out value="${item.money}" /></td>
												<td><c:out value="${item.objMonth}" /></td>
												<td><c:out value="${item.remark}" /></td>
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
							href="<s:url value="/reward/list?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/reward/list?${pager.nextPageString}" />">下一页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>


</div>
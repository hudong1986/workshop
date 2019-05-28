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
		if (confirm("删除将不能恢复并影响该员工薪资，确认要删除吗？")) {
			$.post("<s:url value="/finish_statistic/del" />", {
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
			<h1 class="page-head-line">工人记件列表</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/finish_statistic/list" var="submit_rul" />
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
							产品类型：
							<select class="form-control" name="product_id">
								<option value="-1"
										${product_id==-1 ? "selected=selected" : "" }>全部</option>
								<c:forEach var="item" items="${productlist }">
									<c:if test="${item.type==0 }">
									<option value="${item.id }"
										${product_id==item.id ? "selected=selected" : "" }>${item.productName }</option>
									</c:if>
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
							添加时间 从 <input type="text"
								class="form-control" value="${beg_time}" placeholder=""
								name="beg_time" id="beg_time"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
								到<input type="text"
								class="form-control" value="${end_time}" placeholder=""
								name="end_time" id="end_time"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
							 
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
									 
									<th><a
										href="<s:url value="/finish_statistic/list?${pager.orderString1}" />">
											<li class="glyphicon ${pager.orderString1.contains("=desc") ? "glyphicon-arrow-down" : "glyphicon-arrow-up"}"></li>ID
									</a></th>
									<th>部门</th>
									<th>手机号</th>
									<th>姓名</th>
									<th>产品</th>
									<th>产品数量</th>
									<th>记件提成(元)</th>
									<th>添加人</th>
									<th><a
										href="<s:url value="/finish_statistic/list?${pager.orderString3}" />">
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
												<td><c:out value="${item.deptName}" /></td>
												<td><c:out value="${item.phone}" /></td>
												<td><c:out value="${item.userName}" /></td>
												<td><c:out value="${item.productName}" /></td>
												<td><c:out value="${item.num}" /></td>
												<td><c:out value="${item.getMoney}" /></td>
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
							href="<s:url value="/finish_statistic/list?${pager.prePageString}" />">上一页</a></li>
						<li><a
							href="<s:url value="/finish_statistic/list?${pager.nextPageString}" />">下一页</a></li>
					</ul>
				</nav>
			</div>
		</div>
	</div>


</div>
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

	$(function() {

	});
</script>
<div id="page-inner">
	<div class="row">
		<div class="col-md-12">
			<h1 class="page-head-line">出入库统计</h1>
		</div>
	</div>

	<div class="row">
		<div class="col-md-12">
			<!--   Basic Table  -->
			<div class="panel panel-default">
				<div class="panel-heading">
					<s:url value="/report/goods_in_out" var="submit_rul" />
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
							日期 从 <input type="text" class="form-control" value="${beg_time}"
								placeholder="" name="beg_time" id="beg_time"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'});" />
							到<input type="text" class="form-control" value="${end_time}"
								placeholder="" name="end_time" id="end_time"
								onfocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'});" />

						</div>

						<button type="submit" class="btn btn-default">查询</button>
						<s:url value="/report/goods_in_out" var="exportUrl">
							<s:param name="direction" value="${direction }" />
							<s:param name="beg_time" value="${beg_time }" />
							<s:param name="end_time" value="${end_time }" />
							<s:param name="isExport" value="1" />
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
									<th>产品</th>
									<th>产品数量</th>
									<th>金额</th>
								</tr>
							</thead>
							<tbody>
								<c:choose>
									<c:when test="${pager.totalCount>0}">

										<c:forEach var="item" items="${pager.data }">
											<tr>

												<td><c:out value="${item.product_name}" /></td>
												<td><c:out value="${item.num}" /></td>
												<td><c:out value="${item.totalMoney}" /></td>
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


</div>
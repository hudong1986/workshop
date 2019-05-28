<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<nav class="navbar-default navbar-side" role="navigation">
	<div class="sidebar-collapse">
		<ul class="nav" id="main-menu">
			<li>
				<div class="user-img-div">
					<img
						src="<s:url value="/assets/img/header/${usermode.picUrl }" />"
						class="img-thumbnail" />

					<div class="inner-text">
						${usermode.realName } <br /> <small>${usermode.deptName }
						</small>
					</div>
				</div>

			</li>


			<li><a href="<s:url value="/home" />"
				${active_menu.contains("1000-") ? "class='active-menu'" : "" }><i
					class="fa fa-dashboard "></i>首页</a></li>
			<li><a href="#"><i class="glyphicon glyphicon-shopping-cart"></i>生产管理<span
					class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('2000-') ? 'collapse in' : '' }">
					<sec:authorize
						access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager')">
						<li><a href="<s:url value="/product/list" />"
							${active_menu.contains("2000-2001") ? "class='active-menu'" : "" }>产品类型</a></li>
					</sec:authorize>

					<li><a href="<s:url value="/finish_statistic/list" />"
						${active_menu.contains("2000-2002") ? "class='active-menu'" : "" }>记件管理</a></li>

					<sec:authorize
						access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')">
						<li><a href="<s:url value="/goods_in_out/list" />"
							${active_menu.contains("2000-2003") ? "class='active-menu'" : "" }>产品出入库</a></li>
					</sec:authorize>


				</ul></li>

			<li><a href="#"><i class="glyphicon glyphicon-user"
					aria-hidden="true"></i>员工管理<span class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('3000-') ? 'collapse in' : '' }">

					<li><a href="<s:url value="/user/list" />"
						${active_menu.contains("3000-3001") ? "class='active-menu'" : "" }>员工列表</a></li>

					<li><a href="<s:url value="/attendance/list" />"
						${active_menu.contains("3000-3002") ? "class='active-menu'" : "" }>员工考勤</a></li>

					<li><a href="<s:url value="/salary/list" />"
						${active_menu.contains("3000-3003") ? "class='active-menu'" : "" }>员工薪水</a></li>

					<li><a href="<s:url value="/reward/list" />"
						${active_menu.contains("3000-3004") ? "class='active-menu'" : "" }>员工奖励</a></li>

				</ul></li>


			<li><a href="#"><i class="glyphicon glyphicon-signal"
					aria-hidden="true"></i>报表统计<span class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('4000-') ? 'collapse in' : '' }">
					<sec:authorize
						access="hasAnyRole('HrManager','HrAssistant','FactoryManager','ProductManager','WorkshopManager')">
						<li><a href="<s:url value="/report/statistic" />"
							${active_menu.contains("4000-4001") ? "class='active-menu'" : "" }>记件统计</a></li>
						<li><a href="<s:url value="/report/goods_in_out" />"
							${active_menu.contains("4000-4002") ? "class='active-menu'" : "" }>出入库统计</a></li>
						 
					</sec:authorize>
					<sec:authorize access="hasAnyRole('HrManager','HrAssistant')">
						<li><a href="<s:url value="/report/salary" />"
							${active_menu.contains("4000-4003") ? "class='active-menu'" : "" }>薪资发放统计</a></li>
					</sec:authorize>
				</ul></li>

			<li><a href="#"><i class="glyphicon glyphicon-cog"
					aria-hidden="true"></i>设置<span class="fa arrow"></span></a>
				<ul
					class="nav nav-second-level ${active_menu.contains('5000-') ? 'collapse in' : '' }">
					<sec:authorize access="hasAnyRole('HrManager','HrAssistant')">
						<li><a href="<s:url value="/system/list" />"
							${active_menu.contains("5000-5001") ? "class='active-menu'" : "" }>系统参数设置</a></li>
					</sec:authorize>

					<li><a href="<s:url value="/user/modifyPwd" />"
						${active_menu.contains("5000-5002") ? "class='active-menu'" : "" }>修改密码</a></li>
				</ul></li>
			<li><a href="<s:url value="/logout" />"><i
					class="glyphicon glyphicon-off"></i>退出</a></li>
		</ul>

	</div>

</nav>
<!-- /. NAV SIDE  -->
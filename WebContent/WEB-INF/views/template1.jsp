<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>生产绩效管理后台-<c:out
		value="${CurrentTitle==null ? '' : CurrentTitle }" /></title>

<!-- BOOTSTRAP STYLES-->
<link href="<s:url value="/assets" />/css/bootstrap.css"
	rel="stylesheet" />
<!-- FONTAWESOME STYLES-->
<link href="<s:url value="/assets" />/css/font-awesome.css"
	rel="stylesheet" />
<!--CUSTOM BASIC STYLES-->
<link href="<s:url value="/assets" />/css/basic.css" rel="stylesheet" />
<!--CUSTOM MAIN STYLES-->
<link href="<s:url value="/assets" />/css/custom.css" rel="stylesheet" />
<!-- SCRIPTS -AT THE BOTOM TO REDUCE THE LOAD TIME-->
<!-- JQUERY SCRIPTS -->
<script src="<s:url value="/assets" />/js/jquery-1.10.2.js"></script>
<!-- BOOTSTRAP SCRIPTS -->
<script src="<s:url value="/assets" />/js/bootstrap.js"></script>
<!-- METISMENU SCRIPTS -->
<script src="<s:url value="/assets" />/js/jquery.metisMenu.js"></script>
<!-- CUSTOM SCRIPTS -->
<script src="<s:url value="/assets" />/js/custom.js"></script>

 

</head>
<body>
	<div id="wrapper">
		<div id="header">
			<t:insertAttribute name="header" />
		</div>
		<div id="side">
			<t:insertAttribute name="side" />
		</div>
		<div id="page-wrapper">
			<div id="content">
				<t:insertAttribute name="content" />
			</div>
		</div>
		<!-- /. PAGE WRAPPER  -->
	</div>
	<!-- /. WRAPPER  -->

	<div id="footer-sec" class="text-center">
		<div id="footer">
			<t:insertAttribute name="footer" />
		</div>

	</div>
	<!-- /. FOOTER  -->

</body>
</html>

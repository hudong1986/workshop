<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.HashSet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>生产绩效管理-登录</title>

<!-- BOOTSTRAP STYLES-->
<link href="assets/css/bootstrap.css" rel="stylesheet" />
<!-- FONTAWESOME STYLES-->
<link href="assets/css/font-awesome.css" rel="stylesheet" />
<!-- JQUERY SCRIPTS -->
<script src="assets/js/jquery-1.10.2.js"></script>
<script>
	function checkinfo() {
		var test = window.location.href;
		if (test.indexOf("error") > 0) {
			
			$("#errortip").html("登录失败，请检查用户名与密码是否正确！");
		}

	}
</script>
</head>
<body style="background-color: #E2E2E2;" onload="checkinfo();">
	<div class="container">
		<div class="row text-center " style="padding-top: 100px;">
			<div class="col-md-12">
				<!-- <img src="assets/img/logo-invoice.png" /> -->
				<h1>生产绩效管理V1</h1>
			</div>
		</div>
		<div class="row ">
			
			<div
				class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1">
              
				<div class="panel-body">
				<s:url value="/login" var="submit_rul" />
					<sf:form role="form" method="POST" name="f" action="${submit_rul }">
						<hr />
						 
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="fa fa-tag"></i></span>
							<input type="text" class="form-control" name="username"
								placeholder="请输入手机号" />
						</div>
						<div class="form-group input-group">
							<span class="input-group-addon"><i class="fa fa-lock"></i></span>
							<input type="password" class="form-control" name="password"
								placeholder="请输入密码" />
						</div>
						<div class="form-group">
							<label class="checkbox-inline"> <input type="checkbox"
								id="remember_me" name="remember-me" /> 记住我
							</label>
						</div>

						<button type="submit" class="btn btn-info">登 录</button>
						 
						 <div id="errortip" style="margin:5px;color:red">${errortip }</div>
						<hr />
						已经登录？<a href="home">点击这里进入主页</a>
					</sf:form>
				</div>

			</div>


		</div>
		 
	</div>

</body>
</html>

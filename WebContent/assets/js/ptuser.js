$(function() {
		$("#leaveMore").click(function() {

			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的员工");
				return;
			}

			if (confirm("离职状态将无法再登录系统，确认提交？") == false) {
				return;
			}

			$.post(leaveMoreUrl, {
				"ids" : list,
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
		});

		 

		$("#resetPwdMore").click(function() {
			list = getAllCheck();
			if (list == "") {
				alert("请选择要操作的员工");
				return;
			}

			if (confirm("重置后，登录密码为'123456'，确认提交？") == false) {
				return;
			}

			$.post(resetPwdMoreUrl, {
				"ids" : list,
				"${_csrf.parameterName}" : "${_csrf.token}"

			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		});

		//修改个人信息
		$("#btn_resetDept").click(function() {
			
			$.post(userEditUrl, {
				"id" : editId,
				"deptId" : $("#reset_deptId").val(),
				"roleId" : $("#reset_role_code").val(),
				"edit_name" : $("#edit_name").val(),
				"fix_money" : $("#edit_fix_money").val(),
				"fix_days" : $("#edit_fix_days").val(),
				"job_name" : $("#edit_job_name").val(),
				"state" :$("#edit_state").val(),
				"${_csrf.parameterName}" : "${_csrf.token}"
			}, function(data, status) {
				if (data == 1) {
					alert("操作成功");
					$("#searchForm").submit();
				} else {
					alert("操作失败！");
				}

			}).error(function(data) {
				alert("请求失败!");
			});

		});

		$("#btn_add").click(function() {

			/*手动验证表单，当是普通按钮时。*/
			$('#addEmpForm').data('bootstrapValidator').validate();
			if (!$('#addEmpForm').data('bootstrapValidator').isValid()) {
				return;
			}

			//开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
			var formData = new FormData();
			formData.append("file", $("#file")[0].files[0]);
			formData.append("sex", $("#add_sex").val());
			formData.append("real_name", $("#add_real_name").val());
			formData.append("pwd", $("#add_pwd").val());
			formData.append("phone", $("#add_phone").val());
			formData.append("deptId", $("#add_deptId").val());
			formData.append("role_code", $("#add_role_code").val());
			formData.append("add_job_name", $("#add_job_name").val());
			formData.append("add_fix_money", $("#add_fix_money").val());
			formData.append("add_fix_days", $("#add_fix_days").val());
			formData.append("add_work_time", $("#add_work_time").val());
			formData.append("${_csrf.parameterName}", "${_csrf.token}");
			$.ajax({
				url :userAddUrl,
				type : 'POST',
				data : formData,
				// 告诉jQuery不要去处理发送的数据
				processData : false,
				// 告诉jQuery不要去设置Content-Type请求头
				contentType : false,
				beforeSend : function() {
					$("#user_add_msg").html("正在添加，请不要关闭弹出框........");
				},
				success : function(data) {
					if (data.code == 1) {
						alert("添加成功");
						$('#addModal').modal('hide');
						$("#searchForm").submit();
					} else {
						$("#user_add_msg").html(data.retMsg);
					}
				},
				error : function(responseStr) {
					$("#user_add_msg").html("添加失败");
				}
			});

		});

		//新增管理员前台校验  
		$("#addEmpForm").bootstrapValidator({
			message : 'This value is not valid',
			//反馈图标  
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				add_real_name : {
					message : '姓名无效',
					validators : {
						notEmpty : {
							message : '姓名不能为空'
						},
						stringLength : {
							min : 2,
							max : 10,
							message : '姓名长度>=2位并且<=10位'
						}
					}
				},
				add_phone : {
					message : '手机号格式不正确',
					validators : {
						notEmpty : {
							message : '手机号不能为空'
						},
						/* stringLength : {
							min : 11,
							max : 11,
							message : '手机号长度为11位'
						}, */
						regexp : {
							regexp : '^1[0-9]{10}$',
							message : '手机号长度为11位整数'
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
							regexp : '^[0-9]{0,5}$',
							message : '0-5位整数'
						}
					}
				},
				add_job_name : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}
					} 
				},
				
				add_work_time : {
					message : '格式不正确',
					validators : {
						notEmpty : {
							message : '不能为空'
						}
					} 
				},
				add_pwd : {
					validators : {
						notEmpty : {
							message : '密码不能为空'
						},
						stringLength : {
							min : 6,
							max : 20,
							message : '密码长度介于6到20'
						}
					}
				}
			}
		});
		
		

	});
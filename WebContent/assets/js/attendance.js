$(function() {

	$("#addAttendance").click(function() {

		selectedIds = getAllCheck();
		if (selectedIds == "") {
			alert("请选择要操作的员工");
			return;
		}

		$('#addAttendanceModal').modal('show');

	});

	$("#add_attd").click(function() {

		/* 手动验证表单，当是普通按钮时。 */
		$('#addAttendanceForm').data('bootstrapValidator').validate();
		if (!$('#addAttendanceForm').data('bootstrapValidator').isValid()) {
			return;
		}

		// 开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
		var formData = new FormData();

		formData.append("ids", selectedIds);
		formData.append("beg_time", $("#add_attd_begtime").val());
		formData.append("end_time", $("#add_attd_endtime").val());
		formData.append("remark", $("#add_attd_remark").val());
		formData.append("type", $("#add_attd_type").val());
		formData.append("hours", $("#add_attd_hours").val());

		$.ajax({
			url : attendanceAddUrl,
			type : 'POST',
			data : formData,
			// 告诉jQuery不要去处理发送的数据
			processData : false,
			// 告诉jQuery不要去设置Content-Type请求头
			contentType : false,
			beforeSend : function() {
				$("#Attendance_msg").html("正在添加，请不要关闭弹出框........");
			},
			success : function(data) {
				if (data.code == 1) {
					if (confirm("添加成功，是否马上去查看？")) {
						window.location.href=attendanceUrl;
					} else {
						$('#addAttendanceModal').modal('hide');
						$("#searchForm").submit();
					}
				} else {
					$("#Attendance_msg").html(data.retMsg);
				}
			},
			error : function(responseStr) {
				$("#Attendance_msg").html("添加失败");
			}
		});

	});

	// 新增管理员前台校验
	$("#addAttendanceForm").bootstrapValidator({
		message : 'This value is not valid',
		// 反馈图标
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			add_attd_begtime : {
				message : '时间无效',
				validators : {
					notEmpty : {
						message : '时间不能为空'
					}
				}
			},
			add_attd_endtime : {
				message : '时间无效',
				validators : {
					notEmpty : {
						message : '时间不能为空'
					}
				}
			},
			add_attd_remark : {
				message : '备注无效',
				validators : {
					notEmpty : {
						message : '备注不能为空'
					}
				}
			}
		}
	});

});
$(function() {

	$("#addReward").click(function() {

		selectedIds = getAllCheck();
		if (selectedIds == "") {
			alert("请选择要操作的员工");
			return;
		}

		$('#addRewardModal').modal('show');

	});

	$("#add_reward").click(function() {

		/* 手动验证表单，当是普通按钮时。 */
		$('#addRewardForm').data('bootstrapValidator').validate();
		if (!$('#addRewardForm').data('bootstrapValidator').isValid()) {
			return;
		}

		// 开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
		var formData = new FormData();

		formData.append("ids", selectedIds);
		formData.append("obj_month", $("#add_reward_objmonth").val());
		formData.append("money", $("#add_reward_money").val());
		formData.append("remark", $("#add_reward_remark").val());

		$.ajax({
			url : rewardAddUrl,
			type : 'POST',
			data : formData,
			// 告诉jQuery不要去处理发送的数据
			processData : false,
			// 告诉jQuery不要去设置Content-Type请求头
			contentType : false,
			beforeSend : function() {
				$("#reward_msg").html("正在添加，请不要关闭弹出框........");
			},
			success : function(data) {
				if (data.code == 1) {
					if (confirm("添加成功，是否马上去查看？")) {
						window.location.href=rewardUrl;
					} else {
						$('#addRewardForm').modal('hide');
						$("#searchForm").submit();
					}
				} else {
					$("#reward_msg").html(data.retMsg);
				}
			},
			error : function(responseStr) {
				$("#reward_msg").html("添加失败");
			}
		});

	});

	// 新增管理员前台校验
	$("#addRewardForm").bootstrapValidator({
		message : 'This value is not valid',
		// 反馈图标
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			addRewardForm : {
				message : '归属月份无效',
				validators : {
					notEmpty : {
						message : '时间不能为空'
					}
				}
			},
			add_reward_money : {
				message : '金额无效',
				validators : {
					notEmpty : {
						message : '金额不能为空'
					},
					regexp : {
						regexp : '^[0-9]{0,5}$',
						message : '0-5位整数'
					}
				}
			},
			add_reward_remark : {
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
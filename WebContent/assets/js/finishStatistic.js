$(function() {

	$("#addJijian").click(function() {

		selectedIds = getAllCheck();
		if (selectedIds == "") {
			alert("请选择要操作的员工");
			return;
		}

		$('#addFinishStatisticModal').modal('show');

	});

	$("#add_finishStatistic").click(function() {

		/* 手动验证表单，当是普通按钮时。 */
		$('#addFinishStatisticForm').data('bootstrapValidator').validate();
		if (!$('#addFinishStatisticForm').data('bootstrapValidator').isValid()) {
			return;
		}

		// 开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
		var formData = new FormData();

		formData.append("ids", selectedIds);
		formData.append("product_id", $("#add_finishStatistic_product_id").val());
		formData.append("num", $("#add_finishStatistic_num").val());

		$.ajax({
			url : finishStatisticAddUrl,
			type : 'POST',
			data : formData,
			// 告诉jQuery不要去处理发送的数据
			processData : false,
			// 告诉jQuery不要去设置Content-Type请求头
			contentType : false,
			beforeSend : function() {
				$("#finishStatistic_msg").html("正在添加，请不要关闭弹出框........");
			},
			success : function(data) {
				if (data.code == 1) {
					if (confirm("添加成功，是否马上去查看？")) {
						window.location.href=finishStatisticUrl;
					} else {
						$('#addFinishStatisticModal').modal('hide');
						$("#searchForm").submit();
					}
				} else {
					$("#finishStatistic_msg").html(data.retMsg);
				}
			},
			error : function(responseStr) {
				$("#finishStatistic_msg").html("添加失败");
			}
		});

	});

	// 新增管理员前台校验
	$("#addFinishStatisticForm").bootstrapValidator({
		message : 'This value is not valid',
		// 反馈图标
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			 
			add_finishStatistic_num : {
				message : '数量无效',
				validators : {
					notEmpty : {
						message : '数量不能为空'
					},
					regexp : {
						regexp : '^[0-9]{0,5}$',
						message : '0-5位整数'
					}
				}
			} 
		}
	});

});
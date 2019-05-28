$(function() {

	$("#addSalary").click(function() {

		selectedIds = getAllCheck();
		if (selectedIds == "") {
			alert("请选择要操作的员工");
			return;
		}

		$('#addSalaryModal').modal('show');

	});

	$("#add_salary").click(function() {

		// 开始上传用户信息，以下是主动构造，也可以直接用表单初始化var formData = new FormData("form");
		var formData = new FormData();
		formData.append("ids", selectedIds);

		$.ajax({
			url : salaryAddUrl,
			type : 'POST',
			data : formData,
			// 告诉jQuery不要去处理发送的数据
			processData : false,
			// 告诉jQuery不要去设置Content-Type请求头
			contentType : false,
			beforeSend : function() {
				$("#salary_msg").html("正在计算，请不要关闭弹出框........");
			},
			success : function(data) {
				if (data.code == 1) {
					if (confirm("计算成功，是否马上去查看？")) {
						window.location.href=salaryUrl;
					} else {
						$('#addSalaryModal').modal('hide');
						//$("#searchForm").submit();
					}
				} else {
					$("#salary_msg").html(data.retMsg);
				}
			},
			error : function(responseStr) {
				$("#salary_msg").html("计算失败");
			}
		});

	});

	 

});

$(function() {
	$("#submit").click(function() {
		$.ajax({
            cache: true,
            type: "POST",
            url: "http://localhost:8080/user/api/nameLogin",
            data:$('#form-login').serialize(),// 你的formid
            async: false,
            error: function(request) {
                alert("登录失败");
            },
            success: function(data) {
                alert("登录成功")
            }
        });
	});
});
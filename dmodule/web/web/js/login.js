
$(function() {
	$("#submit").click(function() {
        var name = $("input[name='name']").val();
        var password = $("input[name='password']").val();
        var type = "name"
        data = {
            "name": name,
            "password": password,
            "type": type
        };
		$.ajax({
            cache: true,
            type: "POST",
            url: "/auth/api/login",
            data: JSON.stringify(data),
            dataType:"json",
            async: false,
            error: function(request) {
                alert("登录失败");
            },
            success: function(data) {
                alert("登录成功");
                location.href = "/web/console.html";
            }
        });
	});
});
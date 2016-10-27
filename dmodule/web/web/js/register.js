// register.js
$(function() {
    // 用户名输入框失去焦点，向服务端发送请求判断用户名是否合法
    $("#form-account").blur(function() {
        var name = $("#form-account").val();
        name = trim(name);
        console.log(name.length);
        if (name.length < 1) return false;
        $.getJSON(
            '/user/api/isVaildUsername',
            {
                "username": name
            },
            function(json, status) {
                if (json['stat'] == 'OK') {
                    $(".form-item-account .i-ok")
                    .css("display", "block")
                    .css("background-position", "-61px -33px");
                } else if (json['stat'] == 'ERR_ACCOUNT_EXIST') {
                    alert(json['errMsg']);
                } else {
                    alert(json['stat']);
                }
        });
    });

    $('.field').focus(function(event) {
        var defaultNode = $(this).attr('default');
        $(this).parent().next().children('span').append(defaultNode);
    });

    $('.field').blur(function(event) {
        $('.input-tip span').empty();
        // $('.i-def').remove();
    });

    /* 工具,以后会抽出去做成一个单独的工具类 */
    function trim(str){ //删除左右两端的空格
　　     return str.replace(/(^\s*)|(\s*$)/g, "");
　　 }
});
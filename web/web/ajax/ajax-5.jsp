<%--
  Created by IntelliJ IDEA.
  User: Chris
  Date: 2018/12/5
  Time: 16:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>验证用户名</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.js"></script>
    <%--<script type="text/javascript" src="../scripts/jquery-1.7.2.js"></script>--%>
    <script type="text/javascript">
        // 验证用户名是否可用
        $(function () {
            // 为input设置改变事件
            $(":text[name='username']").change(function () {
                //
                // $("#response").append("<p style='color: red'>" + $(this).val() + "</p>");
                var name = $.trim($(this).val());
                // 去掉空格的NAME不为空
                if (name != "") {
                    // 指定连接地址，使用
                    <%--var url = "${pageContext.request.contextPath}/validateUserName";--%>
                    var url = "validateUserName";
                    // var url = "ajax/validateUserName";
                    var args = {"userName": name, "time": new Date()};
                    // 发送post请求
                    $.post(url, args, function (data) {
                        // 将网络请求的数据直接嵌入到代码中
                        $("#response").html(data);
                    })
                }
            })
        })
    </script>
</head>
<body>
<form action="" method="post">
    UserName: <input type="text" name="username"/>
    <br><br>
    <div id="response"></div>
    <br><br>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>

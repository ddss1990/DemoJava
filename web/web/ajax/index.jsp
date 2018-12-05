<%--
  Created by IntelliJ IDEA.
  User: Chris
  Date: 2018/12/5
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Demo</title>
</head>
<script type="text/javascript" src="../scripts/jquery-1.7.2.js"></script>
<script type="text/javascript">
    /* $(function () {
         $("a").click(function () {
             alert(1);
             return false;
         })
     })*/
    // 使用DOM方式操作
    window.onload = function (ev) {
        // 1. 获得超链接对象，并设置点击事件
        var a = document.getElementsByTagName("a")[0];
        a.onclick = function () {
            // 2. 创建XMLHttpRequest对象
            var httpRequest = new XMLHttpRequest();
            // 3. 调用open方法
            var method = "GET";// 请求方式
            var url = this.href; // 请求地址
            httpRequest.open(method, url);
            // 4. 发送内容，POST的时候用到
            // send之后才会真正的去请求
            httpRequest.send(null);
            // 5 监听状态变化
            httpRequest.onreadystatechange = function (ev1) {
                // 6. 判断请求状态
                if (httpRequest.readyState == 4) {
                    // 7. 判断请求结果
                    if (httpRequest.status == 200 || httpRequest.status == 304) {
                        // 8. 得到请求结果
                        alert(httpRequest.responseText);
                    }
                }
            }
            // 取消超链接
            return false;
        }
    }
</script>
<body>
<p3>Hello Ajax</p3>
<br><br>
<a href="helloAjax.txt">Hello Ajax</a>
</body>
</html>

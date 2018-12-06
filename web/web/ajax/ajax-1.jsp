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
    <title>数据格式 - HTML</title>
    <script type="text/javascript">
        window.onload = function (ev) {
            // 验证数据格式 - HTML
            var aNodes = document.getElementsByTagName("a");
            var detailNode = document.getElementById("details");
            for (var i = 0; i < aNodes.length; i++) {
                aNodes[i].onclick = function () {
                    // 1. 创建Ajax对象
                    var request = new XMLHttpRequest();
                    // 2. 打开链接
                    request.open("GET", this.href);
                    // 3. 发送内容
                    request.send(null);
                    //4. 监听状态变化事件
                    request.onreadystatechange = function (ev1) {
                        // 5. 请求完成
                        if (request.readyState == 4) {
                            // 6. 请求成功
                            if (request.status == 200 || request.status == 304) {
                                //  7. 为div设置读取到的HTML内容
                                detailNode.innerHTML = request.responseText;
                            }
                        }
                    }

                    // 取消跳转
                    return false;
                }
            }
        }
    </script>
</head>
<body>
<h1>People</h1>
<ul>
    <li><a href="files/andy.html">Andy</a></li>
    <li><a href="files/richard.html">Richard</a></li>
    <li><a href="files/jeremy.html">Jeremy</a></li>
</ul>
<div id="details"></div>

</body>
</html>

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
    <title>数据格式 - JSON</title>
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
                                // 7. 读取到JSON
                                var result = request.responseText;
                                /*
                                 * JSON格式
                                 * {
                                 *   "name": "Andy Budd",
                                 *   "website": "http://andybudd.com/",
                                 *   "email": "andy@clearleft.com"
                                 * }
                                 */
                                var json = eval("(" + result + ")");
                                var name = json.person.name;
                                var website = json.person.website;
                                var email = json.person.email;
                                /*目标格式
                                * <h2><a href="mailto:andy@clearleft.com">Andy Budd</a></h2>
                                * <a href="http://andybudd.com/">http://andybudd.com</a>
                                 */
                                var nodeH2 = document.createElement("h2");
                                var nodeA1 = document.createElement("a");
                                nodeA1.href = "mailto:" + email;
                                nodeA1.appendChild(document.createTextNode(name));
                                nodeH2.appendChild(nodeA1)

                                var nodeA2 = document.createElement("a");
                                nodeA2.href = website;
                                nodeA2.appendChild(document.createTextNode(email));

                                // var node_detail = document.getElementById("detail");
                                // 添加节点前先清空
                                detailNode.innerHTML = "";
                                detailNode.appendChild(nodeH2);
                                detailNode.appendChild(nodeA2);
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
    <li><a href="files/andy.json">Andy</a></li>
    <li><a href="files/richard.json">Richard</a></li>
    <li><a href="files/jeremy.json">Jeremy</a></li>
</ul>
<div id="details"></div>

</body>
</html>

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
    <title>购物车</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.js"></script>
    <%--<script type="text/javascript" src="../scripts/jquery-1.7.2.js"></script>--%>
    <script type="text/javascript">
        // 购物车里数量的变化
        $(function () {
            //
            var isHasCart = ${sessionScope.sc == null};
            alert(isHasCart)
            if (isHasCart) {
                alert(isHasCart);
                $("#cartInfo").hide()
            } else {
                $("#cartInfo").show();
                alert(${sessionScope.sc});
                $("#bookName").text("${sessionScope.sc.bookName}")
                $("#bookCount").text("${sessionScope.sc.bookCount}")
                $("#totalMoney").text("${sessionScope.sc.totalMoney}")
            }

            $("a").click(function () {
                var url = this.href;
                // var args = {"id": this.id, "time": new Date()}
                var args = {"time": new Date()}
                // $.post(url, args, function (data) {
                $.getJSON(url, args, function (data) {
                    // alert(data);
                    //{"bookName":java,"bookCount":1,"totalMoney":100.0}
                    var bookName = data.bookName;
                    var bookCount = data.bookCount;
                    // alert(bookCount)
                    var totalMoney = data.totalMoney;
                    if (bookCount == 0) {
                        $("#cartInfo").hide();
                    } else {
                        $("#cartInfo").show();
                        $("#bookName").text(bookName)
                        $("#bookCount").text(bookCount)
                        $("#totalMoney").text(totalMoney)
                    }
                    // }, "JSON")
                })
                // 取消跳转
                return false;
            })
        })
    </script>
</head>
<body>
<div>
    <p id="cartInfo" hidden="hidden">你已经将&nbsp;<span id="bookName"></span>&nbsp;加入到购物车中。
        购物车中的书有&nbsp;<span id="bookCount"></span>&nbsp;本。
        总价格&nbsp;<span id="totalMoney"></span>&nbsp;元</p>
</div>
<br><br>
Java:<a href="addToCart?id=java" id="java">加入购物车</a><br><br>
Orcale:<a href="addToCart?id=orcale" id="orcale">加入购物车</a><br><br>
Python:<a href="addToCart?id=python" id="python">加入购物车</a>
</body>
</html>

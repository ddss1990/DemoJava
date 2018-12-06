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
    <title>JQuery使用Ajax</title>
    <script type="text/javascript" src="../scripts/jquery-1.7.2.js"></script>
    <script type="text/javascript">
        $(function () {
            // alert($(":radio:checked").val());
            // 1. 为所有的超链接添加点击事件
            $("a").click(function () {
                // 被选中的单选框的值
                var flag = $(":radio:checked").val();
                // 预防缓存
                var args = {"time": new Date()};
                // 正则表达式
                var reg = /html$/g
                // alert(reg.test(this.href))
                switch (flag) {
                    case 0:
                        break;
                    case "1":
                        // load()
                        $("#details").load(this.href, args);
                        break;
                    case "2":
                        // $.get()
                        // HTML
                        /*$.get(this.href, args, function (data) {
                           $("#details").html(data);
                        });*/
                        // XML
                        var url = this.href.replace(reg, "xml");
                        $.get(url, args, function (data) {
                            var name = $(data).find("name").text();
                            var website = $(data).find("website").text();
                            var email = $(data).find("email").text();

                            /*
                            <h2><a href="mailto:andy@clearleft.com">Andy Budd</a></h2>
                            <a href="http://andybudd.com/">http://andybudd.com</a>
                             */
                            $("#details").empty().append("<h2><a href=\'mailto:" + email + "'>" + name + "</a></h2>")
                                .append("<a href=\'" + website + "'>" + website + "</a>");
                        });
                        break;
                    case "3":
                        // $.post()
                        var url = this.href.replace(reg, "json");
                        $.post(url, args, function (data) {
                            // alert(data);
                            var name = data.person.name;
                            var website = data.person.website;
                            var email = data.person.email;
                            $("#details").empty().append("<h2><a href=\'mailto:" + email + "'>" + name + "</a></h2>")
                                .append("<a href=\'" + website + "'>" + website + "</a>");
                        }, "JSON")
                        break;
                    case "4":
                        // $.getJSON()
                        var url = this.href.replace(reg, "json");
                        $.getJSON(url, args, function (data) {
                            var name = data.person.name;
                            var website = data.person.website;
                            var email = data.person.email;
                            $("#details").empty().append("<h2><a href=\'mailto:" + email + "'>" + name + "</a></h2>")
                                .append("<a href=\'" + website + "'>" + website + "</a>");
                        })
                        break;
                }
                // 取消超链接
                return false;
            })
        })
    </script>
</head>
<body>
<input type="radio" name="type" checked="checked" value="1"/>load()
<input type="radio" name="type" value="2"/>$.get()
<input type="radio" name="type" value="3"/>$.post()
<input type="radio" name="type" value="4"/>$.getJSON()

<h1>People</h1>
<ul>
    <li><a href="files/andy.html">Andy</a></li>
    <li><a href="files/richard.html">Richard</a></li>
    <li><a href="files/jeremy.html">Jeremy</a></li>
</ul>
<div id="details"></div>

</body>
</html>

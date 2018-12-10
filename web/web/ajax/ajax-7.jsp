<%--
  Created by IntelliJ IDEA.
  User: Chris
  Date: 2018/12/5
  Time: 16:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>三级联动</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="/scripts/jquery.blockUI.js"></script>
    <%--<script type="text/javascript" src="../scripts/jquery-1.7.2.js"></script>--%>
    <script type="text/javascript">
        // 三级联动
        $(function () {
            //
            $(document).ajaxStart(function () {
                $.blockUI({
                    message: $("#loading"),
                    css: {
                        border: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: '#FFFFFF'
                    }
                })
            }).ajaxStop($.unblockUI)
            $("#city").change(function () {
                // department和employee只保留第一个节点
                $("#department option:not(:first)").remove()
                $("#employee option:not(:first)").remove();
                $("#employee_info").hide()
                var locationId = this.value;
                if (locationId != "") {
                    var url = "employeeServlet?method=listDepartments&locationId=" + locationId;
                    var args = {"time": new Date()};
                    $.getJSON(url, args, function (data) {
                        $("#loading").hide()
                        if (data.length == 0) {
                            alert("当前城市没有部门！")
                        } else {
                            for (var i = 0; i < data.length; i++) {
                                var departmentId = data[i].departmentId;
                                var departmentName = data[i].departmentName;
                                $("#department").append("<option value='" + departmentId + "'>" + departmentName + "</option>");
                            }
                        }
                    })
                }
            })
            $("#department").change(function () {
                // 只保留employee的第一个子节点
                $("#employee option:not(:first)").remove();
                $("#employee_info").hide()
                if ($(this).val() != "") {
                    var url = "employeeServlet?method=listEmployees";
                    var args = {"departmentId": this.value, "time": new Date()};
                    $.getJSON(url, args, function (data) {
                        if (data.length == 0) {
                            alert("该部门没有员工！")
                        } else {
                            for (var i = 0; i < data.length; i++) {
                                var employeeId = data[i].employeeId;
                                var lastName = data[i].lastName;
                                // 添加子节点
                                $("#employee").append("<option value='" + employeeId + "'>" + lastName + "</option>")
                            }
                        }
                    })
                }
            })
            $("#employee").change(function () {
                if ($(this).val() != "") {
                    // 查找最准确的个人信息
                    var url = "employeeServlet?method=listEmployee";
                    var args = {"employeeId": this.value, "time": new Date()};
                    $.getJSON(url, args, function (data) {
                        $("#employee_info").show()
                        $("#table_id").text(data.employeeId)
                        $("#table_name").text(data.lastName)
                        $("#table_email").text(data.email)
                        $("#table_salary").text(data.salary)
                    })
                }
            })
        })
    </script>
</head>
<body>
<div>
    <p alt="" id="loading" style="display: none">正在加载...</p>
</div>
<center>
    <br><br>
    City:<select id="city">
    <option value="">请选择...</option>
    <c:forEach items="${locations}" var="location">
        <option value="${location.locationId}">${location.city}</option>
    </c:forEach>
</select>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    Department:<select id="department">
    <option value="">请选择...</option>
</select>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    Employee:<select id="employee">
    <option value="">请选择...</option>
</select>
    <br><br>
    <table id="employee_info" border="1" cellspacing="0" cellpadding="5" style="display: none;">
        <tbody>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Email</th>
            <th>Salary</th>
        </tr>
        <tr>
            <td id="table_id"></td>
            <td id="table_name"></td>
            <td id="table_email"></td>
            <td id="table_salary"></td>
        </tr>

        </tbody>

    </table>
</center>
</body>
</html>

package com.dss.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class ValidateUserName extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> usernames = Arrays.asList("aaa", "bbb", "ccc");
        String username = request.getParameter("userName");
        log(username);
        System.out.println(username);
        String result = null;
        if (usernames.contains(username)) {
            result = "<p style='color: red'>该用户名已注册</p>";
        } else {
            result = "<p style='color: green'>该用户名可以使用</p>";
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        // 将响应结果发送给客户端
        response.getWriter().print(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

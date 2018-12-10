package com.dss.web.servlets;

import com.dss.web.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * User: DSS
 * Date: 2018/12/9
 * Time: 20:47
 * Description: Shopping Cart Servlet
 */
public class ShoppingCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //
        // 获取购物车对象
        HttpSession session = req.getSession();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("sc");
        if (shoppingCart == null) {
            shoppingCart = ShoppingCart.getInstance();
            session.setAttribute("sc", shoppingCart);
        }
        String id = req.getParameter("id");
//        log(id);
        // 将商品加入到购物车中
        shoppingCart.addGoods(id);
        // 得到购物车现在的信息
        int count = shoppingCart.getBookCount();
        double countPrice = shoppingCart.getTotalMoney();
        // 准备JSON字符串，进行返回 {"bookName":id, "bookCount":count, "totalMoney":countPrice}
        /*StringBuilder result = new StringBuilder();
        result.append("{")
                .append("\"bookName\":\"")
                .append(id)
                .append("\",\"bookCount\":")
                .append(count)
                .append(",\"totalMoney\":")
                .append(countPrice)
                .append("}");*/
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(shoppingCart);
        resp.setContentType("text/javascript");
        resp.getWriter().print(result);
        log(count + " - " + countPrice);
    }
}

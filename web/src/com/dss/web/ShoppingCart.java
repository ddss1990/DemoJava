package com.dss.web;

import com.dss.web.bean.CartGoodsBean;
import com.dss.web.bean.GoodsBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: DSS
 * Date: 2018/12/9
 * Time: 20:57
 * Description: Shopping Cart
 */
public class ShoppingCart {
    private static Map<String, CartGoodsBean> mGoods = new HashMap<>();
    private static int ADD = 1;
    private static int MINUS = 2;
    private String id;

    public void addGoods(String id) {
        this.id = id;
        calcGoods(id, ADD);
    }

    public void minusGoods(String id) {
        calcGoods(id, MINUS);
    }

    private static void calcGoods(String id, int flag) {
        CartGoodsBean cartGoodsBean = mGoods.get(id);
        if (cartGoodsBean == null) {
            cartGoodsBean = new CartGoodsBean(id);
        }
        // 添加商品
        if (flag == ADD) {
            cartGoodsBean.setCount(cartGoodsBean.getCount() + 1);
        } else if (flag == MINUS) {
            // 减去商品
            cartGoodsBean.setCount(cartGoodsBean.getCount() - 1);
        }
        mGoods.put(id, cartGoodsBean);
    }

    public String getBookName() {
        return id;
    }

    public int getBookCount() {
        int count = 0;
        Collection<CartGoodsBean> goodsBeans = mGoods.values();
        for (CartGoodsBean bean : goodsBeans) {
            count = count + bean.getCount();
        }
        return count;
    }

    public double getTotalMoney() {
        double count = 0;
        Collection<CartGoodsBean> goodsBeans = mGoods.values();
        for (CartGoodsBean bean : goodsBeans) {
            count = count + bean.getCountPrice();
        }
        return count;
    }

    private static ShoppingCart sShoppingCart = new ShoppingCart();

    public synchronized static ShoppingCart getInstance() {
        return sShoppingCart;
    }

    private ShoppingCart() {

    }

}

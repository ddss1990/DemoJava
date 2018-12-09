package com.dss.web.bean;

/**
 * User: DSS
 * Date: 2018/12/9
 * Time: 21:00
 * Description:
 */
public class GoodsBean {
    private String id;
    private double price;

    public GoodsBean(String id) {
        this.id = id;
    }

    public GoodsBean(String id, double price) {
        this.id = id;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }
}

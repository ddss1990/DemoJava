package com.dss.web.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * User: DSS
 * Date: 2018/12/9
 * Time: 21:03
 * Description: Cart Bean
 */
public class CartGoodsBean {
    private String id;
    private int count;
    private double countPrice;
    private GoodsBean goods;

    private static Map<String, Double> sGoodsPrice = new HashMap<>();

    static {
        sGoodsPrice.put("java", 100d);
        sGoodsPrice.put("orcale", 142d);
        sGoodsPrice.put("python", 123d);
        sGoodsPrice.put("c++", 115d);
        sGoodsPrice.put("android", 82.5);
    }

    public CartGoodsBean(String id) {
        this.id = id;
        count = 0;
        countPrice = 0;
        goods = new GoodsBean(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public GoodsBean getGoods() {
        return goods;
    }

    public void setGoods(GoodsBean goods) {
        this.goods = goods;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.countPrice = count * sGoodsPrice.get(id);
    }

    public double getCountPrice() {
        return countPrice;
    }

    public void setCountPrice(float countPrice) {
        this.countPrice = countPrice;
    }
}

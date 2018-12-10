package com.dss.web.test;

import com.dss.web.bean.CartGoodsBean;
import com.dss.web.bean.GoodsBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * FileName: TestJackson
 * Author: Chris
 * Date: 2018/12/10 10:08
 * Description: Test1 Jackson
 */
public class TestJackson {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        GoodsBean goodsBean = new GoodsBean("Java", 100D);
        String goodsBeanJson = mapper.writeValueAsString(goodsBean);
        System.out.println("goodsBeanJson = " + goodsBeanJson);
        CartGoodsBean cartGoodsBean = new CartGoodsBean("java");
        String cartGoodsBeanJson = mapper.writeValueAsString(cartGoodsBean);
        System.out.println("cartGoodsBeanJson = " + cartGoodsBeanJson);
    }
}

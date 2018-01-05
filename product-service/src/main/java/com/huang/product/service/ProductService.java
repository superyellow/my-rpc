package com.huang.product.service;

import com.huang.product.service.bean.Product;

/**
 * @author simple_huang@foxmail.com on 2018/1/5.
 */
public class ProductService implements IProductService{

    public Product getById(long id) {
        Product product = new Product();
        product.setId(id);
        product.setName("hotDog");
        product.setPrice(100.0);
        return product;
    }
}

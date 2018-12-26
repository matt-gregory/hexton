package com.rarelittlebeastie.onixweb.onixApp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JacksonXmlRootElement(localName = "ONIXmessage")
@Slf4j
public class Products {
    private Map<String, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getIsbn(), product);
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        products.addAll(this.products.values());
        return products;
    }

    @JacksonXmlProperty(localName = "product")
    public void setProducts(List<Product> products) {
        products.stream().forEach(product -> addProduct(product));
    }
}
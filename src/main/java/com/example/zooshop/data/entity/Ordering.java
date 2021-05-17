package com.example.zooshop.data.entity;

import com.example.zooshop.enumerate.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Ordering implements Comparable<Ordering>, Serializable {

    private List<Product> products;
    private String date;
     OrderStatus status;

    public Ordering() {
        this.date = new java.util.Date().toString();
        this.products = new ArrayList<Product>();
        this.status = OrderStatus.UNCOMPLETED;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(Ordering ordering) {
        return date.compareTo(ordering.getDate());
    }
}

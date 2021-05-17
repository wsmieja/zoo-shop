package com.example.zooshop.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Product implements Comparable<Product>, Serializable {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private Integer amountInStock, amountMax;
    private Double volume, price, weight;
    @ManyToOne
    @JsonIgnore
    private Category category;

    public Product() {
    }

    public Product(Product product, Category category) {
        this.name = product.name;
        this.amountInStock = product.amountInStock;
        this.amountMax = product.amountMax;
        this.volume = product.volume;
        this.price = product.price;
        this.weight = product.weight;
        this.category = category;
    }

    public void copy(Product product) {
        this.name = product.name;
        this.amountInStock = product.amountInStock;
        this.amountMax = product.amountMax;
        this.volume = product.volume;
        this.price = product.price;
        this.weight = product.weight;
        this.category = product.category;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(Integer amountInStock) {
        this.amountInStock = amountInStock;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(Integer amountMax) {
        this.amountMax = amountMax;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    @Override
    public int compareTo(Product product) {
        return name.compareTo(product.getName());
    }
}

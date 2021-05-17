package com.example.zooshop.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category implements Comparable<Category>, Serializable{
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    @OneToMany(mappedBy="category")
    private List<Product> products;
    private Double volumeCapacity;

    public Category() {
    }

    public Category(Category category) {
        this.name = category.name;
        this.volumeCapacity=category.volumeCapacity;
        this.products = new ArrayList<Product>();
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Double getVolumeCapacity() {
        return volumeCapacity;
    }

    public void setVolumeCapacity(Double volumeCapacity) {
        this.volumeCapacity = volumeCapacity;
    }

    @Override
    public int compareTo(Category category) {
        return name.compareTo(category.getName());    }
}

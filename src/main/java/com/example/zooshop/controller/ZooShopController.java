package com.example.zooshop.controller;

import com.example.zooshop.data.entity.Category;
import com.example.zooshop.data.entity.Ordering;
import com.example.zooshop.data.entity.Product;
import com.example.zooshop.exception.*;
import com.example.zooshop.manager.ZooShopManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ZooShopController {
    private ZooShopManager zooShopManager;

    @Autowired

    public ZooShopController(ZooShopManager zooShopManager) {
        this.zooShopManager = zooShopManager;
    }

    @PostMapping("/category")
    public ResponseEntity<String> addCategory(@RequestBody Category category){
        if(category.getVolumeCapacity()<0)
            return new ResponseEntity<String>("Capacity must be positive", HttpStatus.UNPROCESSABLE_ENTITY);
        try {
            zooShopManager.saveCategory(category);
        } catch (CategoryAlreadyExistsException e) {
            return new ResponseEntity<String>("Category with this name already exists. Please choose a different name",
                    HttpStatus.CONFLICT);
        }
        return new ResponseEntity<String>("Category succesfully created",
                HttpStatus.CREATED);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id){
        try {
            zooShopManager.deleteCategoryById(id);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<String>("Category with this id doesn't exists",
                    HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("Category succesfully deleted",
                HttpStatus.OK);
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id){
        Category category = null;
        try {
             category= zooShopManager.findCategoryById(id).get();
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<Category>(category,
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Category>(category, HttpStatus.OK);
    }
    @GetMapping("/category/{id}/fulfillment")
    public ResponseEntity<Double> getCategoryFulfillment(@PathVariable("id") Long id){
        Double fulfillment = null;
        try {
            fulfillment= zooShopManager.getFulfillment(id);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<Double>(fulfillment,
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Double>(fulfillment, HttpStatus.OK);
    }
   @PostMapping("/category/{id}/product")
    public ResponseEntity<String> addProduct(@RequestBody Product product, @PathVariable("id") Long id) {
       if(product.getAmountMax()<0)
           return new ResponseEntity<String>("Maximum amount must be positive", HttpStatus.UNPROCESSABLE_ENTITY);
        try {
            zooShopManager.saveProduct(product, id);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<String>("Category with this id doesn't exists",
                    HttpStatus.NOT_FOUND);
        } catch (CategoryCapacityExceededException e) {
            return new ResponseEntity<String>("The product was not added to not exceed category capacity",
                    HttpStatus.NOT_FOUND);
        } catch (MaxAmountExceededException e) {
            return new ResponseEntity<String>("The product was not added to not exceed maximum amount",
                    HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<String>("Product succesfully added",
                HttpStatus.CREATED);
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        try {
            zooShopManager.deleteProductById(id);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<String>("Product with this id doesn't exists",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Product succesfully deleted",
                HttpStatus.OK);
    }
    @PostMapping("/order")
    public ResponseEntity<String> postOrder(@RequestParam String fn ){
        try {
            zooShopManager.addOrder(fn);
        } catch (IOException e) {
            return new ResponseEntity<String>("Category with this id doesn't exists",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Order succesfully added",
                HttpStatus.CREATED);
    }
    @PutMapping("/order/product/{productId}")
    public ResponseEntity<String> addProductToOrder(@RequestParam String fn, @PathVariable("productId")Long productId){
        try {
            zooShopManager.addProductToOrder(fn, productId);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<String>("Product with this id doesn't exists",
                    HttpStatus.NOT_FOUND);
        } catch (ProductAlreadyOnOrderException e) {
            return new ResponseEntity<String>("Product is already on the order",
                    HttpStatus.CONFLICT);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>("Product succesfully added to the order",
                HttpStatus.OK);
    }
    @PutMapping("/order/order")
    public ResponseEntity<String> order(@RequestParam String fn) {

        try {
            zooShopManager.order(fn);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<String>("Ordered",
                HttpStatus.OK);
    }
    @PutMapping("/order/deliver")
    public ResponseEntity<String> deliver(@RequestParam String fn) {

        try {
            zooShopManager.deliver(fn);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return new ResponseEntity<String>("Delivered",
                HttpStatus.OK);
    }

    @GetMapping("/order")
    public ResponseEntity<Ordering> getOrderById(@RequestParam String fn){
        Ordering order = null;

        try {
            order= zooShopManager.deserialize(fn);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Ordering>(order, HttpStatus.OK);
    }
}

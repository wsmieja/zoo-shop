package com.example.zooshop.manager;

import com.example.zooshop.data.entity.Category;
import com.example.zooshop.data.entity.Product;
import com.example.zooshop.data.entity.Ordering;
import com.example.zooshop.data.repository.CategoryRepo;
import com.example.zooshop.data.repository.ProductRepo;
import com.example.zooshop.enumerate.OrderStatus;
import com.example.zooshop.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class ZooShopManager {
    private CategoryRepo categoryRepo;
    private ProductRepo productRepo;

    @Autowired
    public ZooShopManager( CategoryRepo categoryRepo, ProductRepo productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    public Category saveCategory(Category category) throws CategoryAlreadyExistsException {
        for (Category c: categoryRepo.findAll()) {
            if(c.compareTo(category)==0)
                throw new CategoryAlreadyExistsException();
        }
        Category categoryRet=new Category(category);
        return categoryRepo.save(categoryRet);
    }
    public void deleteCategoryById(Long id) throws CategoryNotFoundException, ProductNotFoundException {
        Optional<Category> category= findCategoryById(id);
        if(category.isEmpty())
            throw new CategoryNotFoundException();
        for (Product p :
                category.get().getProducts()) {
            deleteProductById(p.getId());
        }
        categoryRepo.deleteById(id);
    }

    public Optional<Category> findCategoryById(Long id) throws CategoryNotFoundException {
        if(categoryRepo.findById(id).isEmpty())
            throw new CategoryNotFoundException();
        return categoryRepo.findById(id);
    }

    public Double getFulfillment(Long id) throws CategoryNotFoundException{
        if(categoryRepo.findById(id).isEmpty())
            throw new CategoryNotFoundException();
        Category category = categoryRepo.findById(id).get();
        Double sum=0.;
        for (Product pI:category.getProducts()) {
            sum+=pI.getVolume()*pI.getAmountInStock();
        }
        return sum/category.getVolumeCapacity();
    }

    public Product saveProduct(Product product, Long id) throws CategoryNotFoundException, MaxAmountExceededException, CategoryCapacityExceededException {
        if(categoryRepo.findById(id).isEmpty())
            throw new CategoryNotFoundException();
        Category category = findCategoryById(id).get();
        Double sum=0.;
        for (Product pI:category.getProducts()) {
            sum+=pI.getVolume()*pI.getAmountInStock();
        }
        if(sum+product.getAmountInStock()*product.getVolume()>category.getVolumeCapacity())
            throw new CategoryCapacityExceededException();
        for (Product p: category.getProducts()) {
            if(p.compareTo(product)==0) {
                if (p.getAmountInStock() + product.getAmountInStock() > p.getAmountMax())
                    throw new MaxAmountExceededException();
                p.setAmountInStock(p.getAmountInStock() + product.getAmountInStock());
                productRepo.save(p);
                return p;
            }
        }
        Product productRet=new Product(product, category);
        return productRepo.save(productRet);
    }
    public void deleteProductById( Long id) throws ProductNotFoundException {
        Optional<Product> product= productRepo.findById(id);
        if(product.isEmpty()){
            throw new ProductNotFoundException();
        }
        productRepo.deleteById(id);
    }
    /*public Ordering saveOrder() throws CategoryNotFoundException {
        Ordering orderRet=new Ordering();
        return orderRepo.save(orderRet);
    }*/
    public Ordering addOrder(String filename) throws IOException {
        Ordering orderRet=new Ordering();
        serialize(orderRet, filename);
        return orderRet;
    }
    public Ordering addProductToOrder(String filename, Long productId) throws ProductNotFoundException, ProductAlreadyOnOrderException, IOException, ClassNotFoundException {
        Ordering order = deserialize(filename);
        if(productRepo.findById(productId).isEmpty())
            throw new ProductNotFoundException();
        Product product = productRepo.findById(productId).get();
        for (Product p: order.getProducts()) {
            if(p.compareTo(product)==0) {
                p.copy(product);
                serialize(order, filename);
                return order;
            }
        }
        order.getProducts().add(product);
        serialize(order, filename);
        return order;
    }
    public Ordering getOrder(String filename) throws IOException, ClassNotFoundException {
        Ordering order = deserialize(filename);
        return order;
    }
    public Ordering order(String filename) throws IOException, ClassNotFoundException {

        Ordering order = deserialize(filename);
        order.setStatus(OrderStatus.ORDERED);
        serialize(order, filename);
        return order;
    }
    public Ordering deliver(String filename) throws IOException, ClassNotFoundException {
        Ordering order = deserialize(filename);
        order.setStatus(OrderStatus.DELIEVERED);
        serialize(order, filename);
        return order;
    }

    public Ordering serialize(Ordering order, String filename) throws  IOException {
        File outputFile = new File(filename);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileOutputStream file = new FileOutputStream(outputFile, false);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(order);
        out.flush();
        out.close();
        file.close();
        return order;
    }

    public Ordering deserialize(String filename) throws IOException, ClassNotFoundException {
        Ordering order;
        FileInputStream file = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(file);
        order = (Ordering)in.readObject();
        in.close();
        file.close();
        return order;
    }
}

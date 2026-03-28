package com.example.simpleWebApp.Services;

import com.example.simpleWebApp.Model.Product;
import com.example.simpleWebApp.Repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo repo;

    //List<Product> products = new ArrayList<>(Arrays.asList(
    //        new Product(101, "iphone", 5000),
    //        new Product(102, "tablet", 970),
    //        new Product(103, "telefono", 3000)));

    public List<Product> getProducts(){
        return repo.findAll();
    }

    public Product getProductById(int idProd){
        return repo.findById(idProd).orElse(new Product());
    }

    public void addProduct(Product prod){
        repo.save(prod);
    }

    public void updateProduct(Product prod){
        repo.save(prod);
    }

    public void deleteProduct(int prodId) {
        repo.deleteById(prodId);
    }
}

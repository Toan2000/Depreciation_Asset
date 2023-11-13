package com.example.produceService.controller;

import com.example.produceService.dto.ProductRequest;
import com.example.produceService.dto.ProductResponse;
import com.example.produceService.model.Product;
import com.example.produceService.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)

    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
}

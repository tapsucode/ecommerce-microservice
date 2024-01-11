package com.phamanh.productservice.controller;


import com.phamanh.productservice.domains.Product;
import com.phamanh.productservice.response.ProductResponse;
import com.phamanh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,
                                                                      @RequestParam List<String> colors,
                                                                      @RequestParam List<String> size,
                                                                      @RequestParam Integer minPrice,
                                                                      @RequestParam Integer maxPrice,
                                                                      @RequestParam Integer minDiscount,
                                                                      @RequestParam String sort,
                                                                      @RequestParam String stock,
                                                                      @RequestParam Integer pageNumber,
                                                                      @RequestParam Integer pageSize){

        Page<Product> result = productService.getAllProduct(category,colors,size,minPrice,maxPrice,minDiscount,sort,stock,pageNumber,pageSize);

        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId){
        Product product = productService.findProductById(productId);

        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

    @GetMapping("products/check/{productId}")
    public ResponseEntity<ProductResponse> checkProductByIdHandler(@PathVariable Long productId){

        Product product = productService.findProductById(productId);

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);

        return new ResponseEntity<>(productResponse, HttpStatus.ACCEPTED);
    }
}

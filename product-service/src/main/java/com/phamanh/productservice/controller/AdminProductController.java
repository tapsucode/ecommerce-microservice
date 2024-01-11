package com.phamanh.productservice.controller;


import com.phamanh.productservice.config.JwtProvider;
import com.phamanh.productservice.domains.Product;
import com.phamanh.productservice.request.CreateProductRequest;
import com.phamanh.productservice.response.ApiResponse;
import com.phamanh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,@RequestHeader("Authorization") String jwt){

        Long accountId = JwtProvider.getAccountIdFromToken(jwt);

        Product product = productService.createProduct(request,accountId);

        return new  ResponseEntity<Product>(product,HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable() Long productId,@RequestHeader("Authorization") String jwt){
        Long accountId = JwtProvider.getAccountIdFromToken(jwt);
        productService.deleteProduct(productId,accountId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Product deleted successfully");
        apiResponse.setStatus(true);

        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Product>> findAllProduct(@RequestParam Integer pageNumber,@RequestParam Integer pageSize,@RequestParam String sortBy){

        Page<Product> products = productService.findAllProducts(pageNumber,pageSize,sortBy);

        return new ResponseEntity<Page<Product>>(products,HttpStatus.OK);

    }

    @PutMapping("/{productId}update/")
    public ResponseEntity<Product> updateProduct(@PathVariable()Long productId,@RequestBody Product request,@RequestHeader("Authorization") String jwt) {

        Long accountId = JwtProvider.getAccountIdFromToken(jwt);

        Product product = productService.updateProduct(productId,accountId,request);

        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody CreateProductRequest[] product,@RequestHeader("Authorization") String jwt)  {

        Long accountId = JwtProvider.getAccountIdFromToken(jwt);

        for (CreateProductRequest request: product){
            productService.createProduct(request,accountId);
        }
        ApiResponse result = new ApiResponse();
        result.setMessage("Product create successfully");
        result.setStatus(true);
        return new  ResponseEntity<ApiResponse>(result,HttpStatus.CREATED);

    }
}

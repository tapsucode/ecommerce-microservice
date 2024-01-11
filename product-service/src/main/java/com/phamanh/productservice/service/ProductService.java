package com.phamanh.productservice.service;


import com.phamanh.productservice.domains.Product;
import com.phamanh.productservice.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest request,Long accountId);


    public Product updateProduct(Long productId,Long accountId,Product request);

    public Product findProductById(Long id);

    public List<Product> findProductByCategory(String categoryName);

    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
                                       Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);

    public Page<Product> findAllProducts( Integer pageNumber, Integer pageSize,String sortBy);

    public String deleteProduct(Long productId, Long accountId);

}

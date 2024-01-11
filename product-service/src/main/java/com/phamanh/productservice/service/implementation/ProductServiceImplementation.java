package com.phamanh.productservice.service.implementation;


import com.phamanh.commonservice.exceptions.ResourceNotFoundException;
import com.phamanh.productservice.domains.Category;
import com.phamanh.productservice.domains.Product;
import com.phamanh.productservice.repository.CategoryRepository;
import com.phamanh.productservice.repository.ProductRepository;
import com.phamanh.productservice.request.CreateProductRequest;
import com.phamanh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StreamBridge streamBridge;

    public final String CREATE_PRODUCT_INVENTORY_TOPIC="createProduct-out-0";
    public final String UPDATE_PRODUCT_INVENTORY_TOPIC="updateProduct-out-0";

    public final String DELETE_PRODUCT_INVENTORY_TOPIC="deleteProduct-out-0";

    @Override
    public Product createProduct(CreateProductRequest request,Long accountId) {

        List<Category> categories = categoryRepository.findAllById(request.getCategoriesId());

        Product product = new Product();
        BeanUtils.copyProperties(request, product);

        product.setCategories(categories);
        product.setCreatedDate(LocalDateTime.now());
        product.setAccountId(accountId);
        Product saveProduct = productRepository.save(product);


        return saveProduct;
    }

    @Override
    public String deleteProduct(Long productId, Long accountId) {

        Product product = findProductById(productId);
        if (product.getAccountId() == accountId) {
            product.getSizes().clear();
            productRepository.delete(product);
            return "Product deleted Successfully";
        } else {
            throw new ResourceNotFoundException("Account not valid");
        }

    }


    @Override
    public Product updateProduct(Long productId, Long accountId, Product request) {

        Product product = findProductById(productId);

        if (product.getAccountId() == accountId) {

            if (request.getQuantity()!=0){
                product.setQuantity(request.getQuantity()+product.getQuantity());
            }
            product.setTitle(request.getTitle());
            product.setDescription(request.getDescription());
            product.setDiscount(request.getDiscount());
            product.setImageUrl(request.getImageUrl());
            product.setBody(request.getBody());
            product.setWarrantyDuration(request.getWarrantyDuration());

            return productRepository.save(product);


        } else {
            throw new ResourceNotFoundException("Account not valid");

        }

    }
    @Override
    public Product findProductById (Long id){

        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id - " + id));
    }

    @Override
    public List<Product> findProductByCategory (String categoryName){
        Category category = categoryRepository.findByCategoryName(categoryName);
        if (category == null) {
            throw new ResourceNotFoundException("category not found with name"+categoryName);
        }
        return  category.getProducts();
    }


    @Override
    public Page<Product> getAllProduct (String category, List < String > colors, List < String > sizes, Integer
            minPrice, Integer maxPrice,
                                        Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize){

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> products = productRepository.filterProduct(category, minPrice, maxPrice, minDiscount, sort);

        if (!colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, products.size());
    }

    @Override
    public Page<Product> findAllProducts(Integer pageNumber, Integer pageSize,String sortBy) {

        List<Product> listAllProduct = productRepository.findAll();

        Sort sort;

        if (sortBy != null && !sortBy.isEmpty()){
            sort = Sort.by(sortBy);
        } else {
            sort= Sort.by(Sort.Direction.ASC,"id");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), listAllProduct.size());

        List<Product> pageContent = listAllProduct.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, listAllProduct.size());
    }

}

package com.phamanh.productreviews.service.implementation;

import com.phamanh.commonservice.exceptions.ResourceNotFoundException;
import com.phamanh.productreviews.converter.Product;
import com.phamanh.productreviews.domains.Rating;
import com.phamanh.productreviews.repository.RatingRepository;
import com.phamanh.productreviews.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RatingServiceImplementation implements RatingService {

    private final RestTemplate restTemplate;

    private final RatingRepository ratingRepository;

    private  String URl_CHECK_PRODUCT_BY_ID = "http://localhost:8020/api/products/check/{productId}";

    @Override
    public Rating createRating(Long productId, byte totalRating) {

        checkProductExist(productId);

        Rating ratingByProductId = ratingRepository.findByProductId(productId);

        if (ratingByProductId == null) {

            Rating rating = new Rating();
            rating.setProductId(productId);
            rating.setTotalAccount(1);
            rating.setTotalRating(totalRating);

            return ratingRepository.save(rating);

        } else {

            byte count = (byte) ((ratingByProductId.getTotalRating()*ratingByProductId.getTotalAccount()+totalRating)/(ratingByProductId.getTotalAccount()+1));
            ratingByProductId.setTotalRating(count);
            ratingByProductId.setTotalAccount(ratingByProductId.getTotalAccount()+1);

            return ratingRepository.save(ratingByProductId);
        }

    }

    @Override
    public Rating getRatingForProduct(Long productId) {

        Rating ratingByProductId = ratingRepository.findByProductId(productId);

        if (ratingByProductId == null) {

            throw new ResourceNotFoundException("Product not exists");

        } else {

            return ratingByProductId;
        }

    }

    protected Product checkProductExist(Long productId){

        ResponseEntity<Product> productResponseEntity = restTemplate.getForEntity(URl_CHECK_PRODUCT_BY_ID, Product.class,productId);
        Product product = productResponseEntity.getBody();

        if (product == null) {
            throw new ResourceNotFoundException("Product not exists");
        }
        return product;
    }
}

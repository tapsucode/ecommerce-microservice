package com.phamanh.productreviews.controller;


import com.phamanh.productreviews.domains.Rating;
import com.phamanh.productreviews.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("public/getRating")
    public ResponseEntity<Rating> getRatingForProduct(@RequestParam Long ProductId){

        Rating ratingForProduct = ratingService.getRatingForProduct(ProductId);

        return new ResponseEntity<>(ratingForProduct, HttpStatus.OK);
    }

    @PostMapping("user/createRating/{productId}")
    public ResponseEntity<Rating> createRating(@PathVariable Long productId,@RequestParam() @Size(min = 0,max = 100) byte totalRating ){

        Rating rating = ratingService.createRating(productId, totalRating);

        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }
}

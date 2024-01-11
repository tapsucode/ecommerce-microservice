package com.phamanh.productreviews.service;

import com.phamanh.productreviews.domains.Rating;

public interface RatingService {

    public Rating createRating(Long productId,byte totalRating);

    public Rating getRatingForProduct(Long productId);

}

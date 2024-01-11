package com.phamanh.productreviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.phamanh.productreviews", "com.phamanh.commonservice"})
public class ProductReviewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductReviewsApplication.class, args);
	}

}

package com.phamanh.productreviews.service.implementation;

import com.phamanh.commonservice.exceptions.ResourceNotFoundException;
import com.phamanh.productreviews.config.JwtProvider;
import com.phamanh.productreviews.converter.Product;
import com.phamanh.productreviews.domains.Comment;
import com.phamanh.productreviews.request.CommentRequest;
import com.phamanh.productreviews.repository.CommentRepository;
import com.phamanh.productreviews.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImplementation implements CommentService {

    private final CommentRepository commentRepository;

    private final RestTemplate restTemplate;

    private final JwtProvider jwtProvider;


    private final String URl_CHECK_PRODUCT_BY_ID = "http://localhost:8020/api/products/check/{productId}";

    @Override
    public Comment createComment(CommentRequest commentRequest,String jwt) {

        Product product = checkProductExist(commentRequest.getProductId());

        Long accountId = jwtProvider.getAccountIdFromToken(jwt);

        if(accountId.equals(commentRequest.getAccountId())){
            Comment comment = new Comment();

            comment.setProductId(comment.getProductId());
            comment.setProductAccountId(product.getAccountId());
            comment.setAccountId(commentRequest.getAccountId());
            comment.setUsername(jwtProvider.getUsernameFromToken(jwt));
            comment.setCreateTime(LocalDateTime.now());

            return  commentRepository.save(comment);
        } else {
            throw new ResourceNotFoundException("Account not valid");
        }

    }

    @Override
    public Comment getAllComment(Long productId) {
        return commentRepository.findByProductId(productId);
    }

    @Override
    public Comment updateComment(String jwt,Long commentId,String body) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not exists with id :"+commentId));

        if (comment.getAccountId().equals(jwtProvider.getAccountIdFromToken(jwt))) {

            comment.setBody(body);
            comment.setCreateTime(LocalDateTime.now());

            return  commentRepository.save(comment);
        } else {
            throw  new ResourceNotFoundException("Account not valid ");
        }
    }

    @Override
    public void deleteComment(String jwt,Long productAccountId, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not exists with id :"+commentId));

        if (comment.getAccountId().equals(jwtProvider.getAccountIdFromToken(jwt))  || comment.getProductAccountId().equals(productAccountId) ) {

             commentRepository.delete(comment);
        } else {

           throw new  ResourceNotFoundException("This account is invalid");
        }
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not exists with id :"+commentId));
        commentRepository.delete(comment);
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

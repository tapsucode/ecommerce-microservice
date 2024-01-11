package com.phamanh.productreviews.controller;

import com.phamanh.productreviews.domains.Comment;
import com.phamanh.productreviews.request.CommentRequest;
import com.phamanh.productreviews.response.ApiResponse;
import com.phamanh.productreviews.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class UserCommentController {

    private final CommentService commentService;

    @PostMapping("user/comment/createComment")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest commentRequest, @RequestHeader("Authorization") String jwt){

        Comment comment = commentService.createComment(commentRequest, jwt);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    };

    @GetMapping("public/comment/getComment/{productId}")
    public ResponseEntity<Comment> getAllCommentForProduct(@PathVariable() Long productId){

        Comment allComment = commentService.getAllComment(productId);

        return new ResponseEntity<>(allComment, HttpStatus.OK);
    };

    @PutMapping("user/comment/updateComment/{commentId}")
    public ResponseEntity<Comment> updateComment(@RequestHeader("Authorization") String jwt,@PathVariable Long commentId,@RequestParam String body){

        Comment comment = commentService.updateComment(jwt, commentId, body);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    };

    @DeleteMapping("user/comment/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteCommentByUser(@RequestHeader("Authorization") String jwt, @PathVariable Long commentId){

        commentService.deleteComment(jwt,null,commentId);

        ApiResponse apiResponse =new ApiResponse();

        apiResponse.setMessage("Comment deleted successfully");
        apiResponse.setStatus(true);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    };

    @DeleteMapping("sale/comment/delete/{commentId}/{productAccountId}")
    public ResponseEntity<ApiResponse> deleteCommentBySale(@PathVariable Long productAccountId, @PathVariable Long commentId){

        commentService.deleteComment(null,productAccountId,commentId);

        ApiResponse apiResponse =new ApiResponse();

        apiResponse.setMessage("Comment deleted successfully");
        apiResponse.setStatus(true);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    };

    @DeleteMapping("admin/comment/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteCommentByAdmin(@PathVariable Long commentId){

        commentService.deleteCommentByAdmin(commentId);

        ApiResponse apiResponse =new ApiResponse();

        apiResponse.setMessage("Comment deleted successfully");
        apiResponse.setStatus(true);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}

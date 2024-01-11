package com.phamanh.productreviews.service;

import com.phamanh.productreviews.domains.Comment;
import com.phamanh.productreviews.request.CommentRequest;

public interface CommentService {

    public Comment createComment(CommentRequest commentRequest,String jwt);

    public Comment getAllComment(Long productId);

    public Comment updateComment(String jwt,Long commentId,String body);

    public void deleteComment(String jwt,Long productAccountId,Long commentId);

    void deleteCommentByAdmin(Long commentId);
}

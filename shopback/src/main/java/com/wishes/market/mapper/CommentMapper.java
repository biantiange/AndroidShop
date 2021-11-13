package com.wishes.market.mapper;

import com.wishes.market.model.Comment;

import java.util.List;

public interface CommentMapper {

    /**
     * 添加评论
     *
     * @param comment 新评论
     * @return 修改是否成功
     */
    int insert(Comment comment);

    /**
     * 删除品论
     * @param commentId 评论ID
     * @return 修改是否成功
     */
    int deleteByPrimaryKey(Long commentId);

    /**
     * 根据帖子id查询评论
     * @param postId 帖子 postId
     */
    List<Comment> queryComments(Long postId);

    /**
     * 获取对应帖子数量
     * @param postId 帖子 postId
     * @return 商品评论数
     */
    Long queryCommentNumber(Long postId);

    /**
     * 获取所有帖子数量
     * @return 帖子评论数
     */
    Long queryCommentNumberBack();

    /**
     * 查询全部评论
     * @param start
     * @param offset
     */
    List<Comment> queryCommentsBack(int start, int offset);
}

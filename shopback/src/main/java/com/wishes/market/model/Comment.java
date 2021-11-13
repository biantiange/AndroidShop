package com.wishes.market.model;

/**
 * class - 评论
 */
public class Comment {
    /**
     * 评论ID-自增ID
     */
    private Long commentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论创建时间
     */
    private String createTime;
    /**
     * 删除标记{0:未删除 ; 1:删除}
     */
    private int deleteFlag;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", postId=" + postId +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}

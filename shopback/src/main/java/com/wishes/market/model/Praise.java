package com.wishes.market.model;

public class Praise {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 帖子ID
     */
    private Long postId;
    /**
     * 是否删除 「0：未删除 ； 1：已删除」
     */
    private int deleteFlag;

    public Praise(Long userId, Long postId, int deleteFlag) {
        this.userId = userId;
        this.postId = postId;
        this.deleteFlag = deleteFlag;
    }

    public Praise() {
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

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "Praise{" +
                "userId=" + userId +
                ", postId=" + postId +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}

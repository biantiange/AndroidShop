package com.sqh.market.models;

public class CommentModel {
    /**
     * 评论ID-自增ID
     */
    private Long commentId;

    /**
     * 用户名
     */
    private String useName;
    /**
     * 商品ID
     */
    private Long commodityId;

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

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
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
        return "CommentModel{" +
                "commentId=" + commentId +
                ", useName='" + useName + '\'' +
                ", commodityId=" + commodityId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}

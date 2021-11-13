package com.wishes.market.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class Post {
    /**
     * 帖子ID 自增
     */
    private Long postId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 图片1 url
     */
    private String imgOne;
    /**
     * 图片2 url
     */
    private String imgTwo;
    /**
     * 图片3 url
     */
    private String imgThree;
    /**
     * 用户昵称
     */
    @ApiModelProperty(hidden = true)
    private String uname;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * 帖子文字内容
     */
    private String content;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 点赞数
     */
    private String pCount;

    /**
     * 是否点赞了
     */
    private boolean praiseFlag;

    /**
     * 是否删除 「0：未删除 ； 1：已删除」
     */
    private int deleteFlag;

    private List<CommentModel> commentModels;

    public Post() {
        //非持久化成员属性需要初始化
        praiseFlag = false;
        pCount = "0";
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImgOne() {
        return imgOne;
    }

    public void setImgOne(String imgOne) {
        this.imgOne = imgOne;
    }

    public String getImgTwo() {
        return imgTwo;
    }

    public void setImgTwo(String imgTwo) {
        this.imgTwo = imgTwo;
    }

    public String getImgThree() {
        return imgThree;
    }

    public void setImgThree(String imgThree) {
        this.imgThree = imgThree;
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

    public String getpCount() {
        return pCount;
    }

    public void setpCount(String pCount) {
        this.pCount = pCount;
    }

    public boolean isPraiseFlag() {
        return praiseFlag;
    }

    public void setPraiseFlag(boolean praiseFlag) {
        this.praiseFlag = praiseFlag;
    }

    public List<CommentModel> getCommentModels() {
        return commentModels;
    }

    public void setCommentModels(List<CommentModel> commentModels) {
        this.commentModels = commentModels;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", imgOne='" + imgOne + '\'' +
                ", imgTwo='" + imgTwo + '\'' +
                ", imgThree='" + imgThree + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", pCount='" + pCount + '\'' +
                ", praiseFlag=" + praiseFlag +
                ", deleteFlag=" + deleteFlag +
                ", commentModels=" + commentModels +
                '}';
    }
}

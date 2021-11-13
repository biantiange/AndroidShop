package com.sqh.market.models;

import com.sqh.market.adapter.ReplyAdapter;

import java.util.List;

public class PostModel {
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
     * 发帖用户名
     */
    private String uname;

    /**
     * 是否点赞了
     */
    private boolean praiseFlag;

    /**
     * 评论json字符串
     */
    private String commentModels;

    /**
     * 评论List
     */
    private List<ReplyInfo> replyInfos;

    /**
     * 回复控制器
     */
    private ReplyAdapter replyAdapter;

    /**
     * 是否删除 「0：未删除 ； 1：已删除」
     */
    private int deleteFlag;

    public String getCommentModels() {
        return commentModels;
    }

    public void setCommentModels(String commentModels) {
        this.commentModels = commentModels;
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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public boolean isPraiseFlag() {
        return praiseFlag;
    }

    public void setPraiseFlag(boolean praiseFlag) {
        this.praiseFlag = praiseFlag;
    }

    public List<ReplyInfo> getReplyInfos() {
        return replyInfos;
    }

    public void setReplyInfos(List<ReplyInfo> replyInfos) {
        this.replyInfos = replyInfos;
    }

    public ReplyAdapter getReplyAdapter() {
        return replyAdapter;
    }

    public void setReplyAdapter(ReplyAdapter replyAdapter) {
        this.replyAdapter = replyAdapter;
    }
}

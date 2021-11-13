package com.sqh.market.models;

//每个回复条目的数据
public class ReplyInfo {
    private String commentId;
    private String userName;
    private String content;
    private String createTime;
    private boolean yesMine;


    public ReplyInfo(String commentId, String userName, String content, String createTime, boolean yesMine) {
        this.commentId = commentId;
        this.userName = userName;
        this.content = content;
        this.createTime = createTime;
        this.yesMine = yesMine;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public boolean isYesMine() {
        return yesMine;
    }

    public void setYesMine(boolean yesMine) {
        this.yesMine = yesMine;
    }
}

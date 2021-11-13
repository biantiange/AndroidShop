package com.wishes.market.model;

public class CommentModel {
    private Long commentId;
    private boolean yesMine;
    private String userName;
    private String content;
    private String createTime;

    public CommentModel(Long commentId, boolean yesMine, String userName, String content, String createTime) {
        this.commentId = commentId;
        this.yesMine = yesMine;
        this.userName = userName;
        this.content = content;
        this.createTime = createTime;
    }

    public CommentModel() {
    }

    public boolean getYesMine() {
        return yesMine;
    }

    public void setYesMine(boolean yesMine) {
        this.yesMine = yesMine;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
                "commentId=" + commentId +
                ", yesMine=" + yesMine +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

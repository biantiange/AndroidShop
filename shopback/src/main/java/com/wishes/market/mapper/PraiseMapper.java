package com.wishes.market.mapper;

import com.wishes.market.model.Praise;

import java.util.List;

public interface PraiseMapper {
    /**
     * 添加点赞
     * @param praise 点赞
     * @return 「1：添加点赞成功 或 已经点赞 ； 2：恢复点赞」
     */
    int insert(Praise praise);

    /**
     * 根据 组合主键删除点赞
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 改变的行数
     */
    int deleteByPrimaryKey(long userId,long postId);

    /**
     * 根据帖子ID 查询点赞数
     * @param postId 帖子ID
     * @return 该帖子点赞数
     */
    int queryPraiseCount(Long postId);

    /**
     * 根据 用户ID 和 PostIdList 查询出 该用户点赞了那些帖子
     * @param postIdList 帖子数组
     * @param userId 用户ID
     * @return 用户点赞了那些帖子 的 ID List
     */
    List<Long> getPraisesByPostIdList(List<Long> postIdList,Long userId);
}

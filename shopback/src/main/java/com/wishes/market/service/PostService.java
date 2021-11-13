package com.wishes.market.service;

import com.wishes.market.model.Post;
import com.wishes.market.model.Praise;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    /**
     * 点赞
     * @param praise 中间点赞表 实体对象
     * @return 1：点赞成功 或 已经点赞 2：恢复点赞
     */
    int insertPraise(Praise praise);

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

    /**
     * 新增帖子（发帖）
     * @param post 帖子
     * @return 1：发帖成功
     */
    int insertPost(Post post);

    /**
     * 根据主键删除帖子 （修改 delete_flag = 1）
     * @param postId 帖子ID
     * @return 改变的行数
     */
    int deleteByPrimaryKey(long postId);

    /**
     * 分页查询帖子
     * @param start 从哪开始
     * @param offset 每页多少
     * @return List Posts
     */
    List<Post> queryPostsBack(@Param("start") int start, @Param("offset") int offset);

    /**
     * 查询帖子数量
     * @return List count 数量
     */
    Long countPost();

    /**
     * 根据userId查询帖子数量
     * @return List count 数量
     */
    Long countPost(Long userId);
}

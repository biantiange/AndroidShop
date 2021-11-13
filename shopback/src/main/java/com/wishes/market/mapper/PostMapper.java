package com.wishes.market.mapper;

import com.wishes.market.model.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper {
    /**
     * 新增帖子（发帖）
     * @param post 帖子
     * @return 1：发帖成功
     */
    int insert(Post post);

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


    Long countPostByUserId(Long userId);
}

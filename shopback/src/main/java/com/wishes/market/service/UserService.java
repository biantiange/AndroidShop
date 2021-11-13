package com.wishes.market.service;

import com.wishes.market.config.CommConfig;
import com.wishes.market.model.Comment;
import com.wishes.market.model.UserDo;
import com.wishes.market.utils.PageDto;

import java.util.List;
import java.util.Set;

/**
 * 用户登录、登出、注册Service
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param user 用户信息
     * @return String/null 成功/失败
     */
    String login(UserDo user);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return String/null 成功/失败
     */
    String register(UserDo user);

    /**
     * 修改密码、昵称等用户数据
     *
     * @param user 用户信息
     * @return true/false 成功/失败
     */
    boolean modifyUserInfo(UserDo user);


    /**
     * 根据用户id查询用户信息
     *
     * @param uId 用户id
     * @return 用户信息实体
     */
    UserDo getUserInfoByUid(Long uId);

    /**
     * 用户登出
     *
     * @param uid 用户id
     */
    void logout(Integer uid);

    /**
     * 是否存在用户
     *
     * @param uid 用户id
     * @return 是否存在用户
     */
    boolean isExistUser(Long uid);

    /**
     * 是否存在用户
     *
     * @param account 用户账户
     * @return 是否存在用户
     */
    boolean isExistUser(String account);

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @return String/null 成功/失败
     */
    String insertComment(Comment comment);

    /**
     * 删除评论
     * （更该 deleteFlag = 1）
     *
     * @param commentId 评论ID（主键）
     * @return String/null 成功/失败
     */
    String removeComment(Long commentId);

    /**
     * 查询评论
     *
     * @param postId 帖子ID
     * @return 评论集合
     */
    List<Comment> searchComment(Long postId);

    /**
     * 获取评论数目
     * @param commodityId 商品ID
     * @return 评论数
     */
    Long getCommentNumber (Long commodityId);

    /**
     * 查询用户
     *
     * @return 用户集合
     * @param start
     * @param offset
     */
    List<UserDo> getUserInfos(int start, int offset);

    /**
     * 删除用户
     *
     * @return 操作结果
     */
    CommConfig.CODES deleteUserInfo(Long id);

    /**
     * 编辑用户
     * @param userDo 用户信息
     * @return 是否成功
     */
    boolean editUser(UserDo userDo);

    /**
     * 查询评论 后台
     *
     * @return 评论集合
     * @param limit
     * @param page
     */
    PageDto<Comment> searchCommentBack(int limit, int page);

    /**
     * 根据 UIDs 查询 UserDos
     * @param userIds uid list
     * @return list for UserDo
     */
    Set<UserDo> getUsersByUidList(Set<Long> userIds);
}

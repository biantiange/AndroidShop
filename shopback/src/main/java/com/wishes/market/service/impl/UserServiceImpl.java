package com.wishes.market.service.impl;

import com.wishes.market.config.CommConfig;
import com.wishes.market.config.StringConstant;
import com.wishes.market.mapper.CommentMapper;
import com.wishes.market.mapper.UserDoMapperExt;
import com.wishes.market.model.*;
import com.wishes.market.service.UserService;
import com.wishes.market.utils.PageDto;
import com.wishes.market.utils.security.AESTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户Service实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    private UserDoMapperExt userDoMapperExt;
    @Resource
    private CommentMapper commentMapper;

    @Override
    public String login(UserDo user) {
        String account = user.getAccount();
        String password = user.getPassword();

        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)) {
            return null;
        } else {
            //构造查询条件
            UserDoExample example = new UserDoExample();
            UserDoExample.Criteria criteria = example.createCriteria();
            //查询数据库
            criteria.andAccountEqualTo(account);
            criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
            List<UserDo> userInDb = userDoMapperExt.selectByExample(example);
            if (!CollectionUtils.isEmpty(userInDb)) {
                String passInDb = userInDb.get(0).getPassword();
                Long uid = userInDb.get(0).getId();
                String userName = userInDb.get(0).getUname();

                //AES加密输入密码，并校对
                try {
                    String encryptPwdStr = AESTool.encrypt(password,
                            CommConfig.MARKET_KEY);
                    if (passInDb.equals(encryptPwdStr)) {
                        // TODO: 2019/1/24 可能要做一些持久化鉴权操作
                        // 先不做了算了！
                        return uid + "," + StringConstant.LOGIN_SUCCESS + "," + userName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("登陆失败！" + e);
                }

            }
        }

        return null;
    }

    @Override
    public String register(UserDo user) {
        String account = user.getAccount();
        String password = user.getPassword();
        String userName = user.getUname();

        //获取用户昵称
        if (StringUtils.isBlank(userName)) {
            user.setUname("新注册用户" + System.currentTimeMillis());
        }

        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)) {
            return null;
        } else {
            //构造查询条件
            UserDoExample example = new UserDoExample();
            UserDoExample.Criteria criteria = example.createCriteria();
            //查询数据库
            criteria.andAccountEqualTo(account);
            criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
            List<UserDo> userInDb = userDoMapperExt.selectByExample(example);
            if (!CollectionUtils.isEmpty(userInDb)) {
                return StringConstant.REGISTER_FAIL_REASON_DUPLICATE_ACCOUNT;
            } else {
                //将密码进行加密
                try {
                    password = AESTool.encrypt(password, CommConfig.MARKET_KEY);
                    user.setPassword(password);
                    //存入数据库
                    userDoMapperExt.insertSelective(user);
                    return StringConstant.REGISTER_SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("注册失败！" + e);
                }
            }
        }

        return null;
    }

    /**
     * 修改密码、昵称等用户数据
     *
     * @param user 用户信息
     * @return true/false 成功/失败
     */
    @Override
    public boolean modifyUserInfo(UserDo user) {
        return userDoMapperExt.updateByPrimaryKeySelective(user) > 0;
    }

    @Override
    public UserDo getUserInfoByUid(Long uId) {
        return userDoMapperExt.selectByPrimaryKey(uId);
    }

    @Override
    public void logout(Integer uid) {

    }

    @Override
    public boolean isExistUser(Long uid) {
        return userDoMapperExt.selectByPrimaryKey(uid) != null;
    }

    @Override
    public boolean isExistUser(String account) {
        if (StringUtils.isBlank(account)) {
            return false;
        }

        UserDoExample example = new UserDoExample();
        UserDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andAccountEqualTo(account);
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());

        List<UserDo> user = userDoMapperExt.selectByExample(example);

        return !CollectionUtils.isEmpty(user);
    }

    @Override
    public String insertComment(Comment comment) {
        int insert = commentMapper.insert(comment);
        if (insert > 0) {
            return "succeed";
        }
        return null;
    }

    @Override
    public String removeComment(Long commentId) {
        int delete = commentMapper.deleteByPrimaryKey(commentId);
        if (delete > 0) {
            return "succeed";
        }
        return null;
    }

    @Override
    public List<Comment> searchComment(Long postId) {
        return commentMapper.queryComments(postId);

    }

    @Override
    public Long getCommentNumber(Long commodityId) {
        return commentMapper.queryCommentNumber(commodityId);
    }

    @Override
    public List<UserDo> getUserInfos(int start, int offset) {
        return userDoMapperExt.getUserInfos(start,offset);
    }

    @Override
    public CommConfig.CODES deleteUserInfo(Long id) {
        UserDoExample example = new UserDoExample();
        UserDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        criteria.andIdEqualTo(id);
        //获取查询结果
        List<UserDo> userList = userDoMapperExt.selectByExample(example);

        if (CollectionUtils.isEmpty(userList)) {
            //如果结果集为空，则返回目标信息未找到
            return CommConfig.CODES.TARGET_NOT_FOUND;
        } else {
            //因为根据Id定位的数据肯定只有1条或0条，所以这里直接get(0)
            UserDo userDo = userList.get(0);

            userDoMapperExt.deleteByPrimaryKey(userDo);

            return CommConfig.CODES.SUCCESS;
        }
    }

    @Override
    public boolean editUser(UserDo userDo) {
        userDoMapperExt.updateByPrimaryKey(userDo);
        return true;
    }

    @Override
    public PageDto<Comment> searchCommentBack(int limit, int page) {

        //计算起始值和步长
        int start = limit * (page - 1);
        int offset = limit;

        //计算总数
        int total = Math.toIntExact(commentMapper.queryCommentNumberBack());
        PageDto<Comment> commentPageDto = new PageDto<>(page, limit, total);
        List<Comment> list = commentMapper.queryCommentsBack(start, offset);
        commentPageDto.setLists(list);

        return commentPageDto;
    }

    @Override
    public Set<UserDo> getUsersByUidList(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()){
            return null;
        }
        ArrayList<Long> userList = new ArrayList<>(userIds);
        List<UserDo> usersByUidList = userDoMapperExt.getUsersByUidList(userList);
        if (usersByUidList == null || usersByUidList.isEmpty()){
            return null;
        }
        return new HashSet<>(usersByUidList);
    }
}

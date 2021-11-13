package com.wishes.market.service.impl;

import com.wishes.market.mapper.PostMapper;
import com.wishes.market.mapper.PraiseMapper;
import com.wishes.market.model.Post;
import com.wishes.market.model.Praise;
import com.wishes.market.service.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Resource
    private PostMapper postMapper;
    @Resource
    private PraiseMapper praiseMapper;

    @Override
    public int insertPraise(Praise praise) {
        return praiseMapper.insert(praise);
    }

    @Override
    public int deleteByPrimaryKey(long userId, long postId) {
        int delete = praiseMapper.deleteByPrimaryKey(userId, postId);
        if (delete != 1) {
            return 0;
        }
        return delete;
    }

    @Override
    public int queryPraiseCount(Long postId) {
        return praiseMapper.queryPraiseCount(postId);
    }

    @Override
    public List<Long> getPraisesByPostIdList(List<Long> postIdList, Long userId) {
        List<Long> userPraisedPostIds = praiseMapper.getPraisesByPostIdList(postIdList, userId);
        if (userPraisedPostIds == null) {
            return new ArrayList<>();
        }
        return userPraisedPostIds;
    }

    @Override
    public int insertPost(Post post) {
        int insert = postMapper.insert(post);
        if (insert != 1) {
            return 0;
        }
        return insert;
    }

    @Override
    public int deleteByPrimaryKey(long postId) {
        int delete = postMapper.deleteByPrimaryKey(postId);
        if (delete != 1) {
            return 0;
        }
        return delete;
    }

    @Override
    public List<Post> queryPostsBack(int start, int offset) {
        List<Post> posts = postMapper.queryPostsBack(start, offset);
        if (posts == null) {
            return new ArrayList<>();
        }
        return posts;
    }

    @Override
    public Long countPost() {
        return postMapper.countPost();
    }

    @Override
    public Long countPost(Long userId) {
        return postMapper.countPostByUserId(userId);
    }
}

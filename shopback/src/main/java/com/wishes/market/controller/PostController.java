package com.wishes.market.controller;

import com.wishes.market.dto.CartDto;
import com.wishes.market.model.*;
import com.wishes.market.service.PostService;
import com.wishes.market.service.UserService;
import com.wishes.market.utils.PageDto;
import com.wishes.market.utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
public class PostController {
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    /**
     * 点赞 或 恢复点赞
     */
    @RequestMapping(value = "/addPraise", method = RequestMethod.POST)
    public ResultUtil addPraise(Long userId, Long postId) {

        // replace 1：插入成功 或者已存在 ； 2：重新点赞成功
        int replace = postService.insertPraise(new Praise(userId, postId, 0));
        if (replace == 0) {
            return ResultUtil.fail();
        }
        return ResultUtil.success();
    }

    /**
     * 取消点赞
     *
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 影响行数
     */
    @RequestMapping(value = "/deletePraise", method = RequestMethod.POST)
    public ResultUtil deletePraise(Long userId, Long postId) {
        int delete = postService.deleteByPrimaryKey(userId, postId);
        if (delete != 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail("delete fail!");
    }

    /**
     * 根据 帖子ID 获取 该帖子的点赞数
     *
     * @param postId 帖子ID
     * @return 「message，null，count」：count 为该帖子的点赞数。
     */
    @RequestMapping(value = "/queryPraiseCount", method = RequestMethod.GET)
    public ResultUtil queryPraiseCount(Long postId) {
        int count = postService.queryPraiseCount(postId);
        return ResultUtil.success("the post praise count", null, count + "");
    }


    /**
     * 添加帖子（发帖）
     *
     * @param post 帖子
     * @return 是否添加成功
     */
    @RequestMapping(value = "/addPost", method = RequestMethod.POST)
    public ResultUtil addPost(Post post) {
        int insert = postService.insertPost(post);
        if (insert == 0) {
            return ResultUtil.fail();
        }
        return ResultUtil.success();
    }

    /**
     * 删除帖子
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    @RequestMapping(value = "/deletePost", method = RequestMethod.GET)
    public ResultUtil deletePost(Long postId) {
        int delete = postService.deleteByPrimaryKey(postId);
        if (delete != 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail("delete fail!");
    }


    /**
     * 查询所有帖子 分页
     *
     * @param limit 每页数据长度
     * @param page  当前页码
     * @return 分页数据
     */
    @ApiOperation(httpMethod = "GET", value = "查询所有帖子", notes = "查询所有帖子",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "分页显示条数",
                    paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "page", value = "当前页面页码",
                    paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "userId", value = "当前用户ID",
                    paramType = "query", dataType = "Long", required = true)
    })
    @RequestMapping(value = "/getPosts", method = RequestMethod.GET)
    public ResultUtil getPosts(int limit, int page, Long userId, int yesMine) {
        System.out.println(limit + " " + page + " " + userId);
        //计算起始值和步长
        int start = limit * (page - 1);
        int offset = limit;

        List<Post> posts = postService.queryPostsBack(start, offset);
        System.out.println("============posts==============");
        System.out.println(posts);
        System.out.println("==============posts============");
        Long count = postService.countPost();

        if (yesMine == 1) {
            ArrayList<Post> posts1 = new ArrayList<>();
            for (Post post : posts) {
                if (Objects.equals(post.getUserId(), userId)) {
                    posts1.add(post);
                }
            }
            posts = posts1;
            count = postService.countPost(userId);
            System.out.println("=========mine========");
            System.out.println(posts);
            System.out.println("=========mine========");

        }

        //获得 postIds
        List<Long> postIds = posts.stream().map(Post::getPostId).collect(Collectors.toList());
        //被该用户点赞的帖子IDs
        List<Long> bePraisedPosts = new ArrayList<>();
        int emptyFlag = 0;
        if (!postIds.isEmpty()) {
            bePraisedPosts = postService.getPraisesByPostIdList(postIds, userId);
            emptyFlag++;
        }

        for (Post post : posts) {
            if (emptyFlag > 0 && bePraisedPosts.contains(post.getPostId())) {
                post.setPraiseFlag(true);
            }
            int pCount = postService.queryPraiseCount(post.getPostId());
            post.setpCount(String.valueOf(pCount));
            List<Comment> comments = userService.searchComment(post.getPostId());
            if (comments == null || comments.isEmpty()) {
                continue;
            }
            //将 comments 按 uid 分组
            HashMap<Long, List<Comment>> uidForCommentListMap = new HashMap<>();
            for (Comment comment : comments) {
                // 如果get出来的 value 为 null 就 new 一个 ArrayList 放进去
                uidForCommentListMap.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>());
                uidForCommentListMap.get(comment.getUserId()).add(comment);
            }
            //查询 对应的 User 信息
            Set<UserDo> users = userService.getUsersByUidList(uidForCommentListMap.keySet());
            //建立 uid -> User 映射
            HashMap<Long, UserDo> uidForUserMap = new HashMap<>();
            for (UserDo user : users) {
                uidForUserMap.put(user.getId(), user);
            }

            //最终返回结果 commentModels
            ArrayList<CommentModel> commentModels = new ArrayList<>();
            for (Long uid : uidForCommentListMap.keySet()) {
                for (Comment comment : uidForCommentListMap.get(uid)) {
                    commentModels.add(new CommentModel(comment.getCommentId()
                            , Objects.equals(uid, userId)//该评论是不是自己的（当前用户）
                            , uidForUserMap.get(uid).getUname()
                            , comment.getContent()
                            , comment.getCreateTime()));
                }

            }
            // stream 排序（按照创建时间 降序）
            final List<CommentModel> CommentModelCollect = commentModels.stream().sorted(Comparator.comparing(CommentModel::getCreateTime).reversed())
                    .collect(Collectors.toList());
            post.setCommentModels(CommentModelCollect);
        }
        System.out.println(posts);

        return ResultUtil.success("查询所有帖子成功！", posts, String.valueOf(count));

    }


}

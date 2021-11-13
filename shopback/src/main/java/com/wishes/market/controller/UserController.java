package com.wishes.market.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wishes.market.config.CommConfig;
import com.wishes.market.dto.CartDto;
import com.wishes.market.model.Comment;
import com.wishes.market.model.CommodityDo;
import com.wishes.market.model.UserDo;
import com.wishes.market.model.UserDoDate;
import com.wishes.market.service.UserService;
import com.wishes.market.utils.PageDto;
import com.wishes.market.utils.ResultUtil;
import com.wishes.market.utils.security.AESTool;
import com.wishes.market.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONStringer;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 登录、登出、注册用Controller
 */
@Slf4j
@RestController
@RequestMapping("/control")
@Api(tags = "登录登出及注册接口")
public class UserController {
    @Autowired(required = false)
    private UserService userService;

    /**
     * 登录
     *
     * @param user 实体对象
     * @return
     */
    @ApiOperation(value = "登录", notes = "登录接口",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号",
                    required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码",
                    required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResultUtil login(UserDo user) {
        String result = userService.login(user);
        if (StringUtils.isBlank(result)) {
            return ResultUtil.fail("登陆失败");
        } else {
            String[] results = result.split(",");
            return ResultUtil.success(results[1],
                    results[0] + "," + results[2]);
        }


    }

    /**
     * 注册
     *
     * @param user 实体对象
     * @return
     */
    @ApiOperation(value = "注册", notes = "注册接口",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号",
                    required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码",
                    required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "uname", value = "用户昵称",
                    paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultUtil register(UserDo user) {
        String msg = userService.register(user);
        return msg != null ? ResultUtil.success(msg) :
                ResultUtil.fail("注册失败");
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param uId 用户id
     * @return 用户信息实体
     */
    @ApiOperation(value = "根据用户id查询用户信息", notes = "根据用户id查询用户信息接口",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uId", value = "用户id",
                    required = true, paramType = "query", dataType = "Long")
    })
    @RequestMapping(value = "/getUserInfoByUid", method = RequestMethod.GET)
    public ResultUtil getUserInfoByUid(Long uId) {
        if (uId == null || uId < 0) {
            throw new IllegalArgumentException("输入的用户id不合法！");
        }
        UserDo userInfo = userService.getUserInfoByUid(uId);

        if (userInfo != null) {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(userInfo, vo);
            return ResultUtil.success("查询用户信息成功！", vo);
        } else {
            return ResultUtil.fail("根据用户id查询用户信息失败！");
        }
    }

    /**
     * 查询所有用户信息 后台
     *
     * @return 用户信息实体
     */
    @ApiOperation(value = "查询所有用户信息", notes = "查询所有用户信息",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "分页显示条数",
                    paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "page", value = "当前页面页码",
                    paramType = "query", dataType = "int", required = true)
    })
    @RequestMapping(value = "/getUserInfos", method = RequestMethod.GET)
    public ResultUtil getUserInfos(int limit, int page) {

        //计算起始值和步长
        int start = limit * (page - 1);
        int offset = limit;
        List<UserDo> userInfos = userService.getUserInfos(start,offset);
        List<UserDoDate> userDoDates = new ArrayList<>();
        for (UserDo userInfo : userInfos) {
            UserDoDate userDoDate = new UserDoDate(userInfo);
            userDoDates.add(userDoDate);
        }
        return ResultUtil.success("查询用户信息成功！", userDoDates, String.valueOf(userInfos.size()));
    }


    /**
     * 删除用户 后台
     *
     * @param id 用户id
     * @return 状态码
     * @throws IllegalArgumentException 参数不合法异常
     */
    @ApiOperation(httpMethod = "POST", value = "删除一个用户", notes =
            "删除一个用户", response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/deleteUserInfo", method = RequestMethod.POST)
    public ResultUtil deleteUserInfo(Long id) throws IllegalArgumentException {

        CommConfig.CODES code = userService.deleteUserInfo(id);

        return ResultUtil.success(code.getMessage());
    }

    /**
     * 编辑用户 后台
     *
     * @param userDo 用户信息
     * @return ResultUtil
     */
    @ResponseBody
    @PostMapping("/editUser")
    public ResultUtil editUser(UserDo userDo) {
        if (userService.editUser(userDo)) {
            return ResultUtil.success("用户信息修改成功！");
        }

        return ResultUtil.fail("用户信息修改失败！");
    }

    /**
     * 根据用户id修改用户昵称
     *
     * @param uId      用户id
     * @param userName 用户昵称
     * @return 结果result
     */
    @ApiOperation(value = "根据用户id修改用户昵称与密码", notes = "根据用户id修改用户昵称",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uId", value = "用户id",
                    required = true, paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "userName", value = "用户昵称", required =
                    true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required =
                    true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/changeUserInfo", method = RequestMethod.POST)
    public ResultUtil changeUserInfo(Long uId, String userName,
                                     String password) {
        //数据合法性判断
        if (uId == null || uId < 0) {
            return ResultUtil.fail("uId不合法！");
        }

        if (userName.length() > CommConfig.MAX_USERNAME_LENGTH) {
            return ResultUtil.fail("用户名不合法！用户名过长！");
        }

        if (password.length() > CommConfig.MAX_PASSWORD_LENGTH) {
            return ResultUtil.fail("密码长度过长！");
        }

        //将数据添加进实体
        UserDo user = new UserDo();
        user.setId(uId);
        if (StringUtils.isNotBlank(userName)) {
            user.setUname(userName);
        }

        if (StringUtils.isNotBlank(password)) {
            //使用AES加密密码
            try {
                password = AESTool.encrypt(password, CommConfig.MARKET_KEY);
                user.setPassword(password);
            } catch (Exception e) {
                log.error("使用AES加密密码失败！", e);
                return ResultUtil.fail("修改失败");
            }

        }

        return userService.modifyUserInfo(user) ?
                ResultUtil.success("修改成功！") :
                ResultUtil.fail("修改失败！");
    }

    /**
     * 添加评论
     *
     * @param comment 前端传过来的 comment
     * @return 数据插入反馈
     */
    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ResultUtil addComment(Comment comment) {
        System.out.println("UserController : addComment : comment : " + comment.toString());
        String s = userService.insertComment(comment);
        if (s != null) {
            return ResultUtil.success();
        }
        return ResultUtil.fail("insert fail!");
    }

    /**
     * 删除评论（修改 deleteFlag 状态）
     *
     * @param commentId 评论ID（主键）
     * @return 数据修改反馈(当该评论已经被删除之后[数据库里deleteFlag = 1] ， ， 也会返回修改失败)
     */
    @RequestMapping(value = "/deleteComment", method = RequestMethod.GET)
    public ResultUtil deleteComment(Long commentId) {
        System.out.println("UserController : deleteComment : commentId : " + commentId);
        String s = userService.removeComment(commentId);
        if (s != null) {
            return ResultUtil.success();
        }
        return ResultUtil.fail("delete fail!");
    }

//    /**
//     * 查询评论
//     *
//     * @param postId 商品ID
//     * @return 评论集合
//     */
//    @RequestMapping(value = "/searchComment", method = RequestMethod.GET)
//    public ResultUtil searchComment(Long postId) {
//        System.out.println("UserController : searchComment :  postId : " + postId);
//        List<Comment> comments = userService.searchComment(postId);
//        if (comments == null) {
//            return ResultUtil.fail();
//        }
//        //将 comments 按 uid 分组
//        HashMap<Long, List<Comment>> uidForCommentListMap = new HashMap<>();
//        for (Comment comment : comments) {
//            // 如果get出来的 value 为 null 就 new 一个 ArrayList 放进去
//            uidForCommentListMap.computeIfAbsent(comment.getUserId(), k -> new ArrayList<>());
//            uidForCommentListMap.get(comment.getUserId()).add(comment);
//        }
//        //查询 对应的 User 信息
//        Set<UserDo> users = userService.getUsersByUidList(uidForCommentListMap.keySet());
//        //建立 uid -> User 映射
//        HashMap<Long, UserDo> uidForUserMap = new HashMap<>();
//        for (UserDo user : users) {
//            uidForUserMap.put(user.getId(), user);
//        }
//        //局部内部类 CommentModel
//        class CommentModel {
//            protected final Long commentId;
//            protected final String userName;
//            protected final String content;
//            protected final String createTime;
//
//            public CommentModel(Long commentId, String userName, String content, String createTime) {
//                this.commentId = commentId;
//                this.userName = userName;
//                this.content = content;
//                this.createTime = createTime;
//            }
//
//            public String getCreateTime() {
//                return createTime;
//            }
//        }
//        //最终返回结果 commentModels
//        ArrayList<CommentModel> commentModels = new ArrayList<>();
//        for (Long uid : uidForCommentListMap.keySet()) {
//            for (Comment comment : uidForCommentListMap.get(uid)) {
//                commentModels.add(new CommentModel(comment.getCommentId()
//                        , uidForUserMap.get(uid).getUname()
//                        , comment.getContent()
//                        , comment.getCreateTime()));
//            }
//
//        }
//        // stream 排序（按照创建时间 降序）
//        List<CommentModel> CommentModelCollect = commentModels.stream().sorted(Comparator.comparing(CommentModel::getCreateTime).reversed())
//                .collect(Collectors.toList());
//        return ResultUtil.success("1", CommentModelCollect, CommentModelCollect.size() + "");
//    }

    /**
     * 查询评论数目
     *
     * @param postId 帖子ID
     * @return 评论数目
     */
    @RequestMapping(value = "/searchCommentNumber", method = RequestMethod.GET)
    public ResultUtil searchCommentNumber(Long postId) {
        System.out.println("UserController : searchCommentNumber :  postId : " + postId);
        Long commentNumber = userService.getCommentNumber(postId);
        return ResultUtil.success(commentNumber);
    }

    /**
     * 查询所有评论 后台
     *
     * @param limit 每页数据长度
     * @param page  当前页码
     * @return 分页数据
     */
    @ApiOperation(httpMethod = "GET", value = "查询购物车数据", notes = "查询购物车数据",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "分页显示条数",
                    paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "page", value = "当前页面页码",
                    paramType = "query", dataType = "int", required = true)
    })
    @RequestMapping(value = "/searchCommentBack", method = RequestMethod.GET)
    public ResultUtil searchCommentBack(int limit, int page) {
        System.out.println("UserController : searchComment");
        PageDto<Comment> comments = userService.searchCommentBack(limit, page);
        if (comments == null) {
            return ResultUtil.fail();
        }
        return ResultUtil.success("查询评论成功", comments.getLists(), String.valueOf(comments.getTotal()));
    }

}

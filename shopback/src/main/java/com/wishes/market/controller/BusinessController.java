package com.wishes.market.controller;

import com.wishes.market.config.CommConfig;
import com.wishes.market.dto.CartDto;
import com.wishes.market.dto.CommodityQueryRequest;
import com.wishes.market.model.CartSpecs;
import com.wishes.market.model.Collection;
import com.wishes.market.model.Praise;
import com.wishes.market.model.Specs;
import com.wishes.market.service.BusinessService;
import com.wishes.market.utils.PageDto;
import com.wishes.market.utils.ResultUtil;
import com.wishes.market.vo.CommodityTypeVo;
import com.wishes.market.vo.CommodityDo;
import com.wishes.market.vo.TopFive;
import com.wishes.market.vo.TypeTopFive;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品相关操作Controller
 */
@Slf4j
@RestController
@RequestMapping("/business")
@Api(tags = "业务相关接口")
public class BusinessController {
    @Autowired(required = false)
    private BusinessService businessService;

    /**
     * 分页查询
     *
     * @param request 基本请求体
     * @return 分页
     */
    @ApiOperation(httpMethod = "GET", value = "分页查询商品", notes = "分页查询商品接口",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodityType", value = "商品类型id",
                    paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public PageDto<CommodityDo> queryCommodities(CommodityQueryRequest request) {
        return businessService.queryCommodities(request);
    }

    /**
     * 分页查询 后台
     *
     * @param request 基本请求体
     * @return 分页
     */
    @ApiOperation(httpMethod = "GET", value = "分页查询商品", notes = "分页查询商品接口",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commodityType", value = "商品类型id",
                    paramType = "query", dataType = "int")
    })
    @RequestMapping(value = "/queryPageBack", method = RequestMethod.GET)
    public ResultUtil queryCommoditiesBack(CommodityQueryRequest request) {
        //TODO 转成layui要求格式

        PageDto<CommodityDo> commodityVoPageDto = businessService.queryCommodities(request);
        List<CommodityDo> lists = commodityVoPageDto.getLists();
        return ResultUtil.success("查询成功！", lists, String.valueOf(commodityVoPageDto.getTotal()));

    }


    /**
     * 查询商品类别
     *
     * @return ResultUtil 商品类别json
     */
    @ApiOperation(httpMethod = "GET", value = "查询商品类型", notes = "查询商品类型接口",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/queryCommodityTypes", method = RequestMethod.GET)
    public ResultUtil queryCommodityTypes() {
        List<CommodityTypeVo> voList = new ArrayList<>();

        try {
            businessService.queryCommodityTypes()
                    .forEach(commodityTypeDo -> {
                        CommodityTypeVo vo = new CommodityTypeVo();
                        BeanUtils.copyProperties(commodityTypeDo, vo);
                        voList.add(vo);
                    });
            return ResultUtil.success("查询成功！", voList);
        } catch (Exception e) {
            log.error("获取商品类别信息失败！", e);
        }

        return ResultUtil.fail("获取商品类别信息失败！");
    }

    /**
     * 根据商品id查询商品详情
     *
     * @param CommodityId 商品id
     * @return 商品model
     */
    @ApiOperation(httpMethod = "GET", value = "根据商品id查询商品详情", notes = "根据商品id查询商品详情",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CommodityId", value = "商品id", paramType = "query",
                    dataType = "Long", required = true)
    })
    @RequestMapping(value = "/queryCommodityInfoById", method = RequestMethod.GET)
    public CommodityDo queryCommodityInfoById(Long CommodityId) {
        CommodityDo vo = new CommodityDo();
        BeanUtils.copyProperties(businessService.queryCommodityInfoById(CommodityId), vo);
        return vo;
    }

    /**
     * 根据商品名模糊查询商品信息
     *
     * @param CommodityName 商品名
     * @return 商品model list
     */
    @ApiOperation(httpMethod = "GET", value = "根据商品名模糊查询商品信息列表", notes = "根据商品名模糊查询商品信息列表",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "CommodityName", value = "商品名", paramType = "query",
                    dataType = "String", required = true)
    })
    @RequestMapping(value = "/queryCommodityInfoByName", method = RequestMethod.GET)
    public List<CommodityDo> queryCommodityInfoByName(String CommodityName) {
        return businessService.queryCommodityInfoByName(CommodityName);
    }

    /**
     * 添加进购物车
     *
     * @param uId     用户id
     * @param cIds    商品id们
     * @param numbers 购买数量们
     * @return 状态码
     * @throws IllegalArgumentException 参数不合法异常
     */
    @ApiOperation(httpMethod = "POST", value = "将商品添加进购物车", notes = "将商品添加进购物车",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    public ResultUtil addToCart(Long uId, Long[] cIds, Integer[] numbers) throws IllegalArgumentException {
        for (int i = 0; i < cIds.length; i++) {
            CommConfig.CODES code = businessService
                    .addToCart(cIds[i], uId, numbers[i]);
            if (CommConfig.CODES.SUCCESS
                    .getCode().equals(code.getCode())) {
                continue;
            } else {
                return ResultUtil.success(code.getMessage());
            }
        }

        return ResultUtil.success(CommConfig.CODES.SUCCESS.getMessage());
    }

    /**
     * 刷新购物车数据
     *
     * @param uId     用户id
     * @param cIds    商品id们
     * @param numbers 购买数量们
     * @return 状态码
     * @throws IllegalArgumentException 参数不合法异常
     */
    @ApiOperation(httpMethod = "POST", value = "刷新购物车数据", notes = "刷新购物车数据",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/refreshCart", method = RequestMethod.POST)
    public ResultUtil refreshCart(Long uId, Long[] cIds, Integer[] numbers) throws IllegalArgumentException {
        for (int i = 0; i < cIds.length; i++) {
            CommConfig.CODES code = businessService
                    .refreshCart(cIds[i], uId, numbers[i]);
            if (CommConfig.CODES.SUCCESS
                    .getCode().equals(code.getCode())) {
                continue;
            } else {
                return ResultUtil.success(code.getMessage());
            }
        }

        return ResultUtil.success(CommConfig.CODES.SUCCESS.getMessage());
    }

    /**
     * 购买
     *
     * @param uId     用户id
     * @param cIds    商品id们
     * @param numbers 购买数量们
     * @return 状态码
     * @throws IllegalArgumentException 参数不合法异常
     */
    @ApiOperation(httpMethod = "POST", value = "购买商品", notes = "购买商品",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/buy", method = RequestMethod.POST)
    public ResultUtil buy(Long uId,
                          Long[] cIds, Integer[] numbers) throws IllegalArgumentException {
        for (int i = 0; i < cIds.length; i++) {
            CommConfig.CODES code = businessService.buy(cIds[i], uId,
                    numbers[i]);
            if (CommConfig.CODES.SUCCESS.getCode().equals(code.getCode())) {
                continue;
            } else {
                return ResultUtil.success(code.getMessage());
            }
        }
        return ResultUtil.success(CommConfig.CODES.SUCCESS.getMessage());
    }

    /**
     * 查询用户购买历史l
     *
     * @param uId   用户id
     * @param limit 每页数据长度
     * @param page  当前页码
     * @return 分页数据
     */
    @ApiOperation(httpMethod = "GET", value = "查询用户购买历史", notes = "查询用户购买历史",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uId", value = "用户id", paramType = "query",
                    dataType = "Long", required = true),
            @ApiImplicitParam(name = "limit", value = "分页显示条数",
                    paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "page", value = "当前页面页码",
                    paramType = "query", dataType = "int", required = true)
    })
    @RequestMapping(value = "/queryBoughtList", method = RequestMethod.GET)
    public PageDto<CartDto> queryBoughtList(Long uId, int limit, int page) {
        return businessService.queryBoughtList(uId, limit, page);
    }

    /**
     * 查询所有订单 后台
     *
     * @param limit 每页数据长度
     * @param page  当前页码
     * @return 分页数据
     */
    @ApiOperation(httpMethod = "GET", value = "查询所有订单", notes = "查询所有订单",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "分页显示条数",
                    paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "page", value = "当前页面页码",
                    paramType = "query", dataType = "int", required = true)
    })
    @RequestMapping(value = "/queryBoughtListBack", method = RequestMethod.GET)
    public ResultUtil queryBoughtListBack(int limit, int page) {
        //TODO 转成layui要求格式

        PageDto<CartDto> cartDtoPageDto = businessService.queryBoughtListBack(limit, page);
        List<CartDto> lists = cartDtoPageDto.getLists();
        return ResultUtil.success("查询成功！", lists, String.valueOf(cartDtoPageDto.getTotal()));

    }

    /**
     * 查询用户购物车
     *
     * @param uId   用户id
     * @param limit 每页数据长度
     * @param page  当前页码
     * @return 分页数据
     */
    @ApiOperation(httpMethod = "GET", value = "查询购物车数据", notes = "查询购物车数据",
            response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uId", value = "用户id", paramType = "query",
                    dataType = "Long", required = true),
            @ApiImplicitParam(name = "limit", value = "分页显示条数",
                    paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "page", value = "当前页面页码",
                    paramType = "query", dataType = "int", required = true)
    })
    @RequestMapping(value = "/queryCartList", method = RequestMethod.GET)
    public PageDto<CartDto> queryCartList(Long uId, int limit, int page) {
        return businessService.queryCartList(uId, limit, page);
    }

    /**
     * 查询所有用户购物车 后台
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
    @RequestMapping(value = "/queryCartListBack", method = RequestMethod.GET)
    public ResultUtil queryCartListBack(int limit, int page) {
        PageDto<CartDto> cartDtoPageDto = businessService.queryCartListBack(limit, page);
        List<CartDto> lists = cartDtoPageDto.getLists();
        return ResultUtil.success("查询成功！", lists, String.valueOf(cartDtoPageDto.getTotal()));
    }


    /**
     * 从购物车中删除
     *
     * @param uId     用户id
     * @param cIds    商品id们
     * @param numbers 购买数量们
     * @return 状态码
     * @throws IllegalArgumentException 参数不合法异常
     */
    @ApiOperation(httpMethod = "POST", value = "从购物车中批量删除商品(也支持删除一件)", notes =
            "从购物车中批量删除商品(也支持删除一件)", response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/deleteFromCart", method = RequestMethod.POST)
    public ResultUtil deleteFromCart(Long uId,
                                     Long[] cIds, Integer[] numbers) throws IllegalArgumentException {
        for (int i = 0; i < cIds.length; i++) {
            CommConfig.CODES code = businessService.deleteFromCart(cIds[i], uId,
                    numbers[i]);
            if (CommConfig.CODES.SUCCESS.getCode().equals(code.getCode())) {
                continue;
            } else {
                return ResultUtil.success(code.getMessage());
            }
        }
        return ResultUtil.success(CommConfig.CODES.SUCCESS.getMessage());
    }

    /**
     * 从商品中删除 后台
     *
     * @param cId 商品id
     * @return 状态码
     * @throws IllegalArgumentException 参数不合法异常
     */
    @ApiOperation(httpMethod = "POST", value = "从购物车中批量删除商品(也支持删除一件)", notes =
            "从购物车中批量删除商品(也支持删除一件)", response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/deleteFromCommodity", method = RequestMethod.POST)
    public ResultUtil deleteFromCommodity(Long cId) throws IllegalArgumentException {

        CommConfig.CODES code = businessService.deleteFromCommodity(cId);

        return ResultUtil.success(code.getMessage());
    }

    /**
     * 从订单中删除 后台
     *
     * @param id 订单id
     * @return 状态码
     */
    @ApiOperation(httpMethod = "POST", value = "从后台订单中删除一条", notes =
            "从后台订单中删除一条", response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/deleteFromBought", method = RequestMethod.POST)
    public ResultUtil deleteFromBought(Long id) throws IllegalArgumentException {

        CommConfig.CODES code = businessService.deleteFromBought(id);

        return ResultUtil.success(code.getMessage());
    }

    /**
     * 发货订单 后台
     *
     * @param id 订单id
     * @return 状态码
     */
    @ApiOperation(httpMethod = "POST", value = "发货订单", notes =
            "发货订单", response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/sendFromBought", method = RequestMethod.POST)
    public ResultUtil sendFromBought(Long id) throws IllegalArgumentException {

        CommConfig.CODES code = businessService.sendFromBought(id);

        return ResultUtil.success(code.getMessage());
    }

    /**
     * 添加商品规格
     *
     * @param specs 商品规格
     * @return 影响行数
     * @throws IllegalArgumentException 类型错误
     */
    @ApiOperation(httpMethod = "POST", value = "添加商品规格", notes =
            "添加商品规格", response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/addSpecs", method = RequestMethod.POST)
    public ResultUtil addSpecs(Specs specs) throws IllegalArgumentException {
        int insert = businessService.insertSpecs(specs);
        if (insert > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 根据 commodityId 修改 商品规格
     *
     * @param commodityId 商品ID
     * @param detail      规格细节
     * @return 影响行数
     * @throws IllegalArgumentException 类型错误
     */
    @RequestMapping(value = "/updateByCommodityId", method = RequestMethod.POST)
    public ResultUtil updateByCommodityId(Long commodityId, String detail) throws IllegalArgumentException {
        int update = businessService.updateByCommodityId(commodityId, detail);
        if (update > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 根据 commodityId 查询 Specs（商品规格）
     *
     * @param commodityId 商品ID
     * @return 商品规格
     * @throws IllegalArgumentException 类型错误
     */
    @RequestMapping(value = "/findSpecsByCommodityId", method = RequestMethod.GET)
    public ResultUtil findSpecsByCommodityId(Long commodityId) throws IllegalArgumentException {
        Specs specs = businessService.findSpecsByCommodityId(commodityId);
        if (specs.getId() > 0) {
            return ResultUtil.success("", specs, 999999 + "");
        }
        return ResultUtil.fail();
    }

    /**
     * 添加订单商品规格
     *
     * @param specs 商品规格
     * @return 影响行数
     * @throws IllegalArgumentException 类型错误
     */
    @ApiOperation(httpMethod = "POST", value = "添加订单商品规格", notes =
            "添加订单商品规格", response = ResultUtil.class, responseContainer = "Map", produces = "application/json")
    @RequestMapping(value = "/addSCartSpecs", method = RequestMethod.POST)
    public ResultUtil addCartSpecs(CartSpecs specs) throws IllegalArgumentException {
        int insert = businessService.insertCartSpecs(specs);
        if (insert > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 根据 cartId 修改 订单商品规格
     *
     * @param cartId 商品ID
     * @param detail      规格细节
     * @return 影响行数
     * @throws IllegalArgumentException 类型错误
     */
    @RequestMapping(value = "/updateByCartId", method = RequestMethod.POST)
    public ResultUtil updateByCartId(Long cartId, String detail) throws IllegalArgumentException {
        int update = businessService.updateByCartId(cartId, detail);
        if (update > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 根据 commodityId 查询 Specs（订单商品规格）
     *
     * @param cartId 商品ID
     * @return 订单商品规格
     * @throws IllegalArgumentException 类型错误
     */
    @RequestMapping(value = "/findCartSpecsByCartId", method = RequestMethod.GET)
    public ResultUtil findCartSpecsByCartId(Long cartId) throws IllegalArgumentException {
        CartSpecs cartSpecs = businessService.findCartSpecsByCartId(cartId);
        if (cartSpecs.getId() > 0) {
            return ResultUtil.success("", cartSpecs, 999999 + "");
        }
        return ResultUtil.fail();
    }

    /**
     * Commodity TOP 5
     *
     * @return Commodity TOP 5
     * @throws IllegalArgumentException 类型错误
     */
    @RequestMapping(value = "/findTopFiveCommodity", method = RequestMethod.GET)
    public ResultUtil findTopFiveCommodity() throws IllegalArgumentException {
        List<TopFive> topFives = businessService.topFiveCommodity();
        if (topFives != null) {
            return ResultUtil.success("", topFives, topFives.size() + "");
        }
        return ResultUtil.fail();
    }

    /**
     * type TOP 5
     *
     * @return type count TOP 5
     * @throws IllegalArgumentException 类型错误
     */
    @RequestMapping(value = "/findTopFiveForType", method = RequestMethod.GET)
    public ResultUtil findTopFiveForType() throws IllegalArgumentException {
        List<TypeTopFive> topFives = businessService.findTopFiveForType();
        if (topFives != null) {
            return ResultUtil.success("", topFives, topFives.size() + "");
        }
        return ResultUtil.fail();
    }

    /**
     * 收藏 ：userId 收藏了 该商品（commodityId）
     *
     * @param userId      用户ID
     * @param commodityId 商品ID
     * @return 影响行数
     */
    @RequestMapping(value = "/insertCollection", method = RequestMethod.POST)
    public ResultUtil insertCollection(Integer userId, Integer commodityId) throws IllegalArgumentException {
        System.out.println(userId + "===========" + commodityId);
        // replace 1：插入成功 或者已存在 ； 2：重新点赞成功
        int replace = businessService.insertCollection(new Collection(userId, commodityId, 0));
        if (replace == 0) {
            return ResultUtil.fail();
        }
        return ResultUtil.success();
    }

    /**
     * 取消点赞
     *
     * @param userId      用户ID
     * @param commodityId 商品ID
     * @return 影响行数
     */
    @RequestMapping(value = "/deleteCollection", method = RequestMethod.POST)
    public ResultUtil deleteCollection(Integer userId, Integer commodityId) {
        int delete = businessService.deleteCollectionByPrimaryKey(userId, commodityId);
        if (delete != 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail("delete fail!");
    }

    /**
     * 根据 userId 和 commodityId 查询该商品是否被该用户收藏
     *
     * @param userId      用户ID
     * @param commodityId 商品ID
     * @return
     */
    @RequestMapping(value = "/queryCollectionByCommodityId", method = RequestMethod.POST)
    public ResultUtil queryCollectionByCommodityId(Integer userId, Integer commodityId) {
        System.out.println(userId + "=========" + commodityId);
        Integer collection = businessService.queryCollectionByCommodityId(userId, commodityId);
        System.out.println(collection+"======");
        if (collection > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail("not exits");
    }


}

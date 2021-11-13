package com.wishes.market.service;

import com.wishes.market.config.CommConfig;
import com.wishes.market.dto.CartDto;
import com.wishes.market.dto.CommodityQueryRequest;
import com.wishes.market.model.*;
import com.wishes.market.utils.PageDto;
import com.wishes.market.vo.CommodityDo;
import com.wishes.market.vo.TopFive;
import com.wishes.market.vo.TypeTopFive;

import java.util.List;

/**
 * 商品相关service
 *
 * @author 郑龙
 * @date 2019-01-24  15:57
 */
public interface BusinessService {
    /**
     * 按商品类型id，分页查询商品
     *
     * @param request 商品分页查询请求
     * @return pageDto
     */
    PageDto<CommodityDo> queryCommodities(CommodityQueryRequest request);


    /**
     * 查询商品类别
     *
     * @return 商品类别list
     */
    List<CommodityTypeDo> queryCommodityTypes();

    /**
     * 根据商品id查询商品详情
     *
     * @param CommodityId 商品id
     * @return 商品Model
     */
    com.wishes.market.model.CommodityDo queryCommodityInfoById(Long CommodityId);

    /**
     * 根据商品名称查询商品详情
     *
     * @param CommodityName 商品名称
     * @return 商品Model
     */
    List<CommodityDo> queryCommodityInfoByName(String CommodityName);

    /**
     * 加入购物车
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 购买数量
     * @return CommConfig.CODES
     * @throws IllegalArgumentException 参数异常
     */
    CommConfig.CODES addToCart(Long cId, Long uId, int number) throws IllegalArgumentException;

    /**
     * 刷新购物车
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 购买数量
     * @return CommConfig.CODES
     * @throws IllegalArgumentException 参数异常
     */
    CommConfig.CODES refreshCart(Long cId, Long uId, int number) throws IllegalArgumentException;

    /**
     * 直接购买
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 购买数量
     * @return CommConfig.CODES
     * @throws IllegalArgumentException 参数异常
     */
    CommConfig.CODES buy(Long cId, Long uId, int number) throws IllegalArgumentException;

    /**
     * 从用户购物车中删除商品(一个一个地删除)
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 数量
     * @return CommConfig.CODES
     * @throws IllegalArgumentException 传入参数异常
     */
    CommConfig.CODES deleteFromCart(Long cId, Long uId, int number) throws IllegalArgumentException;

    /**
     * 分页查询用户购物车列表
     *
     * @param uId   用户id
     * @param limit 每页显示条数
     * @param page  页码
     * @return 分页数据
     */
    PageDto<CartDto> queryCartList(Long uId, int limit, int page);

    /**
     * 分页查询用户已购买列表
     *
     * @param uId   用户id
     * @param limit 每页显示条数
     * @param page  页码
     * @return 分页数据
     */
    PageDto<CartDto> queryBoughtList(Long uId, int limit, int page);

    CommConfig.CODES deleteFromCommodity(Long cId);

    PageDto<CartDto> queryBoughtListBack(int limit, int page);

    CommConfig.CODES deleteFromBought(Long id);

    CommConfig.CODES sendFromBought(Long id);

    PageDto<CartDto> queryCartListBack(int limit, int page);

    //<----商品规格---->
    int insertSpecs(Specs specs);

    int updateByCommodityId(Long commodityId,String detail);

    Specs findSpecsByCommodityId(Long commodityId);
    //</----商品规格---->

    //<----订单规格---->
    int insertCartSpecs(CartSpecs CartSpecs);

    int updateByCartId(Long CartId,String detail);

    CartSpecs findCartSpecsByCartId(Long CartId);
    //</----订单规格---->

    /**
     * 查询出 status = 待评价 的订单
     * @return 全部 "待评价"的订单
     */
    List<TopFive> topFiveCommodity();

    List<TypeTopFive> findTopFiveForType();

    /**
     * 新增收藏
     *
     * @param collection 收藏实例
     * @return 影响行数
     */
    int insertCollection(Collection collection);

    /**
     * 取消收藏
     *
     * @param userId      当前用户ID
     * @param commodityId 商品ID
     * @return 影响行数
     */
    int deleteCollectionByPrimaryKey(Integer userId, Integer commodityId);

    /**
     * 根据userId 和 commodityId
     */
    Integer queryCollectionByCommodityId(Integer userId, Integer commodityId);

}

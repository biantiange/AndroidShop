package com.wishes.market.service.impl;

import com.wishes.market.config.CommConfig;
import com.wishes.market.dto.CartDto;
import com.wishes.market.dto.CommodityQueryRequest;
import com.wishes.market.mapper.*;
import com.wishes.market.model.*;
import com.wishes.market.service.BusinessService;
import com.wishes.market.service.CommodityService;
import com.wishes.market.service.UserService;
import com.wishes.market.utils.PageDto;
import com.wishes.market.vo.CommodityDo;
import com.wishes.market.vo.TopFive;
import com.wishes.market.vo.TypeTopFive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务Service实现类
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class BusinessServiceImpl implements BusinessService {
    @Autowired(required = false)
    private CommodityService commodityService;

    @Autowired(required = false)
    private UserService userService;

    @Autowired(required = false)
    private CommodityDoMapperExt commodityDoMapperExt;

    @Autowired(required = false)
    private CommodityTypeDoMapperExt commodityTypeDoMapperExt;

    @Autowired(required = false)
    private CartDoMapperExt cartDoMapperExt;

    @Resource
    private SpecsMapper specsMapper;

    @Resource
    private CartSpecsMapper cartSpecsMapper;

    @Resource
    private CollectionMapper collectionMapper;

    @Override
    public PageDto<CommodityDo> queryCommodities(CommodityQueryRequest request) {
        CommodityDoExample example = new CommodityDoExample();
        CommodityDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        //添加筛选种类
        if (request.getCommodityType() != null) {
            criteria.andCommodityTypeEqualTo(request.getCommodityType());
        }

        Long total = commodityDoMapperExt.countByExample(example);
        if (total == 0L) {
            return null;
        }

        //校验limit与page的合法性
        int correctPageIndex = (int) (total / request.getLimit());
        if (total % request.getLimit() != 0) {
            correctPageIndex += 1;
        }

        //分页页码大于计算出来的最大值，则设置分页页码至最大值
        if (correctPageIndex < request.getPage()) {
            request.setPage(correctPageIndex);
        }

        //初始化分页参数
        PageDto<CommodityDo> page = new PageDto<>(request.getPage(),
                request.getLimit(), total.intValue());
        example.setPageDto(page);
        //排序规则
        example.setOrderByClause("id asc");
        //查询数据
        List<CommodityDo> list =
                commodityDoMapperExt.selectByExampleWithBLOBs(example).stream().map(commodityDo -> {
                    CommodityDo vo = new CommodityDo();
                    BeanUtils.copyProperties(commodityDo, vo);
                    return vo;
                }).collect(Collectors.toList());
        page.setLists(list);

        return page;
    }

    /**
     * 查询商品类别
     *
     * @return 商品类别list
     */
    @Override
    public List<CommodityTypeDo> queryCommodityTypes() {
        CommodityTypeDoExample example = new CommodityTypeDoExample();
        CommodityTypeDoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        return commodityTypeDoMapperExt.selectByExample(example);
    }

    @Override
    public com.wishes.market.model.CommodityDo queryCommodityInfoById(Long CommodityId) {
        return commodityDoMapperExt.selectByPrimaryKey(CommodityId);
    }

    @Override
    public List<CommodityDo> queryCommodityInfoByName(String CommodityName) {
        CommodityDoExample example = new CommodityDoExample();
        CommodityDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        criteria.andCommodityNameLike("%" + CommodityName + "%");
        //获得结果集
        return commodityDoMapperExt.selectByExampleWithBLOBs(example).stream()
                .map(commodityDo -> {
                    CommodityDo vo = new CommodityDo();
                    BeanUtils.copyProperties(commodityDo, vo);
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public CommConfig.CODES addToCart(Long cId, Long uId, int number) throws IllegalArgumentException {
        try {
            legalDetection(cId, uId, number);
            return save(cId, uId, number, 0, false);
        } catch (Exception e) {
            //如果是不合法入参异常，则抛出
            if (e instanceof IllegalArgumentException) {
                throw e;
            } else {
                e.printStackTrace();
                log.error("添加进购物车操作异常！" + e);
            }
        }
        return CommConfig.CODES.FAIL_OF_UNKNOW_REASON;
    }

    /**
     * 刷新购物车
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 购买数量
     * @return CommConfig.CODES
     * @throws IllegalArgumentException 参数异常
     */
    @Override
    public CommConfig.CODES refreshCart(Long cId, Long uId, int number) throws IllegalArgumentException {
        try {
            legalDetection(cId, uId, number);
            return save(cId, uId, number, 0, true);
        } catch (Exception e) {
            //如果是不合法入参异常，则抛出
            if (e instanceof IllegalArgumentException) {
                throw e;
            } else {
                e.printStackTrace();
                log.error("添加进购物车操作异常！" + e);
            }
        }
        return CommConfig.CODES.FAIL_OF_UNKNOW_REASON;
    }

    @Override
    public CommConfig.CODES buy(Long cId, Long uId, int number) throws IllegalArgumentException {
        try {
            legalDetection(cId, uId, number);
            return save(cId, uId, number, 1, false);
        } catch (Exception e) {
            //如果是不合法入参异常，则抛出
            if (e instanceof IllegalArgumentException) {
                throw e;
            } else {
                e.printStackTrace();
                log.error("添加进购物车操作异常！" + e);
            }
        }
        return CommConfig.CODES.FAIL_OF_UNKNOW_REASON;
    }

    /**
     * 从用户购物车中删除商品(一个一个地删除)
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 数量
     * @return CommConfig.CODES
     * @throws IllegalArgumentException 传入参数异常
     */
    @Override
    public CommConfig.CODES deleteFromCart(Long cId, Long uId, int number) throws IllegalArgumentException {
        try {
            legalDetection(cId, uId, number);
            return delete(cId, uId, number);
        } catch (Exception e) {
            //如果是不合法入参异常，则抛出
            if (e instanceof IllegalArgumentException) {
                throw e;
            } else {
                e.printStackTrace();
                log.error("从购物车中删除商品操作异常！" + e);
            }
        }

        return CommConfig.CODES.FAIL_OF_UNKNOW_REASON;
    }

    /**
     * 入参合法性检测。不合法会抛出异常
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 购买数量
     * @throws IllegalArgumentException
     */
    private void legalDetection(Long cId, Long uId, int number) throws IllegalArgumentException {
        if (!commodityService.isExistCommodity(cId)) {
            throw new IllegalArgumentException("输入的商品id不合法！商品可能不存在！");
        }
        if (!userService.isExistUser(uId)) {
            throw new IllegalArgumentException("用户id不合法！用户可能不存在！");
        }
        if (number <= 0) {
            throw new IllegalArgumentException("数量不合法！数量至少为1！");
        }
    }

    /**
     * 将数据保存进数据库
     *
     * @param cId           商品id
     * @param uId           用户id
     * @param number        购买数量
     * @param purchase      是否购买（0：仅添加进购物车；1：购买）
     * @param isRefreshMode 模式：false；新增进购物车；true：刷新数据
     * @return CommConfig.CODES 状态码
     */
    private CommConfig.CODES save(Long cId, Long uId, int number, int purchase, boolean isRefreshMode) {
        //先根据商品id查询商品单价
        com.wishes.market.model.CommodityDo commodity = commodityDoMapperExt.selectByPrimaryKey(cId);
        if (commodity == null) {
            //未查询到商品
            return CommConfig.CODES.COMMODITY_NOT_FOUND;
        }
        if (commodity.getCommoditySurplus() <= 0) {
            //商品库存为0
            return CommConfig.CODES.COMMODITY_SOLD_OUT;
        }
        if (commodity.getCommoditySurplus() - number < 0) {
            //库存不足
            return CommConfig.CODES.COMMODITY_SURPLUS_INSUFFICIENT;
        } else {
            commodity.setCommoditySurplus(commodity.getCommoditySurplus() - number);
        }

        //如果是购买，则需要将购买量从商品表中扣除
        if (purchase == 1) {
            commodityDoMapperExt.updateByPrimaryKeySelective(commodity);
        }

        //结合查询数据，计算剩余量并更新表记录
        CartDo cart = new CartDo();
        cart.setCid(cId.intValue());
        cart.setUid(uId.intValue());
        cart.setIsPurchased(purchase);
        cart.setNumber(number);
        cart.setTotalPrice(number * commodity.getCommodityPrice());
        cart.setIsDeleted(CommConfig.IS_DELETE.NO.getType());
        //查询有无购物车记录,选择新增或更新数据
        try {
            insertOrUpdate(cart, isRefreshMode);
            return CommConfig.CODES.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增购买信息失败！" + e);
            return CommConfig.CODES.FAIL_OF_BAD_NETWORK;
        }

    }

    /**
     * 将商品从购物车中删除
     *
     * @param cId    商品id
     * @param uId    用户id
     * @param number 商品数量
     * @return CommConfig.CODES
     */
    private CommConfig.CODES delete(Long cId, Long uId, int number) {
        CartDoExample example = new CartDoExample();
        CartDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        criteria.andIsPurchasedEqualTo(0);
        criteria.andUidEqualTo(uId.intValue());
        criteria.andCidEqualTo(cId.intValue());
        //获取查询结果
        List<CartDo> cartList = cartDoMapperExt.selectByExample(example);

        if (CollectionUtils.isEmpty(cartList)) {
            //如果结果集为空，则返回目标信息未找到
            return CommConfig.CODES.TARGET_NOT_FOUND;
        } else {
            //因为根据uId和cId定位的数据肯定只有1条或0条，所以这里直接get(0)
            CartDo cartDo = cartList.get(0);
            int cartNumber = cartDo.getNumber();

            if (number >= cartNumber) {
                //如果输入数量大于数据库中数量，则默认全部移除
                cartDoMapperExt.deleteByPrimaryKey(cartDo);
            } else {
                //设置剩余数量
                cartDo.setNumber(cartNumber - number);
                cartDoMapperExt.updateByPrimaryKeySelective(cartDo);
            }

            return CommConfig.CODES.SUCCESS;
        }
    }

    /**
     * 新增或更新
     *
     * @param cart          订单实体
     * @param isRefreshMode 模式：false；新增进购物车；true：刷新数据
     */
    private void insertOrUpdate(CartDo cart, boolean isRefreshMode) {
        CartDoExample example = new CartDoExample();
        CartDoExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        criteria.andCidEqualTo(cart.getCid());
        criteria.andUidEqualTo(cart.getUid());
        //查购物车记录，不管购买记录。
        criteria.andIsPurchasedEqualTo(0);
        //查询
        List<CartDo> list = cartDoMapperExt.selectByExample(example);

        switch (cart.getIsPurchased()) {
            //添加进购物车
            case 0:
                //更新或新增
                if (CollectionUtils.isEmpty(list)) {
                    //如果先无数据，则直接新增
                    cartDoMapperExt.insertSelective(cart);
                } else {
                    //如果先有数据,则更新
                    CartDo origin = list.get(0);
                    if (!isRefreshMode) {
                        origin.setNumber(origin.getNumber() + cart.getNumber());
                    } else {
                        origin.setNumber(cart.getNumber());
                    }
                    origin.setTotalPrice(origin.getTotalPrice() + cart.getTotalPrice());
                    cartDoMapperExt.updateByPrimaryKey(origin);
                }
                break;
            //购买
            case 1:
                //更新或新增
                if (CollectionUtils.isEmpty(list)) {
                    //如果先无数据，则直接新增
                    cartDoMapperExt.insertSelective(cart);
                } else {
                    //如果先有数据,则更新
                    CartDo origin = list.get(0);
                    if (cart.getNumber().intValue() == origin.getNumber().intValue()) {
                        //如果数量相同，则默认是清空购物车
                        origin.setIsPurchased(1);
                        cartDoMapperExt.updateByPrimaryKey(origin);
                    } else {
                        //否则不管购物车记录，直接新增购买记录
                        cartDoMapperExt.insertSelective(cart);
                    }

                }
                break;
            default:
                //其他情况，预留
                break;
        }

    }

    @Override
    public PageDto<CartDto> queryBoughtList(Long uId, int limit, int page) {
        //计算起始值和步长
        int start = limit * (page - 1);
        int offset = limit;

        //计算总数
        int total = cartDoMapperExt.countCart(uId, 1L);
        PageDto<CartDto> pageDto = new PageDto<>(page, limit, total);
        List<CartDto> list = cartDoMapperExt.queryCart(uId, 1L, start, offset);
        pageDto.setLists(list);

        return pageDto;
    }

    @Override
    public CommConfig.CODES deleteFromCommodity(Long cId) {
        CommodityDoExample example = new CommodityDoExample();
        CommodityDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        criteria.andIdEqualTo(cId);
        //获取查询结果
        List<com.wishes.market.model.CommodityDo> commodityList = commodityDoMapperExt.selectByExample(example);

        if (CollectionUtils.isEmpty(commodityList)) {
            //如果结果集为空，则返回目标信息未找到
            return CommConfig.CODES.TARGET_NOT_FOUND;
        } else {
            //因为根据uId和cId定位的数据肯定只有1条或0条，所以这里直接get(0)
            com.wishes.market.model.CommodityDo commodityDo = commodityList.get(0);

            commodityDoMapperExt.deleteByPrimaryKey(commodityDo);

            return CommConfig.CODES.SUCCESS;
        }
    }

    @Override
    public CommConfig.CODES deleteFromBought(Long id) {
        CartDoExample example = new CartDoExample();
        CartDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        criteria.andIdEqualTo(id);
        //获取查询结果
        List<CartDo> cartDoList = cartDoMapperExt.selectByExample(example);

        if (CollectionUtils.isEmpty(cartDoList)) {
            //如果结果集为空，则返回目标信息未找到
            return CommConfig.CODES.TARGET_NOT_FOUND;
        } else {
            //因为根据uId和cId定位的数据肯定只有1条或0条，所以这里直接get(0)
            CartDo cartDo = cartDoList.get(0);

            cartDoMapperExt.deleteByPrimaryKey(cartDo);

            return CommConfig.CODES.SUCCESS;
        }
    }

    @Override
    public CommConfig.CODES sendFromBought(Long id) {
        CartDoExample example = new CartDoExample();
        CartDoExample.Criteria criteria = example.createCriteria();
        //构造查询条件
        criteria.andIsDeletedEqualTo(CommConfig.IS_DELETE.NO.getType());
        criteria.andIdEqualTo(id);
        //获取查询结果
        List<CartDo> cartDoList = cartDoMapperExt.selectByExample(example);

        if (CollectionUtils.isEmpty(cartDoList)) {
            //如果结果集为空，则返回目标信息未找到
            return CommConfig.CODES.TARGET_NOT_FOUND;
        } else {
            //因为根据uId和cId定位的数据肯定只有1条或0条，所以这里直接get(0)
            CartDo cartDo = cartDoList.get(0);

            cartDoMapperExt.sendByPrimaryKey(cartDo);

            return CommConfig.CODES.SUCCESS;
        }
    }

    @Override
    public PageDto<CartDto> queryCartListBack(int limit, int page) {
        //计算起始值和步长
        int start = limit * (page - 1);

        //计算总数
        int total = cartDoMapperExt.countRealCartBack();
        PageDto<CartDto> pageDto = new PageDto<>(page, limit, total);
        List<CartDto> list = cartDoMapperExt.queryRealCartBack(start, limit);
        pageDto.setLists(list);

        return pageDto;
    }

    @Override
    public int insertSpecs(Specs specs) {
        int insert = specsMapper.insert(specs);
        return Math.max(insert, 0);
    }

    @Override
    public int updateByCommodityId(Long commodityId, String detail) {
        int update = specsMapper.updateByCommodityId(commodityId, detail);
        return Math.max(update, 0);
    }

    @Override
    public Specs findSpecsByCommodityId(Long commodityId) {
        Specs specs = specsMapper.findSpecsByCommodityId(commodityId);
        if (specs != null) {
            return specs;
        }
        return new Specs();
    }

    @Override
    public int insertCartSpecs(CartSpecs CartSpecs) {
        int insert = cartSpecsMapper.insert(CartSpecs);
        return Math.max(insert, 0);
    }

    @Override
    public int updateByCartId(Long CartId, String detail) {
        int update = cartSpecsMapper.updateByCartId(CartId, detail);
        return Math.max(update, 0);
    }

    @Override
    public CartSpecs findCartSpecsByCartId(Long CartId) {
        CartSpecs cartSpecs = cartSpecsMapper.findCartSpecsByCartId(CartId);
        if (cartSpecs != null) {
            return cartSpecs;
        }
        return new CartSpecs();
    }


    @Override
    public List<TopFive> topFiveCommodity() {
        //查询全部'待评价'订单
        List<CartDo> noCommentOrder = cartDoMapperExt.findNoCommentOrder();
        if (noCommentOrder == null || noCommentOrder.isEmpty()) {
            return new ArrayList<>();
        }
        //按 commodityId 分组
        HashMap<Integer, List<CartDo>> commodityIdCartDoListMap = new HashMap<>();
        noCommentOrder.forEach(cartDo -> {
            commodityIdCartDoListMap.computeIfAbsent(cartDo.getCid(), k -> new ArrayList<>());
            commodityIdCartDoListMap.get(cartDo.getCid()).add(cartDo);
        });


        ArrayList<OrderCount> orderCounts = new ArrayList<>();


        for (Integer commodityId : commodityIdCartDoListMap.keySet()) {
            int count = 0;
            for (CartDo cartDo : commodityIdCartDoListMap.get(commodityId)) {
                count += cartDo.getNumber();
            }
            orderCounts.add(new OrderCount(commodityId, count));
        }

        //按降序
        List<OrderCount> orderTop = orderCounts.stream().sorted(Comparator.comparing(OrderCount::getCount).reversed()
        ).limit(5).collect(Collectors.toList());

        ArrayList<TopFive> fives = new ArrayList<>();

        for (OrderCount orderCount : orderTop) {
            com.wishes.market.model.CommodityDo commodity = commodityService.findCommodityById(orderCount.getCommodityId());
            fives.add(new TopFive(commodity, orderCount.getCount()));
        }

        return fives;
    }

    @Override
    public List<TypeTopFive> findTopFiveForType() {
        //查出所有商品类型对应商品卖出订单的数量
        List<TypeTopFive> typeTopFives = cartDoMapperExt.findTopFiveForType();
        if (typeTopFives == null || typeTopFives.isEmpty()) {
            return new ArrayList<>();
        }
        //按 typename 分组
        HashMap<String, List<TypeTopFive>> typenameForTop5Map = new HashMap<>();
        typeTopFives.forEach(typeTopFive -> {
            typenameForTop5Map.computeIfAbsent(typeTopFive.getTypename(), k -> new ArrayList<>());
            List<TypeTopFive> fives = typenameForTop5Map.get(typeTopFive.getTypename());
            fives.add(typeTopFive);
            typenameForTop5Map.put(typeTopFive.getTypename(), fives);
        });

        ArrayList<TypeTopFive> fives = new ArrayList<>();
        typenameForTop5Map.keySet().forEach(typename -> {
            int count = 0;
            for (TypeTopFive typeTopFive : typenameForTop5Map.get(typename)) {
                count += typeTopFive.getCountNumber();
            }
            fives.add(new TypeTopFive(typename, count));

        });

        return fives.stream().sorted(Comparator.comparing(TypeTopFive::getCountNumber).reversed()).limit(5).collect(Collectors.toList());
    }

    @Override
    public int insertCollection(Collection collection) {
        int insert = collectionMapper.insert(collection);
        return Math.max(insert,0);
    }

    @Override
    public int deleteCollectionByPrimaryKey(Integer userId, Integer commodityId) {
        int delete = collectionMapper.deleteByPrimaryKey(userId, commodityId);
        return Math.max(delete,0);
    }

    @Override
    public Integer queryCollectionByCommodityId(Integer userId, Integer commodityId) {
        return collectionMapper.queryCollectionByCommodityId(userId, commodityId);
    }

    @Override
    public PageDto<CartDto> queryBoughtListBack(int limit, int page) {
        //计算起始值和步长
        int start = limit * (page - 1);

        //计算总数
        int total = cartDoMapperExt.countCartBack();
        PageDto<CartDto> pageDto = new PageDto<>(page, limit, total);
        List<CartDto> list = cartDoMapperExt.queryCartBack(start, limit);
        pageDto.setLists(list);

        return pageDto;
    }

    @Override
    public PageDto<CartDto> queryCartList(Long uId, int limit, int page) {
        //计算起始值和步长
        int start = limit * (page - 1);

        //计算总数
        int total = cartDoMapperExt.countCart(uId, 0L);
        PageDto<CartDto> pageDto = new PageDto<>(page, limit, total);
        List<CartDto> list = cartDoMapperExt.queryCart(uId, 0L, start, limit);
        pageDto.setLists(list);

        return pageDto;
    }
}

class OrderCount {
    private Integer commodityId;
    private Integer count;

    public OrderCount(Integer commodity, Integer count) {
        this.commodityId = commodity;
        this.count = count;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
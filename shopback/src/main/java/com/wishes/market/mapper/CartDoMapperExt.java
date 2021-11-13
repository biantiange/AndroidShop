package com.wishes.market.mapper;

import com.wishes.market.dto.CartDto;
import com.wishes.market.model.CartDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车SQL扩展
 */
public interface CartDoMapperExt extends CartDoMapper {
    /**
     * 查询购物车/购买记录表
     *
     * @param uId       用户id
     * @param purchased 数据类型
     * @param start     开始值
     * @param offset    分页数据长度
     * @return 结果集
     */
    List<CartDto> queryCart(@Param("uId") Long uId,
                            @Param("purchased") Long purchased,
                            @Param("start") int start,
                            @Param("offset") int offset);

    /**
     * 根据条件统计数据数量
     *
     * @param uId       用户id
     * @param purchased 数据类型
     * @return 统计数
     */
    Integer countCart(@Param("uId") Long uId,
                      @Param("purchased") Long purchased);

    /**
     * 统计订单数据数量
     *
     * @return 统计数
     */
    Integer countCartBack();

    List<CartDto> queryCartBack(@Param("start") int start,
                                @Param("offset") int offset);


    /**
     * 统计所有用户购物车数据数量
     *
     * @return 统计数
     */
    Integer countRealCartBack();

    List<CartDto> queryRealCartBack(@Param("start") int start,
                                @Param("offset") int offset);

    void sendByPrimaryKey(CartDo cartDo);
}
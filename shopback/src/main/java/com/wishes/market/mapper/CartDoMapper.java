package com.wishes.market.mapper;

import com.wishes.market.model.CartDo;
import com.wishes.market.model.CartDoExample;

import java.util.List;

import com.wishes.market.vo.TypeTopFive;
import org.apache.ibatis.annotations.Param;

public interface CartDoMapper {
    /**
     * @mbg.generated 2019-01-24
     */
    long countByExample(CartDoExample example);

    /**
     * @mbg.generated 2019-01-24
     */
    int deleteByPrimaryKey(CartDo record);

    /**
     * @mbg.generated 2019-01-24
     */
    int insert(CartDo record);

    /**
     * @mbg.generated 2019-01-24
     */
    int insertSelective(CartDo record);

    /**
     * @mbg.generated 2019-01-24
     */
    List<CartDo> selectByExample(CartDoExample example);

    /**
     * @mbg.generated 2019-01-24
     */
    CartDo selectByPrimaryKey(Long id);

    /**
     * @mbg.generated 2019-01-24
     */
    int updateByExampleSelective(@Param("record") CartDo record, @Param("example") CartDoExample example);

    /**
     * @mbg.generated 2019-01-24
     */
    int updateByExample(@Param("record") CartDo record, @Param("example") CartDoExample example);

    /**
     * @mbg.generated 2019-01-24
     */
    int updateByPrimaryKeySelective(CartDo record);

    /**
     * @mbg.generated 2019-01-24
     */
    int updateByPrimaryKey(CartDo record);

    /**
     * 查询出 status = 待评价 的订单
     *
     * @return 全部 "待评价" 的订单
     */
    List<CartDo> findNoCommentOrder();

    /**
     * 查询销量排名前五的 商品类型
     *
     * @return List<TypeTopFive>
     */
    List<TypeTopFive> findTopFiveForType();
}
package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrdersMapper {
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    //Long count(Orders orders, LocalDateTime beginTime, LocalDateTime endTime);
    Long count(@Param("orders") Orders orders, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    Page<Orders> pageQuery(@Param("orders") Orders orders,
                           @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize,
                           @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders searchByOrderId(Long id);

    @Select("SELECT * FROM orders WHERE status = #{status} AND order_time < #{time}")
    List<Orders> searchByStatusAndOrderTimeLT(Integer status, LocalDateTime time);
}

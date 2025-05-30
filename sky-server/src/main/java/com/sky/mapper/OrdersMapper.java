package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {
    void insert(Orders order);

    /**
     * Query the order based on the order number
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * Change order information
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

    /**
     * Query the sum of the sales under the given condition
     * @param map Condition map
     * @return The total sales amount
     */
    Double sumByMap(Map<String, Object> map);
}

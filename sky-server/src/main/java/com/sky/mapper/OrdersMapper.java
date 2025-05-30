package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Order Mapper
 */
@Mapper
public interface OrdersMapper {
    /**
     *
     * @param order
     */
    void insert(Orders order);

    /**
     * Query the order based on the order number
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * Change order information
     * @param orders order object with detailed information
     */
    void update(Orders orders);

    /**
     *
     * @param orders
     * @param beginTime
     * @param endTime
     * @return
     */
    Long count(@Param("orders") Orders orders, @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     *
     * @param orders
     * @param startPage
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     */
    Page<Orders> pageQuery(@Param("orders") Orders orders,
                           @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize,
                           @Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    /**
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders searchByOrderId(Long id);

    /**
     *
     * @param status
     * @param time
     * @return
     */
    @Select("SELECT * FROM orders WHERE status = #{status} AND order_time < #{time}")
    List<Orders> searchByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

    /**
     * Query the sum of the sales under the given condition
     * @param map Condition map
     * @return The total sales amount
     */
    Double sumByMap(Map<String, Object> map);

    /**
     * Count the number of the orders under the given condition
     * @param map Condition map
     * @return The number of order satisfied
     */
    Integer countByMap(Map<String, Object> map);
}

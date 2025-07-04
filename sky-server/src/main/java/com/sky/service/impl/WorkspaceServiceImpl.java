package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetMealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkSpaceService {
    @Autowired
    private OrdersMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    /**
     * Query today's data requested by work space
     * @param begin Start time
     * @param end End time
     * @return Business date value object containing the business data during the period
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /**
         * Turnover: the total amount of orders completed on the day
         * Valid orders: the number of orders completed on the day
         * Order completion rate: number of valid orders / total number of orders
         * Average order price: turnover / number of valid orders
         * Number of New users: the number of new users added on the day
         */

        Map<String, Object> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);

        //Query total order number
        Integer totalOrderCount = orderMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);
        // Sales
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null? 0.0 : turnover;

        // Valid order number
        Integer validOrderCount = orderMapper.countByMap(map);

        double unitPrice = 0.0;

        double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            // Order completion rate
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            // Average order price
            unitPrice = turnover / validOrderCount;
        }

        // Number of new users
        Integer newUsers = userMapper.countByMap(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    /**
     * Query the data about order management
     * @return Order overview value object containing the number of orders with different states
     */
    public OrderOverViewVO getOrderOverView() {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", Orders.TO_BE_CONFIRMED);

        // Orders to be confirmed
        Integer waitingOrders = orderMapper.countByMap(map);

        // Orders to be delivered
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        // Orders completed
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        // Orders canceled
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        // Total orders
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * Query the overview about dishes
     * @return Dish overview value object containing the number of the dishes sold and discontinued
     */
    public DishOverViewVO getDishOverView() {
        Map<String, Integer> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * Query the overview about set meals
     * @return Set meal overview value object containing the number of the set meals sold and discontinued
     */
    public SetMealOverViewVO getSetMealOverView() {
        Map<String, Integer> map = new HashMap<>();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setMealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setMealMapper.countByMap(map);

        return SetMealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderTask {
    @Autowired
    OrdersMapper ordersMapper;

    @Scheduled(cron = "*/5 * * * * ?")
    public void processTimeOutOrder() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(15L);
        List<Orders> ordersList = ordersMapper.searchByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);

        if(ordersList != null && !ordersList.isEmpty()) {
            for(Orders order: ordersList) {
                order.setStatus(Orders.CANCELLED);
                order.setRejectionReason("Time out");
                order.setCancelTime(LocalDateTime.now());
                ordersMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void processDeliveredOrder() {
        LocalDateTime time = LocalDateTime.now().minusHours(1L);
        List<Orders> ordersList = ordersMapper.searchByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if(ordersList != null && !ordersList.isEmpty()) {
            for(Orders order: ordersList) {
                order.setStatus(Orders.COMPLETED);
                ordersMapper.update(order);
            }
        }
    }
}

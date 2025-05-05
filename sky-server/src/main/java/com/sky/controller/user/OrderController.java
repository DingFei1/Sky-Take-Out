package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "user order controller")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }/*
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }*/

    @GetMapping("/historyOrders")
    public Result<PageResult> searchHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.searchHistoryOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> showOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.showOrderDetail(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    public Result<Void> reminder(@PathVariable Long id) {
        orderService.reminder(id);
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    public Result<Void> repeatOrder(@PathVariable Long id) {
        return Result.success();
    }
}

package com.sky.controller.admin;

import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("administratorOrderController")
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/details/{id}")
    public Result<OrderVO> showOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.showOrderDetail(id);
        return Result.success(orderVO);
    }

    @PutMapping("/confirm")
    public Result<Void> confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    public Result<Void> rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    public Result<Void> deliverOrder(@PathVariable Long id) {
        orderService.deliverOrder(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result<Void> completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> performStatisticsOnOrders() {
        OrderStatisticsVO orderStatisticsVO = orderService.performStatisticsOnOrders();
        return Result.success(orderStatisticsVO);
    }

    @PutMapping("/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {

    }
}

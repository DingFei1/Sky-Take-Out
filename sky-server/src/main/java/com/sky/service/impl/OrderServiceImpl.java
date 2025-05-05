package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.webSocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    WebSocketServer webSocketServer;

    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if(shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setUserId(BaseContext.getCurrentId());
        order.setPayStatus(Orders.UN_PAID);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setOrderTime(LocalDateTime.now());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserName(null);
        order.setPhone(addressBook.getPhone());
        order.setConsignee(addressBook.getConsignee());

        ordersMapper.insert(order);

        Long orderId = order.getId();

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for(ShoppingCart cart: shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetailList.add(orderDetail);
        }

        orderDetailMapper.insertBatch(orderDetailList);

        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());

        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder().orderNumber(order.getNumber()).orderAmount(order.getAmount())
                        .orderTime(order.getOrderTime()).id(order.getId()).build();
        //BeanUtils.copyProperties(order, orderSubmitVO);
        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        /*// 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;*/
        return null;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);

        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("OrderId", ordersDB.getId());
        map.put("content", "Order Id: " + outTradeNo);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    @Override
    public PageResult searchHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        Long userId = BaseContext.getCurrentId();
        Orders orders = new Orders();
        orders.setUserId(userId);
        long total = ordersMapper.count(orders, null, null);

        int pageSize = ordersPageQueryDTO.getPageSize();
        Integer startPage = (ordersPageQueryDTO.getPage() - 1) * pageSize;

        List<Orders> ordersList = ordersMapper.pageQuery(orders, startPage, pageSize, null, null);

        List<OrderVO> orderVOList = new ArrayList<>();

        for(Orders order: ordersList) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            List<OrderDetail> orderDetailList = orderDetailMapper.searchByOrderId(order.getId());
            orderVO.setOrderDetailList(orderDetailList);
            orderVOList.add(orderVO);
        }

        return new PageResult(total, orderVOList);
    }

    @Override
    public OrderVO showOrderDetail(Long id) {
        OrderVO orderVO = new OrderVO();
        Orders order = ordersMapper.searchByOrderId(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.searchByOrderId(id);
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    @Override
    public void cancelOrder(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.CANCELLED);
        ordersMapper.update(orders);
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersPageQueryDTO, orders);
        LocalDateTime beginTime = ordersPageQueryDTO.getBeginTime();
        LocalDateTime endTime = ordersPageQueryDTO.getEndTime();
        long total = ordersMapper.count(orders, beginTime, endTime);

        int pageSize = ordersPageQueryDTO.getPageSize();
        Integer startPage = (ordersPageQueryDTO.getPage() - 1) * pageSize;

        List<Orders> ordersList = ordersMapper.pageQuery(orders, startPage, pageSize, beginTime, endTime);

        List<OrderVO> orderVOList = new ArrayList<>();

        for(Orders order: ordersList) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            List<OrderDetail> orderDetailList = orderDetailMapper.searchByOrderId(order.getId());
            orderVO.setOrderDetailList(orderDetailList);
            orderVOList.add(orderVO);
        }

        return new PageResult(total, orderVOList);
    }

    @Override
    public void confirmOrder(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = new Orders();
        orders.setId(ordersConfirmDTO.getId());
        orders.setStatus(Orders.CONFIRMED);
        ordersMapper.update(orders);
    }

    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Orders orders = new Orders();
        orders.setId(ordersRejectionDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        ordersMapper.update(orders);
    }

    @Override
    public void deliverOrder(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        ordersMapper.update(orders);
    }

    @Override
    public void completeOrder(Long id) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        ordersMapper.update(orders);
    }

    @Override
    public OrderStatisticsVO performStatisticsOnOrders() {
        Orders orders = new Orders();
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        Integer numToBeConfirmed = Math.toIntExact(ordersMapper.count(orders, null, null));
        orders.setStatus(Orders.CONFIRMED);
        Integer numConfirmed = Math.toIntExact(ordersMapper.count(orders, null, null));
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        Integer numDelivered = Math.toIntExact(ordersMapper.count(orders, null, null));
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(numToBeConfirmed);
        orderStatisticsVO.setConfirmed(numConfirmed);
        orderStatisticsVO.setDeliveryInProgress(numDelivered);
        return orderStatisticsVO;
    }

    @Override
    public void reminder(Long id) {
        Orders order = ordersMapper.searchByOrderId(id);

        if(order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);
        map.put("orderId", id);
        map.put("content", "Order Number: " + order.getNumber());

        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    @Override
    public void repeatOrder(Long id) {
        Orders order = ordersMapper.searchByOrderId(id);

        if(order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Orders repeatedOrder = new Orders();
        BeanUtils.copyProperties(order, repeatedOrder);
        repeatedOrder.setUserId(BaseContext.getCurrentId());
        repeatedOrder.setPayStatus(Orders.UN_PAID);
        repeatedOrder.setStatus(Orders.PENDING_PAYMENT);
        repeatedOrder.setOrderTime(LocalDateTime.now());
        repeatedOrder.setNumber(String.valueOf(System.currentTimeMillis()));

        ordersMapper.insert(repeatedOrder);

        List<OrderDetail> orderDetailList = orderDetailMapper.searchByOrderId(order.getId());

        for(OrderDetail orderDetail: orderDetailList) {
            orderDetail.setOrderId(repeatedOrder.getId());
            orderDetailList.add(orderDetail);
        }

        orderDetailMapper.insertBatch(orderDetailList);
    }
}

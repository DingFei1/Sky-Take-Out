package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Report Service
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * Get turnover information during a period
     * @param begin The start date
     * @param end The end date
     * @return Turnover statistics value object containing date list string and turnover list string
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // List all the date in the requested period
        List<LocalDate> dateList = getDateList(begin, end);
        // Convert the date list to string
        String dateString = dateList.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        // Query the daily turnover information in the requested period
        List<Double> dailyTurnoverList = new ArrayList<>(); // turnover list in the requested period
        for(LocalDate date: dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN); // The start time of the current day
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX); // The end time of the current day

            Map<String, Object> map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = ordersMapper.sumByMap(map);
            if (turnover == null) {
                turnover = 0.0;
            }
            dailyTurnoverList.add(turnover);
        }

        // Convert the turnover list to string
        String dailyTurnoverString = dailyTurnoverList.stream().map(Object::toString).collect(Collectors.joining(","));

        return TurnoverReportVO.builder().dateList(dateString).turnoverList(dailyTurnoverString).build();
    }

    /**
     * Get user number information during a period
     * @param begin The start date
     * @param end The end date
     * @return User number statistics value object containing date list string and user number strings
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // List all the date in the requested period
        List<LocalDate> dateList = getDateList(begin, end);
        // Convert the date list to string
        String dateListString = dateList.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        List<Integer> dailyNewUserNumberList = new ArrayList<>(); // List containing daily new user number in the requested period
        List<Integer> dailyTotalUserNumberList = new ArrayList<>(); // List containing daily total user number in the requested period

        for (LocalDate date: dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, LocalDateTime> newUserNumberMap = new HashMap<>();
            newUserNumberMap.put("begin", beginTime);
            newUserNumberMap.put("end", endTime);
            Integer newUserNumber = userMapper.countByMap(newUserNumberMap);
            dailyNewUserNumberList.add(newUserNumber);

            Map<String, LocalDateTime> totalUserNumberMap = new HashMap<>();
            totalUserNumberMap.put("end", endTime);
            Integer totalUserNumber = userMapper.countByMap(totalUserNumberMap);
            dailyTotalUserNumberList.add(totalUserNumber);
        }

        // Convert the lists to strings
        String dailyNewUserNumberString = dailyNewUserNumberList.stream().map(Object::toString).collect(Collectors.joining(","));
        String dailyTotalNumberString = dailyTotalUserNumberList.stream().map(Object::toString).collect(Collectors.joining(","));

        return UserReportVO.builder()
                .dateList(dateListString)
                .newUserList(dailyNewUserNumberString)
                .totalUserList(dailyTotalNumberString)
                .build();
    }

    /**
     * Get order information during a period
     * @param begin The start date
     * @param end The end date
     * @return Order statistics value object containing date list string and order information strings
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // List all the date in the requested period
        List<LocalDate> dateList = getDateList(begin, end);

        // Convert the date list to string
        String dateListString = dateList.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        List<Integer> dailyOrderNumberList = new ArrayList<>(); // List containing daily order number
        List<Integer> dailyCompletedOrderNumberList = new ArrayList<>(); // List containing daily valid order number

        for(LocalDate date: dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> totalOrderNumberMap = new HashMap<>();
            totalOrderNumberMap.put("begin", beginTime);
            totalOrderNumberMap.put("end", endTime);
            Integer orderNumber = ordersMapper.countByMap(totalOrderNumberMap);
            dailyOrderNumberList.add(orderNumber);

            Map<String, Object> completedOrderNumberMap = new HashMap<>();
            completedOrderNumberMap.put("begin", beginTime);
            completedOrderNumberMap.put("end", endTime);
            completedOrderNumberMap.put("status", Orders.COMPLETED);
            Integer completedOrderNumber = ordersMapper.countByMap(completedOrderNumberMap);
            dailyCompletedOrderNumberList.add(completedOrderNumber);
        }

        String dailyOrderNumberString = dailyOrderNumberList.stream().map(Objects::toString).collect(Collectors.joining(","));
        String dailyCompletedNumberString = dailyCompletedOrderNumberList.stream().map(Object::toString).collect(Collectors.joining(","));

        // Total number of the orders or the completed orders during a period
        Integer totalOrderNumber = dailyOrderNumberList.stream().reduce(Integer::sum).orElse(0);
        Integer totalCompletedOrderNumber = dailyCompletedOrderNumberList.stream().reduce(Integer::sum).orElse(0);

        double orderCompletionRate = 0.0;
        if (totalOrderNumber != 0) {
            orderCompletionRate = totalCompletedOrderNumber.doubleValue() / totalOrderNumber;
        }

        return OrderReportVO.builder()
                .dateList(dateListString)
                .orderCountList(dailyOrderNumberString)
                .validOrderCountList(dailyCompletedNumberString)
                .totalOrderCount(totalOrderNumber)
                .validOrderCount(totalCompletedOrderNumber)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * Get top 10 best sales items during a period
     * @param begin The start date
     * @param end The end date
     * @return Top 10 best sales items statistics value object containing item list string and corresponding amount string
     */
    @Override
    public SalesTop10ReportVO getTopTenStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> salesTopTen = ordersMapper.getSalesTopTen(beginTime, endTime);
        String topTenNames = salesTopTen.stream().map(GoodsSalesDTO::getName).collect(Collectors.joining(","));
        String topTenNumber = salesTopTen.stream().map(item -> item.getNumber().toString()).collect(Collectors.joining(","));

        return SalesTop10ReportVO.builder().nameList(topTenNames).numberList(topTenNumber).build();
    }

    /**
     * Create a list containing all the dates by giving start date and end date
     * @param begin Start date
     * @param end End date
     * @return Date list
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>(); // date list in the requested period
        LocalDate current = begin;
        while(!current.isEqual(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }
        dateList.add(end);

        return dateList;
    }
}
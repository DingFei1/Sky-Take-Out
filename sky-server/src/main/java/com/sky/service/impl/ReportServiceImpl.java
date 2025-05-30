package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<LocalDate> dateList = new ArrayList<>(); // date list in the requested period
        LocalDate current = begin;
        while(!current.equals(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }
        dateList.add(end);

        // Convert the date list to string
        String dateString = dateList.stream().map(LocalDate::toString).collect(Collectors.joining(","));

        // Query the daily turnover information in the requested period
        List<Double> turnoverList = new ArrayList<>(); // turnover list in the requested period
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
            turnoverList.add(turnover);
        }

        // Convert the turnover list to string
        String turnoverString = turnoverList.stream().map(Object::toString).collect(Collectors.joining(","));

        return TurnoverReportVO.builder().dateList(dateString).turnoverList(turnoverString).build();
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
        List<LocalDate> dateList = new ArrayList<>(); // date list in the requested period
        LocalDate current = begin;

        while(!current.isEqual(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }
        dateList.add(end);

        // Convert the date list to string
        List<String> dateListStrings = dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
        String dateListString = String.join(",", dateListStrings);

        List<Integer> newUserNumberList = new ArrayList<>(); // List containing daily new user number in the requested period
        List<Integer> totalUserNumberList = new ArrayList<>(); // List containing daily total user number in the requested period

        for (LocalDate date: dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, LocalDateTime> newUserNumberMap = new HashMap<>();
            newUserNumberMap.put("begin", beginTime);
            newUserNumberMap.put("end", endTime);
            Integer newUserNumber = userMapper.countByMap(newUserNumberMap);
            newUserNumberList.add(newUserNumber);

            Map<String, LocalDateTime> totalUserNumberMap = new HashMap<>();
            totalUserNumberMap.put("end", endTime);
            Integer totalUserNumber = userMapper.countByMap(totalUserNumberMap);
            totalUserNumberList.add(totalUserNumber);
        }

        // Convert the lists to strings
        String newUserNumberString = newUserNumberList.stream().map(Object::toString).collect(Collectors.joining(","));
        String totalNumberString = totalUserNumberList.stream().map(Object::toString).collect(Collectors.joining(","));

        return UserReportVO.builder()
                .dateList(dateListString)
                .newUserList(newUserNumberString)
                .totalUserList(totalNumberString)
                .build();
    }
}

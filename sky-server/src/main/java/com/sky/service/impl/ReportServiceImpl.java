package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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
        List<String> dateStrings = dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
        String dateString = String.join(",", dateStrings);

        // Query the daily turnover information in the requested period
        List<Double> turnoverList = new ArrayList<>(); // turnover list in the requested period
        for(LocalDate date: dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

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
        List<String> turnoverStrings = turnoverList.stream().map(Object::toString).collect(Collectors.toList());
        String turnoverString = String.join(",", turnoverStrings);

        return TurnoverReportVO.builder().dateList(dateString).turnoverList(turnoverString).build();
    }
}

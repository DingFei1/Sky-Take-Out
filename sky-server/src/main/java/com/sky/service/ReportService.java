package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

/**
 * Report Service (Interface)
 */
public interface ReportService {
    /**
     * Get turnover information during a period
     * @param begin The start date
     * @param end The end date
     * @return Turnover statistics value object containing date list string and turnover list string
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * Get user number information during a period
     * @param begin The start date
     * @param end The end date
     * @return User number statistics value object containing date list string and user number strings
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * Get order information during a period
     * @param begin The start date
     * @param end The end date
     * @return Order statistics value object containing date list string and order information strings
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * Get top 10 best sales items during a period
     * @param begin The start date
     * @param end The end date
     * @return Top 10 best sales items statistics value object containing item list string and corresponding amount string
     */
    SalesTop10ReportVO getTopTenStatistics(LocalDate begin, LocalDate end);
}

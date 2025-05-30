package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * Get turnover information during a period
     * @param begin The start date
     * @param end The end date
     * @return Turnover statistics value object containing date list string and turnover list string
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}

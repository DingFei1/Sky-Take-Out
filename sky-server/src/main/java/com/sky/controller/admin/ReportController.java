package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Report Controller
 */
@RestController
@RequestMapping("/admin/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * Get turnover information during a period
     * @param begin The start date
     * @param end The end date
     * @return Operation result with turnover statistics value object containing date list string and turnover list string
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(String begin, String end) {
        LocalDate beginDate = null;
        LocalDate endDate = null;
        try {
            beginDate = LocalDate.parse(begin);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e){
            System.out.println("The date format is not valid");
        }
        return Result.success(reportService.getTurnoverStatistics(beginDate, endDate));
    }

    /**
     * Get user number information during a period
     * @param begin The start date
     * @param end The end date
     * @return Operation result with user number statistics value object containing date list string and user number string
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(String begin, String end) {
        LocalDate beginDate = null;
        LocalDate endDate = null;
        try {
            beginDate = LocalDate.parse(begin);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e){
            System.out.println("The date format is not valid");
        }
        return Result.success(reportService.getUserStatistics(beginDate, endDate));
    }

    /**
     * Get order information during a period
     * @param begin The start date
     * @param end The end date
     * @return Operation result with order statistics value object containing date list string and order information string
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderStatistics(String begin, String end) {
        LocalDate beginDate = null;
        LocalDate endDate = null;
        try {
            beginDate = LocalDate.parse(begin);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e){
            System.out.println("The date format is not valid");
        }
        return Result.success(reportService.getOrderStatistics(beginDate, endDate));
    }

    /**
     * Get top 10 best sales items during a period
     * @param begin The start date
     * @param end The end date
     * @return Operation result with top 10 best sales items statistics value object containing item list string and corresponding amount string
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> topTenStatistics(String begin, String end) {
        LocalDate beginDate = null;
        LocalDate endDate = null;
        try {
            beginDate = LocalDate.parse(begin);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException e){
            System.out.println("The date format is not valid");
        }
        return Result.success(reportService.getTopTenStatistics(beginDate, endDate));
    }
}

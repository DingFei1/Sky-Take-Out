package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetMealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Work space
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "Interface relevant with work space")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workspaceService;

    /**
     * Query today's data requested by work space
     * @return Operation Results with business date object value
     */
    @GetMapping("/businessData")
    @ApiOperation("Query today's data requested by work space")
    public Result<BusinessDataVO> businessData(){
        // Get the start time of today
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        // Get the end time of today
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * Query the data about order management
     * @return Operation result with order overview value object
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("Query the data about order management")
    public Result<OrderOverViewVO> orderOverView(){
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * Query the overview about dishes
     * @return Operation results with dish overview value object
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("Query the overview about dishes")
    public Result<DishOverViewVO> dishOverView(){
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * Query the overview about set meals
     * @return Operation results with set meal overview object
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("Query the overview about set meals")
    public Result<SetMealOverViewVO> setMealOverView(){
        return Result.success(workspaceService.getSetMealOverView());
    }
}

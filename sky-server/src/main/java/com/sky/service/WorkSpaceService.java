package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetMealOverViewVO;
import java.time.LocalDateTime;

/**
 * Work space service (Interface)
 */
public interface WorkSpaceService {
    /**
     * Do statistics about business data during the given period
     * @param begin Start Time
     * @param end End Time
     * @return Business date value object containing the business data during the period
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * Query the data about order management
     * @return Order overview value object containing the number of orders with different states
     */
    OrderOverViewVO getOrderOverView();

    /**
     * Query the overview about dishes
     * @return Dish overview value object containing the number of the dishes sold and discontinued
     */
    DishOverViewVO getDishOverView();

    /**
     * Query the overview about set meals
     * @return Set meal overview value object containing the number of the set meals sold and discontinued
     */
    SetMealOverViewVO getSetMealOverView();
}

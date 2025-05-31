package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Order overview data object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {
    // The number of the ones waiting to be confirmed
    private Integer waitingOrders;

    // The number of the ones waiting to be delivered
    private Integer deliveredOrders;

    // The number of the ones completed
    private Integer completedOrders;

    // The number of the ones cancelled
    private Integer cancelledOrders;

    // The number of all orders
    private Integer allOrders;
}

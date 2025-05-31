package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Set meal overview value object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMealOverViewVO implements Serializable {
    // Number of the set meals sold
    private Integer sold;

    // Number of the set meals discontinued
    private Integer discontinued;
}

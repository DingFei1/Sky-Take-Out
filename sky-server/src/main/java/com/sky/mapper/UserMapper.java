package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * User mapper
 */
@Mapper
public interface UserMapper {
    /**
     * Count the number of the users under the given condition
     * @param map Condition map
     * @return The number of user satisfied
     */
    Integer countByMap(Map<String, Object> map);
}

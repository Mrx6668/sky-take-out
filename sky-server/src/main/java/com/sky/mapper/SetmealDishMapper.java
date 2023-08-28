package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品ID列表获取套餐ID列表。
     *
     * @param  dishIds  菜品ID列表
     * @return          套餐ID列表
     */

    // select setmeal_id from setmeal_dish where dish_id in (1,2,3)
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);
}

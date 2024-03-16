package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SetmealMapper {

    @Select("select count(*) from setmeal where category_id=#{categoryId}")
    boolean hasSetmealsInCategory(Long categoryId);

    boolean dishesAreRelated2Setmeal(List<Long> ids);
}

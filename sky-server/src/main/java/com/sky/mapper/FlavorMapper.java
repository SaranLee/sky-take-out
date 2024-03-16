package com.sky.mapper;

import com.sky.dto.DishDTO;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlavorMapper {


    @Insert("insert into dish_flavor (dish_id, name, value) " +
            "values (#{dishId}, #{name}, #{value})")
    void insert(DishFlavor flavor);

    void insertBatch(List<DishFlavor> flavors);

    void deleteByDishIds(List<Long> ids);

    List<DishFlavor> getByDishId(Long id);

}

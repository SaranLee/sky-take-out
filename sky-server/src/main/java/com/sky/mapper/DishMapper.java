package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    @Select("select count(*) from dish where category_id=#{categoryId}")
    boolean hasDishesInCategory(Long categoryId);

    @Autofill(OperationType.INSERT)
    Long insert(Dish dish);

    Page<Dish> pageQuery(DishPageQueryDTO dto);

    boolean areEnabled(List<Long> ids);

    void deleteByIds(List<Long> ids);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Autofill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> listByCategoryId(Long categoryId);
}

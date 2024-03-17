package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SetmealMapper {

    @Select("select count(*) from setmeal where category_id=#{categoryId}")
    boolean hasSetmealsInCategory(Long categoryId);

    boolean dishesAreRelated2Setmeal(List<Long> ids);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO dto);

    @Autofill(OperationType.INSERT)
    void add(Setmeal setmeal);

    @Autofill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    boolean areEnabled(List<Long> ids);

    void deleteByIds(List<Long> ids);
}

package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {

    PageResult pageQuery(CategoryPageQueryDTO dto);

    void updateById(CategoryDTO categoryDTO);

    void add(CategoryDTO categoryDTO);

    void enableOrDisable(Integer status, Long id);
}

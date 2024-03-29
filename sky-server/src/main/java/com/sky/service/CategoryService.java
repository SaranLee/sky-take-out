package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {

    PageResult pageQuery(CategoryPageQueryDTO dto);

    void updateById(CategoryDTO categoryDTO);

    void add(CategoryDTO categoryDTO);

    void enableOrDisable(Integer status, Long id);

    void delete(Long id);

    List<Category> listByType(Integer type);

}

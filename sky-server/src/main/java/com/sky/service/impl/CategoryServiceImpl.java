package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 分页查询分类
     * @param dto
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Category> p = categoryMapper.pageQuery(dto);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 修改、更新分类信息
     * @param categoryDTO
     */
    @Override
    public void updateById(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    @Override
    public void add(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.add(category);
    }

    @Override
    public void enableOrDisable(Integer status, Long id) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        categoryMapper.update(category);
    }

    @Override
    public void delete(Long id) {
        // 首先检查分类是否被启用
        if (categoryMapper.isEnabled(id) )
            throw new DeletionNotAllowedException("分类被启用，不能删除！");
        // 检查分类下是否还存在菜品和套餐，如果存在，不能删除分类
        if (!dishMapper.hasDishesInCategory(id) && !setmealMapper.hasSetmealsInCategory(id))
            categoryMapper.deleteById(id);
        else
            throw new DeletionNotAllowedException("该分类下有菜品或套餐，无法删除！");
    }

    @Override
    public List<Category> queryByType(Integer type) {
        return categoryMapper.queryByType(type);
    }
}

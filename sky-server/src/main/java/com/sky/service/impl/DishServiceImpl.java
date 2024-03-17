package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.Autofill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private FlavorMapper flavorMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 新增菜品（带口味）
     * @param dto
     */
    @Transactional  // 为保证dish和flavor添加的原子性
    public void add(DishDTO dto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);

        // 添加flavor
        dishMapper.insert(dish);
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dto.getFlavors();
        // 添加dish
        if (flavors == null || flavors.isEmpty())
            return;
        for (DishFlavor flavor : flavors)
            flavor.setDishId(dishId);
        flavorMapper.insertBatch(flavors);
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<Dish> page = dishMapper.pageQuery(dto);
        PageResult pr = new PageResult(page.getTotal(), page.getResult());
        return pr;
    }

    /**
     * 根据菜品id批量删除菜品
     * @param ids
     */
    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 首先判断待删除菜品中有没有已经起售的，有起售的，不能删除
        if (dishMapper.areEnabled(ids))
            throw new DeletionNotAllowedException("存在被启用菜品，无法删除！");
        // 判断有没有菜品被套餐关联，有则不能删除
        if (setmealMapper.dishesAreRelated2Setmeal(ids))
            throw new DeletionNotAllowedException("存在被套餐关联菜品，无法删除！");
        // 删除菜品的口味信息
        flavorMapper.deleteByDishIds(ids);
        // 删除菜品
        dishMapper.deleteByIds(ids);
    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {
        // 首先根据id得到Dish
        Dish dish = dishMapper.getById(id);
        // 然后查询口味数据
        List<DishFlavor> flavors = flavorMapper.getByDishId(id);
        //最后封装DishVO返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Transactional
    public void updateWithFlavor(DishDTO dto) {
        // 首先修改dish表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dishMapper.update(dish);
        // 然后修改dish_flavor表：先删除所有flavor，再重新插入
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(dto.getId());
        flavorMapper.deleteByDishIds(ids);
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors == null || flavors.isEmpty())
            return;
        for (DishFlavor flavor : flavors)
            flavor.setDishId(dto.getId());
        flavorMapper.insertBatch(flavors);
    }

    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        return dishMapper.listByCategoryId(categoryId);
    }

    @Override
    public void enableOrDisable(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

}

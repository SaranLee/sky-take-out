package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.Autofill;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(dto);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    public void add(SetmealDTO dto) {
        // 首先新增套餐setmeal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.add(setmeal);
        // 然后新增setmeal_dish数据
        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        if (setmealDishes == null || setmealDishes.isEmpty()) return;
        for (SetmealDish setmealDish : setmealDishes)
            setmealDish.setSetmealId(setmeal.getId());
        setmealDishMapper.insertBatch(setmealDishes);
    }
}

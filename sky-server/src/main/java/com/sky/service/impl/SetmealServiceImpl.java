package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.Autofill;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
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

    @Override
    public void enableOrDisable(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.update(setmeal);
    }

    @Override
    public SetmealVO getByIdWithSetmealDished(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        // 首先获取setmeal信息，填充VO
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        // 然后获取setmeal_dish信息，填充VO
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Transactional
    public void update(SetmealDTO dto) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto, setmeal);
        // 首先修改setmeal表
        setmealMapper.update(setmeal);
        // 然后修改setmeal_dish表：先删除所有setmealDish，再重新插入
        setmealDishMapper.deleteBySetmealId(dto.getId());
        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes)
            setmealDish.setSetmealId(dto.getId());
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 判断能不能删除：被启用不能被删除
        if (setmealMapper.areEnabled(ids))
            throw new DeletionNotAllowedException("存在起售套餐，不能删除！");
        // 先删除setmeal表中数据
        setmealMapper.deleteByIds(ids);
        // 然后删除setmeal_dish表中数据
        setmealDishMapper.deleteBySetmealIds(ids);
    }
}

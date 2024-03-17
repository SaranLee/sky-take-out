package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    PageResult pageQuery(SetmealPageQueryDTO dto);

    void add(SetmealDTO dto);

    void enableOrDisable(Integer status, Long id);

    SetmealVO getByIdWithSetmealDished(Long id);

    void update(SetmealDTO dto);

    void deleteByIds(List<Long> ids);
}

package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO dto) {
        PageResult pr = setmealService.pageQuery(dto);
        return Result.success(pr);
    }

    @PostMapping("")
    @ApiOperation("新增套餐")
    public Result add(@RequestBody SetmealDTO dto) {
        setmealService.add(dto);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("起售、停售套餐")
    public Result enableOrDisable(@PathVariable Integer status, Long id) {
        setmealService.enableOrDisable(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getByIdWithSetmealDishes(@PathVariable Long id) {
        SetmealVO vo = setmealService.getByIdWithSetmealDished(id);
        return Result.success(vo);
    }

    @PutMapping("")
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO dto) {
        setmealService.update(dto);
        return Result.success();
    }

    @DeleteMapping("")
    @ApiOperation("批量删除套餐")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        setmealService.deleteByIds(ids);
        return Result.success();
    }

}

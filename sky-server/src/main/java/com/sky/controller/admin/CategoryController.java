package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags="分类管理")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    @ApiOperation("分类的分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pr = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pr);
    }

    @PutMapping("")
    @ApiOperation("修改分类信息")
    public Result updateById(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateById(categoryDTO);
        return Result.success();
    }

    @PostMapping("")
    @ApiOperation("新增分类")
    public Result add(@RequestBody CategoryDTO categoryDTO) {
        categoryService.add(categoryDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用分类")
    public Result enableOrDisable(@PathVariable Integer status, Long id) {
        categoryService.enableOrDisable(status, id);
        return Result.success();
    }

    @DeleteMapping("")
    @ApiOperation("删除分类")
    public Result delete(Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.listByType(type);
        return Result.success(list);
    }

}

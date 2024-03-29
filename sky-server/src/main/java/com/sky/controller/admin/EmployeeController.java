package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录操作")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出登录操作")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping("")
    @ApiOperation("添加员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("添加员工，{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO dto) {
        log.info("分页查询：页码{}，页大小{}，员工查询{}", dto.getPage(), dto.getPageSize(), dto.getName());
        PageResult pageData = employeeService.pageQuery(dto);
        return Result.success(pageData);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("禁用、启用员工")
    public Result enableOrDisable(@PathVariable Integer status, Long id) {
        log.info("设置员工{}状态为{}", id, status);
        employeeService.enableOrDisable(id, status);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("查询单个员工信息")
    public Result<Employee> queryEmployeeById(@PathVariable Long id) {
        log.info("查询员工{}的信息", id);
        Employee employee = employeeService.queryEmployeeById(id);
        return Result.success(employee);
    }

    @PutMapping("")
    @ApiOperation("修改员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("员工{}的信息被修改", employeeDTO.getId());
        employeeService.update(employeeDTO);
        return Result.success();
    }

}

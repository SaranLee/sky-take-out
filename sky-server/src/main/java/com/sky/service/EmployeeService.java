package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 添加员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param dto
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO dto);

    /**
     * 禁用、启用员工
     * @param id
     * @param status
     */
    void enableOrDisable(Long id, Integer status);

    /**
     * 根据员工id查询员工信息
     * @param id
     * @return
     */
    Employee queryEmployeeById(Long id);


    /**
     * 修改员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}

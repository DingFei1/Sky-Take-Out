package com.sky.controller.admin;

import java.util.HashMap;
import java.util.Map;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Employee management controller
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Employee log in
     * @param employeeLoginDTO employee login data transfer object (username and password)
     * @return {@code Result<EmployeeLoginVO>} Contains employee information (id, token, etc.)
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        // log.info("Employee login attempt: {}", employeeLoginDTO);

        // Authenticate employee
        Employee employee = employeeService.login(employeeLoginDTO);

        //After logging in successfully, generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
            jwtProperties.getAdminSecretKey(),
            jwtProperties.getAdminTtl(),
            claims);

        // Build response object with employee details and token
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
            .id(employee.getId())
            .userName(employee.getUsername())
            .name(employee.getName())
            .token(token)
            .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * Employee log out
     * @return operation result with success message
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    /**
     * Create new employee
     * @param employeeDTO employee data transfer object
     * @return operation result with success message
     */
    @PostMapping
    @ApiOperation("Add Employee")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        //log.info("Add new employee: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * Query employee information by pagination
     * @param employeePageQueryDTO employee page query transfer object
     * @return operation result with employees' information and success message
     */
    @GetMapping("/page")
    @ApiOperation("Page Query")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Change employee status
     * @param status new employee status
     * @param id employee's id
     * @return operation result with success message
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Status Change")
    public Result<Void> statusChange(@PathVariable Integer status, Long id) {
        employeeService.statusChange(id, status);
        return Result.success();
    }

    /**
     * Query the employee based on the given id
     * @param id employee's id
     * @return operation result with employee's information and success message
     */
    @GetMapping("/{id}")
    @ApiOperation("Search by Id")
    public Result<Employee> searchById(@PathVariable Long id) {
        Employee employee = employeeService.searchById(id);
        return Result.success(employee);
    }

    /**
     * Update the employee information based on the given id
     * @param employeeDTO employee data transfer object
     * @return operation result with success message
     */
    @PutMapping
    @ApiOperation("Update by Id")
    public Result<Void> updateById(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateById(employeeDTO);
        return Result.success();
    }

    /**
     * Edit the employee password
     * @param passwordEditDTO employee password editing data transfer object
     * @return operation result with success message
     */
    @PutMapping
    @ApiOperation("Edit Password")
    public Result<Void> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        employeeService.passwordChange(passwordEditDTO);
        return Result.success();
    }
}
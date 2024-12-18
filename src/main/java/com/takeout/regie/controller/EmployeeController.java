package com.takeout.regie.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.takeout.regie.common.R;
import com.takeout.regie.entity.Employee;
import com.takeout.regie.service.EmployeeService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /**
         * 1.根据username查询数据库
         * 2.没有查到返回失败
         * 3.将password md5加密
         * 4.密码比对，不一致返回失败
         * 5.查看状态，如果已禁用，返回员工已禁用
         * 6.登录成功，将员工id存入Session返回成功
         */

        //1.根据username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //2.没有查到返回失败
        if (emp == null) {
            return R.error("不存在此用户，登录失败");
        }
        //3.将password md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //4.密码比对
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误，登录失败");
        }

        //5.查看状态，如果已禁用，返回员工已禁用  0禁用
        if (emp.getStatus()==0){
            return R.error("账号已禁用");
        }

        //6.登录成功，将员工id存入Session返回成功
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }
}

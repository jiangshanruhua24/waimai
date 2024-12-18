package com.takeout.regie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.takeout.regie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

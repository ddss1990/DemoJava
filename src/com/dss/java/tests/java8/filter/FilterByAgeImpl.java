package com.dss.java.tests.java8.filter;

import com.dss.java.tests.java8.bean.Employee;
import com.dss.java.tests.java8.filter.inter.MyFilter;

/**
 * FileName: FilterByAgeImpl
 * Author: Chris
 * Date: 2018/11/20 15:32
 * Description: Filter Impl for Age
 */
public class FilterByAgeImpl implements MyFilter<Employee> {
    @Override
    public boolean filter(Employee employee) {
        return employee.getAge() > 30;
    }
}

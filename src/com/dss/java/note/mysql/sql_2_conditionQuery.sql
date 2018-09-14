# 2. 条件查询   select 列名 from 表名 where 条件语句
/*
  1. 条件运算符
    > < = != <> >= <=
  2. 逻辑运算符
    &&   ||  !  
    and  or  not
  3. 模糊查询
    like
    between and 
    in
    is null 
*/
# 2.1 按条件表达式查询
SELECT * FROM employees WHERE salary > 12000;
SELECT employee_id, department_id FROM employees  WHERE department_id <> 90;
# 2.2 按逻辑表达式查询
SELECT last_name, salary, commission_pct FROM employees WHERE salary <= 20000 AND salary >= 10000;
SELECT * FROM employees WHERE department_id < 90 OR department_id > 110 OR salary > 15000;
SELECT * FROM employees WHERE NOT(department_id >= 90 AND department_id <= 110) OR salary > 15000;# 同上


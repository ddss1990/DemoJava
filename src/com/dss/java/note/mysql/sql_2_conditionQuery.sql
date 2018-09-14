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
# 2.0 使用数据库
USE myemployees;
# 2.1 按条件表达式查询
SELECT * FROM employees WHERE salary > 12000;
SELECT employee_id, department_id FROM employees  WHERE department_id <> 90;
# 2.2 按逻辑表达式查询
SELECT last_name, salary, commission_pct FROM employees WHERE salary <= 20000 AND salary >= 10000;
SELECT * FROM employees WHERE department_id < 90 OR department_id > 110 OR salary > 15000;
SELECT * FROM employees WHERE NOT(department_id >= 90 AND department_id <= 110) OR salary > 15000;# 同上
# 2.3 模糊查询
# 2.3.1 like
SELECT * FROM employees WHERE last_name LIKE '%a%';
SELECT last_name, salary FROM employees WHERE last_name LIKE '__n_l%'; # 第三个字符为n第五个字符为l
SELECT last_name, salary FROM employees WHERE last_name LIKE '_\_%'; # 第二个字符为_
# 2.3.2 between and
SELECT * FROM employees WHERE employee_id BETWEEN 100 AND 120; # 员工编号在100-120之间的数据
# 2.3.3 in
SELECT last_name, job_id FROM employees WHERE job_id IN ('AD_PRES', 'AD_VP', 'IT_prog');
# 2.3.4 is null
SELECT last_name, commission_pct FROM employees WHERE commission_pct IS NULL;
# 2.3.5 安全等于 <=>
SELECT last_name, commission_pct FROM employees WHERE commission_pct <=> NULL;

# test
SELECT * FROM employees WHERE commission_pct LIKE '%%' OR last_name LIKE '%%';





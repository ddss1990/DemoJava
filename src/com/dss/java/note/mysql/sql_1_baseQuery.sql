# 0. 打开指定数据库
USE employees;
# 1. 基础查询   select 查询列表 from 表名;
# 1.1 单个字段
SELECT last_name FROM employees;
# 1.2 多个字段
SELECT last_name, salary, email FROM employees;
# 1.3 所有字段
SELECT * FROM employees;
# 1.4 常量值
SELECT 100;
# 1.5 表达式
SELECT 100*98;
# 1.6 函数
SELECT VERSION();
# 1.7 起别名  a. 方便理解  b. 区分要查询的字段有重名的情况
SELECT 100*98 AS result;
# 1.8 去重
SELECT DISTINCT department_id FROM employees;
# 1.9 +号的作用  在sql语句中，+号的作用只用作运算符
/*
  select 100+90;     // 190
  select '111'+90;   // 201 将字符数值转换成数值型进行运行
  select 'Tom'+90    // 90  转换失败，转成0
  select null+90;    // 一方为null，结果就为null
*/
SELECT CONCAT(last_name, ' ', first_name) AS `name` FROM employees;
# Examples
SELECT 
  CONCAT(
    last_name,
    ',',
    first_name,
    ',',
    email,
    ',',
    phone_number,
    ',',
    job_id,
    ',',
    salary,
    ',',
    IFNULL(commission_pct, 0),
    ',',
    IFNULL(manager_id, 0),
    ',',
    department_id,
    ',',
    hiredate
  ) AS OUT_PUT 
FROM
  employees ;


# 3. 排序
# 3.0 选择数据库
USE myemployees;

# 3.1 ASC - 升序  DESC - 降序
SELECT * FROM employees ORDER BY salary DESC;

# example
# 附加筛选条件
SELECT * FROM employees WHERE department_id >= 90 ORDER BY hiredate ;
# 按表达式排序
SELECT *, salary * 12 * (1 + IFNULL(commission_pct, 0)) AS 年薪 FROM employees ORDER BY salary * 12 * (1 + IFNULL(commission_pct, 0));
# 按别名排序
SELECT *, salary * 12 * (1 + IFNULL(commission_pct, 0)) AS 年薪 FROM employees ORDER BY 年薪;
# 按函数排序
SELECT LENGTH(last_name) AS `LENGTH`, last_name, salary FROM employees ORDER BY `LENGTH` DESC;
# 多个字段排序
SELECT * FROM employees ORDER BY salary , employee_id DESC;



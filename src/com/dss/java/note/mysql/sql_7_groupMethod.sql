# 7 分组函数
# 7.0 选择数据库
USE myemployees;

# 7.1 sum - 求和    avg - 平均值    max - 最大值    min - 最小值    count - 计数
SELECT SUM(salary), ROUND(AVG(salary), 2) AS 'average', MAX(salary), MIN(salary), COUNT(salary) FROM employees;
SELECT COUNT(commission_pct) FROM employees;

# 7.2 和 distinct 搭配使用
SELECT SUM(DISTINCT salary), SUM(salary) FROM employees;

# 8 分组查询  group by
SELECT MAX(salary), job_id FROM employees GROUP BY job_id; # 查询每个部门的最高工资
SELECT COUNT(*), location_id FROM departments GROUP BY location_id;
# 和 where 搭配一起使用
SELECT ROUND(AVG(salary), 2) AS avg_salary, department_id FROM employees WHERE email LIKE '%a%' GROUP BY department_id;

# 8.1 分组后的筛选条件 - having
/* 查询哪个部门员工的数量大于2，需要两步来完成
   1. 先统计每个部门的员工数量
   2. 然后找出满足条件的部门
*/
SELECT COUNT(*), department_id FROM employees GROUP BY department_id;  # 1 
SELECT COUNT(*), department_id FROM employees GROUP BY department_id HAVING COUNT(*) > 2;  # 2

# example1 每个工种有奖金的员工的最高工资>12000的工种编号和最高工资
SELECT job_id, MAX(salary) FROM employees WHERE commission_pct IS NOT NULL GROUP BY job_id;
SELECT job_id, MAX(salary) FROM employees WHERE commission_pct IS NOT NULL GROUP BY job_id HAVING MAX(salary) > 12000;

# example2  - 领导编号大于102的领导手下的最低工资大于5000的最低工资和领导编号
SELECT MIN(salary), manager_id FROM employees WHERE manager_id > 102 GROUP BY manager_id HAVING MIN(salary) > 5000 ORDER BY MIN(salary);

# 8.2 分组-按函数分组
SELECT COUNT(*), LENGTH(last_name) FROM employees GROUP BY LENGTH(last_name);
SELECT COUNT(*), LENGTH(last_name) FROM employees GROUP BY LENGTH(last_name) HAVING COUNT(*) > 5;

# 8.3 分组-多个字段分组
# 每个部门每个工种的平均工资
SELECT ROUND(AVG(salary), 2) AS 'avearge', department_id, job_id FROM employees GROUP BY department_id, job_id;



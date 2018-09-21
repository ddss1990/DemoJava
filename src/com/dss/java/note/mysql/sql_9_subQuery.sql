# 10 子查询
/*
  分类
    按子查询出现的位置划分
        select 后  -  只支持标量子查询
        from 后    -  支持表子查询
        where/having 后           -      主要支持标量子查询和列子查询，行子查询也支持，但用的较少
        exists 后(相关子查询)     -      支持表子查询
    按结果集的行列数不同划分
        标量子查询(结果集只有一行一列)
        列子查询(结果集只有一列多行) - 也叫多行子查询
        行子查询(结果集有多行多列)
        表子查询(结果集上边的格式都行)
*/

# 10.1 where 或 having 后
# 10.1.1 标量子查询(单行子查询)
# 10.1.1.1 标准使用
# 例1. 查询谁的工资比Abel高
SELECT salary FROM employees WHERE last_name = 'Abel'; # 第一步先得出Abel的工资
SELECT  # 第二步将第一步整体作为语句的一部分
  last_name, 
  salary 
FROM
  employees 
WHERE salary > 
  (SELECT 
    salary 
  FROM
    employees 
  WHERE last_name = 'Abel') ;
# 10.1.1.2 子查询中包含分组函数
# 工资最少的员工的信息
SELECT MIN(salary) FROM employees;
SELECT last_name, job_id, salary FROM employees WHERE salary = (SELECT MIN(salary) FROM employees);
# 10.1.1.3 having后使用
# 最低工资大于50号部门最低工资的部门id和最低工资
SELECT MIN(salary) FROM employees WHERE department_id = 50;
SELECT MIN(salary)FROM employees GROUP BY department_id HAVING department_id = 50;
SELECT department_id, MIN(salary) FROM employees GROUP BY department_id HAVING MIN(salary) > (SELECT MIN(salary) FROM employees WHERE department_id = 50);

# 10.1.2 列子查询(多行子查询)
# 10.1.2.1 in
# location_id是1400或1700的部门中的所有员工姓名
SELECT DISTINCT department_id FROM departments WHERE location_id IN (1400,1700);
SELECT last_name FROM employees WHERE department_id IN (SELECT department_id FROM departments WHERE location_id IN (1400,1700));
# 10.1.3 行子查询(多列多行)
SELECT * FROM employees WHERE (employee_id, salary) = (SELECT MIN(employee_id), MAX(salary) FROM employees);


# 10.2 select 后面
# 查询每个部门的员工个数
SELECT d.department_id, department_name FROM departments d LEFT OUTER JOIN employees e ON d.`department_id` = e.`department_id` WHERE e.`employee_id` IS NULL;
SELECT department_name, COUNT(*) FROM employees e RIGHT JOIN departments d ON e.`department_id` = d.`department_id` GROUP BY d.`department_name`;
SELECT d.*, (SELECT COUNT(*) FROM employees e WHERE e.`department_id` = d.`department_id`) 'count' FROM departments d;
# 查询员工号为102的部门名
SELECT department_name FROM departments d JOIN employees e ON d.`department_id` = e.`department_id` WHERE e.`employee_id` = 102;
SELECT department_name FROM departments WHERE department_id = (SELECT department_id FROM employees WHERE employee_id = 102);
# 查询每个员工的部门名
SELECT e.last_name, (SELECT department_name FROM departments d WHERE e.`department_id` = d.`department_id`) department FROM employees e;

# 10.3 from后面
# example1 查询每个部门的平均工资等级
# 第一步 ： 查询每个部门的平均工资
SELECT department_id, AVG(salary) FROM employees GROUP BY department_id;
SELECT ag.*, j.`grade_level` FROM (SELECT department_id d, AVG(salary) a FROM employees GROUP BY department_id) ag INNER JOIN job_grades j ON ag.a BETWEEN j.`lowest_sal` AND j.`highest_sal`;

# 10.4 exists后 - exist() 用来判断值是否存在，返回值是一个bool值
# example 1 查询有员工的部门名
SELECT department_name FROM departments d WHERE (SELECT COUNT(*) FROM employees e WHERE e.`department_id` = d.`department_id`) > 0;
SELECT department_name FROM departments d  WHERE EXISTS(SELECT employee_id FROM employees e WHERE e.`department_id` = d.`department_id`);
# 使用in代替exists
SELECT department_name FROM departments d WHERE d.`department_id` IN (SELECT department_id FROM employees e);
# example 2 查没有女友的男人信息
USE girls;
# in
SELECT bo.* FROM boys bo WHERE bo.`id` NOT IN (SELECT boyfriend_id FROM beauty);
# exists
SELECT bo.* FROM boys bo WHERE NOT EXISTS(SELECT boyfriend_id FROM beauty b WHERE bo.`id` = b.`boyfriend_id`);


# example 
# 1. 和zlotkey相同部门的员工姓名和工资
USE myemployees;
SELECT last_name, salary FROM employees WHERE department_id = (SELECT department_id FROM employees WHERE last_name = 'zlotkey');
# 2. 工资比公司平均工资高的员工号，姓名和工资
SELECT employee_id, last_name, salary FROM employees WHERE salary > (SELECT AVG(salary) FROM employees);
# 3. 各部门中工资比本部门工资高的员工的员工号，姓名和工资
SELECT AVG(salary), department_id FROM employees GROUP BY department_id;
SELECT employee_id, last_name, salary, e.`department_id` FROM employees e JOIN (SELECT AVG(salary) a, department_id FROM employees GROUP BY department_id) av ON e.`department_id` = av.`department_id` WHERE e.`salary` > av.a;
# 4 和姓名中包含u的员工在相同部门的员工号和姓名
SELECT DISTINCT department_id FROM employees;
SELECT employee_id, last_name, e.department_id FROM employees e JOIN (SELECT DISTINCT department_id FROM employees) de ON de.department_id = e.`department_id` AND last_name LIKE '%u%';
# 上边理解错题意了
SELECT employee_id, last_name FROM employees WHERE department_id IN (SELECT DISTINCT department_id FROM employees WHERE last_name LIKE '%u%');
# 5. 在部门的location_id为1700的部门工作的员工及员工号
SELECT department_id FROM departments  WHERE location_id = 1700;
SELECT employee_id, last_name FROM employees WHERE department_id IN (SELECT department_id FROM departments WHERE location_id = 1700);
# 6. 管理者是KING的员工姓名和工资
SELECT employee_id FROM employees WHERE last_name = 'k_ing';
SELECT last_name, salary FROM employees WHERE manager_id = ANY(SELECT employee_id FROM employees WHERE last_name = 'k_ing');
# 7. 工资最高的员工姓名
SELECT CONCAT(last_name, '.', first_name) 'name', salary FROM employees WHERE salary = (SELECT MAX(salary) FROM employees);

# 经典案例
# 1. 平均工资最低的部门信息
SELECT AVG(salary), department_id FROM employees GROUP BY department_id ORDER BY AVG(salary);
SELECT department_id FROM employees GROUP BY department_id ORDER BY AVG(salary) LIMIT 1;
SELECT * FROM departments WHERE department_id = (SELECT department_id FROM employees GROUP BY department_id ORDER BY AVG(salary) LIMIT 1);
# 2. 平均工资最低的部门信息和部门工资
SELECT av.s, d.* FROM departments d JOIN (SELECT department_id, AVG(salary) s FROM employees GROUP BY department_id ORDER BY AVG(salary) LIMIT 1) av ON d.`department_id` = av.department_id;
# 3. 平均工资最高的job信息
SELECT AVG(salary), job_id FROM employees GROUP BY job_id ORDER BY AVG(salary) DESC LIMIT 1;
SELECT * FROM jobs WHERE job_id = (SELECT job_id FROM employees GROUP BY job_id ORDER BY AVG(salary) DESC LIMIT 1);
# 4. 平均工资高于公司平均工资的部门
SELECT AVG(salary) FROM employees;
SELECT AVG(salary), department_id FROM employees GROUP BY department_id HAVING AVG(salary) > (SELECT AVG(salary) FROM employees);
SELECT * FROM departments WHERE department_id IN (SELECT department_id FROM employees GROUP BY department_id HAVING AVG(salary) > (SELECT AVG(salary) FROM employees));
# 5. 所有manager的详细信息
SELECT DISTINCT manager_id FROM employees WHERE manager_id IS NOT NULL;
SELECT * FROM employees WHERE employee_id = ANY(SELECT DISTINCT manager_id FROM employees WHERE manager_id IS NOT NULL);
# 6. 各个部门中，最高工资中最低的那个部门的最低工资是多少
SELECT MAX(salary), department_id FROM employees GROUP BY department_id ORDER BY MAX(salary);
SELECT department_id FROM employees GROUP BY department_id ORDER BY MAX(salary) LIMIT 1;
SELECT MIN(salary) FROM employees WHERE department_id = (SELECT department_id FROM employees GROUP BY department_id ORDER BY MAX(salary) LIMIT 1);
# 7. 平均工资最高的部门的manager的详细信息
SELECT AVG(salary), department_id FROM employees GROUP BY department_id ORDER BY AVG(salary) DESC;
SELECT AVG(salary), department_id FROM employees GROUP BY department_id ORDER BY AVG(salary) DESC LIMIT 1;

SELECT last_name, d.department_id, email, salary FROM employees e JOIN departments d ON d.`manager_id` = e.`employee_id` 
	WHERE d.`department_id` = (SELECT department_id FROM employees GROUP BY department_id ORDER BY AVG(salary) DESC LIMIT 1);
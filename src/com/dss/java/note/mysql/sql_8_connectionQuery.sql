# 9. 连接查询，又叫多名查询 
# 9.0 选择数据库
USE girls;

# 9.1 笛卡尔乘积现象 ：表1有m行，表2有n行，最后的结果是 m * n 行
# 最后结果有 count(beauty) * count(boys) 行
SELECT `name`, boyName FROM beauty, boys; 
SELECT `name`, boyName FROM beauty, boys WHERE beauty.boyfriend_id = boys.id;

/* 选择查询的分类
 * 按年代分类
      SQL92标准 - 仅支持内连接
      SQL99标准 - 支持内连接 + 外连接(左，右) + 交叉连接
 * 功能分类
      内连接
         等值连接
         非等值连接
         自连接
      外连接
         左外连接
         右外连接
         全外连接
      交叉连接
*/

# 9.2 分类
# 9.2.1 SQL92 - 等值连接
# 9.2.1.1 正常使用
SELECT `name`, boyName FROM beauty, boys WHERE beauty.boyfriend_id = boys.id;
SELECT last_name, department_name FROM employees, departments WHERE employees.`department_id` = departments.`department_id`;
# 9.2.1.2 表名过长，为表起别名 - 起过表的别名之后，不能再使用原来的名字
SELECT last_name, employees.job_id, job_title FROM employees, jobs WHERE employees.`job_id` = jobs.`job_id`;
SELECT last_name, em.job_id, job_title FROM employees AS em, jobs WHERE em.`job_id` = jobs.`job_id`;
# 9.2.1.3 表的顺序可以调换
SELECT last_name, em.job_id, job_title FROM jobs,employees AS em WHERE em.`job_id` = jobs.`job_id`;
# 9.2.1.4 加筛选 - 使用AND将筛选条件并列
SELECT last_name, department_name, commission_pct FROM employees e, departments WHERE e.`department_id` = departments.`department_id` AND commission_pct IS NOT NULL;
SELECT department_name, city FROM locations, departments WHERE departments.`location_id` = locations.`location_id`  AND locations.`city` LIKE '_o%';
# 9.2.1.5 分组
SELECT COUNT(*), city FROM locations, departments WHERE locations.`location_id` = departments.`location_id` GROUP BY city;# 每个城市的部门个数
# 查询有奖金的每个部门的部门名和领导编号及该部门的最低工资
SELECT 
  department_name,
  departments.manager_id,
  MIN(salary) 
FROM
  departments,
  employees 
WHERE departments.`department_id` = employees.`department_id` 
  AND commission_pct IS NOT NULL 
GROUP BY employees.department_id ;
# 9.2.1.6 排序
# 每个工种的工种名和员工的个数
SELECT job_title, COUNT(*) 
FROM employees, jobs
WHERE employees.`job_id` = jobs.`job_id` GROUP BY employees.`job_id` ORDER BY COUNT(*);
# 9.2.1.7 三表查询
# 查询员工名，部门名和所在城市
SELECT last_name, department_name, city	FROM employees, departments, locations 
WHERE employees.`department_id` = departments.`department_id` AND departments.`location_id` = locations.`location_id`;

# 9.2.2 非等值连接
# 员工的工资和工资级别
SELECT last_name, salary, grade_level FROM employees e, job_grades g WHERE e.`salary` BETWEEN g.`lowest_sal` AND g.`highest_sal`;

# 9.2.3 自连接
# 员工名及领导名
SELECT e.`employee_id`, e.`last_name`, m.`employee_id`, m.`last_name`
FROM employees e, employees m WHERE e.`manager_id` = m.`employee_id`;


#select password('ABC');

# 9.3 SQL99语法
# 9.3.0 语法
/*
  select 查询列表
  from 表1 【别名】【连接类型】
  join 表2 【别名】  # 原来是逗号,
  on 连接条件        # 原来是where
  where 筛选条件     
*/
# 9.3.1 内连接
# 9.3.1.0 连接类型 - inner(可省略)
# select 查询列表 from 表1 inner join 表2 on 连接条件;
# 9.3.1.1 等值连接
# 9.3.1.1.1 正常查询
# 查询员工名，部门名
SELECT last_name, department_name FROM employees e INNER JOIN departments d ON e.`department_id`=d.`department_id`;
# 9.3.1.1.2 加筛选
# 查询名字中含e的员工名和工种名
SELECT last_name, job_title FROM employees e INNER JOIN jobs j ON e.`job_id` = j.`job_id` WHERE e.`last_name` LIKE '%e%';
# 9.3.1.1.3 分组+筛选
# 部门个数>3的城市名和部门个数   ->  先查询，后筛选
SELECT l.city, COUNT(*) FROM departments d INNER JOIN locations l ON d.`location_id` = l.`location_id` GROUP BY l.`city` HAVING COUNT(*) > 3;
# 9.3.1.1.4 排序
# 部门员工个数>3的部门名和员工个数，个数降序
SELECT department_name, COUNT(*) FROM departments d INNER JOIN employees e ON d.`department_id` = e.`department_id` GROUP BY department_name HAVING COUNT(*) > 3 ORDER BY COUNT(*) DESC;
# 9.3.1.1.5 三表查询
# 员工名，部门名，工种名
SELECT last_name, department_name, job_title FROM employees e 
INNER JOIN departments d ON e.`department_id` = d.`department_id` 
INNER JOIN jobs j ON e.`job_id` = j.`job_id` ORDER BY department_name DESC;

# 9.3.1.2 非等值连接
# 查工资级别
SELECT last_name, salary, grade_level FROM employees e
INNER JOIN job_grades g ON e.`salary` BETWEEN g.`lowest_sal` AND g.`highest_sal`;


# 9.3.2 外连接
# 9.3.2.1 左/右 外连接
# 主从表的顺序翻转有关系，应该让数据多的一方做主表
# 查询没有男友的女的名字
SELECT b.name FROM beauty b LEFT OUTER JOIN boys bo ON b.`boyfriend_id` = bo.`id` WHERE bo.id IS NULL;
SELECT b.name, bo.* FROM boys bo LEFT OUTER JOIN beauty b ON b.`boyfriend_id` = bo.`id` WHERE b.`id` IS NULL;
# examples
# 查没有员工的部门 - 不能理解为没有部门的员工
USE myemployees;
SELECT department_name, COUNT(*) FROM departments d, employees e WHERE d.`department_id` = e.`department_id` GROUP BY d.`department_id`; 
SELECT * FROM employees WHERE department_id IS NULL; # 这个是没有部门的员工
/*
 外连接的查询方式，是通过在主表中，取出每一条数据，去从表中查询是否有匹配对应的数据
*/
SELECT d.department_id, department_name, e.* FROM departments d LEFT JOIN employees e ON d.`department_id` = e.`department_id`;
SELECT d.department_id, department_name, e.`employee_id` FROM departments d LEFT OUTER JOIN employees e ON d.`department_id` = e.`department_id` WHERE e.`employee_id` IS NULL ;
# 这里的查询结果看起来有问题，其实是没问题的，因为是在departments表中故意设置了重复的数据，在employees表中没有对应的department_id进行引用
SELECT * FROM employees WHERE department_id = 120; # 这里是从上步操作中查到数据，去验证在employees表中有对应的数据
SELECT department_name, e.`last_name` FROM departments d LEFT JOIN employees e ON d.`department_id` = e.`department_id` WHERE e.`employee_id` IS NULL;

SELECT COUNT(*) FROM departments;



# example
# 1. 编号>3的女人的男友信息，
USE girls;
SELECT b.id, b.name, bo.* FROM beauty b LEFT JOIN boys bo ON b.`boyfriend_id` = bo.`id` WHERE b.`id` > 3;
# 2. 哪个城市没有部门
USE myemployees;
SELECT city FROM locations l LEFT JOIN departments d ON l.`location_id` = d.`location_id` WHERE d.`department_id` IS NULL;
# 3.部门名为SAL或IT的员工信息
SELECT e.*,d.`department_name` FROM employees e JOIN departments d ON e.`department_id` = d.`department_id` WHERE d.`department_name` = 'sal' OR d.`department_name` = 'it'; 
# 使用内连接的方式得到的结果不全，因为有可能该部门没有员工
SELECT d.`department_id` id, d.`department_name` d_name, e.* FROM departments d LEFT JOIN employees e ON d.`department_id` = e.`department_id` WHERE d.`department_name` IN('sal','it');
# 使用外连接的的方式就包含了没有员工的部门




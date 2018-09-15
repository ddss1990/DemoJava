# 6. 流程控制
# 6.0 选择数据库
USE myemployees;

# 6.1 if
SELECT IF('10>5', '大于', '小于') AS 'if';
SELECT last_name, commission_pct, IF(commission_pct IS NULL, '没奖金', '有奖金') AS '备注' FROM employees;

# 6.2 case - 1
SELECT 
  last_name,
  salary,
  department_id,
  CASE
    department_id 
    WHEN 30 
    THEN salary * 1.1 
    WHEN 40 
    THEN salary * 1.2 
    WHEN 50 
    THEN salary * 1.3 
    ELSE salary 
  END AS 'New Salaray' 
FROM
  employees ;

# 6.3 case - 2
SELECT 
  last_name,
  salary,
  CASE
    WHEN salary >= 20000 
    THEN 'A' 
    WHEN salary >= 15000 
    THEN 'B' 
    WHEN salary >= 10000 
    THEN 'C' 
    ELSE 'D' 
  END AS 'level' 
FROM
  employees;


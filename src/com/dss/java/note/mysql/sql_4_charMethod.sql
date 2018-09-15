# 4. 常见函数
# 4.0 选择数据库
USE myemployees;

# 4.1 字符函数
# 4.1.1 length
SELECT LENGTH('john');
SELECT LENGTH('中国人');  # 中文的话和字符集有关 utf8字符集中，中文占3个字节

# 4.1.2 concat
SELECT CONCAT(last_name, ' ', first_name) AS `Name` FROM employees;

# 4.1.3 upper/lower
SELECT UPPER('John');
SELECT LOWER('JOHN');
SELECT CONCAT(UPPER(last_name), ' ', LOWER(first_name)) FROM employees;

# 4.1.4 substr、substring 下标从1开始
SELECT SUBSTR('李莫愁爱上了陆展元', 7) AS `out`;
SELECT SUBSTR('李莫愁爱上了陆展元', 1, 3) AS `out`;
SELECT SUBSTRING('李莫愁爱上了陆展元', 7);
# example : 姓名首字符大写，其它字符小写，用_拼接
SELECT CONCAT(UPPER(SUBSTR(last_name, 1,1)), '_', LOWER(SUBSTR(last_name, 2, LENGTH(last_name)))) FROM employees;

# 4.1.5 instr
SELECT INSTR('李莫愁爱上了陆展元', '陆展元') AS `out`;

# 4.1.6 trim
SELECT LENGTH(TRIM('    李莫愁   ')) AS `trim`;
SELECT TRIM('a' FROM 'aaaaaa   李a莫a愁a a aaaa') AS `trim`;

# 4.1.7 lpad
SELECT LPAD('李莫愁', 10, '*') AS `lpad`,RPAD('李莫愁', 10, '#') AS `rpad`;
SELECT LPAD('李莫愁', 2, '*') AS 'lpad', RPAD('李莫愁', 2, '#') AS 'rpad';

# 4.1.8 replace
SELECT REPLACE('李莫愁爱上了杨过', '李莫愁', '小龙女') AS '射雕';

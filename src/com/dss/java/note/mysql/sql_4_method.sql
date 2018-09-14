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

# 4.1.4 substr
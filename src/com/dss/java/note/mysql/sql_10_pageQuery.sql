# 11 分页查询
/*
  11.1 语法
      limit offset, size
          offset - 开始的索引，从0开始，如果从第一条开始，那就可以省略
          size   - 要显示的条目数
*/
SELECT * FROM employees LIMIT 0,5;
SELECT * FROM employees LIMIT 5;

SELECT INSTR(last_name, '_') FROM employees;
SELECT SUBSTR(last_name, 1, 2) FROM employees;


# 12 联合查询
# 将多个查询条件拆分为多个多个语句
# select * from 表 where 语句1 or 语句2  ->
# select * from 表 where 语句1 union select * from 表 where 语句2  
# 视图
/*
   虚拟存在的表，只保存sql逻辑，不保存查询结果。
   将经常使用的sql语句封装成视图，以便多次调用。例如：多表的连接查询
   格式
     create view NAME AS SQL语句;
     select * from NAME;
   优势
     重用SQL语句
     简化复杂的SQL操作
     保护数据，提高安全性 - 只知道视图的结构，不知道原始数据表的结构
   修改视图
     create or replace view NAME as SQL语句;
     alter view NAME as SQL语句;
   删除视图
     drop view NAME,NAME2...;
   查看视图
     desc NAME;
     show create view NAME;
   更新视图 - 更改视图中的数据
     插入 - 通过 insert into 向视图中添加数据，原始表中也会加入相应的数据
     对视图的数据操作，会影响原始表的数据
     不允许更新视图的情况：
       包含分组函数、distinct、group by、having、union和union all的sql语句
       常量视图 - 直接设置的常量， create view example as select 'Hello' title;
       select中包含子查询
       join
       from一个不能更新的视图
       where子句的子查询引用了from子句中的表       
   视图和表的对比
		创建的关键字	是否占用物理空间	    使用
     视图	create view	不占用，只保存了sql逻辑   增删改查
     表		create table 	占用，保存了实际数据      增删改查
*/
SHOW DATABASES;
USE myemployees;
SHOW TABLES;
CREATE VIEW v1 AS SELECT last_name, department_name FROM employees e JOIN departments d ON e.department_id = d.department_id;
SELECT * FROM v1;

# 更新视图
CREATE OR REPLACE VIEW v0 AS SELECT last_name, salary FROM employees;
SELECT * FROM v0;
INSERT INTO v0 VALUES('李四', 10000);
# 1 包含group by的情况
CREATE OR REPLACE VIEW v1 AS SELECT department_id, AVG(salary) AVG FROM employees GROUP BY department_id;
SELECT * FROM v1;
UPDATE v1 SET AVG=9000 WHERE department_id = 10; # 不能修改
# 2. 常量视图
CREATE OR REPLACE VIEW v2 AS SELECT 'Hello world' FIRST;
SELECT * FROM v2;
UPDATE v2 SET FIRST='China'; # 不能修改
# 3. select中包含子查询
CREATE OR REPLACE VIEW v3 AS SELECT (SELECT MAX(salary) FROM employees) MAX;
SELECT * FROM v3;
UPDATE v3 SET MAX=10000; # 不能修改
# 4. join
CREATE OR REPLACE VIEW v4 AS SELECT last_name, department_name FROM employees e JOIN departments d ON e.department_id = d.department_id;
SELECT * FROM v4;
UPDATE v4 SET last_name='张三' WHERE last_name = 'Whalen'; # 可更改，原数据也会改变，不能插入新数据
SELECT * FROM employees;
# 5. from一个不能更新的视图
CREATE OR REPLACE VIEW v5 AS SELECT * FROM v3;
SELECT * FROM v5;
# 6. where子句的子查询引用了from子句中的表
CREATE OR REPLACE VIEW v6 AS SELECT last_name, email, salary FROM employees e WHERE employee_id IN (SELECT employee_id FROM employees WHERE manager_id IS NOT NULL);
SELECT * FROM v6;


# 标识列 - 自增长列
/*
  关键字 - auto_increment
  show variables like '%auto_increment%'    # 查看有关 auto_increment 的变量
      auto_increment_increment   -   步长，就是每次增加的值
      auto_increment_offset      -   起始位置
    可通过 set XXXXXX=n  的方式来设置变量的值
  起始值也可以在插入第一条数据的时候插入，可以不连续的插入数据，例如
	1. 先插入一个编号为10的，此时的编号是10
	2. 再插入一个编号为15的，此时的编号是15
	3. 再插入一个不带编号的，此时的编号是16
  特点：
      1. 必须和一个key搭配(主键，唯一键，外键)
      2. 一个表中只能有个一个标识列
      3. 标识列的类型是数值型
      4. 可通过 set auto_increment_increment=N 设置步长，可通过手动插入值设置起始值
  修改表时添加标识列
      ALTER TABLE tab_identity MODIFY COLUMN _id INT PRIMARY KEY AUTO_INCREMENT
  修改表时删除标识列
      alter table tab_identity modify column _id int ;
*/
USE test;
DROP TABLE IF EXISTS tab_identity;
CREATE TABLE IF NOT EXISTS tab_identity(
	_id INT PRIMARY KEY AUTO_INCREMENT,
	`name` VARCHAR(10)
);


INSERT INTO tab_identity VALUES(10, 'Tom');
INSERT INTO tab_identity VALUES(14, 'Jerry');
INSERT INTO tab_identity(`name`) VALUES ('Android');

SELECT * FROM tab_identity;


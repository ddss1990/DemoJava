# 约束 - 一种限制，用于限制表中的数据，为了保证表中的数据的准确和可靠性
/*
 六大约束
  NOT NULL - 非空约束，限制字段的值为非空，例如： id 
  DEFAULT  - 默认约束，保证字段有默认值，例如： 性别
  PRIMARY KEY - 主键约束，保证字段具有唯一性，并且非空
  UNIQUE   - 唯一约束，保证字段具有唯一性，可以为空
  CHECK    - 检查约束，MySQL不支持
  FOREIGN KEY - 外键约束，限制两个表的关系，保证该字段的值必须来源于主表关联列的值，例如：部门编号
 添加约束的分类
  列级约束 - 六大约束都支持，外键约束无效果
  表级约束 - 不支持非空约束和默认约束，其余都支持
*/

CREATE DATABASE IF NOT EXISTS students;
USE students;
# 1 创建表时添加约束
# 1.1 列级约束
CREATE TABLE stuinfo(
	_id INT PRIMARY KEY,
	stuName VARCHAR(20) NOT NULL,
	gender CHAR(1) CHECK(gender ='男' OR gender = '女'),
	seat INT UNIQUE,
	age INT DEFAULT 18,
	majorId INT REFERENCES major(_id)
);
CREATE TABLE IF NOT EXISTS major(
	_id INT PRIMARY KEY,
	majorName VARCHAR(10)
);
DESC stuinfo;
# 1.2 表级约束
DROP TABLE IF EXISTS stuinfo;
SHOW TABLES;
CREATE TABLE IF NOT EXISTS stuinfo(
	_id INT,
	stuName VARCHAR(20),
	gender CHAR(1),
	seat INT,
	age INT,
	majorId INT,
	CONSTRAINT pk PRIMARY KEY(_id), # 主键
	CONSTRAINT uq UNIQUE(seat), # 唯一
	CONSTRAINT ck CHECK(gender = '男' OR gender = '女'), # 检查约束
	CONSTRAINT fk_stu_major FOREIGN KEY(majorId) REFERENCES major(_id) # 外键
);
SHOW INDEX FROM stuinfo;

# 2. 修改表时添加约束
# 2.0 创建一个不带约束的表
DROP TABLE IF EXISTS stuinfo;
CREATE TABLE IF NOT EXISTS stuinfo(
	_id INT,
	stuName VARCHAR(20),
	gender CHAR(1),
	seat INT,
	age INT,
	majorId INT
);
DESC stuinfo;
# 2.1 添加非空约束
ALTER TABLE stuinfo MODIFY COLUMN stuName VARCHAR(20) NOT NULL;
# 2.2 添加默认约束
ALTER TABLE stuinfo MODIFY COLUMN age INT DEFAULT 18;
# 2.3 添加主键约束
# 2.3.1 列级约束
ALTER TABLE stuinfo MODIFY COLUMN _id INT PRIMARY KEY;
# 2.3.2 表级约束
ALTER TABLE stuinfo ADD PRIMARY KEY(_id);
# 2.4 添加唯一约束
ALTER TABLE stuinfo MODIFY COLUMN seat INT UNIQUE;
ALTER TABLE stuinfo ADD UNIQUE(seat);
# 2.5 添加外键约束
ALTER TABLE stuinfo ADD FOREIGN KEY(majorId) REFERENCES major(_id);

# 3 修改表时删除约束
# 3.1 删除非空约束
ALTER TABLE stuinfo MODIFY COLUMN stuName VARCHAR(20) NULL;
# 3.2 删除默认约束
ALTER TABLE stuinfo MODIFY COLUMN age INT;
# 3.3 删除主键约束
ALTER TABLE stuinfo DROP PRIMARY KEY;


CREATE DATABASE students;
SHOW DATABASES;
USE students;
SHOW TABLES;
DROP TABLE major;
CREATE TABLE IF NOT EXISTS major(id INT PRIMARY KEY, majorName VARCHAR(10));
CREATE TABLE IF NOT EXISTS stuinfo(id INT PRIMARY KEY, NAME VARCHAR(20), majorId INT);
# 添加外键 - 传统方式
ALTER TABLE stuinfo ADD CONSTRAINT fk_stu_major FOREIGN KEY(majorId) REFERENCES major(id);
SHOW INDEX FROM major;
SHOW INDEX FROM stuinfo;
INSERT INTO major VALUES(1, 'Java'), (2, 'Android'), (3, 'PHP');
INSERT INTO stuinfo VALUES(1, 'Tom1', 1),(2, 'Tom2', 2), (3, 'Tom3', 2),(4, 'Tom4', 1),(5, 'Tom5', 3),(6, 'Tom6', 3);
SELECT * FROM major;
INSERT INTO major VALUES(3, 'PHP');
DELETE FROM major WHERE id=3; # 这个时候不能删
ALTER TABLE stuinfo DROP FOREIGN KEY fk_stu_major; # 删除外键

SELECT NAME, majorName FROM stuinfo s JOIN major m ON s.`majorId` = m.id;

# 级联删除 - 添加外键的时候多加一个 on delete cascade 
ALTER TABLE stuinfo ADD CONSTRAINT fk_stu_major FOREIGN KEY(majorId) REFERENCES major(id) ON DELETE CASCADE;
DELETE FROM major WHERE id=3; # 这个时候就能删了，相关联的从表中引用的整条数据也会删除
SELECT * FROM stuinfo;
# 级联置空
ALTER TABLE stuinfo ADD CONSTRAINT fk_stu_major FOREIGN KEY(majorId) REFERENCES major(id) ON DELETE SET NULL;
DELETE FROM major WHERE id=2; # 这个时候删除，会将从表中对应的数据置空
SELECT * FROM stuinfo; 



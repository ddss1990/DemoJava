# 1 库的管理
# 1.1 创建库
CREATE DATABASE books;
CREATE DATABASE IF NOT EXISTS books; # 增加一种容错性，当库不存在的时候再创建
# 1.2 修改库
# 1.2.0 不能修改库名，如果想修改的话，只能去修改库对应的文件夹的名字，在 ProgramData下
# 1.2.1 修改库的字符集
ALTER DATABASE books CHARACTER SET gbk;
# 1.3. 删除库
DROP DATABASE IF EXISTS books;

# 2 表的管理
# 2.1 创建表
CREATE TABLE book(_id INT, bName VARCHAR(20), price DOUBLE, authorId INT, publishDate DATETIME);
CREATE TABLE author(_id INT, `name` VARCHAR(20), nation VARCHAR(10));
# 2.2 修改表
# 2.2.1 修改表名
ALTER TABLE books RENAME TO book;

SELECT CURDATE();
SELECT TIME(NOW());
SELECT DATE(NOW());



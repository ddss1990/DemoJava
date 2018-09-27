/*
  1. 变量
    系统变量 - 系统提供
        全局变量 - 服务器启动的时候赋初始值，修改的值服务重启失效
        会话变量 - 当前会话/连接有效
      语法：
        show [global/session(default)] variables; # 查看所有的系统变量
        show global/[session] variables like '%XXX%'; # 查看满足条件的系统变量
        select @@global/[session].变量名 # 查看某个系统变量的具体的值
        set gloabl/[session] 变量名=值; set @@gloabl/[session].变量名=值; # 为系统变量赋值
    自定义变量 - 用户自定义的
        用户变量 - 当前会话/连接有效，同会话变量，可放在任意位置
          语法
            声明并初始化
              set @变量名=值; 或 set @变量名:=值; 或 select @变量名:=值;
            修改变量的值
              set @变量名=值; 或 set @变量名:=值; 或 select @变量名:=值;    或
              select 字段 into @变量名 from 表名;  # 相当于是从表中查询一个值赋给变量，查出来的值只能是一个
            查看变量的值
              select @变量名;
        局部变量 - 仅在定义它的 begin-end中有效，应用在begin-end中，且在第一句
          声明
            declare 变量名 类型; # 没赋初始值
            declare 变量名 类型 default 值; # 有初始值
          赋值
            set 变量名=值; 或 set 变量名:=值; 或 select @变量名:=值;      或
            select 字段 into @变量名 from 表名;
          使用
            select 变量名;
  2. 存储控制和函数
     存储过程 - 一组预先编译好的SQL语句的集合
       好处
         减少了编译次数并减少了和数据路的连接次数，提高了效率
       语法
         创建
           create procedure 存储过程名(参数列表) begin 存储过程体(SQL语句) end
             每条SQL语句结尾需要有分号
           参数列表：
             参数模式  参数名  参数类型  
             in        stuname   varchar(20)
           参数模式: in, out, inout
             in    - 该参数可以作为输入，需要调用方传入值
             out   - 该参数可以作为输出，该参数作为返回值
             inout - 该参数既可以作为输入，又可以作为输出，该参数即需要传入值也需要输出值
           begin-end
             当存储过程体只有1句话时，begin-end可省略。这个时候需要用 delimiter重新设置结束标记
               delimiter 结束标记
         使用
           call 存储过程名(实参列表)
       删除存储过程
	 drop procedure 过程名; # 一次只能删除一个，视图可以一次删除多个
       查看存储过程 - 创建的信息
         show create procedure 过程名;
     函数 - 和存储过程类似，不同点是存储过程可以没有返回，也可以有多个返回。函数有且只有一个返回。
       存储过程：适合批量插入，批量更新
       函数    ：适合做处理数据后返回一个结果
       语法：
         create function 函数名(参数列表) returns 返回类型 begin
		函数体
	 end 
           参数列表 : 参数名 参数类型
	   函数体：必有return语句。
	 select 函数名(参数); # 函数的调用
       查看函数
         show create function 函数名;
       删除函数
         drop function 函数名;
  3. 流程控制结构
    顺序结构 - 从上往下依次执行
    分支结构 - 从多条路径中选择一条去执行
      if函数
      case函数
        作为表达式使用，可放在任何地方；作为独立的语句使用，只能放在begin-end中
        如果else省略，并且when的条件都不满足，返回NULL
      if结构 - 实现多重分支，只能应用在begin-end中
        if 条件1 then 语句1; elseif 条件2 then 语句2; ... else 语句; end if;
    循环结构 - 满足一定条件，重复执行一段代码
      分类 - while, loop, repeat
      循环控制 : iterate - 结束本次循环，继续下一次；leave - 跳出循环
      while 
        [标签:] while 循环条件 do 循环体 end while [标签];
      loop
        [标签:] loop 循环体; end loop [标签];  # 模拟简单的死循环
      repeat - 至少执行一次
        [标签:] repeat 循环体; until 结束循环的条件; end repeat [标签];
*/

# 1 变量
# 1.1 系统变量
# 1.1.1 全局变量
SHOW GLOBAL VARIABLES; # 查看所有的值
SHOW GLOBAL VARIABLES LIKE '%transaction%'; # 查看满足条件的变量
SELECT @@global.autocommit; # 查看具体的值
SET @@global.autocommit = 0; # 修改值
# 1.1.2 会话变量
SHOW VARIABLES;
SHOW SESSION VARIABLES LIKE '%iso%';
SELECT @@tx_isolation;
SET @@autocommit=1;
SELECT @@autocommit;
# 1.2 自定义变量
# 1.2.1 用户变量
SET @name='john'; # 初始化并赋值
USE myemployees;
SELECT COUNT(*) INTO @count FROM employees; # 修改值，变量可以是一个未声明的
SELECT @name; # 取值
SELECT @count;
SHOW VARIABLES LIKE '%begin%';
# 1.2.2 局部变量
DECLARE m INT DEFAULT 1; # 有错误，需要放在begin-end中


# 2 存储过程和函数
# 2.1 空参列表
# 插入到girls.admin表5条记录
USE girls;
SELECT * FROM admin;
DELIMITER $ # 设置结束标记
# 存储过程的创建需要在CMD下执行
CREATE PROCEDURE p1() BEGIN
	INSERT INTO admin(username, PASSWORD) VALUES('tom', 0000),('kav', 0000), ('swed', 0000), ('abdc', 0000), ('jerry', 0000)
END $
CALL p1()$
# 2.2 带in模式的参数的存储过程
# 根据女神名查询对应的男人信息
CREATE PROCEDURE p2(IN gname VARCHAR(20)) BEGIN
	SELECT bo.* FROM boys bo RIGHT JOIN beauty b ON bo.id = b.boyfriend_id WHERE b.name = gname;
END $

CALL p2('柳岩')$
# 验证用户名密码是否正确
CREATE PROCEDURE p3(IN username VARCHAR(10), IN pwd VARCHAR(10)) BEGIN
	DECLARE result INT DEFAULT 0;
	SELECT COUNT(*) INTO result FROM admin WHERE admin.`username` = username AND admin.`password` = pwd;
	SELECT IF(result>0, '成功', '失败');
END $
CALL p3('tom', 666);
# 2.3 带out模式参数的存储过程
# 2.3.1 一个out  根据女人名，返回男友名
CREATE PROCEDURE p4(IN bname VARCHAR(20), OUT boname VARCHAR(20)) BEGIN 
	SELECT boys.`boyName` INTO boname FROM boys bo INNER JOIN beauty b ON boys.`id` = beauty.`boyfriend_id` WHERE b.name = bname;
END $
SET @bName$
CALL p4('柳岩', @bName)$
SELECT @bName$
# 2.3.2 多个out - 根据女人名，返回男人名和魅力值
CREATE PROCEDURE p5(IN beautyname VARCHAR(20), OUT boyname VARCHAR(20), OUT usercp INT) BEGIN
	SELECT bo.boyName, bo.userCP INTO boyname, usercp FROM boys bo INNER JOIN beauty b ON bo.id = b.boyfriend_id WHERE bo.name = beautyname;
END $
CALL p5('柳岩', @bName, @userCP)$
SELECT @bName, @userCP$
# 2.4 带inout模式参数的存储过程
# 传入a,b a,b翻倍返回
CREATE PROCEDURE p6(INOUT a INT, INOUT b INT) BEGIN 
	SET a=a*2;
	SET b=b*2;
END $
SET @m=10$
SET @n=20$
CALL p6(@m, @n)$
SELECT @m, @n$
# 3 函数
USE myemployees$
# 3.1 无参
CREATE FUNCTION f1() RETURNS INT BEGIN
	DECLARE co INT DEFAULT 0;
	SELECT COUNT(*) INTO co FROM employees;
	RETURN co;
END $
SELECT f1()$
# 3.2 有参
# 3.2.1 根据员工名，返回工资
CREATE FUNCTION f2(empName VARCHAR(20)) RETURN DOUBLE BEGIN
	SET @sal=0; # 用户变量
	DECLARE sala DOUBLE; # 局部变量
	SELECT salary INTO @sal FROM employees WHERE last_name = empName;
	RETURN @sal;
END $
SELECT f2('K_ing')$


# 4 流程控制结构
# 4.1 分支结构
# 4.1.1 case作为独立的语句
# 根据传入的成绩，显示等级
CREATE PROCEDURE p7(IN score INT) BEGIN 
	CASE 
	WHEN score >= 90 AND score <= 100 THEN SELECT 'A';
	WHEN socre >= 80 THEN SELECT 'B';
	WHEN score >= 70 THEN SELECT 'C';
	WHEN score >= 60 THEN SELECT 'D';
	ELSE SELECT 'E';
	END CASE;
END
CALL p7(97)$
# 4.1.2 if结构
# 根据传入的成绩，返回等级
CREATE FUNCTION fun_score(score INT) RETURNS CHAR BEGIN
	IF score>=90 AND score <=100 THEN RETURN 'A';
	ELSEIF score>=80 THEN RETURN 'B';
	ELSEIF score>=70 THEN RETURN 'C';
	ELSEIF score>=60 THEN RETURN 'D';
	ELSE RETURN 'E';
	END IF;
END $
SELECT fun_score(78);
# 4.2 循环结构
# 4.2.1 不带循环控制语句
# 批量插入数据
CREATE PROCEDURE pro_while1(IN insertCount INT) BEGIN
	DECLARE i INT DEFAULT 1;
	WHILE i <= insertCount DO
		INSERT INTO admin(username, PASSWORD) VALUES(CONCAT('tom', i), 6666)
		SET i = i +1;
	END WHILE;
END $
CALL pro_while(100)$
# 4.2.2 添加leave语句
# 批量插入数据，插入20次，跳出
CREATE PROCEDURE pro_while2(IN insertCount INT) BEGIN
	DECLARE i INT DEFAULT 1;
	a: WHILE i <= insertCount DO
		INSERT INTO admin(username, PASSWORD) VALUES(CONCAT('jerry', i), 8888)
		SET i = i +1;
		IF i>=20 THEN LEAVE a; END IF;
	END WHILE a;
END $
CALL pro_while2(120)$
# 4.2.3 添加iterate语句
# 批量插入数据，只插入偶数次
CREATE PROCEDURE pro_while3(IN insertCount INT) BEGIN
	DECLARE i INT DEFAULT 0;
	a:WHILE i <= insertCount DO
		SET i = i +1;
		IF MOD(i, 2) != 0 THEN ITERATE a; END IF;
		INSERT INTO admin(username, PASSWORD) VALUES(CONCAT('tom', i), 6666)
	END WHILE a;
END $
CALL pro_while3(100)$


# example
# 向表stringcontent中插入指定个数的随机的字符串
USE test;
CREATE TABLE IF NOT EXISTS stringcontent(id INT PRIMARY KEY AUTO_INCREMENT, content VARCHAR(26));
CREATE PROCEDURE test_randstr_insert(IN insertCount INT) BEGIN
	DECLARE i INT DEFAULT 0; # 插入次数s
	DECLARE str VARCHAR(20) DEFAULT 'abcdefghijklmnopqrstuvwxyz';
	DECLARE startIndex INT DEFAULT 0; # 起始索引
	DECLARE len INT DEFAULT 0; # 截取长度
	WHILE i<= insertCount DO 
		SET startIndex = FLOOR(RAND() * 26 + 1); # 产生一个随机的整数 floor-向下转型，ceil-向上转型
		SET len = FLOOR(RAND() * (20 - startIndex + 1) + 1);
		INSERT INTO stringcontent(content) VALUES(SUBSTR(str, startIndex, len));
		SET i = i + 1; # 循环变量更新
	END WHILE;
END
CALL test_randstr_insert(10);


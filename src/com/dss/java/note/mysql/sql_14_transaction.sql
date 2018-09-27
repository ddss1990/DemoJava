# 事务
/*
 TCL - Transaction Control Language 事务控制语言
 一个或一组SQL语句组成一个执行单元，这个执行单元要么全部执行，要么全部不执行。例：转账
   执行失败或错误，整个单元会回滚，回到最初的状态
 
  存储引擎(表类型)
    不同的存储技术
      show engines;  -  查看支持的存储引擎，innodb(default)
    innodb支持事务，myisam,memory不支持事务
    
  事务的特点/ACID属性 ⭐
    Atomicity    -  原子性，事务不可分割，要么全部执行，要么全部不执行
    Consistency  -  一致性，事务执行必须是从一个一致性状态到另一个一致性状态。意思是转账余额需要保持一致
    Isolation    -  隔离性，一个事务的执行不受其它事务的干扰，即事务的内部操作及使用的数据对并发的其它事务是隔离的，并发事务之间不能相互干扰
    Durability   -  持久性，事务一旦提交，就是对数据库中的数据永久性的改变
  事务的创建
    隐式事务 -  无明显的开启和结束的标记
      例如： insert, update, delete 语句
    显示事务 -  有明显的开启和结束的标记
      必须先设置自动提交功能为禁用。因为如果自动提交功能是开启的话，执行完一条SQL语句(增、删、改)就会默认的当做一个事务进行提交。那么紧接着的SQL语句，如果执行错误，就没法保证一致性
      1. 开启事务
      set autocommit=0;
      start transaction; # 可选
      2. 编写事务中的语句
        这里可以有多个SQL语句
      3. 结束事务，二选一
      commit; # 提交事务 - 将事务中修改的内容保存到数据库
      roolback; # 回滚事务 - 不保存事务中修改的内容

    事务的并发问题
      同时运行多个事务，这些事务操作相同的数据时，如果没有隔离机制，会导致并发问题。类似于Java中的同步
      导致的并发问题：两个事务T1,T2
        脏读：一个事务读取了另一事务修改但未提交的数据，如果此事务回滚，那么读取的数据就是临时且无效的
        不可重复读：多次读取相同字段，得到的结果不一样
          不一样: T1更新了数据，还未提交，T2读取数据是原来的值；T1进行提交，T2再次读取数据，数据改变。在T2中，读取同一数据，出现了两次不一样的结果。
        幻读：读取同一字段，得到的行数不一样。和脏读类似，脏读读到的是其它事务更新的数据，幻读读到的是其它事务新插入的数据。
    隔离级别
      READ UNCOMMITED - 读未提交数据，允许事务读取未被其它事务提交的变更，以上三种现象都会出现
      READ COMMITED   - 读已提交数据，避免了脏读
      REPEATABLE READ - 可重复读，避免了脏读，不可重复读
      SERIALIZABLE    - 串行化，可全部避免，但性能低
     Oracle 支持：READ COMMITED(default), SERIALIZABLE
     MySQL都支持，默认是 REPEATABLE READ
     查看隔离级别
       SHOW VARIABLES LIKE '%isolation%';
       SELECT @@tx_isolation;
     设置隔离级别
       SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED; # 当前连接
       SET GLOBAL TRANSACTION ISOLATION LEVEL READ COMMITTED;  # 全局
     
    回滚点 - 只搭配rollback使用
      savepoint NAME; 设置保存点
      rollback to NAME; 回滚到保存点
  delete和truncate在事务中的区别
    truncate是实际的删除，不支持回滚
    delete支持回滚
*/
START TRANSACTION;
SHOW ENGINES;
SHOW VARIABLES LIKE '%autocommit%';
USE test;
DROP TABLE IF EXISTS account;
CREATE TABLE IF NOT EXISTS account(_id INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(20) NOT NULL, balance FLOAT);
INSERT INTO account VALUES(1000, '李莫愁', 1000), (1001, '小龙女', 1000);
SELECT * FROM account;
# 查看隔离级别
SHOW VARIABLES LIKE '%isolation%'; 
SELECT @@tx_isolation;
# 演示事务的隔离级别，需要在CMD中操作
# 1. 设置隔离级别
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
# set names gbk; # 在cmd中中文出现乱码，更改字符集显示










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
    显示事务
*/
SHOW ENGINES;
SHOW VARIABLES LIKE '%autocommit%';
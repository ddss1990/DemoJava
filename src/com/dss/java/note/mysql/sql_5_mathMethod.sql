# 5 数学函数
# 5.0 选择数据库
USE myemployees;

# 5.1 round - 四舍五入
SELECT ROUND(1.65);
SELECT ROUND(1.4);
SELECT ROUND(1.567, 2) AS 'round';

# 5.2 ceil - 向上取整
SELECT CEIL(1.52), CEIL(1.02), CEIL(1.00);

# 5.3 floor - 向下取整
SELECT FLOOR(1.52), FLOOR(2.00), FLOOR(-1.5);

# 5.4 truncate - 截断
SELECT TRUNCATE(1.68343, 3);

# 5.5 mod - 取余
SELECT MOD(10, 3);

# 6. 日期函数
# 6.1 now - 当前系统日期和时间
SELECT NOW();

# 6.2 curdate - 当前日期，不包含时间
SELECT CURDATE();
# 6.3 curtime - 当前时间，不包含日期
SELECT CURTIME();

# 6.4 分别获得年、月、日、时、分、秒
SELECT YEAR(NOW()) AS 'year', MONTH(NOW()) 'month', DAY(NOW()) 'day', HOUR(NOW()) AS  'hour', MINUTE(NOW()) 'minute', SECOND(NOW()) 'second';
SELECT MONTHNAME(CURDATE()), DAYNAME(CURDATE()) ;

# 6.5 str_to_date 将指定字符串按指定格式转成日期格式
# date_formate 将日期格式按指定格式转成字符串
/*
 %Y - 4位制的年份     	%y - 2位制的年份
 %m - 月份，补0(01, 02)	  %c - 月份，不补0
 %d - 日
 %H - 24小时制      %h - 12小时制
 %i - 分钟          %s - 秒
*/
SELECT STR_TO_DATE('12-03-1923', '%d-%m-%Y');
# example - 查询入职日期为 1992-4-3的员工信息
SELECT * FROM employees WHERE hiredate = STR_TO_DATE('4-3 1992', '%c-%d %Y');
# 6.6 to_days函数 - 将日期转换成天数
select TO_DAYS(NOW());
# 6.7 others
/* 
 dayofweek('')   -  返回是一周的第几天，1-日，2-一，3-二，4-三，5-四，6-五，7-六
 weekday('')     -  返回日期的星期索引，0-一，1-二，2-三，3-四，4-五，5-六，6-日
 dayofmonth('')  -  返回日期是月份中的哪一天，1~31
 dayofyear('')   -  日期在当年中的日数
 dayname('')     -  日期的星期名字
 monthname('')   -  日期的月份名字
 quarter('')     -  日期是哪一季度，1~4
 last_day('')    -  日期当月的最后一天
*/

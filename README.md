# weather

用来记录历史天气数据，以便后续参考

历史天气数据主要来自于[北京2017年12月份天气](http://lishi.tianqi.com/beijing/201712.html)

## 常见问题

org.springframework.dao.DeadlockLoserDataAccessException

MySQL中行锁锁的不是记录，而是索引。所以在update和delete的时候最好根据主键去处理，这样可以避免对不同记录进行的写操作使用到相同的索引中的数据。

比如，此次死锁案例中的update语句

```SQL
update weather_month set crawlFlag='1', crawlCount=1, updateTime=?
    	where areaCode=? and yearMonth = ?
```

weather_month表中areaCode和yearMonth都有索引idx_weather_month_areacode,idx_weather_month_yearmonth

```MySQL
id |select_type |table         |type        |possible_keys                                          |key                                                    |key_len |ref |rows |Extra                                                                                |
---|------------|--------------|------------|-------------------------------------------------------|-------------------------------------------------------|--------|----|-----|-------------------------------------------------------------------------------------|
1  |SIMPLE      |weather_month |index_merge |idx_weather_month_areacode,idx_weather_month_yearmonth |idx_weather_month_areacode,idx_weather_month_yearmonth |123,27  |    |1    |Using intersect(idx_weather_month_areacode,idx_weather_month_yearmonth); Using where |
```

在更新的时候，不同的线程更新的数据是不同的，线程一更新的是('xxx', '201812')线程二更新的是('yanhuqu', '201812')，虽然areacode和yearMonth结合起来不同，但是yearMonth是相同的，这两条数据使用到的索引数据是相同的。

根据上面所说MySQL的InnoDB的行锁锁的是索引，那么这两条条update语句就会死锁。

解决办法是，创建areacode, yearmonth这两列上的组合索引

```SQL
CREATE INDEX idx_weather_month_areacode_yearmonth ON weather_month (areacode, yearmonth);
```

最佳方法应该是update和delete根据主键进行操作，只不过这个案例中使用组合索引可以避免修改程序。

[MySQL中的锁（表锁、行锁）](https://www.cnblogs.com/chenqionghe/p/4845693.html)

死锁是否可以有mysql的相关日志

[Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException: Deadlock found when tryin](https://blog.csdn.net/zheng0518/article/details/54695605)

[MySQL更新死锁问题](http://ju.outofmemory.cn/entry/199937)

[MySQL更新死锁问题](https://www.cnblogs.com/softidea/p/5816502.html)

[InnoDB数据库死锁问题处理](https://www.jb51.net/article/81020.htm)

## 参考资料

[springboot 学习之路 9 (项目启动后就执行特定方法)](https://www.cnblogs.com/huhongy/p/8183390.html)

[springboot 启动后执行特定的方法](https://blog.csdn.net/nimoyaoww/article/details/79299201)

定时任务

[SpringBoot-@Schedule定时任务](https://blog.csdn.net/ysp_0607/article/details/71430281)

[Spring官网定时任务示例](https://spring.io/guides/gs/scheduling-tasks/)

[SpringBoot几种定时任务的实现方式](https://blog.csdn.net/wqh8522/article/details/79224290)
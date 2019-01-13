# MySQL查询优化的一些笔记

通过weather这个项目中涉及到的sql来记录自己对sql优化的学习

## 查询1

### 概况

weather_city表有

```MySQL
# 查询出哪些city的crawlFlag为1，但是并没有爬取成功
select * from weather_city c
where crawlFlag="1"
and not exists (select * from weather_month m where c.areaCode=m.areaCode)

```

匹配到了15条记录

执行计划

```MySQL
explain select * from weather_city c
where crawlFlag="1"
and not exists (select * from weather_month m where c.areaCode=m.areaCode)
```


id |select_type        |table |type |possible_keys |key |key_len |ref |rows   |Extra       |
---|-------------------|------|-----|--------------|----|--------|----|-------|------------|
1  |PRIMARY            |c     |ALL  |              |    |        |    |3197   |Using where |
2  |DEPENDENT SUBQUERY |m     |ALL  |              |    |        |    |263429 |Using where |


## 参考资料

[MySQL高级 之 explain执行计划详解](https://blog.csdn.net/wuseyukui/article/details/71512793)

[MySQL explain执行计划解读](https://blog.csdn.net/xifeijian/article/details/19773795)

[mysql的sql执行计划详解（非常有用）](https://blog.csdn.net/heng_yan/article/details/78324176)

[(转)学习MySQL优化原理，这一篇就够了！](https://www.cnblogs.com/liujiacai/p/7605612.html)

[MySQL 性能优化的最佳20多条经验分享](http://www.cnblogs.com/pengyunjing/p/6591660.html)

MySQL查询执行时间

[MySQL 的性能（上篇）—— SQL 执行分析](https://www.cnblogs.com/xueweihan/p/6864401.html)  推荐

[MySQL 的性能（下篇）—— 性能优化方法](http://www.cnblogs.com/xueweihan/p/6900652.html)

[MySQL慢查询日志总结](http://www.cnblogs.com/kerrycode/p/5593204.html)

[MYSQL中SQL执行分析](https://inter12.iteye.com/blog/1420789)

[Mysql性能优化一](http://www.cnblogs.com/jiekzou/p/5371085.html)

[Mysql性能优化二](http://www.cnblogs.com/jiekzou/p/5380073.html)

MySQL数据导入导出

[mysql数据的导入导出](https://www.cnblogs.com/regit/p/8041762.html)

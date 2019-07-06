# 股票信息查询系统

## 系统介绍
该服务启动后，将自动拉取近10年数据存入数据库.每天00:00将当天数据写入数据库

## 系统代码配置
Spring Boot
Maven
MySql 5.7+
Redis 5.0+

## 接口

http://{host}/stock/stock/all 获得所有股票的名称以及股票id

http://{host}/stock/history/getCount 前30天涨幅超过5%的股票统计

http://{host}/stock/history/search/{stockId}/{size}/{page}  股票详情查看,参数解释如下:

&nbsp;&nbsp;&nbsp;&nbsp;stockId:股票id

&nbsp;&nbsp;&nbsp;&nbsp;size:一次查询的数量

&nbsp;&nbsp;&nbsp;&nbsp;page:当前页数

##### 参数说明

| 参数名 | 必选 | 类型   | 说明     | 备注                       |
| ------ | ---- | ------ | -------- | -------------------------- |
| update | 是   | String | 更新标志 | 0或者10代表不更新1代表更新 |

##### 

path:https://freegatty.swuosa.xenoeye.org/api/grade/all/0


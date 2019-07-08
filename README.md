# 股票信息查询系统

## 环境要求

Spring Boot    v2.1.6

Maven    3.6+

MySql    5.7+

Redis     5.0+



## 功能

- 启动时同步数据 - 使用spring boot框架的事件机制，在所有bean初始化完成后，开始同步股票信息及历史数据到数据库中
- 定时任务 - 使用Quartz库，实现当前股票数据定时更新
- 数据缓存 - 使用redis缓存数据库



## 安装与部署

mysql 需设置ip:127.0.0.1 port:3306

redis 需设置ip:127.0.0.1 port:6379

经maven编译后，直接通过 java -jar 项目名称.jar 运行


## 接口说明



### 获得所有股票的名称以及股票id

##### 请求URL

- http://{host}/stock/stock/all

##### 请求方式

- GET

##### 参数说明

- 无参数 

##### 请求示例

- path: http://localhost:8080/stock/stock/all

- method: GET



### 前30天涨幅超过5%的股票统计

##### 请求URL

- http://{host}/stock/history/getCount

##### 请求方式

- GET

##### 参数说明

- 无参数

##### 请求示例

- path: http://localhost:8080/stock/history/getCount

- method: GET



### 股票详情查看

##### 请求URL

- http://{host}/stock/history/search/{stockId}/{size}/{page}

##### 请求方式

- GET

##### 参数说明

| 参数名  | 必选 | 类型 |         说明         | 备注 |
| :-----: | :--: | :--: | :------------------: | :--: |
| stockId |  是  | int  |        股票id        |      |
|  size   |  是  | int  | 分页时，每一页的大小 |      |
|  page   |  是  | int  |      分页的页码      |      |

##### 请求示例

- path: http://localhost:8080/stock/history/search/sh600000/10/1

- method: GET

drop database if exists stock;

create database stock;

use stock;

CREATE TABLE `history`
(
    `id`           int(11)        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `stock_name`   varchar(32)    NOT NULL COMMENT '股票名称',
    `stock_id`     varchar(8)     NOT NULL COMMENT '股票id\n',
    `opening_date` date           NOT NULL COMMENT '开盘日期',
    `open_price`   decimal(10, 4) NOT NULL DEFAULT '0.0000' COMMENT '开盘价',
    `close_price`  decimal(10, 4) NOT NULL DEFAULT '0.0000' COMMENT '收盘价',
    `max_price`    decimal(10, 4) NOT NULL DEFAULT '0.0000' COMMENT '最高价',
    `min_price`    decimal(10, 4) NOT NULL DEFAULT '0.0000' COMMENT '最低价',
    `trade_amount` int(8)         NOT NULL DEFAULT '0' COMMENT '交易量',
    PRIMARY KEY (`id`),
    KEY `history__index_stock_id` (`stock_id`),
    KEY `history__index_stock_name` (`stock_name`),
    KEY `history__index_stockId_stockName` (`stock_id`, `stock_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `stock`
(
    `id`         int(11)     NOT NULL AUTO_INCREMENT COMMENT 'id',
    `stock_name` varchar(32) NOT NULL COMMENT '股票名',
    `stock_id`   varchar(8)  NOT NULL COMMENT '股票id',
    PRIMARY KEY (`id`),
    KEY `stock__index_stock_id` (`stock_id`),
    KEY `stock__index_stock_name` (`stock_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8

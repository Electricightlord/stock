package com.lihao.stock.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class HistoryObject {
    @JsonIgnore
    private Integer id;
    private String stockName;
    private String stockId;
    private Date openingDate;
    private Double openPrice;
    private Double closePrice;
    private Double maxPrice;
    private Double minPrice;
    private Integer tradeAmount;

    public HistoryObject(){}

    public HistoryObject(String stockName, String stockId, Date openingDate, Double openPrice, Double closePrice, Double maxPrice, Double minPrice, Integer tradeAmount) {
        this.stockName = stockName;
        this.stockId = stockId;
        this.openingDate = openingDate;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.tradeAmount = tradeAmount;
    }
}

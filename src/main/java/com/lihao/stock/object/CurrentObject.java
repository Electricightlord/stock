package com.lihao.stock.object;

import lombok.Data;

import java.util.Date;

@Data
public class CurrentObject {
    private String stockName;
    private String stockId;
    private Date openingDate;
    private Double currentPrice;
    private Double openPrice;
    private Double yesterdayPrice;
    private Double maxPrice;
    private Double minPrice;
    private Integer tradeAmount;
    @Override
    public String toString() {
        return "CurrentObject{" +
                "stockName='" + stockName + '\'' +
                ", stockId='" + stockId + '\'' +
                ", openingDate=" + openingDate +
                ", currentPrice=" + currentPrice +
                ", openPrice=" + openPrice +
                ", yesterdayPrice=" + yesterdayPrice +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", tradeAmount=" + tradeAmount +
                '}';
    }

    public HistoryObject toHistoryObject(){
        HistoryObject historyObject=new HistoryObject();
        historyObject.setStockName(this.stockName);
        historyObject.setStockId(this.stockId);
        historyObject.setOpeningDate(this.openingDate);
        historyObject.setMaxPrice(this.maxPrice);
        historyObject.setMinPrice(this.minPrice);
        historyObject.setOpenPrice(this.openPrice);
        historyObject.setClosePrice(this.yesterdayPrice);
        historyObject.setTradeAmount(this.tradeAmount);
        return historyObject;
    }
}

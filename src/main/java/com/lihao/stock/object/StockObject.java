package com.lihao.stock.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class StockObject {
    @JsonIgnore
    private Integer id;
    private String stockId;
    private String stockName;
}

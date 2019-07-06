package com.lihao.stock.controller;

import com.lihao.stock.object.StockObject;
import com.lihao.stock.service.impl.StockServiceImpl;
import com.lihao.stock.spider.impl.SpiderStockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    SpiderStockServiceImpl spiderStockService;

    @GetMapping("/all")
    public ModelAndView getAllStocks() {
        List<StockObject> stockObjectList = stockService.getAllStocks();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("stocks", stockObjectList);
        modelAndView.setViewName("stocks");
        return modelAndView;
    }
}

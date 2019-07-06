package com.lihao.stock.controller;

import com.github.pagehelper.PageInfo;
import com.lihao.stock.object.HistoryObject;
import com.lihao.stock.service.impl.HistoryServiceImpl;
import com.lihao.stock.spider.impl.SpiderHistoryServiceImpl;
import com.lihao.stock.spider.impl.SpiderStockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    HistoryServiceImpl historyServiceImpl;

    @Autowired
    SpiderHistoryServiceImpl spiderHistoryService;

    @Autowired
    SpiderStockServiceImpl spiderStockService;

    @GetMapping("/search/{stockId}/{size}/{page}")
    public ModelAndView search(@PathVariable("stockId") String stockId, @PathVariable(value = "size") int size, @PathVariable("page") int page){
        PageInfo<HistoryObject> pageInfo= historyServiceImpl.findByStockId(stockId,size,page);
        System.out.println(pageInfo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("stockId",stockId);
        modelAndView.setViewName("history");
        return modelAndView;
    }

    @GetMapping("/getCount")
    public List<Map<String,Object>> getCount(){
        return historyServiceImpl.getCount();
    }

}

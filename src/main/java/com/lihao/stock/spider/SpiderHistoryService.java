package com.lihao.stock.spider;

import com.lihao.stock.object.StockObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface SpiderHistoryService {
    void getHistoryInfos(List<StockObject> stockObjectList) ;

    static Date stringToDate(String dateInString, String format) {
        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        //必须捕获异常
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateInString.trim());
        } catch (ParseException px) {
            px.printStackTrace();
        }
        return date;
    }

    static int getSysYear() {
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.YEAR) % 100;
    }
}

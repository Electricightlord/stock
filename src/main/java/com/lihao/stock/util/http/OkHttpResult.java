package com.lihao.stock.util.http;

import lombok.Data;
import okhttp3.Cookie;
import okhttp3.Headers;

import java.io.Serializable;
import java.util.List;

@Data
public class OkHttpResult implements Serializable {
    private static final long serialVersionUID = 2168152194164783950L;

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;

    /**
     * 响应头
     */
    private Headers header;

    /**
     * Cookies
     *
     * @return
     */
    private List<Cookie> cookies;

    @Override
    public String toString() {
        return "OkHttpResult{" +
                "code=" + code +
                ", content='" + content + '\'' +
                ", header=" + header +
                ", cookies=" + cookies +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public OkHttpResult(int code) {
        this.code = code;
    }

    public OkHttpResult(int code, String content, Headers header) {
        this.code = code;
        this.content = content;
        this.header = header;
    }

    public OkHttpResult(int code, String content, Headers header, List<Cookie> cookies) {
        this.code = code;
        this.content = content;
        this.header = header;
        this.cookies = cookies;
    }
}

package com.lihao.stock.util.http;

import okhttp3.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OkHttp {

    public static OkHttpResult doGet(String url, Map<String, String> header) {
        OkHttpResult okHttpResult = new OkHttpResult(500);
        OkHttpClient okHttpClient = getOkHttpClient();
        Request.Builder builder = new Request.Builder();
        Headers.Builder headers = new Headers.Builder();

        if (header != null) {
            Set<String> keySet = header.keySet();
            for (String key : keySet) {
                String value = header.get(key);
                headers.add(key, value);
            }
        }
        Request request = builder.get().url(url).headers(headers.build()).build();
        try {
            Response response;
            response = okHttpClient.newCall(request).execute();
            okHttpResult = new OkHttpResult(response.code(), new String(response.body().bytes(), "gb2312"), response.headers(), ((CookieJarImpl) okHttpClient.cookieJar()).getCookieStore().getCookies());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return okHttpResult;
    }

    public static OkHttpResult doGet(String url) {
        return doGet(url, null);
    }

    private static OkHttpClient getOkHttpClient() {
        CookieJar cookieJar = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient.Builder clientBu = new OkHttpClient.Builder();
        clientBu.connectTimeout(3600, TimeUnit.SECONDS);
        clientBu.writeTimeout(3600, TimeUnit.SECONDS);
        clientBu.readTimeout(3600, TimeUnit.SECONDS);
        clientBu.cookieJar(cookieJar);
        return clientBu.build();
    }
}

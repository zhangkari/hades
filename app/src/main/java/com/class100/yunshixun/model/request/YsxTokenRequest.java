package com.class100.yunshixun.model.request;

import com.class100.hades.http.YsxApiRequest;

import java.util.Map;

public class YsxTokenRequest extends YsxApiRequest {
    private static final String API_URL = UrlRegister.URL_GET_TOKEN;
    private static final String IDENTITY = "6ff56fecf02981471ce1c319f520be2f";

    private String mobile;
    public String password;

    public YsxTokenRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    @Override
    protected String getUrl() {
        return API_URL;
    }

    @Override
    protected Map<String, String> buildParameters(Map<String, String> map) {
        map.put("mobile", mobile);
        map.put("password", password);
        return map;
    }
}
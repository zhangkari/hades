package com.class100.yunshixun.model.request;

import com.class100.hades.http.HadesManifest;
import com.class100.hades.http.YsxApiRequest;

import java.util.Map;

public class YsxTokenRequest extends YsxApiRequest {
    private static final String API_URL = UrlRegister.URL_GET_TOKEN;

    private final String mobile;
    public final String password;

    public YsxTokenRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    @Override
    protected String getHost() {
        return HadesManifest.host_ipower_token;
    }

    @Override
    protected String getUrl() {
        return API_URL;
    }

    @Override
    protected void buildParams(Map<String, String> map) {
        map.put("mobile", mobile);
        map.put("password", password);
    }
}
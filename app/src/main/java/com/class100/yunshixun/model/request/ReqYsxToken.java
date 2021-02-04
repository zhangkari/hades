package com.class100.yunshixun.model.request;

import com.class100.hades.http.HaApiRequest;

public class ReqYsxToken extends HaApiRequest {
    private static final String API_URL = UrlRegister.URL_GET_TOKEN;

    private final String mobile;

    public ReqYsxToken(String mobile) {
        this.mobile = mobile;
    }

    @Override
    protected String getUrl() {
        return API_URL + "/" + mobile;
    }
}
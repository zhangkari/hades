package com.class100.yunshixun.model.request;

import com.class100.hades.http.HaRequestDispatcher;
import com.class100.hades.http.ysx.YsxApiRequest;
import com.class100.hades.http.ysx.YsxApiToken;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

public class ReqYsxMyMeetings extends YsxApiRequest {
    private static final String API_URL = UrlRegister.URL_MY_MEETINGS;

    @Override
    protected String getUrl() {
        return API_URL;
    }

    @Override
    protected void buildParams(Map<String, String> map) {
        map.putAll(buildParams());
    }

    private Map<String, String> buildParams() {
        Map<String, String> map = new HashMap<>();
        String token = Base64.encodeBase64String(YsxApiToken.INSTANCE.getToken().getBytes());
        map.put("token", token);
        return map;
    }

    public String getCompleteUrl() {
        String url = HaRequestDispatcher.buildCompleteUrl(getUrl(), getHost());
        return HaRequestDispatcher.appendGetParameters(url, buildParams());
    }
}

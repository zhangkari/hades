package com.class100.hades.http.ysx;

import com.class100.hades.http.HaRequest;

import org.apache.commons.codec.binary.Base64;

import java.util.Map;

// Yunshixun Api request
public abstract class YsxApiRequest extends HaRequest {
    @Override
    protected String getHost() {
        return "yunshixun";
    }

    protected void buildHeader(Map<String, String> map) {

    }

    @Override
    protected void buildHeaders(Map<String, String> map) {
        buildHeader(map);
        map.put("Content-Type", "application/json");
        String token = YsxApiToken.INSTANCE.getToken();
        map.put("X-ACCESS-TOKEN", Base64.encodeBase64String(token.getBytes()));
    }

    protected void buildParams(final Map<String, String> map) {

    }

    @Override
    protected void buildParameters(Map<String, String> map) {
        buildParams(map);
    }
}

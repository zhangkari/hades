package com.class100.hades.http;

import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtMD5;
import com.class100.atropos.generic.AtTexts;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Yunshixun Api request
public abstract class YsxApiRequest extends HaRequest {
    private static final String IDENTITY = "6ff56fecf02981471ce1c319f520be2f";
    private static final String KEY = "8525c7db3d3d55ece4fbc08394f135d4";

    @Override
    protected String getHost() {
        return HadesManifest.host_ipower;
    }

    @Override
    public Map<String, String> buildHeaders(Map<String, String> map) {
        map.put("Content-Type", "application/json");
        return super.buildHeaders(map);
    }

    protected void buildParams(final Map<String, String> map) {

    }

    @Override
    protected final Map<String, String> buildParameters(Map<String, String> map) {
        buildParams(map);
        map.put("identity", IDENTITY);
        map.put("sign", signParams(KEY, map));
        return map;
    }

    protected static String signParams(String key, Map<String, String> params) {
        params = filterParameters(params);
        String link = linkParams(params);
        return AtMD5.signYsxApi(link, key, StandardCharsets.UTF_8.name());
    }

    private static Map<String, String> filterParameters(Map<String, String> params) {
        if (AtCollections.isEmpty(params)) {
            return params;
        }
        Map<String, String> result = new HashMap<>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (AtTexts.isEmpty(entry.getValue())) {
                continue;
            }
            if ("sign".equalsIgnoreCase(entry.getValue())) {
                continue;
            }
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static String linkParams(Map<String, String> param) {
        List<String> keys = new ArrayList<>(param.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        final int size = keys.size();
        for (int i = 0; i < size; i++) {
            String key = keys.get(i);
            String value = param.get(key);
            if (i == keys.size() - 1) {
                sb.append(key)
                    .append("=")
                    .append(value);
            } else {
                sb.append(key)
                    .append("=")
                    .append(value)
                    .append("&");
            }
        }
        return sb.toString();
    }
}

package com.class100.hades.http.ysx;

import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtMD5;
import com.class100.atropos.generic.AtTexts;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Yunshixun Sign Api request
public abstract class YsxSignApiRequest extends YsxApiRequest {
    // TODO  save them in security env
    private static final String IDENTITY = "08ccad5103ce1d4ee402aee4c7937c8f";
    private static final String KEY = "f5400cecdac750ac344919ba50381d29";

    @Override
    protected final void buildParameters(Map<String, String> map) {
        super.buildParameters(map);
        map.put("identity", IDENTITY);
        map.put("sign", signParams(KEY, map));
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
            if ("sign".equalsIgnoreCase(entry.getKey())) {
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

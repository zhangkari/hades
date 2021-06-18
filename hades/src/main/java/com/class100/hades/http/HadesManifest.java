package com.class100.hades.http;

import java.util.HashMap;
import java.util.Map;

public interface HadesManifest {
    String host_invalid = "";
    String host_mainApp = "host_class100";
    String host_ipower_api = "host_ipower_api";
    String host_ipower_token = "host_ipower_token";

    String group_none = "";
    String group_mainApp = "api_class100";

    Map<String, String[]> HostTable = new HashMap<String, String[]>() {
        {
            put(host_mainApp, new String[]{
                "https://yun-tv.dev.class100.com",
                "https://yun-tv.qa.class100.com",
                "https://yun-tv.qa.class100.com"}
            );
            put(host_ipower_api, new String[]{
                "https://meeting.125339.ebupt.net",
                "https://meetingpre.125339.ebupt.net",
                "https://meetingpre.125339.ebupt.net"
            });
            put(host_ipower_token, new String[]{
                "https://contacts.125339.com.cn",
                "https://contacts.125339.com.cn",
                "https://contacts.125339.com.cn"
            });
        }
    };

    Map<String, Class<?>> GroupTable = new HashMap<String, Class<?>>() {
        {
            put(group_mainApp, HaApiResponse.class);
        }
    };

}
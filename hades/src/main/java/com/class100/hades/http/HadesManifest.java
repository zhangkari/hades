package com.class100.hades.http;

import java.util.HashMap;
import java.util.Map;

public interface HadesManifest {
    String host_mainApp = "host_class100";
    String host_ipower = "host_ipower";

    String wrapper_mainApp = "api_class100";

    Map<String, String[]> HostTable = new HashMap<String, String[]>() {
        {
            // TODO
            put(host_mainApp, new String[]{
                "http://class100.com",
                "http://class100-qa.com",
                "http://class100-dev.com"}
            );
            put(host_ipower, new String[]{
                "https://meeting.125339.ebupt.net/mixapi",
                "https://contacts.125339.com.cn",
                "https://meetingpre.125339.ebupt.net/mixapi"
            });
        }
    };

    Map<String, Class<?>> ApiWrapperTable = new HashMap<String, Class<?>>() {
        {
            // todo
            put(wrapper_mainApp, HaApiResponse.class);
        }
    };

}
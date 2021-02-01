package com.class100.hades.http;

import java.util.HashMap;
import java.util.Map;

public interface HadesManifest {
    String mainApp = "100";
    String ipower = "200";

    Map<String, String[]> HostTable = new HashMap<String, String[]>() {
        {
            // TODO
            put(mainApp, new String[]{
                "http://class100.com",
                "http://class100-qa.com",
                "http://class100-dev.com"}
            );
            put(ipower, new String[]{
                "https://meeting.125339.ebupt.net/mixapi",
                "https://meetingpre.125339.ebupt.net/mixapi",
                "https://meetingpre.125339.ebupt.net/mixapi"
            });
        }
    };
}
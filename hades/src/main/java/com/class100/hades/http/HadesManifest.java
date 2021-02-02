package com.class100.hades.http;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public interface HadesManifest {
    String mainApp = "class100";
    String ipower = "ipower";

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
                "https://contacts.125339.com.cn",
                "https://meetingpre.125339.ebupt.net/mixapi"
            });
        }
    };

    Map<String, RequestBody> RequestTable = new HashMap<String, RequestBody>() {
        {
            // todo
            put(mainApp, RequestBody.create("", MediaType.parse("application/json")));
        }
    };

    HashMap<String, Class<? extends HaApiResponse<?>>> ResponseTable = new HashMap<String, Class<? extends HaApiResponse<?>>>() {
        {
            /* todo */
            // put(mainApp, (Class<? extends HaApiResponse<?>>) HaApiResponse.class);
        }
    };
}
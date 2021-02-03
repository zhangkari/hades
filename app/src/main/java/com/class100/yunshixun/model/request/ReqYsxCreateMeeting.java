package com.class100.yunshixun.model.request;

import com.class100.hades.http.ysx.YsxApiRequest;

import java.util.Map;

public class ReqYsxCreateMeeting extends YsxApiRequest {
    private static final String API_URL = UrlRegister.URL_CREATE_MEETING;

    @Override
    protected String getUrl() {
        return API_URL;
    }

    @Override
    protected int getMethod() {
        return METHOD_POST;
    }

    @Override
    protected void buildParams(Map<String, String> map) {
        map.put("meeting_operate_type", "2");
        map.put("meeting_type", "0");
        map.put("meeting_theme", "融合api软终端会议测试");
        map.put("meeting_content", "content");
        map.put("meeting_ifmute", "false");
        map.put("meeting_starttime", "2021-2-8 00:00:00");
        map.put("meeting_length", "3600");
        map.put("meeting_mode", "HD");
    }
}

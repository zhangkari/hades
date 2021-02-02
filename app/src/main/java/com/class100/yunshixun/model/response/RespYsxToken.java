package com.class100.yunshixun.model.response;

import java.util.List;

public class RespYsxToken {
    public int code; // 200 成功
    public String msg;
    public boolean weakPwd; // 是否弱密码标识 True: 弱密码 False: 非弱密码
    public String token;
    public String createTime; // createTime
    public int expires; // 过期时间，单位为秒，如果为 0 表示过期
    public String enterpriseId;
    public String userId;
    public String username;
    public int dutyLevel;
    public List<String> roles;
    public AuthInfo authorities;

    public static class AuthInfo {
        public boolean canAccessKnowLedge;
        public boolean canAccessPortal;
    }
}

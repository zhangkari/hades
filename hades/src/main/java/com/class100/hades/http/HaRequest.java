package com.class100.hades.http;

import androidx.annotation.IntDef;

import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtSerializers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

public abstract class HaRequest {

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;
    public static final int METHOD_DELETE = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({METHOD_GET, METHOD_POST, METHOD_DELETE})
    @interface RequestMethod {

    }

    String id;
    String group;
    String url;
    int method;
    Map<String, String> headers;
    Map<String, String> parameters;
    Map<String, String> userParams;

    public HaRequest() {
        headers = new HashMap<>(8);
        parameters = new HashMap<>(8);
        userParams = new HashMap<>(8);
    }

    protected String getId() {
        return "";
    }

    protected String getGroup() {
        return "";
    }

    protected abstract String getUrl();

    protected int getMethod() {
        return METHOD_GET;
    }

    protected Map<String, String> buildParameters(final Map<String, String> map) {
        return map;
    }

    protected Map<String, String> getHeaders() {
        return null;
    }

    private Map<String, String> getUserParameters() {
        userParams.clear();
        userParams = buildParameters(userParams);
        return userParams;
    }

    final String emit() {
        id = getId();
        group = getGroup();
        url = getUrl();
        method = getMethod();
        Map<String, String> map = getUserParameters();
        if (!AtCollections.isEmpty(map)) {
            parameters.putAll(map);
        }
        Map<String, String> header = getHeaders();
        if (!AtCollections.isEmpty(header)) {
            headers.putAll(header);
        }
        return AtSerializers.toJson(this);
    }
}

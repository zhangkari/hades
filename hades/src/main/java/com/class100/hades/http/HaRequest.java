package com.class100.hades.http;

import androidx.annotation.IntDef;

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

    String id;      // request identity
    String host;    // host id
    String url;     // api path
    String group;   // distinguish api return structure

    @RequestMethod
    int method;
    Map<String, String> headers;
    Map<String, String> parameters;

    public HaRequest() {
        headers = new HashMap<>(8);
        parameters = new HashMap<>(8);
    }

    protected String getId() {
        return "";
    }

    protected String getGroup() {
        return "";
    }

    protected String getHost() {
        return "";
    }

    protected abstract String getUrl();

    protected @RequestMethod
    int getMethod() {
        return METHOD_GET;
    }

    protected Map<String, String> buildParameters(final Map<String, String> map) {
        return map;
    }

    protected Map<String, String> buildHeaders(final Map<String, String> map) {
        return map;
    }

    final HaRequest inflate() {
        id = getId();
        host = getHost();
        group = getGroup();
        url = getUrl();
        method = getMethod();
        parameters = buildParameters(parameters);
        headers = buildHeaders(headers);
        return this;
    }

    final String emit() {
        return AtSerializers.toJson(inflate());
    }
}

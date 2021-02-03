package com.class100.hades.http;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

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

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
        HadesManifest.host_mainApp,
        HadesManifest.host_ipower_api,
        HadesManifest.host_ipower_token
    })
    @interface RequestHost {

    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({HadesManifest.group_mainApp})
    @interface RequestGroup {

    }

    String id;      // request identity

    @RequestHost
    String host;    // host id

    String url;     // api path

    @RequestGroup
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

    protected @RequestGroup
    String getGroup() {
        return "";
    }

    protected @RequestHost
    String getHost() {
        return "";
    }

    protected abstract String getUrl();

    protected @RequestMethod
    int getMethod() {
        return METHOD_GET;
    }

    protected void buildParameters(final Map<String, String> map) {
    }

    protected void buildHeaders(final Map<String, String> map) {
    }

    private void assembleParameters(Map<String, String> map) {
        buildParameters(map);
    }

    private void assembleHeaders(Map<String, String> map) {
        buildHeaders(map);
    }

    final HaRequest inflate() {
        id = getId();
        host = getHost();
        group = getGroup();
        url = getUrl();
        method = getMethod();
        assembleHeaders(headers);
        assembleParameters(parameters);
        return this;
    }

    final String emit() {
        return AtSerializers.toJson(inflate());
    }
}

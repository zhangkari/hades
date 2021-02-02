package com.class100.hades.http;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtLog;
import com.class100.atropos.generic.AtSerializers;
import com.class100.atropos.generic.AtTexts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HaRequestDispatcher {
    private static final String TAG = HaRequestDispatcher.class.getSimpleName();

    public static final int ENV_PROD = 0;
    public static final int ENV_QA = 1;
    public static final int ENV_DEV = 2;

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @IntDef({ENV_PROD, ENV_QA, ENV_DEV})
    @interface HostEnv {

    }

    /**
     * 0 product env
     * 1 qa env
     * 2 dev env
     */
    private static @HostEnv
    int env = 0;

    /**
     * Use to count request, seqId will add 10 when a new request emit
     */
    private AtomicLong seqId;

    private final OkHttpClient client;

    public static void switchEnv(@HostEnv int env) {
        if (env < 0 || env > 2) {
            HaRequestDispatcher.env = 0;
        } else {
            HaRequestDispatcher.env = env;
        }
    }

    public HaRequestDispatcher(OkHttpClient client) {
        this.client = client;
        seqId = new AtomicLong(0);
    }

    public <T> void dispatch(String command, @NonNull HaApiCallback<T> callback) {
        HaRequest request = AtSerializers.fromJson(command, HaRequest.class);
        dispatch(request, callback);
    }

    public <T> void dispatch(final HaRequest request, @NonNull final HaApiCallback<T> callback) {
        seqId.addAndGet(10);
        if (AtTexts.isEmpty(request.id)) {
            request.id = String.valueOf(seqId.get());
        }
        AtLog.d(TAG, "dispatch", "seqId = " + seqId.get());

        client.newCall(adaptRequest(request)).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onError(404, e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                invokeCallback(request.group, response, callback);
            }
        });
    }

    private <T> void invokeCallback(String tag, @NonNull Response response, @NonNull HaApiCallback<T> callback) {
        if (response.body() == null) {
            callback.onError(500, "response body is null");
            return;
        }
        try {
            String text = response.body().string();
            if (AtTexts.isEmpty(tag)) {
                invokeRawCallback(text, callback);
            } else {
                invokeUniversalCallback(text, callback);
            }
        } catch (Exception e) {
            callback.onError(500, e.getMessage());
        }
    }

    // todo
    // 1. Add response mapping
    // 2. Use AtSerializer instead
    private <T> void invokeUniversalCallback(@NonNull String response, @NonNull HaApiCallback<T> callback) {
        HaApiResponse<T> resp = new Gson().fromJson(response, new TypeToken<HaApiResponse<T>>() {
        }.getType());
        if (resp.code != 0) {
            callback.onError(resp.code, resp.message);
        } else {
            callback.onSuccess(resp.content);
        }
    }

    // TODO use AtSerializer instead
    private <T> void invokeRawCallback(@NonNull String response, @NonNull HaApiCallback<T> callback) {
        ParameterizedType type = (ParameterizedType) callback.getClass().getGenericInterfaces()[0];
        T resp = new Gson().fromJson(response, type.getActualTypeArguments()[0]);
        callback.onSuccess(resp);
    }

    Request adaptRequest(HaRequest req) {
        req.inflate();
        Request.Builder builder = new Request.Builder();
        if (!AtCollections.isEmpty(req.headers)) {
            for (Map.Entry<String, String> e : req.headers.entrySet()) {
                builder.addHeader(e.getKey(), e.getValue());
            }
        }
        final String url = buildCompleteUrl(req.url, req.host);
        switch (req.getMethod()) {
            case HaRequest.METHOD_GET:
                builder.url(buildGetParameters(url, req.parameters));
                builder.get();
                break;

            case HaRequest.METHOD_POST:
                builder.url(url);
                builder.post(RequestBody.create(adaptRequestBody(req.parameters), buildRequestMediaType()));
                break;

            case HaRequest.METHOD_DELETE:
                builder.delete(RequestBody.create(adaptRequestBody(req.parameters), buildRequestMediaType()));
                break;
        }
        return builder.build();
    }

    String buildGetParameters(String url, Map<String, String> params) {
        if (AtCollections.isEmpty(params)) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey())
                .append("=")
                .append(entry.getValue())
                .append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    String buildCompleteUrl(String url, String hostKey) {
        String[] hosts = HadesManifest.HostTable.get(hostKey);
        if (AtCollections.isEmpty(hosts)) {
            throw new RuntimeException("Please define " + hostKey + " => host");
        }
        if (env < 0 || env >= hosts.length) {
            env = 0;
        }
        String host = hosts[env];
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return host + "/" + url;
    }

    MediaType buildRequestMediaType() {
        return MediaType.parse("application/json");
    }

    String adaptRequestBody(Map<String, String> params) {
        return AtSerializers.toJson(params);
    }
}

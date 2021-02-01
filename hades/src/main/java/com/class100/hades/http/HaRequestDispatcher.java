package com.class100.hades.http;

import androidx.annotation.NonNull;

import com.class100.atropos.generic.AtCollections;
import com.class100.atropos.generic.AtLog;
import com.class100.atropos.generic.AtSerializers;
import com.class100.atropos.generic.AtTexts;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
    
    /**
     * 0 product env
     * 1 qa env
     * 2 dev env
     */
    private static int env = 0;

    /**
     * Use to count request, seqId will add 10 when a new request emit
     */
    private AtomicLong seqId;

    private final OkHttpClient client;

    public static void switchEnv(int env) {
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

    public <T> void dispatch(HaRequest request, @NonNull final HaApiCallback<T> callback) {
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
                invokeCallback(response, callback);
            }
        });
    }

    private <T> void invokeCallback(@NonNull Response response, @NonNull HaApiCallback<T> callback) {
        if (response.body() == null) {
            callback.onError(500, "response body is null");
            return;
        }
        try {
            String text = response.body().string();
            HaApiResponse<T> resp = AtSerializers.fromJson(text, HaApiResponse.class);
            if (resp.code != 0) {
                callback.onError(resp.code, resp.message);
            } else {
                callback.onSuccess(resp.content);
            }
            callback.onSuccess(resp.content);
        } catch (Exception e) {
            callback.onError(500, e.getMessage());
        }
    }

    Request adaptRequest(HaRequest req) {
        Request.Builder builder = new Request.Builder();
        if (!AtCollections.isEmpty(req.headers)) {
            for (Map.Entry<String, String> e : req.headers.entrySet()) {
                builder.addHeader(e.getKey(), e.getValue());
            }
        }
        builder.url(buildCompleteUrl(req.url, req.group));
        switch (req.getMethod()) {
            case HaRequest.METHOD_GET:
                builder.get();
                break;

            case HaRequest.METHOD_POST:
                builder.post(RequestBody.create(adaptRequestBody(req.parameters), buildRequestMediaType()));
                break;

            case HaRequest.METHOD_DELETE:
                builder.delete(RequestBody.create(adaptRequestBody(req.parameters), buildRequestMediaType()));
                break;
        }
        return builder.build();
    }

    String buildCompleteUrl(String url, String group) {
        String[] hosts = HadesManifest.HostTable.get(group);
        if (AtCollections.isEmpty(hosts)) {
            throw new RuntimeException("Please define " + group + " => host");
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

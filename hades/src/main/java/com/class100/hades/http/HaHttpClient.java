package com.class100.hades.http;

import com.class100.atropos.env.context.AtContextAbility;
import com.class100.atropos.generic.AtLog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public final class HaHttpClient extends AtContextAbility {
    private static final String TAG = "HaHttpClient";
    private static boolean enableLog;
    private static final HaHttpClient _instance = new HaHttpClient();
    private HaRequestDispatcher dispatcher;

    public static void enableLog(boolean enableLog) {
        HaHttpClient.enableLog = enableLog;
    }

    public static HaHttpClient getInstance() {
        return _instance;
    }

    private HaHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (enableLog) {
            builder.addInterceptor(new LogInterceptor());
        }
        dispatcher = new HaRequestDispatcher(builder.build());
    }

    public <T> void enqueue(String command, HaApiCallback<T> callback) {
        dispatcher.dispatch(command, callback);
    }

    public <T> void enqueue(HaRequest request, HaApiCallback<T> callback) {
        dispatcher.dispatch(request, callback);
    }

    static class Logs {
        static void d(String tag, String message) {
            AtLog.d(TAG, tag, message);
        }
    }

    static class LogInterceptor implements Interceptor {
        private final static String TAG = "HttpApi";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Logs.d(TAG, "url => " + request.url());
            Logs.d(TAG, "method => " + request.method());
            Logs.d(TAG, "request-headers => \n" + request.headers().toString());
            Logs.d(TAG, "request-body => \n" + request.body());

            long startNs = System.nanoTime();
            Response response = chain.proceed(request);
            long elapseMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            Logs.d(TAG, "elapse => " + elapseMs + " ms");

            Headers headers = response.headers();
            Logs.d(TAG, "response-headers => \n" + headers.toString());
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();
                Logs.d(TAG, "response => \n" + buffer.clone().readString(StandardCharsets.UTF_8));
            }
            return response;
        }
    }
}

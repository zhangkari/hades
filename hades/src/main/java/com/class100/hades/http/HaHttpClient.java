package com.class100.hades.http;

import com.class100.atropos.env.context.AtContextAbility;
import com.class100.atropos.generic.AtLog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public final class HaHttpClient extends AtContextAbility {
    private static final String TAG = "HaHttpClient";
    private static boolean enableLog = true;
    private volatile static HaHttpClient _instance;
    private HaRequestDispatcher dispatcher;

    public static void enableLog(boolean enableLog) {
        HaHttpClient.enableLog = enableLog;
    }

    public static HaHttpClient getInstance() {
        if (_instance == null) {
            synchronized (HaHttpClient.class) {
                if (_instance == null) {
                    _instance = new HaHttpClient();
                }
            }
        }
        return _instance;
    }

    private HaHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (enableLog) {
            builder.addInterceptor(new LogInterceptor());
        }
        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());
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
            final String url = request.url().toString();
            Logs.d(TAG, url + " method => " + request.method());
            Logs.d(TAG, url + " request-headers => \n" + request.headers().toString());
            String body = null;
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = StandardCharsets.UTF_8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(StandardCharsets.UTF_8);
                }
                body = buffer.readString(charset);
            }
            Logs.d(TAG, url + " request-body => \n" + body);

            long startNs = System.nanoTime();
            Response response = chain.proceed(request);
            long elapseMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            Logs.d(TAG, url + " elapse => " + elapseMs + " ms");

            Headers headers = response.headers();
            Logs.d(TAG, url + " response-headers => \n" + headers.toString());
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();
                Logs.d(TAG, url + " response => \n" + buffer.clone().readString(StandardCharsets.UTF_8));
            }
            return response;
        }
    }

    public static class SSLSocketClient {
        public static SSLSocketFactory getSSLSocketFactory() {
            try {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, getTrustManager(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static TrustManager[] getTrustManager() {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
            return trustAllCerts;
        }

        public static HostnameVerifier getHostnameVerifier() {
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
            return hostnameVerifier;
        }
    }

}

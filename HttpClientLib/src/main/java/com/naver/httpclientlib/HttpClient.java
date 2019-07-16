package com.naver.httpclientlib;

import com.naver.httpclientlib.converter.Converter;
import com.naver.httpclientlib.converter.GsonConverter;

import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static com.naver.httpclientlib.Utils.checkNotNull;

public final class HttpClient {
    private HttpUrl baseUrl;
    private okhttp3.Call.Factory callFactory;
    private Converter converter;

    public HttpClient(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.callFactory = builder.callFactory;
        this.converter = builder.converter;
    }

    public <T> T create(Class<T> service) {
        if(!service.isInterface()) {
            throw new IllegalArgumentException("declarations must be interface.");
        }

        return (T) Proxy.newProxyInstance(service.getClassLoader()
                , new Class<?>[]{service}
                , new HttpInvocationHandler(this));
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = HttpUrl.get(baseUrl);
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    HttpUrl getBaseUrl() {
        return baseUrl;
    }

    okhttp3.Call.Factory getCallFactory() {
        return callFactory;
    }

    Converter getConverter() {
        return converter;
    }
    /**
     * Builder
     */
    public static final class Builder {
        private HttpUrl baseUrl;
        private okhttp3.Call.Factory callFactory;
        private Converter converter;

        public Builder() {

        }

        public Builder baseUrl(String baseUrl) {
            checkNotNull(baseUrl, "URL is null");
            return baseUrl(HttpUrl.get(baseUrl));
        }

        public Builder baseUrl(URL baseUrl) {
            checkNotNull(baseUrl, "URL is null");
            return baseUrl(HttpUrl.get(baseUrl));
        }

        public Builder baseUrl(URI baseUrl) {
            checkNotNull(baseUrl, "URL is null");
            return baseUrl(HttpUrl.get(baseUrl));
        }

        public Builder baseUrl(HttpUrl baseUrl) {
            checkNotNull(baseUrl, "URL is null");
            this.baseUrl = baseUrl;
            return this;
        }

        public HttpClient build() {
            if (baseUrl == null) {
                throw new IllegalStateException("BaseURL is needed");
            }

            if (callFactory == null) {
                this.callFactory = new OkHttpClient.Builder()
                        .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                        .build();
            }

            return new HttpClient(this);
        }
    }
}

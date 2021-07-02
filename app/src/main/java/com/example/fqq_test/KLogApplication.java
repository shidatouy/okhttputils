package com.example.fqq_test;

import android.app.Application;

import com.socks.library.KLog;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhaokaiqiang on 15/11/14.
 */
public class KLogApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG == true) {
            KLog.init(true);
        } else {
            KLog.init(false);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggerInterceptor("TAG"))
                //配置Https
//                .sslSocketFactory(sslParams.sSLSocketFactory)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("token", "8sxomi3XFZsgXeUntYMLUuw1JNpwKORjZ7/maU9BvTs=")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
}

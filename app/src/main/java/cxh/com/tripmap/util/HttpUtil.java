package cxh.com.tripmap.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhuang_ge on 2017/11/13.
 */

public class HttpUtil {
    private OkHttpClient client;
//    private String token;
//    private SharedPreferences sp;
//    private SharedPreferences.Editor editor;

    private HttpUtil() {
        client = new OkHttpClient();
    }

    private static HttpUtil httpUtil = null;

    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    public void sendRequestWithCallback(final RequestTypeEnum method, final String address,
                                        final RequestBody body, final HttpCallbackListener listener
    ) {

        new Thread(new Runnable() {
            @Override
            public void run() {

            Request.Builder builder = new Request.Builder()
                    .url(address);

            switch (method) {
                case POST:
                    builder.post(body);
                    break;
                case PUT:
                    builder.put(body);
                    break;
                case DELETE:
                    builder.delete(body);
                    break;
                default:
                    builder.get();
                    break;
            }

            Request request = builder.build();
            try {
                //实际进行请求的代码
                Log.i("url = ", address);
                Response response = client.newCall(request).execute();
//                editor.putString("token", token).apply();
                String result = response.body().string();
                if (result != null && listener != null) {
                    //当response的code大于200，小于300时，视作请求成功
                    if (response.isSuccessful()) {
                        listener.onFinish(result);
                    } else {
                        listener.onError(new ServerException(result));
                    }
                }

            } catch (IOException e) {
                if (listener != null) {
                    listener.onError(e);
                }
            }
            }
        }).start();

    }

    public void post(final String address, RequestBody body, final HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.POST, address, body, listener);
    }

    public void get(String address, HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.GET, address, null, listener);
    }

    public void delete(String address, RequestBody body, HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.DELETE, address, body, listener);
    }

    public void put(String address, RequestBody body, HttpCallbackListener listener) {
        sendRequestWithCallback(RequestTypeEnum.PUT, address, body, listener);
    }


}

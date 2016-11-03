package com.taozi.gifts.util;

import android.os.Handler;

import com.taozi.gifts.i.BaseCallBack;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tao Yimin on 16/2/29. 网络请求的三个要素: 1.请求方式(GET,POST) 2.请求的url 3.参数
 */
public class HttpUtil {

    private static Handler mHandler;

    static {
        mHandler = new Handler();
    }

    /**
     * 执行网络请求操作
     *
     * @param method 请求方式(需要传入String类型的参数:"GET","POST")
     * @param url    请求的url
     * @param params 请求的参数
     */
    public static void doHttpReqeust(final String method, final String url,
                                      final HashMap<String, String> params, final BaseCallBack callBack) {
        ThreadTask.getInstance().executorNetThread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL(url);
                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                    // 设置输入可用
                    connection.setDoInput(true);
                    // 设置输出可用
                    connection.setDoOutput(true);
                    // 设置请求方式
                    connection.setRequestMethod(method);
                    // 设置连接超时
                    connection.setConnectTimeout(5000);
                    // 设置读取超时
                    connection.setReadTimeout(5000);
                    // 设置缓存不可用
                    connection.setUseCaches(false);
                    // 开始连接
                    connection.connect();
                    // 只有当POST请求时才会执行此代码段
                    if (params != null) {
                        // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
                        OutputStream outputStream = connection
                                .getOutputStream();
                        StringBuilder sb = new StringBuilder();
                        Set<Map.Entry<String, String>> sets = params.entrySet();
                        // 将Hashmap转换为string
                        for (Map.Entry<String, String> entry : sets) {
                            sb.append(entry.getKey()).append("=")
                                    .append(entry.getValue()).append("&");
                        }
                        String param = sb.substring(0, sb.length() - 1);
                        // 使用输出流将string类型的参数写到服务器
                        outputStream.write(param.getBytes());
                        outputStream.flush();
                    }
                    // 当返回码为200时才读取数据
                    if (connection.getResponseCode() == 200) {
                        // 通过网络连接的输入流来读取返回数据
                        final String result = new String(readData(connection
                                .getInputStream()), Charset.defaultCharset());
                        // Log.i("info",result);
                        if (result != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.success(result);
                                }
                            });
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postFailed(callBack);
            }
        }, ThreadTask.ThreadPeriod.PERIOD_HIGHT);
    }

    /**
     * 通过xUtils进行网络请求
     *
     * @param method 请求方式(需要传入String类型的参数:"GET","POST")
     * @param url    请求的url
     * @param params 请求的参数
     */
    public static void doHttpReqeustByxUtils(final String method, final String url,
                                     final HashMap<String, String> params, final BaseCallBack callBack) {
        HttpMethod m = HttpMethod.GET;
        final RequestParams requestParams = new RequestParams(url);
        if ("POST".equals(method)) {
            m = HttpMethod.POST;
            Set<Map.Entry<String, String>> sets = params.entrySet();
            // 将Hashmap转换为string
            for (Map.Entry<String, String> entry : sets) {
                requestParams.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().request(m, requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (callBack != null) {
                    callBack.success(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.failed(0, "请检查网络连接");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private static void postFailed(final BaseCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.failed(0, "请检查网络连接");
            }
        });
    }

    /**
     * 通过网络连接的输入流来读取返回数据
     *
     * @param inputStream 输入流
     * @return
     */
    public static byte[] readData(InputStream inputStream) {
        byte[] result = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 缓冲区
        byte[] bytes = new byte[1024];
        int len = -1;
        try {
            // 使用字节数据输出流来保存数据
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            result = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static void requestBySendJson(final String method, final String url,
                                         final String json, final BaseCallBack callBack) {
        ThreadTask.getInstance().executorNetThread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL(url);
                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) u.openConnection();
                    // 设置输入可用
                    connection.setDoInput(true);
                    // 设置输出可用
                    connection.setDoOutput(true);
                    //将请求的类型设置为json
                    connection.setRequestProperty("Content-Type",
                            "application/json");
                    // 设置请求方式
                    connection.setRequestMethod(method);
                    // 设置连接超时
                    connection.setConnectTimeout(5000);
                    // 设置读取超时
                    connection.setReadTimeout(5000);
                    // 设置缓存不可用
                    connection.setUseCaches(false);
                    // 开始连接
                    connection.connect();
                    // 只有当POST请求时才会执行此代码段
                    if (json != null) {
                        // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
                        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                        writer.write(json);
                        writer.flush();
                    }
                    // 当返回码为200时才读取数据
                    if (connection.getResponseCode() == 200) {
                        // 通过网络连接的输入流来读取返回数据
                        final String result = new String(readData(connection
                                .getInputStream()), Charset.defaultCharset());
                        // Log.i("info",result);
                        if (result != null) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.success(result);
                                }
                            });
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postFailed(callBack);
            }
        }, ThreadTask.ThreadPeriod.PERIOD_HIGHT);
    }

}

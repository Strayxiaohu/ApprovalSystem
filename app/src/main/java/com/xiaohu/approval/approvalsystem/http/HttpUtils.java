package com.xiaohu.approval.approvalsystem.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.HttpCookie;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class HttpUtils {
    public void HttpGet(final Context context, final Handler myHandler, final String url, final boolean isTrue) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //用HttpClicent发送请求，分为五步
                //第一步：创建httpclient对象
                HttpClient httpClient = new DefaultHttpClient();
                //第二步：创建代表请求的对象，参数是访问的服务器的地址
                HttpGet httpGet = new HttpGet(url);
                SharedPreferences sharedPreferences = context.getSharedPreferences("APPROVAL", Context.MODE_PRIVATE);
                String sessionid = sharedPreferences.getString("ASP.SessionId", "");
                if (!sessionid.equals("")) {
                    httpGet.setHeader("Cookie", "ASP.NET_SessionId=" + sessionid);
                }

                try {


                    //第三步：执行请求，获取服务器发还的响应的对象
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //5.从相应对象当中取出数据，放到entiry当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        //session
                        if (isTrue) {
                            CookieStore mCookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
                            List<Cookie> cookies = mCookieStore.getCookies();
                            for (int i = 0; i < cookies.size(); i++) {
                                if (cookies.get(i).getName().equals("ASP.NET_SessionId")) {
                                    System.out.println("sss---写入session");
                                    String sid = cookies.get(i).getValue();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("ASP.SessionId", sid);
                                    editor.commit();

                                    break;
                                }
                            }
                        }


                        //将entity当中的数据转换为字符串
                        //在子线程中将Message对象发出去
                        Message message = new Message();
                        message.what = 0;
                        message.obj = response.toString();
                        myHandler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = "";
                        myHandler.sendMessage(message);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }).start();
    }

}

package com.lifegofood.jt3282.lifego.food.login;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jt3282 on 2017/11/29.
 */

public class PhpLogin {
    public static String sendPostDataToInternet(String str1, String str2)
    {
        /* 建立HTTP Post連線 */
        HttpPost httpRequest = new HttpPost("http://140.121.199.147/httpSignInTest.php");
        /*
         * Post運作傳送變數必須用NameValuePair[]陣列儲存
         */
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account", str1));
        params.add(new BasicNameValuePair("password", str2));

        try {

            /* 發出HTTP request */

            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            /* 取得HTTP response */
            HttpResponse httpResponse = new DefaultHttpClient()

                    .execute(httpRequest);
//deny why?

            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                /* 取出回應字串 */

                String strResult = EntityUtils.toString(httpResponse

                        .getEntity());

                // 回傳回應字串

                return strResult;

            }

        } catch (ClientProtocolException e)
        {
            Log.i("log_tag" ,e.getMessage().toString());
            e.printStackTrace();

        } catch (IOException e){
            Log.i("log_tag" ,e.getMessage().toString());
            e.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

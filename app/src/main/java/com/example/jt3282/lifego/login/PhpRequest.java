package com.example.jt3282.lifego.login;

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
 * Created by jt3282 on 2017/11/28.
 */

public class PhpRequest {
    public static String sendPostDataToInternet(String str1, String str2, String str3)
    {
        /* 建立HTTP Post連線 */
        HttpPost httpRequest = new HttpPost("https://lifego777.000webhostapp.com/httpPostTest.php");
        /*
         * Post運作傳送變數必須用NameValuePair[]陣列儲存
         */

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("account", str1));
        params.add(new BasicNameValuePair("password", str2));
        params.add(new BasicNameValuePair("user_name", str3));

        try {

            /* 發出HTTP request */

            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            /* 取得HTTP response */

            HttpResponse httpResponse = new DefaultHttpClient()

                    .execute(httpRequest);

            /* 若狀態碼為200 ok */

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
            e.printStackTrace();

        } catch (IOException e){
            e.printStackTrace();

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

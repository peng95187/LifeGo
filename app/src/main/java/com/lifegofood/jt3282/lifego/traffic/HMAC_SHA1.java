package com.lifegofood.jt3282.lifego.traffic;

import android.util.Base64;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jt3282 on 2018/2/25.
 */

public class HMAC_SHA1 {

    public static String Signature(String xData, String AppKey) throws SignatureException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(AppKey.getBytes("UTF-8"),"HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(xData.getBytes("UTF-8"));
            String result = Base64.encodeToString(rawHmac, Base64.DEFAULT); //這行要改成這樣，不然就只能在 Andorid 8.0 才能使用
            result=result.replace("\n", ""); // 要加這一行，不然會認證失敗
            return result;

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : "+ e.getMessage());
        }
    }

}

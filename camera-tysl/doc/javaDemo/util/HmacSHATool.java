package com.cn21.open.javaDemo.util;

/**
 * @author cbx
 * @date 2020/7/3
 **/

import com.cn21.open.javaDemo.common.Constant;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class HmacSHATool {

    public static String encodeHmacSHA256(String plantText, String appSecret) {
        try {
            byte[] data = ByteFormat.hexToBytes(ByteFormat.toHex((plantText).getBytes()));
            byte[] key = ByteFormat.hexToBytes(ByteFormat.toHex(appSecret.getBytes()));

            SecretKey secretKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte digest[] = mac.doFinal(data);
            return (new HexBinaryAdapter()).marshal(digest);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        String testStr = "helloworld";
        String result = encodeHmacSHA256(testStr, "test");
        System.out.println(result);
    }

}

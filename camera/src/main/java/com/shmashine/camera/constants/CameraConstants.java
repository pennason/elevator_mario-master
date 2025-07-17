package com.shmashine.camera.constants;

/**
 * 业务默认常量类
 *
 * @author Dean Winchester
 */
public class CameraConstants {

    /**
     * 海康萤石云
     */
    public enum CameraType {
        HAIKANG(1, "海康"),
        XIONGMAI(2, "雄迈");
        private int type;
        private String name;

        CameraType(int type, String name) {
            this.name = name;
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 海康萤石云
     */
    public static class Ezopen {
        /**
         * 获取appToken URL
         */
        public static final String APP_TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";

        /**
         * appKey
         */
        public static final String APP_KEY = "8374cfb69acd473d8b4a65c8837c364a";
        /**
         * appSecret
         */
        public static final String APP_SECRET = "25f086df116c78899863c7fc0b8e24ae";

        /**
         * 保存视频URL
         */
        public static final String SAVE_VIDEO_URL = "http://172.31.183.101:9989/video";

        /**
         * 截取图片URL
         */
        public static final String SAVE_IMAGE_URL = "http://172.31.183.101:9989/image";
    }


    /**
     * 雄迈摄像头
     */
    public static class XMNet {

        /**
         * 保存视频URL
         */
        public static final String SAVE_VIDEO_URL = "http://172.31.183.101:9988/video";

        /**
         * 截取图片URL
         */
        public static final String SAVE_IMAGE_URL = "http://172.31.183.101:9988/image";

        //API
        public static String uuid = "8b49b69b6655416b85f0a10bc43710d5";
        public static String appKey = "64f7972949f6405b8ecbcfa326df1839";
        public static String appSecret = "3ffbbbd38a914d6da177e1923635e36b";
        public static int movedCard = 3;


        //test
        /*public static String uuid = "8b49b69b6655416b85f0a10bc43710d5";
        public static String appKey = "823c691c7c50469eb47a61b92114b8b0";
        public static String appSecret = "332b3672725a4c4090bdb14082475a76";
        public static int movedCard = 6;*/


        // [WEB]WEB端摄像头（A）
        /*public static String uuid = "8b49b69b6655416b85f0a10bc43710d5";
        public static String appKey = "64f7972949f6405b8ecbcfa326df1839";
        public static String appSecret = "3ffbbbd38a914d6da177e1923635e36b";
        public static int movedCard = 3;*/

        /**
         * 获取hls流地址
         */
        public static String hlsUrlForHttps = "https://misc.xmeye.net/api/getPlayUrl";

        public static String hlsUrlForhttp = "http://misc.xmeye.net/api/getPlayUrl";


    }

}

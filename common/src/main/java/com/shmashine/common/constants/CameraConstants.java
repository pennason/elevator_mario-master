package com.shmashine.common.constants;

/**
 * 业务默认常量类
 */
public class CameraConstants {

    /**
     * 海康萤石云
     */
    public enum CameraType {
        HAIKANG_YS(1, "海康萤石"),
        XIONGMAI(2, "雄迈"),
        HAIER(3, "海尔"),
        HAIKANG_YM(4, "海康云眸"),
        TYYY(5, "天翼云眼"),
        TYBD(6, "中兴");
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
         * 直播流url
         */
        public static final String VEDIO_URL = "https://open.ys7.com/api/lapp/v2/live/address/get";

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


        public static String uuid = "8b49b69b6655416b85f0a10bc43710d5";
        public static String appKey = "64f7972949f6405b8ecbcfa326df1839";
        public static String appSecret = "3ffbbbd38a914d6da177e1923635e36b";
        public static int movedCard = 3;

        /**
         * 获取hls流地址
         */
        public static String hlsUrl = "http://misc.xmeye.net/api/getPlayUrl";

        /**
         * 获取hls流地址 https
         */
        public static String hlsUrl_https = "https://misc.xmeye.net/api/getPlayUrl";

        /**
         * 获取录像
         */
        public static String cssVideoUrl = "https://misc.xmeye.net/api/getCssVideo";

        /**
         * 指定时间段内的第一个短视频
         */
        public static String cssVideoCCUrl = "https://misc.xmeye.net/api/getCssVideoCC";

        /**
         * 获取录像是否开通
         */
        public static String reqCapsUrl = "https://misc.xmeye.net/api/reqCaps";

    }

}

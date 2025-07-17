package com.shmashine.cameratysl.gateway.tysl.common;

/**
 * @author lyf15
 * @date 2023/7/12
 * 公共参数-通用常量
 * 请将此次常量修改成合作方自身的相关参数，否则，网关校验，加解密测试等不通过
 **/
public class Constant {
    /**
     * 合作方申请的appId
     */
    public static final String APP_ID = "1691339368373030912";
    /**
     * 合作方申请的appSecret
     */
    public static final String APP_SECRET = "gKS2cxjz9B";
    /**
     * 企业主账户对应手机号,只有平台应用需要填写修改，标准应用不需要用到此参数
     */
    public static final String ENTERPRISE_USER = "13810281625";

    /**
     * 服务端版本号，如v1.0，若公共参数version填写此值，返回敏感参数使用XXTea加密
     */
    public static final String VERSION = "v1.0";
    /**
     * 服务端版本号，如1.1，若公共参数version填写此值，返回敏感参数使用RSA加密（不加v前缀）
     */
    public static final String VERSION2 = "1.1";

    /**
     * 接入端类型 , 可选值：
     * 0-IOS
     * 1-Android
     * 2-Web/WAP/H5
     * 3-PC
     * 4-服务端
     */
    public static final Integer CLINT_TYPE = 4;

    /** 合作方自己生成的RSA加密秘钥中，用于本地解密的 本地私钥,这里不是真实的，请自己生成后填写这里。*/

    /**
     * 公钥
     */
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDc5xR8eZacivaD6TUAv2KAEqd92mQ/xrnSFFUyek1RM8A0I2oA4oCxY2CNjAOY1i0KyJKqNQqw0MAHXRybA4UVz5flNrvtJ1QDFSldra6V+oKIM+O2mEv+ielrNFSf82NbAzpsZ0VRdZCy3iD1kjqRtVp1skVq1/ayiSrsWEElgQIDAQAB";

    /**
     * 私钥
     */
    public static final String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANznFHx5lpyK9oPpNQC/YoASp33aZD/GudIUVTJ6TVEzwDQjagDigLFjYI2MA5jWLQrIkqo1CrDQwAddHJsDhRXPl+U2u+0nVAMVKV2trpX6gogz47aYS/6J6Ws0VJ/zY1sDOmxnRVF1kLLeIPWSOpG1WnWyRWrX9rKJKuxYQSWBAgMBAAECgYANE5LHYY00XBeGrH3qOuoSSchnb0+i5NkoIfL/J3U/OtCEZZpHTiEvHwJerqyh1f/ZlRD1iWlGGT1c0jq4xGcSzMxg4tIZB/KHSC8oYM0CqanmURRC31QKJFjKPfxDV7BbZly7ddWQ5RVW17PIhIE0u1En/Q5fXMG1YNg4eyuXHwJBAOpz2gGR1MCr5F5su/wqiNIt1H7y9zLXg6kI4mEo8p4Nn1dUfp0w1gTCdZhhK3KcRr0yd35D5/3NlRMAIMlyTD8CQQDxNG36eCLCFDnakn6GTRX9CefwBwWRimxFBKgvF2WsdW81xWiWpAYSQzUvqv0laRdMoMvwefo3O1oRZar3jx4/AkAQAkchkk6ftV0LoYsYheL/zwdSf86nVxXzZdrnXX9rkWyO7wMSQrJqcsjK1d+8nLUr2BVWttZRe0cxm/GhL7/LAkBaeATMDiVTUAzHC21DaERYDVAeOjP/e8DSPkXqq8bp4X4d1/Y7kjfov6aCQ4sBbiJcDm+PueB8uNk2b0Cp69c3AkAxMRzO54uvqq28+RjCy7V+JBlCHAbGlofcIUsUiXrwv+bJPb+hHyNk8/03K6mc5tsFyUA29Zp80cJs8bQaVVQi";
    /**
     * 能力开放正式环境域名
     */
    public static final String PRO_DOMAIN = "https://vcp.21cn.com";


    /**
     * 语音对讲 一对一
     */
    public static final String VOICE_URI_TALKBACK_TOKEN_POST = "/open/u/vpaas/voice/talkback";
    /**
     * 语音对讲 获取Token
     */
    public static final String VOICE_URL_GET_TOKEN_POST = "/open/oauth/getAccessToken";
}

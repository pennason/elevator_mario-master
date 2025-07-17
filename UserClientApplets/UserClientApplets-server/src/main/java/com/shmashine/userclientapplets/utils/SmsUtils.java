package com.shmashine.userclientapplets.utils;

import static com.shmashine.userclientapplets.constants.SmsConstants.OK;
import static com.shmashine.userclientapplets.constants.SmsConstants.SMS_PARAM_KEY_PHONE;
import static com.shmashine.userclientapplets.constants.SmsConstants.SMS_PARAM_KEY_SIGN_NAME;
import static com.shmashine.userclientapplets.constants.SmsConstants.SMS_PARAM_KEY_TEMPLATE_CODE;
import static com.shmashine.userclientapplets.constants.SmsConstants.SMS_PARAM_KEY_TEMPLATE_PARAM;
import static com.shmashine.userclientapplets.constants.SmsConstants.SMS_RESPONSE_KEY_CODE;
import static com.shmashine.userclientapplets.constants.SmsConstants.SMS_RESPONSE_KEY_MESSAGE;
import static com.shmashine.userclientapplets.constants.SmsConstants.VERIFY_CODE_PARAM_TEMPLATE;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.shmashine.userclientapplets.config.SmsProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信工具
 *
 * @author jiangheng
 */
@Slf4j
@Component
public class SmsUtils {

    private final IAcsClient client;

    private final SmsProperties prop;

    public SmsUtils(IAcsClient client, SmsProperties prop) {
        this.client = client;
        this.prop = prop;
    }

    /**
     * 发送短信验证码的方法
     *
     * @param phone 手机号
     * @param code  验证码
     */
    public void sendVerifyCode(String phone, String code) {
        // 参数
        String param = String.format(VERIFY_CODE_PARAM_TEMPLATE, code);
        // 发送短信
        sendMessage(phone, prop.getSignName(), prop.getVerifyCodeTemplate(), param);
    }

    /**
     * 通用的发送短信的方法
     *
     * @param phone    手机号
     * @param signName 签名
     * @param template 模板
     * @param param    模板参数，json风格
     */
    public void sendMessage(String phone, String signName, String template, String param) {

        CommonRequest request = new CommonRequest();
        request.setSysProtocol(ProtocolType.HTTPS);
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(prop.getDomain());
        request.setSysVersion(prop.getVersion());
        request.setSysAction(prop.getAction());
        request.putQueryParameter(SMS_PARAM_KEY_PHONE, phone);
        request.putQueryParameter(SMS_PARAM_KEY_SIGN_NAME, signName);
        request.putQueryParameter(SMS_PARAM_KEY_TEMPLATE_CODE, template);
        request.putQueryParameter(SMS_PARAM_KEY_TEMPLATE_PARAM, param);

        try {
            CommonResponse response = client.getCommonResponse(request);
            if (response.getHttpStatus() >= 300) {
                log.error("【SMS服务】发送短信失败。响应信息：{}", response.getData());
            }
            // 获取响应体
            Map<String, String> resp = JsonUtils.toMap(response.getData(), String.class, String.class);
            // 判断是否是成功
            if (!StringUtils.equals(OK, resp.get(SMS_RESPONSE_KEY_CODE))) {
                // 不成功，
                log.error("【SMS服务】发送短信失败，原因{}", resp.get(SMS_RESPONSE_KEY_MESSAGE));
            }
            log.info("【SMS服务】发送短信成功，手机号：{}, 响应：{}", phone, response.getData());
        } catch (ServerException e) {
            log.error("【SMS服务】发送短信失败，服务端异常。", e);
        } catch (ClientException e) {
            log.error("【SMS服务】发送短信失败，客户端异常。", e);
        }
    }

}

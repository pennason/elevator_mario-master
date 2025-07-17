package com.shmashine.camera.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.mts.model.v20140618.SubmitJobsRequest;
import com.aliyuncs.mts.model.v20140618.SubmitJobsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.shmashine.common.constants.ServiceConstants;

/**
 * 阿里云视频格式转换
 *
 * @author little.li
 */
public class AliTranscode {


    /**
     * 阿里云视频转码
     *
     * @param ossH264Url h264视频
     * @param ossMp4Url  转码后的MP4视频
     */
    public static void ossH264ToMp4(String ossH264Url, String ossMp4Url) {
        // 创建DefaultAcsClient实例并初始化
        DefaultProfile profile = DefaultProfile.getProfile(
                ServiceConstants.MPS_REGION_ID,
                ServiceConstants.ACCESS_KEY_ID,
                ServiceConstants.ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        // 创建API请求并设置参数
        SubmitJobsRequest request = new SubmitJobsRequest();
        // Input
        JSONObject input = new JSONObject();
        input.put("Location", ServiceConstants.OSS_LOCATION);
        input.put("Bucket", ServiceConstants.OSS_BUCKET);
        input.put("Object", URLEncoder.encode(ossH264Url, StandardCharsets.UTF_8));
        request.setInput(input.toJSONString());
        // Output
        String outputOSSObject;
        outputOSSObject = URLEncoder.encode(ossMp4Url, StandardCharsets.UTF_8);
        JSONObject output = new JSONObject();
        output.put("OutputObject", outputOSSObject);
        // Output->Container
        JSONObject container = new JSONObject();
        container.put("Format", "mp4");
        output.put("Container", container.toJSONString());
        // Output->Video
        JSONObject video = new JSONObject();
        video.put("Codec", "H.264");
        video.put("Bitrate", "1500");
        video.put("Width", "1280");
        video.put("Fps", "25");
        output.put("Video", video.toJSONString());
        // Output->Audio
        JSONObject audio = new JSONObject();
        audio.put("Codec", "AAC");
        audio.put("Bitrate", "128");
        audio.put("Channels", "2");
        audio.put("Samplerate", "44100");
        output.put("Audio", audio.toJSONString());
        // Output->TemplateId
        output.put("TemplateId", ServiceConstants.TEMPLATE_ID);
        JSONArray outputs = new JSONArray();
        outputs.add(output);


        request.setOutputs(outputs.toString());
        request.setOutputBucket(ServiceConstants.OSS_BUCKET);
        request.setOutputLocation(ServiceConstants.OSS_LOCATION);
        // PipelineId
        request.setPipelineId(ServiceConstants.PIP_LINE_ID);
        // 发起请求并处理应答或异常
        SubmitJobsResponse response;
        try {
            response = client.getAcsResponse(request);
            System.out.println("RequestId is:" + response.getRequestId());
            if (response.getJobResultList().get(0).getSuccess()) {
                System.out.println("JobId is:" + response.getJobResultList().get(0).getJob().getJobId());
            } else {
                System.out.println("SubmitJobs Failed code:" + response.getJobResultList().get(0).getCode() +
                        " message:" + response.getJobResultList().get(0).getMessage());
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
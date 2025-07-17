package com.shmashine.camera.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shmashine.common.entity.TblResponseXmReport;
import com.shmashine.common.model.Result;

/**
 * @author jiangheng
 * @date 2020/12/22 —— 14:55
 */
//@FeignClient(value = "shmashine-camera",fallback = CameraClientFallback.class, configuration = FeignConfig.class)
@FeignClient(url = "${endpoint.shmashine-camera:shmashine-camera:8080}", value = "shmashine-camera")
public interface CameraClient {

    /**
     * 添加文件上传记录
     *
     * @param tblResponseXmReport
     * @return
     */
    @PostMapping("/cameras/vedios/insertResponeXmReport")
    Result insertResponeXmReport(@RequestBody TblResponseXmReport tblResponseXmReport);
}

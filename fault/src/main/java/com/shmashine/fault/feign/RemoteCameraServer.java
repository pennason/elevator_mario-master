package com.shmashine.fault.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shmashine.common.entity.TblResponseXmReport;
import com.shmashine.common.model.Result;
import com.shmashine.common.model.request.ImageHandleRequest;
import com.shmashine.common.model.request.VideoHandlerRequest;

/**
 * 远程调用摄像头服务
 *
 * @author Dean Winchester
 */
@Component
@FeignClient(value = "SHMASHINE-CAMERA")
public interface RemoteCameraServer {

    /**
     * 录制视频处理
     *
     * @param videoHandlerRequest 请求参数
     */
    @RequestMapping(value = {"/cameras/videoHandlerApplication"}, method = {RequestMethod.POST})
    Object videoHandlerApplication(@RequestBody VideoHandlerRequest videoHandlerRequest);

    /**
     * 截图处理
     *
     * @param imageHandleRequest 请求参数
     */
    @RequestMapping(value = {"/cameras/imageHandleApplication"}, method = {RequestMethod.POST})
    Object imageHandleApplication(@RequestBody ImageHandleRequest imageHandleRequest);


    /**
     * 添加文件上传记录
     *
     * @param tblResponseXmReport 文件记录
     */
    @RequestMapping(value = {"/cameras/vedios/insertResponeXmReport"}, method = {RequestMethod.POST})
    Result insertResponeXmReport(@RequestBody TblResponseXmReport tblResponseXmReport);

    /**
     * 更新文件上传记录
     *
     * @param tblResponseXmReport 文件记录
     */
    @RequestMapping(value = {"/cameras/vedios/updateResponeXmReport"}, method = {RequestMethod.POST})
    Result updateResponeXmReport(@RequestBody TblResponseXmReport tblResponseXmReport);

}

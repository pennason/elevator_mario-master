package com.shmashine.api.controller.system;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传
 */
@RequestMapping("/systemFileUpload")
@Slf4j
@RestController
public class BizFileController {

    /**
     * 文件上传
     *
     * @param files
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFile")
    public Object uploadImg(@RequestParam("files") MultipartFile[] files) throws IOException {
        //前端没有选择文件，srcFile为空

        if (files == null) {
            log.info("message", "请选择一个文件");
            return "redirect:upload_status";
        }
        for (MultipartFile file : files) {
            // 上传简单文件名
//            OSSUtil.setOSSServer(file.getBytes(), file.getOriginalFilename());
        }

        return null;
    }

}

package com.shmashine.common.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.zip.CRC32;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 视频下载工具文件
 *
 * @author chenxue
 */

@Slf4j
public class FileUtil {

    public static String uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            var targetRes = targetFile.mkdirs();
            if (!targetRes) {
                throw new Exception("创建文件夹失败" + filePath);
            }
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
        return filePath + fileName;
    }

    public static File downloadFile(String url, String fileName, HttpServletResponse response) {
        File scFileDir = new File(url);
        File[] trxFiles = scFileDir.listFiles();
        String path = url + fileName;
        for (int i = 0; i < trxFiles.length; i++) {
            String trxFile = trxFiles[i].getName();
            // 如果文件名不为空，则进行下载
            if (trxFile.equals(fileName)) {
                File file = new File(path);
                // 如果文件名存在，则进行下载
                if (file.exists()) {
                    // 配置文件下载
                    response.setHeader("content-type", "application/octet-stream");
                    response.setContentType("application/octet-stream");
                    // 下载文件能正常显示中文
                    try {
                        response.setHeader("Content-Disposition",
                                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                    } catch (UnsupportedEncodingException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    // 实现文件下载
                    try {
                        FileInputStream fis = new FileInputStream(path);
                        byte[] content = new byte[fis.available()];
                        fis.read(content);
                        fis.close();
                        ServletOutputStream sos = response.getOutputStream();
                        sos.write(content);
                        sos.flush();
                        sos.close();

                        System.out.println("Download the song successfully!");
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println("Download the song failed!");
                    }
                }
            }
        }
        return null;
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    /**
     * 文件转存到本地
     *
     * @param fileUrl  资源路径
     * @param filePath 本地存储路径
     * @param fileName 文件名
     * @return 地址
     * @throws Exception 异常
     */
    public static String downloadFile2LocalByUrl(String fileUrl, String filePath, String fileName) throws Exception {
        byte[] bytesByFile = getBytesByRemotePath(fileUrl);
        uploadFile(bytesByFile, filePath, fileName);
        return filePath + fileName;
    }

    /**
     * MultipartFile转file
     *
     * @param multiFile 文件
     * @return File
     */
    public static File multipartFileToFile(MultipartFile multiFile) throws IOException {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();

        // 获取文件后缀
        assert fileName != null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File file = null;

        try (var ins = multiFile.getInputStream()) {
            file = new File(multiFile.getOriginalFilename());
            inputStreamToFile(ins, file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 输入流转file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try (var os = new FileOutputStream(file)) {
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * File转换为byte[]
     *
     * @return byte[]
     */
    public static byte[] getBytesByFile(String filePath) {
        try {
            File file = new File(filePath);
            //获取输入流
            FileInputStream fis = new FileInputStream(file);

            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件crc
     */
    public static String getCRC32(InputStream inputStream) {
        CRC32 crc32 = new CRC32();
        try {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                crc32.update(buffer, 0, length);
            }
            return String.valueOf(crc32.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * File转换为byte[]
     *
     * @param remoteFilePath 远程地址
     * @return byte[]
     */
    public static byte[] getBytesByRemotePath(String remoteFilePath) {
        byte[] data = null;
        try {
            var urlFile = new URL(remoteFilePath);
            var httpUrl = remoteFilePath.startsWith("https")
                    ? (HttpsURLConnection) urlFile.openConnection()
                    : (HttpURLConnection) urlFile.openConnection();
            httpUrl.connect();

            var inputStream = httpUrl.getInputStream();

            var bis = new BufferedInputStream(inputStream);
            int len = 2048;
            byte[] b = new byte[len];
            var bos = new ByteArrayOutputStream(1024);
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            data = bos.toByteArray();
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (FileNotFoundException e) {
            log.info("getBytesByRemotePath 从 {} 获取流信息失败 FileNotFoundException {}", remoteFilePath,
                    ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            log.info("getBytesByRemotePath 从 {} 获取流信息失败 Exception {}", remoteFilePath,
                    ExceptionUtils.getStackTrace(e));
        }
        return data;
    }

}

/**
 * 示例说明
 *
 * <p>
 * HelloOSS是OSS Java SDK的示例程序，您可以修改endpoint、accessKeyId、accessKeySecret、bucketName后直接运行。
 * 运行方法请参考README。
 * 本示例中的并不包括OSS Java SDK的所有功能，详细功能及使用方法，请参看“SDK手册 > Java-SDK”，
 * 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/preface.html?spm=5176.docoss/sdk/java-sdk/。
 * 调用OSS Java SDK的方法时，抛出异常表示有错误发生；没有抛出异常表示成功执行。
 * 当错误发生时，OSS Java SDK的方法会抛出异常，异常中包括错误码、错误信息，详细请参看“SDK手册 > Java-SDK > 异常处理”，
 * https://help.aliyun.com/document_detail/oss/sdk/java-sdk/exception.html?spm=5176.docoss/api-reference/error-response
 * OSS控制台可以直观的看到您调用OSS Java SDK的结果，OSS控制台地址是：https://oss.console.aliyun.com/index#/。
 * OSS控制台使用方法请参看文档中心的“控制台用户指南”， 指南的来链接地址是：
 * https://help.aliyun.com/document_detail/oss/getting-started/get-started.html?spm=5176.docoss/user_guide。
 * OSS的文档中心地址是：https://help.aliyun.com/document_detail/oss/user_guide/overview.html。
 * OSS Java SDK的文档：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/install.html?spm=5176.docoss/sdk/java-sdk。
 */

package com.shmashine.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 阿里OSS文件上传工具类
 *
 * @author liu.lifu
 */
@Slf4j
public class OSSUtil {

    public static final String OSS_URL = "https://oss-maixin.oss-cn-shanghai.aliyuncs.com/";

    private static final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAIYOtTu4EmnOKn";
    private static final String ACCESS_KEY_SECRET = "1A7iXXtcy5kBXLW2uPAFRIhgIqRGRA";
    private static final String BUCKET_NAME = "oss-maixin";

    private static final String OREO_PROJECT_WORK_ORDER_URL = "Oreo_Project/work_order/";

    public static final String OREO_PROJECT_FAULT_URL = "Oreo_Project/fault/";

    /**
     * 电梯详情图片
     */
    public static final String OREO_PROJECT_ELEVATOR_DETAIL = "oreo-project/elevator-detail/";

    /**
     * 电梯详情图片
     */
    public static final String OREO_PROJECT_SYSTEM_LOG = "oreo-project/elevator-log/";

    /**
     * 电梯详情图片
     */
    public static final String OREO_DEVICE_UPLOAD_FILE = "oreo-project/device-file/";


    /**
     * 现勘详情
     */
    public static final String OREO_SITE_INVESTIGATION_UPLOAD_FILE = "oreo-project/site-investigation/";


    /**
     * 现勘任务相关文件
     */
    public static final String OREO_PM_TASK_FILE = "oreo-project/pm-task/";


    /**
     * 保存工单文件
     *
     * @param files      上传文件数组
     * @param businessId 关联业务id
     * @return 上传是否成功
     */
    public static List<String> saveWorkOrderFile(MultipartFile[] files, String businessId) {

        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        List<String> fileNameList = new ArrayList<>();

        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String fileName = OREO_PROJECT_WORK_ORDER_URL + TimeUtils.nowDate() + "/"
                        + FileUtil.renameToUUID(file.getOriginalFilename());
                byte[] bytes = file.getBytes();
                // 文件存入OSS
                ossClient.putObject(BUCKET_NAME, fileName, new ByteArrayInputStream(bytes));
                fileNameList.add(fileName);
            }
        } catch (Exception e) {
            log.info("OSS文件上传失败 --- businessId: " + businessId);
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return fileNameList;
    }


    /**
     * 保存故障文件
     *
     * @param files      上传文件数组
     * @param businessId 关联业务id
     * @return 上传是否成功
     */
    public static List<String> saveFaultFile(MultipartFile[] files, String businessId) {

        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        List<String> fileNameList = new ArrayList<>();

        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String fileName = OREO_PROJECT_FAULT_URL + TimeUtils.nowDate() + "/"
                        + FileUtil.renameToUUID(file.getOriginalFilename());
                byte[] bytes = file.getBytes();
                // 文件存入OSS
                ossClient.putObject(BUCKET_NAME, fileName, new ByteArrayInputStream(bytes));
                fileNameList.add(fileName);
            }
        } catch (Exception e) {
            log.info("文件上传失败 --- businessId: " + businessId);
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return fileNameList;
    }

    /**
     * 查询 上传文件是否存在
     *
     * @return 是否成功
     */
    public static boolean doesObjectExist(String filename) {
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        return ossClient.doesObjectExist(BUCKET_NAME, filename);

    }


    /**
     * 保存故障文件
     *
     * @param filePath 文件路径
     * @return 上传是否成功
     */
    public static String saveFaultMP4(String filePath) {

        log.info("%s --- %s --- OSS文件上传开始 ---  %s...\n");
        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileUrl;

        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            File file = new File(filePath);
            if (!file.isFile()) {
                log.info("%s --- %s --- OSS文件解析错误 --- filePath:{}   %s...\n" + filePath);
                return "";
            }

            // 获取文件名称及文件字节数据
            var fileName = OREO_PROJECT_FAULT_URL + TimeUtils.nowDate() + "/" + FileUtil.renameToUUID(file.getName());
            // 文件存入OSS
            ossClient.putObject(BUCKET_NAME, fileName, new FileInputStream(file));
            fileUrl = fileName;
            int ossFlag = 3;
            while (!doesObjectExist(fileName)) {
                ossClient.putObject(BUCKET_NAME, fileName, new FileInputStream(file));
                log.info("%s --- %s --- OSS文件上传失败,开始3次重试 --- ossFlag剩余次数: %s...\n" + ossFlag);
                ossFlag--;
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        log.info("%s --- %s --- OSS文件上传成功 fileUrl：{} ---  %s...\n" + fileUrl);
        return fileUrl;
    }


    /**
     * 保存电梯图片
     *
     * @param files      文件
     * @param elevatorId 电梯ID
     * @return 结果
     */
    public static List<Map> saveElevatorImg(MultipartFile[] files, String elevatorId) {
        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        List<Map> fileNameList = new ArrayList<>();
        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String originalFilename = file.getOriginalFilename();
                String fileAbsoluteUrl =
                        OREO_PROJECT_ELEVATOR_DETAIL + elevatorId + "/" + FileUtil.renameToUUID(originalFilename);
                byte[] bytes = file.getBytes();
                // 文件存入OSS
                ossClient.putObject(BUCKET_NAME, fileAbsoluteUrl, new ByteArrayInputStream(bytes));

                HashMap<Object, Object> temp = new HashMap<>();
                temp.put("fileName", originalFilename);
                temp.put("url", fileAbsoluteUrl);
                fileNameList.add(temp);
            }
        } catch (Exception e) {
            log.info("文件上传失败 --- 电梯编号为: " + elevatorId);
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return fileNameList;
    }

    /**
     * 删除电梯图片
     *
     * @param elevatorId 电梯ID
     */
    public static void deleteElevatorImg(String elevatorId) {
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            ossClient.deleteObject(BUCKET_NAME, "AI4.svg"); //  + "/" + OREO_PROJECT_ELEVATOR_DETAIL
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
    }


    /**
     * 保存部门Log图片
     *
     * @param files 文件
     * @param logId ID
     * @return 结果
     */
    public static List<Map> saveDeptLog(MultipartFile[] files, String logId) {
        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        List<Map> fileNameList = new ArrayList<>();
        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String originalFilename = file.getOriginalFilename();
                String fileAbsoluteUrl =
                        OREO_PROJECT_SYSTEM_LOG + logId + "/" + FileUtil.renameToUUID(originalFilename);
                byte[] bytes = file.getBytes();
                // 文件存入OSS
                ossClient.putObject(BUCKET_NAME, fileAbsoluteUrl, new ByteArrayInputStream(bytes));

                HashMap<Object, Object> temp = new HashMap<>();
                temp.put("fileName", originalFilename);
                temp.put("url", fileAbsoluteUrl);
                fileNameList.add(temp);
            }
        } catch (Exception e) {
            log.info("文件上传失败 --- log编号为: " + logId);
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return fileNameList;
    }


    /**
     * 保存现勘上传图片
     *
     * @param files                文件
     * @param vSiteInvestigationId ID
     * @return 结果
     */
    public static List<Map> saveSiteInvestigationImg(MultipartFile[] files, String vSiteInvestigationId) {
        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        List<Map> fileNameList = new ArrayList<>();
        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String originalFilename = file.getOriginalFilename();
                String fileAbsoluteUrl = OREO_SITE_INVESTIGATION_UPLOAD_FILE + vSiteInvestigationId + "/"
                        + FileUtil.renameToUUID(originalFilename);
                byte[] bytes = file.getBytes();
                // 文件存入OSS
                ossClient.putObject(BUCKET_NAME, fileAbsoluteUrl, new ByteArrayInputStream(bytes));

                HashMap<Object, Object> temp = new HashMap<>();
                temp.put("fileName", originalFilename);
                temp.put("url", fileAbsoluteUrl);
                fileNameList.add(temp);
            }
        } catch (Exception e) {
            log.info("图片上传失败 --- 现勘id: " + vSiteInvestigationId);
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return fileNameList;
    }


    /**
     * 保存设备升级文件
     *
     * @param file 文件
     * @return 上传是否成功
     */
    public static String saveDeviceUploadFile(MultipartFile file) {

        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileUrl;

        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            // 获取文件名称及文件字节数据
            String originalFilename = file.getOriginalFilename();
            fileUrl = OREO_DEVICE_UPLOAD_FILE + FileUtil.renameToUUID(originalFilename);
            byte[] bytes = file.getBytes();
            // 文件存入OSS
            ossClient.putObject(BUCKET_NAME, fileUrl, new ByteArrayInputStream(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return OSSUtil.OSS_URL + fileUrl;
    }

    /**
     * 保存任务上传文件
     *
     * @param file MultipartFile文件
     * @return 上传是否成功
     */
    public static String savePmTaskFile(MultipartFile file) {

        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileUrl;

        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            // 获取文件名称及文件字节数据
            String originalFilename = file.getOriginalFilename();
            fileUrl = OREO_PM_TASK_FILE + FileUtil.renameToUUID(originalFilename);
            byte[] bytes = file.getBytes();
            // 文件存入OSS
            ossClient.putObject(BUCKET_NAME, fileUrl, new ByteArrayInputStream(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return OSSUtil.OSS_URL + fileUrl;
    }

    /**
     * 保存任务上传文件
     *
     * @param file File文件
     * @return 上传是否成功
     */
    public static String savePmTaskFile(File file) {

        // 文件上传OSS
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        String fileUrl;

        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            // 获取文件名称及文件字节数据
            String originalFilename = file.getName();

            fileUrl = OREO_PM_TASK_FILE + FileUtil.renameToUUID(originalFilename);

            FileInputStream input = new FileInputStream(file);

            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), input);

            byte[] bytes = multipartFile.getBytes();
            // 文件存入OSS
            ossClient.putObject(BUCKET_NAME, fileUrl, new ByteArrayInputStream(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
        return OSSUtil.OSS_URL + fileUrl;
    }
}

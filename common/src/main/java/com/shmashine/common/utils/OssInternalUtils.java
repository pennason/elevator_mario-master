/**
 * 示例说明
 * <p>
 * HelloOSS是OSS Java SDK的示例程序，您可以修改endpoint、accessKeyId、accessKeySecret、bucketName后直接运行。
 * 运行方法请参考README。
 * <p>
 * 本示例中的并不包括OSS Java SDK的所有功能，详细功能及使用方法，请参看“SDK手册 > Java-SDK”，
 * 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/preface.html?spm=5176.docoss/sdk/java-sdk/。
 * <p>
 * 调用OSS Java SDK的方法时，抛出异常表示有错误发生；没有抛出异常表示成功执行。
 * 当错误发生时，OSS Java SDK的方法会抛出异常，异常中包括错误码、错误信息，详细请参看“SDK手册 > Java-SDK > 异常处理”，
 * 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/exception.html?spm=5176.docoss/api-reference/error-response。
 * <p>
 * OSS控制台可以直观的看到您调用OSS Java SDK的结果，OSS控制台地址是：https://oss.console.aliyun.com/index#/。
 * OSS控制台使用方法请参看文档中心的“控制台用户指南”， 指南的来链接地址是：https://help.aliyun.com/document_detail/oss/getting-started/get-started.html?spm=5176.docoss/user_guide。
 * <p>
 * OSS的文档中心地址是：https://help.aliyun.com/document_detail/oss/user_guide/overview.html。
 * OSS Java SDK的文档地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/install.html?spm=5176.docoss/sdk/java-sdk。
 */

package com.shmashine.common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.ObjectMetadata;

import lombok.extern.slf4j.Slf4j;

/**
 * 阿里OSS文件上传工具类
 *
 * @author chenxue
 */
@Slf4j
public class OssInternalUtils {

    public static final String OSS_URL = "https://oss-mashine.oss-cn-qingdao.aliyuncs.com/";
    public static final String OSS_URL_INTERNAL = "https://oss-mashine.oss-cn-qingdao-internal.aliyuncs.com/";

    public static final String ENDPOINT = "https://oss-cn-qingdao.aliyuncs.com";
    public static final String ENDPOINT_INTERNAL = "https://oss-cn-qingdao-internal.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAIYOtTu4EmnOKn";
    private static final String ACCESS_KEY_SECRET = "1A7iXXtcy5kBXLW2uPAFRIhgIqRGRA";
    private static final String BUCKET_NAME = "oss-mashine";

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
     * 存储字节文件到OSS
     *
     * @param file     文件字节
     * @param fileName 文件名
     */
    public static void setOSS(byte[] file, String fileName) {
        setOSS(file, fileName, false);
    }

    public static void setOSS(byte[] file, String fileName, Boolean isInternal) {
        var ossClient = buildOssClient(isInternal);
        try {
            if (ossClient.doesBucketExist(BUCKET_NAME)) {
                log.info("您已经创建Bucket：" + BUCKET_NAME + "。");
            } else {
                log.info("您的Bucket不存在，创建Bucket：" + BUCKET_NAME + "。");
                ossClient.createBucket(BUCKET_NAME);
            }

            BucketInfo info = ossClient.getBucketInfo(BUCKET_NAME);
            log.info("Bucket " + BUCKET_NAME + "的信息如下：");
            log.info("\t数据中心：" + info.getBucket().getLocation());
            log.info("\t创建时间：" + info.getBucket().getCreationDate());
            log.info("\t用户标志：" + info.getBucket().getOwner());

            InputStream is = new ByteArrayInputStream(file);
            ossClient.putObject(BUCKET_NAME, fileName, is);
            log.info("Object：" + fileName + "存入OSS成功。");
        } catch (Exception ce) {
            ce.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        log.info("Completed");
    }

    /**
     * 存阿里云并设置过期时间
     *
     * @param file       上传文件
     * @param fileName   文件名
     * @param isInternal 是否为内部地址
     * @param expiration 过期时间 分钟
     */
    public static void setOSS(byte[] file, String fileName, Boolean isInternal, int expiration) {
        var ossClient = buildOssClient(isInternal);
        try {
            if (ossClient.doesBucketExist(BUCKET_NAME)) {
                log.info("您已经创建Bucket：" + BUCKET_NAME + "。");
            } else {
                log.info("您的Bucket不存在，创建Bucket：" + BUCKET_NAME + "。");
                ossClient.createBucket(BUCKET_NAME);
            }

            BucketInfo info = ossClient.getBucketInfo(BUCKET_NAME);
            log.info("Bucket " + BUCKET_NAME + "的信息如下：");
            log.info("\t数据中心：" + info.getBucket().getLocation());
            log.info("\t创建时间：" + info.getBucket().getCreationDate());
            log.info("\t用户标志：" + info.getBucket().getOwner());

            InputStream is = new ByteArrayInputStream(file);
            ObjectMetadata metadata = new ObjectMetadata();
            // 设置过期时间
            metadata.setExpirationTime(new Date(System.currentTimeMillis() + expiration * 60 * 1000));
            ossClient.putObject(BUCKET_NAME, fileName, is, metadata);

            log.info("Object：" + fileName + "存入OSS成功。");
        } catch (Exception ce) {
            ce.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        log.info("Completed");
    }

    /**
     * 删除OSS文件
     *
     * @param fileName 文件名
     */
    public static void delFile(String fileName) {
        delFile(fileName, false);
    }

    public static void delFile(String fileName, Boolean isInternal) {
        var ossClient = buildOssClient(isInternal);
        try {
            ossClient.deleteObject(BUCKET_NAME, fileName);
            log.info("Object：" + fileName + "删除成功。");
        } catch (Exception oe) {
            oe.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        log.info("Completed");
    }


    /**
     * 保存工单文件
     *
     * @param files      上传文件数组
     * @param businessId 关联业务id
     * @return 上传是否成功
     */
    public static List<String> saveWorkOrderFile(MultipartFile[] files, String businessId) {
        return saveWorkOrderFile(files, businessId, false);
    }

    public static List<String> saveWorkOrderFile(MultipartFile[] files, String businessId, Boolean isInternal) {

        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
        var fileNameList = new ArrayList<String>();
        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String fileName = OREO_PROJECT_WORK_ORDER_URL + TimeUtils.nowDate().replace("-", "/") + "/" + FileUtil.renameToUUID(file.getOriginalFilename());
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
        return saveFaultFile(files, businessId, false);
    }

    public static List<String> saveFaultFile(MultipartFile[] files, String businessId, Boolean isInternal) {

        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
        var fileNameList = new ArrayList<String>();

        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String fileName = OREO_PROJECT_FAULT_URL + TimeUtils.nowDate().replace("-", "/") + "/" + FileUtil.renameToUUID(file.getOriginalFilename());
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
     * @param filename 文件名
     * @return 上传是否成功
     */
    public static boolean doesObjectExist(String filename) {
        return doesObjectExist(filename, false);
    }

    public static boolean doesObjectExist(String filename, Boolean isInternal) {
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
        return ossClient.doesObjectExist(BUCKET_NAME, filename);
    }


    /**
     * 保存故障文件
     *
     * @param filePath 文件路径
     * @return 上传是否成功
     */
    public static String saveFaultMP4(String filePath) {
        return saveFaultMP4(filePath, false);
    }

    public static String saveFaultMP4(String filePath, Boolean isInternal) {

        log.info("%s --- %s --- OSS文件上传开始 ---  %s...\n");
        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
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
            String fileName = OREO_PROJECT_FAULT_URL + TimeUtils.nowDate().replace("-", "/") + "/" + FileUtil.renameToUUID(file.getName());
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
     * @param files      上传文件数组
     * @param elevatorId 关联电梯id
     * @return 结果
     */
    public static List<Map> saveElevatorImg(MultipartFile[] files, String elevatorId) {
        return saveElevatorImg(files, elevatorId, false);
    }

    public static List<Map> saveElevatorImg(MultipartFile[] files, String elevatorId, Boolean isInternal) {
        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
        List<Map> fileNameList = new ArrayList<>();
        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String originalFilename = file.getOriginalFilename();
                String fileAbsoluteUrl =
                        OREO_PROJECT_ELEVATOR_DETAIL + TimeUtils.nowDate().replace("-", "/") + elevatorId + "/" + FileUtil.renameToUUID(originalFilename);
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
     * @param elevatorId 电梯id
     * @return 结果
     */
    public static void deleteElevatorImg(String elevatorId) {
        deleteElevatorImg(elevatorId, false);
    }

    public static void deleteElevatorImg(String elevatorId, Boolean isInternal) {
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
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
     * @param files 上传文件数组
     * @param logId 关联logId
     * @return 结果
     */
    public static List<Map> saveDeptLog(MultipartFile[] files, String logId) {
        return saveDeptLog(files, logId, false);
    }

    public static List<Map> saveDeptLog(MultipartFile[] files, String logId, Boolean isInternal) {
        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
        List<Map> fileNameList = new ArrayList<>();
        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String originalFilename = file.getOriginalFilename();
                String fileAbsoluteUrl =
                        OREO_PROJECT_SYSTEM_LOG + TimeUtils.nowDate().replace("-", "/") + logId + "/" + FileUtil.renameToUUID(originalFilename);
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
     * @param files                上传文件数组
     * @param vSiteInvestigationId 关联现勘id
     * @return 结果
     */
    public static List<Map> saveSiteInvestigationImg(MultipartFile[] files, String vSiteInvestigationId) {
        return saveSiteInvestigationImg(files, vSiteInvestigationId, false);
    }

    public static List<Map> saveSiteInvestigationImg(MultipartFile[] files, String vSiteInvestigationId, Boolean isInternal) {
        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
        List<Map> fileNameList = new ArrayList<>();
        try {
            if (!ossClient.doesBucketExist(BUCKET_NAME)) {
                ossClient.createBucket(BUCKET_NAME);
            }
            for (MultipartFile file : files) {
                // 获取文件名称及文件字节数据
                String originalFilename = file.getOriginalFilename();
                String fileAbsoluteUrl =
                        OREO_SITE_INVESTIGATION_UPLOAD_FILE + vSiteInvestigationId + "/" + FileUtil.renameToUUID(originalFilename);
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
        return saveDeviceUploadFile(file, false);
    }

    public static String saveDeviceUploadFile(MultipartFile file, Boolean isInternal) {
        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
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
        return OssInternalUtils.OSS_URL + fileUrl;
    }

    /**
     * 保存任务上传文件
     *
     * @param file MultipartFile文件
     * @return 上传是否成功
     */
    public static String savePmTaskFile(MultipartFile file) {
        return savePmTaskFile(file, false);
    }

    public static String savePmTaskFile(MultipartFile file, Boolean isInternal) {

        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
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
        return OssInternalUtils.OSS_URL + fileUrl;
    }

    /**
     * 保存任务上传文件
     *
     * @param file File文件
     * @return 上传是否成功
     */
    public static String savePmTaskFile(File file) {
        return savePmTaskFile(file, false);
    }

    public static String savePmTaskFile(File file, Boolean isInternal) {

        // 文件上传OSS
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        var ossClient = buildOssClient(isInternal);
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
        return OssInternalUtils.OSS_URL + fileUrl;
    }

    private static OSS buildOssClient() {
        //OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        return buildOssClient(false);
    }

    private static OSS buildOssClient(Boolean isInternal) {
        return new OSSClientBuilder()
                .build(isInternal ? ENDPOINT_INTERNAL : ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    public static String getOssUrl(Boolean isInternal) {
        return isInternal ? OSS_URL_INTERNAL : OSS_URL;
    }

    public static String extendOssUrl(String url) {
        return OSS_URL + url;
    }

    public static String extendOssUrl(String url, Boolean isInternal) {
        return getOssUrl(isInternal) + url;
    }


    public static String changeToInternalUrl(String url) {
        return url.replace(OSS_URL, OSS_URL_INTERNAL);
    }

    public static String changeToExternalUrl(String url) {
        return url.replace(OSS_URL_INTERNAL, OSS_URL);
    }
}

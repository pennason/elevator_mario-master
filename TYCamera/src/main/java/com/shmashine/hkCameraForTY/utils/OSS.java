package com.shmashine.hkCameraForTY.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketInfo;

/**
 * OSS
 *
 */
public class OSS {

    private static Logger log = LoggerFactory.getLogger(OSS.class);

    private static String endpoint = "http://oss-cn-shanghai.aliyuncs.com";

    private static String accessKeyId = "LTAIYOtTu4EmnOKn";

    private static String accessKeySecret = "1A7iXXtcy5kBXLW2uPAFRIhgIqRGRA";

    private static String bucketName = "oss-maixin";

    public static void setOSS(byte[] file, String fileName) {

        //PropertyConfigurator.configure("conf/log4j.properties");

        //logger.info("Started");

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {
            if (ossClient.doesBucketExist(bucketName)) {
                log.info("您已经创建Bucket：" + bucketName + "。");
            } else {
                log.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
                ossClient.createBucket(bucketName);
            }

            BucketInfo info = ossClient.getBucketInfo(bucketName);
            log.info("Bucket " + bucketName + "的信息如下：");
            log.info("\t数据中心：" + info.getBucket().getLocation());
            log.info("\t创建时间：" + info.getBucket().getCreationDate());
            log.info("\t用户标志：" + info.getBucket().getOwner());

            InputStream is = new ByteArrayInputStream(file);
            ossClient.putObject(bucketName, fileName, is);
            log.info("Object：" + fileName + "存入OSS成功。");
        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

        log.info("Completed");
    }

}

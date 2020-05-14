package com.zhupeng.baseframe.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;

@Slf4j
public class OSSUtil {

    // 设置URL过期时间为100年  3600l* 1000*24*365*10
    private static Date EXPIRATION = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 100);
    //阿里云API的内或外网域名
    private static String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID = "ACCESS_KEY_ID";
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET = "ACCESS_KEY_SECRET";
    //阿里云API的bucket名称
    private static String BUCKET_NAME = "BUCKET_NAME";


    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    public static OSSClient getOSSClient(String endpoint, String accessKeyId, String accessKeySecret) {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 创建存储空间
     *
     * @param ossClient  OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public static String createBucketName(OSSClient ossClient, String bucketName) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        try {
            ossClient.createBucket(createBucketRequest);
        } catch (Exception e) {
            log.error("创建bucket失败 , 建议bucket的名称含有中划线", e);
            throw new RuntimeException(e);
        }

        return createBucketRequest.getBucketName();
    }

    /**
     * 删除存储空间buckName
     *
     * @param ossClient  oss对象
     * @param bucketName 存储空间
     */
    public static Boolean deleteBucket(OSSClient ossClient, String bucketName) {
        if (ossClient.doesBucketExist(bucketName)) {
            ossClient.deleteBucket(bucketName);
            return true;
        }
        log.error("该Bucket{}不存在", bucketName);
        return false;
    }


    /**
     * 上传文件
     *
     * @param ossClient
     * @param file
     * @param bucketName
     * @param fileNameOnOSS
     * @param folderOnOSS
     * @return
     */
    public static Boolean uploadFile(OSSClient ossClient , File file , String bucketName , String fileNameOnOSS , String folderOnOSS) {
        if (!ossClient.doesBucketExist(bucketName)) {
            return false;
        }

        //创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();

        //指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        //指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        //指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        //如果没有扩展名则填默认值application/octet-stream
        if (StringUtils.isNotBlank(getContentType(fileNameOnOSS))) {
            metadata.setContentType(getContentType(fileNameOnOSS));
        }

        try {
            //以输入流的形式上传文件
            InputStream is = new FileInputStream(file);

            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileNameOnOSS + "/" + file.getTotalSpace() + "Byte.");
            //上传的文件的长度
            metadata.setContentLength(is.available());

            if(StringUtils.isNotBlank(folderOnOSS) && folderOnOSS.startsWith("/")){
                folderOnOSS = folderOnOSS.substring(1);
            }
            if(StringUtils.isNotBlank(folderOnOSS) && folderOnOSS.endsWith("/")){
                folderOnOSS = folderOnOSS.substring(0 , folderOnOSS.length() -1);
            }
            //parentFolder + fileName : 表示从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg
            ossClient.putObject(bucketName, folderOnOSS + "/" +fileNameOnOSS, is, metadata);
        } catch (Exception e) {
            log.error("上传文件{}失败", fileNameOnOSS, e);
            return false;
        }
        return true;
    }

    /**
     * 上传字符串
     *
     * @param ossClient
     * @param content
     * @param bucketName
     * @param fileNameOnOSS
     * @param folderOnOSS
     * @return
     */
    public static Boolean uploadString(OSSClient ossClient, String content, String bucketName, String fileNameOnOSS, String folderOnOSS) {
        if (!ossClient.doesBucketExist(bucketName)) {
            return false;
        }

        try {
            if(StringUtils.isNotBlank(folderOnOSS) && folderOnOSS.startsWith("/")){
                folderOnOSS = folderOnOSS.substring(1);
            }
            if(StringUtils.isNotBlank(folderOnOSS) && folderOnOSS.endsWith("/")){
                folderOnOSS = folderOnOSS.substring(0 , folderOnOSS.length() -1);
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderOnOSS + "/" + fileNameOnOSS , new ByteArrayInputStream(content.getBytes()));

            ossClient.putObject(putObjectRequest);

            return true;
        } catch (Exception e) {
            log.error("上传字符串失败" , e);
        }
        return false;
    }


    /***
     * 下载文件
     * @param ossClient
     * @param bucketName
     * @return
     */
    public static String getString(OSSClient ossClient, String bucketName, String absolutePathOnOSS) {
        if (!ossClient.doesBucketExist(bucketName)) {
            return null;
        }
        InputStream inputStream = null;
        try {
            if(StringUtils.isNotBlank(absolutePathOnOSS) && absolutePathOnOSS.startsWith("/")){
                absolutePathOnOSS = absolutePathOnOSS.substring(1);
            }
            OSSObject ossObject = ossClient.getObject(bucketName, absolutePathOnOSS);

            inputStream = ossObject.getObjectContent();
            StringBuilder objectContent = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                objectContent.append("" + line);
            }

            return objectContent.toString();
        } catch (Exception e) {
            log.error("获取文件{}失败", absolutePathOnOSS, e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 下载文件
     * @param ossClient
     * @param file
     * @param bucketName
     * @return
     */
    public static Boolean getFile(OSSClient ossClient , File file,  String bucketName, String absolutePathOnOSS) {
        if (!ossClient.doesBucketExist(bucketName)) {
            return false;
        }

        try {
            if(StringUtils.isNotBlank(absolutePathOnOSS) && absolutePathOnOSS.startsWith("/")){
                absolutePathOnOSS = absolutePathOnOSS.substring(1);
            }
            ossClient.getObject(new GetObjectRequest(bucketName, absolutePathOnOSS), file);
            return true;
        } catch (Exception e) {
            log.error("下载文件{}失败" , absolutePathOnOSS , e);
        }
        return false;
    }

    /**
     * 删除文件
     *
     * @param ossClient
     * @param bucketName
     */
    public static Boolean deleteFile(OSSClient ossClient, String bucketName, String absolutePathOnOSS) {
        if (!ossClient.doesBucketExist(bucketName)) {
            return false;
        }

        if(StringUtils.isNotBlank(absolutePathOnOSS) && absolutePathOnOSS.startsWith("/")){
            absolutePathOnOSS = absolutePathOnOSS.substring(1);
        }
        if (ossClient.doesObjectExist(bucketName, absolutePathOnOSS)) {
            ossClient.deleteObject(bucketName, absolutePathOnOSS);
            return true;
        }
        log.error("该文件{}不存在", absolutePathOnOSS);
        return false;
    }

    /**
     * 获取文件的URL地址（该URL地址用户下载，观看文件）
     *
     * @param ossClient
     * @param expiration 该URL的过期时间
     * @param bucketName
     * @return
     */
    public static String getUrl(OSSClient ossClient, String bucketName, Date expiration , String absolutePathOnOSS) {
        if (!ossClient.doesBucketExist(bucketName)) {
            return null;
        }
        if (expiration == null) {
            expiration = EXPIRATION;
        }
        try {
            if(StringUtils.isNotBlank(absolutePathOnOSS) && absolutePathOnOSS.startsWith("/")){
                absolutePathOnOSS = absolutePathOnOSS.substring(1);
            }
            URL url = ossClient.generatePresignedUrl(bucketName, absolutePathOnOSS , expiration);

            return url.toString();
        } catch (Exception e) {
            log.error("获取文件{}URL地址失败", absolutePathOnOSS, e);
        }

        return null;
    }

    /**
     * 关闭OSSClient客户端
     *
     * @param ossClient
     */
    public static void closeOSSClient(OSSClient ossClient) {
        ossClient.shutdown();
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static String getContentType(String fileName) {
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return null;
    }


    public static void main(String[] args) {
        OSSClient ossClient = OSSUtil.getOSSClient(OSSUtil.ENDPOINT, OSSUtil.ACCESS_KEY_ID, OSSUtil.ACCESS_KEY_SECRET);

//        File  file = new File("C:\\Users\\Administrator\\Desktop\\haizeiwang.gif");
        String fileName = "zhupeng2.jpg";
        String folder = "/test_Floder111/test_Floder222";
//        OSSUtil.uploadFile(ossClient , file , OSSUtil.BUCKET_NAME , fileName , folder);
//
//        String url = OSSUtil.getUrl(ossClient ,  OSSUtil.BUCKET_NAME ,  null , folder + "/" + fileName);
//
//        log.info("URL:{}" , url);
//
//        File file2 = new File("C:\\Users\\Administrator\\Desktop\\haizeiwang2.gif");    //1、建立连接
//        OSSUtil.getFile(ossClient , file2 , OSSUtil.BUCKET_NAME , folder + "/" + fileName);

//        OSSUtil.uploadString(ossClient , "============adadfadzhupeng朱朋=======" , OSSUtil.BUCKET_NAME , fileName , folder);
//
//        String url = OSSUtil.getUrl(ossClient ,  OSSUtil.BUCKET_NAME ,  null , folder + "/" + fileName);
//
//        String string = OSSUtil.getString(ossClient , OSSUtil.BUCKET_NAME , folder + "/" + fileName);
//
//        log.info("url:{} , content:{} " , url , string);


        OSSUtil.deleteFile(ossClient , OSSUtil.BUCKET_NAME , folder + "/" + fileName);
//        String string = OSSUtil.getString(ossClient , OSSUtil.BUCKET_NAME , folder + "/" + fileName);
//
//        File file2 = new File("C:\\Users\\Administrator\\Desktop\\haizeiwang2.gif");    //1、建立连接
//        OutputStream os = null;
//        try {
//            //2、选择输出流,以追加形式(在原有内容上追加) 写出文件 必须为true 否则为覆盖
//            os = new BufferedOutputStream(new FileOutputStream(file2,true));
//
//            byte[] data = string.getBytes();    //将字符串转换为字节数组,方便下面写入
//
//            os.write(data, 0, data.length);    //3、写入文件
//            os.flush();
//
//        } catch (Exception e){
//            log.error("" , e);
//        } finally {
//            try {
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        OSSUtil.closeOSSClient(ossClient);

    }
}

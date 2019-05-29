package com.zhupeng.baseframe.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;

public class OSSUtil {
	//log日志
    private static Logger logger = LoggerFactory.getLogger(OSSUtils.class);
    //阿里云API的内或外网域名
    private static String ENDPOINT;
    //阿里云API的密钥Access Key ID
    private static String ACCESS_KEY_ID;
    //阿里云API的密钥Access Key Secret
    private static String ACCESS_KEY_SECRET;
    //阿里云API的bucket名称
    private static String BUCKET_NAME;
    //阿里云API的文件夹名称
    private static String FOLDER;
    //初始化属性
    static{
        try {
			ENDPOINT = RedisUtil.getOrSetValueByKey("OSS_ENDPOINT");
			ACCESS_KEY_ID = RedisUtil.getOrSetValueByKey("OSS_ACCESS_KEY_ID");
			ACCESS_KEY_SECRET = RedisUtil.getOrSetValueByKey("OSS_ACCESS_KEY_SECRET");
			BUCKET_NAME = RedisUtil.getOrSetValueByKey("OSS_BUCKET_NAME");
			FOLDER = RedisUtil.getOrSetValueByKey("OSS_FOLDER");
		} catch (Exception e) {
			logger.error("请设置OSS相关配置");
		}
    }
	
    /**
     * 获取阿里云OSS客户端对象
     * @return ossClient
     */
    public static OSSClient getOSSClient(){
        return new OSSClient(ENDPOINT,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 创建存储空间
     * @param ossClient      OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public  static String createBucketName(OSSClient ossClient,String bucketName){
        //存储空间
        final String bucketNames=bucketName;
        if(!ossClient.doesBucketExist(bucketName)){
            //创建存储空间
            Bucket bucket=ossClient.createBucket(bucketName);
            logger.info("创建存储空间成功");
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间buckName
     * @param ossClient  oss对象
     * @param bucketName  存储空间
     */
    public static  void deleteBucket(OSSClient ossClient, String bucketName){
        ossClient.deleteBucket(bucketName);
        logger.info("删除" + bucketName + "Bucket成功");
    }

    /**
     * 创建模拟文件夹
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @param folder   模拟文件夹名如"qj_nanjing/"
     * @return  文件夹名
     */
    public  static String createFolder(OSSClient ossClient,String bucketName,String folder){
    	if(StringUtils.isBlank(bucketName)){
        	bucketName = BUCKET_NAME;
        }
        //文件夹名
        final String keySuffixWithSlash =folder;
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            logger.info("创建文件夹成功");
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir=object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param ossClient  oss连接
     * @param bucketName  存储空间
     * @param folder  模拟文件夹名 如"qj_nanjing/"
     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key){
    	if(StringUtils.isBlank(bucketName)){
        	bucketName = BUCKET_NAME;
        }
        ossClient.deleteObject(bucketName, folder + key);
        logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
    }

    /**
     * 上传图片至OSS
     * @param ossClient  oss连接
     * @param file 上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName  存储空间
     * @param folder 模拟文件夹名 如"qj_nanjing/"
     * @return String 返回的唯一MD5数字签名
     * */
    public static  String uploadObject2OSS(OSSClient ossClient, String fileName, MultipartFile file, String bucketName, String folder) {
        String resultStr = null;
        if(StringUtils.isBlank(bucketName)){
        	bucketName = BUCKET_NAME;
        }
        try {
            //以输入流的形式上传文件
            InputStream is = file.getInputStream();
            //文件大小
            Long fileSize = file.getSize();
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileName));
            //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, is, metadata);
            //解析结果
            resultStr = putResult.getETag();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public static  String getContentType(String fileName){
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if(".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if(".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)  || ".png".equalsIgnoreCase(fileExtension) ) {
            return "image/jpeg";
        }
        if(".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if(".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if(".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if(".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if(".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if(".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }
	
	
	public static String getObject(OSSClient ossClient,String fileName,String bucketName){
		InputStream inputStream = null;
		try {
			OSSObject ossObject = ossClient.getObject(bucketName, fileName);
	        inputStream = ossObject.getObjectContent();
	        StringBuilder objectContent = new StringBuilder();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	        while (true) {
	            String line = reader.readLine();
	            if (line == null){
	            	 break;
	            }
	            objectContent.append(line);
	        }
	        
	        return objectContent.toString();
		} catch (Exception e) {
			// e.printStackTrace();
		}finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != ossClient) {
				ossClient.shutdown();
			}
		}
		return null;
	}
	
	public static String getUrl(OSSClient ossClient,String fileName,String bucketName){
		if(StringUtils.isBlank(bucketName)){
        	bucketName = BUCKET_NAME;
        }
		try {
			Date expiration = new Date(new Date().getTime() + 3600 * 1000);
			URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
			return url.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (null != ossClient) {
				ossClient.shutdown();
			}
		}
		return null;
	}
	
//	 public static String byteImage(String imageName, String aesKey, String accessKeyId,String accessKeySecret,String bucketName,String endpoint) {
//        try {
//            String frontImg = getObject(imageName, bucketName, accessKeyId, accessKeySecret, endpoint);
//            if (frontImg == null) {
//            	return "";
//			}
//            byte[] byteFrontImg = AesUtils.decrypt(AesUtils.parseHexStr2Byte(frontImg), aesKey);
//            return new String(byteFrontImg != null ? byteFrontImg : new byte[0]);
//		} catch (Exception e) {
//			// e.printStackTrace();
//			return "";
//		}
//    	
//    }
	
	public static void main(String[] args) {
		//uploadFile("text.jpg", new File("E:\\tmp\\pic\\100020170627203628takephotoB.jpg"));
		//putObject("mmoreport", "100020170627203628takephotoB".getBytes());
		
//		downLoad("100820170628114429takephotoB.jpg", null, "F://111.JPG",false);
//		
//		OSSUtils.putObject("文件名", "aaa".getBytes(),"rs-crc",false);
//		OSSUtils.getObject("文件名", "rs-crc",false);
		
		//初始化OSSClient
//        OSSClient ossClient=OSSUtil.getOSSClient();
        //上传文件
//        String files="C:\\Users\\wuming\\Desktop\\a\\2244.png,C:\\Users\\wuming\\Desktop\\a\\2219.png,"
//                + "C:\\Users\\wuming\\Desktop\\a\\3608.png,C:\\Users\\wuming\\Desktop\\a\\5341.png,"
//                + "C:\\Users\\wuming\\Desktop\\a\\61310.png,C:\\Users\\wuming\\Desktop\\a\\74657.png";
//        String[] file=files.split(",");
//        for(String filename:file){
//            //System.out.println("filename:"+filename);
//            File filess=new File(filename);
            //String md5key = OSSUtil.uploadObject2OSS(ossClient, filess, BACKET_NAME, FOLDER);
           // logger.info("上传后的文件MD5数字唯一签名:" + md5key);
            //上传后的文件MD5数字唯一签名:40F4131427068E08451D37F02021473A
//        }
	}
}

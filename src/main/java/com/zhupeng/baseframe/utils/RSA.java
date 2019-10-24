package com.zhupeng.baseframe.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 1、公钥加密，私钥解密用于信息加密
 * 2、私钥加密，公钥解密用于数字签名
 */
/**
 * 常用签名算法:
 * RSASignature
 * DSASignature
 * ECDSASignature（需Bouncy Castle）
 *
 * 数字签名作用:
 * 验证数据完整性
 * 认证数据来源
 * 抗否认
 */
/**
 * https://segmentfault.com/a/1190000011263680
 * RSA加解密中必须考虑到的密钥长度、明文长度和密文长度问题。明文长度需要小于密钥长度，而密文长度则等于密钥长度。因此当加密内容长度大于密钥长度时，有效的RSA加解密就需要对内容进行分段。
 *
 * 这是因为，RSA算法本身要求加密内容也就是明文长度m必须0<m<密钥长度n。如果小于这个长度就需要进行padding，因为如果没有padding，就无法确定解密后内容的真实长度，字符串之类的内容问题还不大，以0作为结束符，但对二进制数据就很难，因为不确定后面的0是内容还是内容结束符。而只要用到padding，那么就要占用实际的明文长度，于是实际明文长度需要减去padding字节长度。我们一般使用的padding标准有NoPPadding、OAEPPadding、PKCS1Padding等，其中PKCS#1建议的padding就占用了11个字节。
 *
 * 这样，对于1024长度的密钥。128字节（1024bits）-减去11字节正好是117字节，但对于RSA加密来讲，padding也是参与加密的，所以，依然按照1024bits去理解，但实际的明文只有117字节了。
 *
 * 所以如果要对任意长度的数据进行加密，就需要将数据分段后进行逐一加密，并将结果进行拼接。同样，解码也需要分段解码，并将结果进行拼接。
 */
@Slf4j
public class RSA {
    //定义加密方式
    public static final String KEY_RSA = "RSA";
    //定义公钥关键词
    public static final String KEY_RSA_PUBLICKEY = "RSAPublicKey";
    //定义私钥关键词
    public static final String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";
    //定义签名算法
    private final static String KEY_RSA_SIGNATURE = "MD5withRSA"; //签名算法：MD2withRSA,SHA1WithRSA,SHA256withRSA,SHA384withRSA,SHA512withRSA

    private static final String CHARSET = "UTF-8";

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        int keySize = 1024;
        map = RSA.createKey(keySize);
        System.out.println("公钥："+RSA.getPublicKey(map));
        System.out.println("私钥："+RSA.getPrivateKey(map));

        String publicKey = RSA.getPublicKey(map);
        String privateKey = RSA.getPrivateKey(map);

        String str = "你好gol！@#￥！#@%……￥%@&……*%……&（……&*）《？》M>dhne,!MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoI6/B/AvpxlZDgKBiftKekM9pBgsQeKPd0oFO82v+VoQuWFDnecbfl7cjbCenZvgFFLUOgELKgYeUyaG4Sguk0dL6XSpS6hoHalxPqsTvhFzGGPRdO3iwKtnmAL10N3BeMN7kKLLSwnyY+JT+Mr0rSMwJb5uI4AllIFAlpsD/pQIDAQAB\";";


        // 公钥加密，私钥解密
        String enStr1 = RSA.encryptByPublic(str, publicKey);
        System.out.println("公钥加密后："+enStr1);
        String deStr1 = RSA.decryptByPrivate(enStr1, privateKey);
        System.out.println("私钥解密后："+deStr1);

        // 私钥加密，公钥解密
        String enStr2 = RSA.encryptByPrivate(str, privateKey);
        System.out.println("私钥加密后："+enStr2);
        String deStr2 = RSA.decryptByPublic(enStr2, publicKey);
        System.out.println("公钥解密后："+deStr2);

        // 产生签名
        String sign = sign(enStr1, privateKey);
        System.out.println("签名:"+sign);
        // 验证签名
        boolean status = verify(enStr1, publicKey, sign);
        System.out.println("状态:"+status);

    }


    /**
     * 生成公私密钥对
     */
    public static Map<String, Object> createKey(int keySize) {
        Map<String, Object> map = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
            //设置密钥对的bit数，越大越安全，但速度减慢，一般使用512或1024
            generator.initialize(keySize);
            KeyPair keyPair = generator.generateKeyPair();
            // 获取公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 获取私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 将密钥对封装为Map
            map = new HashMap<String, Object>();
            map.put(KEY_RSA_PUBLICKEY, publicKey);
            map.put(KEY_RSA_PRIVATEKEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 获取Base64编码的公钥字符串
     */
    public static String getPublicKey(Map<String, Object> map) {
        String str = "";
        Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
        str = encryptBase64(key.getEncoded());
        return str;
    }

    /**
     * 获取Base64编码的私钥字符串
     */
    public static String getPrivateKey(Map<String, Object> map) {
        String str = "";
        Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);
        str = encryptBase64(key.getEncoded());
        return str;
    }

    /**
     * BASE64 解码
     * @param key 需要Base64解码的字符串
     * @return 字节数组
     */
    public static byte[] decryptBase64(String key) {
        try {
            return Base64.getDecoder().decode(key.getBytes(CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("该编码方式不存在");
        }
        return null;
    }

    /**
     * BASE64 编码
     * @param key 需要Base64编码的字节数组
     * @return 字符串
     */
    public static String encryptBase64(byte[] key) {
        try {
            return new String(Base64.getEncoder().encode(key),CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("编码不存在");
        }
        return null;
    }

    /**
     * 公钥加密
     * @param encryptingStr
     * @param publicKeyStr
     * @return
     */
    public static String encryptByPublic(String encryptingStr, String publicKeyStr){
        try {
            //由于在自己写获取公钥的地方进行了base64的加密
            // 将公钥由字符串转为UTF-8格式的字节数组
            byte[] publicKeyBytes = decryptBase64(publicKeyStr);
            // 取得待加密数据
            byte[] data = encryptingStr.getBytes(CHARSET);
            //获得公钥
            RSAPublicKey publicKey = getPublicKey(publicKeyBytes);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(KEY_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 返回加密后由Base64编码的加密信息
            return encryptBase64(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 私钥解密
     * @param encryptedStr
     * @param privateKeyStr
     * @return
     */
    public static String decryptByPrivate(String encryptedStr, String privateKeyStr){
        try {
            // 对私钥解密
            byte[] privateKeyBytes = decryptBase64(privateKeyStr);
            // 获得待解密数据
            byte[] data = decryptBase64(encryptedStr);
            // 获得私钥
            RSAPrivateKey privateKey = getPrivateKey(privateKeyBytes);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(KEY_RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 返回UTF-8编码的解密信息
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, data, privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 私钥加密
     * @param encryptingStr
     * @param privateKeyStr
     * @return
     */
    public static String encryptByPrivate(String encryptingStr, String privateKeyStr){
        try {
            byte[] privateKeyBytes = decryptBase64(privateKeyStr);
            // 取得待加密数据
            byte[] data = encryptingStr.getBytes(CHARSET);
            // 取私钥对象
            RSAPrivateKey privateKey = getPrivateKey(privateKeyBytes);
            Cipher cipher = Cipher.getInstance(KEY_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            // 返回加密后由Base64编码的加密信息
            return encryptBase64(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 公钥解密
     * @param encryptedStr
     * @param publicKeyStr
     * @return
     */
    public static String decryptByPublic(String encryptedStr, String publicKeyStr){
        try {
            // 对公钥解密
            byte[] publicKeyBytes = decryptBase64(publicKeyStr);
            // 取得待加密数据
            byte[] data = decryptBase64(encryptedStr);
            // 取公钥对象
            RSAPublicKey publicKey = getPublicKey(publicKeyBytes);
            Cipher cipher = Cipher.getInstance(KEY_RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            // 返回UTF-8编码的解密信息
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, data, publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * 用私钥对加密数据进行签名
     * @param encryptedStr 使用私钥加密后的密文
     * @param privateKey   私钥
     * @return
     */
    public static String sign(String encryptedStr, String privateKey) {
        String str = "";
        try {
            //将私钥加密数据字符串转换为字节数组
            byte[] data = encryptedStr.getBytes(CHARSET);
            // 解密由base64编码的私钥
            byte[] bytes = decryptBase64(privateKey);
            // 取私钥对象
            RSAPrivateKey key = getPrivateKey(bytes);
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            str = encryptBase64(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 校验数字签名
     * @param encryptedStr
     * @param publicKey
     * @param sign
     * @return 校验成功返回true，失败返回false
     */
    public static boolean verify(String encryptedStr, String publicKey, String sign) {
        boolean flag = false;
        try {
            //将私钥加密数据字符串转换为字节数组
            byte[] data = encryptedStr.getBytes(CHARSET);
            // 解密由base64编码的公钥
            byte[] bytes = decryptBase64(publicKey);
            // 取公钥对象
            RSAPublicKey key = getPublicKey(bytes);
            // 用公钥验证数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag = signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private static RSAPublicKey getPublicKey(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        // 指定的加密算法
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        // 取公钥对象
        RSAPublicKey key = (RSAPublicKey) factory.generatePublic(keySpec);

        System.out.println("公钥长度：" + key.getModulus().bitLength());

        return key;
    }

    private static RSAPrivateKey getPrivateKey(byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(privateKey);
        // 指定的加密算法
        KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        // 取私钥对象
        RSAPrivateKey key = (RSAPrivateKey)factory.generatePrivate(pkcs);

        System.out.println("私钥长度：" + key.getModulus().bitLength());

        return key;
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

}

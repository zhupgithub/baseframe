package com.zhupeng.baseframe.utils;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

/**
 *  字符串转字节或字节转字符串时 一定要加上编码，否则可能出现乱码
 *  https://segmentfault.com/a/1190000007286619
 */
public class DES {

    private static final String DECRYPT_TYPE = "DES";
    private static final String CHARSET = "UTF-8";
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String str = "你好,goldlone#￥%@#￥%@#￥asdfadsf阿打发打发打发ad1234!#$!%*&^&#$%//';l';',.";
        //密码最好是8的倍数
        String password = "12345678";

        String encryStr =  DES.encrypt(str, password);
        System.out.println("加密结果："+encryStr);

        String decryStr = DES.decrypt(encryStr, password);
        System.out.println("解密结果："+decryStr);


        String password2 = createPassword();

        String encryStr2 =  DES.encrypt(str, password2);
        System.out.println("加密结果："+encryStr2);

        String decryStr2 = DES.decrypt(encryStr2, password2);
        System.out.println("解密结果："+decryStr2);


        String file1 = "C:\\Users\\Administrator\\Desktop\\file1.txt";
        String file2 = "C:\\Users\\Administrator\\Desktop\\file2.txt";
        String file3 = "C:\\Users\\Administrator\\Desktop\\file3.txt";
        encryptFile(file1 , file2 , password);
        decryptFile(file2 , file3 , password);
    }

    public static String createPassword(){
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            //为我们选择的DES算法生成一个KeyGenerator对象
            KeyGenerator kg = KeyGenerator.getInstance (DECRYPT_TYPE);
            kg.init (sr);
            //生成密钥
            Key key = kg.generateKey();

            //直接将byte数组转成字符串，存在乱码
            String password = BytesToHex.arr2HexStr(key.getEncoded() ,true);

            System.out.println("密码：" + password);
            password = new String(Base64.getEncoder().encode(password.getBytes(CHARSET)),CHARSET);
            return password;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进行加密操作
     * 参数一：待加密的字符串，参数二：加密密钥
     * 返回经过Base64编码后的字符串
     * 编码格式为UTF-8
     */
    public static String encrypt(String encryptionStr, String password) {
        try{
            password = new String(Base64.getDecoder().decode(password.getBytes(CHARSET)));
            byte[] encryptionBytes = encryptionStr.getBytes(CHARSET);

            Cipher cipher = getCipher(password , Cipher.ENCRYPT_MODE);
            // 执行加密操作
            cipher.doFinal(encryptionBytes);//DES 加密
            byte[] encryptionBase64Bytes = Base64.getEncoder().encode(cipher.doFinal(encryptionBytes));
            // 转换为字符串返回
            return new String(encryptionBase64Bytes);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进行解密操作
     * 参数一：待解密的字符串，参数二：加密密钥
     * 返回解密后的字符串
     */
    public static String decrypt(String decryptionBase64Str, String password) {
        try {
            password = new String(Base64.getDecoder().decode(password.getBytes(CHARSET)));
            byte[] decryptionbytes = Base64.getDecoder().decode(decryptionBase64Str.getBytes(CHARSET));

            Cipher cipher = getCipher(password , Cipher.DECRYPT_MODE);
            cipher.doFinal(decryptionbytes);//DES 解密 ，加解密的区别在于Cipher.DECRYPT_MODE
            // 开始解密操作
            return new String(cipher.doFinal(decryptionbytes), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将文件加密，写入另一个文件中
     * @param file 目标文件
     * @param destFile 加密后的文件
     * @param password 密码
     */
    public static void encryptFile(String file, String destFile , String password){
        try {
            password = new String(Base64.getDecoder().decode(password.getBytes(CHARSET)));
            Cipher cipher = getCipher(password , Cipher.ENCRYPT_MODE);

            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(destFile);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }

            out.flush();
            cis.close();
            is.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将待解密的文件进行解密
     * @param file 待解密的文件
     * @param destFile 解密后的文件
     * @param password 密码
     */
    public static void decryptFile(String file, String destFile , String password){
        try {
            password = new String(Base64.getDecoder().decode(password.getBytes(CHARSET)));
            Cipher cipher = getCipher(password , Cipher.DECRYPT_MODE);

            InputStream is = new FileInputStream(file);
            OutputStream out = new FileOutputStream(destFile);
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            cos.flush();
            cos.close();
            out.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成Cipher对象
     * @param password
     * @param encryptMode  加解密模式：  加密或者解密
     * @return
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static Cipher getCipher(String password , int encryptMode) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        // 生成一个可信任的随机数源
        SecureRandom random = new SecureRandom();

        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes(CHARSET));
        // 创建一个密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DECRYPT_TYPE);
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DECRYPT_TYPE);
        // 用密钥初始化Cipher对象
        cipher.init(encryptMode, securekey, random);

        return cipher;
    }
}
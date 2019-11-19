package com.zhupeng.baseframe.utils;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

//1 生成源密钥
//2 把源公钥交给目标，目标通过源公钥，生成目标公钥和私钥
//3 把目标公钥交给源
//4 双方使用对方的公钥和和自己的私钥，生成本地密钥
//5 如果双方生成本地密钥相同则完成密钥交换
//双方在没有确定共同密钥的情况下，生成密钥，不提供加密工作，加解密还需要其他对称加密算法实现
public class DHUtil {

    public static final String PUBLIC_KEY = "DH_Public_Key";
    public static final String PRIVATE_KEY = "DH_Private_key";

    /**
     * 生成源密钥对
     * @return
     * @throws Exception
     */
    public static Map<String,Object> initSourceKey() throws Exception{
        //创建KeyPairGenerator的实例，选用DH算法
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");

        //初始化密钥长度，默认1024，可选范围512-65536 & 64的倍数
        keyPairGenerator.initialize(1024);

        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey dhPublicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey dhPrivateKey = (DHPrivateKey) keyPair.getPrivate();

        //将密钥对放入Map
        Map<String,Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, dhPublicKey);
        keyMap.put(PRIVATE_KEY, dhPrivateKey);
        return keyMap;
    }

    /**
     * 通过源公钥 生成 目标密钥对
     * @param sourcePublicKey
     * @return
     * @throws Exception
     */
    public static Map<String,Object> initTargetKey(byte[] sourcePublicKey) throws Exception {

        KeyFactory keyFactory = KeyFactory.getInstance("DH");

        //通过源公钥，生成keySpec，使用KeyFactory生成源PublicKey相关信息
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(sourcePublicKey);
        DHPublicKey sourcePublic = (DHPublicKey) keyFactory.generatePublic(keySpec);

        DHParameterSpec dhPublicKeyParams = sourcePublic.getParams();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(dhPublicKeyParams);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        DHPublicKey dhPublicKey = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey dhPrivateKey = (DHPrivateKey) keyPair.getPrivate();

        //将密钥对放入Map
        Map<String,Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, dhPublicKey);
        keyMap.put(PRIVATE_KEY, dhPrivateKey);
        return keyMap;
    }

    /**
     * 使用一方的公钥和另一方的私钥，生成本地密钥
     * @return
     */
    public static byte[] generateLocalSecretKey(byte[] aPublicKey, byte[] bPrivateKey) throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance("DH");

        //通过A公钥，生成keySpec，使用KeyFactory生成A PublicKey相关信息
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(aPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        //通过B私钥，生成B PrivateKey相关信息
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //通过KeyAgreement对A的PublicKey和B的PrivateKey进行加密
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey,true);


        return keyAgreement.generateSecret("AES").getEncoded();//算法使用对称加密算法（DES，DESede，AES）
        //return keyAgreement.generateSecret();                // 也可以不选择算法，使用默认方法计算
    }

    //获取公钥字节数组
    public static byte[] getPublicKey(Map<String,Object> map){
        return ((DHPublicKey) map.get(PUBLIC_KEY)).getEncoded();
    }

    //获取私钥字节数组
    public static byte[] getPrivateKey(Map<String,Object> map){
        return ((DHPrivateKey) map.get(PRIVATE_KEY)).getEncoded();
    }

    public static void main(String[] args) throws Exception {

        byte[] source_public_key;
        byte[] source_private_key;
        byte[] source_local_key;

        byte[] target_public_key;
        byte[] target_private_key;
        byte[] target_local_key;

        Map<String, Object> sourceKey = initSourceKey();
        source_public_key = getPublicKey(sourceKey);
        source_private_key = getPrivateKey(sourceKey);

        System.out.println("源公钥："+BytesToHex.arr2HexStr(source_public_key , true));
        System.out.println("源私钥："+BytesToHex.arr2HexStr(source_private_key , true));

        Map<String, Object> targetKey = initTargetKey(getPublicKey(sourceKey));
        target_public_key = getPublicKey(targetKey);
        target_private_key = getPrivateKey(targetKey);

        System.out.println("目标公钥："+BytesToHex.arr2HexStr(target_public_key , true));
        System.out.println("目标私钥："+BytesToHex.arr2HexStr(target_private_key , true));

        source_local_key = generateLocalSecretKey(target_public_key, source_private_key);
        target_local_key = generateLocalSecretKey(source_public_key, target_private_key);

        System.out.println("源本地密钥："+BytesToHex.arr2HexStr(source_local_key , true));
        System.out.println("目标本地密钥："+BytesToHex.arr2HexStr(target_local_key , true));
    }
}

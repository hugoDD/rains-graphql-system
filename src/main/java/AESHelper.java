import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import com.rains.graphql.common.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

/**
 * AES的加密和解密(前端JS和JAVA后台)
 */
public class AESHelper {
    //密钥 (需要前端和后端保持一致)
    private static final String KEY = "mplanyou07558888";  
    //算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    
    /** 
     * AES解密 
     * @param encrypt   内容 
     * @return 
     * @throws Exception 
     */  
    public static String DecryptAES(String encrypt) {  
        try {
            return DecryptAES(encrypt, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }  
      
    /** 
     * AES加密 
     * @param content 
     * @return 
     * @throws Exception 
     */  
    public static String EncryptAES(String content) {  
        try {
            return EncryptAES(content, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }  
  
    /** 
     * 将byte[]转为各种进制的字符串 
     * @param bytes byte[] 
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制 
     * @return 转换后的字符串 
     */  
    public static String binary(byte[] bytes, int radix){  
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
    }  
  
    /** 
     * base 64 encode 
     * @param bytes 待编码的byte[] 
     * @return 编码后的base 64 code 
     */  
    public static String base64Encode(byte[] bytes){  
        return Base64.encodeBase64String(bytes);  
    }  
  
    /** 
     * base 64 decode 
     * @param base64Code 待解码的base 64 code 
     * @return 解码后的byte[] 
     * @throws Exception 
     */  
    public static byte[] base64Decode(String base64Code) throws Exception{  
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }  
  
      
    /** 
     * AES加密 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的byte[] 
     * @throws Exception 
     */  
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));  
  
        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
  
  
    /** 
     * AES加密为base 64 code 
     * @param content 待加密的内容 
     * @param encryptKey 加密密钥 
     * @return 加密后的base 64 code 
     * @throws Exception 
     */  
    public static String EncryptAES(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }  
  
    /** 
     * AES解密 
     * @param encryptBytes 待解密的byte[] 
     * @param decryptKey 解密密钥 
     * @return 解密后的String 
     * @throws Exception 
     */  
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  
        return new String(decryptBytes);  
    }  
  
  
    /** 
     * 将base 64 code AES解密 
     * @param encryptStr 待解密的base 64 code 
     * @param decryptKey 解密密钥 
     * @return 解密后的string 
     * @throws Exception 
     */  
    public static String DecryptAES(String encryptStr, String decryptKey) throws Exception {  
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    public static void main(String[] args) {
        String deReuestParas = AESHelper.DecryptAES("vrdCxWqmC6nNDXrxEyNCX4jWSt3vuQ5jzp2PG0o3D2vCC9tqdaomdikQHFF5Pvyc8XvZO0tLh/l07paAEeDcOpR05faYJaLC15v0ok9M8LWK4VWcPTmnS5Vft7ESzrC0Qyc5JR43Z4lKFtZQM/V+3ae9J/K2dk0GtUQiGoggI99XNIYw7L8+21ANamRsuZUM1de6kn73MMckpriXTqdnY+oekqjS3sUjOTOyDHbMTq9BozKMHKGDXBiZ7ohZlrOxFeTpkHw6DNjmhlaL6YFacBU2D3225RFbyoeh5DFfCWBWt1/GTfAS08QUFkwZvbOc9pf55NA8pyVlIWMAQkS/Xo3D4uvbL8MC/sQ2JuXGmPMc7NAV9CtaN4WnVpI7bJuo");
        System.out.println(deReuestParas);
    }
    
}
package xwh.lib.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;


/**
 * 处理字符串格式
 * 
 */
public class StringUtil {

	/**
	 * 将时间转化为字符串
	 * @param time 毫秒时间
	 * @return 字符串，如：09:22:34 
	 */
	public static String getTimeFormat(long time) {
		long seconds = time / 1000;
		long m = seconds/60;
		long h = m/60;
		if(h==0){
			return MessageFormat.format(
					"{0,number,00}:{1,number,00}",
					m% 60, seconds% 60);
		}else{
			return MessageFormat.format(
					"{0,number,00}:{1,number,00}:{2,number,00}",
					h, m% 60, seconds% 60);
		}
	}

	
	/**
	 * 从Url地址中截取文件地址
	 */
	public static String getFileNameFromUrl(String url){
		int index = url.lastIndexOf('/');
		if(index==-1){
			url.lastIndexOf('\\');
		}
		if(index==-1){
			return null;
		}
		
		return url.substring(index+1);
	}
	
	/**
	 * 获取文件后缀名
	 */
	public static String getFileExtension(String url){
		if(url==null){
			return null;
		}
		
		int index = url.lastIndexOf('.');		
		if(index==-1){
			return null;
		}
		
		return url.substring(index+1);
	}
	
	
	/** 
     * MD5 加密 
     */  
    public static String getMD5Str(String str) {  
        MessageDigest messageDigest = null;  
        try {  
            messageDigest = MessageDigest.getInstance("MD5");  
  
            messageDigest.reset();  
  
            messageDigest.update(str.getBytes("UTF-8"));  
        } catch (NoSuchAlgorithmException e) {  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  

        byte[] byteArray = messageDigest.digest();  
        StringBuffer md5StrBuff = new StringBuffer();  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
        return md5StrBuff.toString();  
    }
	
}

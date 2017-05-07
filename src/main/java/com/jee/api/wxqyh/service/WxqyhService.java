package com.jee.api.wxqyh.service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jee.api.wxqyh.bean.MsgTemplate;
import com.jee.api.wxqyh.util.ConstantUtil;
import com.jee.rest.base.annotation.cache.Cacheable;
import com.jee.rest.base.response.code.ResponseCode;
import com.jee.rest.base.util.RandomUtil;
import com.jee.rest.base.exception.BusinessException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service("wxqyhService")
public class WxqyhService {
	
	private static final Logger logger = LogManager.getLogger(WxqyhService.class);

	private  OkHttpClient client = new OkHttpClient.Builder()
			 .connectTimeout(10, TimeUnit.SECONDS)
			 .writeTimeout(10, TimeUnit.SECONDS) 
			 .readTimeout(30, TimeUnit.SECONDS).build() ;
	
	
	public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }
 
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }
 
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
 
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
 
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
 
            OkHttpClient okHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS)
            		                           .writeTimeout(60, TimeUnit.SECONDS)
            		                           .readTimeout(60, TimeUnit.SECONDS).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	/**
	 * 获取accessToken
	 * @param cropId
	 * @param corpsecret
	 * @return
	 */
	@Cacheable(name = "access:token:" , key = "#cropId" , expire = 7300)
	public String getAccessToken(String cropId , String corpsecret){
		try{
			
			Request request = new Request.Builder()
					.url(String.format(ConstantUtil.FETCH_ACCESS_TOKEN, cropId , corpsecret)).build();
			Response response = 
					client.newCall(request).execute() ;
			
			if(!response.isSuccessful()){
				throw new BusinessException(ResponseCode.NETWORK_ERROR_511 , "调用接口发送网络异常") ;
			}
			String json = response.body().string() ;
			logger.info("fetch access token:{}" , json);
			JSONObject object = JSONObject.parseObject(json) ;
			
			String accessToken = object.getString("access_token") ;
			if(StringUtils.isNotBlank(accessToken)){
				return accessToken ;
			}else{
				logger.error("fetch access token error , return :{}" , json);
				return null ;
			}
			
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			return null ;
		}
	}
	/**
	 * 回调模式下  根据code查询用户userId
	 * @param accessToke
	 * @param code
	 * @return
	 */
	public JSONObject getUserInfo(String accessToken , String code){
		if(StringUtils.isBlank(accessToken)){
			return null ;
		}
		if(StringUtils.isBlank(code)){
			return null ;
		}
		
		try{
			Request request = new Request.Builder()
					.url(String.format(ConstantUtil.USERID_GET_URl, accessToken , code)).build();
			Response response = 
					client.newCall(request).execute() ;
			
			if(!response.isSuccessful()){
				throw new BusinessException(ResponseCode.NETWORK_ERROR_511 , "调用接口发送网络异常") ;
			}
			String json = response.body().string() ;
			logger.info("fetch access token:{}" , json);
			JSONObject object = JSONObject.parseObject(json) ;
			return object ;
			
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			return null ;
		}
	}
	
	
	
	/**
	 * 根据userId获取用户详细信息
	 * @param accessToken
	 * @param userId
	 * @return
	 */
	//@Cacheable(prefix = "qyh:user:info:" , key = "#userId" , expire = 1000)
	public JSONObject getDetailUserInfo(String accessToken , String userId){
		if(StringUtils.isBlank(accessToken)){
			return null ;
		}
		if(StringUtils.isBlank(userId)){
			return null ;
		}
		try{
			Request request = new Request.Builder()
					.url(String.format(ConstantUtil.USER_GET_URL, accessToken , userId)).build();
			Response response = 
					client.newCall(request).execute() ;
			
			if(!response.isSuccessful()){
				throw new BusinessException(ResponseCode.NETWORK_ERROR_511 , "网络异常") ;
			}
			String json = response.body().string() ;
			logger.info("fetch access token:{}" , json);
			JSONObject object = JSONObject.parseObject(json) ;
			if(object != null){
				if(object.getIntValue("errcode") == 0){
					return object ;
				}
				throw new BusinessException(ResponseCode.FAILUER_10 , 
						object.getString("errmsg") ) ;
			}else{
				logger.error("fetch access token:{}" , json);
				return null ;
			}
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			return null ;
		}
		
	}
	
	/**
	 * 发消息
	 * @param accessToken
	 * @param msg
	 * @return
	 */
	public JSONObject sendMsg(String accessToken , MsgTemplate msg){
		if(StringUtils.isBlank(accessToken)){
			return null ;
		}
		String url = String.format(ConstantUtil.MSG_SEND_URL, accessToken) ;
		try{
			Request request = new Request.Builder()
	                .url(url)
	                .post(RequestBody.create(
	                		MediaType.parse("application/json; charset=utf-8"),
	                		msg.toString()
	                 )).build();
	               
			Response response = client.newCall(request).execute();  
			if(!response.isSuccessful()){
				throw new BusinessException(ResponseCode.NETWORK_ERROR_511 , "调用接口发送网络异常") ;
			}
			return JSON.parseObject(response.body().string());

		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
		}
		
		return null ;
	}
	/**
	 * 上传临时素材
	 * @param accessToken
	 * @param type
	 * @param file
	 * @return
	 */
	public String uploadTempFile(String accessToken , String type , File file ){
		if(StringUtils.isBlank(accessToken)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "accessToken不为空") ;
		}
		if(StringUtils.isBlank(type)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "type不为空") ;
		}
		try{  
			  //http://blog.csdn.net/android_freshman/article/details/51910937
			  RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
		      RequestBody requestBody = new MultipartBody.Builder()
		                .setType(MultipartBody.FORM) 
		                .addFormDataPart(type , file.getName() , fileBody)
		                .build();
		      Request request = new Request.Builder()
		        		.url(String.format(ConstantUtil.TEMP_UPLOAD_MEDIA_URL, accessToken , type ))
		                .post(requestBody)
		                .build();
		        
			   
			  Response response = client.newCall(request).execute();
			  if (!response.isSuccessful()) {
				  throw new BusinessException(ResponseCode.NETWORK_ERROR_511 , "调用接口发送网络异常") ;
			  }
			  JSONObject obj = JSONObject.parseObject(response.body().string()) ;
			  if(StringUtils.isNotBlank(obj.getString("media_id"))){
				  return obj.getString("media_id") ;
			  }
			  logger.error("upload temp file , return:{}" , obj);
			  throw new BusinessException(ResponseCode.FAILUER_10 , "上传临时文件失败") ;
			    
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.FAILUER_10 , "上传文件发送异常") ;
		}
		
		
	}
	
	public String uploadTempFile(String accessToken , String type , byte[] file ){
		if(StringUtils.isBlank(accessToken)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "accessToken不为空") ;
		}
		if(StringUtils.isBlank(type)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "type不为空") ;
		}
		try{
			  RequestBody fileBody = RequestBody.create(
					           MediaType.parse("application/octet-stream"), file);
			  
			
		      RequestBody requestBody = new MultipartBody.Builder()
		                .setType(MultipartBody.FORM) 
		                .addFormDataPart(type , RandomUtil.randomLower(8) , fileBody)
		                .build();
		      Request request = new Request.Builder()
		        		.url(String.format(ConstantUtil.TEMP_UPLOAD_MEDIA_URL, accessToken , type ))
		                .post(requestBody)
		                .build();
		        
			   
			  Response response = client.newCall(request).execute();
			  if (!response.isSuccessful()) {
				  throw new BusinessException(ResponseCode.NETWORK_ERROR_511 , "调用接口发送网络异常") ;
			  }
			  JSONObject obj = JSONObject.parseObject(response.body().string()) ;
			  if(StringUtils.isNotBlank(obj.getString("media_id"))){
				  return obj.getString("media_id") ;
			  }
			  logger.error("upload temp file , return:{}" , obj);
			  throw new BusinessException(ResponseCode.FAILUER_10 , "上传临时文件失败") ;
			    
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.FAILUER_10 , "上传文件发送异常") ;
		}
		
		
	}

	/**
	 * 上传永久文件
	 * @param accessToken
	 * @param type
	 * @param file
	 * @return
	 */
	public String uploadPerFile(String accessToken , String type , File file ){
		if(StringUtils.isBlank(accessToken)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "accessToken不为空") ;
		}
		if(StringUtils.isBlank(type)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "type不为空") ;
		}
		try{  
			  //http://blog.csdn.net/android_freshman/article/details/51910937
			  RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
		      RequestBody requestBody = new MultipartBody.Builder()
		                .setType(MultipartBody.FORM) 
		                .addFormDataPart(type , file.getName() , fileBody)
		                .build();
		      Request request = new Request.Builder()
		        		.url(String.format(ConstantUtil.PER_UPLOAD_MEDIA_URL, accessToken , type ))
		                .post(requestBody)
		                .build();
		        
			   
			  Response response = client.newCall(request).execute();
			  if (!response.isSuccessful()) {
				  throw new BusinessException(ResponseCode.NETWORK_ERROR_511 , "调用接口发送网络异常") ;
			  }
			  JSONObject obj = JSONObject.parseObject(response.body().string()) ;
			  if(StringUtils.isNotBlank(obj.getString("media_id"))){
				  return obj.getString("media_id") ;
			  }
			  logger.error("upload permanent file , return:{}" , obj);
			  throw new BusinessException(ResponseCode.FAILUER_10 , "上传文件失败") ;
			    
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.FAILUER_10 , "上传文件发送异常") ;
		}
		
		
	}
	
}
	
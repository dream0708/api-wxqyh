package test.com.jee.wxqyh.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostFileTest {

	
	public static void main(String args[]) throws IOException{
	    MediaType 
		MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
		 
	    OkHttpClient client = new OkHttpClient();
		
	    
	   Request request = new Request.Builder()
       		.url("http://shp.qpic.cn/bizmp/9MDyejDrrzLsZ7aiayWyDxom0X7zvQhsj7uFGjhiaj4n0xSmQ98H0o0A/")
               
               .build();
	   Response response = client.newCall(request).execute() ;
	   
	   File file = new File("C:\\Users\\yaomengke\\Pictures\\to.jpg");
	   FileOutputStream fos = new FileOutputStream(file);

	  
	   IOUtils.write(response.body().bytes(),	 fos);
	    
	    /*
		    File file = new File("C:\\Users\\yaomengke\\Pictures\\config.txt");
		    String accessToken= "RW-NHzlLUNcyYg5RF0hAe-q91HswWbncPJk_P1zZ6ehi8erA7RNjBLNvpqW-2Jd4" ;
		 
		    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
	        RequestBody requestBody = new MultipartBody.Builder()
	                .setType(MultipartBody.FORM) 
	                .addFormDataPart("config", "config.txt", fileBody)
	                
	                .build();
	        Request request = new Request.Builder()
	        		.url(String.format(ConstantUtil.TEMP_UPLOAD_MEDIA_URL, accessToken , "file"))
	                .post(requestBody)
	                .build();
	        
		   
		    Response response = client.newCall(request).execute();
		    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		 
		    System.out.println(response.body().string());*/
	}
}

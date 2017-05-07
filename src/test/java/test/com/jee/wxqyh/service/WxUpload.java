package test.com.jee.wxqyh.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;

public class WxUpload {
	
	
	public static void main(String args[]){
		String accessToken= "RW-NHzlLUNcyYg5RF0hAe-q91HswWbncPJk_P1zZ6ehi8erA7RNjBLNvpqW-2Jd4" ;
		File file = new File("C:\\Users\\yaomengke\\Pictures\\config.txt");
		upload(accessToken , "file" , file) ;
		
		
	}
	

	private static final String upload_wechat_url = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";  
	
	     public static JSONObject upload(String accessToken, String type, File file) {  
	        JSONObject jsonObject = null;  
	        String last_wechat_url = upload_wechat_url.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);  
	        // 定义数据分割符  
	        String boundary = "----------sunlight";  
	        try {  
	            URL url = new URL(last_wechat_url);  
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	            // 发送POST请求必须设置如下两行  
	            conn.setDoOutput(true);  
	            conn.setDoInput(true);  
	            conn.setUseCaches(false);  
	            conn.setRequestMethod("POST");  
	            conn.setRequestProperty("connection", "Keep-Alive");  
	            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
	            conn.setRequestProperty("Charsert", "UTF-8");  
	            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);  
	  
	            OutputStream out = new DataOutputStream(conn.getOutputStream());  
	            byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();// 定义最后数据分隔线  
	            StringBuilder sb = new StringBuilder();  
	            sb.append("--");  
	            sb.append(boundary);  
	            sb.append("\r\n");  
	            sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");  
	            sb.append("Content-Type:application/octet-stream\r\n\r\n");  
	  
	            byte[] data = sb.toString().getBytes();  
	            out.write(data);  
	            DataInputStream in = new DataInputStream(new FileInputStream(file));  
	            int bytes = 0;  
	            byte[] bufferOut = new byte[1024 * 8];  
	            while ((bytes = in.read(bufferOut)) != -1) {  
	                out.write(bufferOut, 0, bytes);  
	            }  
	            out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个  
	            in.close();  
	            out.write(end_data);  
	            out.flush();  
	            out.close();  
	  
	            // 定义BufferedReader输入流来读取URL的响应  
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
	            String line = null;  
	            StringBuffer buffer = new StringBuffer();  
	            while ((line = reader.readLine()) != null) {  
	                buffer.append(line);  
	            }  
	            // 使用json解析  
	            jsonObject = JSONObject.parseObject(buffer.toString());  
	            System.out.println(jsonObject);  
	  
	        } catch (Exception e) {  
	            System.out.println("发送POST请求出现异常！" + e);  
	            e.printStackTrace();  
	        }  
	        return jsonObject;  
	    }  
	} 
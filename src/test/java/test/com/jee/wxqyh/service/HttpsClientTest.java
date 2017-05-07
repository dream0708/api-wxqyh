package test.com.jee.wxqyh.service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSON;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpsClientTest {
	
	
	private static OkHttpClient client = new OkHttpClient.Builder()
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
	
	public static void main(String args[]) throws IOException{
		
		
		
        
       
		Random random = new Random() ;
        for(int i= 0 ; i < 200 ; i ++){

    		RequestBody body = RequestBody.create(
           		   MediaType.parse("application/json; charset=utf-8"),
           		    JSON.toJSONString(new Object[]{ i * random.nextInt(10)}) ) ;
    		Request request = new Request.Builder()
                    .url("http://localhost:8080/tc-proxy/test?serviceId=2228&mFunc=10")
                    .post(body)
                    .build() ;
    		
    				client.newCall(request).enqueue(new Callback(){

						@Override
						public void onFailure(Call arg0, IOException arg1) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onResponse(Call arg0, Response arg1) throws IOException {
							System.out.println(arg1.body().string());
							
						}});
    		
    		//System.out.println(i*i +"==" + JSON.parseObject(result , String.class));
        }
		
	}

}
